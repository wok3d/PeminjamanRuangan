package model;

import java.sql.*;

public class Database {
    private static Connection conn;

    public static void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/peminjaman_ruangan?useSSL=false&serverTimezone=UTC",
                    "root", ""
            );
            System.out.println("Koneksi ke database berhasil.");
            createTableIfNotExists();
        } catch (Exception e) {
            System.out.println("Koneksi ke database gagal.");
            e.printStackTrace();
        }
    }


    private static void createTableIfNotExists() {
        try {
            Statement stmt = conn.createStatement();
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS bookings (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    room_name VARCHAR(10) NOT NULL,
                    date DATE NOT NULL,
                    timeslot VARCHAR(20) NOT NULL,
                    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    UNIQUE KEY unique_booking (room_name, date, timeslot)
                )
            """;
            stmt.executeUpdate(createTableSQL);
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
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkAnyBooking(String room, String date) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM bookings WHERE room_name=? AND date=?"
            );
            stmt.setString(1, room);
            stmt.setString(2, date);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertBooking(String room, String date, String slot) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO bookings (room_name, date, timeslot) VALUES (?, ?, ?)"
            );
            stmt.setString(1, room);
            stmt.setString(2, date);
            stmt.setString(3, slot);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}