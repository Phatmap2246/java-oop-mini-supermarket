package person;

import interfaces.IQuanLy;
import utils.TepVanBan;
import utils.Validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/**
 * Lớp quản lý danh sách tài khoản người dùng / nhân viên.
 * Tên đăng nhập được dùng trực tiếp bằng mã nhân viên (maNV) để loại bỏ dư thừa
 * dữ liệu.
 * Hiện thực interface IQuanLy<TaiKhoan> với đầy đủ chức năng thêm, xóa, sửa,
 * xuất và tìm kiếm.
 */
public class DSTaiKhoan implements IQuanLy<TaiKhoan> {
    private ArrayList<TaiKhoan> list;
    private transient DSNhanVien dsNhanVien;

    public DSTaiKhoan() {
        this.list = new ArrayList<>();
    }

    public DSTaiKhoan(ArrayList<TaiKhoan> list) {
        this.list = list == null ? new ArrayList<>() : list;
    }

    public ArrayList<TaiKhoan> getList() {
        return list;
    }

    public void setList(ArrayList<TaiKhoan> list) {
        this.list = list == null ? new ArrayList<>() : list;
    }

    public void cauHinh(DSNhanVien dsNhanVien) {
        this.dsNhanVien = dsNhanVien;
    }

    /**
     * Tự động đồng bộ tài khoản cho các nhân viên trong DSNhanVien nếu chưa có tài
     * khoản.
     * Tên đăng nhập trực tiếp là mã nhân viên.
     */
    public void dongBoVoiNhanVien() {
        if (dsNhanVien == null || dsNhanVien.getDanhSachNhanVien() == null)
            return;
        for (NhanVien nv : dsNhanVien.getDanhSachNhanVien()) {
            if (nv == null || nv.isSaThai() || nv.getMaNV() == null)
                continue;
            if (timTheoMaNV(nv.getMaNV()) == null) {
                String role = nv.getVaiTro() != null ? nv.getVaiTro() : "ThuNgan";
                String pass = nv.getMaNV().toLowerCase(Locale.ROOT) + "@123";
                list.add(new TaiKhoan(nv.getMaNV(), pass, role, true));
            }
        }
    }

    // CHỨC NĂNG CƠ BẢN (THÊM, XÓA, SỬA)

    /**
     * Thêm tài khoản qua Scanner: Tên đăng nhập trực tiếp là mã nhân viên.
     */
    @Override
    public void them(Scanner sc) {
        String maNV;
        String vaiTroMacDinh = null;
        while (true) {
            maNV = Validation.inputStringRegex(sc, "Nhập mã nhân viên (tên đăng nhập, VD: NVxxx): ",
                    Validation.MaNV_REGEX, "Mã NV không hợp lệ (NVxxx).");
            if (timTheoMaNV(maNV) != null) {
                System.out.println("[!] Mã nhân viên này đã có tài khoản. Vui lòng nhập mã khác!");
                continue;
            }
            if (dsNhanVien != null) {
                NhanVien nv = dsNhanVien.timMa(maNV);
                if (nv == null) {
                    System.out.println(
                            "[!] Lưu ý: Không tìm thấy nhân viên " + maNV + " trong danh sách nhân sự hiện tại.");
                    System.out.print(
                            "Bạn có chắc chắn muốn tiếp tục tạo tài khoản này? (1: Có, phím khác: Nhập lại mã): ");
                    String tiep = sc.nextLine().trim();
                    if (!"1".equals(tiep))
                        continue;
                } else if (nv.isSaThai()) {
                    System.out.println("[!] Nhân viên này đã bị sa thải. Không thể tạo tài khoản!");
                    return;
                } else {
                    vaiTroMacDinh = nv.getVaiTro();
                    System.out.println("-> Liên kết nhân viên: " + nv.getHoten() + " | Vai trò: " + nv.getVaiTro());
                }
            }
            break;
        }

        String matKhau;
        while (true) {
            System.out.print("Nhập mật khẩu: ");
            matKhau = sc.nextLine();
            if (matKhau.isEmpty()) {
                System.out.println("[!] Mật khẩu không được để trống!");
                continue;
            }
            break;
        }

        String vaiTro = vaiTroMacDinh;
        if (vaiTro == null || vaiTro.isEmpty()) {
            vaiTro = Validation.inputStringRegex(
                    sc,
                    "Nhập vai trò (QuanLy/QuanKho/ThuNgan/KeToan): ",
                    "^(QuanLy|QuanKho|ThuNgan|KeToan)$",
                    "Vai trò không hợp lệ.");
        }

        TaiKhoan tk = new TaiKhoan(maNV, matKhau, vaiTro, true);
        list.add(tk);
        System.out.println("[+] Thêm tài khoản thành công! Tên đăng nhập (Mã NV): " + maNV);
    }

