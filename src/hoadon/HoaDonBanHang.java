package hoadon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.List;
import java.util.function.Predicate;
import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import dichvu.KhuyenMaiService;
import sanpham.QuanLySanPham;
import sanpham.QuanLyTonKho;
import sanpham.SanPham;
import utils.Validation;

public class HoaDonBanHang extends HoaDon {
    private long tienKhuyenMai;
    private boolean khuyenMaiPhanBoTheoChiTiet;
    private long chietKhauHoaDon;
    private double phanTramChietKhauHoaDon;
    private boolean chietKhauHoaDonTheoPhanTram;
    
    // SỬA: Chứa đối tượng thanh toán (Đa hình), KHÔNG implements IThanhToan nữa
    private IThanhToan phuongThucThanhToan; 
    private boolean daThanhToan;

    public static final String MA_HD_BAN_REGEX = "^HD\\d+$";

    public HoaDonBanHang(String maHoaDon, String tenDoiTac) {
        super(maHoaDon, tenDoiTac);
        this.tienKhuyenMai = 0;
        this.khuyenMaiPhanBoTheoChiTiet = true;
        this.chietKhauHoaDon = 0;
        this.phanTramChietKhauHoaDon = 0;
        this.chietKhauHoaDonTheoPhanTram = false;
        this.phuongThucThanhToan = null;
        this.daThanhToan = false;
    }

    public HoaDonBanHang() {
        super();
        this.tienKhuyenMai = 0;
        this.khuyenMaiPhanBoTheoChiTiet = true;
        this.chietKhauHoaDon = 0;
        this.phanTramChietKhauHoaDon = 0;
        this.chietKhauHoaDonTheoPhanTram = false;
        this.phuongThucThanhToan = null;
        this.daThanhToan = false;
    }

    @Override 
    public long tinhTongTien(){
        long tongHang = 0;
        for(CTHoaDon ct : danhSachChiTiet){
            tongHang += ct.tinhThanhTien();
        }
        long tienGiam = khuyenMaiPhanBoTheoChiTiet
                ? tinhTienChietKhauHoaDon(tongHang)
                : Math.min(tongHang, this.tienKhuyenMai);
        long thanhToan = tongHang - tienGiam;
        return (thanhToan > 0) ? thanhToan : 0; 
    }
   
