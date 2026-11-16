import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import hoadon.DanhSachCongNo;

public class CongNoLuuTruTest {
    public static void main(String[] args) throws Exception {
        Path source = Path.of("src/luutru/hien-tai");
        Path temp = Files.createTempDirectory("cong-no-regression-");
        Path current = Files.createDirectories(temp.resolve("hien-tai"));
        for (String name : utils.LuuTruNhieuFile.TEN_FILE)
            Files.copy(source.resolve(name), current.resolve(name));
        String before = Files.readString(current.resolve("CongNo.txt"), StandardCharsets.UTF_8);
        GiaoDien app = GiaoDien.docDuLieu(temp.toString());
        for (int i = 0; i < 3; i++) {
            app.ghiDuLieu(temp.toString());
            if (!Files.isRegularFile(current.resolve("CongNo.txt"))) throw new AssertionError("Missing CongNo.txt");
            app = GiaoDien.docDuLieu(temp.toString());
        }
        DanhSachCongNo old = new DanhSachCongNo();
        old.docFile(source.resolve("CongNo.txt").toString());
        DanhSachCongNo loaded = new DanhSachCongNo();
        loaded.docFile(current.resolve("CongNo.txt").toString());
        int count = 0;
        for (String line : before.split("\\R")) {
            if (!line.startsWith("CONGNO;")) continue;
            String ma = line.split(";")[1];
            if (loaded.timMa(ma) == null || !old.timMa(ma).toDongFile().equals(loaded.timMa(ma).toDongFile()))
                throw new AssertionError("Debt changed: " + ma);
            count++;
        }
        System.out.println("PASS: 3 save/reload cycles; preserved " + count + " debt records.");
    }
}
