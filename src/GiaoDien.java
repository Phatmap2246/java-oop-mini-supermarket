import person.*;
import sanpham.QuanLySanPham;
import hoadon.DanhSachHoaDonBan;
import hoadon.DanhSachHoaDonNhap;
import hoadon.DanhSachCongNo;
import doitac.Danhsachdoitac;
import dichvu.Danhsachkhuyenmai;
import dichvu.DanhSachSPKM;
import utils.Validation;
import utils.LuuTruNhieuFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import doitac.Nhacungcap;
import doitac.Doitac;
import sanpham.SanPham;

import java.util.Scanner;

public class GiaoDien {
    public static final String THU_MUC_DU_LIEU = utils.DuongDanLuuTru.thuMuc().toString();

    private void lienKetVaiTro(NhanVien nv) {
        if (nv instanceof QuanLy) {
            QuanLy ql = (QuanLy) nv;
            ql.setDSNhanVien(dsNhanVien);
            ql.setDSKhuyenMai(dsKhuyenMai);
            ql.setDanhSachSPKM(dsSanPhamKhuyenMai);
            ql.setDSTaiKhoan(dsTaiKhoan);
        }
        if (nv instanceof QuanKho) {
            ((QuanKho) nv).setQlsp(dsSanPham);
            ((QuanKho) nv).setDanhSachHoaDonNhap(dsHoaDonNhap);
        }
        if (nv instanceof ThuNgan) ((ThuNgan) nv).setDanhSachHoaDonBan(dsHoaDonBan);
    }

    private void lienKetDuLieu() {
        dsSanPham.cauHinh(dsDoiTac);
        dsSanPhamKhuyenMai.cauHinh(dsSanPham, dsKhuyenMai);
        dsHoaDonBan.cauHinh(dsSanPhamKhuyenMai, dsKhuyenMai, dsSanPham, dsKhachHang, thuNgan.getMaNV());
        dsHoaDonNhap.cauHinh(quanKho.getMaNV(), dsSanPham, dsDoiTac);
        dsCongNo.cauHinh(dsHoaDonNhap, dsDoiTac);
        dsTaiKhoan.cauHinh(dsNhanVien);
        lienKetVaiTro(quanLy);
        lienKetVaiTro(quanKho);
        lienKetVaiTro(thuNgan);
        for (NhanVien nv : dsNhanVien.getDanhSachNhanVien()) lienKetVaiTro(nv);
        Map<String, SanPham> sanPhams = new LinkedHashMap<>();
        for (SanPham sp : dsSanPham.getDanhSachSanPham()) {
            if (sp != null) sanPhams.put(sp.getMaSanPham().toUpperCase(java.util.Locale.ROOT), sp);
        }
        for (Doitac dt : dsDoiTac.getDanhSachDoiTac()) {
            if (!(dt instanceof Nhacungcap)) continue;
            Nhacungcap ncc = (Nhacungcap) dt;
            ArrayList<SanPham> ds = new ArrayList<>();
            for (SanPham cu : ncc.getSanPhamCungCap().getDanhSachSanPham()) {
                if (cu == null) continue;
                SanPham sp = sanPhams.get(cu.getMaSanPham().toUpperCase(java.util.Locale.ROOT));
                // Danh mục chính là nguồn chuẩn; bỏ liên kết tới sản phẩm đã bị xóa hẳn.
                if (sp != null) ds.add(sp);
            }
            ncc.getSanPhamCungCap().setDanhSachSanPham(ds);
        }
    }


    private QuanLySanPham dsSanPham = new QuanLySanPham();
    private DSNhanVien dsNhanVien = new DSNhanVien();
    private DSKhachHang dsKhachHang = new DSKhachHang();
    private Danhsachdoitac dsDoiTac = new Danhsachdoitac();
    private Danhsachkhuyenmai dsKhuyenMai = new Danhsachkhuyenmai();
    private DanhSachSPKM dsSanPhamKhuyenMai = new DanhSachSPKM();
    private DanhSachHoaDonBan dsHoaDonBan = new DanhSachHoaDonBan();
    private DanhSachHoaDonNhap dsHoaDonNhap = new DanhSachHoaDonNhap();
    private DanhSachCongNo dsCongNo = new DanhSachCongNo();
    private QuanLy quanLy = new QuanLy();
    private QuanKho quanKho = new QuanKho(dsSanPham, dsHoaDonNhap);
    private ThuNgan thuNgan = new ThuNgan();
    private KeToan keToan = new KeToan();

    private DSTaiKhoan dsTaiKhoan = new DSTaiKhoan();

