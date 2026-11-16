import person.DSTaiKhoan;
import utils.KhoDuLieuTxt;
import java.nio.file.Files;

public class DSTaiKhoanRenameTest {
    public static void main(String[] args) throws Exception {
        DSTaiKhoan cu = KhoDuLieuTxt.doc(DSTaiKhoan.class, "src/luutru/hien-tai/NhanVien.txt");
        var file = Files.createTempFile("tai-khoan-", ".txt");
        cu.ghiFile(file.toString());
        DSTaiKhoan moi = KhoDuLieuTxt.doc(DSTaiKhoan.class, file.toString());
        if (cu.getDanhSachTaiKhoan().size() != moi.getDanhSachTaiKhoan().size())
            throw new AssertionError("Sai số lượng tài khoản");
        for (int i = 0; i < cu.getDanhSachTaiKhoan().size(); i++) {
            if (!cu.getDanhSachTaiKhoan().get(i).toDongFile().equals(moi.getDanhSachTaiKhoan().get(i).toDongFile()))
                throw new AssertionError("Thay đổi dữ liệu khi đổi tên lớp");
        }
        Files.delete(file);
        System.out.println("PASS: legacy group and DSTaiKhoan persistence round-trip");
    }
}
