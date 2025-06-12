package model;

import java.sql.*;

public class Database {
    private static Connection conn;

    public static void connect() {
        try {
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/peminjaman_ruangan",
                "root", ""
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkBooking(String room, String date, String slot) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM bookings WHERE room_name=? AND date=? AND timeslot=?"
            );
            stmt.setString(1, room);
            stmt.setString(2, date);
            stmt.setString(3, slot);
            ResultSet rs = stmt.executeQuery();
            return rs.next();  // true if already booked
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}