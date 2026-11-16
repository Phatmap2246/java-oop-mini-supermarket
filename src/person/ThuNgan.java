package person;

import utils.DongDuLieu;

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
    private transient DanhSachHoaDonBan danhSachHoaDonBan ;

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
        setDanhSachHoaDonBan(danhSachHoaDonBan);
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
        danhSachHoaDonBan.xuatDanhSach();
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
            System.out.println("5. Tính tiền cuối ca!");
            System.out.println("6. Lọc hóa đơn theo tổng giá!");
            System.out.println("7. Thống kê hóa đơn chưa thanh toán!");
            System.out.println("8. Thêm k hóa đơn bán!");
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
                    danhSachHoaDonBan.timKhoa(sc);
                    break;
                }
                case 5: {
                    System.out.println("Tổng tiền cuối ca là: " + tinhTienCuoiCa());
                    break;
                }
                case 6: {
                    long nguongGia = Validation.inputLong(sc, "Nhập ngưỡng tổng giá: ");
                    locCacHoaDonTheoTongGia(nguongGia);
                    break;
                }
                case 7: {
                    danhSachHoaDonBan.thongKeHoaDonChuaThanhToan();
                    break;
                }
                case 8: {
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

    // ================= ĐỌC / GHI FILE TXT =================

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "THUNGAN;" + thongTinNhanVienFile() + ";" + DongDuLieu.ghep(tongTienDauCa);
    }

    public static ThuNgan tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 16 && "THUNGAN".equals(p[0]), "Sai dòng THUNGAN (cần 16 cột).");
        int dem = NhanVien.getSoThuTu();
        ThuNgan obj = new ThuNgan();
        NhanVien.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinNhanVienFile(p);
        obj.tongTienDauCa = Long.parseLong(p[15]);
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.khongAm(tongTienDauCa, "tongTienDauCa");
    }
}
