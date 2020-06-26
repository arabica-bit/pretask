package moneyspray.dao;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SprayTask {

    public SprayTask() {}

    public SprayTask(final String token, final String created, final Long owner,
                     final String room, final int number, final int seed){
        this.token = token;
        this.created = created;
        this.owner = owner;
        this.room = room;
        this.number = number;
        this.seed = seed;
    }

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

}
