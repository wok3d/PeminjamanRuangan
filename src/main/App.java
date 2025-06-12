package main;

import gui.MainFrame;
import model.Database;

public class App {
    public static void main(String[] args) {
        Database.connect();
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainFrame();
        });
    }
}