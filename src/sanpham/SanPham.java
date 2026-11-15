package sanpham;

import utils.DongDuLieu;


import utils.Validation;
import java.util.Scanner;

public abstract class SanPham implements interfaces.BanGhiFile {
    private boolean tonTai = true;

    public boolean isTonTai() { return tonTai; }

    public void setTonTai(boolean tonTai) { this.tonTai = tonTai; }

    private String maSanPham , tenSanPham , loaiSanPham , maNhaCungCap;
    private long giaNhap , giaBan ;
    private int soLuongTonKho  ;
    private double thue ;
    private static int soThuTu = 1 ;
    private static void capNhatSoThuTuTuMa(String ma) {
        if (ma != null && !ma.isEmpty()) {
            soThuTu = Math.max(soThuTu, DongDuLieu.maKeTiep(ma, "(?i)SP\\d+"));
        }
    }

    protected SanPham(double thueMacDinh, String loaiMacDinh) {
        this();
        if (!Double.isFinite(thueMacDinh) || thueMacDinh < 0 || thueMacDinh > 1) {
            throw new IllegalArgumentException("thue không hợp lệ.");
        }
        this.thue = thueMacDinh;
        this.loaiSanPham = loaiMacDinh;
    }

    public SanPham()
    {
        maSanPham = tenSanPham = loaiSanPham = maNhaCungCap = "" ;
        giaNhap = giaBan = 0 ;
        soLuongTonKho = 0 ;
        thue = 0 ;
        maSanPham = Validation.taoMa("SP%03d" , soThuTu) ;
        soThuTu++ ;
    }
    public SanPham( String maSanPham , String tenSanPham , String loaiSanPham , long giaNhap , long giaBan , int soLuongTonKho , String maNhaCungCap , double thue )
    {
        if (giaNhap < 0) throw new IllegalArgumentException("GiaNhap không được âm.");
        if (giaBan < 0) throw new IllegalArgumentException("GiaBan không được âm.");
        if (soLuongTonKho < 0) throw new IllegalArgumentException("SoLuongTonKho không được âm.");
        if (!Double.isFinite(thue) || thue < 0 || thue > 1) throw new IllegalArgumentException("thue không hợp lệ.");
        if (maSanPham != null && !maSanPham.isEmpty() && !maSanPham.matches("(?i)SP\\d+")) {
            throw new IllegalArgumentException("MaSanPham không đúng định dạng.");
        }
        if (tenSanPham != null && !tenSanPham.isEmpty() && tenSanPham.isBlank()) {
            throw new IllegalArgumentException("TenSanPham không được để trống.");
        }
        if (maNhaCungCap != null && !maNhaCungCap.isEmpty() && !maNhaCungCap.matches("(?i)NCC\\d+")) {
            throw new IllegalArgumentException("MaNhaCungCap không đúng định dạng.");
        }
        this.maSanPham = maSanPham ;
        this.tenSanPham = tenSanPham ;
        this.loaiSanPham = loaiSanPham ;
        this.giaNhap = giaNhap ;
        this.giaBan = giaBan ;
        this.soLuongTonKho = soLuongTonKho ;
        this.maNhaCungCap = maNhaCungCap ;
        this.thue = thue ;
        capNhatSoThuTuTuMa(maSanPham);
    }

    public static int getSoThuTu() {
        return soThuTu;
    }


    public void setThue(double thue) {
        if (!Double.isFinite(thue) || thue < 0 || thue > 1) {
            throw new IllegalArgumentException("thue không hợp lệ.");
        }
        this.thue = thue;
    }

    public double getThue() {
        return thue;
    }

    public void setMaSanPham(String maSanPham) {
        if (maSanPham == null || !maSanPham.matches("(?i)SP\\d+")) {
            throw new IllegalArgumentException("MaSanPham không đúng định dạng.");
        }
        capNhatSoThuTuTuMa(maSanPham);
        this.maSanPham = maSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        if (tenSanPham == null || tenSanPham.isBlank()) {
            throw new IllegalArgumentException("TenSanPham không được để trống.");
        }
        this.tenSanPham = tenSanPham;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        if (maNhaCungCap == null || !maNhaCungCap.matches("(?i)NCC\\d+")) {
            throw new IllegalArgumentException("MaNhaCungCap không đúng định dạng.");
        }
        this.maNhaCungCap = maNhaCungCap;
    }

