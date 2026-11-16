package doitac;

import utils.DongDuLieu;
import hoadon.HoaDon;
import sanpham.SanPham;
import sanpham.QuanLySanPham;
import utils.Validation;

import java.util.Scanner;
public class Nhacungcap extends Doitac{
    private static int soThuTuNCC = 1;

    public static int getSoThuTuNCC() {
        return soThuTuNCC;
    }
    private String nganhHangCungCap;
    private long hanMucNoCong ;
    private QuanLySanPham sanPhamCungCap ;
    public Nhacungcap()
    {
        nganhHangCungCap = "" ;
        hanMucNoCong = 0 ;
        super.setLoaiDoiTac("Nhà cung cấp");
        sanPhamCungCap = new QuanLySanPham() ;
        super.setMaDoiTac(Validation.taoMa("NCC%03d" , getSoThuTuNCC()));
        soThuTuNCC++;
    }
    public Nhacungcap ( String maDoiTac , String tenDoiTac , String soDienThoai , String diaChi , String maSoThue , String email , String loaiDoiTac , String nganhHangCungCap , long hanMucNoCong , int soThuTu)
    {
        super(maDoiTac, tenDoiTac, soDienThoai, diaChi, maSoThue, email , loaiDoiTac);
        if (soThuTu < 1) {
            throw new IllegalArgumentException("Số thứ tự phải lớn hơn 0.");
        }
        soThuTuNCC = soThuTu;
        this.nganhHangCungCap = nganhHangCungCap;
        this.hanMucNoCong = hanMucNoCong;
        sanPhamCungCap = new QuanLySanPham() ;
    }

    public String getNganhHangCungCap() {
        return nganhHangCungCap;
    }

    public void setNganhHangCungCap(String nganhHangCungCap) {
        if (nganhHangCungCap == null || nganhHangCungCap.isBlank()) {
            throw new IllegalArgumentException("NganhHangCungCap không được để trống.");
        }
        this.nganhHangCungCap = nganhHangCungCap;
    }

    public void setHanMucNoCong(long hanMucNoCong) {
        if ( hanMucNoCong < 0 )
            throw new IllegalArgumentException("Hạn mức nợ cồng không âm!") ;
        this.hanMucNoCong = hanMucNoCong;
    }

    public long getHanMucNoCong() {
        return hanMucNoCong;
    }

    public QuanLySanPham getSanPhamCungCap() {
        return sanPhamCungCap;
    }

    public void setSanPhamCungCap(QuanLySanPham sanPhamCungCap) {
        if (sanPhamCungCap == null) {
            throw new IllegalArgumentException("sanPhamCungCap không được null.");
        }
        this.sanPhamCungCap = sanPhamCungCap;
    }

    public void setDoiTac(Scanner sc)
    {
        super.setDoiTac( sc );
        System.out.println("Nhập ngành hàng cung cấp: ");
        nganhHangCungCap = Validation.inputStringRegex(sc, "Nhập thông tin: ", "(?s).+", "Thông tin không được để trống!");
        hanMucNoCong = Validation.inputInt(sc , "Nhập hạn mức nợ công!") ;
    }

    public void KhoiTaoDanhSachSPCungCap ( QuanLySanPham qlsp )
    {
        sanPhamCungCap.getDanhSachSanPham().clear();
        for ( SanPham sp: qlsp.getDanhSachSanPham() )
            if ( sp != null && sp.getMaNhaCungCap() != null && sp.getMaNhaCungCap().equalsIgnoreCase(super.getMaDoiTac()) )
                sanPhamCungCap.getDanhSachSanPham().add(sp) ;
        System.out.println("Khởi tao danh sách sản phẩm thành công!");
    }
    public void inDanhSachSanPham ()
    {
        System.out.println("Danh sách sản phẩm do nhà cung cấp " + super.getTenDoiTac() + " cung cấp!");
        sanPhamCungCap.xuatDanhSach();
    }
    public boolean kiemTraHanMucNoCong ()
    {
        return hanMucNoCong <= 100000000;
    }
    public String toString()
    {
        return utils.HienThi.canLe(super.toString() + " | Ngành hàng cung cấp: " + nganhHangCungCap + " | Hạn mức nợ công: " + hanMucNoCong );
    }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "NHACUNGCAP;" + thongTinDoiTacFile() + ";" + DongDuLieu.ghep(nganhHangCungCap, hanMucNoCong);
    }

    public static Nhacungcap tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 11 && "NHACUNGCAP".equals(p[0]), "Sai dòng NHACUNGCAP (cần 11 cột).");
        int dem = Nhacungcap.getSoThuTuNCC();
        Nhacungcap obj = new Nhacungcap();
        Nhacungcap.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinDoiTacFile(p);
        obj.nganhHangCungCap = p[9];
        obj.hanMucNoCong = Long.parseLong(p[10]);
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.khongAm(hanMucNoCong, "hanMucNoCong");
        DongDuLieu.maKeTiep(getMaDoiTac(), "(?i)NCC\\d+");
    }

    public static void datBoDemFile(int value) {
        DongDuLieu.yeuCau(value > 0, "Bộ đếm mã phải dương.");
        soThuTuNCC = value;
    }

    public void capNhatMaFile() {
        soThuTuNCC = Math.max(soThuTuNCC, DongDuLieu.maKeTiep(getMaDoiTac(), "(?i)NCC\\d+"));
    }
}
