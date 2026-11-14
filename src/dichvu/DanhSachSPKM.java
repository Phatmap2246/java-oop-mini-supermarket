package dichvu;

import sanpham.ThucPham;

import java.util.ArrayList;

import sanpham.QuanLySanPham;
import utils.Validation;
import sanpham.SanPham ;

import java.time.LocalDate;
import java.util.Scanner;

public class DanhSachSPKM implements interfaces.IQuanLy<SanPhamKhuyenMai> {
    private transient QuanLySanPham nguonSanPham;
    private transient Danhsachkhuyenmai nguonKhuyenMai;

    public void cauHinh(QuanLySanPham sanPhams, Danhsachkhuyenmai khuyenMais) {
        nguonSanPham = sanPhams;
        nguonKhuyenMai = khuyenMais;
    }

    @Override
    public SanPhamKhuyenMai timMa(String ma) {
        int i = timViTri(ma);
        return i == -1 ? null : sanPhamKhuyenMais[i];
    }

    @Override
    public SanPhamKhuyenMai timTen(String ten) {
        if (ten == null || ten.isBlank() || nguonSanPham == null) return null;
        for (SanPhamKhuyenMai spkm : sanPhamKhuyenMais) {
            if (spkm == null || !spkm.isTonTai()) continue;
            SanPham sp = nguonSanPham.timMa(spkm.getMaSanPham());
            if (sp != null && sp.getTenSanPham() != null
                    && sp.getTenSanPham().toLowerCase(java.util.Locale.ROOT)
                    .contains(ten.trim().toLowerCase(java.util.Locale.ROOT))) return spkm;
        }
        return null;
    }

