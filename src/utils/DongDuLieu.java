package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.StringJoiner;

/** Một dòng = các cột ngăn bởi ;. Không dùng escape hoặc đối tượng lồng nhau. */
public final class DongDuLieu {
    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ROOT)
            .withResolverStyle(ResolverStyle.STRICT);

    private DongDuLieu() { }

    /** null ghi thành ô rỗng; không tự thay đổi nội dung không hợp lệ. */
    public static String chuoi(Object value) {
        if (value == null) return "";
        String text = value instanceof LocalDate ? NGAY.format((LocalDate) value) : value.toString();
        if (text.contains(";") || text.contains("\n") || text.contains("\r")) {
            throw new IllegalArgumentException("Nội dung ghi TXT không được chứa dấu ; hoặc xuống dòng: " + text);
        }
        return text;
    }

    public static String ghep(Object... cot) {
        StringJoiner dong = new StringJoiner(";");
        for (Object value : cot) dong.add(chuoi(value));
        return dong.toString();
    }

    public static LocalDate ngay(String text) {
        return text.isEmpty() ? null : LocalDate.parse(text, NGAY);
    }

    public static LocalDateTime thoiGian(String text) {
        return text.isEmpty() ? null : LocalDateTime.parse(text);
    }

    public static boolean logic(String text) {
        yeuCau(text.equals("true") || text.equals("false"), "Boolean phải là true hoặc false.");
        return Boolean.parseBoolean(text);
    }

    public static void yeuCau(boolean dung, String loi) {
        if (!dung) throw new IllegalArgumentException(loi);
    }

    public static void khongAm(double n, String ten) {
        yeuCau(Double.isFinite(n) && n >= 0, ten + " phải hữu hạn và không âm.");
    }

    public static void tyLe(double n, double max, String ten) {
        yeuCau(Double.isFinite(n) && n >= 0 && n <= max, ten + " phải nằm trong 0.." + max);
    }

    public static void thuTuNgay(LocalDate dau, LocalDate cuoi) {
        yeuCau(dau == null || cuoi == null || !cuoi.isBefore(dau), "Ngày kết thúc trước ngày bắt đầu.");
    }

    public static int maKeTiep(String ma, String regex) {
        yeuCau(ma != null && ma.matches(regex), "Mã không hợp lệ: " + ma);
        int n = Integer.parseInt(ma.replaceFirst("^[A-Za-z]+", ""));
        yeuCau(n > 0 && n < Integer.MAX_VALUE, "Mã vượt giới hạn: " + ma);
        return n + 1;
    }
}
