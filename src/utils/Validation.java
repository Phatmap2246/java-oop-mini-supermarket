package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Validation {

    public static final String HOTEN_REGEX = "^[\\p{L}\\s'-]+$";
    public static final String PHONE_REGEX = "^(03|05|07|08|09)\\d{8}$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static final String MaNV_REGEX = "(?i)^NV\\d{3}$";
    public static final String MaKH_REGEX = "(?i)^KH\\d{3,5}$";
    public static final String MaSP_REGEX = "(?i)^SP\\d{3,5}$";
    public static final String MaHDB_REGEX = "(?i)^HD(B)?\\d{3,5}$";
    public static final String MaHDN_REGEX = "(?i)^HD(N)?\\d{3,5}$";
    public static final String MaNCC_REGEX = "(?i)^NCC\\d+$";
    public static final String MaDT_REGEX = "(?i)^(NCC|DT)\\d+$";
    public static final String MaKM_REGEX = "(?i)^KM\\d+$";
    public static final String MaSoThue_REGEX = "^\\d{10}(-?\\d{3})?$";

    private static final String DATE_REGEX =
            "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$";
    private static final String DATE_TIME_REGEX =
            "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4} ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";

    public static int inputInt(Scanner sc, String message) {
        if (sc == null) return 0;
        while (true) {
            System.out.print(message);
            if (!sc.hasNextLine()) {
                return 0;
            }
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < 0) {
                    System.out.println("[!] Lỗi: Bạn phải nhập số nguyên lớn hơn hoặc bằng 0. Vui lòng thử lại!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[!] Lỗi: Bạn phải nhập số nguyên. Vui lòng thử lại!");
            }
        }
    }

    public static long inputLong(Scanner sc, String message) {
        if (sc == null) return 0L;
        while (true) {
            System.out.print(message);
            if (!sc.hasNextLine()) {
                return 0L;
            }
            String line = sc.nextLine().trim();
            try {
                long value = Long.parseLong(line);
                if (value < 0) {
                    System.out.println("[!] Lỗi: Bạn phải nhập số nguyên lớn hơn hoặc bằng 0. Vui lòng thử lại!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[!] Lỗi: Bạn phải nhập số nguyên. Vui lòng thử lại!");
            }
        }
    }

    public static double inputDouble(Scanner sc, String message) {
        if (sc == null) return 0.0;
        while (true) {
            System.out.print(message);
            if (!sc.hasNextLine()) {
                return 0.0;
            }
            String line = sc.nextLine().trim();
            try {
                double value = Double.parseDouble(line);
                if (!Double.isFinite(value) || value < 0) {
                    System.out.println("[!] Lỗi: Bạn phải nhập số thực lớn hơn hoặc bằng 0. Vui lòng thử lại!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[!] Lỗi: Bạn phải nhập số thực (VD: 10.5). Vui lòng thử lại!");
            }
        }
    }

    public static float inputFloat(Scanner sc, String message) {
        if (sc == null) return 0.0f;
        while (true) {
            System.out.print(message);
            if (!sc.hasNextLine()) {
                return 0.0f;
            }
            String line = sc.nextLine().trim();
            try {
                float value = Float.parseFloat(line);
                if (!Float.isFinite(value)) {
                    System.out.println("[!] Số thực phải hữu hạn. Vui lòng nhập lại!");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[!] Lỗi: Bạn phải nhập số thực (VD: 10.5). Vui lòng thử lại!");
            }
        }
    }

    public static String inputStringRegex(Scanner sc, String message, String regex, String errorMessage) {
        if (sc == null) throw new NoSuchElementException("Scanner is null");
        while (true) {
            System.out.print(message);
            if (!sc.hasNextLine()) {
                throw new NoSuchElementException("Hết dữ liệu đầu vào (EOF).");
            }
            String value = sc.nextLine().trim();
            if (value.matches(regex)) {
                return value;
            }
            System.out.println("[!] " + errorMessage);
        }
    }

    public static String inputStringRegex(Scanner sc, String message, String regex,
                                          String errorMessage, int soThuTuTiepTheo) {
        if (soThuTuTiepTheo <= 1) {
            throw new IllegalStateException("Chua co ma nao duoc cap cho loai doi tuong nay.");
        }
        while (true) {
            String value = inputStringRegex(sc, message, regex, errorMessage);
            String phanSo = value.replaceFirst("^[A-Za-z]+", "");
            if (phanSo.matches("[0-9]+")) {
                java.math.BigInteger soThuTu = new java.math.BigInteger(phanSo);
                if (soThuTu.signum() > 0 && soThuTu.compareTo(
                        java.math.BigInteger.valueOf(soThuTuTiepTheo)) < 0) {
                    return value;
                }
            }
            System.out.println("[!] Ma chua duoc cap. Vui long nhap lai!");
        }
    }

    public static String taoMa(String regex, int soTiepTheo) {
        return String.format(regex, soTiepTheo);
    }

    public static LocalDate inputLocalDateRegex(Scanner sc, String message) {
        if (sc == null) throw new NoSuchElementException("Scanner is null");
        DateTimeFormatter khuon = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
        while (true) {
            System.out.print(message);
            if (!sc.hasNextLine()) {
                throw new NoSuchElementException("Hết dữ liệu đầu vào (EOF).");
            }
            String date = sc.nextLine().trim();
            if (!date.matches(DATE_REGEX)) {
                System.out.println("[!] Bạn nhập sai định dạng (dd/MM/yyyy). Vui lòng nhập lại!");
                continue;
            }
            try {
                return LocalDate.parse(date, khuon);
            } catch (Exception e) {
                System.out.println("[!] Ngày nhập không tồn tại (VD: 30/02). Vui lòng nhập lại!");
            }
        }
    }

    public static LocalDateTime inputLocalDateTimeRegex(Scanner sc, String message) {
        if (sc == null) throw new NoSuchElementException("Scanner is null");
        while (true) {
            System.out.print(message);
            if (!sc.hasNextLine()) {
                throw new NoSuchElementException("Hết dữ liệu đầu vào (EOF).");
            }
            String dateTime = sc.nextLine().trim();
            if (!dateTime.matches(DATE_TIME_REGEX)) {
                System.out.println("[!] Bạn nhập sai định dạng (dd/MM/yyyy HH:mm:ss). Vui lòng nhập lại!");
                continue;
            }
            try {
                return LocalDateTime.parse(dateTime, DateUtil.DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("[!] Ngày giờ nhập không tồn tại (VD: 30/02). Vui lòng nhập lại!");
            }
        }
    }

    public static boolean inputCoKhong(Scanner sc, String message) {
        if (sc == null) return false;
        while (true) {
            System.out.print(message + " (Có/Không): ");
            if (!sc.hasNextLine()) {
                return false;
            }
            String value = sc.nextLine().trim();
            if (value.equalsIgnoreCase("có") || value.equalsIgnoreCase("co") || value.equalsIgnoreCase("1")) {
                return true;
            }
            if (value.equalsIgnoreCase("không") || value.equalsIgnoreCase("khong") || value.equalsIgnoreCase("0")) {
                return false;
            }
            System.out.println("[!] Vui lòng nhập Có hoặc Không!");
        }
    }

    public static int luaChon(Scanner sc, String thongBao, int khoangBatDau, int khoangKetThuc) {
        if (sc == null) return khoangBatDau;
        while (true) {
            System.out.print(thongBao);
            if (!sc.hasNextLine()) {
                return (khoangBatDau <= 0 && 0 <= khoangKetThuc) ? 0 : khoangBatDau;
            }
            String value = sc.nextLine().trim();
            try {
                int soNguyen = Integer.parseInt(value);
                if (soNguyen < khoangBatDau || soNguyen > khoangKetThuc) {
                    System.out.println("[!] Giá trị không hợp lệ. Vui lòng nhập từ " + khoangBatDau + " đến " + khoangKetThuc);
                    continue;
                }
                return soNguyen;
            } catch (NumberFormatException e) {
                System.out.println("[!] Giá trị nhập phải là số nguyên!");
            }
        }
    }

    public static void danghoatdong(long giay) {
        // Không gọi Thread.sleep để tránh đơ giao diện console
    }
}
