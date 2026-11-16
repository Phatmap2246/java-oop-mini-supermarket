package hoadon;
import utils.BoDemMa;
import utils.DongDuLieu;
import utils.TepVanBan;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import hoadon.HoaDon;

import java.io.IOException;

import java.io.FileWriter;

import java.io.File;

import java.io.BufferedWriter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import dichvu.DanhSachSPKM;
import java.time.LocalDateTime;
import dichvu.Danhsachkhuyenmai;
import person.DSKhachHang;
import person.KhachHang;
import sanpham.QuanLySanPham;
import utils.Validation;

public class DanhSachHoaDonBan implements interfaces.IQuanLy<HoaDonBanHang> {
    private transient DanhSachSPKM nguonSPKM;
    private transient Danhsachkhuyenmai nguonKhuyenMai;
    private transient QuanLySanPham nguonSanPham;
    private transient DSKhachHang nguonKhachHang;
    private transient String maNhanVienNhap;

    public void cauHinh(DanhSachSPKM spkm, Danhsachkhuyenmai km, QuanLySanPham sp, DSKhachHang kh, String maNV) {
        nguonSPKM = spkm; nguonKhuyenMai = km; nguonSanPham = sp; nguonKhachHang = kh; maNhanVienNhap = maNV;
    }

    private void kiemTraNguon() {
        if (nguonSPKM == null || nguonKhuyenMai == null || nguonSanPham == null
                || nguonKhachHang == null || maNhanVienNhap == null) {
            throw new IllegalStateException("Cần gọi cauHinh để nối dữ liệu lập hóa đơn bán.");
        }
    }

