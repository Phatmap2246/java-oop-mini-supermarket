package dichvu;

import interfaces.IThongTin;
import utils.Validation;
import java.time.LocalDate;
import java.util.Scanner;

public abstract class Khuyenmai implements IThongTin {
    private boolean tonTai = true;

    public boolean isTonTai() { return tonTai; }

    public void setTonTai(boolean tonTai) { this.tonTai = tonTai; }

    private String maKhuyenMai;
    private String loaiKhuyenMai;
    private LocalDate ngayBatDau, ngayKetThuc;
    private long mucChiToiThieu;
    private static int soThuTu = 1;

    private static void capNhatSoThuTuTuMa(String ma) {
        if (ma != null && ma.matches("(?i)^KM\\d+$")) {
            try {
                int num = Integer.parseInt(ma.substring(2));
                if (num >= soThuTu) {
                    soThuTu = num + 1;
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public Khuyenmai() {
        maKhuyenMai = Validation.taoMa("KM%03d", Khuyenmai.getSoThuTu());
        loaiKhuyenMai = "";
        ngayBatDau = ngayKetThuc = null;
        mucChiToiThieu = 0;
        soThuTu++;
    }

    protected Khuyenmai(String loaiMacDinh) {
        this();
        this.loaiKhuyenMai = loaiMacDinh;
    }

    public Khuyenmai(String maKhuyenMai, String loaiKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc, long mucChiToiThieu, int soThuTu) {
        if (mucChiToiThieu < 0) {
            throw new IllegalArgumentException("MucChiToiThieu không được âm.");
        }
        if (ngayBatDau != null && ngayKetThuc != null && ngayKetThuc.isBefore(ngayBatDau)) {
            throw new IllegalArgumentException("Ngày kết thúc không được bé hơn ngày bắt đầu.");
        }
        this.maKhuyenMai = maKhuyenMai;
        this.loaiKhuyenMai = loaiKhuyenMai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.mucChiToiThieu = mucChiToiThieu;
        if (soThuTu > Khuyenmai.soThuTu) {
            Khuyenmai.soThuTu = soThuTu;
        }
        capNhatSoThuTuTuMa(maKhuyenMai);
    }

    public void setKM(Scanner sc) {
        ngayBatDau = Validation.inputLocalDateRegex(sc, "Nhập ngày bắt đầu(dd/MM/yyyy): ");
        ngayKetThuc = Validation.inputLocalDateRegex(sc, "Nhập ngày kết thúc(dd/MM/yyyy): ");
        while (true) {
            if (ngayKetThuc.isBefore(ngayBatDau)) {
                System.out.println("Ngày kết thúc không được sớm hơn ngày bắt đầu!");
                ngayKetThuc = Validation.inputLocalDateRegex(sc, "Nhập ngày kết thúc(dd/MM/yyyy): ");
            } else break;
        }
        mucChiToiThieu = Validation.inputLong(sc, "Mức chi tiêu tối thiểu để sử dụng khuyến mãi: ");
    }

    @Override
    public void nhapThongTin(Scanner sc) {
        setKM(sc);
    }

    public static int getSoThuTu() {
        return soThuTu;
    }

    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public long getMucChiToiThieu() {
        return mucChiToiThieu;
    }

    public String getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setLoaiKhuyenMai(String loaiKhuyenMai) {
        if (loaiKhuyenMai == null || loaiKhuyenMai.isBlank()) {
            throw new IllegalArgumentException("LoaiKhuyenMai không được để trống.");
        }
        this.loaiKhuyenMai = loaiKhuyenMai;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        if (ngayBatDau == null) {
            throw new IllegalArgumentException("ngayBatDau không được null.");
        }
        if (ngayKetThuc != null && ngayBatDau.isAfter(ngayKetThuc))
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kêt thúc!");
        this.ngayBatDau = ngayBatDau;
    }

    public void setMucChiToiThieu(long mucChiToiThieu) {
        if (mucChiToiThieu < 0) {
            throw new IllegalArgumentException("MucChiToiThieu không được âm.");
        }
        this.mucChiToiThieu = mucChiToiThieu;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        if (ngayKetThuc == null) {
            throw new IllegalArgumentException("ngayKetThuc không được null.");
        }
        if (ngayBatDau != null && ngayKetThuc.isBefore(ngayBatDau))
            throw new IllegalArgumentException("Ngày kết thúc không được bé hơn ngày bắt đầu!!");
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        if (maKhuyenMai == null || !maKhuyenMai.matches("(?i)KM\\d+")) {
            throw new IllegalArgumentException("MaKhuyenMai không đúng định dạng.");
        }
        this.maKhuyenMai = maKhuyenMai;
        capNhatSoThuTuTuMa(maKhuyenMai);
    }

    public boolean kiemTraHetHanKhuyenMai() {
        return tonTai && ngayKetThuc != null && LocalDate.now().isAfter(ngayKetThuc);
    }

    public boolean dangHoatDong(LocalDate ngay) {
        return tonTai && ngay != null
                && ngayBatDau != null
                && ngayKetThuc != null
                && !ngay.isBefore(ngayBatDau)
                && !ngay.isAfter(ngayKetThuc);
    }

    public boolean chuaHoatDong() {
        return tonTai && ngayBatDau != null && LocalDate.now().isBefore(ngayBatDau);
    }

    // Đa hình tính toán giảm giá cho từng loại khuyến mãi
    public long tinhTienGiam(long donGia, int soLuong) {
        return 0;
    }

    public int tinhSoLuongTang(int soLuong) {
        return 0;
    }

    @Override
    public String toString() {
        return "Mã khuyến mãi: " + maKhuyenMai + " | Loại khuyến mãi: " + loaiKhuyenMai + " | Ngày bắt đầu: " + ngayBatDau + " | Ngày kết thúc: " + ngayKetThuc + " | Mức chi tiêu tối thiểu: " + mucChiToiThieu;
    }
}
