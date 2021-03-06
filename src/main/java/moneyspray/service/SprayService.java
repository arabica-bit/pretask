package moneyspray.service;

import moneyspray.Util.TokenNameUtil;
import moneyspray.dao.SprayChild;
import moneyspray.dao.SprayTask;
import moneyspray.dto.SprayChildResponseDto;
import moneyspray.dto.SprayStatusResponseDto;
import moneyspray.exception.ServiceException;
import moneyspray.repository.SprayChildRepository;
import moneyspray.repository.SprayTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SprayService {
    private static Logger logger = LoggerFactory.getLogger(SprayService.class);

    private final SprayTaskRepository sprayTaskRepository;
    private final SprayChildRepository sprayChildRepository;

    public SprayService(SprayTaskRepository sprayTaskRepository, SprayChildRepository sprayChildRepository) {
        this.sprayTaskRepository = sprayTaskRepository;
        this.sprayChildRepository = sprayChildRepository;
    }

    @Transactional
    public String createSprayService (int money, int member, Long userId, String roomId) throws Exception {
        logger.info("createSprayService input1[{}], input2[{}], input3[{}], input4[{}]", money, member, userId, roomId);

        //생성 시간
        LocalDateTime currentDateTime = LocalDateTime.now();
        String created = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //Token 생성 + 중복 검사
        String newToken = new TokenNameUtil().getNewName();
        SprayTask sameSprayTask = sprayTaskRepository.findByTokenAndRoom(newToken, roomId);
        //혹시 Token이 중복되면 최대 10번까지 재생성 시도.
        for(int i = 0; i < 10; i++){
            if(sameSprayTask == null) break;
            newToken = new TokenNameUtil().getNewName();
            sameSprayTask = sprayTaskRepository.findByTokenAndRoom(newToken, roomId);
        }
        if(sameSprayTask != null){
            //생성한 토큰이 자꾸 중복됨.
            throw new ServiceException("뿌리기 생성중 문제가 발생했습니다.");
        }

        //새로운 뿌리기 객체 생성
        SprayTask sprayTask = new SprayTask(newToken, created, userId, roomId, member, money);
        sprayTaskRepository.save(sprayTask);

        //금액 분배
        Random rnd = new Random();
        int remain = money;
        for(int i = 0; i < member-1; i++) {
            int rMoney = rnd.nextInt(remain-member+i); //남은 멤버들이 최소1원씩은 갖도록 bound 설정.
            if(remain < (rMoney+member-i)) rMoney = 1;  //임의 생성한 금액이 총액보다 크면 1로 변경.
            if(rMoney == 0) rMoney = 1; //값이 0원이어도 1로 변경.

            SprayChild sprayChild = new SprayChild(sprayTask.getToken(), rMoney);
            sprayChildRepository.save(sprayChild);
            remain = remain-rMoney;
        }
        // (member-1)만큼만 루프 돌리고. 마지막 멤버가 잔액을 모두 갖도록 하여 분배 마무리.
        SprayChild sprayChild1 = new SprayChild(sprayTask.getToken(), remain);
        sprayChildRepository.save(sprayChild1);

        return sprayTask.getToken();
    }


    @Transactional
    public int receiveMoney (String token, Long userId, String roomId) throws Exception {
        logger.info("receiveMoney input1[{}], input2[{}], input3[{}]", token, userId, roomId);
        int amount = -1;

        SprayTask sprayTask = sprayTaskRepository.findByTokenAndRoom(token, roomId);
        if(sprayTask == null){
            //대화방에 해당하는 토큰이 없음.
            throw new ServiceException("존재하지 않는 뿌린기 정보입니다.");
        }

        //오너체크
        Long sprayOwner = sprayTask.getOwner();
        if(sprayOwner.compareTo(userId) == 0){
            //뿌리기를 만든 사용자는 받을 수 없음.
            throw new ServiceException("자신이 뿌리기한 건은 자신이 받을 수 없습니다.");
        }

        //시간 체크
        String createdStr = sprayTask.getCreated();
        LocalDateTime createdLocal = LocalDateTime.parse(createdStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //제한시간 = 뿌리기생성시간+10
        LocalDateTime compareTime = createdLocal.plusMinutes(10);
        LocalDateTime now = LocalDateTime.now();
        if(!now.isBefore(compareTime)){
            //현재 시간이 (생성시간+10분)보다 before가 아니므로 에러.
            throw new ServiceException("뿌리기는 10분간만 유효합니다.");
        }

        List<SprayChild> sprayChildList = sprayChildRepository.findAllByParent(token);
        for(SprayChild sprayChild : sprayChildList){
            if(sprayChild.getWho() != null && userId.equals(sprayChild.getWho())){
                //이미 이 뿌리기에서 돈을 한번 받은 상황.
                throw new ServiceException("뿌리기 당 한 사용자는 한번만 받을 수 있습니다.");
            }

            if(sprayChild.getWho() == null){
                //받은 사람이 없는 금액에 현재 사용자 할당
                amount =  sprayChild.getAmount();
                sprayChild.setWho(userId);
                sprayChildRepository.save(sprayChild);
                return amount;
            }

        }

        if(amount < 0){
            //모든 금액 분배 완료
            throw new ServiceException("뿌리기가 완료되었습니다.");
        }

        return -1;
    }


    @Transactional
    public SprayStatusResponseDto getSprayStatus (String token, Long userId, String roomId) throws Exception {
        logger.info("getSprayStatus input1[{}], input2[{}], input3[{}]", token, userId, roomId);

        SprayTask sprayTask = sprayTaskRepository.findByTokenAndRoom(token, roomId);
        if(sprayTask == null){
            //조회할 뿌리기 없음.
            throw new ServiceException("존재하지 않는 뿌리기 정보입니다.");
        }

        //오너 체크
        Long sprayOwner = sprayTask.getOwner();
        if(sprayOwner.compareTo(userId) != 0){
            //뿌리기를 만든 사용자만 조회할 수 있음.
            throw new ServiceException("해당 정보를 조회할 수 없습니다.");
        }

        //시간 체크
        String createdStr = sprayTask.getCreated();
        LocalDateTime createdLocal = LocalDateTime.parse(createdStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //제한시간 = 뿌리기생성시간+7일
        LocalDateTime compareTime = createdLocal.plusDays(7);
        LocalDateTime now = LocalDateTime.now();
        if(!now.isBefore(compareTime)){
            //현재 시간이 (생성시간+7일)보다 before가 아니므로 에러.
            throw new ServiceException("뿌리기 상태 조회는 7일 동안만 가능합니다.");
        }

        //조합
        SprayStatusResponseDto statusResponseDto = new SprayStatusResponseDto();
        statusResponseDto.setCreated(createdStr);
        statusResponseDto.setSeed(sprayTask.getSeed());
        List<SprayChild> sprayChildList = sprayChildRepository.findAllByParent(token);
        int sum = assembleStatusResponse(statusResponseDto, sprayChildList);
        statusResponseDto.setReceivedTotal(sum);

        return statusResponseDto;
    }


    private int assembleStatusResponse(final SprayStatusResponseDto statusResponseDto, final List<SprayChild> sprayChildList){
        List<SprayChildResponseDto> childResponseDtoList = new ArrayList<>();

        //받기 완료된 사용자를 결과 리스트에 추가하고, 완료된 금액을 계산.
        int sum = 0;
        for(SprayChild child : sprayChildList) {
            if(child.getWho() != null){
                SprayChildResponseDto childResponseDto = new SprayChildResponseDto(child.getAmount(), child.getWho());
                sum += child.getAmount();
                childResponseDtoList.add(childResponseDto);
            }
        }

        statusResponseDto.setReceivedList(childResponseDtoList);
        return sum;
    }

}


