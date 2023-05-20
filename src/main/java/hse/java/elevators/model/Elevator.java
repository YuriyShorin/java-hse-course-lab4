package hse.java.elevators.model;

import hse.java.elevators.entity.Direction;
import hse.java.elevators.entity.Ride;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Elevator implements Runnable {

    private final BlockingQueue<Ride> requests;
    private final TextArea log;
    private Ride ride = new Ride();
    private int floor = 0;
    private final int number;
    private boolean isStopped = true;

    public Elevator(BlockingQueue<Ride> requests, int number, TextArea log) {
        this.requests = requests;
        this.number = number;
        this.log = log;
    }

    @Override
    public void run() {
        try {
            log.appendText("Лифт №" + number + " направляется к заказчику на " + ride.getStart() + " этаже\n");
            performRide(ride.getStart(), ride.getStart() > floor ? 1 : -1, ride.getStart() > floor ? Direction.UP : Direction.DOWN);
            log.appendText("Лифт №" + number + " забрал заказчика на " + floor + " этаже\n");
            log.appendText("Лифт №" + number + " везет заказчика на " + ride.getFinish() + " этаж\n");

            performRide(ride.getFinish(), ride.getFinish() > floor ? 1 : -1, ride.getFinish() > floor ? Direction.UP : Direction.DOWN);
            log.appendText("Заказ исполнен. Лифт №" + number + ", этаж: " + floor + "\n");
            isStopped = true;
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.appendText("Error during ride\n");
        }
    }

    private void performRide(int goalFloor, int step, Direction direction) {
        try {
            List<Ride> companions = new ArrayList<>();
            List<Integer> companionsStart = new ArrayList<>();
            List<Integer> companionsFinish = new ArrayList<>();
            while (floor != goalFloor) {
                if (direction == Direction.DOWN) {
                    companions.addAll(requests.stream().filter(r -> r.getDirection() == direction && r.getStart() < ride.getStart() && r.getFinish() > ride.getFinish()).toList());
                } else {
                    companions.addAll(requests.stream().filter(r -> r.getDirection() == direction && r.getStart() > ride.getStart() && r.getFinish() < ride.getFinish()).toList());
                }
                companionsStart.addAll(companions.stream().map(Ride::getStart).toList());
                companionsFinish.addAll(companions.stream().map(Ride::getFinish).toList());
                requests.removeAll(companions);
                if (companionsFinish.contains(floor)) {
                    log.appendText("Лифт №" + number + " высадил на этаже: " + floor + "\n");
                    companionsFinish.removeAll(companionsFinish.stream().filter(f -> f == floor).toList());
                    companions.removeAll(companions.stream().filter(c -> c.getStart() == floor).toList());
                }
                if (companionsStart.contains(floor)) {
                    log.appendText("Лифт №" + number + " подсадил на этаже: " + floor + "\n");
                    companionsStart.removeAll(companionsStart.stream().filter(s -> s == floor).toList());
                }
                floor += step;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.appendText("Error during ride\n");
        }
    }

    public int getFloor() {
        return floor;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public boolean isStopped() {
        return isStopped;
    }
}