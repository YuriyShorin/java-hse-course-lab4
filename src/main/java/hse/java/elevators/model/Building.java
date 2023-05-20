package hse.java.elevators.model;

import hse.java.elevators.entity.Ride;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Building extends Thread {

    private final BlockingQueue<Ride> requests;
    private final List<Elevator> elevators = new ArrayList<>();
    private final List<Thread> liftsThreads = new ArrayList<>();
    private boolean isStopped = false;

    public Building(BlockingQueue<Ride> request, int elevatorsNumber, TextArea log) {
        this.requests = request;
        for (int i = 0; i < elevatorsNumber; ++i) {
            elevators.add(new Elevator(requests, i + 1, log));
            liftsThreads.add(new Thread(elevators.get(i), "lift" + i + 1 + "thread"));
        }
    }

    @Override
    public void run() {
        try {
            while (!isStopped) {
                if (requests.isEmpty()) {
                    Thread.sleep(500);
                    continue;
                }
                if (!areAllLiftRunning()) {
                    Ride ride = requests.poll();
                    if (ride != null) {
                        int optimalIndex = getOptimalLiftIndex(ride);
                        Elevator optimalElevator = elevators.get(optimalIndex);
                        optimalElevator.setRide(ride);
                        optimalElevator.setStopped(false);
                        liftsThreads.set(optimalIndex, new Thread(optimalElevator, "lift " + (optimalIndex + 1) + " thread"));
                        liftsThreads.get(optimalIndex).start();
                    }
                }
            }
            for (Thread thread : liftsThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopWorking() {
        isStopped = true;
    }

    private int getOptimalLiftIndex(Ride ride) {
        int optimal = Integer.MAX_VALUE;
        int optimalIndex = 0;
        for (int i = 0; i < elevators.size(); ++i) {
            if (!elevators.get(i).isStopped()) {
                continue;
            }
            if (optimal > Math.abs(elevators.get(i).getFloor() - ride.getStart())) {
                optimal = Math.abs(elevators.get(i).getFloor() - ride.getStart());
                optimalIndex = i;
            }
        }
        return optimalIndex;
    }

    private boolean areAllLiftRunning() {
        for (Elevator elevator : elevators) {
            if (elevator.isStopped()) {
                return false;
            }
        }
        return true;
    }
}