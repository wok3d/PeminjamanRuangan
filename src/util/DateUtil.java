package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String formatDisplayDate(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = inputFormat.parse(date);
            return outputFormat.format(dateObj);
        } catch (Exception e) {
            return date;
        }
    }

    /**
     * Memeriksa apakah tanggal dalam format "yyyy-MM-dd" berada di masa lalu.
     * @param dateStr Tanggal yang akan diperiksa.
     * @return true jika tanggal tersebut sebelum hari ini, false jika sebaliknya.
     */
    public static boolean isDateInPast(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // setLenient(false) memastikan format tanggal harus ketat.
            // Contohnya, tanggal "2024-02-30" akan dianggap error.
            sdf.setLenient(false);

            Date dateToBook = sdf.parse(dateStr);

            // Dapatkan tanggal hari ini tanpa komponen waktu (jam, menit, detik)
            // dengan cara memformat lalu mem-parsingnya kembali.
            Date today = sdf.parse(getTodayDate());

            // Kembalikan true jika tanggal booking sebelum hari ini
            return dateToBook.before(today);
        } catch (ParseException e) {
            // Jika format tanggal salah, anggap saja tidak valid untuk mencegah error.
            System.err.println("Format tanggal tidak valid: " + dateStr);
            return true;
        }
    }
}