    public GiaoDien()
    {
        lienKetDuLieu();
    }

    public DSTaiKhoan getDsTaiKhoan() {
        return dsTaiKhoan;
    }

    public void setDsTaiKhoan(DSTaiKhoan dsTaiKhoan) {
        if (dsTaiKhoan != null) {
            this.dsTaiKhoan = dsTaiKhoan;
            this.dsTaiKhoan.cauHinh(dsNhanVien);
        }
    }
    public void TrangChu(Scanner sc )
    {
        dsSanPhamKhuyenMai.tuDongGiamGiaCanHSD(dsSanPham, dsKhuyenMai);
        while (true)
        {
            System.out.println("==========================TRANG CHỦ========================");
            System.out.println("Bạn muốn đăng nhập vào nhân viên nào!");
            System.out.println("1. Đăng nhập dưới vai trò nhân viên thu ngân.");
            System.out.println("2. Đăng nhập dưới vai trò nhân viên quản lý kho.");
            System.out.println("3. Đăng nhập dưới vai trò nhân viên quản lý cửa hàng.");
            System.out.println("4. Đăng nhập dưới vai trò nhân viên kế toán.");
            System.out.println("0. Thoát chương trình.");
            int luaChon = Validation.luaChon(sc , "Nhập lựa chọn của bạn: " , 0 , 4 );
            try {
                switch (luaChon)
                {
                    case 1:
                        MenuThuNgan(sc);
                        break;
                    case 2:
                        MenuQuanLyKho(sc);
                        break;
                    case 3:
                        MenuQuanLy(sc);
                        break;
                    case 4:
                        MenuKeToan(sc);
                        break;
                    case 0:
                        System.out.println("Chương trình kết thúc, tạm biệt!");
                        return;
                    default:
                        break;
                }
            } catch (IllegalStateException e) {
                System.out.println("[!] " + e.getMessage());
            }
            // Lưu thay đổi sau mỗi lần trở về từ một menu nghiệp vụ.
            luuDuLieu();
        }
    }
    /**
     * Xác thực đăng nhập cho từng vai trò nghiệp vụ:
     * - Luôn đọc cặp tài khoản và mật khẩu theo định dạng giao diện chuẩn để tránh lệch luồng Scanner.
     * - Xét vai trò của người đó theo mã nhân viên trước (sử dụng biến vaitro trong Person/NhanVien).
     * - Chỉ khi vai trò hợp lệ mới xét tới mật khẩu.
     */
    public boolean xacThucDangNhap(Scanner sc, String vaiTroYeuCau, String tenVaiTroHienThi) {
        System.out.println("=================ĐĂNG NHẬP " + tenVaiTroHienThi.toUpperCase(java.util.Locale.ROOT) + "=================");
        System.out.println("Vui lòng nhập thông tin đăng nhập!");
        int soLanNhapSai = 5;

        while (soLanNhapSai > 0) {
            System.out.println("Nhâp tài khoản: ");
            String taiKhoan = sc.nextLine().trim();
            System.out.println("Nhập mật khẩu: ");
            String matKhau = sc.nextLine();

            if (taiKhoan.isEmpty()) {
                soLanNhapSai--;
                if (soLanNhapSai == 0) break;
                System.out.println("Bạn đã nhập sai mật khẩu hoặc tài khoản vui lòng nhập lại!(Còn " + soLanNhapSai + " lần thử)");
                continue;
            }

            TaiKhoan tk = dsTaiKhoan.timKiem(taiKhoan);
            if (tk == null) {
                soLanNhapSai--;
                if (soLanNhapSai == 0) break;
                System.out.println("Bạn đã nhập sai mật khẩu hoặc tài khoản vui lòng nhập lại!(Còn " + soLanNhapSai + " lần thử)");
                continue;
            }

            if (!tk.isTrangThai()) {
                System.out.println("[!] Tài khoản này đã bị khóa hoặc ngừng hoạt động!");
                return false;
            }

            // 1. Xét tới vai trò của người đó theo mã nhân viên (dùng biến vaitro trong Person/NhanVien)
            String maNV = tk.getMaNV();
            NhanVien nv = dsNhanVien.timMa(maNV);
            if (nv == null && dsNhanVien.getDanhSachNhanVien() != null) {
                for (NhanVien e : dsNhanVien.getDanhSachNhanVien()) {
                    if (e != null && maNV.equalsIgnoreCase(e.getMaNV())) {
                        nv = e;
                        break;
                    }
                }
            }

            if (nv != null && nv.isSaThai()) {
                System.out.println("[!] Nhân viên " + maNV + " đã bị sa thải, không thể đăng nhập!");
                return false;
            }

            // Lấy vai trò của người đó từ Person (nv.getVaiTro())
            String vaiTroNguoiDo = (nv != null && nv.getVaiTro() != null) ? nv.getVaiTro() : tk.getVaiTro();

            // XÉT VAI TRÒ TRƯỚC: Nếu vai trò không khớp thì từ chối ngay, tuyệt đối không xét tới mật khẩu
            if (!vaiTroYeuCau.equalsIgnoreCase(vaiTroNguoiDo)) {
                soLanNhapSai--;
                System.out.println("Từ chối quyền truy cập: Mã nhân viên " + maNV + " có vai trò là [" + vaiTroNguoiDo
                        + "] không được phép đăng nhập vào vai trò [" + tenVaiTroHienThi + "]!");
                if (soLanNhapSai == 0) break;
                System.out.println("Bạn đã nhập sai mật khẩu hoặc tài khoản vui lòng nhập lại!(Còn " + soLanNhapSai + " lần thử)");
                continue;
            }

            // 2. Đúng vai trò rồi mới xét tới mật khẩu
            if (!tk.kiemTraMatKhau(matKhau)) {
                soLanNhapSai--;
                if (soLanNhapSai == 0) break;
                System.out.println("Bạn đã nhập sai mật khẩu hoặc tài khoản vui lòng nhập lại!(Còn " + soLanNhapSai + " lần thử)");
                continue;
            }

            // Đăng nhập thành công -> liên kết với đối tượng nhân viên đang phiên
            if (nv != null) {
                if ("QuanKho".equalsIgnoreCase(vaiTroYeuCau) && nv instanceof QuanKho) this.quanKho = (QuanKho) nv;
                if ("KeToan".equalsIgnoreCase(vaiTroYeuCau) && nv instanceof KeToan) this.keToan = (KeToan) nv;
                if ("QuanLy".equalsIgnoreCase(vaiTroYeuCau) && nv instanceof QuanLy) this.quanLy = (QuanLy) nv;
                if ("ThuNgan".equalsIgnoreCase(vaiTroYeuCau) && nv instanceof ThuNgan) this.thuNgan = (ThuNgan) nv;
            }
            return true;
        }

        System.out.println("Bạn đã nhập sai quá 5 lần do đó bạn phải chờ thời gian nhập lại!");
        return false;
    }

