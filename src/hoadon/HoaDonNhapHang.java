package hoadon;

import dichvu.Danhsachdoitac;
import dichvu.Doitac;
import dichvu.Donvivanchuyen;
import dichvu.Nhacungcap;
import dichvu.Xe;
import java.util.ArrayList;
import java.util.Scanner;
import utils.Validation;

public class HoaDonNhapHang extends HoaDon {
    private String maNhaCungCap;
    private long chiPhiVanChuyen;
    private String maDonViVanChuyen;
    private String maXeVanChuyen;
    private String loaiXeVanChuyen;
    private String trangThai;
    private double thueVAT; // Lưu ý VAT lưu dạng 10 (10%)

    public static final String TRANG_THAI_KHOI_TAO = "Khởi tạo";
    public static final String TRANG_THAI_CHO_NCC_BAO_PHI = "Chờ NCC báo phí";
    public static final String TRANG_THAI_CHO_QUAN_LY_DUYET = "Chờ Quản lý duyệt";
    public static final String TRANG_THAI_DA_DUYET = "Đã duyệt, đã nhập kho";

    // Mã hóa đơn nhập: Bắt đầu bằng chữ "HDN" (in hoa) và theo sau là các chữ số. VD: HDN001
    public static final String MA_HD_NHAP_REGEX = "(?i)^HDN\\d+$";

    // Mã nhà cung cấp: Bắt đầu bằng chữ "NCC" (in hoa) và theo sau là các chữ số. VD: NCC01
    public static final String MA_NCC_REGEX = "(?i)^NCC\\d+$";

    // Constructors
    public HoaDonNhapHang() {
        super();
        this.maNhaCungCap = "";
        this.thueVAT = 0;
        this.chiPhiVanChuyen = 0;
        this.maDonViVanChuyen = "";
        this.maXeVanChuyen = "";
        this.loaiXeVanChuyen = "";
        this.trangThai = TRANG_THAI_KHOI_TAO;
    }

    public HoaDonNhapHang(String maHoaDon, String tenNhaCungCap, String maNhaCungCap, long chiPhiVanChuyen, double thueVAT) {
        super(maHoaDon, tenNhaCungCap);
        this.maNhaCungCap = maNhaCungCap;
        this.chiPhiVanChuyen = chiPhiVanChuyen;
        this.thueVAT = thueVAT;
        this.maDonViVanChuyen = "";
        this.maXeVanChuyen = "";
        this.loaiXeVanChuyen = "";
        this.trangThai = chiPhiVanChuyen > 0
                ? TRANG_THAI_CHO_QUAN_LY_DUYET
                : TRANG_THAI_KHOI_TAO;
    }

    // Override
    @Override 
    public long tinhTongTien(){
        long tongTienHang = 0;
        for(CTHoaDon ct : danhSachChiTiet){
            tongTienHang += ct.tinhThanhTien();
        }
        long tienThue = (long)((tongTienHang * thueVAT) / 100); 
        // ĐÃ SỬA: Cộng thêm chi phí vận chuyển
        return tongTienHang + tienThue + chiPhiVanChuyen; 
    }

    @Override
    public void hienThiHoaDon() {
        System.out.println("\n\n==========================================================================================");
        System.out.println("                                    HÓA ĐƠN NHẬP HÀNG                                      ");
        System.out.println("==========================================================================================");
        System.out.println("Mã HĐ: " + maHoaDon + " | Ngày nhập: " + ngayLap);
        System.out.println("Nhà cung cấp: " + tenDoiTac + " (Mã: " + maNhaCungCap + ")");
        System.out.println("Thuế VAT áp dụng: " + thueVAT + "%");
        System.out.println("Trạng thái: " + trangThai);
        if (!maXeVanChuyen.isEmpty()) {
            System.out.println("Đơn vị vận chuyển: " + maDonViVanChuyen
                    + " | Xe: " + maXeVanChuyen + " - " + loaiXeVanChuyen);
        }
        System.out.println("------------------------------------------------------------------------------------------");
        
        
        System.out.printf("%-5s | %-25s | %-5s | %15s | %15s\n", 
                "STT", "Tên Hàng Hóa", "SL", "Giá Nhập", "T.Tiền");
        System.out.println("------------------------------------------------------------------------------------------");

        int stt = 1;
        for (CTHoaDon ct : danhSachChiTiet) {
            System.out.printf("%-5d | %-25s | %-5d | %,15d | %,15d |\n",
                    stt, ct.getTenSanPham(), ct.getSoLuongTonKho(), ct.getDonGia(), ct.tinhThanhTien());
            stt++;
        }

        System.out.println("------------------------------------------------------------------------------------------");
        
        long tienHang = 0;
        for (CTHoaDon ct : danhSachChiTiet) tienHang += ct.tinhThanhTien();
        long tienThue = (long)(tienHang * thueVAT / 100.0);

        // SỬA: Căn lề phải (bỏ dấu trừ) và đẩy lề sang phải cho đẹp
        System.out.printf("%70s %15d\n", "Tiền hàng:", tienHang);
        System.out.printf("%70s %15d\n", "Tiền thuế VAT:", tienThue);
        System.out.printf("%70s %15d\n", "Phí vận chuyển:", chiPhiVanChuyen);
        System.out.printf("%70s %15d\n", "TỔNG CỘNG PHẢI TRẢ:", tinhTongTien());
        
        System.out.println("==========================================================================================");
        System.out.println(); // Thêm dòng trống để phân biệt hóa đơn
    }