    /**
     * Thêm tài khoản đối tượng trực tiếp.
     */
    public boolean them(TaiKhoan tk) {
        if (tk == null || tk.getMaNV() == null || tk.getMaNV().isBlank())
            return false;
        if (timTheoMaNV(tk.getMaNV()) != null) {
            return false;
        }
        return list.add(tk);
    }

    /**
     * Sửa tài khoản theo mã nhân viên qua Scanner.
     */
    @Override
    public boolean sua(String ma, Scanner sc) {
        TaiKhoan tk = timKiem(ma);
        if (tk == null) {
            System.out.println("[!] Không tìm thấy tài khoản cần sửa: " + ma);
            return false;
        }

        while (true) {
            System.out.println("\n=== SỬA TÀI KHOẢN (Mã NV: " + tk.getMaNV() + ") ===");
            System.out.println("1. Đổi mật khẩu");
            System.out.println("2. Đổi vai trò");
            System.out.println("3. Đổi trạng thái ("
                    + (tk.isTrangThai() ? "Đang hoạt động -> Khóa" : "Đang khóa -> Mở lại") + ")");
            System.out.println("0. Hoàn tất");
            int chon = Validation.luaChon(sc, "Chọn thông tin cần sửa (0-3): ", 0, 3);
            switch (chon) {
                case 1:
                    System.out.print("Nhập mật khẩu mới: ");
                    String newPass = sc.nextLine();
                    if (!newPass.isEmpty()) {
                        tk.setMatKhau(newPass);
                        System.out.println("[+] Đổi mật khẩu thành công!");
                    } else {
                        System.out.println("[!] Mật khẩu không được để trống!");
                    }
                    break;
                case 2:
                    String newRole = Validation.inputStringRegex(
                            sc,
                            "Nhập vai trò mới (QuanLy/QuanKho/ThuNgan/KeToan): ",
                            "^(QuanLy|QuanKho|ThuNgan|KeToan)$",
                            "Vai trò không hợp lệ.");
                    tk.setVaiTro(newRole);
                    if (dsNhanVien != null) {
                        NhanVien nv = dsNhanVien.timMa(tk.getMaNV());
                        if (nv != null) {
                            nv.setVaiTro(newRole);
                        }
                    }
                    System.out.println("[+] Đổi vai trò thành công!");
                    break;
                case 3:
                    tk.setTrangThai(!tk.isTrangThai());
                    System.out
                            .println("[+] Đã chuyển trạng thái sang: " + (tk.isTrangThai() ? "Hoạt động" : "Đã khóa"));
                    break;
                case 0:
                    return true;
            }
        }
    }

    /**
     * Sửa thông tin tài khoản trực tiếp qua mã nhân viên.
     */
    public boolean sua(String ma, String newMatKhau, String newVaiTro, Boolean newTrangThai) {
        TaiKhoan tk = timKiem(ma);
        if (tk == null)
            return false;
        if (newMatKhau != null && !newMatKhau.isEmpty()) {
            tk.setMatKhau(newMatKhau);
        }
        if (newVaiTro != null && !newVaiTro.isBlank()) {
            tk.setVaiTro(newVaiTro.trim());
            if (dsNhanVien != null) {
                NhanVien nv = dsNhanVien.timMa(tk.getMaNV());
                if (nv != null) {
                    nv.setVaiTro(newVaiTro.trim());
                }
            }
        }
        if (newTrangThai != null) {
            tk.setTrangThai(newTrangThai);
        }
        return true;
    }

