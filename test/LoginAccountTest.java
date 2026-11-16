import person.*;
import java.nio.file.*;
import java.util.Scanner;

/** Run with a disposable sieuthi.dataDir. */
public class LoginAccountTest {
    public static void main(String[] args) throws Exception {
        System.setProperty("sieuthi.dataDir", Files.createTempDirectory("account-validation-").toString());
        DSTaiKhoan ds = new DSTaiKhoan();
        ds.docFile("src/luutru/hien-tai/NhanVien.txt");
        TaiKhoanNhanVien a = ds.getDanhSachTaiKhoan().get(0), b = ds.getDanhSachTaiKhoan().get(1);
        a.setTaiKhoan("AccountA"); a.setMatKhau("PassA");
        b.nhapTaiKhoan(new Scanner("ACCOUNTA\nAccountB\nPassB\n"), ds);
        if (!b.getTaiKhoan().equals("AccountB")) throw new AssertionError("duplicate creation");
        ds.sua(b.getMaNV(), new Scanner("10\naccounta\nAccountC\nPassC\n0\n"));
        if (!b.getTaiKhoan().equals("AccountC")) throw new AssertionError("duplicate edit");
        if (ds.timKiemTheoTaiKhoan("ACCOUNTc") != b) throw new AssertionError("identity");
        try {
            b.nhapTaiKhoan(new Scanner("Unfinished\n"), ds);
            throw new AssertionError("Expected EOF");
        } catch (java.util.NoSuchElementException expected) { }
        if (!b.getTaiKhoan().equals("AccountC") || !b.getMatKhau().equals("PassC"))
            throw new AssertionError("EOF changed account partially");
        b.setTaiKhoan("accounta");
        try {
            ds.ghiFile(Path.of(System.getProperty("sieuthi.dataDir"), "duplicate.txt").toString());
            throw new AssertionError("duplicate persistence");
        } catch (IllegalArgumentException expected) { }
        System.out.println("PASS: duplicate creation/edit/save and lookup identity");
    }
}