    @Override 
    public void hienThiHoaDon(){
        System.out.println("\n\n==========================================================================================");
        System.out.println("                                    SIÊU THỊ MINI OOP                                     ");
        System.out.println("             Đ/c: 123 Đường Số 1, Q.1, TP.HCM                   ĐT: 0123.456.789          ");
        System.out.println("==========================================================================================");
        System.out.println("                                     HÓA ĐƠN BÁN LẺ                                       ");
        System.out.println("Ngày: " + ngayLap + " \t\t\t\t\t Số: " + maHoaDon);
        System.out.println("KH:   " + tenDoiTac);
        System.out.println("------------------------------------------------------------------------------------------");
        
        
        System.out.printf("%-5s | %-25s | %-5s | %15s | %15s\n", 
                "STT", "Tên Sản Phẩm", "SL", "Đơn Giá", "T.Tiền");
        System.out.println("------------------------------------------------------------------------------------------");

        int stt = 1;
        long tongHangGoc = 0;
        long tongSauKhuyenMaiSanPham = 0;
        for (CTHoaDon ct : danhSachChiTiet) {
            String tenHienThi = ct.isHangKhuyenMai()
                    ? ct.getTenSanPham() + " (TẶNG)" : ct.getTenSanPham();
            System.out.printf("%-5d | %-25s | %-5d | %15d | %15d\n",
                    stt, tenHienThi, ct.getSoLuongTonKho(), ct.getDonGia(), ct.tinhThanhTien());
            tongHangGoc += (long) ct.getSoLuongTonKho() * ct.getDonGia();
            tongSauKhuyenMaiSanPham += ct.tinhThanhTien();
            stt++;
        }

        System.out.println("------------------------------------------------------------------------------------------");

        System.out.printf("%70s %15d\n", "Tổng tiền hàng (giá gốc):", tongHangGoc);
        if (khuyenMaiPhanBoTheoChiTiet) {
            if (getTienKhuyenMai() > 0) {
                System.out.printf("%70s -%14d\n",
                        "Khuyến mãi sản phẩm:", getTienKhuyenMai());
            }
            System.out.printf("%70s %15d\n",
                    "Tạm tính sau khuyến mãi:", tongSauKhuyenMaiSanPham);
            long tienChietKhau = tinhTienChietKhauHoaDon(tongSauKhuyenMaiSanPham);
            if (tienChietKhau > 0) {
                System.out.printf("%70s -%14d\n", "Chiết khấu hóa đơn:", tienChietKhau);
            }
        } else if (getTienKhuyenMai() > 0) {
            System.out.printf("%70s -%14d\n", "Chiết khấu (dữ liệu cũ):", getTienKhuyenMai());
        }
        System.out.printf("%70s %15d\n", "TỔNG THANH TOÁN:", tinhTongTien());
        
        System.out.println("==========================================================================================");
        
        if (daThanhToan) {
            System.out.println(" TRẠNG THÁI: [X] ĐÃ THANH TOÁN");
            if (phuongThucThanhToan != null) {
                phuongThucThanhToan.inBienLai();
            }
        } else {
            System.out.println(" TRẠNG THÁI: [ ] CHƯA THANH TOÁN");
        }
        
        System.out.println("==========================================================================================");
        System.out.println("                          HÂN HẠNH ĐƯỢC PHỤC VỤ QUÝ KHÁCH                                ");
        System.out.println("==========================================================================================");
        System.out.println(); // Thêm dòng trống để phân biệt hóa đơn
    }

    // Hàm nhập dùng Validation
    public boolean nhapHoaDon(Scanner sc) {
        return nhapHoaDon(sc, new DanhSachSPKM(), new Danhsachkhuyenmai(),
                ma -> false, new QuanLySanPham().getDanhSachSanPham());
    }

    public boolean nhapHoaDon(Scanner sc, DanhSachSPKM danhSachSPKM,
            Danhsachkhuyenmai danhSachKhuyenMai, Predicate<String> maHoaDonDaTonTai) {
        return nhapHoaDon(sc, danhSachSPKM, danhSachKhuyenMai,
                maHoaDonDaTonTai, new QuanLySanPham().getDanhSachSanPham());
    }

