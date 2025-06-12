package controller;

import model.Database;

public class BookingController {
    public static boolean isSlotBooked(String room, String date, String slot) {
        return Database.checkBooking(room, date, slot);
    }

    public static boolean isAnySlotBooked(String room, String date) {
        return Database.checkAnyBooking(room, date);
    }

    public static boolean bookRoom(String room, String date, String slot) {
        if (isSlotBooked(room, date, slot)) {
            return false;
        }
        return Database.insertBooking(room, date, slot);
    }
}