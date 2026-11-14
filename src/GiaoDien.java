import person.*;
import sanpham.QuanLySanPham;
import hoadon.DanhSachHoaDonBan;
import hoadon.DanhSachHoaDonNhap;
import hoadon.DanhSachCongNo;
import dichvu.Danhsachdoitac;
import dichvu.Danhsachkhuyenmai;
import dichvu.DanhSachSPKM;
import utils.Validation;
import utils.DataBase;
import utils.LuuTruNhieuFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import dichvu.Nhacungcap;
import dichvu.Doitac;
import sanpham.SanPham;

import java.util.Scanner;

public class GiaoDien {
    public static final String THU_MUC_DU_LIEU = utils.DuongDanLuuTru.thuMuc().toString();

    public static GiaoDien docDuLieu(String thuMuc) throws IOException {
        Files.createDirectories(Paths.get(thuMuc));
        Path hienTai = LuuTruNhieuFile.thuMucHienTai(thuMuc);
        GiaoDien gd;
        if (Files.exists(hienTai)) {
            gd = new GiaoDien();
            gd.dsSanPham = docFile(QuanLySanPham.class, hienTai, "SanPham.txt");
            gd.dsNhanVien = docFile(DSNhanVien.class, hienTai, "NhanVien.txt");
            gd.dsKhachHang = docFile(DSKhachHang.class, hienTai, "KhachHang.txt");
            gd.dsDoiTac = docFile(Danhsachdoitac.class, hienTai, "DoiTac.txt");
            gd.dsKhuyenMai = docFile(Danhsachkhuyenmai.class, hienTai, "KhuyenMai.txt");
            gd.dsSanPhamKhuyenMai = docFile(DanhSachSPKM.class, hienTai, "SanPhamKhuyenMai.txt");
            gd.dsHoaDonBan = docFile(DanhSachHoaDonBan.class, hienTai, "HoaDonBan.txt");
            gd.dsHoaDonNhap = docFile(DanhSachHoaDonNhap.class, hienTai, "HoaDonNhap.txt");
            gd.dsCongNo = docFile(DanhSachCongNo.class, hienTai, "CongNo.txt");
            NhanVien[] vaiTro = docFile(NhanVien[].class, hienTai, "NhanVienDangNhap.txt");
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
        return gd;
    }

    private static <T> T docFile(Class<T> lop, Path thuMuc, String tenFile) throws IOException {
        Path file = thuMuc.resolve(tenFile);
        T duLieu = DataBase.docDoiTuong(lop, file.toString());
        if (duLieu == null) throw new IOException("Thiếu file dữ liệu: " + tenFile);
        return duLieu;
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
        duLieu.put("NhanVienDangNhap.txt", new NhanVien[]{quanLy, quanKho, thuNgan, keToan});
        LuuTruNhieuFile.ghiDuLieu(thuMuc, duLieu);
    }

    private void lienKetVaiTro(NhanVien nv) {
        if (nv instanceof QuanLy) {
            QuanLy ql = (QuanLy) nv;
            ql.setDSNhanVien(dsNhanVien);
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

    public void luuDuLieu() {
        try {
            ghiDuLieu(THU_MUC_DU_LIEU);
        } catch (IOException e) {
            System.err.println("[!] Chưa lưu được dữ liệu: " + e.getMessage());
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
        lienKetDuLieu();
    }
    public void TrangChu(Scanner sc )
    {
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
            // Lưu thay đổi sau mỗi lần trở về từ một menu nghiệp vụ.
            luuDuLieu();
        }
    }
    public void MenuQuanLyKho( Scanner sc )
    {
        System.out.println("=================ĐĂNG NHẬP QUẢN LÝ KHO=================");
        System.out.println("Vui lòng nhập thông tin đăng nhập!");
        System.out.println("Nhâp tài khoản: ");
        String taiKhoan = sc.nextLine() ;
        System.out.println("Nhập mật khẩu: ");
        String matKhau = sc.nextLine() ;
        int soLanNhapSai = 5 ;
        while ( !taiKhoan.equalsIgnoreCase(taiKhoanQuanKho) || !matKhau.equalsIgnoreCase(matKhauQuanKho) )
        {
            soLanNhapSai-- ;
            if ( soLanNhapSai == 0 ) break;
            System.out.println("Bạn đã nhập sai mật khẩu hoặc tài khoản vui lòng nhập lại!(Còn " +soLanNhapSai + " lần thử)");
            System.out.println("Nhâp tài khoản: ");
            taiKhoan = sc.nextLine() ;
            System.out.println("Nhập mật khẩu: ");
            matKhau = sc.nextLine() ;
        }
        if ( soLanNhapSai == 0 )
        {
            System.out.println("Bạn đã nhập sai quá 5 lần do đó bạn phải chờ thời gian nhập lại!");
            return;
        }
        quanKho.thucHienNghiepVu(sc, dsDoiTac);
    }
    public void MenuKeToan( Scanner sc )
    {
        System.out.println("=================ĐĂNG NHẬP KẾ TOÁN=================");
        System.out.println("Vui lòng nhập thông tin đăng nhập!");
        System.out.println("Nhâp tài khoản: ");
        String taiKhoan = sc.nextLine() ;
        System.out.println("Nhập mật khẩu: ");
        String matKhau = sc.nextLine() ;
        int soLanNhapSai = 5 ;
        while ( !taiKhoan.equalsIgnoreCase(taiKhoanKeToan) || !matKhau.equalsIgnoreCase(matKhauKeToan) )
        {
            soLanNhapSai--;
            if ( soLanNhapSai == 0 ) break;
            System.out.println("Bạn đã nhập sai mật khẩu hoặc tài khoản vui lòng nhập lại!(Còn " +soLanNhapSai + " lần thử)");
            System.out.println("Nhâp tài khoản: ");
            taiKhoan = sc.nextLine() ;
            System.out.println("Nhập mật khẩu: ");
            matKhau = sc.nextLine() ;
        }
        if ( soLanNhapSai == 0 )
        {
            System.out.println("Bạn đã nhập sai quá 5 lần do đó bạn phải chờ thời gian nhập lại!");
            return;
        }
        keToan.thucHienNghiepVu(sc, dsHoaDonBan, dsHoaDonNhap, dsDoiTac, dsCongNo);
    }
    public void MenuQuanLy( Scanner sc )
    {
        System.out.println("=================ĐĂNG NHẬP QUẢN LÝ=================");
        System.out.println("Vui lòng nhập thông tin đăng nhập!");
        System.out.println("Nhâp tài khoản: ");
        String taiKhoan = sc.nextLine() ;
        System.out.println("Nhập mật khẩu: ");
        String matKhau = sc.nextLine() ;
        int soLanNhapSai = 5 ;
        while ( !taiKhoan.equalsIgnoreCase(taiKhoanQuanLy) || !matKhau.equalsIgnoreCase(matKhauQuanLy) )
        {
            soLanNhapSai--;
            if ( soLanNhapSai == 0 ) break;
            System.out.println("Bạn đã nhập sai mật khẩu hoặc tài khoản vui lòng nhập lại!(Còn " +soLanNhapSai + " lần thử)");
            System.out.println("Nhâp tài khoản: ");
            taiKhoan = sc.nextLine() ;
            System.out.println("Nhập mật khẩu: ");
            matKhau = sc.nextLine() ;
        }
        if ( soLanNhapSai == 0 )
        {
            System.out.println("Bạn đã nhập sai quá 5 lần do đó bạn phải chờ thời gian nhập lại!");
            return;
        }
        quanLy.thucHienNghiepVu(sc, dsSanPham, dsDoiTac, dsHoaDonNhap, dsHoaDonBan, dsKhachHang);
    }
    public void MenuThuNgan(Scanner sc )
    {
        System.out.println("=================ĐĂNG NHẬP THU NGÂN=================");
        System.out.println("Vui lòng nhập thông tin đăng nhập!");
        System.out.println("Nhâp tài khoản: ");
        String taiKhoan = sc.nextLine() ;
        System.out.println("Nhập mật khẩu: ");
        String matKhau = sc.nextLine() ;
        int soLanNhapSai = 5 ;
        while ( !taiKhoan.equalsIgnoreCase(taiKhoanThuNgan) || !matKhau.equalsIgnoreCase(matKhauThuNgan) )
        {
            soLanNhapSai--;
            if ( soLanNhapSai == 0 ) break;
            System.out.println("Bạn đã nhập sai mật khẩu hoặc tài khoản vui lòng nhập lại!(Còn " +soLanNhapSai + " lần thử)");
            System.out.println("Nhâp tài khoản: ");
            taiKhoan = sc.nextLine() ;
            System.out.println("Nhập mật khẩu: ");
            matKhau = sc.nextLine() ;
        }
        if ( soLanNhapSai == 0 )
        {
            System.out.println("Bạn đã nhập sai quá 5 lần do đó bạn phải chờ thời gian nhập lại!");
            return;
        }
        thuNgan.thucHienNghiepVu(sc, dsSanPham, dsSanPhamKhuyenMai, dsKhuyenMai, dsKhachHang);
    }
}
