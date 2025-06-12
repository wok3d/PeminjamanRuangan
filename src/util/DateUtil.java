package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
}
