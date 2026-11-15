import java.util.Scanner;
import java.io.IOException;

public class Main {
    public static void main(String[] args)
    {
        GiaoDien giaoDien;
        try {
            giaoDien = GiaoDien.docDuLieu(GiaoDien.THU_MUC_DU_LIEU);
        } catch (IOException e) {
            System.err.println("[!] Không thể mở dữ liệu: " + e.getMessage());
            System.err.println("Giữ nguyên dữ liệu cũ. Hãy kiểm tra thư mục " + GiaoDien.THU_MUC_DU_LIEU);
            return;
        }
        Thread luuKhiTat = new Thread(giaoDien::luuDuLieu, "luu-du-lieu");
        Runtime.getRuntime().addShutdownHook(luuKhiTat);
        try (Scanner sc = new Scanner(System.in)) {
            giaoDien.TrangChu(sc);
        } finally {
            // Khi JVM đang tắt, hook sẽ đảm nhiệm việc lưu.
            try {
                Runtime.getRuntime().removeShutdownHook(luuKhiTat);
                giaoDien.luuDuLieu();
            } catch (IllegalStateException e) {
                // JVM đã bắt đầu chạy shutdown hook.
            }
        }
    }
}
