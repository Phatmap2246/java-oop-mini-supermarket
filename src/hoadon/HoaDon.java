package hoadon;

import java.util.ArrayList;
import java.time.LocalDate;

public abstract class HoaDon {
    protected String maHoaDon;
    protected LocalDate ngayLap;
    protected String tenDoiTac;
    private static int demHoaDon = 0; // Sửa: Thêm = 0
    protected ArrayList<CTHoaDon> danhSachChiTiet;
    
    public HoaDon() {
        this.maHoaDon = "";
        this.ngayLap = LocalDate.now(); 
        this.tenDoiTac = "";
        this.danhSachChiTiet = new ArrayList<>();
        demHoaDon++;
    }

    public HoaDon(String maHoaDon, String tenDoiTac) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = LocalDate.now();
        this.tenDoiTac = tenDoiTac;
        this.danhSachChiTiet = new ArrayList<>();
        demHoaDon++;
    }

    // SỬA: Tự động gộp số lượng nếu thêm trùng mã sản phẩm
    public void themChiTiet(CTHoaDon ct){
        for (CTHoaDon existing : this.danhSachChiTiet) {
            if (existing.getMaSanPham().equalsIgnoreCase(ct.getMaSanPham())) {
                existing.setSoLuong(existing.getSoLuong() + ct.getSoLuong());
                return;
            }
        }
        this.danhSachChiTiet.add(ct);
    }

    public boolean xoaChiTietMSP(String maSanPham){
        for( int i = 0 ; i<this.danhSachChiTiet.size();i++){
            if (danhSachChiTiet.get(i).getMaSanPham().equals(maSanPham)) {
                danhSachChiTiet.remove(i);
                return true;
            }
        }
        return false;
    }

    public static int getDemHoaDon(){ return demHoaDon; }
    
    public abstract double tinhTongTien();
    public abstract void hienThiHoaDon();

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }
    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }
    public String getTenDoiTac() { return tenDoiTac; }
    public void setTenDoiTac(String tenDoiTac) { this.tenDoiTac = tenDoiTac; }
    public ArrayList<CTHoaDon> getDanhSachChiTiet() { return danhSachChiTiet; }
    public void setDanhSachChiTiet(ArrayList<CTHoaDon> danhSachChiTiet) { this.danhSachChiTiet = danhSachChiTiet; }
}