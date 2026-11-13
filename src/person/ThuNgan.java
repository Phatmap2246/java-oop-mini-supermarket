package person;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import hoadon.HoaDonBanHang;
import sanpham.SanPham;
import utils.DataBase;

public class ThuNgan extends NhanVien {
    private long tongTienDauCa;
    private HoaDonBanHang[] dsHoaDonBanHang ;

    public ThuNgan() {
        super();
        tongTienDauCa = 0;
        dsHoaDonBanHang = new HoaDonBanHang[0];
    }

    public ThuNgan(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh,
            String maNV, String vaitro, long luong, long phuCapTrachNhiem, long tongTienDauCa) {
        super(email, hoten, sdt, diachi, ngsinh, maNV, vaitro, luong, phuCapTrachNhiem);
        this.tongTienDauCa = tongTienDauCa;
    }

    public ThuNgan(ThuNgan tn) {
        super(tn);
        tongTienDauCa = tn.tongTienDauCa;
    }
    public HoaDonBanHang[] getDanhSachHDB(){
        return dsHoaDonBanHang ; 
    }
    public void setDanhSachHDB(HoaDonBanHang[] dshd)
    {
        dsHoaDonBanHang = dshd; 
    }
    public long getTienDauCa() {
        return tongTienDauCa;
    }

    public long tinhTienCuoiCa() {
        long tongTienCuoiCa = 0;
        for (HoaDonBanHang hoaDon : dsHoaDonBanHang) {
            if (hoaDon.isDaThanhToan()) {
                tongTienCuoiCa += hoaDon.tinhTongTien();
            }
        }
        return tongTienDauCa + tongTienCuoiCa;
    }

    public void taoHoaDonThanhToan(Scanner sc, DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai danhSachKhuyenMai, List<SanPham> danhSachSanPham) {
        HoaDonBanHang hdbh = new HoaDonBanHang();
        hdbh.nhapHoaDon(sc,danhSachSPKM,danhSachKhuyenMai,danhSachSanPham);
        HoaDonBanHang[] dsMoi = new HoaDonBanHang[dsHoaDonBanHang.length + 1];
        for (int i = 0; i < dsHoaDonBanHang.length; i++) {
            dsMoi[i] = dsHoaDonBanHang[i];
        }
        dsHoaDonBanHang = dsMoi;
        dsHoaDonBanHang[dsHoaDonBanHang.length - 1] = hdbh;
    }

    public void thayCa(ThuNgan tn) throws IOException {
        File f = new File("hoadon.LichSuHoaDon");
        if (!f.exists()) {
            System.out.println("[!] Không tìm thấy file lịch sử hóa đơn.");
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            for (int i = 0; i < dsHoaDonBanHang.length; i++) {
                String[] data = new String[4];
                data[0] = maNV;
                data[1] = dsHoaDonBanHang[i].getMaHoaDon();
                data[2] = dsHoaDonBanHang[i].getSDTDoiTac();
                data[3] = dsHoaDonBanHang[i].getNgayLap().toString();
                bw.newLine();
                bw.write(String.join(",", data));
            }
            tn.tongTienDauCa = tinhTienCuoiCa();
        }
    }
}
