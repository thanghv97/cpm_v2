package tcbs.com.cpm.util;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DateTimeUtils {

  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  private DateTimeUtils() {
  }

  public static String dateToString(Date date, String pattern) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat formatter = new SimpleDateFormat(pattern);
    return formatter.format(date);
  }


  public static String instantDateToString(Instant date, String pattern) {
    if (date == null) {
      return null;
    }
    return dateToString(Date.from(date), pattern);
  }
}
