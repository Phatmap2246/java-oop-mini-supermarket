package hoadon;

import sanpham.SanPham;

import dichvu.Nhacungcap;

import dichvu.Doitac;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import dichvu.Danhsachdoitac;
import sanpham.QuanLySanPham;
import utils.Validation;

public class DanhSachHoaDonNhap {
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
    public void taoDulieuHoaDonNhap(Scanner sc , String maNhanVien, QuanLySanPham qlsp , Danhsachdoitac dsDoiTac)
    {
        System.out.println("Tạo dữ liệu hoá đơn nhập!");
        do {
            themHoaDonNhap(sc, maNhanVien, qlsp , dsDoiTac);
        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục(Có/Không): "));
        System.out.println("Tạo dữ liệu hoá đơn nhập hàng thành công!");
    }

    public void hienThiDanhSachHoaDonNhap()
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
    public void themHoaDonNhap(Scanner sc , String maNhanVien, QuanLySanPham qlsp  , Danhsachdoitac dsDoiTac)
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
            themHoaDonNhap(sc, maNhanVien , qlsp , dsDoiTac);
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
                        qlsp.themSanPham(sc, dsNhaCungCap);
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
                        SanPham sp = qlsp.traCuuSanPham(ct.getMaSanpham()) ;
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
                            int indexNCC = dsDoiTac.traCuuThongTinDoiTac(sp.getMaNhaCungCap());
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

    public void suaHoaDon(Scanner sc, String maHoaDon, QuanLySanPham qlsp)
    {
        HoaDonNhapHang hdn = timKiemHoaDonNhapHang(maHoaDon);
        if (hdn == null) {
            System.out.println("Mã hoá đơn không tồn tại!");
            return;
        }
        if (hdn.isDuyet()) {
            System.out.println("Hoá đơn đã được duyệt nên không thể chỉnh sửa thuộc tính!");
            return;
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
                            "Mã nhà cung cấp không đúng định dạng (VD: NCC001)!"));
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
                        break;
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
    }
    public HoaDonNhapHang timKiemHoaDonNhapHang( String maHoaDon )
    {
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
            if ( hdn.getNgayLap() != null && hdn.getNgayLap().isAfter(thoiGianBatDau) && hdn.getNgayLap().isBefore(thoiGianKetThuc))
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
            if ( hdn.getNgayLap() != null && hdn.getNgayLap().isAfter(thoiGianBatDau.atStartOfDay()) && hdn.getNgayLap().isBefore(thoiGianKetThuc.atStartOfDay()))
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
            if ( hdn.getNgayLap() != null && hdn.getNgayLap().equals(ngay.atStartOfDay()) && hdn.getNgayLap().getHour() >= gioBatDau && hdn.getNgayLap().getHour() <= gioKetThuc )
            {
                hdn.hienThiHoaDon();
            }
        }
    }

    public void timKiemTheoNhieuKhoa(Scanner sc) {
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
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã hóa đơn cần tìm: ", "(?i)" + HoaDonNhapHang.MA_HD_NHAP_REGEX, "Mã hóa đơn không đúng định dạng. Vui lòng nhập lại!");
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
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã nhà cung cấp cần tìm: ", Validation.MaNCC_REGEX, "Mã nhà cung cấp không đúng định dạng. Vui lòng nhập lại!");
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
                            for (HoaDonNhapHang hdn : dsHoaDonNhapHang) {
                                if (hdn != null && (hdn.getNgayLap() != null && !hdn.getNgayLap().toLocalDate().isBefore(tu) && !hdn.getNgayLap().toLocalDate().isAfter(den))) {
                                    hdn.hienThiHoaDon();
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
                            for (HoaDonNhapHang hdn : dsHoaDonNhapHang) {
                                if (hdn != null && (hdn.getNgayLap() != null && hdn.getNgayLap().toLocalDate().equals(ngay) && hdn.getNgayLap().getHour() >= tu && hdn.getNgayLap().getHour() <= den)) {
                                    hdn.hienThiHoaDon();
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
                            for (HoaDonNhapHang hdn : dsHoaDonNhapHang) {
                                if (hdn != null && (hdn.getNgayLap() != null && !hdn.getNgayLap().isBefore(tu) && !hdn.getNgayLap().isAfter(den))) {
                                    hdn.hienThiHoaDon();
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
        System.out.println("==================DANH SÁCH HOÁ ĐƠN CHƯA DUYỆT==================");
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( HoaDonNhapHang hdn: dsHoaDonNhapHang )
        {
            if (hdn == null) continue;
            if ( !hdn.isDuyet())
            {
                hdn.hienThiHoaDon();
                tongHoaDon++ ;
            }
        }
        System.out.println("Tổng hoá đơn chưa duyệt là: " + tongHoaDon);
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
                    hienThiDanhSachHoaDonNhap();
                    break;
                }
                case 2: {
                    timKiemTheoNhieuKhoa(sc);
                    break;
                }
                case 3: {
                    thongKeCacNgayNhapHang();
                    break;
                }
                case 4: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã nhà cung cấp: ", Validation.MaNCC_REGEX, "Mã nhà cung cấp không đúng định dạng!");
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

                int indexSP = qlsp.timKiemSanPham(ct.getMaSanpham());

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
                }

                hdn.setDaNhapSP(true);
                System.out.println("Nhập kho thành công!");
            } else {
                System.out.println("Hóa đơn chưa được nhập kho.");
            }
        }
        System.out.println("Thêm hàng vào kho thành công!");
    }

    public void quanLyDanhSach(Scanner sc, String maNhanVien, QuanLySanPham qlsp, Danhsachdoitac dsDoiTac)
    {
        int luaChon ;
        do {
            System.out.println("===================QUẢN LÝ HÓA ĐƠN NHẬP===================");
            System.out.println("1. Nhập danh sách hóa đơn nhập!");
            System.out.println("2. Xuất danh sách hóa đơn nhập!");
            System.out.println("3. Thêm hóa đơn nhập!");
            System.out.println("4. Thêm k hóa đơn nhập!");
            System.out.println("5. Sửa hóa đơn nhập!");
            System.out.println("6. Tìm kiếm hóa đơn nhập!");
            System.out.println("7. Xem báo cáo nhập hàng!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 7);
            switch (luaChon)
            {
                case 1: {
                    taoDulieuHoaDonNhap(sc, maNhanVien, qlsp, dsDoiTac);
                    break;
                }
                case 2: {
                    hienThiDanhSachHoaDonNhap();
                    break;
                }
                case 3: {
                    themHoaDonNhap(sc, maNhanVien, qlsp, dsDoiTac);
                    break;
                }
                case 4: {
                    int k = Validation.inputInt(sc, "Nhập số phần tử cần thêm: ");
                    taoKHoaDonNhap(sc, maNhanVien, qlsp, dsDoiTac, k);
                    break;
                }
                case 5: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã hóa đơn: ", "(?i)" + HoaDonNhapHang.MA_HD_NHAP_REGEX, "Mã hóa đơn không đúng định dạng!");
                    suaHoaDon(sc, ma, qlsp);
                    break;
                }
                case 6: {
                    timKiemTheoNhieuKhoa(sc);
                    break;
                }
                case 7: {
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

    public void duyetHoaDonNhap(Scanner sc)
    {
        thongKeCacDonChuaDuyet();
        String maHoaDon = Validation.inputStringRegex(sc, "Nhập mã hóa đơn cần duyệt: ",
                "(?i)" + HoaDonNhapHang.MA_HD_NHAP_REGEX, "Mã hóa đơn không đúng định dạng!");
        HoaDonNhapHang hdn = timKiemHoaDonNhapHang(maHoaDon);
        if (hdn == null) return;
        if (hdn.isDuyet()) {
            System.out.println("Hóa đơn này đã được duyệt!");
            return;
        }
        if (Validation.inputCoKhong(sc, "Bạn có muốn duyệt hóa đơn này không")) {
            hdn.setDuyet(true);
            System.out.println("Duyệt hóa đơn nhập thành công!");
        }
    }
}
