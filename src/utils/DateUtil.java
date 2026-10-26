package utils;

import java.time.LocalDate;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class DateUtil {
    // Khai báo các bộ định dạng dùng chung cho cả dự án
    public static final DateTimeFormatter DATE_FORMATTER 
    = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter DATE_TIME_FORMATTER 
    = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm:ss").withResolverStyle(ResolverStyle.STRICT);

    public DateUtil() {
        // Chặn khởi tạo đối tượng vì đây là Utility Class
    }

    // Chuyển chuỗi (String) thành LocalDate
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
        } catch (DateTimeException e) {
            return null; // Trả về null nếu sai định dạng
        }
    }

    // Chuyển LocalDate thành chuỗi (String) để in ra màn hình hoặc ghi file
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DATE_FORMATTER);
    }

    // Kiểm tra tính hợp lệ của chuỗi ngày tháng
    public static boolean isValidDate(String dateStr) {
        return parseDate(dateStr) != null;

    }
}