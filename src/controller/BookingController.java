package controller;

import model.Database;

public class BookingController {
    public static boolean isSlotBooked(String room, String date, String slot) {
        return Database.checkBooking(room, date, slot);
    }
}