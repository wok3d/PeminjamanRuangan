package gui;

import controller.BookingController;
import util.DateUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFrame extends JFrame {
    private JPanel roomPanel;
    private JPanel bookingPanel;
    private JButton selectedRoomButton;
    private JButton selectedTimeButton;
    private JLabel dateLabel;
    private String selectedRoom = null;
    private String selectedTime = null;
    private String selectedDate;

    public MainFrame() {
        initializeFrame();
        createComponents();
        setupLayout();
        refreshRoomStatus();
    }

    private void initializeFrame() {
        setTitle("Sistem Peminjaman Ruangan");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);

        // Set current date
        selectedDate = DateUtil.getTodayDate();
    }

    private void createComponents() {
        roomPanel = createRoomPanel();
        bookingPanel = createBookingPanel();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Room panel container
        JPanel roomContainer = new JPanel(new BorderLayout());
        roomContainer.setBackground(Color.WHITE);
        roomContainer.add(roomPanel, BorderLayout.CENTER);

        // Booking panel container
        JPanel bookingContainer = new JPanel(new BorderLayout());
        bookingContainer.setBackground(Color.WHITE);
        bookingContainer.setBorder(new EmptyBorder(0, 20, 0, 0));
        bookingContainer.add(bookingPanel, BorderLayout.CENTER);

        mainPanel.add(roomContainer, BorderLayout.CENTER);
        mainPanel.add(bookingContainer, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] rooms = {"404", "403", "402", "401", "304", "303", "302", "301", "204", "203", "202", "201"};

        for (String room : rooms) {
            JButton btn = createRoomButton(room);
            panel.add(btn);
        }

        return panel;
    }

    private JButton createRoomButton(String roomName) {
        JButton btn = new JButton(roomName);
        btn.setPreferredSize(new Dimension(120, 120));
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));

        // Set initial color based on availability
        updateRoomButtonColor(btn, roomName);

        btn.addActionListener(e -> {
            selectRoom(btn, roomName);
            refreshTimeSlots();
        });

        return btn;
    }

    private void updateRoomButtonColor(JButton btn, String roomName) {
        boolean isBooked = BookingController.isAnySlotBooked(roomName, selectedDate);
        if (isBooked) {
            btn.setBackground(new Color(240, 240, 240));
            btn.setForeground(Color.RED);
        } else {
            btn.setBackground(new Color(240, 240, 240));
            btn.setForeground(new Color(34, 139, 34));
        }
    }

    private void selectRoom(JButton btn, String roomName) {
        // Reset previous selection
        if (selectedRoomButton != null) {
            updateRoomButtonColor(selectedRoomButton, selectedRoomButton.getText());
        }

        // Set new selection
        selectedRoomButton = btn;
        selectedRoom = roomName;
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 600));

        // Date section
        JPanel datePanel = createDatePanel();
        panel.add(datePanel);
        panel.add(Box.createVerticalStrut(20));

        // Time slots section
        JPanel timePanel = createTimePanel();
        panel.add(timePanel);
        panel.add(Box.createVerticalStrut(20));

        // Action buttons
        JPanel actionPanel = createActionPanel();
        panel.add(actionPanel);

        return panel;
    }

    private JPanel createDatePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);

        dateLabel = new JLabel();
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dateLabel.setText(formatDate(selectedDate));
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dateLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 20, 10, 20)
        ));
        dateLabel.setBackground(Color.WHITE);
        dateLabel.setOpaque(true);

        // Add calendar icon
        JLabel calendarIcon = new JLabel("📅");
        calendarIcon.setFont(new Font("Arial", Font.PLAIN, 20));

        panel.add(dateLabel);
        panel.add(calendarIcon);

        return panel;
    }

    private JPanel createTimePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBackground(Color.WHITE);

        String[] times = {
                "07:00 - 07:50", "07:50 - 08:40",
                "08:40 - 09:30", "09:30 - 10:20",
                "10:20 - 11:10", "11:10 - 12:00",
                "13:00 - 13:50", "13:50 - 14:40",
                "14:40 - 15:30", "15:30 - 16:20",
                "16:20 - 17:10", "17:10 - 18:00"
        };

        for (String time : times) {
            JButton timeBtn = createTimeButton(time);
            panel.add(timeBtn);
        }

        return panel;
    }

    private JButton createTimeButton(String time) {
        JButton btn = new JButton(time);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setFont(new Font("Arial", Font.PLAIN, 11));
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

        // Set initial state
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);

        btn.addActionListener(e -> selectTimeSlot(btn, time));

        return btn;
    }

    private void selectTimeSlot(JButton btn, String time) {
        // Reset previous selection
        if (selectedTimeButton != null) {
            updateTimeButtonColor(selectedTimeButton, selectedTimeButton.getText());
        }

        // Set new selection
        selectedTimeButton = btn;
        selectedTime = time;
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
    }

    private void updateTimeButtonColor(JButton btn, String time) {
        if (selectedRoom != null) {
            boolean isBooked = BookingController.isSlotBooked(selectedRoom, selectedDate, time);
            if (isBooked) {
                btn.setBackground(new Color(128, 128, 128));
                btn.setForeground(Color.WHITE);
                btn.setEnabled(false);
            } else {
                btn.setBackground(Color.WHITE);
                btn.setForeground(Color.BLACK);
                btn.setEnabled(true);
            }
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setEnabled(true);
        }
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBackground(Color.WHITE);

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.setPreferredSize(new Dimension(120, 40));
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setBackground(Color.WHITE);
        cancelBtn.setForeground(Color.BLACK);
        cancelBtn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
        cancelBtn.setFocusPainted(false);

        JButton bookBtn = new JButton("BOOK");
        bookBtn.setPreferredSize(new Dimension(120, 40));
        bookBtn.setFont(new Font("Arial", Font.BOLD, 12));
        bookBtn.setBackground(new Color(70, 130, 180));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1, true));
        bookBtn.setFocusPainted(false);

        cancelBtn.addActionListener(e -> cancelSelection());
        bookBtn.addActionListener(e -> bookRoom());

        panel.add(cancelBtn);
        panel.add(bookBtn);

        return panel;
    }

    private void refreshRoomStatus() {
        Component[] components = roomPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String roomName = btn.getText();
                if (!btn.equals(selectedRoomButton)) {
                    updateRoomButtonColor(btn, roomName);
                }
            }
        }
    }

    private void refreshTimeSlots() {
        if (selectedRoom == null) return;

        Component[] components = ((JPanel) bookingPanel.getComponent(2)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String time = btn.getText();
                if (!btn.equals(selectedTimeButton)) {
                    updateTimeButtonColor(btn, time);
                }
            }
        }
    }

    private void cancelSelection() {
        selectedRoom = null;
        selectedTime = null;

        if (selectedRoomButton != null) {
            updateRoomButtonColor(selectedRoomButton, selectedRoomButton.getText());
            selectedRoomButton = null;
        }

        if (selectedTimeButton != null) {
            updateTimeButtonColor(selectedTimeButton, selectedTimeButton.getText());
            selectedTimeButton = null;
        }

        refreshTimeSlots();
    }

    private void bookRoom() {
        if (selectedRoom == null || selectedTime == null) {
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih ruangan dan waktu terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = BookingController.bookRoom(selectedRoom, selectedDate, selectedTime);
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Ruangan " + selectedRoom + " berhasil dibooking untuk " + selectedTime + "!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            cancelSelection();
            refreshRoomStatus();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Gagal melakukan booking. Ruangan mungkin sudah dibooking!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatDate(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = inputFormat.parse(date);
            return outputFormat.format(dateObj);
        } catch (Exception e) {
            return date;
        }
    }
}
