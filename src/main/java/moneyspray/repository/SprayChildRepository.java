package moneyspray.repository;

import moneyspray.dao.SprayChild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SprayChildRepository extends JpaRepository<SprayChild,Long> {
    List<SprayChild> findAllByParent(String parent);
}
