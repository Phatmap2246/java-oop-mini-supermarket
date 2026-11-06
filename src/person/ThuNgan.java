package person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import hoadon.HoaDonBanHang;
import sanpham.QuanLySanPham;
import sanpham.QuanLyTonKho;

public class ThuNgan extends NhanVien {
    private long tongTienDauCa; 
    private ArrayList<HoaDonBanHang> dsHoaDonBanHang = new ArrayList<>();
    public ThuNgan(){
        super();
        tongTienDauCa = 0;
    }
    public ThuNgan(String email,String hoten,String sdt,DiaChi diachi, LocalDate ngsinh,String maNV,String vaitro,long luong,long phuCapTrachNhiem,long tongTienDauCa){
        super(email,hoten,sdt,diachi,ngsinh,maNV,vaitro,luong,phuCapTrachNhiem);
        this.tongTienDauCa = tongTienDauCa;
    }
    public ThuNgan(ThuNgan tn){
        super(tn);
        tongTienDauCa = tn.tongTienDauCa;
    }
    public long getTienDauCa(){
        return tongTienDauCa;
    }
    public long tinhTienCuoiCa(){
        long tongTienCuoiCa = 0; 
        for(HoaDonBanHang i: dsHoaDonBanHang) {
            if (i.isDaThanhToan()) {
                tongTienCuoiCa += i.tinhTongTien();
            }
        }
        return tongTienDauCa +  tongTienCuoiCa;
    }
    public void taoHoaDon(Scanner sc){
        taoHoaDon(sc, new DanhSachSPKM(), new Danhsachkhuyenmai());
    }
    public void taoHoaDon(Scanner sc, DanhSachSPKM danhSachSPKM,
            Danhsachkhuyenmai danhSachKhuyenMai){
        taoHoaDon(sc, danhSachSPKM, danhSachKhuyenMai, new QuanLySanPham());
    }
    public void taoHoaDon(Scanner sc, DanhSachSPKM danhSachSPKM,
            Danhsachkhuyenmai danhSachKhuyenMai, QuanLySanPham quanLySanPham){
        HoaDonBanHang hdbh = new HoaDonBanHang();
        boolean daLap = hdbh.nhapHoaDon(sc, danhSachSPKM, danhSachKhuyenMai,
                maHoaDon -> dsHoaDonBanHang.stream()
                        .anyMatch(hoaDon -> hoaDon.getMaHoaDon().equalsIgnoreCase(maHoaDon)),
                quanLySanPham.getDanhSachSanPham());
        if (!daLap) {
            return;
        }
        if (hdbh.isDaThanhToan()) {
            QuanLyTonKho.truTonKho(
                    quanLySanPham.getDanhSachSanPham(), hdbh.getDanhSachChiTiet());
        }
        dsHoaDonBanHang.add(hdbh);
    }
    public void thayCa(ThuNgan tn){
        // ghi list hoa don vao file 
        tn.tongTienDauCa = tinhTienCuoiCa();
    }

}
