package moneyspray.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SprayChild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String parent;

    @Column
    private int amount;

    @Column
    private String who;
}
