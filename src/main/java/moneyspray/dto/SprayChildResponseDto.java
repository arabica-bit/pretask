package moneyspray.dto;

import lombok.Data;

@Data
public class SprayChildResponseDto {
    private int amount;
    private Long who;


    public SprayChildResponseDto(int amount, Long who) {
        this.amount = amount;
        this.who = who;
    }
}