    public boolean nhapHoaDon(Scanner sc, DanhSachSPKM danhSachSPKM,
            Danhsachkhuyenmai danhSachKhuyenMai, Predicate<String> maHoaDonDaTonTai,
            List<SanPham> danhSachSanPham) {
        if (danhSachSanPham == null || danhSachSanPham.isEmpty()) {
            System.out.println("[!] Chưa có sản phẩm trong danh mục/tồn kho để lập hóa đơn bán.");
            return false;
        }
        this.maHoaDon = Validation.inputStringRegex(sc,
            "Nhap ma hoa don (VD: HD001): ", 
            MA_HD_BAN_REGEX,
            "Loi: Ma HD phai bat dau bang HD va theo sau la so!");
        while (maHoaDonDaTonTai.test(this.maHoaDon)) {
            System.out.println("[!] Mã hóa đơn đã tồn tại.");
            this.maHoaDon = Validation.inputStringRegex(sc,
                    "Nhập mã hóa đơn khác: ", MA_HD_BAN_REGEX,
                    "Lỗi: Mã HD phải bắt đầu bằng HD và theo sau là số!");
        }
            
        this.tenDoiTac = Validation.inputStringRegex(sc, 
            "Nhap ten khach hang: ", 
            Validation.HOTEN_REGEX, 
            "Loi: Ten khach hang khong hop le!");

        System.out.println("--- Nhap danh sach san pham ---");
        while (true) {
            CTHoaDon ct = new CTHoaDon();
            ct.nhapThongTinBan(sc, danhSachSanPham, danhSachChiTiet);
            this.themChiTiet(ct);
            
            int tiepTuc = Validation.inputInt(sc, "Nhap tiep san pham? (1: Co / 0: Khong): ");
            if (tiepTuc == 0) {
                break;
            }
        }

        this.tienKhuyenMai = KhuyenMaiService.apDungKhuyenMai(
                this, danhSachSPKM, danhSachKhuyenMai, danhSachSanPham);
        if (!QuanLyTonKho.kiemTraDuTonKho(danhSachSanPham, danhSachChiTiet)) {
            return false;
        }
        if (this.tienKhuyenMai > 0) {
            System.out.println("[+] Đã áp dụng khuyến mãi: giảm "
                    + this.tienKhuyenMai + " VND.");
        }
        nhapChietKhauHoaDon(sc);

        System.out.println("\n--- CHỌN PHƯƠNG THỨC THANH TOÁN ---");
        System.out.println("1. Tiền mặt");
        System.out.println("2. Chuyển khoản ngân hàng");
        int chon = Validation.luaChon(sc, "Lựa chọn của bạn: ", 1, 2);

        if (chon == 1) {
            this.phuongThucThanhToan = new ThanhToanTienMat();
        } else {
            System.out.print("Nhập tên ngân hàng: ");
            String bank = sc.nextLine();
            System.out.print("Nhập số tài khoản: ");
            String stk = sc.nextLine();
            
            ThanhToanChuyenKhoan ck = new ThanhToanChuyenKhoan(bank, stk);
            // SỬA: TẠO MÃ QR TRƯỚC KHI THANH TOÁN
            ck.taoMaQR(tinhTongTien());
            this.phuongThucThanhToan = ck;
        }

        // Gọi hàm thanh toán (Đa hình)
         this.daThanhToan = this.phuongThucThanhToan.thucHienThanhToan(sc, tinhTongTien());
        
        if (this.daThanhToan) {
            System.out.println("\n[+] Thanh toán thành công!");
            System.out.println("=================================================");
            System.out.println("         IN HÓA ĐƠN (ĐÃ THANH TOÁN)              ");
            System.out.println("=================================================");
            this.hienThiHoaDon(); // TỰ ĐỘNG IN HÓA ĐƠN LUÔN Ở ĐÂY
        } else {
            System.out.println("[!] Thanh toán thất bại (Khách đưa thiếu tiền)!");
        }
        return true;
    }
    // getters - setters
    public long getTienKhuyenMai() {
        if (khuyenMaiPhanBoTheoChiTiet) {
            return tienKhuyenMai;
        }
        long tongHang = 0;
        for (CTHoaDon ct : danhSachChiTiet) {
            tongHang += ct.tinhThanhTien();
        }
        return Math.min(tongHang, tienKhuyenMai);
    }
    public void setTienKhuyenMai(long tienKhuyenMai) {
        this.tienKhuyenMai = Math.max(0, tienKhuyenMai);
    }

    private void nhapChietKhauHoaDon(Scanner sc) {
        System.out.println("\n--- CHIẾT KHẤU HÓA ĐƠN ---");
        System.out.println("0. Không chiết khấu");
        System.out.println("1. Giảm số tiền cố định");
        System.out.println("2. Giảm theo phần trăm");
        int luaChon = Validation.luaChon(sc, "Chọn loại chiết khấu: ", 0, 2);

        if (luaChon == 1) {
            long tamTinh = tinhTongSauKhuyenMaiSanPham();
            long soTien;
            do {
                soTien = Validation.inputLong(sc, "Nhập số tiền chiết khấu (VND): ");
                if (soTien > tamTinh) {
                    System.out.println("[!] Chiết khấu không được lớn hơn tạm tính.");
                }
            } while (soTien > tamTinh);
            setChietKhauHoaDonSoTien(soTien);
        } else if (luaChon == 2) {
            double phanTram;
            do {
                phanTram = Validation.inputDouble(sc, "Nhập phần trăm chiết khấu (0-100): ");
                if (!Double.isFinite(phanTram) || phanTram > 100) {
                    System.out.println("[!] Phần trăm chiết khấu phải từ 0 đến 100.");
                }
            } while (!Double.isFinite(phanTram) || phanTram > 100);
            setChietKhauHoaDonPhanTram(phanTram);
        } else {
            setChietKhauHoaDonSoTien(0);
        }

        long tienChietKhau = tinhTienChietKhauHoaDon(tinhTongSauKhuyenMaiSanPham());
        if (tienChietKhau > 0) {
            System.out.println("[+] Chiết khấu hóa đơn: " + tienChietKhau + " VND.");
        }
    }