    @Override public void taoDanhSach(Scanner sc) {
        kiemTraNguon(); taoDanhSach(sc, nguonSPKM, nguonKhuyenMai, nguonSanPham, nguonKhachHang, maNhanVienNhap);
    }
    @Override public void them(Scanner sc) {
        kiemTraNguon(); them(sc, nguonSPKM, nguonKhuyenMai, nguonSanPham, nguonKhachHang, maNhanVienNhap);
    }
    @Override public boolean sua(String ma, Scanner sc) {
        HoaDonBanHang hd = timMa(ma);
        if (hd == null || hd.isDaThanhToan()) return false;
        kiemTraNguon(); return sua(sc, ma, nguonSPKM, nguonKhuyenMai, nguonSanPham, nguonKhachHang, maNhanVienNhap);
    }
    @Override public boolean xoa(String ma) {
        HoaDonBanHang hd = timMa(ma);
        if (hd == null || hd.isDaThanhToan()) return false;
        return dsHoaDonBanHang.remove(hd);
    }
    @Override public HoaDonBanHang timTen(String ten) {
        if (ten == null || ten.isBlank()) return null;
        String khoa = ten.trim().toLowerCase(java.util.Locale.ROOT);
        for (HoaDonBanHang hd : dsHoaDonBanHang) {
            if (hd == null) continue;
            KhachHang kh = nguonKhachHang == null ? null : nguonKhachHang.timMa(hd.getMaKhachHang());
            if (kh != null && kh.getHoten() != null && kh.getHoten().toLowerCase(java.util.Locale.ROOT).contains(khoa)) return hd;
            for (CTHoaDonBan ct : hd.getCtHoaDonBans()) {
                if (ct != null && ct.getTenSanPham() != null && ct.getTenSanPham().toLowerCase(java.util.Locale.ROOT).contains(khoa)) return hd;
            }
        }
        return null;
    }
    @Override public HoaDonBanHang timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) return null;
        for (HoaDonBanHang hd : dsHoaDonBanHang) {
            if (hd != null && hd.toString().toLowerCase(java.util.Locale.ROOT)
                    .contains(khoa.trim().toLowerCase(java.util.Locale.ROOT))) return hd;
        }
        return timTen(khoa);
    }
    private ArrayList<HoaDonBanHang> dsHoaDonBanHang;

    public DanhSachHoaDonBan() {
        this.dsHoaDonBanHang = new ArrayList<>();
    }

    public DanhSachHoaDonBan(ArrayList<HoaDonBanHang> danhSachHoaDonBan) {
        if (danhSachHoaDonBan == null) throw new IllegalArgumentException("Danh sách hóa đơn không được null.");
        this.dsHoaDonBanHang = danhSachHoaDonBan;
    }

    public ArrayList<HoaDonBanHang> getDsHoaDonBanHang() {
        return dsHoaDonBanHang;
    }
    public void taoDanhSach( Scanner sc, DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai danhSachKhuyenMai, QuanLySanPham qlsp , DSKhachHang dskh, String maNhanVien )
    {
        System.out.println("Tạo dữ liệu hoá đơn nhập!");
        do {
            them(sc,danhSachSPKM , danhSachKhuyenMai , qlsp , dskh , maNhanVien);
        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục(Có/Không): "));
        System.out.println("Tạo dữ liệu hoá đơn bán hàng thành công!");
    }

    @Override
    public void xuatDanhSach(){
        if (dsHoaDonBanHang.isEmpty()) {
            System.out.println("Danh sách hóa đơn đang trống !");
            return;
        }
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for (HoaDonBanHang hdb : dsHoaDonBanHang) {
            if (hdb == null) continue;
            hdb.hienThiHoaDon();
        }
    }
    public void them(Scanner sc, DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai danhSachKhuyenMai, QuanLySanPham qlsp , DSKhachHang dskh, String maNhanVien)
    {
        System.out.println("Nhập hoá đơn bán!");
        HoaDonBanHang hdb = new HoaDonBanHang(maNhanVien);
        if (!hdb.nhapHoaDon(sc, qlsp , danhSachSPKM, danhSachKhuyenMai , dskh)) return;
        dsHoaDonBanHang.add(hdb);
        System.out.println("Thêm hóa đơn "+hdb.getMaHoaDon()+" Thành công!");
    }
    public void taoKHoaDonBan( Scanner sc, DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai danhSachKhuyenMai, QuanLySanPham qlsp , DSKhachHang dskh, String maNhanVien , int k )
    {
        System.out.println("Tạo " + k + " Hoá đơn bán hàng!");
        for ( int i = 0 ; i < k ; i++ )
        {
            them(sc,danhSachSPKM , danhSachKhuyenMai , qlsp , dskh , maNhanVien);
        }
        System.out.println("Thêm " + k + " hoá đơn bán thành công!");
    }

    public void taoHoaDonThanhToan(Scanner sc , QuanLySanPham qlsp , DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai dsKhuyenMai , DSKhachHang dskh, String maNhanVien) {
        System.out.println("Nhập hoá đơn bán hàng!");
        HoaDonBanHang hdbh = new HoaDonBanHang(maNhanVien);
        if (!hdbh.nhapHoaDon(sc , qlsp , danhSachSPKM , dsKhuyenMai , dskh )) return;
        dsHoaDonBanHang.add(hdbh) ;
        System.out.println("Thêm hoá đơn bán hàng thành công!");
    }
    public boolean sua(Scanner sc, String maHoaDon, DanhSachSPKM danhSachSPKM,
            Danhsachkhuyenmai danhSachKhuyenMai, QuanLySanPham qlsp, DSKhachHang dskh, String maNhanVien)
    {
        HoaDonBanHang hd = timMa(maHoaDon);
        if (hd == null) {
            System.out.println("Mã hoá đơn không tồn tại!");
            return false;
        }
        if (hd.isDaThanhToan()) {
            System.out.println("Hoá đơn đã thanh toán nên không thể chỉnh sửa thuộc tính!");
            return false;
        }
        System.out.println("Nhập thuộc tính của hoá đơn cần sửa: ");
            System.out.println("1. Sửa mã khách hàng!");
            System.out.println("2. Sửa chi tiết hoá đơn!");
            int luaChon = Validation.luaChon(sc , "Nhập lựa chọn: " , 1 ,2 ) ;
            switch (luaChon)
            {
                case 1:
                    hd.setMaKhachHang(Validation.inputStringRegex(sc , "Nhập mã khách hàng mới: " , Validation.MaKH_REGEX, "Mã khách hàng đang nhập sai định dạng vui lòng nhập lại!(VD:KH001)" , KhachHang.getSoThuTu()));
                    while (dskh.timMa(hd.getMaKhachHang()) == null || !dskh.timMa(hd.getMaKhachHang()).isTonTai())
                    {
                        System.out.println("Khách hàng không tồn tại vui lòng nhập lại mã khách hàng!");
                        hd.setMaKhachHang(Validation.inputStringRegex(sc , "Nhập mã khách hàng mới: " , Validation.MaKH_REGEX, "Mã khách hàng đang nhập sai định dạng vui lòng nhập lại!(VD:KH001)" , KhachHang.getSoThuTu()));
                    }
                    break;
                case 2:
                    System.out.println("Nhập thuộc tính chi tiết hoá đơn cần sửa: ");
                    System.out.println("1. Thêm sản phẩm cần mua!");
                    System.out.println("2. Xoá sản phẩm đã mua!");
                    System.out.println("3. Thay đổi sản phẩm đã mua!");
                    int luachon = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, 3);
                    if (luachon != 1 && hd.getCtHoaDonBans().isEmpty()) {
                        System.out.println("Chi tiết hoá đơn bán đang trống!");
                        return false;
                    }
                    switch (luachon) {
                        case 1:
                            CTHoaDonBan ct = new CTHoaDonBan();
                            ct.nhapThongTinBan(sc, qlsp, danhSachKhuyenMai, danhSachSPKM);
                            hd.getCtHoaDonBans().add(ct);
                            System.out.println("Thêm chi tiết sản phẩm thành công!");
                            break;
                        case 2:
                            for (int i = 0; i < hd.getCtHoaDonBans().size(); i++) {
                                System.out.println((i + 1) + ". " + hd.getCtHoaDonBans().get(i));
                            }
                            int viTriXoa = Validation.luaChon(sc, "Chọn dòng sản phẩm cần xoá: ",
                                    1, hd.getCtHoaDonBans().size()) - 1;
                            hd.getCtHoaDonBans().remove(viTriXoa) ;
                            System.out.println("Xoá chi tiết sản phẩm thành công!");
                            break;
                        case 3:
                            for (int i = 0; i < hd.getCtHoaDonBans().size(); i++) {
                                System.out.println((i + 1) + ". " + hd.getCtHoaDonBans().get(i));
                            }
                            int viTri = Validation.luaChon(sc, "Chọn dòng sản phẩm cần sửa: ",
                                    1, hd.getCtHoaDonBans().size()) - 1;
                                CTHoaDonBan ctMoi = new CTHoaDonBan();
                                System.out.println("Chọn lại sản phẩm mới cho chi tiết hoá đơn");
                                ctMoi.nhapThongTinBan(sc, qlsp, danhSachKhuyenMai, danhSachSPKM);
                                hd.getCtHoaDonBans().set(viTri, ctMoi);
                                System.out.println("Chỉnh sửa sản phẩm thành công!");
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    return false;
            }
        return true;
    }

    public void chinhSuaHoaDonChuaThanhToan(Scanner sc, DanhSachSPKM danhSachSPKM,
                                            Danhsachkhuyenmai danhSachKhuyenMai, QuanLySanPham qlsp, DSKhachHang dskh, String maNhanVien)
    {
        System.out.println("=================CHỈNH SỬA HOÁ ĐƠN===================");
        System.out.println("Danh sách hoá đơn chưa thanh toán: ");
        boolean flag = false ;
        for (HoaDonBanHang hdb: dsHoaDonBanHang )
        {
            if (hdb == null) continue;
            if ( !hdb.isDaThanhToan() )
            {
                System.out.println(hdb);
                flag = true ;
            }
        }
        if ( !flag )
        {
            System.out.println("Tất cả hoá đơn điều đã được thanh toán nên không chỉnh sửa được nữa!");
            return;
        }
        String ma = Validation.inputStringRegex(sc , "Nhập mã hoá đơn cần chỉnh sửa: " , HoaDonBanHang.MA_HD_BAN_REGEX , "Mã hoá đơn không đung định dạng vui lòng nhập lại!(VD:HD001)" , HoaDonBanHang.getSoThuTu() ) ;
        sua(sc , ma , danhSachSPKM , danhSachKhuyenMai , qlsp , dskh , maNhanVien);
    }
    @Override
    public HoaDonBanHang timMa(String maHD){
        if (maHD == null || maHD.isBlank()) return null;
        maHD = maHD.trim();
        if (dsHoaDonBanHang.isEmpty()) {
            System.out.println("Danh sách hóa đơn đang trống !");
            return null;
        }
        for (HoaDonBanHang hdb : dsHoaDonBanHang) {
            if (hdb == null) continue;
            if (hdb.getMaHoaDon() != null && hdb.getMaHoaDon().equalsIgnoreCase(maHD)) {
                return hdb;
            }
        }
        System.out.println("Không tìm thấy hóa đơn phù hợp!");
        return null;
    }
    @Override
    public int timViTri(String maHD){
        if (maHD == null || maHD.isBlank()) return -1;
        maHD = maHD.trim();
        if (dsHoaDonBanHang.isEmpty()) {
            System.out.println("Danh sách hóa đơn đang trống !");
            return -1;
        }
        for(int i =0; i<dsHoaDonBanHang.size();i++){
            if (dsHoaDonBanHang.get(i) != null && dsHoaDonBanHang.get(i).getMaHoaDon() != null && dsHoaDonBanHang.get(i).getMaHoaDon().equalsIgnoreCase(maHD)) {
                return i ;
            }
        }
        System.out.println("Không tìm thấy hóa đơn phù hợp!");
        return -1;
    }

    public HoaDonBanHang timKiemHoaDonBanHang_MaKhachHang(String maKhachHang){
        if (dsHoaDonBanHang.isEmpty()) {
            System.out.println("Danh sách hóa đơn đang trống !");
            return null;
        }
        for (HoaDonBanHang hdb : dsHoaDonBanHang) {
            if (hdb == null) continue;
            if (hdb.getMaKhachHang() != null && hdb.getMaKhachHang().equalsIgnoreCase(maKhachHang)) {
                return hdb;
            }
        }
        System.out.println("Không tìm thấy hóa đơn phù hợp!");
        return null;
    }
    public void traCuuHoaDonTrenKhoangThoiGian ( LocalDateTime thoiGianBatDau , LocalDateTime thoiGianKetThuc )
    {
        if (thoiGianBatDau == null || thoiGianKetThuc == null) {
            System.out.println("Bạn chưa nhập khoảng thời gian!");
            return;
        }
        System.out.println("Các hoá đơn bán được in vào khoảng thời gian từ " + thoiGianBatDau + " đến " + thoiGianKetThuc + "là: ");
        for ( HoaDonBanHang hdb: dsHoaDonBanHang )
        {
            if (hdb == null) continue;
            if (hdb.getNgayLap() != null && !hdb.getNgayLap().isBefore(thoiGianBatDau)
                    && !hdb.getNgayLap().isAfter(thoiGianKetThuc))
            {
                hdb.hienThiHoaDon();
            }
        }
    }
    public void traCuuHoaDonTrenKhoangThoiGian (LocalDate thoiGianBatDau , LocalDate thoiGianKetThuc )
    {
        if (thoiGianBatDau == null || thoiGianKetThuc == null) {
            System.out.println("Bạn chưa nhập khoảng thời gian!");
            return;
        }
        System.out.println("Các hoá đơn bán được in vào khoảng thời gian từ " + thoiGianBatDau + " đến " + thoiGianKetThuc + "là: ");
        for ( HoaDonBanHang hdb: dsHoaDonBanHang )
        {
            if (hdb == null) continue;
            if (hdb.getNgayLap() != null && !hdb.getNgayLap().toLocalDate().isBefore(thoiGianBatDau)
                    && !hdb.getNgayLap().toLocalDate().isAfter(thoiGianKetThuc))
            {
                hdb.hienThiHoaDon();
            }
        }
    }

    public void timKhoa(Scanner sc) {
        while (true) {
            System.out.println("================ TÌM KIẾM HÓA ĐƠN BÁN ================");
            System.out.println("1. Mã hóa đơn");
            System.out.println("2. Mã khách hàng");
            System.out.println("3. Khoảng thời gian");
            System.out.println("4. Mã nhân viên");
            System.out.println("0. Trở về");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 4);
            if (luaChon == 0) return;
            boolean flag = false;
            System.out.println("Kết quả tra cứu:");
            switch (luaChon) {
                case 1: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã hóa đơn cần tìm: ", HoaDonBanHang.MA_HD_BAN_REGEX, "Mã hóa đơn không đúng định dạng. Vui lòng nhập lại!", HoaDon.getSoThuTu());
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (HoaDonBanHang hdb : dsHoaDonBanHang) {
                        if (hdb != null && ((hdb.getMaHoaDon()) != null && hdb.getMaHoaDon().equalsIgnoreCase(tuKhoa))) {
                            hdb.hienThiHoaDon();
                            flag = true;
                        }
                    }
                    break;
                }
                case 2: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã khách hàng cần tìm: ", Validation.MaKH_REGEX, "Mã khách hàng không đúng định dạng. Vui lòng nhập lại!", KhachHang.getSoThuTu());
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (HoaDonBanHang hdb : dsHoaDonBanHang) {
                        if (hdb != null && ((hdb.getMaKhachHang()) != null && hdb.getMaKhachHang().equalsIgnoreCase(tuKhoa))) {
                            hdb.hienThiHoaDon();
                            flag = true;
                        }
                    }
                    break;
                }
                case 3: {
                    System.out.println("1. Khoảng ngày");
                    System.out.println("2. Khoảng giờ trong một ngày");
                    System.out.println("3. Khoảng ngày giờ");
                    System.out.println("0. Trở về");
                    int kieuThoiGian = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 3);
                    switch (kieuThoiGian) {
                        case 1: {
                            LocalDate tu = Validation.inputLocalDateRegex(sc, "Từ (dd/MM/yyyy): ");
                            LocalDate den = Validation.inputLocalDateRegex(sc, "Đến (dd/MM/yyyy): ");
                            if (tu.isAfter(den)) {
                                System.out.println("Thời gian bắt đầu không được sau thời gian kết thúc!");
                                continue;
                            }
                            System.out.println("Đang tra cứu...");
                            Validation.danghoatdong(1);
                            for (HoaDonBanHang hdb : dsHoaDonBanHang) {
                                if (hdb != null && (hdb.getNgayLap() != null && !hdb.getNgayLap().toLocalDate().isBefore(tu) && !hdb.getNgayLap().toLocalDate().isAfter(den))) {
                                    hdb.hienThiHoaDon();
                                    flag = true;
                                }
                            }
                            break;
                        }
                        case 2: {
                            LocalDate ngay = Validation.inputLocalDateRegex(sc, "Nhập ngày (dd/MM/yyyy): ");
                            int tu = Validation.luaChon(sc, "Từ giờ (0-23): ", 0, 23);
                            int den = Validation.luaChon(sc, "Đến giờ (0-23): ", 0, 23);
                            if (tu > den) {
                                System.out.println("Giờ bắt đầu không được sau giờ kết thúc!");
                                continue;
                            }
                            System.out.println("Đang tra cứu...");
                            Validation.danghoatdong(1);
                            for (HoaDonBanHang hdb : dsHoaDonBanHang) {
                                if (hdb != null && (hdb.getNgayLap() != null && hdb.getNgayLap().toLocalDate().equals(ngay) && hdb.getNgayLap().getHour() >= tu && hdb.getNgayLap().getHour() <= den)) {
                                    hdb.hienThiHoaDon();
                                    flag = true;
                                }
                            }
                            break;
                        }
                        case 3: {
                            LocalDateTime tu = Validation.inputLocalDateTimeRegex(sc, "Từ (dd/MM/yyyy HH:mm:ss): ");
                            LocalDateTime den = Validation.inputLocalDateTimeRegex(sc, "Đến (dd/MM/yyyy HH:mm:ss): ");
                            if (tu.isAfter(den)) {
                                System.out.println("Thời gian bắt đầu không được sau thời gian kết thúc!");
                                continue;
                            }
                            System.out.println("Đang tra cứu...");
                            Validation.danghoatdong(1);
                            for (HoaDonBanHang hdb : dsHoaDonBanHang) {
                                if (hdb != null && (hdb.getNgayLap() != null && !hdb.getNgayLap().isBefore(tu) && !hdb.getNgayLap().isAfter(den))) {
                                    hdb.hienThiHoaDon();
                                    flag = true;
                                }
                            }
                            break;
                        }
                        default:
                            continue;
                    }
                    break;
                }
                case 4: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã nhân viên cần tìm: ", Validation.MaNV_REGEX, "Mã nhân viên không đúng định dạng. Vui lòng nhập lại!", person.NhanVien.getSoThuTu());
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (HoaDonBanHang hdb : dsHoaDonBanHang) {
                        if (hdb != null && ((hdb.getMaNhanVien()) != null && hdb.getMaNhanVien().equalsIgnoreCase(tuKhoa))) {
                            hdb.hienThiHoaDon();
                            flag = true;
                        }
                    }
                    break;
                }

                default:
                    continue;
            }
            if (!flag) System.out.println("Không tìm thấy kết quả phù hợp!");
        }
    }

    public void locCacHoaDonTheoTongGia ( long nguongGia )
    {
        System.out.println("Danh sách hoá đơn có tổng giá trên " + nguongGia );
        System.out.println("Đang tra cứu...");
        Validation.danghoatdong(1);
        for ( HoaDonBanHang hdb: dsHoaDonBanHang )
        {
            if (hdb == null) continue;
            if ( hdb.tinhTongTien() > nguongGia )
                hdb.hienThiHoaDon();
        }
    }

    /**
     * Thống kê Top N sản phẩm bán chạy nhất dựa trên các hóa đơn bán đã thanh toán.
     */
    public ArrayList<String> topNSanPham(int n) {
        ArrayList<String> ketQua = new ArrayList<>();
        if (n <= 0) {
            System.out.println("[!] Số lượng N phải lớn hơn 0!");
            return ketQua;
        }
        if (dsHoaDonBanHang.isEmpty()) {
            System.out.println("[!] Danh sách hóa đơn bán đang trống!");
            return ketQua;
        }

        java.util.Map<String, String> tenSPMap = new java.util.LinkedHashMap<>();
        java.util.Map<String, Integer> soLuongBanMap = new java.util.LinkedHashMap<>();
        java.util.Map<String, Long> doanhThuMap = new java.util.LinkedHashMap<>();

        for (HoaDonBanHang hdb : dsHoaDonBanHang) {
            if (hdb == null || !hdb.isDaThanhToan()) continue;
            for (CTHoaDonBan ct : hdb.getCtHoaDonBans()) {
                if (ct == null || !ct.isTonTai() || ct.getMaSanPham() == null) continue;
                String maKey = ct.getMaSanPham().trim().toUpperCase(java.util.Locale.ROOT);
                tenSPMap.putIfAbsent(maKey, ct.getTenSanPham());
                soLuongBanMap.merge(maKey, ct.getSoLuong(), Integer::sum);
                doanhThuMap.merge(maKey, ct.tinhThanhTien(), Long::sum);
            }
        }

        if (soLuongBanMap.isEmpty()) {
            System.out.println("[i] Chưa có sản phẩm nào được bán trong các hóa đơn đã thanh toán.");
            return ketQua;
        }

        ArrayList<String> dsMa = new ArrayList<>(soLuongBanMap.keySet());
        dsMa.sort((ma1, ma2) -> {
            int cmpSL = Integer.compare(soLuongBanMap.get(ma2), soLuongBanMap.get(ma1));
            if (cmpSL != 0) return cmpSL;
            return Long.compare(doanhThuMap.get(ma2), doanhThuMap.get(ma1));
        });

        int gioiHan = Math.min(n, dsMa.size());
        System.out.println("\n============== TOP " + gioiHan + " SẢN PHẨM BÁN CHẠY NHẤT ==============");
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for (int i = 0; i < gioiHan; i++) {
            String ma = dsMa.get(i);
            String dong = String.format("Top %d | Mã SP: %s | Tên SP: %s | Đã bán: %d | Doanh thu: %,d VND",
                    i + 1, ma, tenSPMap.get(ma), soLuongBanMap.get(ma), doanhThuMap.get(ma));
            System.out.println(dong);
            ketQua.add(dong);
        }
        return ketQua;
    }

    public void thongKeTopNSanPham(Scanner sc) {
        int n = Validation.inputInt(sc, "Nhập số lượng N sản phẩm bán chạy cần xem: ");
        topNSanPham(n);
    }

    public void timKiemHoaDon( String maHoaDon )
    {
        HoaDonBanHang hdb = timMa(maHoaDon) ;
        if ( hdb == null )
        {
            System.out.println("Hoá đơn không tồn tại!");
            return;
        }
        System.out.println("Đang tra cứu...");
        Validation.danghoatdong(1);
        System.out.println("Thông tin hoá đơn: ");
        hdb.hienThiHoaDon();
    }
    public void thongKeHoaDonChuaThanhToan()
    {
        int tongHoaDon = 0 ;
        System.out.println("==============THỐNG KÊ HOÁ ĐƠN CHƯA THANH TOÁN===============");
        boolean flag = false ;
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( HoaDonBanHang hdb: dsHoaDonBanHang )
        {
            if (hdb == null) continue;
            if ( !hdb.isDaThanhToan())
            {
                hdb.hienThiHoaDon();
                tongHoaDon++ ;
                flag = true ;
            }
        }
        System.out.println("Tổng hoá đơn chưa thanh toán là: " + tongHoaDon);
        if ( !flag )
        {
            System.out.println("Không có hoá đơn nào chưa thanh toán!");
            return;
        }
    }
    public long thongKeDoanhThuTheoKhoang( LocalDate ngayBD , LocalDate ngayKT )
    {
        if (ngayBD == null || ngayKT == null) {
            System.out.println("Bạn chưa nhập khoảng thời gian!");
            return 0;
        }
        long tongTien = 0 ;
        for ( HoaDonBanHang hdb: dsHoaDonBanHang )
        {
            if (hdb == null) continue;
            if ( hdb.isDaThanhToan() && hdb.getNgayLap() != null ) {
                LocalDate ngayLap = hdb.getNgayLap().toLocalDate();
                if (!ngayLap.isBefore(ngayBD) && !ngayLap.isAfter(ngayKT)) {
                    tongTien += hdb.tinhTongTien();
                }
            }
        }
        return tongTien ;
    }
    public void thongKeDoanhThu( Scanner sc )
    {
        LocalDate ngayBD = Validation.inputLocalDateRegex( sc , "Nhập ngày bắt đầu tính tiền nhập: ") ;
        LocalDate ngayKT = Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc tính tiền nhập: " );
        if ( ngayKT.isBefore(ngayBD))
        {
            LocalDate tam = ngayBD ;
            ngayBD = ngayKT ;
            ngayKT = tam ;
        }
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        System.out.println("Doanh thu bán được từ ngày " + ngayBD + " đến ngày: " + ngayKT + " là: " + thongKeDoanhThuTheoKhoang(ngayBD , ngayKT));
    }

    public long tinhTongTienDaThanhToan() {
        long tongTienCuoiCa = 0;
        for (HoaDonBanHang hoaDon : dsHoaDonBanHang) {
            if (hoaDon != null && hoaDon.isDaThanhToan()) {
                tongTienCuoiCa += hoaDon.tinhTongTien();
            }
        }
        return tongTienCuoiCa;
    }

    public long tinhTongDoanhThu(ArrayList<HoaDon> dshd,long songay) {
        if (dshd == null || dshd.isEmpty()) return 0;
        long tongDoanhThu = 0;
        LocalDate lastDay = LocalDate.now();
        for (HoaDon hoaDon : dshd) {
            if (hoaDon != null && hoaDon.getNgayLap() != null)
                lastDay = hoaDon.getNgayLap().toLocalDate();
        }
        for (int i = dshd.size()-1; i>=0; i--) {
            if (dshd.get(i) != null && dshd.get(i).getNgayLap() != null && dshd.get(i) instanceof HoaDonBanHang && dshd.get(i).getNgayLap().toLocalDate().isAfter( lastDay.minusDays(songay)) ) {
                tongDoanhThu += dshd.get(i).tinhTongTien();
            }
        }
        return tongDoanhThu;
    }

    public long tinhTongDoanhThu(long songay)
    {
        ArrayList<HoaDon> danhSach = new ArrayList<>();
        for (HoaDon hoaDon : dsHoaDonBanHang) {
            danhSach.add(hoaDon);
        }
        return tinhTongDoanhThu(danhSach, songay);
    }

    public void xemBaoCao(Scanner sc)
    {
        int luaChon ;
        do {
            System.out.println("===================BÁO CÁO HÓA ĐƠN BÁN===================");
            System.out.println("1. Xuất danh sách hóa đơn bán!");
            System.out.println("2. Tìm kiếm hóa đơn bán!");
            System.out.println("3. Thống kê hóa đơn chưa thanh toán!");
            System.out.println("4. Thống kê doanh thu!");
            System.out.println("5. Lọc hóa đơn theo tổng giá!");
            System.out.println("6. Thống kê Top N sản phẩm bán chạy!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 6);
            switch (luaChon)
            {
                case 1: {
                    xuatDanhSach();
                    break;
                }
                case 2: {
                    timKhoa(sc);
                    break;
                }
                case 3: {
                    thongKeHoaDonChuaThanhToan();
                    break;
                }
                case 4: {
                    thongKeDoanhThu(sc);
                    break;
                }
                case 5: {
                    long nguongGia = Validation.inputLong(sc, "Nhập ngưỡng tổng giá: ");
                    locCacHoaDonTheoTongGia(nguongGia);
                    break;
                }
                case 6: {
                    thongKeTopNSanPham(sc);
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        } while (luaChon != 0);
    }

    public void ghiFileText(String tenFile) {
        try { ghiFile(tenFile); }
        catch (java.io.IOException e) { throw new java.io.UncheckedIOException(e); }
    }

    public void docFileText(String tenFile) {
        try { docFile(tenFile); }
        catch (java.io.IOException e) { throw new java.io.UncheckedIOException(e); }
    }

    public boolean ghiLichSuCa(String maNhanVien) throws IOException
    {
        File f = new File(utils.DuongDanLuuTru.file("LichSuCa.txt"));
        java.nio.file.Files.createDirectories(f.toPath().getParent());
        // Tự tạo file và ghi nối tiếp để không mất lịch sử các ca trước.
        try (BufferedWriter bw = java.nio.file.Files.newBufferedWriter(f.toPath(),
                java.nio.charset.StandardCharsets.UTF_8,
                java.nio.file.StandardOpenOption.CREATE, java.nio.file.StandardOpenOption.APPEND)) {
            for (int i = 0; i < dsHoaDonBanHang.size(); i++) {
                HoaDonBanHang hdb = dsHoaDonBanHang.get(i);
                if (hdb == null) continue;
                String[] data = new String[4];
                data[0] = maNhanVien;
                data[1] = dsHoaDonBanHang.get(i).getMaHoaDon();
                data[2] = dsHoaDonBanHang.get(i).getMaKhachHang();
                data[3] = hdb.getNgayLap() == null ? "" : hdb.getNgayLap().toString();
                bw.newLine();
                bw.write(String.join(",", data));
            }
        }
        return true;
    }

    /** Mỗi đối tượng tạo một dòng; thông tin con được ghi thành dòng riêng. */


    /** Có lỗi thì giữ danh sách đang dùng, không bỏ dòng lỗi. */

    // ================= ĐỌC / GHI FILE TXT =================

    public void ghiFile(String tenFile) throws IOException {
        ArrayList<String> cacDong = new ArrayList<>();
        Set<String> maDaGhi = new HashSet<>();
        for (HoaDonBanHang obj : dsHoaDonBanHang) {
            if (obj == null) throw new IOException("Danh sách chứa null.");
            TepVanBan.khongTrung(maDaGhi, obj.getMaHoaDon());
            cacDong.add(obj.toDongFile());
            Set<String> maChiTiet = new HashSet<>();
            for (CTHoaDonBan ct : obj.getCtHoaDonBans()) {
                TepVanBan.khongTrung(maChiTiet, ct.getMaSanpham());
                String[] p = ct.toDongFile().split(";", 2);
                cacDong.add(p[0] + ";" + obj.getMaHoaDon() + ";" + p[1]);
            }
            if (obj.getPhuongThucThanhToan() instanceof ThanhToanTienMat) {
                cacDong.add("THANHTOAN;" + obj.getMaHoaDon() + ";" + ((ThanhToanTienMat) obj.getPhuongThucThanhToan()).toDongFile());
            } else if (obj.getPhuongThucThanhToan() instanceof ThanhToanChuyenKhoan) {
                cacDong.add("THANHTOAN;" + obj.getMaHoaDon() + ";" + ((ThanhToanChuyenKhoan) obj.getPhuongThucThanhToan()).toDongFile());
            } else if (obj.getPhuongThucThanhToan() != null) {
                throw new IOException("Phương thức thanh toán chưa được hỗ trợ.");
            }
        }
        TepVanBan.ghi(tenFile, "DanhSachHoaDonBan", cacDong);
    }

    public void docFile(String tenFile) throws IOException {
        int[] demTruoc = BoDemMa.chup();
        ArrayList<HoaDonBanHang> moi = new ArrayList<>();
        Set<String> maDaDoc = new HashSet<>();
        Map<String, HoaDonBanHang> theoMa = new LinkedHashMap<>();
        Set<String> maChiTiet = new HashSet<>();
        Set<String> daCoThanhToan = new HashSet<>();
        try {
            for (String dong : TepVanBan.doc(tenFile)) {
                if (dong.startsWith("CTBAN;")) {
                    String[] p = dong.split(";", 3);
                    HoaDonBanHang chu = p.length == 3 ? theoMa.get(p[1].toUpperCase(Locale.ROOT)) : null;
                    DongDuLieu.yeuCau(chu != null, "Chi tiết phải đứng sau hóa đơn tương ứng.");
                    CTHoaDonBan ct = CTHoaDonBan.tuDongFile(p[0] + ";" + p[2]);
                    TepVanBan.khongTrung(maChiTiet, p[1] + ":" + ct.getMaSanpham());
                    chu.getCtHoaDonBans().add(ct);
                    continue;
                }
                if (dong.startsWith("THANHTOAN;")) {
                    String[] p = dong.split(";", 3);
                    HoaDonBanHang chu = p.length == 3 ? theoMa.get(p[1].toUpperCase(Locale.ROOT)) : null;
                    DongDuLieu.yeuCau(chu != null, "Thanh toán không có hóa đơn.");
                    TepVanBan.khongTrung(daCoThanhToan, p[1]);
                    if (p[2].startsWith("TIENMAT;")) chu.setPhuongThucThanhToan(ThanhToanTienMat.tuDongFile(p[2]));
                    else if (p[2].startsWith("CHUYENKHOAN;")) chu.setPhuongThucThanhToan(ThanhToanChuyenKhoan.tuDongFile(p[2]));
                    else throw new IllegalArgumentException("Loại thanh toán không hợp lệ.");
                    continue;
                }
                HoaDonBanHang obj = HoaDonBanHang.tuDongFile(dong);
                TepVanBan.khongTrung(maDaDoc, obj.getMaHoaDon());
                moi.add(obj);
                theoMa.put(obj.getMaHoaDon().toUpperCase(Locale.ROOT), obj);
            }
        } catch (RuntimeException | IOException e) {
            BoDemMa.khoiPhuc(demTruoc);
            throw new IOException("Không đọc được " + tenFile + ": " + e.getMessage(), e);
        }
        this.dsHoaDonBanHang = moi;
    }
}
