package moneyspray.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SprayChild {

    public SprayChild (final String parent, final int amount){
        this.parent = parent;
        this.amount = amount;
        this.who = null;
    }

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
