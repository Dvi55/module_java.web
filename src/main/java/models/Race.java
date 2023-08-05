package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Race {
        private int id;
        private String date;
        private int numberOfHorses;
        private int userHorseBet;
        private List<Horse> horses;

        public Race(int id, String date, int numberOfHorses, int userHorseBet) {
            this.id = id;
            this.date = date;
            this.numberOfHorses = numberOfHorses;
            this.horses = new ArrayList<>();
            this.userHorseBet = userHorseBet;
        }
    public void addHorse(Horse horse) {
        if (horses.size() < numberOfHorses) {
            horses.add(horse);
        }
    }

    public boolean isRaceCompleted() {
        for (Horse horse : horses) {
            if (horse.isRunning()) {
                return false;
            }
        }
        return true;
    }

}
