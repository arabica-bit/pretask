package moneyspray.service;

import moneyspray.Util.TokenNameUtil;
import moneyspray.dao.SprayChild;
import moneyspray.dao.SprayTask;
import moneyspray.repository.SprayChildRepository;
import moneyspray.repository.SprayTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class SprayService {
    private final SprayTaskRepository sprayTaskRepository;
    private final SprayChildRepository sprayChildRepository;

    public SprayService(SprayTaskRepository sprayTaskRepository, SprayChildRepository sprayChildRepository) {
        this.sprayTaskRepository = sprayTaskRepository;
        this.sprayChildRepository = sprayChildRepository;
    }

    @Transactional
    public String createSprayService (int money, int member, Long userId, String roomId) throws Exception {

        //생성 시간
        LocalDateTime currentDateTime = LocalDateTime.now();
        String created = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        //Token 생성
        String newToken = new TokenNameUtil().getNewName();
        SprayTask sameSprayTask = sprayTaskRepository.findByTokenAndRoom(newToken, roomId);
        //혹시 Token이 중복되면 최대 10번까지 재생성 시도.
        for(int i = 0; i < 10; i++){
            if(sameSprayTask == null) break;
            newToken = new TokenNameUtil().getNewName();
            sameSprayTask = sprayTaskRepository.findByTokenAndRoom(newToken, roomId);
        }

        //뿌리기 객체 생성
        SprayTask sprayTask = new SprayTask(newToken, created, userId, roomId, member, money);
        sprayTaskRepository.save(sprayTask);

        //금액 분배
        Random rnd = new Random();
        int remain = money;
        for(int i = 0; i < member-1; i++) {
            int rMoney = rnd.nextInt(remain-member+i); //남은 멤버들이 최소1원씩은 갖도록 bound 설정.
            if(remain < (rMoney+member-i)) rMoney = 1;  //임의 생성한 금액이 총액보다 크면 1로 변경.

            SprayChild sprayChild = new SprayChild(sprayTask.getToken(), rMoney);
            sprayChildRepository.save(sprayChild);

            remain = remain-rMoney;
        }
        // (member-1)만큼만 루프 돌리고. 마지막 멤버가 잔액을 모두 갖도록 하여 분배 마무리.
        SprayChild sprayChild1 = new SprayChild(sprayTask.getToken(), remain);
        sprayChildRepository.save(sprayChild1);

        return sprayTask.getToken();
    }


}