    public void setSoLuongTonKho(int soLuongTonKho) {
        if (soLuongTonKho < 0) {
            throw new IllegalArgumentException("SoLuongTonKho không được âm.");
        }
        this.soLuongTonKho = soLuongTonKho;
    }

    public void setGiaNhap(long giaNhap) {
        if (giaNhap < 0) {
            throw new IllegalArgumentException("GiaNhap không được âm.");
        }
        this.giaNhap = giaNhap;
    }

    public void setGiaBan(long giaBan) {
        if (giaBan < 0) {
            throw new IllegalArgumentException("GiaBan không được âm.");
        }
        this.giaBan = giaBan;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        if (loaiSanPham == null || loaiSanPham.isBlank()) {
            throw new IllegalArgumentException("LoaiSanPham không được để trống.");
        }
        this.loaiSanPham = loaiSanPham;
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public int getSoLuongTonKho() {
        return soLuongTonKho;
    }


    public long getGiaBan() {
        return giaBan;
    }

    public long getGiaNhap() {
        return giaNhap;
    }

    public void setSanPham(Scanner sc)
    {
        System.out.println("Nhập tên sản phẩm: ");
        tenSanPham = Validation.inputStringRegex(sc, "Nhập thông tin: ", "(?s).+", "Thông tin không được để trống!");
        giaBan = Validation.inputLong(sc , "Nhập giá bán: ") ;
        giaNhap = Validation.inputLong(sc , "Nhập giá nhập hàng: ") ;
        soLuongTonKho = Validation.inputInt(sc , "Nhập số lượng tồn kho: ") ;
    }
    public abstract long tinhThue() ;
    @Override
    public String toString() {
        return utils.HienThi.canLe("Mã sản phẩm: " + maSanPham + " | Tên sản phẩm: " + tenSanPham + " | Giá nhập: " + giaNhap + " | Giá bán: " + giaBan + " | Số sản phẩm tồn kho : " + soLuongTonKho + " | Nhà cung cấp : " + maNhaCungCap);
    }

    // ================= ĐỌC / GHI FILE TXT =================

    protected String thongTinSanPhamFile() {
        return DongDuLieu.ghep(maSanPham, tenSanPham, loaiSanPham, maNhaCungCap, giaNhap, giaBan, soLuongTonKho, thue, tonTai);
    }

    protected void docThongTinSanPhamFile(String[] p) {
        maSanPham = p[1];
        tenSanPham = p[2];
        loaiSanPham = p[3];
        maNhaCungCap = p[4];
        giaNhap = Long.parseLong(p[5]);
        giaBan = Long.parseLong(p[6]);
        soLuongTonKho = Integer.parseInt(p[7]);
        thue = Double.parseDouble(p[8]);
        tonTai = DongDuLieu.logic(p[9]);
    }

    public static SanPham tuDongFile(String dong) {
        String loai = dong.split(";", -1)[0];
        if ("THUCPHAM".equals(loai)) return ThucPham.tuDongFile(dong);
        if ("DIENTU".equals(loai)) return DienTu.tuDongFile(dong);
        if ("TIEUDUNG".equals(loai)) return TieuDung.tuDongFile(dong);
        throw new IllegalArgumentException("Loại SanPham không hợp lệ: " + loai);
    }

    public void kiemTraDuLieuFile() {
        DongDuLieu.khongAm(giaNhap, "giaNhap");
        DongDuLieu.khongAm(giaBan, "giaBan");
        DongDuLieu.khongAm(soLuongTonKho, "soLuongTonKho");
        DongDuLieu.maKeTiep(maSanPham, "(?i)SP\\d+");
        DongDuLieu.tyLe(thue, 1, "thuế sản phẩm");
    }

    public static void datBoDemFile(int value) {
        DongDuLieu.yeuCau(value > 0, "Bộ đếm mã phải dương.");
        soThuTu = value;
    }

    public void capNhatMaFile() {
        soThuTu = Math.max(soThuTu, DongDuLieu.maKeTiep(maSanPham, "(?i)SP\\d+"));
    }
}
