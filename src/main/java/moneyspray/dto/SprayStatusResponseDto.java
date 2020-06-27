package moneyspray.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SprayStatusResponseDto {
    private String created;
    private int seed;
    private int receivedTotal;
    private List<SprayChildResponseDto> receivedList =  new ArrayList<>();

}