    /**
     * Xóa tài khoản (vô hiệu hóa / khóa tài khoản theo cơ chế xóa mềm).
     */
    @Override
    public boolean xoa(String ma) {
        TaiKhoan tk = timKiem(ma);
        if (tk == null) {
            System.out.println("[!] Không tìm thấy tài khoản để vô hiệu hóa: " + ma);
            return false;
        }
        tk.setTrangThai(false);
        System.out.println("[+] Đã vô hiệu hóa (khóa) tài khoản mã NV: " + tk.getMaNV());
        return true;
    }

    /**
     * Xóa hoàn toàn tài khoản khỏi danh sách.
     */
    public boolean xoaHoanToan(String ma) {
        int idx = timViTri(ma);
        if (idx != -1) {
            list.remove(idx);
            return true;
        }
        return false;
    }

    @Override
    public void taoDanhSach(Scanner sc) {
        int soLuong = Validation.inputInt(sc, "Nhập số lượng tài khoản cần tạo: ");
        for (int i = 0; i < soLuong; i++) {
            System.out.printf("%n--- Nhập tài khoản %d/%d ---%n", i + 1, soLuong);
            them(sc);
        }
    }

    @Override
    public void xuatDanhSach() {
        if (list.isEmpty()) {
            System.out.println("Danh sách tài khoản trống.");
            return;
        }
        System.out.println("\n" + "=".repeat(75));
        System.out.printf("| %-25s | %-15s | %-15s | %-12s |%n", "Mã NV (Tên đăng nhập)", "Mật khẩu", "Vai trò",
                "Trạng thái");
        System.out.println("-".repeat(75));
        for (TaiKhoan tk : list) {
            System.out.printf("| %-25s | %-15s | %-15s | %-12s |%n",
                    tk.getMaNV(),
                    "******",
                    tk.getVaiTro(),
                    tk.isTrangThai() ? "Hoạt động" : "Đã khóa");
        }
        System.out.println("=".repeat(75));
    }

    // ================= TÌM KIẾM =================

    @Override
    public TaiKhoan timMa(String ma) {
        if (ma == null || ma.isBlank())
            return null;
        for (TaiKhoan tk : list) {
            if (tk.getMaNV().equalsIgnoreCase(ma.trim())) {
                return tk;
            }
        }
        return null;
    }

    @Override
    public TaiKhoan timTen(String ten) {
        return timMa(ten);
    }