    @Override
    public SanPhamKhuyenMai timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) return null;
        for (SanPhamKhuyenMai spkm : sanPhamKhuyenMais) {
            if (spkm != null && spkm.isTonTai() && spkm.toString().toLowerCase(java.util.Locale.ROOT)
                    .contains(khoa.trim().toLowerCase(java.util.Locale.ROOT))) return spkm;
        }
        return timTen(khoa);
    }
    private SanPhamKhuyenMai[] sanPhamKhuyenMais ;
    private int soLuongSanPham ;
    public DanhSachSPKM()
    {
        soLuongSanPham = 0 ;
        sanPhamKhuyenMais = new SanPhamKhuyenMai[soLuongSanPham] ;
    }
    public void setSanPhamKhuyenMais(SanPhamKhuyenMai[] sanPhamKhuyenMais) {
        if (sanPhamKhuyenMais == null) {
            throw new IllegalArgumentException("Danh sách không được null.");
        }
        for (Object phanTu : sanPhamKhuyenMais) {
            if (phanTu == null) throw new IllegalArgumentException("Danh sách có phần tử null.");
        }

        this.sanPhamKhuyenMais = sanPhamKhuyenMais;
        this.soLuongSanPham = sanPhamKhuyenMais.length;
    }

    public SanPhamKhuyenMai[] getSanPhamKhuyenMais() {
        return sanPhamKhuyenMais;
    }

    public int getSoLuongSanPham() {
        return soLuongSanPham;
    }

    public void setSoLuongSanPham(int soLuongSanPham) {
        if (soLuongSanPham < 0 || soLuongSanPham > sanPhamKhuyenMais.length) {
            throw new IllegalArgumentException("Số lượng vượt giới hạn mảng.");
        }
        this.soLuongSanPham = soLuongSanPham;
    }
    @Override
    public void taoDanhSach(Scanner sc )
    {
        System.out.println("Khởi tạo danh sách khuyến mãi!");
        do {
            them(sc);
        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục thêm sản phẩm khuyến mãi(Có/Không): "));
        System.out.println("Tạo dữ liệu sản phẩm khuyến mãi thành công!");
    }
    @Override
    public void xuatDanhSach()
    {
        System.out.println("Danh sách sản phẩm khuyến mãi!");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for ( SanPhamKhuyenMai spkm: sanPhamKhuyenMais )
            if (spkm != null && spkm.isTonTai()) System.out.println(spkm);
    }

    @Override
    public void them(Scanner sc )
    {
        System.out.println("Thêm sản phẩm khuyến mãi!");
        SanPhamKhuyenMai spkm = new SanPhamKhuyenMai() ;
        spkm.setSanPhamKhuyenMai(sc);
        while ( timViTri(spkm.getMaSanPham()) != -1 )
        {
            System.out.println("Bạn không thể dùng hai mã khuyến mãi trên 1 loại sản phẩm!");
            spkm.setSanPhamKhuyenMai(sc);
        }
        them(spkm);
    }

    public void them(SanPhamKhuyenMai spkm)
    {
        if (spkm == null) {
            System.out.println("Không thể thêm đối tượng null!");
            return;
        }
        int newSoLuongSanPham = soLuongSanPham + 1 ;
        SanPhamKhuyenMai[] tam = new SanPhamKhuyenMai[newSoLuongSanPham];
        System.arraycopy(sanPhamKhuyenMais, 0, tam, 0, soLuongSanPham);
        tam[soLuongSanPham] = spkm ;
        sanPhamKhuyenMais = tam ;
        soLuongSanPham = newSoLuongSanPham ;
        System.out.println("Thêm sản phẩm mới thành công");
    }
    public void themKSanPhamKM( Scanner sc , int k )
    {
        System.out.println("Thêm " + k + " Sản phẩm khuyến mãi!");
        for ( int i = 0 ; i < k ; i++ )
        {
            them(sc );
        }
        System.out.println("Thêm " + k + " sản phẩm khuyến mãi thành công!");
    }

    public void giamGiaCanHSD(Scanner sc, ArrayList<ThucPham> dscanDate, Danhsachkhuyenmai dsKhuyenMai) {
        //ràng điều kiện
        if (sc == null || dscanDate == null) {
            throw new IllegalArgumentException("Scanner và danh sách thực phẩm không được null.");
        }
        if (dscanDate.isEmpty()) {
            System.out.println("Không có thực phẩm cận hạn để tạo khuyến mãi.");
            return;
        }

        LocalDate homNay = LocalDate.now();
        for (ThucPham thucPham : dscanDate) {
            if (thucPham == null || !thucPham.isTonTai() || thucPham.getHanSuDung() == null
                    || thucPham.getHanSuDung().isBefore(homNay)) {
                continue;
            }
            if (thucPham.getSoLuongTonKho() <= 0) {
                System.out.println("Bỏ qua " + thucPham.getMaSanPham()
                        + " vì số lượng tồn không còn.");
                continue;
            }
            if (timViTri(thucPham.getMaSanPham()) != -1) {
                System.out.println("Bỏ qua " + thucPham.getMaSanPham()
                        + " vì sản phẩm đã có khuyến mãi.");
                continue;
            }

            System.out.printf("%s - %s | Tồn: %d | HSD: %s%n",
                    thucPham.getMaSanPham(), thucPham.getTenSanPham(),
                    thucPham.getSoLuongTonKho(), thucPham.getHanSuDung());
            int luaChon = Validation.luaChon(
                    sc, "Chọn loại giảm giá (1: phần trăm, 2: trực tiếp): ", 1, 2);
            Khuyenmai khuyenMai = luaChon == 1
                    ? new KhuyenMaiPhanTram()
                    : new KhuyenMaiTrucTiep();

            do {
                khuyenMai.setKM(sc);
                if (khuyenMai.getNgayBatDau().isBefore(homNay)
                        || khuyenMai.getNgayKetThuc().isAfter(thucPham.getHanSuDung())) {
                    System.out.println("[!] Thời gian khuyến mãi phải bắt đầu từ hôm nay "
                            + "và kết thúc không muộn hơn hạn sử dụng.");
                } else {
                    break;
                }
            } while (true);
// thêm sp giảm cận date vào dsSanPhamKhuyenMais
            dsKhuyenMai.them(khuyenMai);
            them(
                    new SanPhamKhuyenMai(thucPham.getMaSanPham(),
                            khuyenMai.getMaKhuyenMai()));
            System.out.println("Đã gắn khuyến mãi " + khuyenMai.getMaKhuyenMai()
                    + " cho sản phẩm " + thucPham.getMaSanPham() + ".");
        }
    }
    @Override
    public boolean sua(String maSanPham, Scanner sc)
    {
        try {

            int index = timViTri(maSanPham) ;
            if ( index == -1 )
            {
                System.out.println("Mã sản phẩm không tồn tại");
                return false;
            }
            System.out.println("Sửa thông tin sản phẩm khuyến mãi!");
            System.out.println("1. Sửa mã sản phẩm!");
            System.out.println("2. Sửa mã khuyến mãi!");
            int luaChon = Validation.inputInt( sc , "Nhập lựa chọn của bạn!") ;
            switch (luaChon)
            {
                case 1:
                    String maMoi = Validation.inputStringRegex(sc, "Nhập mã sản phẩm mới: ", Validation.MaSP_REGEX, "Mã sản phẩm không hợp lệ!");
                    int trung = timViTri(maMoi);
                    if (trung != -1 && trung != index) return false;
                    if (nguonSanPham != null && nguonSanPham.timMa(maMoi) == null) return false;
                    sanPhamKhuyenMais[index].setMaSanPham(maMoi);
                    break;
                case 2:
                    sanPhamKhuyenMais[index].setMaKhuyenMai(Validation.inputStringRegex(sc , "Nhập mã khuyến mãi mới: " , "KM\\d{3}" , "Mã khuyến mãi sai định dạng vui lòng nhập lại!(ví dụ hợp lệ: KM001)", dichvu.Khuyenmai.getSoThuTu()));
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    return false;
            }
            System.out.println("Sửa thông tin sản phẩm khuyến mãi thành công!");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean xoa( String maSanPham )
    {
        int index = timViTri(maSanPham);
        if (index == -1) return false;
        sanPhamKhuyenMais[index].setTonTai(false);
        System.out.println("Xóa sản phẩm khuyến mãi thành công!");
        return true;
    }
    @Override
    public int timViTri( String maSanPham )
    {
        if (maSanPham == null || maSanPham.isBlank()) return -1;
        maSanPham = maSanPham.trim();
        for ( int i = 0 ; i < soLuongSanPham ; i++ )
        {
            if (sanPhamKhuyenMais[i] != null && sanPhamKhuyenMais[i].isTonTai() && (maSanPham != null && maSanPham.equalsIgnoreCase(sanPhamKhuyenMais[i].getMaSanPham()))) return i ;
        }
        System.out.println("Không tìm thấy sản phẩm khuyến mãi hoặc liên kết đã bị xóa!");
        return -1 ;
    }

    public void timKhoa(Scanner sc) {
        while (true) {
            System.out.println("================ TÌM KIẾM SẢN PHẨM KHUYẾN MÃI ================");
            System.out.println("1. Mã sản phẩm");
            System.out.println("2. Mã khuyến mãi");
            System.out.println("0. Trở về");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 2);
            if (luaChon == 0) return;
            boolean flag = false;
            System.out.println("Kết quả tra cứu:");
            switch (luaChon) {
                case 1: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã sản phẩm cần tìm: ", Validation.MaSP_REGEX, "Mã sản phẩm không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (SanPhamKhuyenMai spkm : sanPhamKhuyenMais) {
                        if (spkm != null && spkm.isTonTai() && ((spkm.getMaSanPham()) != null && spkm.getMaSanPham().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(spkm);
                            flag = true;
                        }
                    }
                    break;
                }
                case 2: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã khuyến mãi cần tìm: ", Validation.MaKM_REGEX, "Mã khuyến mãi không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (SanPhamKhuyenMai spkm : sanPhamKhuyenMais) {
                        if (spkm != null && spkm.isTonTai() && ((spkm.getMaKhuyenMai()) != null && spkm.getMaKhuyenMai().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(spkm);
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
    public void thongKeSanPhamKhuyenmai(Scanner sc, Danhsachkhuyenmai dskm, QuanLySanPham qlsp)
    {
        int tongSanPham = 0 ;
        boolean flag = false;
        LocalDate homNay = LocalDate.now();
        System.out.println("===================SẢN PHẨM ĐANG GIẢM GIÁ===================");
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for (int i = 0; i < soLuongSanPham; i++)
        {
            SanPhamKhuyenMai spkm = sanPhamKhuyenMais[i];
            if (spkm == null || !spkm.isTonTai()) continue;

            SanPham sp = qlsp.timMa(spkm.getMaSanPham());
            Khuyenmai km = dskm.timMa(spkm.getMaKhuyenMai());
            if (sp == null || km == null || !sp.isTonTai() || !km.isTonTai()
                    || !km.dangHoatDong(homNay)) continue;

            String thongTin = getString(sp, km);
            System.out.println(thongTin + " | Ngày bắt đầu khuyến mãi: " + km.getNgayBatDau()
                    + " | Ngày kết thúc khuyến mãi: " + km.getNgayKetThuc());
            tongSanPham++ ;
            flag = true;
        }
        if (!flag) System.out.println("Không có sản phẩm đang giảm giá!");
        System.out.println("Tổng sản phẩm đang giảm giá là: " + tongSanPham);
    }
    public void thongKeSanPhamTheoTungKhuyenMai( QuanLySanPham qlsp , Danhsachkhuyenmai danhsachkhuyenmai)
    {
        System.out.println("Thống kê sản phẩm theo từng loại khuyến mãi!");
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( Khuyenmai km : danhsachkhuyenmai.getKhuyenmais() )
        {
            boolean flag = false ;
            int tongSanPham = 0 ;
            if ( km.isTonTai() && km.dangHoatDong(LocalDate.now()))
            {
                System.out.println(km);
                System.out.println("Các sản phẩm chứa khuyến mãi: ");
                for ( SanPhamKhuyenMai spkm: sanPhamKhuyenMais )
                {
                    SanPham sp = qlsp.timMa(spkm.getMaSanPham()) ;
                    if ( spkm.getMaKhuyenMai().equalsIgnoreCase(km.getMaKhuyenMai()))
                    {
                        System.out.println( getString(sp , km));
                        tongSanPham++ ;
                        flag = true ;
                    }
                }
                System.out.println("Tổng sản phẩm áp dụng mã khuyến mãi " + km.getMaKhuyenMai() + " là: " + tongSanPham);
            }
            if ( !flag )
            {
                System.out.println("Chưa có sản phẩm nào áp dụng mã giảm giá này!");
            }
        }
    }

    private static String getString(SanPham sp, Khuyenmai km) {
        String thongTin = "Mã sản phẩm: " + sp.getMaSanPham()
                + " | Tên sản phẩm: " + sp.getTenSanPham()
                + " | Số lượng: " + sp.getSoLuongTonKho()
                + " | Loại khuyến mãi: " + km.getLoaiKhuyenMai();
        if (km instanceof KhuyenMaiPhanTram kmPhanTram)
        {
            thongTin += " | Phần trăm giảm giá: " + kmPhanTram.getPhanTramGiamGia();
        }
        else if (km instanceof KhuyenMaiTrucTiep kmTrucTiep)
        {
            thongTin += " | Mức giảm giá trực tiếp: " + kmTrucTiep.getSoTienGiam();
        }
        return thongTin;
    }
    public boolean kiemTraSanPhamTrung (String maSanPham )
    {
        int dem = 0 ;
        for ( SanPhamKhuyenMai spkm:sanPhamKhuyenMais )
            if (spkm != null && spkm.isTonTai() && (maSanPham != null && maSanPham.equalsIgnoreCase(spkm.getMaSanPham()))) dem++ ;
        return dem > 1 ;
    }

    public void quanLyDanhSach(Scanner sc, QuanLySanPham qlsp, Danhsachkhuyenmai dskm)
    {
        cauHinh(qlsp, dskm);
        int luaChon ;
        do {
            System.out.println("===================QUẢN LÝ SẢN PHẨM KHUYẾN MÃI===================");
            System.out.println("1. Nhập danh sách sản phẩm khuyến mãi!");
            System.out.println("2. Xuất danh sách sản phẩm khuyến mãi!");
            System.out.println("3. Thêm sản phẩm khuyến mãi!");
            System.out.println("4. Thêm k sản phẩm khuyến mãi!");
            System.out.println("5. Sửa sản phẩm khuyến mãi!");
            System.out.println("6. Xóa sản phẩm khuyến mãi!");
            System.out.println("7. Tìm kiếm sản phẩm khuyến mãi!");
            System.out.println("8. Thống kê sản phẩm đang giảm giá!");
            System.out.println("9. Thống kê theo từng khuyến mãi!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 9);
            switch (luaChon)
            {
                case 1: {
                    taoDanhSach(sc);
                    break;
                }
                case 2: {
                    xuatDanhSach();
                    break;
                }
                case 3: {
                    them(sc);
                    break;
                }
                case 4: {
                    int k = Validation.inputInt(sc, "Nhập số phần tử cần thêm: ");
                    themKSanPhamKM(sc, k);
                    break;
                }
                case 5: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ", Validation.MaSP_REGEX, "Mã sản phẩm không đúng định dạng!");
                    sua(ma, sc);
                    break;
                }
                case 6: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ", Validation.MaSP_REGEX, "Mã sản phẩm không đúng định dạng!");
                    xoa(ma);
                    break;
                }
                case 7: {
                    timKhoa(sc);
                    break;
                }
                case 8: {
                    thongKeSanPhamKhuyenmai(sc, dskm, qlsp);
                    break;
                }
                case 9: {
                    thongKeSanPhamTheoTungKhuyenMai(qlsp, dskm);
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
}
