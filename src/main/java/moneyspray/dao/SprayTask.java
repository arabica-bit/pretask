package moneyspray.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SprayTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String token;

    @Column
    private String created;

    @Column
    private Long owner;

    @Column
    private String room;

    @Column
    private int number;

    @Column
    private int seed;

    public SprayTask() {
    }
}
