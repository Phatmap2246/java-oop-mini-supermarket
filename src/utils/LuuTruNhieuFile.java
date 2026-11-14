package utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

/** Ghi đủ các file của một phiên rồi mới thay bộ dữ liệu đang dùng. */
public class LuuTruNhieuFile {
    public static final String[] TEN_FILE = {
            "SanPham.txt", "NhanVien.txt", "KhachHang.txt", "DoiTac.txt",
            "KhuyenMai.txt", "SanPhamKhuyenMai.txt", "HoaDonBan.txt",
            "HoaDonNhap.txt", "CongNo.txt", "NhanVienDangNhap.txt"
    };

    public static Path thuMucHienTai(String thuMuc) throws IOException {
        Path goc = Paths.get(thuMuc).toAbsolutePath();
        Path hienTai = goc.resolve("hien-tai");
        Path truoc = goc.resolve("ban-truoc");
        // Khôi phục nếu lần lưu trước dừng giữa hai bước đổi tên thư mục.
        if (Files.notExists(hienTai) && Files.exists(truoc)) {
            doiTen(truoc, hienTai);
        }
        return hienTai;
    }

    public static synchronized void ghiDuLieu(String thuMuc, Map<String, Object> duLieu) throws IOException {
        for (String ten : TEN_FILE) {
            if (duLieu.get(ten) == null) throw new IOException("Thiếu dữ liệu để ghi " + ten);
        }
        Path hienTai = thuMucHienTai(thuMuc);
        Path goc = hienTai.getParent();
        Files.createDirectories(goc);
        Path truoc = goc.resolve("ban-truoc");
        Path tam = Files.createTempDirectory(goc, "dang-ghi-");
        try {
            for (String ten : TEN_FILE) {
                DataBase.ghiDoiTuong(duLieu.get(ten), tam.resolve(ten).toString());
            }
            // Chỉ thay dữ liệu hiện tại khi tất cả các file đều đã ghi xong.
            xoaBanLuu(truoc);
            if (Files.exists(hienTai)) doiTen(hienTai, truoc);
            try {
                doiTen(tam, hienTai);
            } catch (IOException e) {
                if (Files.notExists(hienTai) && Files.exists(truoc)) {
                    try {
                        doiTen(truoc, hienTai);
                    } catch (IOException loiKhoiPhuc) {
                        e.addSuppressed(loiKhoiPhuc);
                    }
                }
                throw e;
            }
        } finally {
            xoaBanLuu(tam);
        }
    }

    private static void doiTen(Path nguon, Path dich) throws IOException {
        try {
            Files.move(nguon, dich, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(nguon, dich);
        }
    }

    private static void xoaBanLuu(Path thuMuc) throws IOException {
        if (Files.notExists(thuMuc)) return;
        // Chỉ xóa các file dữ liệu biết trước, không xóa đệ quy nội dung khác.
        for (String ten : TEN_FILE) {
            Files.deleteIfExists(thuMuc.resolve(ten));
        }
        Files.delete(thuMuc);
    }
}
