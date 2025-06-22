package controller;

import model.Database;

public class BookingController {

    private static final int TOTAL_TIME_SLOTS = 12;

    public static boolean isSlotBooked(String room, String date, String slot) {
        return Database.checkBooking(room, date, slot);
    }

    public static boolean isAnySlotBooked(String room, String date) {
        return Database.checkAnyBooking(room, date);
    }

    public static boolean isRoomFullyBooked(String room, String date) {
        int bookedCount = Database.countBookingsForRoomOnDate(room, date);
        return bookedCount >= TOTAL_TIME_SLOTS;
    }

    public static boolean bookRoom(String room, String date, String slot) {
        if (isSlotBooked(room, date, slot)) {
            return false;
        }
        return Database.insertBooking(room, date, slot);
    }

    public static boolean cancelBooking(String room, String date, String timeslot) {
        return Database.deleteBooking(room, date, timeslot);
    }
}