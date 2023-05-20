package hse.java.elevators.model;

import hse.java.elevators.entity.Ride;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class RidesGenerator extends Thread {

    private final BlockingQueue<Ride> requests;
    private final int minFloor;
    private final int maxFloor;
    private final int generationFrequency;
    private boolean isStopped = false;

    public RidesGenerator(BlockingQueue<Ride> requests, int minFloor, int maxFloor, int generationFrequency) {
        this.requests = requests;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.generationFrequency = generationFrequency;
    }

    @Override
    public void run() {
        while (!isStopped) {
            Random random = new Random();
            requests.add(new Ride(random.nextInt(minFloor, maxFloor), random.nextInt(minFloor, maxFloor)));
            try {
                sleep(generationFrequency);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopGeneration() {
        isStopped = true;
    }
}