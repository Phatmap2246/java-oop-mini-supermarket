package utils;

import java.util.Scanner;

public class Validation {

    // Họ tên: Chấp nhận chữ cái (kể cả tiếng Việt), khoảng trắng, dấu nháy đơn, dấu gạch ngang
    public static final String HOTEN_REGEX = "^[\\p{L}\\s'-]+$";

    // Số điện thoại: Bắt đầu bằng 03, 05, 07, 08, 09 và theo sau là 8 chữ số
    public static final String PHONE_REGEX = "^(03|05|07|08|09)\\d{8}$";

    // Email: Định dạng chuẩn abc@domain.com
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

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
                sc.nextLine(); // RẤT QUAN TRỌNG: Xóa chữ "abc" vừa nhập để không bị lặp vô hạn
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
}