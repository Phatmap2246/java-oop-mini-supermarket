package hoadon;

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
                        case 3:
                            for (int i = 0; i < hd.getCtHoaDonBans().size(); i++) {
                                System.out.println((i + 1) + ". " + hd.getCtHoaDonBans().get(i));
                            }
                            int viTri = Validation.luaChon(sc, "Chọn dòng sản phẩm cần sửa hoặc xoá: ",
                                    1, hd.getCtHoaDonBans().size()) - 1;
                            if (luachon == 2) {
                                hd.getCtHoaDonBans().remove(viTri);
                                System.out.println("Xoá chi tiết sản phẩm thành công!");
                            } else {
                                CTHoaDonBan ctMoi = new CTHoaDonBan();
                                ctMoi.nhapThongTinBan(sc, qlsp, danhSachKhuyenMai, danhSachSPKM);
                                hd.getCtHoaDonBans().set(viTri, ctMoi);
                                System.out.println("Chỉnh sửa sản phẩm thành công!");
                            }
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
            if ( hdb.getNgayLap() != null && hdb.getNgayLap().isAfter(thoiGianBatDau) && hdb.getNgayLap().isBefore(thoiGianKetThuc))
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
            if ( hdb.getNgayLap() != null && hdb.getNgayLap().isAfter(thoiGianBatDau.atStartOfDay()) && hdb.getNgayLap().isBefore(thoiGianKetThuc.atStartOfDay()))
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
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã hóa đơn cần tìm: ", HoaDonBanHang.MA_HD_BAN_REGEX, "Mã hóa đơn không đúng định dạng. Vui lòng nhập lại!");
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
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã khách hàng cần tìm: ", Validation.MaKH_REGEX, "Mã khách hàng không đúng định dạng. Vui lòng nhập lại!");
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
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã nhân viên cần tìm: ", Validation.MaNV_REGEX, "Mã nhân viên không đúng định dạng. Vui lòng nhập lại!");
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
            if ( hdb.isDaThanhToan() && hdb.getNgayLap() != null && hdb.getNgayLap().isAfter(ngayBD.atStartOfDay()) && hdb.getNgayLap().isBefore(ngayKT.atStartOfDay()))
                tongTien += hdb.tinhTongTien();
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
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 5);
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
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        } while (luaChon != 0);
    }

    public void ghiFileText(String tenFile) {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(tenFile), java.nio.charset.StandardCharsets.UTF_8))) {
            for (HoaDonBanHang hdb : dsHoaDonBanHang) {
                if (hdb == null) continue;
                // Lưu thông tin chung: BAN|MaHD|MaKH|DaThanhToan
                bw.write("BAN|" + hdb.getMaHoaDon() + "|" + hdb.getMaKhachHang() + "|" + hdb.isDaThanhToan());
                bw.newLine();

                // Lưu chi tiết: CT|MaSP|TenSP|SoLuong|DonGia|SoTienGiam|SoLuongTangKem
                for (CTHoaDonBan ct : hdb.getCtHoaDonBans()) {
                    bw.write("CT|" + ct.getMaSanPham() + "|" + ct.getTenSanPham() + "|" +
                            ct.getSoLuong() + "|" + ct.getDonGia() + "|" +
                            ct.getSoTienGiam() + "|" + ct.getSoLuongTangKem());
                    bw.newLine();
                }
            }
            System.out.println("[+] Đã ghi dữ liệu hóa đơn bán hàng vào file: " + tenFile);
        } catch (Exception e) {
            System.out.println("[!] Lỗi khi ghi file: " + e.getMessage());
        }
    }

    public void docFileText(String tenFile) {
        this.dsHoaDonBanHang.clear();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(tenFile), java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            HoaDonBanHang hdbHienTai = null;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length > 0) {
                    String loai = parts[0];

                    if (loai.equals("BAN") && parts.length == 4) {
                        if (timMa(parts[1]) == null) {
                            hdbHienTai = new HoaDonBanHang(parts[1], LocalDateTime.now(), "", "Hoá đơn bán", null, Boolean.parseBoolean(parts[3]), 0);
                            hdbHienTai.setMaKhachHang(parts[2]);
                            hdbHienTai.setCtHoaDonBans(new ArrayList<>());
                            hdbHienTai.setDaThanhToan(Boolean.parseBoolean(parts[3]));
                            this.dsHoaDonBanHang.add(hdbHienTai);
                        } else {
                            hdbHienTai = null;
                        }
                    }
                    else if (loai.equals("CT") && hdbHienTai != null && parts.length == 7) {
                        CTHoaDonBan ct = new CTHoaDonBan(parts[1], parts[2], Integer.parseInt(parts[3]), Long.parseLong(parts[4]), "Không áp dụng");
                        ct.setSoTienGiam(Long.parseLong(parts[5]));
                        ct.setSoLuongTangKem(Integer.parseInt(parts[6]));
                        hdbHienTai.getCtHoaDonBans().add(ct);
                    }
                }
            }
            System.out.println("[+] Đã đọc dữ liệu hóa đơn bán hàng từ file: " + tenFile);
        } catch (Exception e) {
            System.out.println("[!] Lỗi khi đọc file (Có thể file chưa tồn tại): " + e.getMessage());
        }
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
}
