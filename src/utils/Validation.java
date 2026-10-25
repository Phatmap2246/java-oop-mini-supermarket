package utils;

import java.util.Scanner;

public class Validation {
    
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