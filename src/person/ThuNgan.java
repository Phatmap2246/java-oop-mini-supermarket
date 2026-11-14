package person;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import hoadon.DanhSachHoaDonBan;
import sanpham.QuanLySanPham;
import utils.Validation;

public class ThuNgan extends NhanVien {
    private long tongTienDauCa;
    private DanhSachHoaDonBan danhSachHoaDonBan ;

    public ThuNgan() {
        super();
        tongTienDauCa = 0;
        danhSachHoaDonBan = new DanhSachHoaDonBan() ;
    }

    public ThuNgan(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh,
            String maNV, String vaitro, long luong, long phuCapTrachNhiem, long tongTienDauCa) {
        super(email, hoten, sdt, diachi, ngsinh, maNV, vaitro, luong, phuCapTrachNhiem);
        this.tongTienDauCa = tongTienDauCa;
        danhSachHoaDonBan = new DanhSachHoaDonBan();
    }

    public ThuNgan(ThuNgan tn , DanhSachHoaDonBan danhSachHoaDonBan) {
        super(tn );
        if (danhSachHoaDonBan == null) {
            throw new IllegalArgumentException("Danh sách hoá đơn bán không được rỗng!");
        }
        this.danhSachHoaDonBan = danhSachHoaDonBan;
        tongTienDauCa = tn.tongTienDauCa;
    }

    public DanhSachHoaDonBan getDanhSachHoaDonBan() {
        return danhSachHoaDonBan;
    }

    public void setDanhSachHoaDonBan(DanhSachHoaDonBan danhSachHoaDonBan) {
        if ( danhSachHoaDonBan == null )
        {
            throw new IllegalArgumentException("Danh sách hoá đơn bán không được rỗng!");
        }
        this.danhSachHoaDonBan = danhSachHoaDonBan;
    }
    public void chinhSuaHoaDonChuaThanhToan(Scanner sc, DanhSachSPKM danhSachSPKM,
                                            Danhsachkhuyenmai danhSachKhuyenMai, QuanLySanPham qlsp, DSKhachHang dskh)
    {
        danhSachHoaDonBan.chinhSuaHoaDonChuaThanhToan(sc, danhSachSPKM, danhSachKhuyenMai, qlsp, dskh, getMaNV());
    }
    public void inDanhSachHoaDonBan()
    {
        danhSachHoaDonBan.hienThiDanhSachHoaDonBan();
    }
    public void locCacHoaDonTheoTongGia ( long nguongGia )
    {
        danhSachHoaDonBan.locCacHoaDonTheoTongGia(nguongGia);
    }
    public void timKiemHoaDon( String maHoaDon )
    {
        danhSachHoaDonBan.timKiemHoaDon(maHoaDon);
    }
    public long getTienDauCa() {
        return tongTienDauCa;
    }

    public long tinhTienCuoiCa()
    {
        return tongTienDauCa + danhSachHoaDonBan.tinhTongTienDaThanhToan();
    }

    public void taoHoaDonThanhToan(Scanner sc , QuanLySanPham qlsp , DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai dsKhuyenMai , DSKhachHang dskh)
    {
        danhSachHoaDonBan.taoHoaDonThanhToan(sc, qlsp, danhSachSPKM, dsKhuyenMai, dskh, getMaNV());
    }
    //  Cần sửa chữa
    public void thayCa(ThuNgan tn) throws IOException {
        if (tn == null) {
            System.out.println("Không tìm thấy thu ngân nhận ca!");
            return;
        }
        if (danhSachHoaDonBan.ghiLichSuCa(getMaNV())) {
            tn.tongTienDauCa = tinhTienCuoiCa();
        }
    }

    @Override
    public String getVaiTro() {
        return "ThuNgan";
    }

    public void thucHienNghiepVu(Scanner sc, QuanLySanPham qlsp, DanhSachSPKM dsspkm, Danhsachkhuyenmai dskm, DSKhachHang dskh)
    {
        int luaChon ;
        do {
            System.out.println("===================NGHIỆP VỤ THU NGÂN===================");
            System.out.println("1. Lập hóa đơn thanh toán!");
            System.out.println("2. Chỉnh sửa hóa đơn chưa thanh toán!");
            System.out.println("3. Xuất danh sách hóa đơn bán!");
            System.out.println("4. Tìm kiếm hóa đơn bán!");
            System.out.println("5. Quản lý khách hàng!");
            System.out.println("6. Tính tiền cuối ca!");
            System.out.println("7. Lọc hóa đơn theo tổng giá!");
            System.out.println("8. Thống kê hóa đơn chưa thanh toán!");
            System.out.println("9. Thêm k hóa đơn bán!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 9);
            switch (luaChon)
            {
                case 1: {
                    taoHoaDonThanhToan(sc, qlsp, dsspkm, dskm, dskh);
                    break;
                }
                case 2: {
                    chinhSuaHoaDonChuaThanhToan(sc, dsspkm, dskm, qlsp, dskh);
                    break;
                }
                case 3: {
                    inDanhSachHoaDonBan();
                    break;
                }
                case 4: {
                    danhSachHoaDonBan.timKiemTheoNhieuKhoa(sc);
                    break;
                }
                case 5: {
                    dskh.quanLyDanhSach(sc);
                    break;
                }
                case 6: {
                    System.out.println("Tổng tiền cuối ca là: " + tinhTienCuoiCa());
                    break;
                }
                case 7: {
                    long nguongGia = Validation.inputLong(sc, "Nhập ngưỡng tổng giá: ");
                    locCacHoaDonTheoTongGia(nguongGia);
                    break;
                }
                case 8: {
                    danhSachHoaDonBan.thongKeHoaDonChuaThanhToan();
                    break;
                }
                case 9: {
                    int k = Validation.inputInt(sc, "Nhập số phần tử cần thêm: ");
                    danhSachHoaDonBan.taoKHoaDonBan(sc, dsspkm, dskm, qlsp, dskh, getMaNV(), k);
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
