package hoadon;
import java.util.Scanner;

import dichvu.*;
import sanpham.SanPham;
import utils.Validation;
import sanpham.QuanLySanPham;

public interface CTHoaDon
{
    String getMaSanPham();
    String getTenSanPham();
    int getSoLuong();
    long getDonGia();
    long tinhThanhTien() ;
}
class CTHoaDonBan implements CTHoaDon {
    private String maSanPham;
    private String tenSanPham;
    private  int soLuong;
    private  long donGia;
    private long mucGiamGia; // Tổng số tiền giảm cho cả dòng sản phẩm.\
    private String loaiKhuyenMai ;

    // TODO: Tạm thời khai báo ở đây, sau này class SanPham có thể chuyển sang đó
    public static final String MA_SP_REGEX = "^SP\\d+$";
    //

    // Khởi tạo giá trị mặc định.
    public CTHoaDonBan() {
        this.maSanPham = "";
        this.tenSanPham = "";
        this.soLuong = 0;
        this.donGia = 0;
        this.mucGiamGia = 0;
        this.loaiKhuyenMai = "" ;
    }

    // Khôi phục đầy đủ dữ liệu chi tiết hóa đơn khi đọc file.
    public CTHoaDonBan(String maSanPham, String tenSanPham, int soLuong,
                       long donGia, long mucGiamGia , String loaiKhuyenMai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.mucGiamGia = mucGiamGia ;
        this.loaiKhuyenMai = loaiKhuyenMai ;
    }
    // methods
    public void nhapCTHoaDon(Scanner sc , Danhsachkhuyenmai dskm , SanPhamKhuyenMai spkm , QuanLySanPham qlsp ){
        this.maSanPham = Validation.inputStringRegex(sc, "Nhập mã sản phẩm (VD: SP001): ", MA_SP_REGEX, "Lỗi: Mã SP phải bắt đầu bằng SP in hoa và theo sau là số!", sanpham.SanPham.getSoThuTu());
        SanPham sanPhamDangXet = qlsp.getDanhSachSanPham().get(qlsp.timKiemSanPham(maSanPham)) ;
        this.tenSanPham = sanPhamDangXet.getTenSanPham();
        this.soLuong = Validation.inputInt(sc, "Nhập số lượng: ");
        if ( sanPhamDangXet.getSoLuongTonKho() == 0 )
        {
            System.out.println("San phẩm đã hết vui lòng chọn sản phẩm khác!");
            return;
        }
        while ( soLuong > sanPhamDangXet.getSoLuongTonKho() )
        {
            System.out.println(tenSanPham +" chỉ còn lại " + sanPhamDangXet.getSoLuongTonKho() + " sản phẩm bạn có muốn mua không(Có/Không): ");
            if ( Validation.inputCoKhong(sc , "Nhập lựa chọn của khách hàng: "))
            {
                soLuong = sanPhamDangXet.getSoLuongTonKho() ;
            }
            else
            {
                soLuong = 0 ;
                return;
            }
        }
        this.donGia  =sanPhamDangXet.getGiaBan() ;
        String maGiamGia = spkm.getMaKhuyenMai() ;
        Khuyenmai km = dskm.getKhuyenmais().get(dskm.traCuuThongTinKhuyenMai(maGiamGia)) ;
        if ( !km.kiemTraHetHanKhuyenMai() && donGia*soLuong >= km.getMucChiToiThieu())
        {
            switch (km) {
                case KhuyenMaiPhanTram khuyenMaiPhanTram -> {
                    mucGiamGia = (long) ((long) donGia * soLuong * khuyenMaiPhanTram.getPhanTramGiamGia());
                    if (mucGiamGia > khuyenMaiPhanTram.getMucGiamToiDa())
                        mucGiamGia = khuyenMaiPhanTram.getMucGiamToiDa();
                }
                case KhuyenMaiTrucTiep khuyenMaiTrucTiep ->
                        mucGiamGia = khuyenMaiTrucTiep.getSoTienGiam();
                case KhuyenMaiMuaTang khuyenMaiMuaTang -> {
                    int tiLeSanPhamMuaTrenSanPhamTang = soLuong / khuyenMaiMuaTang.getSoLuongMuaToiThieu();
                    mucGiamGia = khuyenMaiMuaTang.getSoLuongTang() * donGia * tiLeSanPhamMuaTrenSanPhamTang;
                }
                default -> {
                }
            }
        }
        loaiKhuyenMai = km.getLoaiKhuyenMai() ;
        System.out.println("Nhập chi tiết sản phẩm thành công!");
    }

    @Override
    public long tinhThanhTien() {
        return donGia*soLuong - mucGiamGia ;
    }


//    public void hienThiChiTiet(){
//        String tenHienThi = hangKhuyenMai ? tenSanPham + " (TẶNG)" : tenSanPham;
//        System.out.printf("  - Mã SP: %-10s | Tên: %-20s | SL: %-5d | Đơn giá gốc: %-10d | Thành tiền sau KM: %d\n",
//                maSanPham, tenHienThi, soLuong, donGia, tinhThanhTien());
//    } chờ cần xét lại
    public String toString()
    {
        return "Mã sản phẩm: " + maSanPham + " | Tên sản phẩm: " + tenSanPham + " | Số lượng: " + soLuong + " | Đơn giá: " + donGia + " | Tổng tiền: " + tinhThanhTien()+mucGiamGia + " | Thành tiền: " + tinhThanhTien() + " | Loại khuyến mãi: " + loaiKhuyenMai ;
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
    }

    public long getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
    }

    public long getMucGiamGia() {
        return mucGiamGia;
    }


    public static String getMaSpRegex() {
        return MA_SP_REGEX;
    }
}
class CTHoaDonNhap implements CTHoaDon
{
    private String maSanPham, tenSanPham;
    private int soLuong;
    private long donGia;

    public CTHoaDonNhap() {
        maSanPham = tenSanPham = "";
        soLuong = 0;
        donGia = 0;
    }

    public CTHoaDonNhap(String maSanPham, String tenSanPham, int soLuong, long donGia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    @Override
    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    @Override
    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    @Override
    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public long getDonGia() {
        return donGia;
    }

    public void setDonGia(long donGia) {
        this.donGia = donGia;
    }

    @Override
    public long tinhThanhTien() {
        return soLuong * donGia;
    }

    public void setHoaDonNhap(Scanner sc, QuanLySanPham quanLySanPham) {
        maSanPham = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ",
                "SP\\d{3}", "Mã sản phẩm không hợp lệ (VD: SP001)!", sanpham.SanPham.getSoThuTu());
        int index = quanLySanPham.timKiemSanPham(maSanPham);
        while (index == -1) {
            System.out.println("Mã sản phẩm không tồn tại vui lòng nhập lại!");
            maSanPham = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ",
                    "SP\\d{3}", "Mã sản phẩm không hợp lệ (VD: SP001)!", sanpham.SanPham.getSoThuTu());
            index = quanLySanPham.timKiemSanPham(maSanPham);
        }
        soLuong = Validation.inputInt(sc, "Nhập số lượng sản phẩm cần nhập: ");
        SanPham sanPham = quanLySanPham.getDanhSachSanPham().get(index);
        tenSanPham = sanPham.getTenSanPham();
        donGia = sanPham.getGiaNhap();
    }

    @Override
    public String toString() {
        return "Mã sản phẩm: " + maSanPham + " | Tên sản phẩm: " + tenSanPham
                + " | Số lượng: " + soLuong + " | Đơn giá: " + donGia;
    }
}
