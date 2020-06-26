package moneyspray.repository;

import moneyspray.dao.SprayTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprayTaskRepository extends JpaRepository<SprayTask,Long> {
    SprayTask findByToken(String token);

}
