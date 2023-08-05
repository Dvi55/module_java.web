package models;

import lombok.Data;

@Data
public class Horse {
    private int id;
    private int raceDistance;
    private int currentPosition;
    private boolean isRunning;
    private Thread raceThread;

    public Horse(int id) {
        this.id = id;
        this.raceDistance = 1000;
        this.currentPosition = 0;
        this.isRunning = false;
    }

    public void startRace() {
        isRunning = true;
        raceThread = new Thread(new RaceRunnable());
        raceThread.start();
    }

    private class RaceRunnable implements Runnable {
        @Override
        public void run() {
            simulateRace();
        }
    }

    private void simulateRace() {
        try {
            while (currentPosition < raceDistance) {
                int distanceCovered = (int) (Math.random() * 100) + 100;
                currentPosition += distanceCovered;
                System.out.println(getId() + " " + currentPosition);
                int sleepTime = (int) (Math.random() * 100) + 400;
                Thread.sleep(sleepTime);
            }

            isRunning = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
