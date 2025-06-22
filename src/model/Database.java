package model;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        try (Statement stmt = conn.createStatement()) {
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
        String sql = "SELECT * FROM bookings WHERE room_name=? AND date=? AND timeslot=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room);
            stmt.setString(2, date);
            stmt.setString(3, slot);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkAnyBooking(String room, String date) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE room_name=? AND date=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room);
            stmt.setString(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int countBookingsForRoomOnDate(String room, String date) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE room_name = ? AND date = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room);
            stmt.setString(2, date);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean insertBooking(String room, String date, String slot) {
        String sql = "INSERT INTO bookings (room_name, date, timeslot) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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

    public static boolean deleteBooking(String room, String date, String timeslot) {
        String sql = "DELETE FROM bookings WHERE room_name = ? AND date = ? AND timeslot = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room);
            stmt.setString(2, date);
            stmt.setString(3, timeslot);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Object[]> getAllBookings() {
        List<Object[]> bookings = new ArrayList<>();
        String sql = "SELECT id, room_name, date, timeslot, booked_at FROM bookings ORDER BY date DESC, room_name, timeslot";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = new Object[5];

                // Ambil ID, ruangan, dan waktu
                row[0] = rs.getInt("id");
                row[1] = rs.getString("room_name");

                // Format tanggal booking
                Date date = rs.getDate("date");
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                row[2] = sdf.format(date);

                // Slot waktu
                row[3] = rs.getString("timeslot");

                // Format booked_at
                Timestamp timestamp = rs.getTimestamp("booked_at");
                if (timestamp != null) {
                    LocalDateTime bookedTime = timestamp.toLocalDateTime();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    row[4] = bookedTime.format(formatter);
                } else {
                    row[4] = "-";
                }

                bookings.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}