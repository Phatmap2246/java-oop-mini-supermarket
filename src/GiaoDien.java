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

    private void lienKetVaiTro(TaiKhoanNhanVien nv) {
        if (nv instanceof QuanLy) {
            QuanLy ql = (QuanLy) nv;
            ql.setDSTaiKhoan(dsTaiKhoan);
            ql.setDSKhuyenMai(dsKhuyenMai);
            ql.setDanhSachSPKM(dsSanPhamKhuyenMai);
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
        lienKetVaiTro(quanLy);
        lienKetVaiTro(quanKho);
        lienKetVaiTro(thuNgan);
        for (TaiKhoanNhanVien nv : dsTaiKhoan.getDanhSachTaiKhoan()) lienKetVaiTro(nv);
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
    private DSTaiKhoan dsTaiKhoan = new DSTaiKhoan();
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

    private String taiKhoanQuanKho , matKhauQuanKho , taiKhoanThuNgan , matKhauThuNgan , taiKhoanKeToan , matKhauKeToan , taiKhoanQuanLy, matKhauQuanLy  ;
    public GiaoDien()
    {
        taiKhoanKeToan = "KeToanSieuThi";
        taiKhoanQuanKho = "QuanKhoSieuThi";
        taiKhoanQuanLy = "QuanLySieuThi";
        taiKhoanThuNgan = "ThuNganSieuThi";
        matKhauThuNgan = "thungan@123" ;
        matKhauKeToan = "ketoan@123" ;
        matKhauQuanKho = "quankho@123" ;
        matKhauQuanLy = "quanly@123" ;
        khoiTaoTaiKhoanMacDinh();
        lienKetDuLieu();
    }
    private TaiKhoanNhanVien nguoiDangNhap;

    public void TrangChu(Scanner sc) {
        if (sc == null) throw new IllegalArgumentException("Scanner không được null.");
        dsSanPhamKhuyenMai.tuDongGiamGiaCanHSD(dsSanPham, dsKhuyenMai);
        while (true) {
            nguoiDangNhap = dangNhap(sc);
            if (nguoiDangNhap == null) return;
            try {
                System.out.println("Đăng nhập thành công: " + nguoiDangNhap.getMaNV());
                moMenuNguoiDangNhap(sc);
            } catch (IllegalStateException e) {
                System.out.println("[!] " + e.getMessage());
            } finally {
                nguoiDangNhap = null;
                dsHoaDonBan.cauHinh(dsSanPhamKhuyenMai, dsKhuyenMai, dsSanPham, dsKhachHang, null);
                dsHoaDonNhap.cauHinh(null, dsSanPham, dsDoiTac);
                luuDuLieu();
            }
        }
    }

    private TaiKhoanNhanVien dangNhap(Scanner sc) {
        System.out.println("\n================ ĐĂNG NHẬP ================");
        int soLanSai = 0;
        while (true) {
            System.out.print("Tài khoản (0 để thoát): ");
            System.out.flush();
            if (!sc.hasNextLine()) return null;
            String tk = sc.nextLine().trim();
            if (tk.equals("0")) return null;
            System.out.print("Mật khẩu: ");
            System.out.flush();
            if (!sc.hasNextLine()) return null;
            TaiKhoanNhanVien nv = dsTaiKhoan.timKiemTheoTaiKhoan(tk);
            String mk = sc.nextLine();
            if (nv != null && !nv.isSaThai() && nv.getMatKhau().equals(mk)) return nv;
            System.out.println("Sai tài khoản hoặc mật khẩu, hoặc tài khoản đã bị vô hiệu hóa.");
            System.out.println("Số lần đăng nhập sai: " + (++soLanSai));
        }
    }

    private void khoiTaoTaiKhoanMacDinh() {
        TaiKhoanNhanVien[] ds = {quanLy, quanKho, thuNgan, keToan};
        String[] tk = {taiKhoanQuanLy, taiKhoanQuanKho, taiKhoanThuNgan, taiKhoanKeToan};
        String[] mk = {matKhauQuanLy, matKhauQuanKho, matKhauThuNgan, matKhauKeToan};
        for (int i = 0; i < ds.length; i++) {
            TaiKhoanNhanVien cu = ds[i];
            TaiKhoanNhanVien canonical = null;
            for (TaiKhoanNhanVien nv : dsTaiKhoan.getDanhSachTaiKhoan())
                if (nv.getMaNV().equalsIgnoreCase(ds[i].getMaNV())) canonical = nv;
            if (canonical == null) {
                canonical = ds[i];
                dsTaiKhoan.getDanhSachTaiKhoan().add(canonical);
            }
            if (canonical.getClass() != ds[i].getClass())
                throw new IllegalStateException("Xung đột kiểu nhân viên: " + canonical.getMaNV());
            ds[i] = canonical;

            if (ds[i].getTaiKhoan().isEmpty()) {
                String ten = cu.getTaiKhoan().isEmpty() ? tk[i] : cu.getTaiKhoan();
                String matKhau = cu.getTaiKhoan().isEmpty() ? mk[i] : cu.getMatKhau();
                TaiKhoanNhanVien trung = dsTaiKhoan.timKiemTheoTaiKhoan(ten);
                if (trung != null && trung != ds[i]) throw new IllegalStateException("Trùng tài khoản cũ: " + ten);
                ds[i].setTaiKhoan(ten);
                ds[i].setMatKhau(matKhau);
            }
        }
        quanLy = (QuanLy) ds[0];
        quanKho = (QuanKho) ds[1];
        thuNgan = (ThuNgan) ds[2];
        keToan = (KeToan) ds[3];
    }

    private void moMenuNguoiDangNhap(Scanner sc) {
        lienKetVaiTro(nguoiDangNhap);
        dsHoaDonBan.cauHinh(dsSanPhamKhuyenMai, dsKhuyenMai, dsSanPham, dsKhachHang, nguoiDangNhap.getMaNV());
        dsHoaDonNhap.cauHinh(nguoiDangNhap.getMaNV(), dsSanPham, dsDoiTac);
        if (nguoiDangNhap instanceof ThuNgan) MenuThuNgan(sc);
        else if (nguoiDangNhap instanceof QuanKho) MenuQuanLyKho(sc);
        else if (nguoiDangNhap instanceof QuanLy) MenuQuanLy(sc);
        else if (nguoiDangNhap instanceof KeToan) MenuKeToan(sc);
        else System.out.println("Kiểu nhân viên chưa có menu: " + nguoiDangNhap.getClass().getSimpleName());
    }
    public void MenuQuanLyKho(Scanner sc) {
        if (nguoiDangNhap instanceof QuanKho nv) nv.thucHienNghiepVu(sc, dsDoiTac);
    }
    public void MenuQuanLy(Scanner sc) {
        if (nguoiDangNhap instanceof QuanLy nv) nv.thucHienNghiepVu(sc, dsSanPham, dsDoiTac, dsHoaDonNhap, dsHoaDonBan, dsKhachHang);
    }
    public void MenuThuNgan(Scanner sc) {
        if (nguoiDangNhap instanceof ThuNgan nv) nv.thucHienNghiepVu(sc, dsSanPham, dsSanPhamKhuyenMai, dsKhuyenMai, dsKhachHang);
    }
    public void MenuKeToan(Scanner sc) {
        if (nguoiDangNhap instanceof KeToan nv) {
            dsCongNo.cauHinh(dsHoaDonNhap, dsDoiTac);
            nv.thucHienNghiepVu(sc, dsHoaDonBan, dsHoaDonNhap, dsDoiTac, dsCongNo);
        }
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
                gd.dsTaiKhoan.docFile(duongDanFile(hienTai, "NhanVien.txt"));
                gd.dsKhachHang.docFile(duongDanFile(hienTai, "KhachHang.txt"));
                gd.dsDoiTac.docFile(duongDanFile(hienTai, "DoiTac.txt"));
                gd.dsKhuyenMai.docFile(duongDanFile(hienTai, "KhuyenMai.txt"));
                gd.dsSanPhamKhuyenMai.docFile(duongDanFile(hienTai, "SanPhamKhuyenMai.txt"));
                gd.dsHoaDonBan.docFile(duongDanFile(hienTai, "HoaDonBan.txt"));
                gd.dsHoaDonNhap.docFile(duongDanFile(hienTai, "HoaDonNhap.txt"));
                gd.dsCongNo.docFile(duongDanFile(hienTai, "CongNo.txt"));
                TaiKhoanNhanVien[] vaiTro = utils.KhoDuLieuTxt.docVaiTro(duongDanFile(hienTai, "NhanVienDangNhap.txt"));
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
            gd.khoiTaoTaiKhoanMacDinh();
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
        duLieu.put("NhanVien.txt", dsTaiKhoan);
        duLieu.put("KhachHang.txt", dsKhachHang);
        duLieu.put("DoiTac.txt", dsDoiTac);
        duLieu.put("KhuyenMai.txt", dsKhuyenMai);
        duLieu.put("SanPhamKhuyenMai.txt", dsSanPhamKhuyenMai);
        duLieu.put("HoaDonBan.txt", dsHoaDonBan);
        duLieu.put("HoaDonNhap.txt", dsHoaDonNhap);
        duLieu.put("CongNo.txt", dsCongNo);
        duLieu.put("NhanVienDangNhap.txt", new TaiKhoanNhanVien[]{quanLy, quanKho, thuNgan, keToan});
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
        chayChuongTrinh(new Scanner(System.in));
    }

    public static void chayChuongTrinh(Scanner sc) {
        GiaoDien giaoDien;
        try {
            giaoDien = docDuLieu(THU_MUC_DU_LIEU);
        } catch (IOException e) {
            System.err.println("Không mở được dữ liệu: " + e.getMessage());
            return;
        }
        try {
            giaoDien.TrangChu(sc);
        } catch (java.util.NoSuchElementException e) {
            System.out.println("Đã kết thúc nhập liệu.");
        } finally {
            giaoDien.luuDuLieu();
        }
    }
}