    public void khoiTaoThongTinChung(Scanner sc, Nhacungcap nhaCungCap) {
        System.out.println("--- BƯỚC 1: KHỞI TẠO PHIẾU NHẬP (QUẢN KHO) ---");
        this.maHoaDon = Validation.inputStringRegex(sc,
                "Nhập mã HDN (VD: HDN001): ", MA_HD_NHAP_REGEX, "Lỗi: Sai định dạng!");
        this.maNhaCungCap = nhaCungCap.getMaDoiTac();
        this.tenDoiTac = nhaCungCap.getTenDoiTac();
        System.out.println("Nhà cung cấp đã chọn: " + tenDoiTac + " (" + maNhaCungCap + ")");
        this.thueVAT = Validation.inputDouble(sc, "Nhập thuế VAT (%): ");
    }

    public void quanKhoNhapChiTiet(Scanner sc) {
        System.out.println("--- BƯỚC 2: KIỂM ĐẾM SẢN PHẨM NHẬP (QUẢN KHO) ---");
        while (true) {
            CTHoaDon ct = new CTHoaDon();
            ct.nhapThongTin(sc, false); 
            this.themChiTiet(ct); 
            
            if (Validation.inputInt(sc, "Nhập tiếp sản phẩm? (1: Có / 0: Không): ") == 0) {
                break;
            }
        }
        this.trangThai = TRANG_THAI_CHO_NCC_BAO_PHI;
    }

    public boolean nhaCungCapKhaiBaoVanChuyen(Scanner sc, Danhsachdoitac danhSachDoiTac) {
        if (!TRANG_THAI_CHO_NCC_BAO_PHI.equals(trangThai)) {
            System.out.println("[!] Hóa đơn không ở trạng thái chờ NCC báo phí.");
            return false;
        }

        System.out.println("--- BƯỚC 3: XÁC NHẬN VẬN CHUYỂN (NHÀ CUNG CẤP) ---");
        ArrayList<Donvivanchuyen> donViVanChuyens = new ArrayList<>();
        ArrayList<Xe> cacXe = new ArrayList<>();
        for (Doitac doiTac : danhSachDoiTac.getDanhSachDoiTac()) {
            if (!(doiTac instanceof Donvivanchuyen)) {
                continue;
            }
            Donvivanchuyen donVi = (Donvivanchuyen) doiTac;
            if (donVi.getDanhSachXe() == null) {
                continue;
            }
            for (Xe xe : donVi.getDanhSachXe()) {
                donViVanChuyens.add(donVi);
                cacXe.add(xe);
            }
        }

        if (cacXe.isEmpty()) {
            System.out.println("[!] Chưa có xe nào được đăng ký trong danh sách đơn vị vận chuyển.");
            return false;
        }

        System.out.println("Chọn xe vận chuyển:");
        for (int i = 0; i < cacXe.size(); i++) {
            Donvivanchuyen donVi = donViVanChuyens.get(i);
            Xe xe = cacXe.get(i);
            System.out.println((i + 1) + ". " + donVi.getTenDoiTac()
                    + " (" + donVi.getMaDoiTac() + ") | "
                    + xe.getMaXe() + " - " + xe.getTenXe() + " | Loại: " + xe.getLoaiXe());
        }

        int luaChon = Validation.luaChon(sc, "Chọn xe: ", 1, cacXe.size()) - 1;
        Donvivanchuyen donViChon = donViVanChuyens.get(luaChon);
        Xe xeChon = cacXe.get(luaChon);
        long chiPhi = Validation.inputLong(sc, "Nhập chi phí vận chuyển (VND): ");

        this.maDonViVanChuyen = donViChon.getMaDoiTac();
        this.maXeVanChuyen = xeChon.getMaXe();
        this.loaiXeVanChuyen = xeChon.getTenXe() + " - " + xeChon.getLoaiXe();
        this.chiPhiVanChuyen = chiPhi;
        this.trangThai = TRANG_THAI_CHO_QUAN_LY_DUYET;
        System.out.println("[+] Đã cập nhật xe và phí vận chuyển.");
        return true;
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public double getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(double thueVAT) {
        this.thueVAT = thueVAT;
    }

// Getter/Setter cho chi phí vận chuyển
    public long getChiPhiVanChuyen() {
        return chiPhiVanChuyen;
    }

    public void setChiPhiVanChuyen(long chiPhiVanChuyen) {
        this.chiPhiVanChuyen = chiPhiVanChuyen;
    }

    public String getMaDonViVanChuyen() {
        return maDonViVanChuyen;
    }

    public String getMaXeVanChuyen() {
        return maXeVanChuyen;
    }

    public String getLoaiXeVanChuyen() {
        return loaiXeVanChuyen;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setThongTinVanChuyen(String maDonViVanChuyen, String maXeVanChuyen,
            String loaiXeVanChuyen, String trangThai) {
        this.maDonViVanChuyen = maDonViVanChuyen;
        this.maXeVanChuyen = maXeVanChuyen;
        this.loaiXeVanChuyen = loaiXeVanChuyen;
        this.trangThai = trangThai;
    }

    public boolean xacNhanDuyetNhapKho() {
        if (!TRANG_THAI_CHO_QUAN_LY_DUYET.equals(trangThai)) {
            System.out.println("[!] Hóa đơn không ở trạng thái chờ quản lý duyệt.");
            return false;
        }
        this.trangThai = TRANG_THAI_DA_DUYET;
        return true;
    }
}