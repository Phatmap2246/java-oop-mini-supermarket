package dichvu ;

import utils.DongDuLieu;
import utils.Validation;
import java.time.LocalDate;
import java.util.Scanner;

public abstract class Khuyenmai implements interfaces.BanGhiFile {
    private boolean tonTai = true;

    public boolean isTonTai() { return tonTai; }

    public void setTonTai(boolean tonTai) { this.tonTai = tonTai; }

    private String maKhuyenMai ;
    private String loaiKhuyenMai ;
    private LocalDate ngayBatDau , ngayKetThuc ;
    private long mucChiToiThieu ;
    private static int soThuTu = 1 ;
    private static void capNhatSoThuTuTuMa(String ma) {
        if (ma != null && !ma.isEmpty()) {
            soThuTu = Math.max(soThuTu, DongDuLieu.maKeTiep(ma, "(?i)KM\\d+"));
        }
    }

    protected Khuyenmai(String loaiMacDinh) {
        this();
        this.loaiKhuyenMai = loaiMacDinh;
    }

    public Khuyenmai()
    {
        maKhuyenMai = loaiKhuyenMai = "" ;
        ngayBatDau = ngayKetThuc = null ;
        mucChiToiThieu = 0 ;
        maKhuyenMai = (Validation.taoMa("KM%03d" , Khuyenmai.getSoThuTu())) ;
        soThuTu++ ;
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
    public void setKM ( Scanner sc )
    {
        ngayBatDau = Validation.inputLocalDateRegex(sc , "Nhập ngày bắt đầu(dd/MM/yyyy): ") ;
        ngayKetThuc = Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc(dd/MM/yyyy): ");
        while (true)
        {
            if ( ngayKetThuc.isBefore(ngayBatDau))
            {
                System.out.println("Ngày kết thúc không được sớm hơn ngày bắt đầu!");
                ngayKetThuc = Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc(dd/MM/yyyy): ");
            }
            else break;
        }
        mucChiToiThieu = Validation.inputLong(sc, "Mức chi tiêu tối thiểu để sử dụng khuyến mãi: " );
    }

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
        if ( ngayKetThuc != null && ngayBatDau.isAfter(ngayKetThuc) )
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kêt thúc!") ;
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
        if ( ngayBatDau != null && ngayKetThuc.isBefore(ngayBatDau))
            throw new IllegalArgumentException("Ngày kết thúc không được bé hơn ngày bắt đầu!!") ;
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        if (maKhuyenMai == null || !maKhuyenMai.matches("(?i)KM\\d+")) {
            throw new IllegalArgumentException("MaKhuyenMai không đúng định dạng.");
        }
        capNhatSoThuTuTuMa(maKhuyenMai);
        this.maKhuyenMai = maKhuyenMai;
    }
    public boolean kiemTraHetHanKhuyenMai()
    {
        return tonTai && ngayKetThuc != null && LocalDate.now().isAfter(ngayKetThuc) ;
    }

    public boolean dangHoatDong(LocalDate ngay) {
        return tonTai && ngay != null
                && ngayBatDau != null
                && ngayKetThuc != null
                && !ngay.isBefore(ngayBatDau)
                && !ngay.isAfter(ngayKetThuc);
    }
    public boolean chuaHoatDong()
    {
        return tonTai && ngayBatDau != null && LocalDate.now().isBefore(ngayBatDau) ;
    }
    @Override
    public String toString() {
        return utils.HienThi.canLe("Mã khuyến mãi: " + maKhuyenMai + " | Loại khuyến mãi: " + loaiKhuyenMai + " | Ngày bắt đầu: " + ngayBatDau + " | Ngày kết thúc: " + ngayKetThuc +  " | Mức chi tiêu tối thiểu: " + mucChiToiThieu );
    }

    public long tinhTienGiam(long donGia, int soLuong) {
        return 0;
    }

    public int tinhSoLuongTang(int soLuong) {
        return 0;
    }

    // ================= ĐỌC / GHI FILE TXT =================

    protected String thongTinKhuyenMaiFile() {
        return DongDuLieu.ghep(maKhuyenMai, loaiKhuyenMai, ngayBatDau, ngayKetThuc, mucChiToiThieu, tonTai);
    }

    protected void docThongTinKhuyenMaiFile(String[] p) {
        maKhuyenMai = p[1];
        loaiKhuyenMai = p[2];
        ngayBatDau = DongDuLieu.ngay(p[3]);
        ngayKetThuc = DongDuLieu.ngay(p[4]);
        mucChiToiThieu = Long.parseLong(p[5]);
        tonTai = DongDuLieu.logic(p[6]);
    }

    public static Khuyenmai tuDongFile(String dong) {
        String loai = dong.split(";", -1)[0];
        if ("KMPHANTRAM".equals(loai)) return KhuyenMaiPhanTram.tuDongFile(dong);
        if ("KMTRUCTIEP".equals(loai)) return KhuyenMaiTrucTiep.tuDongFile(dong);
        if ("KMMUATANG".equals(loai)) return KhuyenMaiMuaTang.tuDongFile(dong);
        throw new IllegalArgumentException("Loại Khuyenmai không hợp lệ: " + loai);
    }

    public void kiemTraDuLieuFile() {
        DongDuLieu.khongAm(mucChiToiThieu, "mucChiToiThieu");
        DongDuLieu.maKeTiep(maKhuyenMai, "(?i)KM\\d+");
        DongDuLieu.thuTuNgay(ngayBatDau, ngayKetThuc);
    }

    public static void datBoDemFile(int value) {
        DongDuLieu.yeuCau(value > 0, "Bộ đếm mã phải dương.");
        soThuTu = value;
    }

    public void capNhatMaFile() {
        soThuTu = Math.max(soThuTu, DongDuLieu.maKeTiep(maKhuyenMai, "(?i)KM\\d+"));
    }
}
