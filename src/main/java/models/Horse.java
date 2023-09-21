package models;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Data
public class Horse implements Runnable {
    private static int counter = 1;
    private final int id;
    private final String name;
    private int distanceCovered;
    private static int hasFinished = 0;
    private int place = 1;


    public Horse(String name) {
        this.name = name;
        this.id = counter++;
        this.distanceCovered = 0;
    }

    public void run() {

        while (distanceCovered < 1000) {
            int distanceRun = ThreadLocalRandom.current().nextInt(100, 201);
            distanceCovered += distanceRun;
            System.out.println("Horse " + id + " ran " + distanceRun + " meters (" + distanceCovered + "/1000)");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(100, 401));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        place = ++hasFinished;
        System.out.println(name + " has finished the race at" + place + " place.");
    }
}
