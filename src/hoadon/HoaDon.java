package hoadon;

import utils.Validation;

import java.time.LocalDateTime;

public abstract class HoaDon {
    private String maHoaDon;
    private LocalDateTime ngayLap;
    private String maNhanVien;
    private String loaiHoaDon;
    private static int soThuTu = 1;

    private static void capNhatSoThuTuTuMa(String ma) {
        if (ma != null && ma.matches("(?i)^HD(B|N)?\\d+$")) {
            try {
                String digits = ma.replaceFirst("(?i)^HD(B|N)?", "");
                int num = Integer.parseInt(digits);
                if (num >= soThuTu) {
                    soThuTu = num + 1;
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    public HoaDon(String maNhanVien) {
        this.ngayLap = LocalDateTime.now();
        this.maNhanVien = maNhanVien;
        this.maHoaDon = Validation.taoMa("HD%03d", soThuTu);
        this.loaiHoaDon = "";
        soThuTu++;
    }

    protected HoaDon(String maNhanVien, String loaiMacDinh) {
        this(maNhanVien);
        this.loaiHoaDon = loaiMacDinh;
    }

    public HoaDon(String maHoaDon, LocalDateTime ngayLap, String maNhanVien, String loaiHoaDon) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.loaiHoaDon = loaiHoaDon;
        this.maNhanVien = maNhanVien;
        capNhatSoThuTuTuMa(maHoaDon);
    }

    public abstract long tinhTongTien();
    public abstract void hienThiHoaDon();

    public String getMaHoaDon() { return maHoaDon; }

    public void setMaHoaDon(String maHoaDon) {
        if (maHoaDon == null || !maHoaDon.matches("(?i)HD(B|N)?\\d+")) {
            throw new IllegalArgumentException("MaHoaDon không đúng định dạng.");
        }
        this.maHoaDon = maHoaDon;
        capNhatSoThuTuTuMa(maHoaDon);
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        if (ngayLap == null) {
            throw new IllegalArgumentException("ngayLap không được null.");
        }
        this.ngayLap = ngayLap;
    }

    public static int getSoThuTu() {
        return soThuTu;
    }

    public void setMaNhanVien(String maNhanVien) {
        if (maNhanVien == null || !maNhanVien.matches(Validation.MaNV_REGEX)) {
            throw new IllegalArgumentException("Mã nhân viên không hợp lệ.");
        }
        this.maNhanVien = maNhanVien;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public String getLoaiHoaDon() {
        return loaiHoaDon;
    }

    public void setLoaiHoaDon(String loaiHoaDon) {
        if (loaiHoaDon == null || loaiHoaDon.isBlank()) {
            throw new IllegalArgumentException("Loại hóa đơn không được rỗng.");
        }
        this.loaiHoaDon = loaiHoaDon;
    }

    @Override
    public String toString() {
        return "Mã hoá đơn: " + maHoaDon + " | Mã nhân viên:" + maNhanVien + " | Ngày lập: " + ngayLap + " | Loại hoá đơn: " + loaiHoaDon;
    }
}
