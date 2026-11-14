package person;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import dichvu.Danhsachdoitac;
import hoadon.*;
import sanpham.*;
import utils.Validation;
public class QuanKho extends NhanVien
{
    private QuanLySanPham qlsp ;
    private DanhSachHoaDonNhap dsHoaDonNhap;
    public QuanKho(QuanLySanPham qlsp , DanhSachHoaDonNhap dsHoaDonNhap) {
        super();
        setQlsp(qlsp);
        if (dsHoaDonNhap == null) throw new IllegalArgumentException("Danh sách hóa đơn nhập không được null.");
        this.dsHoaDonNhap = dsHoaDonNhap ;
        super.setVaiTro("QuanKho");
    }

    public QuanKho(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh,
                     String maNV, long luong, long phuCapTrachNhiem) {
        super(email, hoten, sdt, diachi, ngsinh, maNV, "QuanKho", luong, phuCapTrachNhiem);
        qlsp = new QuanLySanPham();
        dsHoaDonNhap = new DanhSachHoaDonNhap();
    }
    public QuanLySanPham getQlsp() {
        return qlsp;
    }

    public void setQlsp(QuanLySanPham qlsp) {
        if (qlsp == null) {
            throw new IllegalArgumentException("Danh sách sản phẩm không được null.");
        }
        this.qlsp = qlsp;
    }
    public void checkHSD( String maSanPham )
    {
        qlsp.checkHSD(maSanPham);
    }
    public ArrayList<SanPham> danhSachQuaHan()
    {
        return qlsp.danhSachQuaHan();
    }
    public void inDanhSachQuaHan()
    {
        qlsp.inDanhSachQuaHan();
    }
    public ArrayList<SanPham> danhSachSanPhamBanHet()
    {
        return qlsp.danhSachSanPhamBanHet();
    }
    public void inDanhSachSanPhamBanHet()
    {
        qlsp.inDanhSachSanPhamBanHet();
    }
    public ArrayList<SanPham> danhSachSapHetHan()
    {
        return qlsp.danhSachSapHetHan();
    }
    public ArrayList<SanPham> danhSachTonKhoThap()
    {
        return qlsp.danhSachTonKhoThap();
    }
    public void inDanhSachTonKhoThap()
    {
        qlsp.inDanhSachTonKhoThap();
    }
    public void inDanhSachSapHetHan()
    {
        qlsp.inDanhSachSapHetHan();
    }
    public void nhapThemHang(Scanner sc , QuanLySanPham qlsp , Danhsachdoitac dsDoiTac)
    {
        dsHoaDonNhap.nhapThemHang(sc, qlsp, dsDoiTac, getMaNV());
    }
    public void ktraHoaDonDaDuyet()
    {
        dsHoaDonNhap.ktraHoaDonDaDuyet();
    }
    public void capNhatKho()
    {
        dsHoaDonNhap.capNhatKho(qlsp);
    }
    public void inDanhSachSanPham()
    {
        qlsp.inDanhSachSanPham();
    }
    public void kiemTraVaLoaiBoHangHetHan(Scanner sc )
    {
        qlsp.kiemTraVaLoaiBoHangHetHan(sc);
    }
    public void danhSachPhieuNhap()
    {
        dsHoaDonNhap.hienThiDanhSachHoaDonNhap();
    }
    public void kiemTraThongTinSanPham( String maSanPham)
    {
        dsHoaDonNhap.kiemTraThongTinSanPham(maSanPham);
    }
    public int kiemTraSoLuongSPNhapGanNhat( String maSanPham)
    {
        return dsHoaDonNhap.kiemTraSoLuongSPNhapGanNhat(maSanPham);
    }

    public void thucHienNghiepVu(Scanner sc, Danhsachdoitac dsDoiTac)
    {
        int luaChon ;
        do {
            System.out.println("===================NGHIỆP VỤ QUẢN KHO===================");
            System.out.println("1. Xuất danh sách sản phẩm!");
            System.out.println("2. Tìm kiếm sản phẩm!");
            System.out.println("3. Kiểm tra hạn sử dụng!");
            System.out.println("4. Xem sản phẩm đã bán hết!");
            System.out.println("5. Xem sản phẩm tồn kho thấp!");
            System.out.println("6. Xem sản phẩm sắp hết hạn!");
            System.out.println("7. Kiểm tra và loại bỏ hàng hết hạn!");
            System.out.println("8. Lập phiếu nhập hàng theo đề xuất!");
            System.out.println("9. Quản lý hóa đơn nhập!");
            System.out.println("10. Cập nhật hàng vào kho!");
            System.out.println("11. Tra cứu lịch sử nhập của sản phẩm!");
            System.out.println("12. Kiểm tra số lượng nhập gần nhất!");
            System.out.println("13. Thống kê sản phẩm theo loại!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 13);
            switch (luaChon)
            {
                case 1: {
                    inDanhSachSanPham();
                    break;
                }
                case 2: {
                    qlsp.timKiemTheoNhieuKhoa(sc);
                    break;
                }
                case 3: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ", Validation.MaSP_REGEX, "Mã sản phẩm không đúng định dạng!");
                    checkHSD(ma);
                    break;
                }
                case 4: {
                    inDanhSachSanPhamBanHet();
                    break;
                }
                case 5: {
                    inDanhSachTonKhoThap();
                    break;
                }
                case 6: {
                    inDanhSachSapHetHan();
                    break;
                }
                case 7: {
                    kiemTraVaLoaiBoHangHetHan(sc);
                    break;
                }
                case 8: {
                    nhapThemHang(sc, qlsp, dsDoiTac);
                    break;
                }
                case 9: {
                    dsHoaDonNhap.quanLyDanhSach(sc, getMaNV(), qlsp, dsDoiTac);
                    break;
                }
                case 10: {
                    capNhatKho();
                    break;
                }
                case 11: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ", Validation.MaSP_REGEX, "Mã sản phẩm không đúng định dạng!");
                    kiemTraThongTinSanPham(ma);
                    break;
                }
                case 12: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ", Validation.MaSP_REGEX, "Mã sản phẩm không đúng định dạng!");
                    System.out.println("Số lượng nhập gần nhất là: " + kiemTraSoLuongSPNhapGanNhat(ma));
                    break;
                }
                case 13: {
                    qlsp.thongKeTheoLoai(sc);
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        } while (luaChon != 0);
    }
}
