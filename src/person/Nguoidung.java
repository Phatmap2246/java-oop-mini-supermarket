package person;

import java.time.LocalDate;
import java.util.Scanner;

/** Thông tin đăng nhập dùng chung; thông tin cá nhân kế thừa từ Person. */
public abstract class Nguoidung extends Person {
    protected String taiKhoan = "";
    protected String matKhau = "";

    public Nguoidung() { super(); }
    public Nguoidung(String email, String ten, String sdt, DiaChi dc, LocalDate ngaySinh) {
        super(email, ten, sdt, dc, ngaySinh);
    }
    public Nguoidung(Nguoidung nguon) {
        super(nguon);
        taiKhoan = nguon.taiKhoan;
        matKhau = nguon.matKhau;
    }
    public String getTaiKhoan() { return taiKhoan; }
    public String getMatKhau() { return matKhau; }
    public void setTaiKhoan(String value) {
        if (value == null || value.isBlank() || value.equals("0") || value.contains(";")
                || value.chars().anyMatch(Character::isWhitespace))
            throw new IllegalArgumentException("Tài khoản không được rỗng, chứa khoảng trắng hoặc dấu ;.");
        taiKhoan = value.trim();
    }
    public void setMatKhau(String value) {
        if (value == null || value.trim().length() < 4 || value.contains(";")
                || value.contains("\n") || value.contains("\r"))
            throw new IllegalArgumentException("Mật khẩu cần ít nhất 4 ký tự, không chứa ; hoặc xuống dòng.");
        matKhau = value.trim();
    }
    public void nhapTaiKhoan(Scanner sc, DSTaiKhoan ds) {
        String taiKhoanCu = taiKhoan;
        String matKhauCu = matKhau;
        boolean hoanTat = false;
        try {
        while (true) {
            System.out.print("Tên đăng nhập: ");
            String value = sc.nextLine();
            try {
                TaiKhoanNhanVien trung = ds == null ? null : ds.timKiemTheoTaiKhoan(value);
                if (trung != null && trung != this)
                    throw new IllegalArgumentException("Tên đăng nhập đã được sử dụng.");
                setTaiKhoan(value);
                break;
            } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }
        while (true) {
            System.out.print("Mật khẩu (ít nhất 4 ký tự): ");
            try { setMatKhau(sc.nextLine()); break; }
            catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }
        hoanTat = true;
        } finally {
            if (!hoanTat) {
                taiKhoan = taiKhoanCu;
                matKhau = matKhauCu;
            }
        }
    }
    public abstract boolean isDaXoa();
    public abstract String getVaiTro();
}
