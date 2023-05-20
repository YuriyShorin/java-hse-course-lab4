package hse.java.elevators.entity;

import java.util.Objects;

public class Ride {

    private Direction direction;
    private int start = 0;
    private int finish = 0;

    public Ride() {
    }

    public Ride(int start, int finish) {
        this.start = start;
        this.finish = finish;
        if (start > finish) {
            direction = Direction.DOWN;
        } else {
            direction = Direction.UP;
        }
    }

    public int getStart() {
        return start;
    }

    public int getFinish() {
        return finish;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ride ride = (Ride) o;
        return start == ride.start && finish == ride.finish && direction == ride.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, finish, direction);
    }
}