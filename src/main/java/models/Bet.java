package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Bet {
        private int raceId;
        private int horseId;
}