    private long tinhTongSauKhuyenMaiSanPham() {
        long tongTien = 0;
        for (CTHoaDon chiTiet : danhSachChiTiet) {
            tongTien += chiTiet.tinhThanhTien();
        }
        return tongTien;
    }

    private long tinhTienChietKhauHoaDon(long tamTinh) {
        if (!khuyenMaiPhanBoTheoChiTiet || tamTinh <= 0) {
            return 0;
        }
        if (!chietKhauHoaDonTheoPhanTram) {
            return Math.min(tamTinh, chietKhauHoaDon);
        }
        long tienChietKhau = BigDecimal.valueOf(tamTinh)
                .multiply(BigDecimal.valueOf(phanTramChietKhauHoaDon))
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.DOWN)
                .longValueExact();
        return Math.min(tamTinh, tienChietKhau);
    }

    public String getKieuChietKhauHoaDon() {
        if (chietKhauHoaDonTheoPhanTram) {
            return "PERCENT";
        }
        return chietKhauHoaDon > 0 ? "AMOUNT" : "NONE";
    }

    public String getGiaTriChietKhauHoaDon() {
        return chietKhauHoaDonTheoPhanTram
                ? Double.toString(phanTramChietKhauHoaDon)
                : Long.toString(chietKhauHoaDon);
    }

    public void setChietKhauHoaDonSoTien(long soTien) {
        if (soTien < 0) {
            throw new IllegalArgumentException("Chiết khấu hóa đơn không được âm.");
        }
        this.chietKhauHoaDon = soTien;
        this.phanTramChietKhauHoaDon = 0;
        this.chietKhauHoaDonTheoPhanTram = false;
    }

    public void setChietKhauHoaDonPhanTram(double phanTram) {
        if (!Double.isFinite(phanTram) || phanTram < 0 || phanTram > 100) {
            throw new IllegalArgumentException("Phần trăm chiết khấu phải từ 0 đến 100.");
        }
        this.chietKhauHoaDon = 0;
        this.phanTramChietKhauHoaDon = phanTram;
        this.chietKhauHoaDonTheoPhanTram = true;
    }

    public long getSoTienChietKhauHoaDon() {
        return tinhTienChietKhauHoaDon(tinhTongSauKhuyenMaiSanPham());
    }

    public void setChietKhauHoaDon(String kieu, String giaTri) {
        if ("AMOUNT".equals(kieu)) {
            setChietKhauHoaDonSoTien(Long.parseLong(giaTri));
        } else if ("PERCENT".equals(kieu)) {
            setChietKhauHoaDonPhanTram(Double.parseDouble(giaTri));
        } else if ("NONE".equals(kieu)) {
            setChietKhauHoaDonSoTien(0);
        } else {
            throw new IllegalArgumentException("Loại chiết khấu hóa đơn không hợp lệ.");
        }
    }

    public boolean isKhuyenMaiPhanBoTheoChiTiet() {
        return khuyenMaiPhanBoTheoChiTiet;
    }

    public void setKhuyenMaiPhanBoTheoChiTiet(boolean khuyenMaiPhanBoTheoChiTiet) {
        this.khuyenMaiPhanBoTheoChiTiet = khuyenMaiPhanBoTheoChiTiet;
    }

    public IThanhToan getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(IThanhToan phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public boolean isDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(boolean daThanhToan) { this.daThanhToan = daThanhToan; }
}