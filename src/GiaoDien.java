import person.*;
import sanpham.QuanLySanPham;
import hoadon.DanhSachHoaDonBan;
import hoadon.DanhSachHoaDonNhap;
import hoadon.DanhSachCongNo;
import dichvu.Danhsachdoitac;
import dichvu.Danhsachkhuyenmai;
import dichvu.DanhSachSPKM;
import utils.Validation;

import java.util.Scanner;

public class GiaoDien {
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
        quanLy.setDSNhanVien(dsNhanVien);
        quanLy.setDSKhuyenMai(dsKhuyenMai);
        quanLy.setDanhSachSPKM(dsSanPhamKhuyenMai);
        thuNgan.setDanhSachHoaDonBan(dsHoaDonBan);
        keToan.setDanhSachHoaDonBan(dsHoaDonBan);
        keToan.setDanhSachHoaDonNhap(dsHoaDonNhap);
        keToan.setDanhSachDoiTac(dsDoiTac);
        keToan.setDanhSachCongNo(dsCongNo);
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
        keToan.thucHienNghiepVu(sc);
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
