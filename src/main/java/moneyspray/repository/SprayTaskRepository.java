package moneyspray.repository;

import moneyspray.dao.SprayTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprayTaskRepository extends JpaRepository<SprayTask,Long> {
    //대화방과 토큰의 조합으로 검색. 3자리면 너무 짧아서, 한 대화방에서만 고유함을 보장.
    SprayTask findByTokenAndRoom(String token, String room);

}
