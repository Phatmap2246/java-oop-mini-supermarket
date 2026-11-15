package hoadon;

import utils.DongDuLieu;

import utils.Validation;

import java.time.LocalDateTime;

public abstract class HoaDon implements interfaces.BanGhiFile {
    private String maHoaDon;
    private LocalDateTime ngayLap;
    private String maNhanVien ;
    private String loaiHoaDon ;
    private static int soThuTu = 1;
    
    private static void capNhatSoThuTuTuMa(String ma) {
        if (ma != null && !ma.isEmpty()) {
            soThuTu = Math.max(soThuTu, DongDuLieu.maKeTiep(ma, "(?i)HD(B|N)?\\d+"));
        }
    }

    protected HoaDon(String maNhanVien, String loaiMacDinh) {
        this(maNhanVien);
        this.loaiHoaDon = loaiMacDinh;
    }

    public HoaDon( String maNhanVien ) {
        this.ngayLap = LocalDateTime.now(); 
        this.maNhanVien = maNhanVien ;
        this.maHoaDon = Validation.taoMa("HD%03d" , soThuTu) ;
        loaiHoaDon = "" ;
        soThuTu++ ;
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
        } capNhatSoThuTuTuMa(maHoaDon);
        this.maHoaDon = maHoaDon; }

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
        return utils.HienThi.canLe("Mã hoá đơn: " + maHoaDon + " | Mã nhân viên:" + maNhanVien + " | Ngày lập: " + ngayLap + " | Loại hoá đơn: " + loaiHoaDon);
    }

    // ================= ĐỌC / GHI FILE TXT =================

    protected String thongTinHoaDonFile() {
        return DongDuLieu.ghep(maHoaDon, ngayLap, maNhanVien, loaiHoaDon);
    }

    protected void docThongTinHoaDonFile(String[] p) {
        maHoaDon = p[1];
        ngayLap = DongDuLieu.thoiGian(p[2]);
        maNhanVien = p[3];
        loaiHoaDon = p[4];
    }

    public static HoaDon tuDongFile(String dong) {
        String loai = dong.split(";", -1)[0];
        if ("HDBAN".equals(loai)) return HoaDonBanHang.tuDongFile(dong);
        if ("HDNHAP".equals(loai)) return HoaDonNhapHang.tuDongFile(dong);
        throw new IllegalArgumentException("Loại HoaDon không hợp lệ: " + loai);
    }

    public void kiemTraDuLieuFile() {
        DongDuLieu.maKeTiep(maHoaDon, "(?i)HD(B|N)?\\d+");
        DongDuLieu.yeuCau(ngayLap != null, "Thiếu ngày lập hóa đơn.");
    }

    public static void datBoDemFile(int value) {
        DongDuLieu.yeuCau(value > 0, "Bộ đếm mã phải dương.");
        soThuTu = value;
    }

    public void capNhatMaFile() {
        soThuTu = Math.max(soThuTu, DongDuLieu.maKeTiep(maHoaDon, "(?i)HD(B|N)?\\d+"));
    }
}
