package hoadon;

import utils.DongDuLieu;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import interfaces.IThanhToan ;
import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import interfaces.IThanhToan;
import person.DSKhachHang;
import person.KhachHang;
import sanpham.QuanLySanPham;
import sanpham.QuanLyTonKho;
import sanpham.SanPham;
import utils.Validation;

public class HoaDonBanHang extends HoaDon {
    private String maKhachHang ;
    private IThanhToan phuongThucThanhToan;
    private boolean daThanhToan;
    private double chietKhau ;
    public static final String MA_HD_BAN_REGEX = "(?i)^HD(B)?\\d+$";
    private ArrayList<CTHoaDonBan> ctHoaDonBans ;
    public HoaDonBanHang() {
        this("");
    }
    public HoaDonBanHang( String maNhanVien )
    {
        super(maNhanVien, "Hoá đơn bán");
        maKhachHang = "" ;
        chietKhau = 0 ;
        phuongThucThanhToan = null ;
        daThanhToan = false ;
        ctHoaDonBans = new ArrayList<>() ;
    }
    public HoaDonBanHang(String maHoaDon, LocalDateTime ngayLap , String maNhanVien , String loaiHoaDon, IThanhToan thanhToan , boolean daThanhToan , long tienKhuyenMai)
    {
        super(maHoaDon , ngayLap , maNhanVien , loaiHoaDon);
        this.phuongThucThanhToan = thanhToan ;
        this.daThanhToan = daThanhToan ;
        this.ctHoaDonBans = new ArrayList<>();
    }

    public double getChietKhau() {
        return chietKhau;
    }

    public void setChietKhau(double chietKhau) {
        if (!Double.isFinite(chietKhau) || chietKhau < 0 || chietKhau > 1) {
            throw new IllegalArgumentException("Chiết khấu phải nằm trong khoảng từ 0 đến 1.");
        }
        this.chietKhau = chietKhau;
    }

