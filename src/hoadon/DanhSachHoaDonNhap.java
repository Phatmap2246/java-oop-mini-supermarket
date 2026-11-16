package hoadon;
import sanpham.ThucPham;
import utils.BoDemMa;
import utils.DongDuLieu;
import utils.TepVanBan;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import sanpham.SanPham;

import doitac.Nhacungcap;

import doitac.Doitac;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import doitac.Danhsachdoitac;
import sanpham.QuanLySanPham;
import utils.Validation;

public class DanhSachHoaDonNhap implements interfaces.IQuanLy<HoaDonNhapHang> {
    private transient String maNhanVienNhap;
    private transient QuanLySanPham nguonSanPham;
    private transient Danhsachdoitac nguonDoiTac;

    public void cauHinh(String maNV, QuanLySanPham sanPhams, Danhsachdoitac doiTacs) {
        maNhanVienNhap = maNV; nguonSanPham = sanPhams; nguonDoiTac = doiTacs;
    }
    private void kiemTraNguon() {
        if (maNhanVienNhap == null || nguonSanPham == null || nguonDoiTac == null) {
            throw new IllegalStateException("Cần gọi cauHinh để nối dữ liệu lập hóa đơn nhập.");
        }
    }
    @Override public void taoDanhSach(Scanner sc) {
        kiemTraNguon(); taoDanhSach(sc, maNhanVienNhap, nguonSanPham, nguonDoiTac);
    }
    @Override public void them(Scanner sc) {
        kiemTraNguon(); them(sc, maNhanVienNhap, nguonSanPham, nguonDoiTac);
    }
    @Override public boolean sua(String ma, Scanner sc) {
        HoaDonNhapHang hd = timMa(ma);
        if (hd == null || hd.getTrangThaiDuyet() != TrangThaiDuyet.CHO_DUYET || hd.isDaNhapSP()) return false;
        kiemTraNguon(); return sua(sc, ma, nguonSanPham);
    }
    @Override public boolean xoa(String ma) {
        HoaDonNhapHang hd = timMa(ma);
        if (hd == null || hd.getTrangThaiDuyet() != TrangThaiDuyet.TU_CHOI || hd.isDaNhapSP())
        {
            return false;
        }
        return dsHoaDonNhapHang.remove(hd);
    }
    @Override public int timViTri(String ma) {
        if (ma == null || ma.isBlank()) return -1;
        for (int i = 0; i < dsHoaDonNhapHang.size(); i++) {
            HoaDonNhapHang hd = dsHoaDonNhapHang.get(i);
            if (hd != null && ma.trim().equalsIgnoreCase(hd.getMaHoaDon())) return i;
        }
        return -1;
    }
    @Override public HoaDonNhapHang timTen(String ten) {
        if (ten == null || ten.isBlank()) return null;
        String khoa = ten.trim().toLowerCase(java.util.Locale.ROOT);
        for (HoaDonNhapHang hd : dsHoaDonNhapHang) {
            if (hd == null) continue;
            Doitac dt = nguonDoiTac == null ? null : nguonDoiTac.timMa(hd.getMaNhaCungCap());
            if (dt != null && dt.getTenDoiTac() != null && dt.getTenDoiTac().toLowerCase(java.util.Locale.ROOT).contains(khoa)) return hd;
            for (CTHoaDonNhap ct : hd.getCtHoaDonNhaps()) {
                if (ct != null && ct.getTenSanPham() != null && ct.getTenSanPham().toLowerCase(java.util.Locale.ROOT).contains(khoa)) return hd;
            }
        }
        return null;
    }
    @Override public HoaDonNhapHang timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) return null;
        for (HoaDonNhapHang hd : dsHoaDonNhapHang) {
            if (hd != null && hd.toString().toLowerCase(java.util.Locale.ROOT)
                    .contains(khoa.trim().toLowerCase(java.util.Locale.ROOT))) return hd;
        }
        return timTen(khoa);
    }
    private ArrayList<HoaDonNhapHang> dsHoaDonNhapHang;

    public DanhSachHoaDonNhap() {
        this.dsHoaDonNhapHang = new ArrayList<>();
    }

    public DanhSachHoaDonNhap(ArrayList<HoaDonNhapHang> danhSachHoaDonNhap) {
        if (danhSachHoaDonNhap == null) throw new IllegalArgumentException("Danh sách hóa đơn không được null.");
        this.dsHoaDonNhapHang = danhSachHoaDonNhap;
    }

    public ArrayList<HoaDonNhapHang> getDsHoaDonNhapHang() {
        return dsHoaDonNhapHang;
    }
    public void taoDanhSach(Scanner sc , String maNhanVien, QuanLySanPham qlsp , Danhsachdoitac dsDoiTac)
    {
        System.out.println("Tạo dữ liệu hoá đơn nhập!");
        do {
            them(sc, maNhanVien, qlsp , dsDoiTac);
        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục(Có/Không): "));
        System.out.println("Tạo dữ liệu hoá đơn nhập hàng thành công!");
    }

    @Override
    public void xuatDanhSach()
    {
        if ( dsHoaDonNhapHang.isEmpty() )
        {
            System.out.println("Danh sách hoá đơn nhập đang trống!");
            return;
        }
        System.out.println("==============DANH SÁCH HOÁ ĐƠN NHẬP===============");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for ( HoaDonNhapHang hdn : dsHoaDonNhapHang)
        {
            if (hdn == null) continue;
            hdn.hienThiHoaDon();
        }
    }
    public void them(Scanner sc , String maNhanVien, QuanLySanPham qlsp  , Danhsachdoitac dsDoiTac)
    {
        System.out.println("Nhập hoá đơn nhập!");
        HoaDonNhapHang hdn = new HoaDonNhapHang(maNhanVien) ;
        hdn.nhapHoaDon(sc , qlsp , dsDoiTac);
        dsHoaDonNhapHang.add(hdn) ;
        System.out.println("Đã thêm hoá đơn nhập hàng thành công!");
    }
    public void taoKHoaDonNhap ( Scanner sc , String maNhanVien, QuanLySanPham qlsp , Danhsachdoitac dsDoiTac, int k )
    {
        System.out.println("Tạo " + k + " Hoá đơn nhập hàng!");
        for ( int i = 0 ; i < k ; i++ )
        {
            them(sc, maNhanVien , qlsp , dsDoiTac);
        }
        System.out.println("Thêm " + k + " hoá đơn nhập thành công!");
    }

    public void nhapThemHang(Scanner sc , QuanLySanPham qlsp , Danhsachdoitac dsDoiTac, String maNhanVien)
    {
        ArrayList<CTHoaDonNhap> ctHoaDonNhaps = new ArrayList<CTHoaDonNhap>() ;
        ArrayList<HoaDonNhapHang> hoaDonNhapHangs = new ArrayList<>() ;
        while (true)
        {
            System.out.println("========================NHẬP THÊM HÀNG=====================");
            System.out.println("1. Xem và chọn các sản phẩm để nhập theo đề xuất. ");
            System.out.println("2. Nhập thêm sản phẩm mới!");
            System.out.println("3. Thêm sửa xoá hoá đơn nhập hàng!");
            System.out.println("0. Hoàn tất và tạo hoá đơn nhập.");
            int luaChon = Validation.luaChon(sc , "Nhập lựa chọn: " , 0 , 3 ) ;
            switch (luaChon)
            {
                case 1:
                    int luaChonDeXuat ;
                    do {
                        System.out.println("===================ĐANH SÁCH ĐỀ XUẤT NHẬP HÀNG===================");
                        System.out.println("1. Nhập sản phẩm đã bán hết!");
                        System.out.println("2. Nhập sản phẩm tồn kho thấp!");
                        System.out.println("3. Nhập sản phẩm sắp hết hạn!");
                        System.out.println("4. Nhập sản phẩm hết hạn");
                        System.out.println("0. Trở về.");
                        luaChonDeXuat = Validation.luaChon(sc , "Nhập lựa chọn: ", 0 , 4 ) ;
                        switch (luaChonDeXuat)
                        {
                            case 1:
                                qlsp.inDanhSachSanPhamBanHet();
                                System.out.println("==============NHẬP HÀNG BÁN HẾT=============");
                                System.out.println("1. Nhập hết sản phẩm!");
                                System.out.println("2. Nhập từng sản phẩm!");
                                int LuaChon = Validation.luaChon(sc , "Nhập lựa chọn: " , 1 , 2 );
                                switch (LuaChon)
                                {
                                    case 1:
                                        for ( SanPham sp: qlsp.danhSachSanPhamBanHet())
                                        {
                                            if (sp == null) continue;
                                            System.out.println("Nhập số lượng cần nhập thêm cho sản phẩm " + sp.getTenSanPham() + "!");
                                            CTHoaDonNhap ct = new CTHoaDonNhap() ;
                                            ct.nhapThongTin(sc , sp );
                                            ctHoaDonNhaps.add(ct) ;
                                        }
                                        System.out.println("Thêm sản phẩm đã bán hết vào hoá đơn nhập thành công!");
                                        break;
                                    case 2:
                                        do {
                                            CTHoaDonNhap ct = new CTHoaDonNhap();
                                            ct.nhapThongTin(sc, qlsp);
                                            ctHoaDonNhaps.add(ct) ;
                                        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục(Có/Không): "));
                                        System.out.println("Thêm sản phẩm đã bán hết vào hoá đơn nhập thành công!");
                                        break;
                                    default:
                                        System.out.println("Lựa chọn không hợp lệ!");
                                        break;
                                }
                                break;
                            case 2:
                                qlsp.inDanhSachTonKhoThap();
                                System.out.println("==============NHẬP HÀNG TỒN KHO THẤP=============");
                                System.out.println("1. Nhập hết sản phẩm!");
                                System.out.println("2. Nhập từng sản phẩm!");
                                int LuaChon2 = Validation.luaChon(sc , "Nhập lựa chọn: " , 1 , 2 );
                                switch (LuaChon2)
                                {
                                    case 1:
                                        for ( SanPham sp: qlsp.danhSachTonKhoThap())
                                        {
                                            if (sp == null) continue;
                                            System.out.println("Nhập số lượng cần nhập thêm cho sản phẩm " + sp.getTenSanPham() + "!");
                                            CTHoaDonNhap ct = new CTHoaDonNhap() ;
                                            ct.nhapThongTin(sc , sp );
                                            ctHoaDonNhaps.add(ct) ;
                                        }
                                        System.out.println("Thêm sản phẩm đã bán hết vào hoá đơn nhập thành công!");
                                        break;
                                    case 2:
                                        do {
                                            CTHoaDonNhap ct = new CTHoaDonNhap();
                                            ct.nhapThongTin(sc, qlsp);
                                            ctHoaDonNhaps.add(ct) ;
                                        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục(Có/Không): "));
                                        System.out.println("Thêm sản phẩm đã bán hết vào hoá đơn nhập thành công!");
                                        break;
                                    default:
                                        System.out.println("Lựa chọn không hợp lệ!");
                                        break;
                                }
                                break;
                            case 3:
                                qlsp.inDanhSachSapHetHan();
                                System.out.println("==============NHẬP HÀNG TỒN KHO THẤP=============");
                                System.out.println("1. Nhập hết sản phẩm!");
                                System.out.println("2. Nhập từng sản phẩm!");
                                int LuaChon3 = Validation.luaChon(sc , "Nhập lựa chọn: " , 1 , 2 );
                                switch (LuaChon3)
                                {
                                    case 1:
                                        for ( SanPham sp: qlsp.danhSachSapHetHan())
                                        {
                                            if (sp == null) continue;
                                            System.out.println("Nhập số lượng cần nhập thêm cho sản phẩm " + sp.getTenSanPham() + "!");
                                            CTHoaDonNhap ct = new CTHoaDonNhap() ;
                                            ct.nhapThongTin(sc , sp );
                                            ctHoaDonNhaps.add(ct) ;
                                        }
                                        System.out.println("Thêm sản phẩm đã bán hết vào hoá đơn nhập thành công!");
                                        break;
                                    case 2:
                                        do {
                                            CTHoaDonNhap ct = new CTHoaDonNhap();
                                            ct.nhapThongTin(sc, qlsp);
                                            ctHoaDonNhaps.add(ct) ;
                                        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục(Có/Không): "));
                                        System.out.println("Thêm sản phẩm đã bán hết vào hoá đơn nhập thành công!");
                                        break;
                                    default:
                                        System.out.println("Lựa chọn không hợp lệ!");
                                        break;
                                }
                                break;
                            case 4:
                                qlsp.inDanhSachQuaHan();
                                System.out.println("==============NHẬP HÀNG TỒN KHO THẤP=============");
                                System.out.println("1. Nhập hết sản phẩm!");
                                System.out.println("2. Nhập từng sản phẩm!");
                                int LuaChon4 = Validation.luaChon(sc , "Nhập lựa chọn: " , 1 , 2 );
                                switch (LuaChon4)
                                {
                                    case 1:
                                        for ( SanPham sp: qlsp.danhSachQuaHan())
                                        {
                                            if (sp == null) continue;
                                            System.out.println("Nhập số lượng cần nhập thêm cho sản phẩm " + sp.getTenSanPham() + "!");
                                            CTHoaDonNhap ct = new CTHoaDonNhap() ;
                                            ct.nhapThongTin(sc , sp );
                                            ctHoaDonNhaps.add(ct) ;
                                        }
                                        System.out.println("Thêm sản phẩm đã bán hết vào hoá đơn nhập thành công!");
                                        break;
                                    case 2:
                                        do {
                                            CTHoaDonNhap ct = new CTHoaDonNhap();
                                            ct.nhapThongTin(sc, qlsp);
                                            ctHoaDonNhaps.add(ct) ;
                                        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục(Có/Không): "));
                                        System.out.println("Thêm sản phẩm đã bán hết vào hoá đơn nhập thành công!");
                                        break;
                                    default:
                                        System.out.println("Lựa chọn không hợp lệ!");
                                        break;
                                }
                                break;
                            case 0:
                                break;
                            default:
                                System.out.println("Lựa chọn không hợp lệ!");
                                break;
                        }
                    }
                    while ( luaChonDeXuat != 0 ) ;
                    break;
                case 2:
                    ArrayList<Nhacungcap> dsNhaCungCap = new ArrayList<>() ;
                    for (Doitac dt: dsDoiTac.getDanhSachDoiTac())
                    {
                        if ( dt instanceof Nhacungcap)
                            dsNhaCungCap.add((Nhacungcap) dt) ;
                    }
                    if ( dsNhaCungCap.isEmpty() )
                    {
                        System.out.println("Vui lòng thêm nhà cung cấp để nhập thêm sản phẩm!");
                        break;
                    }
                    do {
                        int soLuongCu = qlsp.getDanhSachSanPham().size() ;
                        qlsp.them(sc, dsNhaCungCap);
                        SanPham spMoi = null;
                        if ( qlsp.getDanhSachSanPham().size() > soLuongCu )
                        {
                            spMoi = qlsp.getDanhSachSanPham().getLast();
                        }
                        if (spMoi == null) {
                            System.out.println("Chưa thêm được sản phẩm, quay lại menu.");
                            break;
                        }
                        spMoi.setSoLuongTonKho(0);
                        CTHoaDonNhap ct = new CTHoaDonNhap() ;
                        ct.nhapThongTin(sc , spMoi);
                        ctHoaDonNhaps.add(ct) ;
                    } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục(Có/Không)): "));
                    break;
                case 3:
                    System.out.println("==============CHỈNH SỬA HOÁ ĐƠN NHẬP==============");
                    System.out.println("1. Thêm sản phẩm cần nhập!");
                    System.out.println("2. Xoá sản phẩm khỏi chi tiết hoá đơn!");
                    System.out.println("3. Thay đổi sản phẩm cần nhập!");
                    int luaChonChiTiet = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, 3);
                    if (luaChonChiTiet == 1) {
                        CTHoaDonNhap ctMoi = new CTHoaDonNhap();
                        ctMoi.nhapThongTin(sc, qlsp);
                        ctHoaDonNhaps.add(ctMoi);
                        System.out.println("Thêm chi tiết sản phẩm thành công!");
                        break;
                    }
                    if (ctHoaDonNhaps.isEmpty()) {
                        System.out.println("Chi tiết hoá đơn nhập đang trống!");
                        break;
                    }
                    for (int i = 0; i < ctHoaDonNhaps.size(); i++) {
                        System.out.println((i + 1) + ". " + ctHoaDonNhaps.get(i));
                    }
                    int viTri = Validation.luaChon(sc,
                            "Chọn dòng sản phẩm cần sửa hoặc xoá: ", 1, ctHoaDonNhaps.size()) - 1;
                    if (luaChonChiTiet == 2) {
                        ctHoaDonNhaps.remove(viTri);
                        System.out.println("Xoá chi tiết sản phẩm thành công!");
                    } else {
                        CTHoaDonNhap ctMoi = new CTHoaDonNhap();
                        ctMoi.nhapThongTin(sc, qlsp);
                        ctHoaDonNhaps.set(viTri, ctMoi);
                        System.out.println("Chỉnh sửa sản phẩm thành công!");
                    }
                    break;
                case 0:
                    if ( ctHoaDonNhaps.isEmpty())
                    {
                        System.out.println("Chi tiết hoá đơn đang trống, không tạo được hoá đơn!");
                        return;
                    }
                    for ( CTHoaDonNhap ct: ctHoaDonNhaps )
                    {
                        if (ct == null) continue;
                        SanPham sp = qlsp.timMa(ct.getMaSanpham()) ;
                        boolean flag = false ;
                        if ( sp != null )
                        {
                            for ( HoaDonNhapHang hdn: hoaDonNhapHangs)
                            {
                                if (hdn == null) continue;
                                if ( hdn.getMaNhaCungCap() != null && hdn.getMaNhaCungCap().equalsIgnoreCase(sp.getMaNhaCungCap()) )
                                {
                                    hdn.getCtHoaDonNhaps().add(ct) ;
                                    flag = true ;
                                    break;
                                }
                            }
                        }
                        if ( !flag && sp != null )
                        {
                            ArrayList<CTHoaDonNhap> chiTietMoi = new ArrayList<>();
                            chiTietMoi.add(ct);
                            HoaDonNhapHang hdn = new HoaDonNhapHang(maNhanVien) ;
                            int indexNCC = dsDoiTac.timViTri(sp.getMaNhaCungCap());
                            if (indexNCC == -1) continue;
                            System.out.println("Nhập phiếu nhập cho từ nhà cung cấp " + dsDoiTac.getDanhSachDoiTac().get(indexNCC).getTenDoiTac());
                            hdn.nhapHoaDon(sc , chiTietMoi , sp.getMaNhaCungCap());
                            hoaDonNhapHangs.add(hdn) ;
                        }
                    }
                    for ( HoaDonNhapHang hdn : hoaDonNhapHangs)
                    {
                        if (hdn == null) continue;
                        dsHoaDonNhapHang.add(hdn) ;
                        System.out.println(hdn);
                        System.out.println("Tạo hoá đơn thành công!");
                    }
                    System.out.println("Hoá đơn đang chờ quản lý duyệt và gửi đến từng nhà cung cấp!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        }
    }

    public boolean sua(Scanner sc, String maHoaDon, QuanLySanPham qlsp)
    {
        HoaDonNhapHang hdn = timMa(maHoaDon);
        if (hdn == null) {
            System.out.println("Mã hoá đơn không tồn tại!");
            return false;
        }
        if (hdn.getTrangThaiDuyet() != TrangThaiDuyet.CHO_DUYET || hdn.isDaNhapSP()) {
            System.out.println("Chỉ hóa đơn chờ duyệt và chưa nhập kho mới được chỉnh sửa!");
            return false;
        }
        System.out.println("Nhập thuộc tính của hoá đơn cần sửa: ");
            System.out.println("1. Sửa mã nhà cung cấp!");
            System.out.println("2. Sửa chi tiết hoá đơn!");
            System.out.println("3. Sửa thuế VAT!");
            System.out.println("4. Sửa phí vận chuyển!");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, 4);
            switch (luaChon) {
                case 1:
                    hdn.setMaNhaCungCap(Validation.inputStringRegex(sc,
                            "Nhập mã nhà cung cấp mới: ", HoaDonNhapHang.MA_NCC_REGEX,
                            "Mã nhà cung cấp không đúng định dạng (VD: NCC001)!", Nhacungcap.getSoThuTuNCC()));
                    System.out.println("Sửa mã nhà cung cấp thành công!");
                    break;
                case 2:
                    System.out.println("1. Thêm sản phẩm cần nhập!");
                    System.out.println("2. Xoá sản phẩm khỏi chi tiết hoá đơn!");
                    System.out.println("3. Thay đổi sản phẩm cần nhập!");
                    int luaChonChiTiet = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, 3);
                    ArrayList<CTHoaDonNhap> chiTiet = hdn.getCtHoaDonNhaps();
                    if (luaChonChiTiet == 1) {
                        CTHoaDonNhap ctMoi = new CTHoaDonNhap();
                        ctMoi.nhapThongTin(sc, qlsp);
                        chiTiet.add(ctMoi);
                        System.out.println("Thêm chi tiết sản phẩm thành công!");
                        break;
                    }
                    if (chiTiet.isEmpty()) {
                        System.out.println("Chi tiết hoá đơn nhập đang trống!");
                        return false;
                    }
                    for (int i = 0; i < chiTiet.size(); i++) {
                        System.out.println((i + 1) + ". " + chiTiet.get(i));
                    }
                    int viTri = Validation.luaChon(sc,
                            "Chọn dòng sản phẩm cần sửa hoặc xoá: ", 1, chiTiet.size()) - 1;
                    if (luaChonChiTiet == 2) {
                        chiTiet.remove(viTri);
                        System.out.println("Xoá chi tiết sản phẩm thành công!");
                    } else {
                        CTHoaDonNhap ctMoi = new CTHoaDonNhap();
                        ctMoi.nhapThongTin(sc, qlsp);
                        chiTiet.set(viTri, ctMoi);
                        System.out.println("Chỉnh sửa sản phẩm thành công!");
                    }
                    break;
                case 3:
                    double vat;
                    do {
                        vat = Validation.inputDouble(sc, "Nhập thuế VAT mới (0-100%): ");
                        if (!Double.isFinite(vat) || vat < 0 || vat > 100) {
                            System.out.println("Thuế VAT phải từ 0 đến 100%!");
                        }
                    } while (!Double.isFinite(vat) || vat < 0 || vat > 100);
                    hdn.setThueVAT(vat);
                    System.out.println("Sửa thuế VAT thành công!");
                    break;
                case 4:
                    hdn.setChiPhiVanChuyen(Validation.inputLong(sc,
                            "Nhập phí vận chuyển mới (VND): "));
                    System.out.println("Sửa phí vận chuyển thành công!");
                    break;
                default:
                    break;
            }
        return true;
    }
    @Override
    public HoaDonNhapHang timMa( String maHoaDon )
    {
        if (maHoaDon == null || maHoaDon.isBlank()) return null;
        maHoaDon = maHoaDon.trim();
        if ( dsHoaDonNhapHang.isEmpty() )
        {
            System.out.println("Danh sách hoá đơn đang trống!");
            return null ;
        }
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang )
        {
            if (hdn == null) continue;
            if ( hdn.getMaHoaDon() != null && hdn.getMaHoaDon().equalsIgnoreCase(maHoaDon))
                return hdn ;
        }
        System.out.println("Không tìm thấy hóa đơn phù hợp!");
        return null ;
    }
    public void traCuuHoaDonTrenKhoangThoiGian (LocalDateTime thoiGianBatDau , LocalDateTime thoiGianKetThuc )
    {
        if ( thoiGianBatDau == null || thoiGianKetThuc == null )
        {
            System.out.println("Bạn chưa nhập thời gian!");
            return;
        }
        if ( thoiGianBatDau.isAfter(thoiGianKetThuc) )
        {
            LocalDateTime tam = thoiGianBatDau ;
            thoiGianBatDau = thoiGianKetThuc ;
            thoiGianKetThuc = tam ;
        }
        System.out.println("Các hoá đơn nhập được in vào khoảng thời gian từ " + thoiGianBatDau + " đến " + thoiGianKetThuc + "là: ");
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang )
        {
            if (hdn == null) continue;
            if ( hdn.getNgayLap() != null && !hdn.getNgayLap().isAfter(thoiGianKetThuc) && !hdn.getNgayLap().isBefore(thoiGianBatDau))
            {
                hdn.hienThiHoaDon();
            }
        }
    }
    public void traCuuHoaDonTrenKhoangThoiGian (LocalDate thoiGianBatDau , LocalDate thoiGianKetThuc )
    {
        if ( thoiGianBatDau == null || thoiGianKetThuc == null )
        {
            System.out.println("Bạn chưa nhập thời gian!");
            return;
        }
        if ( thoiGianBatDau.isAfter(thoiGianKetThuc) )
        {
            LocalDate tam = thoiGianBatDau ;
            thoiGianBatDau = thoiGianKetThuc ;
            thoiGianKetThuc = tam ;
        }
        System.out.println("Các hoá đơn bán được in vào khoảng thời gian từ " + thoiGianBatDau + " đến " + thoiGianKetThuc + "là: ");
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang )
        {
            if (hdn == null) continue;
            LocalDate ngayLap = hdn.getNgayLap().toLocalDate() ;
            if ( ngayLap != null && !ngayLap.isAfter(thoiGianKetThuc) && !ngayLap.isBefore(thoiGianBatDau))
            {
                hdn.hienThiHoaDon();
            }
        }
    }
    public void traCuuHoaDonTrenKhoangThoiGian ( int gioBatDau , int gioKetThuc , LocalDate ngay)
    {
        if ( ngay == null  )
        {
            System.out.println("Bạn chưa nhập thời gian!");
            return;
        }
        if (gioBatDau < 0 || gioBatDau > 23
                || gioKetThuc < 0 || gioKetThuc > 23) {
            System.out.println("Giờ phải từ 0 đến 23!");
            return;
        }
        if ( gioBatDau > gioKetThuc )
        {
            int tam = gioBatDau ;
            gioBatDau = gioKetThuc ;
            gioKetThuc = tam ;
        }
        System.out.println("Các hoá đơn bán được in vào khoảng thời gian từ " + gioBatDau + " giờ đến " + gioKetThuc + " giờ ngày " + LocalDate.now() + "là: ");
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang )
        {
            if (hdn == null) continue;
            if ( hdn.getNgayLap() != null && hdn.getNgayLap().toLocalDate().equals(ngay) && hdn.getNgayLap().getHour() >= gioBatDau && hdn.getNgayLap().getHour() <= gioKetThuc )
            {
                hdn.hienThiHoaDon();
            }
        }
    }

    public void timKhoa(Scanner sc) {
        while (true) {
            System.out.println("================ TÌM KIẾM HÓA ĐƠN NHẬP ================");
            System.out.println("1. Mã hóa đơn");
            System.out.println("2. Mã nhà cung cấp");
            System.out.println("3. Khoảng thời gian");
            System.out.println("4. Mã nhân viên");
            System.out.println("0. Trở về");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 8);
            if (luaChon == 0) return;
            boolean flag = false;
            System.out.println("Kết quả tra cứu:");
            switch (luaChon) {
                case 1: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã hóa đơn cần tìm: ", "(?i)" + HoaDonNhapHang.MA_HD_NHAP_REGEX, "Mã hóa đơn không đúng định dạng. Vui lòng nhập lại!", HoaDon.getSoThuTu());
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (HoaDonNhapHang hdn : dsHoaDonNhapHang) {
                        if (hdn != null && ((hdn.getMaHoaDon()) != null && hdn.getMaHoaDon().equalsIgnoreCase(tuKhoa))) {
                            hdn.hienThiHoaDon();
                            flag = true;
                        }
                    }
                    break;
                }
                case 2: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã nhà cung cấp cần tìm: ", Validation.MaNCC_REGEX, "Mã nhà cung cấp không đúng định dạng. Vui lòng nhập lại!", Nhacungcap.getSoThuTuNCC());
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (HoaDonNhapHang hdn : dsHoaDonNhapHang) {
                        if (hdn != null && ((hdn.getMaNhaCungCap()) != null && hdn.getMaNhaCungCap().equalsIgnoreCase(tuKhoa))) {
                            hdn.hienThiHoaDon();
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
                            traCuuHoaDonTrenKhoangThoiGian( tu , den );
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
                            traCuuHoaDonTrenKhoangThoiGian(tu , den , ngay);
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
                            traCuuHoaDonTrenKhoangThoiGian(tu , den );
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
                    for (HoaDonNhapHang hdn : dsHoaDonNhapHang) {
                        if (hdn != null && ((hdn.getMaNhanVien()) != null && hdn.getMaNhanVien().equalsIgnoreCase(tuKhoa))) {
                            hdn.hienThiHoaDon();
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

    public void ktraHoaDonDaDuyet()
    {
        System.out.println("Các hoá đơn nhập đã được quản lý duyệt!");
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang ) {
            if (hdn != null && hdn.isDuyet()) {
                System.out.println(hdn);
            }
        }
    }

    public void kiemTraThongTinSanPham( String maSanPham)
    {
        System.out.println("===================THÔNG TIN NHẬP HÀNG CỦA SẢN PHẨM===================");
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang)
        {
            if (hdn == null) continue;
            if ( hdn.isDaNhapSP() )
            {
                for ( CTHoaDonNhap ct: hdn.getCtHoaDonNhaps() )
                {
                    if (ct == null) continue;
                    if (ct.getMaSanpham() != null && ct.getMaSanpham().equalsIgnoreCase(maSanPham)) {
                        System.out.println(ct + " | Thời gian nhập: " + hdn.getNgayLap() + " | Nhân viên nhập: " + hdn.getMaNhanVien());
                        break;
                    }
                }
            }
        }
    }

    public int kiemTraSoLuongSPNhapGanNhat( String maSanPham)
    {
        for ( int i = dsHoaDonNhapHang.size() - 1 ; i >= 0 ; i-- )
        {
            if (dsHoaDonNhapHang.get(i) == null) continue;
            if ( dsHoaDonNhapHang.get(i).isDaNhapSP() )
            {
                for ( CTHoaDonNhap ct: dsHoaDonNhapHang.get(i).getCtHoaDonNhaps() )
                {
                    if (ct == null) continue;
                    if (ct.getMaSanpham() != null && ct.getMaSanpham().equalsIgnoreCase(maSanPham))
                        return ct.getSoLuong();
                }
            }
        }
        System.out.println("Sản phẩm này chưa nhập bao giờ!");
        return 0 ;
    }
    // tìm kiếm cần lọc các cái có tồn tại đã tồn tại thanh toán hay đã chưa
    public void thongKeCacNgayNhapHang()
    {
        int tongHoaDon = 0 ;
        System.out.println("==================LỊCH SỬ NHẬP HÀNG==================");
        System.out.println("Danh sách các ngày nhập hàng: ");
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang )
        {
            if (hdn == null) continue;
            if ( hdn.isDaNhapSP())
            {
                System.out.println("Ngày nhập hàng: " + hdn.getNgayLap() + " | Nhập bởi: " + hdn.getMaNhanVien());
                tongHoaDon++ ;
            }
        }
        System.out.println("Tổng hoá đơn đã nhập hàng là: " + tongHoaDon);
    }
    public void thongKeHoaDonTheoNhaCungCap( String maNCC)
    {
        int tongHoaDon = 0 ;
        boolean flag = false ;
        System.out.println("Danh sách hoá đơn nhập hàng từ nhà cung cấp có mã là: " + maNCC);
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for (HoaDonNhapHang hdn : dsHoaDonNhapHang) {
            if (hdn != null && ((hdn.getMaNhaCungCap()) != null && hdn.getMaNhaCungCap().equalsIgnoreCase(maNCC))) {
                hdn.hienThiHoaDon();
                tongHoaDon++ ;
                flag = true;
            }
        }
        System.out.println("Tổng hoá đơn nhập từ nhà cung cấp " + maNCC + " là: " + tongHoaDon);
        if (!flag)
        {
            System.out.println("Chưa có hoá đơn nhập nào đến từ nhà cung cấp có mã là " + maNCC);
            return;
        }
    }
    public void thongKeCacDonChuaDuyet ()
    {
        int tongHoaDon = 0 ;
        System.out.println("==================DANH SÁCH HOÁ ĐƠN CHỜ DUYỆT==================");
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang )
        {
            if (hdn == null) continue;
            if (hdn.getTrangThaiDuyet() == TrangThaiDuyet.CHO_DUYET)
            {
                hdn.hienThiHoaDon();
                tongHoaDon++ ;
            }
        }
        System.out.println("Tổng hoá đơn chờ duyệt là: " + tongHoaDon);
    }
    public void thongKeCacDonChuaNhap()
    {
        int tongHoaDon = 0 ;
        System.out.println("==================DANH SÁCH HOÁ ĐƠN CHƯA NHẬP==================");
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang )
        {
            if (hdn == null) continue;
            if ( !hdn.isDaNhapSP() )
            {
                hdn.hienThiHoaDon();
                tongHoaDon++ ;
            }
        }
        System.out.println("Tổng hoá đơn chưa nhập kho là: " + tongHoaDon);
    }
    public long tongTienNhap(LocalDate ngayBD , LocalDate ngayKT )
    {
        if (ngayBD == null || ngayKT == null) {
            System.out.println("Bạn chưa nhập khoảng thời gian!");
            return 0;
        }
        long tongTien = 0 ;
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang)
        {
            if (hdn == null) continue;
            if ( hdn.getNgayLap() != null ) {
                LocalDate ngayLap = hdn.getNgayLap().toLocalDate();
                if (!ngayLap.isBefore(ngayBD) && !ngayLap.isAfter(ngayKT)) {
                    tongTien += hdn.tinhTongTien() ;
                }
            }
        }
        return tongTien ;
    }
    public void thongKeTongTienNhap( Scanner sc )
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
        System.out.println("Tổng tiền nhập từ nhập từ ngày " + ngayBD + " đến ngày: " + ngayKT + " là: " + tongTienNhap(ngayBD , ngayKT));
    }

    public long tinhTongChiPhi(ArrayList<HoaDon> dshd,long songay) {
        if (dshd == null || dshd.isEmpty()) return 0;
        long tongChiPhi = 0;
        LocalDate lastDay = LocalDate.now();
        for (HoaDon hoaDon : dshd) {
            if (hoaDon != null && hoaDon.getNgayLap() != null)
                lastDay = hoaDon.getNgayLap().toLocalDate();
        }
        for (int i = dshd.size()-1; i>=0; i--) {
            if (dshd.get(i) != null && dshd.get(i).getNgayLap() != null && dshd.get(i) instanceof HoaDonNhapHang && dshd.get(i).getNgayLap().toLocalDate().isAfter( lastDay.minusDays(songay)) ) {
                tongChiPhi += dshd.get(i).tinhTongTien();
            }
        }
        return tongChiPhi;
    }

    public long tinhTongChiPhi(long songay)
    {
        ArrayList<HoaDon> danhSach = new ArrayList<>();
        for (HoaDon hoaDon : dsHoaDonNhapHang) {
            danhSach.add(hoaDon);
        }
        return tinhTongChiPhi(danhSach, songay);
    }

    public void xemBaoCao(Scanner sc)
    {
        int luaChon ;
        do {
            System.out.println("===================BÁO CÁO HÓA ĐƠN NHẬP===================");
            System.out.println("1. Xuất danh sách hóa đơn nhập!");
            System.out.println("2. Tìm kiếm hóa đơn nhập!");
            System.out.println("3. Lịch sử ngày nhập hàng!");
            System.out.println("4. Thống kê theo nhà cung cấp!");
            System.out.println("5. Thống kê hóa đơn chưa duyệt!");
            System.out.println("6. Thống kê hóa đơn chưa nhập kho!");
            System.out.println("7. Thống kê tổng tiền nhập!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 7);
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
                    thongKeCacNgayNhapHang();
                    break;
                }
                case 4: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã nhà cung cấp: ", Validation.MaNCC_REGEX, "Mã nhà cung cấp không đúng định dạng!", Nhacungcap.getSoThuTuNCC());
                    thongKeHoaDonTheoNhaCungCap(ma);
                    break;
                }
                case 5: {
                    thongKeCacDonChuaDuyet();
                    break;
                }
                case 6: {
                    thongKeCacDonChuaNhap();
                    break;
                }
                case 7: {
                    thongKeTongTienNhap(sc);
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

    public void capNhatKho(QuanLySanPham qlsp)
    {
        ktraHoaDonDaDuyet();
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang)
        {
            if (hdn == null || !hdn.isDuyet() || hdn.isDaNhapSP()) continue;
            int soSanPham = qlsp.getDanhSachSanPham().size();
            int[] tonDuKien = new int[soSanPham];
            for (int i = 0; i < soSanPham; i++) {
                tonDuKien[i] = qlsp.getDanhSachSanPham()
                        .get(i).getSoLuongTonKho();
            }
            boolean hopLe = !hdn.getCtHoaDonNhaps().isEmpty();

            for (CTHoaDonNhap ct : hdn.getCtHoaDonNhaps()) {
                if (ct == null || ct.getSoLuong() <= 0) {
                    System.out.println("Chi tiết nhập không hợp lệ!");
                    hopLe = false;
                    break;
                }

                int indexSP = qlsp.timViTri(ct.getMaSanpham());

                if (indexSP == -1) {
                    System.out.println("Không tìm thấy: " + ct.getMaSanpham());
                    hopLe = false;
                    break;
                }

                long soLuongMoi = (long) tonDuKien[indexSP] + ct.getSoLuong();

                if (soLuongMoi < 0 || soLuongMoi > Integer.MAX_VALUE) {
                    System.out.println("Số lượng nhập kho vượt giới hạn!");
                    hopLe = false;
                    break;
                }

                tonDuKien[indexSP] = (int) soLuongMoi;
            }
            if (hopLe) {
                for (int i = 0; i < soSanPham; i++) {
                    qlsp.getDanhSachSanPham().get(i)
                            .setSoLuongTonKho(tonDuKien[i]);
                    if ( qlsp.getDanhSachSanPham().get(i) instanceof ThucPham)
                    {
                        long soNgaySuDung = ChronoUnit.DAYS.between(((ThucPham) qlsp.getDanhSachSanPham().get(i)).getNgaySanXuat() , ((ThucPham) qlsp.getDanhSachSanPham().get(i)).getHanSuDung());
                        ((ThucPham) qlsp.getDanhSachSanPham().get(i)).setHanSuDung(hdn.getNgayLap().toLocalDate().plusDays(soNgaySuDung)) ;
                        ((ThucPham) qlsp.getDanhSachSanPham().get(i)).setNgaySanXuat(hdn.getNgayLap().toLocalDate());
                    }
                }

                hdn.setDaNhapSP(true);
                System.out.println("Nhập kho thành công!");
            } else {
                System.out.println("Hóa đơn chưa được nhập kho.");
            }
        }
    }

    public void quanLyDanhSach(Scanner sc, String maNhanVien, QuanLySanPham qlsp, Danhsachdoitac dsDoiTac)
    {
        cauHinh(maNhanVien, qlsp, dsDoiTac);
        int luaChon ;
        do {
            System.out.println("===================QUẢN LÝ HÓA ĐƠN NHẬP===================");
            System.out.println("1. Nhập danh sách hóa đơn nhập!");
            System.out.println("2. Xuất danh sách hóa đơn nhập!");
            System.out.println("3. Thêm hóa đơn nhập!");
            System.out.println("4. Thêm k hóa đơn nhập!");
            System.out.println("5. Xóa hóa đơn bị từ chối!");
            System.out.println("6. Sửa hóa đơn nhập!");
            System.out.println("7. Tìm kiếm hóa đơn nhập!");
            System.out.println("8. Xem báo cáo nhập hàng!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 8);
            switch (luaChon)
            {
                case 1: {
                    taoDanhSach(sc, maNhanVien, qlsp, dsDoiTac);
                    break;
                }
                case 2: {
                    xuatDanhSach();
                    break;
                }
                case 3: {
                    them(sc, maNhanVien, qlsp, dsDoiTac);
                    break;
                }
                case 4: {
                    int k = Validation.inputInt(sc, "Nhập số phần tử cần thêm: ");
                    taoKHoaDonNhap(sc, maNhanVien, qlsp, dsDoiTac, k);
                    break;
                }
                case 5: {
                    xoaHoaDonBiTuChoi(sc);
                    break;
                }
                case 6: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã hóa đơn: ", "(?i)" + HoaDonNhapHang.MA_HD_NHAP_REGEX, "Mã hóa đơn không đúng định dạng!", HoaDon.getSoThuTu());
                    sua(sc, ma, qlsp);
                    break;
                }
                case 7: {
                    timKhoa(sc);
                    break;
                }
                case 8: {
                    xemBaoCao(sc);
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

    private String nhapMaXuLy(Scanner sc) {
        while (true) {
            System.out.print("Nhập mã hóa đơn (0 để quay lại): ");
            if (!sc.hasNextLine()) return "0";
            String ma = sc.nextLine().trim();
            if (ma.equals("0") || ma.matches(HoaDonNhapHang.MA_HD_NHAP_REGEX)) return ma;
            System.out.println("Mã hóa đơn không đúng định dạng!");
        }
    }

    public void duyetHoaDonNhap(Scanner sc) {
        thongKeCacDonChuaDuyet();
        if (dsHoaDonNhapHang.stream().noneMatch(hd -> hd != null
                && hd.getTrangThaiDuyet() == TrangThaiDuyet.CHO_DUYET && !hd.isDaNhapSP())) {
            System.out.println("Không có hóa đơn chờ duyệt.");
            return;
        }
        String ma = nhapMaXuLy(sc);
        if (ma.equals("0")) return;
        HoaDonNhapHang hd = timMa(ma);
        if (hd == null) { System.out.println("Mã hóa đơn không tồn tại!"); return; }
        if (hd.getTrangThaiDuyet() != TrangThaiDuyet.CHO_DUYET || hd.isDaNhapSP()) {
            System.out.println("Chỉ có thể xử lý hóa đơn chờ duyệt và chưa nhập kho."); return;
        }
        hd.hienThiHoaDon();
        int chon = Validation.luaChon(sc, "1. Duyệt / 2. Từ chối / 0. Quay lại: ", 0, 2);
        if (chon == 0) return;
        hd.setTrangThaiDuyet(chon == 1 ? TrangThaiDuyet.DA_DUYET : TrangThaiDuyet.TU_CHOI);
        System.out.println("Trạng thái hóa đơn: " + hd.getTrangThaiDuyet());
    }

    public void xoaHoaDonBiTuChoi(Scanner sc) {
        boolean coHoaDon = false;
        for (HoaDonNhapHang hd : dsHoaDonNhapHang) {
            if (hd != null && hd.getTrangThaiDuyet() == TrangThaiDuyet.TU_CHOI && !hd.isDaNhapSP()) {
                hd.hienThiHoaDon();
                coHoaDon = true;
            }
        }
        if (!coHoaDon) { System.out.println("Không có hóa đơn bị từ chối để xóa."); return; }
        String ma = nhapMaXuLy(sc);
        if (ma.equals("0")) return;
        HoaDonNhapHang hd = timMa(ma);
        if (hd == null) { System.out.println("Mã hóa đơn không tồn tại!"); return; }
        if (hd.getTrangThaiDuyet() != TrangThaiDuyet.TU_CHOI || hd.isDaNhapSP()) {
            System.out.println("Chỉ được xóa hóa đơn bị từ chối và chưa nhập kho."); return;
        }
        if (Validation.inputCoKhong(sc, "Xác nhận xóa hóa đơn " + hd.getMaHoaDon() + "?")) {
            if (xoa(ma)) System.out.println("Đã xóa hóa đơn bị từ chối.");
        }
    }

    /** Mỗi đối tượng tạo một dòng; thông tin con được ghi thành dòng riêng. */


    /** Có lỗi thì giữ danh sách đang dùng, không bỏ dòng lỗi. */

    // ================= ĐỌC / GHI FILE TXT =================

    public void ghiFile(String tenFile) throws IOException {
        ArrayList<String> cacDong = new ArrayList<>();
        Set<String> maDaGhi = new HashSet<>();
        for (HoaDonNhapHang obj : dsHoaDonNhapHang) {
            if (obj == null) throw new IOException("Danh sách chứa null.");
            TepVanBan.khongTrung(maDaGhi, obj.getMaHoaDon());
            cacDong.add(obj.toDongFile());
            Set<String> maChiTiet = new HashSet<>();
            for (CTHoaDonNhap ct : obj.getCtHoaDonNhaps()) {
                TepVanBan.khongTrung(maChiTiet, ct.getMaSanpham());
                String[] p = ct.toDongFile().split(";", 2);
                cacDong.add(p[0] + ";" + obj.getMaHoaDon() + ";" + p[1]);
            }
        }
        TepVanBan.ghi(tenFile, "DanhSachHoaDonNhap", cacDong);
    }

    public void docFile(String tenFile) throws IOException {
        int[] demTruoc = BoDemMa.chup();
        ArrayList<HoaDonNhapHang> moi = new ArrayList<>();
        Set<String> maDaDoc = new HashSet<>();
        Map<String, HoaDonNhapHang> theoMa = new LinkedHashMap<>();
        Set<String> maChiTiet = new HashSet<>();
        try {
            for (String dong : TepVanBan.doc(tenFile)) {
                if (dong.startsWith("CTNHAP;")) {
                    String[] p = dong.split(";", 3);
                    HoaDonNhapHang chu = p.length == 3 ? theoMa.get(p[1].toUpperCase(Locale.ROOT)) : null;
                    DongDuLieu.yeuCau(chu != null, "Chi tiết phải đứng sau hóa đơn tương ứng.");
                    CTHoaDonNhap ct = CTHoaDonNhap.tuDongFile(p[0] + ";" + p[2]);
                    TepVanBan.khongTrung(maChiTiet, p[1] + ":" + ct.getMaSanpham());
                    chu.getCtHoaDonNhaps().add(ct);
                    continue;
                }
                HoaDonNhapHang obj = HoaDonNhapHang.tuDongFile(dong);
                TepVanBan.khongTrung(maDaDoc, obj.getMaHoaDon());
                moi.add(obj);
                theoMa.put(obj.getMaHoaDon().toUpperCase(Locale.ROOT), obj);
            }
        } catch (RuntimeException | IOException e) {
            BoDemMa.khoiPhuc(demTruoc);
            throw new IOException("Không đọc được " + tenFile + ": " + e.getMessage(), e);
        }
        this.dsHoaDonNhapHang = moi;
    }
}
