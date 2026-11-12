package person;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

import hoadon.HoaDonBanHang;
import utils.DataBase;

public class ThuNgan extends NhanVien {
    private long tongTienDauCa;
    private HoaDonBanHang[] dsHoaDonBanHang = new HoaDonBanHang[0];

    public ThuNgan() {
        super();
        tongTienDauCa = 0;
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

    public void taoHoaDonThanhToan(Scanner sc) {
        HoaDonBanHang hdbh = new HoaDonBanHang();
        hdbh.nhapHoaDon(sc);
        HoaDonBanHang[] dsMoi = new HoaDonBanHang[dsHoaDonBanHang.length + 1];
        for (int i = 0; i < dsHoaDonBanHang.length; i++) {
            dsMoi[i] = dsHoaDonBanHang[i];
        }
        dsHoaDonBanHang = dsMoi;
        dsHoaDonBanHang[dsHoaDonBanHang.length - 1] = hdbh;
    }

    public void thayCa(ThuNgan tn) throws IOException {
        Path filePath = Paths.get("hoadon.LichSuHoaDon");
        if (!DataBase.tonTai(filePath)) {
            System.out.println("[!] Không tìm thấy file lịch sử hóa đơn.");
            return;
        }
        StringBuilder noiDung = new StringBuilder();
        for (HoaDonBanHang hoaDon : dsHoaDonBanHang) {
            noiDung.append(System.lineSeparator())
                    .append(String.join(",", maNV, hoaDon.getMaHoaDon(),
                            hoaDon.getTenDoiTac(), hoaDon.getNgayLap().toString()));
        }
        DataBase.ghiVanBan(filePath, noiDung.toString(), false);
        tn.tongTienDauCa = tinhTienCuoiCa();
    }
}
