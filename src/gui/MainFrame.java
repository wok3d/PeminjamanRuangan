package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel roomPanel;
    private JPanel bookingPanel;

    public MainFrame() {
        setTitle("Sistem Peminjaman Ruangan");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        roomPanel = createRoomPanel();
        bookingPanel = createBookingPanel();

        add(roomPanel);   // 70%
        add(bookingPanel); // 30%

        setVisible(true);
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));
        String[] rooms = {"201", "202", "203", "204", "301", "302", "303", "304", "401", "402", "403", "404"};
        for (String room : rooms) {
            JButton btn = new JButton(room);
            btn.setForeground(Color.GREEN); // Default: tersedia
            panel.add(btn);
        }
        return panel;
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton dateBtn = new JButton("Pilih Tanggal");
        panel.add(dateBtn);

        String[] times = {
            "07:00 - 07:50", "07:50 - 08:40",
            "08:40 - 09:30", "09:30 - 10:20",
            "10:20 - 11:10", "11:10 - 12:00",
            "13:00 - 13:50", "13:50 - 14:40",
            "14:40 - 15:30", "15:30 - 16:20",
            "16:20 - 17:10", "17:10 - 18:00"
        };

        for (String t : times) {
            JButton timeBtn = new JButton(t);
            panel.add(timeBtn);
        }

        panel.add(new JButton("Cancel"));
        panel.add(new JButton("Book"));
        return panel;
    }
}
