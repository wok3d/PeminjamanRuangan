package main;

import gui.MainFrame;
import model.Database;

public class App {
    public static void main(String[] args) {
        Database.connect(); // Inisialisasi koneksi DB
        new MainFrame();
    }
}