    public void MenuQuanLyKho(Scanner sc) {
        if (!xacThucDangNhap(sc, "QuanKho", "QUẢN LÝ KHO")) return;
        quanKho.thucHienNghiepVu(sc, dsDoiTac);
    }

    public void MenuKeToan(Scanner sc) {
        if (!xacThucDangNhap(sc, "KeToan", "KẾ TOÁN")) return;
        keToan.thucHienNghiepVu(sc, dsHoaDonBan, dsHoaDonNhap, dsDoiTac, dsCongNo);
    }

    public void MenuQuanLy(Scanner sc) {
        if (!xacThucDangNhap(sc, "QuanLy", "QUẢN LÝ")) return;
        quanLy.thucHienNghiepVu(sc, dsSanPham, dsDoiTac, dsHoaDonNhap, dsHoaDonBan, dsKhachHang);
    }

    public void MenuThuNgan(Scanner sc) {
        if (!xacThucDangNhap(sc, "ThuNgan", "THU NGÂN")) return;
        dsHoaDonBan.cauHinh(dsSanPhamKhuyenMai, dsKhuyenMai, dsSanPham, dsKhachHang, thuNgan.getMaNV());
        thuNgan.thucHienNghiepVu(sc, dsSanPham, dsSanPhamKhuyenMai, dsKhuyenMai, dsKhachHang);
    }
    /** Nạp khi mở, lưu khi thoát menu hoặc kết thúc nhập liệu. */

    // ================= ĐỌC / GHI FILE TXT =================

