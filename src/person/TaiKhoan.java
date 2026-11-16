package person;

import interfaces.BanGhiFile;
import utils.DongDuLieu;
import utils.Validation;

import java.util.Objects;
import java.util.Scanner;

/**
 * Lớp đại diện cho tài khoản người dùng / nhân viên trong hệ thống siêu thị.
 * Tên đăng nhập được dùng trực tiếp bằng mã nhân viên (maNV) để loại bỏ dư thừa dữ liệu.
 * Vai trò của người dùng thuộc về thực thể Person/NhanVien; khi đăng nhập hệ thống sẽ
 * dựa vào mã nhân viên để xét vai trò trong Person trước, sau đó mới xét tới mật khẩu.
 */
public class TaiKhoan implements BanGhiFile {
    private String maNV;          // Mã nhân viên (đồng thời là khóa chính và tên đăng nhập, VD: NV001)
    private String matKhau;        // Mật khẩu
    private String vaiTro;         // Vai trò theo Person/NhanVien (QuanLy, QuanKho, ThuNgan, KeToan)
    private boolean trangThai;     // true: Hoạt động, false: Khóa

    public TaiKhoan() {
        this.maNV = "";
        this.matKhau = "";
        this.vaiTro = "";
        this.trangThai = true;
    }

    public TaiKhoan(String maNV, String matKhau) {
        this(maNV, matKhau, "", true);
    }

    public TaiKhoan(String maNV, String matKhau, boolean trangThai) {
        this(maNV, matKhau, "", trangThai);
    }

    public TaiKhoan(String maNV, String matKhau, String vaiTro) {
        this(maNV, matKhau, vaiTro, true);
    }

    public TaiKhoan(String maNV, String matKhau, String vaiTro, boolean trangThai) {
        this.maNV = maNV == null ? "" : maNV.trim();
        this.matKhau = matKhau == null ? "" : matKhau;
        this.vaiTro = vaiTro == null ? "" : vaiTro.trim();
        this.trangThai = trangThai;
    }

    public TaiKhoan(TaiKhoan tk) {
        if (tk == null) throw new IllegalArgumentException("Tài khoản sao chép không được null.");
        this.maNV = tk.maNV;
        this.matKhau = tk.matKhau;
        this.vaiTro = tk.vaiTro;
        this.trangThai = tk.trangThai;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV == null ? "" : maNV.trim();
    }

    /**
     * Tên đăng nhập dùng trực tiếp mã nhân viên.
     */
    public String getTenDangNhap() {
        return maNV;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau == null ? "" : matKhau;
    }

    /**
     * Vai trò tương ứng với nhân viên trong Person.
     */
    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro == null ? "" : vaiTro.trim();
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    /**
     * Kiểm tra mật khẩu cung cấp có trùng khớp với mật khẩu tài khoản hay không.
     */
    public boolean kiemTraMatKhau(String pass) {
        return this.matKhau != null && this.matKhau.equals(pass);
    }

    /**
     * Nhập thông tin tài khoản từ bàn phím.
     */
    public void nhapThongTin(Scanner sc) {
        System.out.println("\n=== NHẬP THÔNG TIN TÀI KHOẢN ===");
        this.maNV = Validation.inputStringRegex(sc, "Nhập mã nhân viên (tên đăng nhập - NVxxx): ", Validation.MaNV_REGEX, "Mã nhân viên không hợp lệ (NVxxx).");

        do {
            System.out.print("Nhập mật khẩu: ");
            this.matKhau = sc.nextLine();
            if (this.matKhau.isEmpty()) {
                System.out.println("Mật khẩu không được để trống!");
            }
        } while (this.matKhau.isEmpty());

        this.vaiTro = Validation.inputStringRegex(
                sc,
                "Nhập vai trò (QuanLy/QuanKho/ThuNgan/KeToan): ",
                "^(QuanLy|QuanKho|ThuNgan|KeToan)$",
                "Vai trò không hợp lệ."
        );
        this.trangThai = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaiKhoan taiKhoan = (TaiKhoan) o;
        return maNV != null && maNV.equalsIgnoreCase(taiKhoan.maNV);
    }

    @Override
    public int hashCode() {
        return maNV != null ? maNV.toUpperCase(java.util.Locale.ROOT).hashCode() : 0;
    }

    @Override
    public String toString() {
        return utils.HienThi.canLe("Mã NV (Tên đăng nhập): " + maNV
                + " | Vai trò: " + vaiTro
                + " | Trạng thái: " + (trangThai ? "Hoạt động" : "Đã khóa"));
    }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override
    public String toDongFile() {
        return DongDuLieu.ghep("TAIKHOAN", maNV, matKhau, vaiTro, trangThai);
    }

    public static TaiKhoan tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        if (p.length < 4 || !"TAIKHOAN".equals(p[0])) {
            throw new IllegalArgumentException("Dòng TAIKHOAN không hợp lệ: " + dong);
        }
        if (p.length == 4) {
            // TAIKHOAN;maNV;matKhau;trangThai
            return new TaiKhoan(p[1], p[2], "", DongDuLieu.logic(p[3]));
        } else if (p.length == 5) {
            // TAIKHOAN;maNV;matKhau;vaiTro;trangThai
            return new TaiKhoan(p[1], p[2], p[3], DongDuLieu.logic(p[4]));
        } else {
            // TAIKHOAN;maNV;tenDangNhap;matKhau;vaiTro;trangThai
            return new TaiKhoan(p[1], p[3], p[4], DongDuLieu.logic(p[5]));
        }
    }
}
