package gui;

import controller.BookingController;
import util.DateUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class MainFrame extends JFrame {
    private JPanel roomPanel;
    private JPanel bookingPanel;
    private JButton selectedRoomButton;
    private JButton selectedTimeButton;
    private JDatePickerImpl datePicker;
    private String selectedRoom = null;
    private String selectedTime = null;
    private String selectedDate;

    public MainFrame() {
        initializeFrame();
        createMenuBar();
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
        selectedDate = DateUtil.getTodayDate();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opsi");
        JMenuItem historyMenuItem = new JMenuItem("Lihat Riwayat Booking");
        historyMenuItem.addActionListener(e -> new HistoryFrame(this).setVisible(true));
        menu.add(historyMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void createComponents() {
        roomPanel = createRoomPanel();
        bookingPanel = createBookingPanel();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        JPanel roomContainer = new JPanel(new BorderLayout());
        roomContainer.setBackground(Color.WHITE);
        roomContainer.add(roomPanel, BorderLayout.CENTER);
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
        JButton btn = new RoundedButton(roomName);
        btn.setPreferredSize(new Dimension(120, 120));
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true));
        updateRoomButtonColor(btn, roomName);
        btn.addActionListener(e -> {
            selectRoom(btn, roomName);
            refreshTimeSlots();
        });
        return btn;
    }

    private void updateRoomButtonColor(JButton btn, String roomName) {
        boolean isFullyBooked = BookingController.isRoomFullyBooked(roomName, selectedDate);
        if (isFullyBooked) {
            btn.setBackground(new Color(240, 240, 240));
            btn.setForeground(Color.RED);
        } else {
            btn.setBackground(new Color(240, 240, 240));
            btn.setForeground(new Color(34, 139, 34));
        }
    }

    private void selectRoom(JButton btn, String roomName) {
        if (selectedRoomButton != null) {
            updateRoomButtonColor(selectedRoomButton, selectedRoomButton.getText());
        }
        selectedRoomButton = btn;
        selectedRoom = roomName;
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.BLACK);
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(300, 600));
        JPanel datePanel = createDatePanel();
        panel.add(datePanel);
        panel.add(Box.createVerticalStrut(20));
        JPanel timePanelContainer = new JPanel(new BorderLayout());
        timePanelContainer.setBackground(Color.WHITE);
        timePanelContainer.add(createTimePanel(), BorderLayout.CENTER);
        panel.add(timePanelContainer);
        panel.add(Box.createVerticalStrut(20));
        JPanel actionPanel = createActionPanel();
        panel.add(actionPanel);
        return panel;
    }

    private JPanel createDatePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanelImpl = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanelImpl, new DateLabelFormatter());
        datePicker.addActionListener(e -> {
            Date selectedValue = (Date) datePicker.getModel().getValue();
            if (selectedValue != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                selectedDate = sdf.format(selectedValue);
                cancelSelection();
                refreshRoomStatus();
            }
        });
        panel.add(new JLabel("Pilih Tanggal: "));
        panel.add(datePicker);
        return panel;
    }

    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "dd/MM/yyyy";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
        @Override
        public Object stringToValue(String text) throws java.text.ParseException {
            return dateFormatter.parseObject(text);
        }
        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    private JPanel createTimePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Pilih Waktu"));
        String[] times = {"07:00 - 07:50", "07:50 - 08:40", "08:40 - 09:30", "09:30 - 10:20", "10:20 - 11:10", "11:10 - 12:00", "13:00 - 13:50", "13:50 - 14:40", "14:40 - 15:30", "15:30 - 16:20", "16:20 - 17:10", "17:10 - 18:00"};
        for (String time : times) {
            JButton timeBtn = createTimeButton(time);
            panel.add(timeBtn);
        }
        return panel;
    }

    private JButton createTimeButton(String time) {
        RoundedButton btn = new RoundedButton(time);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setHorizontalAlignment(SwingConstants.CENTER);

        // Cek apakah slot sudah dibooking
        boolean isBooked = selectedRoom != null && BookingController.isSlotBooked(selectedRoom, selectedDate, time);

        if (isBooked) {
            btn.setBackground(new Color(255, 255, 255)); // abu-abu gelap
            btn.setForeground(Color.WHITE);
            btn.setEnabled(false); // disable klik
        } else {
            btn.setBackground(new Color(200, 200, 200)); // abu-abu muda
            btn.setForeground(Color.BLACK);
        }

        btn.addActionListener(e -> {
            if (selectedTimeButton != null) {
                updateTimeButtonColor(selectedTimeButton, selectedTimeButton.getText());
            }
            selectedTimeButton = btn;
            selectedTime = time;
            btn.setBackground(new Color(161, 161, 161)); // selected grey
            btn.setForeground(Color.BLACK);
        });

        return btn;
    }


    private void updateTimeButtonColor(JButton btn, String time) {
        if (selectedRoom != null) {
            boolean isBooked = BookingController.isSlotBooked(selectedRoom, selectedDate, time);
            if (isBooked) {
                btn.setBackground(new Color(80, 80, 80)); // abu gelap
                btn.setForeground(Color.WHITE); // atau bisa juga Color.WHITE
                btn.setEnabled(false); // biar nggak bisa diklik
            } else {
                btn.setBackground(new Color(200, 200, 200));
                btn.setForeground(Color.BLACK);
                btn.setEnabled(true);
            }
        } else {
            btn.setBackground(new Color(200, 200, 200));
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
        bookBtn.setForeground(Color.BLACK);
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
                // Jika tombol adalah yang sedang dipilih, warnanya biru. Jika tidak, update warnanya.
                if (!btn.equals(selectedRoomButton)) {
                    updateRoomButtonColor(btn, roomName);
                }
            }
        }
    }

    private void refreshTimeSlots() {
        JPanel bookingSubPanel = (JPanel) bookingPanel.getComponent(2);
        Component[] components = ((JPanel) bookingSubPanel.getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                String time = btn.getText();
                // Selalu update warna semua tombol waktu
                updateTimeButtonColor(btn, time);
            }
        }
    }

    private void cancelSelection() {
        if (selectedRoomButton != null) {
            updateRoomButtonColor(selectedRoomButton, selectedRoomButton.getText());
            selectedRoomButton = null;
        }
        if (selectedTimeButton != null) {
            selectedTimeButton.setBackground(Color.WHITE);
            selectedTimeButton.setForeground(Color.BLACK);
            selectedTimeButton = null;
        }
        selectedRoom = null;
        selectedTime = null;
        refreshTimeSlots();
    }

    private void bookRoom() {
        if (DateUtil.isDateInPast(selectedDate)) {
            JOptionPane.showMessageDialog(this, "Anda tidak dapat melakukan booking untuk tanggal yang sudah lewat.", "Tanggal Tidak Valid", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedRoom == null || selectedTime == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih ruangan dan waktu terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean success = BookingController.bookRoom(selectedRoom, selectedDate, selectedTime);
        if (success) {
            JOptionPane.showMessageDialog(this, "Ruangan " + selectedRoom + " berhasil dibooking untuk tanggal " + formatDate(selectedDate) + " pada " + selectedTime + "!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            cancelSelection();
            refreshRoomStatus();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal melakukan booking. Ruangan mungkin sudah dibooking!", "Error", JOptionPane.ERROR_MESSAGE);
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