    public static synchronized GiaoDien docDuLieu(String thuMuc) throws IOException {
        int[] truoc = utils.BoDemMa.chup();
        boolean thanhCong = false;
        try {
            Files.createDirectories(Paths.get(thuMuc));
            Path hienTai = LuuTruNhieuFile.thuMucHienTai(thuMuc);
            GiaoDien gd;
            if (Files.exists(hienTai)) {
                gd = new GiaoDien();
                // Bốn vai trò mặc định sẽ được thay bằng dữ liệu file, không cấp mã mới.
                utils.BoDemMa.khoiPhuc(truoc);
                gd.dsSanPham.docFile(duongDanFile(hienTai, "SanPham.txt"));
                gd.dsNhanVien.docFile(duongDanFile(hienTai, "NhanVien.txt"));
                gd.dsKhachHang.docFile(duongDanFile(hienTai, "KhachHang.txt"));
                gd.dsDoiTac.docFile(duongDanFile(hienTai, "DoiTac.txt"));
                gd.dsKhuyenMai.docFile(duongDanFile(hienTai, "KhuyenMai.txt"));
                gd.dsSanPhamKhuyenMai.docFile(duongDanFile(hienTai, "SanPhamKhuyenMai.txt"));
                gd.dsHoaDonBan.docFile(duongDanFile(hienTai, "HoaDonBan.txt"));
                gd.dsHoaDonNhap.docFile(duongDanFile(hienTai, "HoaDonNhap.txt"));
                gd.dsCongNo.docFile(duongDanFile(hienTai, "CongNo.txt"));
                String fileDangNhap = duongDanFile(hienTai, "NhanVienDangNhap.txt");
                NhanVien[] vaiTro = utils.KhoDuLieuTxt.docVaiTro(fileDangNhap);
                gd.dsTaiKhoan.docFile(fileDangNhap);
                Path fileTaiKhoanCu = hienTai.resolve("DSTaiKhoan.txt");
                if (gd.dsTaiKhoan.getList().isEmpty() && Files.isRegularFile(fileTaiKhoanCu)) {
                    gd.dsTaiKhoan.docFile(fileTaiKhoanCu.toString());
                }
                if (vaiTro.length != 4 || !(vaiTro[0] instanceof QuanLy)
                        || !(vaiTro[1] instanceof QuanKho) || !(vaiTro[2] instanceof ThuNgan)
                        || !(vaiTro[3] instanceof KeToan)) {
                    throw new IOException("File nhân viên đăng nhập không hợp lệ.");
                }
                gd.quanLy = (QuanLy) vaiTro[0];
                gd.quanKho = (QuanKho) vaiTro[1];
                gd.thuNgan = (ThuNgan) vaiTro[2];
                gd.keToan = (KeToan) vaiTro[3];
            } else {
                gd = new GiaoDien();
            }
            gd.lienKetDuLieu();
            thanhCong = true;
            return gd;
        } finally {
            if (!thanhCong) utils.BoDemMa.khoiPhuc(truoc);
        }
    }

    private static String duongDanFile(Path thuMuc, String tenFile) throws IOException {
        Path file = thuMuc.resolve(tenFile);
        if (!Files.isRegularFile(file)) throw new IOException("Thiếu file dữ liệu: " + tenFile);
        return file.toString();
    }

    public synchronized void ghiDuLieu(String thuMuc) throws IOException {
        Map<String, Object> duLieu = new LinkedHashMap<>();
        duLieu.put("SanPham.txt", dsSanPham);
        duLieu.put("NhanVien.txt", dsNhanVien);
        duLieu.put("KhachHang.txt", dsKhachHang);
        duLieu.put("DoiTac.txt", dsDoiTac);
        duLieu.put("KhuyenMai.txt", dsKhuyenMai);
        duLieu.put("SanPhamKhuyenMai.txt", dsSanPhamKhuyenMai);
        duLieu.put("HoaDonBan.txt", dsHoaDonBan);
        duLieu.put("HoaDonNhap.txt", dsHoaDonNhap);
        duLieu.put("CongNo.txt", dsCongNo);
        duLieu.put("NhanVienDangNhap.txt",
                new utils.KhoDuLieuTxt.DuLieuDangNhap(
                        new NhanVien[]{quanLy, quanKho, thuNgan, keToan}, dsTaiKhoan));
        LuuTruNhieuFile.ghiDuLieu(thuMuc, duLieu);
    }

    public void luuDuLieu() {
        try {
            ghiDuLieu(THU_MUC_DU_LIEU);
        } catch (IOException e) {
            System.err.println("[!] Chưa lưu được dữ liệu: " + e.getMessage());
        }
    }

    public static void chayChuongTrinh() {
        GiaoDien giaoDien;
        try {
            giaoDien = docDuLieu(THU_MUC_DU_LIEU);
        } catch (IOException e) {
            System.err.println("Không mở được dữ liệu: " + e.getMessage());
            return;
        }
        try (Scanner sc = new Scanner(System.in)) {
            giaoDien.TrangChu(sc);
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Đã kết thúc nhập liệu.");
        } finally {
            giaoDien.luuDuLieu();
        }
    }
}
