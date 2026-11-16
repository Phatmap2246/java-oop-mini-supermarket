import java.nio.file.*;
import utils.*;

/** Kiểm tra bộ TXT dạng dòng: lưu/mở lại giữ đủ dữ liệu. */
public class ChuyenDoiTxtTest {
    public static void main(String[] args) throws Exception {
        Path dich = Paths.get(args[1]);
        GiaoDien gd = GiaoDien.docDuLieu(args[0]);
        gd.ghiDuLieu(dich.toString());
        for (String ten : LuuTruNhieuFile.TEN_FILE) {
            String txt = Files.readString(dich.resolve("hien-tai").resolve(ten));
            if (!txt.startsWith(TepVanBan.TIEU_DE)) throw new AssertionError(ten);
        }
        int[] dem = BoDemMa.chup();
        GiaoDien moLai = GiaoDien.docDuLieu(dich.toString());
        if (!java.util.Arrays.equals(dem, BoDemMa.chup())) throw new AssertionError("Mở lại làm tăng bộ đếm");
        Path lanHai = dich.resolve("kiem-tra-lan-hai");
        moLai.ghiDuLieu(lanHai.toString());
        for (String ten : LuuTruNhieuFile.TEN_FILE) {
            String truoc = Files.readString(dich.resolve("hien-tai").resolve(ten));
            String sau = Files.readString(lanHai.resolve("hien-tai").resolve(ten));
            if (!truoc.equals(sau)) throw new AssertionError("Mất thuộc tính khi chuyển đổi: " + ten);
        }
        String value = "Tiếng Việt |, \\N";
        Path file = dich.resolve("escape.txt");
        DataBase.ghiDoiTuong(value, file.toString());
        if (!value.equals(DataBase.docDoiTuong(String.class, file.toString()))) {
            throw new AssertionError("Sai dữ liệu TXT đơn giản");
        }
        Files.writeString(file, TepVanBan.TIEU_DE + "\n#NHOM;Chuoi\nCHUOI;cột;thừa\n");
        try {
            DataBase.docDoiTuong(String.class, file.toString());
            throw new AssertionError("Phải báo thừa cột");
        } catch (java.io.IOException expected) { }
        System.out.println("PASS: TXT dạng dòng, lưu/mở lại giữ đủ dữ liệu và báo số cột lỗi");
    }
}