    public void setMaKhachHang(String maKhachHang) {
        if (maKhachHang == null || !maKhachHang.matches(Validation.MaKH_REGEX)) {
            throw new IllegalArgumentException("Mã khách hàng không hợp lệ.");
        }
        this.maKhachHang = maKhachHang;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setCtHoaDonBans(ArrayList<CTHoaDonBan> ctHoaDonBans) {
        if (ctHoaDonBans == null || ctHoaDonBans.contains(null)) {
            throw new IllegalArgumentException("Chi tiết hóa đơn không được null hoặc chứa phần tử null.");
        }
        this.ctHoaDonBans = ctHoaDonBans;
    }

    public ArrayList<CTHoaDonBan> getCtHoaDonBans() {
        return ctHoaDonBans;
    }

    public boolean nhapHoaDon(Scanner sc , QuanLySanPham qlsp , DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai dsKhuyenMai , DSKhachHang dskh ) {
        danhSachSPKM.tuDongGiamGiaCanHSD(qlsp, dsKhuyenMai);
        maKhachHang = Validation.inputStringRegex(sc , "Nhập mã khách hàng: " , Validation.MaKH_REGEX , "Mã khách hàng không hợp lệ vui lòng nhập lại!(VD:KH001)" , KhachHang.getSoThuTu()) ;
        while ( dskh.timMa(maKhachHang) == null || !dskh.timMa(maKhachHang).isTonTai())
        {
            System.out.println("Khách hàng không tồn tại vui lòng nhập lại mã: ");
            maKhachHang = Validation.inputStringRegex(sc , "Nhập mã khách hàng: " , Validation.MaKH_REGEX , "Mã khách hàng không hợp lệ vui lòng nhập lại!(VD:KH001)" , KhachHang.getSoThuTu()) ;
        }
        do {
            CTHoaDonBan ct = new CTHoaDonBan();
            ct.nhapThongTinBan( sc , qlsp , dsKhuyenMai , danhSachSPKM);
            boolean flag = false ;
            for ( CTHoaDonBan cthdb:ctHoaDonBans)
            {
                if ( cthdb.getMaSanPham().equalsIgnoreCase(ct.getMaSanPham()))
                {
                   int soLuongThem = ct.getSoLuong();
                   long soLuongMoi = (long) soLuongThem + cthdb.getSoLuong();
                   while (true)
                   {
                       if (soLuongThem > 0 && soLuongMoi <= qlsp.timMa(ct.getMaSanPham()).getSoLuongTonKho()) {
                           try {
                               cthdb.setSoLuong((int) soLuongMoi);
                               break;
                           } catch (IllegalArgumentException e) {
                               System.out.println(e.getMessage());
                           }
                       }
                       System.out.println("Số lương sản phẩm mua thêm vào vượt quá hàng tồn kho( còn lại: " +qlsp.timMa(ct.getMaSanPham()).getSoLuongTonKho() + " ) vui lòng nhập lại!"  );
                       soLuongThem = Validation.inputInt(sc, "Nhập lại số lượng sản phẩm thêm vào: " );
                       soLuongMoi = (long) soLuongThem + cthdb.getSoLuong();
                   }
                   flag = true ;
                   break;
                }
            }
            if (!flag) ctHoaDonBans.add(ct) ;
        } while (Validation.luaChon(sc, "Nhập tiếp sản phẩm? (1/0): ", 0, 1) == 1);
        KhachHang kh = dskh.timMa(maKhachHang);
        setChietKhau(kh.tinhChietKhau());
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
        if ( daThanhToan )
        {
            for ( CTHoaDonBan ct: ctHoaDonBans )
            {
                SanPham sp = qlsp.timMa(ct.getMaSanPham()) ;
                sp.setSoLuongTonKho(sp.getSoLuongTonKho() - ct.getSoLuong());
                qlsp.getDanhSachSanPham().set(qlsp.timViTri(ct.getMaSanPham()) , sp ) ;
            }
            kh.tichLuyChiTieu(tinhTongTien());
        }
        System.out.println(daThanhToan ? "Thanh toán thành công!" : "Thanh toán chưa thành công!");
        return daThanhToan ;
    }

    @Override 
    public long tinhTongTien(){
        long tongTienHang = 0;
        for(CTHoaDonBan ct : ctHoaDonBans){
            if (ct == null || !ct.isTonTai()) continue;
            try {
                tongTienHang = Math.addExact(tongTienHang, ct.tinhThanhTien());
            } catch (ArithmeticException e) {
                throw new IllegalStateException("Tổng tiền hóa đơn vượt giới hạn.", e);
            }
        }
        return java.math.BigDecimal.valueOf(tongTienHang)
                .multiply(java.math.BigDecimal.ONE.subtract(java.math.BigDecimal.valueOf(chietKhau)))
                .toBigInteger().longValueExact();
    }
   
    @Override 
    public void hienThiHoaDon(){
        System.out.println("===================HOÁ ĐƠN BÁN HÀNG==================");
        System.out.println("Mã hoá đơn: " + super.getMaHoaDon() + " | Mã nhân viên:" + super.getMaNhanVien() + " | Ngày lập: " + super.getNgayLap() + " | Mã khách hàng" + maKhachHang);
        System.out.println("Các món hàng đã mua: ");
        for ( CTHoaDonBan ct: ctHoaDonBans)
        {
            System.out.println(ct);
        }
        System.out.println("Thành tiền phải trả: " + tinhTongTien());
        if (phuongThucThanhToan instanceof ThanhToanTienMat tienMat) {
            System.out.println("Số tiền nhận: " + tienMat.getTienKhachDua() + " VND");
            System.out.println("Số tiền trả lại: " + tienMat.getTienThua() + " VND");
        }
        System.out.println( "Trạng thái: " + ((daThanhToan)?"Đã thanh toán":"Chưa thanh toán") + " | Phương thức thanh toán: " + (phuongThucThanhToan == null ? "Chưa chọn" : phuongThucThanhToan.getTenPhuongThuc()));
    }
    public void danhSachSanPhamDaMua()
    {
        System.out.println("Danh sách các mặc hàng đã mua: ");
        for ( CTHoaDonBan ct: ctHoaDonBans)
        {
            System.out.println(ct);
        }
    }
    // getters - setters
    public IThanhToan getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(IThanhToan phuongThucThanhToan) {
        if (phuongThucThanhToan == null) {
            throw new IllegalArgumentException("phuongThucThanhToan không được null.");
        } this.phuongThucThanhToan = phuongThucThanhToan; }
    public String toString()
    {
        return utils.HienThi.canLe(super.toString() + " | Mã khách hàng: " + maKhachHang + " | Trạng thái: " + ((daThanhToan)?"Đã thanh toán":"Chưa thanh toán") + " | Phương thức thanh toán: " + (phuongThucThanhToan == null ? "Chưa chọn" : phuongThucThanhToan.getTenPhuongThuc()) );
    }
    public boolean isDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(boolean daThanhToan) { this.daThanhToan = daThanhToan; }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "HDBAN;" + thongTinHoaDonFile() + ";" + DongDuLieu.ghep(maKhachHang, daThanhToan, chietKhau);
    }

    public static HoaDonBanHang tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 8 && "HDBAN".equals(p[0]), "Sai dòng HDBAN (cần 8 cột).");
        int dem = HoaDon.getSoThuTu();
        HoaDonBanHang obj = new HoaDonBanHang();
        HoaDon.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinHoaDonFile(p);
        obj.maKhachHang = p[5];
        obj.daThanhToan = DongDuLieu.logic(p[6]);
        obj.chietKhau = Double.parseDouble(p[7]);
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.tyLe(chietKhau, 1, "chiết khấu");
    }
}
