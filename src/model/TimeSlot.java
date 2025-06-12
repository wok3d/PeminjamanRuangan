package model;

public class TimeSlot {
    private String timeRange;
    private boolean isBooked;

    public TimeSlot(String timeRange) {
        this.timeRange = timeRange;
        this.isBooked = false;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}