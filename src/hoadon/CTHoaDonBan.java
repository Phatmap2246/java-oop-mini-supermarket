package hoadon;
import java.util.Scanner;

import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import dichvu.KhuyenMaiMuaTang;
import dichvu.KhuyenMaiPhanTram;

import java.util.List;
import sanpham.QuanLyTonKho;
import sanpham.SanPham;
import utils.Validation;
public class CTHoaDonBan implements TrongFile {
    private String maSanPham;
    private String tenSanPham;
    private  int soLuong;
    private  long donGia;
    private long thanhTienSauKhuyenMai;
    private int soLuongTangKem;
    private int soLuongPhaiTra;
    private boolean hangKhuyenMai;
    private boolean kemHangChoTang;
    private long soTienGiam;

    // constructor
    public CTHoaDonBan() {
        this.maSanPham = "";
        this.soLuong = 0;
        this.donGia = 0;
        this.thanhTienSauKhuyenMai = 0;
        this.soTienGiam = 0;
        this.soLuongTangKem = 0;
        this.soLuongPhaiTra = 0;
        this.hangKhuyenMai = false;
        this.kemHangChoTang = false;
    }

    public CTHoaDonBan(String maSanPham, String tenSanPham, int soLuong, long donGia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.soTienGiam = 0;
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
        this.soLuongTangKem =0;
        this.soLuongPhaiTra=0;
        this.kemHangChoTang = false;
        this.hangKhuyenMai = false;
    }

    // methods
    public void nhapThongTinBan(Scanner sc, List<SanPham> danhSachSanPham,DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai dsKhuyenMai) {
        while (true) {
            this.maSanPham = Validation.inputStringRegex(sc, "Nhập mã sản phẩm (VD: SP01): ", Validation.MaSP_REGEX, "Lỗi: Mã SP phải bắt đầu bằng SP và theo sau là số!");
            SanPham sanPham = QuanLyTonKho.timSanPham(danhSachSanPham, maSanPham);
            if (sanPham == null) {
                System.out.println("[!] Mã sản phẩm không có trong danh mục/tồn kho.");
                continue;
            }
            this.tenSanPham = sanPham.getTenSanPham();
            this.donGia = sanPham.getGiaBan();
            int soLuongConLai = sanPham.getSoLuongTonKho();
            do {
                this.soLuong = Validation.inputInt(sc, "Nhập số lượng (còn " + soLuongConLai + "): ");
                if (this.soLuong == 0 || this.soLuong > soLuongConLai) {
                    System.out.println("[!] Số lượng phải từ 1 đến " + soLuongConLai + ".");
                }
            } while (this.soLuong == 0 || this.soLuong > soLuongConLai);

            int indexSPKM = danhSachSPKM.timViTri(maSanPham);
            if (indexSPKM >= 0) {
                String maKM = danhSachSPKM.getSanPhamKhuyenMais()[indexSPKM].getMaKhuyenMai();
                int indexKM = dsKhuyenMai.traCuuThongTinKhuyenMai(maKM);
                if (indexKM>=0 && dsKhuyenMai.getKhuyenmais()[indexKM] instanceof KhuyenMaiPhanTram pt && (this.donGia*this.soLuong) >= pt.getMucChiToiThieu()) {
                    this.soLuongTangKem = 0;
                    this.soTienGiam = (long)(this.donGia*this.soLuong*(pt.getPhanTramGiamGia()/100));
                    if (this.soTienGiam>pt.getMucGiamToiDa()) {
                        this.soTienGiam = pt.getMucGiamToiDa();
                    }
                }
                else if (indexKM>=0 && dsKhuyenMai.getKhuyenmais()[indexKM] instanceof KhuyenMaiMuaTang ch && this.soLuong >= ch.getSoLuongMuaToiThieu()) {
                    this.soTienGiam = 0;
                    this.soLuongTangKem = (this.soLuong/(ch.getSoLuongMuaToiThieu()+ch.getSoLuongTang()))*ch.getSoLuongTang();
                }
                // TODO: Hiện tại chưa có khuyễn mãi trực tiếp!
            }
            else{
                this.soTienGiam = 0;
                this.soLuongTangKem = 0;
            }
            this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
            this.hangKhuyenMai = isHangKhuyenMai();
            this.kemHangChoTang = isKemHangChoTang();
            return;
        }
    }

    private long tinhThanhTienSauKhuyenMai() {
        if (this.soLuongTangKem >0) {
            this.soLuongPhaiTra = this.soLuong - this.soLuongTangKem;
            if (soLuongPhaiTra<0) {
                this.soLuongPhaiTra = 0;
            }
        }
        else{
            this.soLuongPhaiTra = this.soLuong;
        }
        long tienGoc = soLuongPhaiTra * donGia;
        return tienGoc - this.soTienGiam;
    }

    public long tinhThanhTien() {
        return thanhTienSauKhuyenMai;
    }

    public void hienThiChiTiet(){
        System.out.println("Mã sản phẩm: "+this.getMaSanPham());
        System.out.println("Tên sản phẩm: "+this.getTenSanPham());
        System.out.println("Số lượng: "+this.getSoLuong());
        System.out.println("Đơn giá: "+this.getDonGia());
        System.out.println("Số lượng phải trả: "+this.getSoLuongPhaiTra());
        System.out.println("Số tiền được giảm: "+this.getSoTienGiam());
        System.out.println("Thành tiền: "+this.getThanhTienSauKhuyenMai());

    }

    // getters - setters
    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
    }

    public long getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
    }


    public boolean isHangKhuyenMai() {
        return this.soTienGiam>0;
    }
    public boolean kemHangChoTang(){
        return this.soLuongTangKem>0;
    }

    public void setHangKhuyenMai(boolean hangKhuyenMai) {
        this.hangKhuyenMai = hangKhuyenMai;
    }

    public long getThanhTienSauKhuyenMai() {
        return thanhTienSauKhuyenMai;
    }

    public void setThanhTienSauKhuyenMai(long thanhTienSauKhuyenMai) {
        if (thanhTienSauKhuyenMai < 0) {
            throw new IllegalArgumentException("Thành tiền sau khuyến mãi không được âm.");
        }
        this.thanhTienSauKhuyenMai = thanhTienSauKhuyenMai;
    }

    public int getSoLuongTangKem() {
        return soLuongTangKem;
    }

    public void setSoLuongTangKem(int soLuongTangKem) {
        this.soLuongTangKem = soLuongTangKem;
    }

    public boolean isKemHangChoTang() {
        return kemHangChoTang;
    }

    public void setKemHangChoTang(boolean kemHangChoTang) {
        this.kemHangChoTang = kemHangChoTang;
    }

    public long getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(long soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    public int getSoLuongPhaiTra() {
        return soLuongPhaiTra;
    }

    public void setSoLuongPhaiTra(int soLuongPhaiTra) {
        this.soLuongPhaiTra = soLuongPhaiTra;
    }

    

}