    @Override
    public TaiKhoan timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank())
            return null;
        String k = khoa.toLowerCase(Locale.ROOT).trim();
        for (TaiKhoan tk : list) {
            if (tk.getMaNV().toLowerCase(Locale.ROOT).contains(k)
                    || tk.getVaiTro().toLowerCase(Locale.ROOT).contains(k)) {
                return tk;
            }
        }
        return null;
    }

    @Override
    public int timViTri(String ma) {
        if (ma == null || ma.isBlank())
            return -1;
        for (int i = 0; i < list.size(); i++) {
            TaiKhoan tk = list.get(i);
            if (tk.getMaNV().equalsIgnoreCase(ma.trim())) {
                return i;
            }
        }
        return -1;
    }

    public TaiKhoan timTheoMaNV(String maNV) {
        return timMa(maNV);
    }

    public TaiKhoan timTheoTenDangNhap(String tenDangNhap) {
        return timMa(tenDangNhap);
    }

    /**
     * Tìm kiếm linh hoạt theo mã nhân viên (đồng thời là tên đăng nhập) và alias
     * tương thích.
     */
    public TaiKhoan timKiem(String maHoacTen) {
        if (maHoacTen == null || maHoacTen.isBlank())
            return null;
        String s = maHoacTen.trim();
        for (TaiKhoan tk : list) {
            if (tk.getMaNV().equalsIgnoreCase(s)) {
                return tk;
            }
        }
        // Hỗ trợ alias cũ nếu người dùng nhập tên vai trò cũ
        if ("QuanLySieuThi".equalsIgnoreCase(s))
            return timTheoMaNV("NV001");
        if ("QuanKhoSieuThi".equalsIgnoreCase(s))
            return timTheoMaNV("NV002");
        if ("ThuNganSieuThi".equalsIgnoreCase(s))
            return timTheoMaNV("NV003");
        if ("KeToanSieuThi".equalsIgnoreCase(s))
            return timTheoMaNV("NV004");
        return null;
    }

    // ================= MENU QUẢN LÝ TÀI KHOẢN =================

    public void quanLyDanhSach(Scanner sc) {
        while (true) {
            System.out.println("\n=== QUẢN LÝ DANH SÁCH TÀI KHOẢN ===");
            System.out.println("1. Xem danh sách tài khoản");
            System.out.println("2. Thêm tài khoản mới");
            System.out.println("3. Sửa thông tin tài khoản");
            System.out.println("4. Khóa/Vô hiệu hóa tài khoản");
            System.out.println("5. Tìm kiếm tài khoản");
            System.out.println("6. Đồng bộ tài khoản từ danh sách nhân viên");
            System.out.println("0. Quay lại");
            int chon = Validation.luaChon(sc, "Chọn chức năng (0-6): ", 0, 6);
            switch (chon) {
                case 1:
                    xuatDanhSach();
                    break;
                case 2:
                    them(sc);
                    break;
                case 3:
                    System.out.print("Nhập mã nhân viên cần sửa: ");
                    String maSua = sc.nextLine().trim();
                    sua(maSua, sc);
                    break;
                case 4:
                    System.out.print("Nhập mã nhân viên cần khóa: ");
                    String maXoa = sc.nextLine().trim();
                    xoa(maXoa);
                    break;
                case 5:
                    System.out.print("Nhập từ khóa tìm kiếm (mã NV / vai trò): ");
                    String tuKhoa = sc.nextLine().trim();
                    TaiKhoan ketQua = timKhoa(tuKhoa);
                    if (ketQua != null) {
                        System.out.println("-> Tìm thấy: " + ketQua);
                    } else {
                        System.out.println("[!] Không tìm thấy tài khoản phù hợp.");
                    }
                    break;
                case 6:
                    dongBoVoiNhanVien();
                    System.out.println("[+] Đã đồng bộ tài khoản từ danh sách nhân viên.");
                    break;
                case 0:
                    return;
            }
        }
    }

    // ================= ĐỌC / GHI FILE TXT =================

    public void docFile(String duongDan) throws IOException {
        List<String> cacDong = TepVanBan.doc(duongDan);
        ArrayList<TaiKhoan> danhSachMoi = new ArrayList<>();
        Set<String> maDaDoc = new HashSet<>();
        for (String dong : cacDong) {
            if (!dong.startsWith("TAIKHOAN;"))
                continue;
            try {
                TaiKhoan taiKhoan = TaiKhoan.tuDongFile(dong);
                if (taiKhoan.getMaNV().isBlank() || taiKhoan.getMatKhau().isEmpty()
                        || taiKhoan.getVaiTro().isBlank()) {
                    throw new IllegalArgumentException("Thiếu mã nhân viên, mật khẩu hoặc vai trò.");
                }
                if (!maDaDoc.add(taiKhoan.getMaNV().toUpperCase(Locale.ROOT))) {
                    throw new IllegalArgumentException("Trùng mã nhân viên: " + taiKhoan.getMaNV());
                }
                danhSachMoi.add(taiKhoan);
            } catch (RuntimeException e) {
                throw new IOException("Không đọc được tài khoản từ " + duongDan + ": " + e.getMessage(), e);
            }
        }
        this.list = danhSachMoi;
    }

    public void ghiFile(String duongDan) throws IOException {
        List<String> cacDong = new ArrayList<>();
        for (TaiKhoan tk : list) {
            cacDong.add(tk.toDongFile());
        }
        TepVanBan.ghi(duongDan, "DSTaiKhoan", cacDong);
    }
}
