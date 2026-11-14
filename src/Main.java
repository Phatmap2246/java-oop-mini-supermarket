import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        try {
            GiaoDien giaoDien = new GiaoDien();
            giaoDien.TrangChu(sc);
        } catch (NoSuchElementException e) {
            System.out.println("\n[!] Đã kết thúc luồng dữ liệu đầu vào.");
        } finally {
            sc.close();
        }
    }
}
