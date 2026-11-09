package hoadon;
import java.util.Scanner;
import java.util.List;
import sanpham.QuanLyTonKho;
import sanpham.SanPham;
import utils.Validation;
public class CTHoaDon {
    private String maSanPham;
    private String tenSanPham;
    private  int soLuong;
    private  long donGia;
    private long thanhTienSauKhuyenMai;
    private double chietKhau;
    private boolean hangKhuyenMai;

    // TODO: Tạm thời khai báo ở đây, sau này class SanPham có thể chuyển sang đó
    public static final String MA_SP_REGEX = "^SP\\d+$";
    //

    // constructor
    public CTHoaDon() {
        this.maSanPham = "";
        this.tenSanPham = "";
        this.soLuong = 0;
        this.donGia = 0;
        this.thanhTienSauKhuyenMai = 0;
        this.chietKhau = 0.0;
        this.hangKhuyenMai = false;
    }

    public CTHoaDon(String maSanPham, String tenSanPham, int soLuong,
            long donGia, double chietKhau) {
        this(maSanPham, tenSanPham, soLuong, donGia, chietKhau, false);
    }

    public CTHoaDon(String maSanPham, String tenSanPham, int soLuong,
            long donGia, double chietKhau, boolean hangKhuyenMai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.chietKhau = chietKhau;
        this.hangKhuyenMai = hangKhuyenMai;
        this.thanhTienSauKhuyenMai = tinhThanhTienTheoChietKhauCu();
    }

    public CTHoaDon(String maSanPham, String tenSanPham, int soLuong,
            long donGia, long donGiaSauKhuyenMai, double chietKhau) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTienSauKhuyenMai = (long) soLuong * donGiaSauKhuyenMai;
        this.chietKhau = chietKhau;
        this.hangKhuyenMai = false;
    }

    public CTHoaDon(String maSanPham, String tenSanPham, int soLuong, long donGia,
            double chietKhau, boolean hangKhuyenMai, long thanhTienSauKhuyenMai) {
        this(maSanPham, tenSanPham, soLuong, donGia, chietKhau, hangKhuyenMai);
        setThanhTienSauKhuyenMai(thanhTienSauKhuyenMai);
    }

    public CTHoaDon(String maSanPham, String tenSanPham, int soLuong,
            long donGia, long donGiaSauKhuyenMai,
            double chietKhau, boolean hangKhuyenMai) {
        this(maSanPham, tenSanPham, soLuong, donGia, donGiaSauKhuyenMai, chietKhau);
        this.hangKhuyenMai = hangKhuyenMai;
    }

    // methods
    public void nhapThongTin(Scanner sc, boolean isBanHang){
        this.maSanPham = Validation.inputStringRegex(sc, "Nhập mã sản phẩm (VD: SP01): ", MA_SP_REGEX, "Lỗi: Mã SP phải bắt đầu bằng SP in hoa và theo sau là số!");
        System.out.print("Nhập tên sản phẩm: ");
        this.tenSanPham = sc.nextLine();
        do {
            this.soLuong = Validation.inputInt(sc, "Nhập số lượng: ");
            if (this.soLuong == 0) {
                System.out.println("[!] Số lượng phải lớn hơn 0.");
            }
        } while (this.soLuong == 0);
        do {
            this.donGia = Validation.inputLong(sc, "Nhập đơn giá: ");
            if (this.donGia == 0) {
                System.out.println("[!] Đơn giá phải lớn hơn 0.");
            }
        } while (this.donGia == 0);
        this.chietKhau = 0;
        this.thanhTienSauKhuyenMai = (long) this.soLuong * this.donGia;
        this.hangKhuyenMai = false;
    }

    public void nhapThongTinBan(Scanner sc, List<SanPham> danhSachSanPham,
            List<CTHoaDon> chiTietDaNhap) {
        while (true) {
            this.maSanPham = Validation.inputStringRegex(sc,
                    "Nhập mã sản phẩm (VD: SP01): ", MA_SP_REGEX,
                    "Lỗi: Mã SP phải bắt đầu bằng SP và theo sau là số!");
            SanPham sanPham = QuanLyTonKho.timSanPham(danhSachSanPham, maSanPham);
            if (sanPham == null) {
                System.out.println("[!] Mã sản phẩm không có trong danh mục/tồn kho.");
                continue;
            }

            int daNhap = QuanLyTonKho.soLuongDaCo(chiTietDaNhap, maSanPham);
            int conLai = sanPham.getSoLuongTonKho() - daNhap;
            if (conLai <= 0) {
                System.out.println("[!] Sản phẩm đã hết số lượng có thể bán.");
                continue;
            }

            this.tenSanPham = sanPham.getTenSanPham();
            this.donGia = sanPham.getGiaBan();
            do {
                this.soLuong = Validation.inputInt(sc,
                        "Nhập số lượng (còn " + conLai + "): ");
                if (this.soLuong == 0 || this.soLuong > conLai) {
                    System.out.println("[!] Số lượng phải từ 1 đến " + conLai + ".");
                }
            } while (this.soLuong == 0 || this.soLuong > conLai);

            this.chietKhau = 0;
            this.thanhTienSauKhuyenMai = (long) this.soLuong * this.donGia;
            this.hangKhuyenMai = false;
            return;
        }
    }

    private long tinhThanhTienTheoChietKhauCu() {
        long tienGoc = (long) soLuong * donGia;
        long tienGiam = (long) (tienGoc * (chietKhau / 100.0));
        return tienGoc - tienGiam;
    }

    public long tinhThanhTien() {
        return thanhTienSauKhuyenMai;
    }

    public void hienThiChiTiet(){
        String tenHienThi = hangKhuyenMai ? tenSanPham + " (TẶNG)" : tenSanPham;
        System.out.printf("  - Mã SP: %-10s | Tên: %-20s | SL: %-5d | Đơn giá gốc: %-10d | Thành tiền sau KM: %d\n",
                maSanPham, tenHienThi, soLuong, donGia, tinhThanhTien());
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

    public int getSoLuongTonKho() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        this.thanhTienSauKhuyenMai = tinhThanhTienTheoChietKhauCu();
    }

    public long getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
        this.thanhTienSauKhuyenMai = tinhThanhTienTheoChietKhauCu();
    }

    public double getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(double chietKhau) {
        this.chietKhau = chietKhau;
        this.thanhTienSauKhuyenMai = tinhThanhTienTheoChietKhauCu();
    }

    public boolean isHangKhuyenMai() {
        return hangKhuyenMai;
    }

    public void setHangKhuyenMai(boolean hangKhuyenMai) {
        this.hangKhuyenMai = hangKhuyenMai;
    }

    public static String getMaSpRegex() {
        return MA_SP_REGEX;
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

}
