package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Scanner;

public class Validation {

    // --- KHO BIỂU THỨC CHÍNH QUY (REGEX) DÙNG CHUNG ---
    // Họ tên: Chấp nhận chữ cái (kể cả tiếng Việt), khoảng trắng, dấu nháy đơn, dấu gạch ngang
    public static final String HOTEN_REGEX = "^[\\p{L}\\s'-]+$";

    // Số điện thoại: Bắt đầu bằng 03, 05, 07, 08, 09 và theo sau là 8 chữ số
    public static final String PHONE_REGEX = "^(03|05|07|08|09)\\d{8}$";

    // Email: Định dạng chuẩn abc@domain.com
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    public static final String MaNV_REGEX = "^NV\\d{3}$";
    public static final String MaKH_REGEX = "^KH\\d{3,5}$";

    // ================= CÁC HÀM NHẬP SỐ =================

    // 1. Hàm nhập số nguyên (chống sập bằng try-catch)
    public static int inputInt(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                int value = sc.nextInt();
                sc.nextLine(); // Xóa bộ đệm sau khi nhập số
                return value;
            } catch (Exception e) {
                System.out.println("[!] Lỗi: Bạn phải nhập số nguyên. Vui lòng thử lại!");
                sc.nextLine(); // Xóa chữ "abc" vừa nhập để không bị lặp vô hạn
            }
        }
    }

    public static long inputLong(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                long value = sc.nextLong();
                sc.nextLine();
                return value;
            } catch (Exception e) {
                System.out.println("[!] Lỗi: Bạn phải nhập số nguyên. Vui lòng thử lại!");
                sc.nextLine();
            }
        }
    }

    // 2. Hàm nhập số thực (double) - Dùng cho Đơn giá, Tổng tiền
    public static double inputDouble(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                double value = sc.nextDouble();
                sc.nextLine();
                return value;
            } catch (Exception e) {
                System.out.println("[!] Lỗi: Bạn phải nhập số thực (VD: 10.5). Vui lòng thử lại!");
                sc.nextLine();
            }
        }
    }

    public static float inputFloat(Scanner sc, String message) {
        while (true) {
            try {
                System.out.print(message);
                float value = sc.nextFloat();
                sc.nextLine();
                return value;
            } catch (Exception e) {
                System.out.println("[!] Lỗi: Bạn phải nhập số thực (VD: 10.5). Vui lòng thử lại!");
                sc.nextLine();
            }
        }
    }

    // ================= CÁC HÀM NHẬP CHUỖI VÀ TIỆN ÍCH =================

    // 3. Hàm nhập chuỗi có kiểm tra Regex (Dùng cho Mã HĐ, SĐT, Tên)
    public static String inputStringRegex(Scanner sc, String message, String regex, String errorMessage) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();
            if (value.matches(regex)) {
                return value; // Đúng định dạng thì trả về
            }
            System.out.println("[!] " + errorMessage);
        }
    }
    
    // 4. Hàm tự động tạo mã (VD: NV001, KH001)
    public static String taoMa(String regex, int soTiepTheo) {
        return String.format(regex, soTiepTheo);
    }

    // 5. Hàm nhập ngày tháng (dd/MM/yyyy)
    public static LocalDate inputLocalDateRegex(Scanner sc, String message) {
        DateTimeFormatter khuon = DateTimeFormatter.ofPattern("dd/MM/yyyy").withResolverStyle(ResolverStyle.STRICT);
        while(true) {
            System.out.print(message);
            String date = sc.nextLine();
            if (!date.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$")) {
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

    // 6. Hàm nhập Có/Không (Dùng cho các câu hỏi xác nhận)
    public static boolean inputCoKhong(Scanner sc, String message) {
        while (true) {
            System.out.print(message + " (Có/Không): ");
            String value = sc.nextLine().trim();

            if (value.equalsIgnoreCase("có") || value.equalsIgnoreCase("co")) {
                return true;
            }
            if (value.equalsIgnoreCase("không") || value.equalsIgnoreCase("khong")) {
                return false;
            }
            System.out.println("[!] Vui lòng nhập Có hoặc Không!");
        }
    }

    // 7. Hàm nhập lựa chọn Menu (Giới hạn từ khoảng A đến B)
    public static int luaChon(Scanner sc, String thongBao, int khoangBatDau, int khoangKetThuc) {
        while (true) {
            System.out.print(thongBao);
            String value = sc.nextLine();
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
}