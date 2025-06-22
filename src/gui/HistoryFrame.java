package gui;

import model.Database;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryFrame extends JFrame {

    public HistoryFrame(JFrame parent) {
        setTitle("Riwayat Booking Ruangan");
        setSize(800, 500);
        setLocationRelativeTo(parent); // Muncul di tengah parent frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Hanya menutup jendela ini

        // Kolom untuk tabel
        String[] columnNames = {"ID", "Ruangan", "Tanggal Booking", "Slot Waktu", "Dipesan Pada"};

        // Buat model tabel tapi belum diisi data
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Panggil data dari database
        List<Object[]> bookings = Database.getAllBookings();
        for (Object[] row : bookings) {
            model.addRow(row);
        }

        // Buat JScrollPane untuk menampung tabel
        JScrollPane scrollPane = new JScrollPane(table);

        // Tambahkan scrollPane ke frame
        add(scrollPane, BorderLayout.CENTER);
    }
}