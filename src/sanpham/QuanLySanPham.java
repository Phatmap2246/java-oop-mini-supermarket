package sanpham;

import java.time.LocalDate;

import java.util.Locale;

import dichvu.Nhacungcap;
import utils.Validation;

import java.util.ArrayList;
import java.util.Scanner;

public class QuanLySanPham implements interfaces.IQuanLy<SanPham> {
    private transient dichvu.Danhsachdoitac nguonDoiTac;

    public void cauHinh(dichvu.Danhsachdoitac doiTacs) { nguonDoiTac = doiTacs; }

    private ArrayList<Nhacungcap> nhaCungCaps() {
        return nguonDoiTac == null ? new ArrayList<>() : nguonDoiTac.layDanhSachNhaCungCap();
    }

    @Override
    public void taoDanhSach(Scanner sc) { taoDanhSach(sc, nhaCungCaps()); }

    @Override
    public void them(Scanner sc) { them(sc, nhaCungCaps()); }

    @Override
    public boolean sua(String ma, Scanner sc) { return sua(sc, ma, nhaCungCaps()); }

    @Override
    public SanPham timTen(String ten) {
        if (ten == null || ten.isBlank()) return null;
        for (SanPham sp : danhSachSanPham) {
            if (sp != null && sp.isTonTai() && sp.getTenSanPham() != null
                    && sp.getTenSanPham().toLowerCase(Locale.ROOT).contains(ten.trim().toLowerCase(Locale.ROOT))) return sp;
        }
        return null;
    }

    @Override
    public SanPham timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) return null;
        for (SanPham sp : danhSachSanPham) {
            if (sp != null && sp.isTonTai()
                    && sp.toString().toLowerCase(Locale.ROOT).contains(khoa.trim().toLowerCase(Locale.ROOT))) return sp;
        }
        return null;
    }
    private ArrayList<SanPham> danhSachSanPham;

    public QuanLySanPham() {
        danhSachSanPham = new ArrayList<>();
    }

    public ArrayList<SanPham> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(ArrayList<SanPham> danhSachSanPham) {
        if (danhSachSanPham == null) {
            throw new IllegalArgumentException("Danh sách không được null.");
        }
        for (Object phanTu : danhSachSanPham) {
            if (phanTu == null) throw new IllegalArgumentException("Danh sách có phần tử null.");
        }

        this.danhSachSanPham = danhSachSanPham;
    }

    public void taoDanhSach(Scanner sc, ArrayList<Nhacungcap> danhSachNhaCungCap) {
        System.out.println("Tạo dữ liệu cho danh sách sản phẩm!");
        boolean tiepTuc = true;
        while (tiepTuc) {
            them(sc, danhSachNhaCungCap);
            System.out.println("Bạn có muốn tiếp tục(1/0): ");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 1);
            if (luaChon == 0) {
                tiepTuc = false;
            }
        }
        System.out.println("Tạo dữ liệu thành công!");
    }
    @Override
    public void xuatDanhSach()
    {
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for ( SanPham sanPham:danhSachSanPham )
            if (sanPham != null && sanPham.isTonTai()) System.out.println(sanPham);
    }
    public void them(Scanner sc, ArrayList<Nhacungcap> danhSachNhaCungCap) {
        if (danhSachNhaCungCap != null) {
            danhSachNhaCungCap = new ArrayList<>(danhSachNhaCungCap);
            danhSachNhaCungCap.removeIf(ncc -> ncc == null || !ncc.isTonTai());
        }

        SanPham sanPham;
        System.out.println("Nhập loại sản phẩm cần thêm: ");
        System.out.println("1. Thực Phẩm");
        System.out.println("2. Tiêu Dùng");
        System.out.println("3. Điện tử ");
        int luaChon = Validation.inputInt(sc , "Nhập lựa chọn: ");
        switch (luaChon) {
            case 1:
                if (danhSachNhaCungCap == null || danhSachNhaCungCap.isEmpty()) {
                    System.out.println("Vui lòng thêm nhà cung cấp!");
                } else {
                    sanPham = new ThucPham();
                    sanPham.setSanPham(sc);
                    System.out.println("Chọn nhà cung cấp: ");
                    for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                        System.out.println((i + 1) + ". Nhà cung cấp" + danhSachNhaCungCap.get(i).getTenDoiTac() + "!");
                    }
                    int luaChonNhaCungCap = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, danhSachNhaCungCap.size());
                    sanPham.setMaNhaCungCap(danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getMaDoiTac());
                    danhSachSanPham.add(sanPham);
                    System.out.println("Thêm sản phẩm thành công!");
                     danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getSanPhamCungCap().getDanhSachSanPham().add(sanPham) ; // tác vụ đặc biệt nên xem xét lại
                    // hoá đơn đối tác
                }
                break;
            case 2:
                if (danhSachNhaCungCap == null || danhSachNhaCungCap.isEmpty()) {
                    System.out.println("Vui lòng thêm nhà cung cấp!");
                } else {
                    sanPham = new TieuDung();
                    sanPham.setSanPham(sc);
                    System.out.println("Chọn nhà cung cấp: ");
                    for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                        System.out.println((i + 1) + ". Nhà cung cấp" + danhSachNhaCungCap.get(i).getTenDoiTac() + "!");
                    }
                    int luaChonNhaCungCap = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, danhSachNhaCungCap.size());
                    sanPham.setMaNhaCungCap(danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getMaDoiTac());
                    danhSachSanPham.add(sanPham);
                    System.out.println("Thêm sản phẩm thành công!");
                    danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getSanPhamCungCap().getDanhSachSanPham().add(sanPham) ;
                    // hoá đơn đối tác
                }
                break;
            case 3:
                if (danhSachNhaCungCap == null || danhSachNhaCungCap.isEmpty()) {
                    System.out.println("Vui lòng thêm nhà cung cấp!");
                } else {
                    sanPham = new DienTu();
                    sanPham.setSanPham(sc);
                    System.out.println("Chọn nhà cung cấp: ");
                    for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                        System.out.println((i + 1) + ". Nhà cung cấp" + danhSachNhaCungCap.get(i).getTenDoiTac() + "!");
                    }
                    int luaChonNhaCungCap = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, danhSachNhaCungCap.size());
                    sanPham.setMaNhaCungCap(danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getMaDoiTac());
                    danhSachSanPham.add(sanPham);
                    System.out.println("Thêm sản phẩm thành công!");
                    danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getSanPhamCungCap().getDanhSachSanPham().add(sanPham) ;
                    // hoá đơn đối tác
                }
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                break;
        }
    }
    public void themKSanpham( Scanner sc , ArrayList<Nhacungcap> danhsachNhaCungCap , int k )
    {
        System.out.println("Khởi tạo k sản phẩm cho danh sách sản phẩm!");
        for ( int i = 0 ; i < k ; i++ )
        {
            them(sc , danhsachNhaCungCap);
        }
        System.out.println("Thêm " + k + " Sản phẩm thành công!");
    }
    
    public boolean sua(Scanner sc, String maSanPham, ArrayList<Nhacungcap> danhSachNhaCungCap) {
        try {

            if (danhSachNhaCungCap != null) {
                danhSachNhaCungCap = new ArrayList<>(danhSachNhaCungCap);
                danhSachNhaCungCap.removeIf(ncc -> ncc == null || !ncc.isTonTai());
            }

            System.out.println("Sửa thông tin sản phẩm!");
            int index = timViTri(maSanPham);
            if (index == -1) return false;
            SanPham sanPham = danhSachSanPham.get(index);
            if (sanPham instanceof ThucPham) {
                System.out.println("Chọn thuộc tính cần sửa: ");
                System.out.println("1.Tên sản phẩm!");
                System.out.println("2.Giá nhập!");
                System.out.println("3.Giá bán!");
                System.out.println("4.Số sản phẩm tồn kho!");
                System.out.println("5.Ngày sản xuất!");
                System.out.println("6.Hạn sử dung!");
                System.out.println("7.Nhiệt độ bảo quản!");
                System.out.println("8.Bảo quản!");
                System.out.println("9.Nhà cung cấp!");
                int luachon = Validation.inputInt(sc, "Nhập lựa chọn: ");
                switch (luachon) {
                    case 1:
                        System.out.println("Nhập tên mới cho sản phẩm: ");
                        sanPham.setTenSanPham(sc.nextLine());
                        break;
                    case 2:
                        sanPham.setGiaNhap(Validation.inputLong(sc , "Nhập giá nhập mới cho sản phẩm: "));
                        break;
                    case 3:
                        sanPham.setGiaBan(Validation.inputLong(sc , "Nhập giá bán mới cho sản phẩm: "));
                        break;
                    case 4:
                        sanPham.setSoLuongTonKho(Validation.inputInt(sc , "Nhập số lượng tồn kho mới cho sản phẩm: "));
                        break;
                    case 5:
                        System.out.println("Nhập ngày sản xuất mới cho sản phẩm(dd/MM/yyyy): ");
                        ((ThucPham) sanPham).setNgaySanXuat(Validation.inputLocalDateRegex(sc, "Nhập ngày mới (dd/MM/yyyy): "));
                        break;
                    case 6:
                        System.out.println("Nhập hạn sử dụng mới cho sản phẩm(dd/MM/yyyy): ");
                        ((ThucPham) sanPham).setHanSuDung(Validation.inputLocalDateRegex(sc, "Nhập ngày mới (dd/MM/yyyy): "));
                        break;
                    case 7:
                        ((ThucPham) sanPham).setNhietDoBaoQuan(Validation.inputInt(sc , "Nhập nhiệt độ bảo quản mới cho sản phẩm: "));
                        break;
                    case 8:
                        System.out.println("Nhập môi trường bảo quản mới cho sản phẩm: ");
                        ((ThucPham) sanPham).setBaoQuan(sc.nextLine());
                        break;
                    case 9:
                        if (danhSachNhaCungCap == null || danhSachNhaCungCap.isEmpty()) {
                            System.out.println("Chưa có nhà cung cấp!");
                            return false;
                        }
                        System.out.println("Chọn nhà cung cấp: ");
                        for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                            System.out.println((i + 1) + ". Nhà cung cấp" + danhSachNhaCungCap.get(i).getTenDoiTac() + "!");
                        }
                        int luaChonNhaCungCap = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, danhSachNhaCungCap.size());
                        sanPham.setMaNhaCungCap(danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getMaDoiTac());
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        return false;
                }
            } else if (sanPham instanceof TieuDung) {
                System.out.println("Chọn thuộc tính cần sửa: ");
                System.out.println("1.Tên sản phẩm!");
                System.out.println("2.Giá nhập!");
                System.out.println("3.Giá bán!");
                System.out.println("4.Số sản phẩm tồn kho!");
                System.out.println("5.Thương hiệu sản phẩm!");
                System.out.println("6.Nơi sản xuất!");
                System.out.println("7.Phân loại!");
                System.out.println("8.Lưu ý an toàn!");
                System.out.println("9.Nhà cung cấp!");
                int luachon = Validation.inputInt(sc, "Nhập lựa chọn: ");
                switch (luachon) {
                    case 1:
                        System.out.println("Nhập tên mới cho sản phẩm: ");
                        sanPham.setTenSanPham(sc.nextLine());
                        break;
                    case 2:
                        sanPham.setGiaNhap(Validation.inputLong(sc, "Nhập giá nhập mới cho sản phẩm: "));
                        break;
                    case 3:
                        sanPham.setGiaBan(Validation.inputLong(sc, "Nhập giá bán mới cho sản phẩm: "));
                        break;
                    case 4:
                        sanPham.setSoLuongTonKho(Validation.inputInt(sc, "Nhập số lượng tồn kho mới cho sản phẩm: "));
                        break;
                    case 5:
                        System.out.println("Nhập thương hiệu mới cho sản phẩm: ");
                        ((TieuDung) sanPham).setThuongHieu(sc.nextLine());
                        break;
                    case 6:
                        System.out.println("Nhập nơi sản xuất mới cho sản phẩm: ");
                        ((TieuDung) sanPham).setNoiSanXuat(sc.nextLine());
                        break;
                    case 7:
                        System.out.println("Nhập phân loại mới cho sản phẩm: ");
                        ((TieuDung) sanPham).setPhanLoai(sc.nextLine());
                        break;
                    case 8:
                        System.out.println("Nhập lưu ý an toàn mới cho sản phẩm: ");
                        ((TieuDung) sanPham).setLuuYAnToan(sc.nextLine());
                        break;
                    case 9:
                        if (danhSachNhaCungCap == null || danhSachNhaCungCap.isEmpty()) {
                            System.out.println("Chưa có nhà cung cấp!");
                            return false;
                        }
                        System.out.println("Chọn nhà cung cấp: ");
                        for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                            System.out.println((i + 1) + ". Nhà cung cấp " + danhSachNhaCungCap.get(i).getTenDoiTac());
                        }
                        int luaChonNhaCungCap = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, danhSachNhaCungCap.size());
                        sanPham.setMaNhaCungCap(danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getMaDoiTac());
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        return false;
                }
            } else if (sanPham instanceof DienTu) {
                System.out.println("Chọn thuộc tính cần sửa: ");
                System.out.println("1.Tên sản phẩm!");
                System.out.println("2.Giá nhập!");
                System.out.println("3.Giá bán!");
                System.out.println("4.Số sản phẩm tồn kho!");
                System.out.println("5.Thời gian bảo hành!");
                System.out.println("6.Chất liệu!");
                System.out.println("7.Nhà cung cấp!");
                int luachon = Validation.inputInt(sc, "Nhập lựa chọn: ");
                switch (luachon) {
                    case 1:
                        System.out.println("Nhập tên mới cho sản phẩm: ");
                        sanPham.setTenSanPham(sc.nextLine());
                        break;
                    case 2:
                        sanPham.setGiaNhap(Validation.inputLong(sc, "Nhập giá nhập mới cho sản phẩm: "));
                        break;
                    case 3:
                        sanPham.setGiaBan(Validation.inputLong(sc, "Nhập giá bán mới cho sản phẩm: "));
                        break;
                    case 4:
                        sanPham.setSoLuongTonKho(Validation.inputInt(sc, "Nhập số lượng tồn kho mới cho sản phẩm: "));
                        break;
                    case 5:
                        ((DienTu) sanPham).setThoiGianBaoHanh(Validation.inputInt(sc, "Nhập thời gian bảo hành mới (theo tháng): "));
                        break;
                    case 6:
                        System.out.println("Nhập chất liệu mới cho sản phẩm: ");
                        ((DienTu) sanPham).setChatLieu(sc.nextLine());
                        break;
                    case 7:
                        if (danhSachNhaCungCap == null || danhSachNhaCungCap.isEmpty()) {
                            System.out.println("Chưa có nhà cung cấp!");
                            return false;
                        }
                        System.out.println("Chọn nhà cung cấp: ");
                        for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                            System.out.println((i + 1) + ". Nhà cung cấp " + danhSachNhaCungCap.get(i).getTenDoiTac());
                        }
                        int luaChonNhaCungCap = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, danhSachNhaCungCap.size());
                        sanPham.setMaNhaCungCap(danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getMaDoiTac());
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        return false;
                }
            }
            System.out.println("Đã thay đổi thông tin sản phẩm!");
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }

    public void dieuChinhGiaBan(Scanner sc){
        String maSP = Validation.inputStringRegex(sc, "Nhập mã sản phẩm cần sửa giá: ",
                Validation.MaSP_REGEX, "Mã sản phẩm không hợp lệ!");
        SanPham sanPham = timMa(maSP);
        if(sanPham == null){
            return;
        }
        long giaBanMoi = Validation.inputLong(sc, "Nhập giá bán mới: ");
        sanPham.setGiaBan(giaBanMoi);
        System.out.println("Đã cập nhật giá bán sản phẩm " + sanPham.getTenSanPham() + ".");
    }

    @Override
    public boolean xoa(String maSanPham) {
        int viTri = timViTri(maSanPham);
        if (viTri == -1) {
            System.out.println("Mã sản phẩm không hợp lệ, sản phẩm không tồn tại!");
            return false;
        } else {
            danhSachSanPham.get(viTri).setTonTai(false);
            System.out.println("Xoá sản phẩm thành công!");
            return true;
        }
    }

    public void kiemTraVaLoaiBoHangHetHan(Scanner sc )
    {
         inDanhSachQuaHan();
         if ( Validation.inputCoKhong(sc ,"Bạn có muốn loại bỏ sản phẩm hết hạn không(Có/Không): "))
         {
             for ( SanPham sp: danhSachQuaHan())
             {
                 if (sp == null) continue;
                 sp.setSoLuongTonKho(0);
             }
         }
         System.out.println("Đã loại bỏ các sản phẩm hết hạn!");
         inDanhSachQuaHan();
    }

    @Override
    public int timViTri(String maSanPham) {
        if (maSanPham == null || maSanPham.isBlank()) return -1;
        maSanPham = maSanPham.trim();
        for (int i = 0; i < danhSachSanPham.size(); i++) {
            if (danhSachSanPham.get(i) != null && danhSachSanPham.get(i).isTonTai() && (maSanPham != null && maSanPham.equalsIgnoreCase(danhSachSanPham.get(i).getMaSanPham()))) {
                return i;
            }
        }
        System.out.println("Không tìm thấy sản phẩm hoặc sản phẩm đã bị xóa!");
        return -1;
    }
    @Override
    public SanPham timMa( String maSanPham)
    {
        int index = timViTri(maSanPham);
        if ( index == -1 )
        {
            return null ;
        }
        return danhSachSanPham.get(index) ;
    }

    public void timKhoa(Scanner sc) {
        while (true) {
            System.out.println("================ TÌM KIẾM SẢN PHẨM ================");
            System.out.println("1. Mã sản phẩm");
            System.out.println("2. Tên sản phẩm");
            System.out.println("3. Loại sản phẩm");
            System.out.println("4. Mã nhà cung cấp");
            System.out.println("5. Giá bán");
            System.out.println("6. Số lượng tồn kho");
            System.out.println("0. Trở về");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 7);
            if (luaChon == 0) return;
            boolean flag = false;
            System.out.println("Kết quả tra cứu:");
            switch (luaChon) {
                case 1: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã sản phẩm cần tìm: ", Validation.MaSP_REGEX, "Mã sản phẩm không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (SanPham sp : danhSachSanPham) {
                        if (sp != null && sp.isTonTai() && ((sp.getMaSanPham()) != null && sp.getMaSanPham().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(sp);
                            flag = true;
                        }
                    }
                    break;
                }
                case 2: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập tên sản phẩm cần tìm: ", ".+", "Tên sản phẩm không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (SanPham sp : danhSachSanPham) {
                        if (sp != null && sp.isTonTai() && ((sp.getTenSanPham()) != null && (sp.getTenSanPham()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(sp);
                            flag = true;
                        }
                    }
                    break;
                }
                case 3: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập loại sản phẩm cần tìm: ", ".+", "Loại sản phẩm không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (SanPham sp : danhSachSanPham) {
                        if (sp != null && sp.isTonTai() && ((sp.getLoaiSanPham()) != null && (sp.getLoaiSanPham()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(sp);
                            flag = true;
                        }
                    }
                    break;
                }
                case 4: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã nhà cung cấp cần tìm: ", Validation.MaNCC_REGEX, "Mã nhà cung cấp không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (SanPham sp : danhSachSanPham) {
                        if (sp != null && sp.isTonTai() && ((sp.getMaNhaCungCap()) != null && sp.getMaNhaCungCap().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(sp);
                            flag = true;
                        }
                    }
                    break;
                }
                case 5: {
                    long tu = Validation.inputLong(sc, "Giá trị từ: ");
                    long den = Validation.inputLong(sc, "Giá trị đến: ");
                    if (tu > den) {
                        System.out.println("Giá trị bắt đầu không được lớn hơn giá trị kết thúc!");
                        continue;
                    }
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (SanPham sp : danhSachSanPham) {
                        if (sp != null && sp.isTonTai() && (sp.getGiaBan() >= tu && sp.getGiaBan() <= den)) {
                            System.out.println(sp);
                            flag = true;
                        }
                    }
                    break;
                }
                case 6: {
                    long tu = Validation.inputLong(sc, "Giá trị từ: ");
                    long den = Validation.inputLong(sc, "Giá trị đến: ");
                    if (tu > den) {
                        System.out.println("Giá trị bắt đầu không được lớn hơn giá trị kết thúc!");
                        continue;
                    }
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (SanPham sp : danhSachSanPham) {
                        if (sp != null && sp.isTonTai() && (sp.getSoLuongTonKho() >= tu && sp.getSoLuongTonKho() <= den)) {
                            System.out.println(sp);
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

    public void checkHSD( String maSanPham )
    {
        SanPham sp = timMa(maSanPham) ;
        if ( sp == null || !sp.isTonTai() )
        {
            System.out.println("Mã sản phẩm không tồn tại!");
            return;
        }
        if ( sp instanceof ThucPham )
        {
            if ( ((ThucPham) sp).ktraNgayHetHan() > ((ThucPham) sp).kiemTraSoNgaySuDung()*0.5 )
            {
                System.out.println("Sản phẩm vẫn có thể sử dụng tốt với " + ((ThucPham) sp).ktraNgayHetHan() + " Ngày sử dụng!");
            }
            else if ( ((ThucPham) sp).kiemTraSanPhamHong() )
            {
                System.out.println("Sản phẩm đã bị hỏng vui lòng thay mới!");
            }
            else
            {
                System.out.println("Sản phẩm sắp hết hạn vui lòng thêm khuyến mãi để sản phẩm bán hết");
            }
        }
        else
        {
            System.out.println("Sản phẩm không tồn tại ngày sản xuất và hạn sử dụng do là hàng tiêu dùng hoặc điện tử");
        }
    }

    public ArrayList<SanPham> danhSachQuaHan()
    {
        ArrayList<SanPham> sanPhamHetHan = new ArrayList<>() ;
        for ( SanPham sp:danhSachSanPham )
        {
            if (sp == null) continue;
            if ( sp instanceof ThucPham && sp.isTonTai() && ((ThucPham) sp).kiemTraSanPhamHong() && sp.getSoLuongTonKho() != 0 )
            {
                sanPhamHetHan.add(sp) ;
            }
        }
        return sanPhamHetHan ;
    }

    public void inDanhSachQuaHan()
    {
        ArrayList<SanPham> danhSachSPHetHan = danhSachQuaHan() ;
        if ( danhSachSPHetHan == null )
        {
            System.out.println("Không có sản phẩm nào quá hạn sử dụng!");
            return;
        }
        System.out.println("====================DANH SÁCH SẢN PHẨM QUÁ HẠN====================");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for ( SanPham sp: danhSachSPHetHan )
        {
            if (sp == null) continue;
            System.out.println(sp);
        }
    }

    public ArrayList<SanPham> danhSachSanPhamBanHet()
    {
        ArrayList<SanPham> sanPhamBanHet = new ArrayList<>() ;
        for ( SanPham sp:danhSachSanPham )
        {
            if (sp == null) continue;
            if ( sp.isTonTai() && sp.getSoLuongTonKho() == 0 )
            {
                sanPhamBanHet.add(sp) ;
            }
        }
        return sanPhamBanHet ;
    }

    public void inDanhSachSanPhamBanHet()
    {
        ArrayList<SanPham> danhSachSPBanHet = danhSachSanPhamBanHet() ;
        if ( danhSachSPBanHet == null )
        {
            System.out.println("Mọi sản phẩm đều không bị thiếu hàng!");
            return;
        }
        System.out.println("====================DANH SÁCH SẢN PHẨM ĐÃ BÁN HẾT====================");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for ( SanPham sp: danhSachSPBanHet )
        {
            if (sp == null) continue;
            System.out.println(sp);
        }
    }

    public ArrayList<SanPham> danhSachSapHetHan()
    {
        ArrayList<SanPham> sanPhams = new ArrayList<>();
        for ( SanPham sp: danhSachSanPham)
        {
            if (sp == null) continue;
            if ( sp.isTonTai() && sp instanceof ThucPham && ((ThucPham) sp).ktraNgayHetHan() < ((ThucPham) sp).kiemTraSoNgaySuDung()*0.7 && sp.getSoLuongTonKho() != 0 )
            {
                sanPhams.add(sp) ;
            }
        }
        return sanPhams ;
    }

    public ArrayList<SanPham> danhSachTonKhoThap()
    {
        ArrayList<SanPham> sanPhams = new ArrayList<>() ;
        for ( SanPham sp: danhSachSanPham )
        {
            if (sp == null) continue;
            if ( sp.isTonTai() && ((sp instanceof ThucPham && sp.getSoLuongTonKho() <= 5) || (sp instanceof TieuDung && sp.getSoLuongTonKho() <= 5 ) || ( sp instanceof DienTu && sp.getSoLuongTonKho() <= 3)) )
            {
                sanPhams.add(sp) ;
            }
        }
        return sanPhams ;
    }

    public void inDanhSachTonKhoThap()
    {
        ArrayList<SanPham> sanPhams = danhSachTonKhoThap() ;
        if ( sanPhams == null )
        {
            System.out.println("Không có sản phẩm nào sắp hết hàng!");
            return;
        }
        System.out.println("====================DANH SÁCH SẢN PHẨM SẮP HẾT HÀNG====================");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for ( SanPham sp:sanPhams)
        {
            if (sp == null) continue;
            System.out.println(sp);
        }
    }

    public void inDanhSachSapHetHan()
    {
        ArrayList<SanPham> sanPhams = danhSachSapHetHan() ;
        if ( sanPhams == null )
        {
            System.out.println("Không có sản phẩm nào đang hết hạn!");
            return;
        }
        System.out.println("====================DANH SÁCH SẢN PHẨM HẾT HẠN====================");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for ( SanPham sp:sanPhams)
        {
            if (sp == null) continue;
            System.out.println(sp);
        }
    }

    public ArrayList<ThucPham> danhSachThucPhamSapHetHan()
    {
        ArrayList<ThucPham> danhSach = new ArrayList<>();
        for (SanPham sp : danhSachSapHetHan()) {
            if (sp instanceof ThucPham) danhSach.add((ThucPham) sp);
        }
        return danhSach;
    }
    public void thongKeTheoLoai(Scanner sc )
    {
        int luaChon ;
        boolean flag ;
        int tongSanPham ;
        if ( danhSachSanPham.isEmpty() )
        {
            System.out.println("Danh sách sản phẩm đang rỗng không thể tra cứu!");
            return;
        }
       do {
           System.out.println("==================CHỌN LOẠI THỐNG KÊ===================");
           System.out.println("1. Thống kê hàng thực phẩm!");
           System.out.println("2. Thống kê hàng tiêu dùng!");
           System.out.println("3. Thống kê hàng điện tử!");
           System.out.println("0. Trở về.");
           luaChon = Validation.luaChon( sc , "Nhập lựa chọn: " , 0 , 3 ) ;
           switch (luaChon)
           {
               case 1:
                   flag = false ;
                   tongSanPham = 0 ;
                   System.out.println("Danh sách các sản phẩm thuộc hàng thực phẩm!");
                   System.out.println("Đang thống kê...");
                   Validation.danghoatdong(1);
                   for ( SanPham sp: danhSachSanPham )
                   {
                       if (sp == null) continue;
                       if ( sp.isTonTai() && sp instanceof ThucPham )
                       {
                           System.out.println(sp);
                           tongSanPham++;
                           flag = true ;
                       }
                   }
                   if ( !flag )
                   {
                       System.out.println("Không có sản phẩm thuộc hàng thực phẩm!");
                   }
                   System.out.println("Tổng sản phẩm thuộc loại thực phẩm là: " + tongSanPham);
                   break;
               case 2:
                   flag = false;
                   tongSanPham = 0 ;
                   System.out.println("Danh sách các sản phẩm thuộc hàng tiêu dùng!");
                   System.out.println("Đang thống kê...");
                   Validation.danghoatdong(1);
                   for ( SanPham sp: danhSachSanPham )
                   {
                       if (sp == null) continue;
                       if ( sp.isTonTai() && sp instanceof TieuDung )
                       {
                           System.out.println(sp);
                           tongSanPham++ ;
                           flag = true ;
                       }
                   }
                   if ( !flag )
                   {
                       System.out.println("Không có sản phẩm thuộc hàng tiêu dùng!");
                   }
                   System.out.println("Tổng sản phẩm thuộc loại tiêu dùng là: " + tongSanPham);
                   break;
               case 3:
                   flag = false ;
                   tongSanPham = 0 ;
                   System.out.println("Danh sách các sản phẩm thuộc hàng điện tử!");
                   System.out.println("Đang thống kê...");
                   Validation.danghoatdong(1);
                   for ( SanPham sp: danhSachSanPham )
                   {
                       if (sp == null) continue;
                       if ( sp.isTonTai() && sp instanceof DienTu )
                       {
                           System.out.println(sp);
                           tongSanPham++ ;
                           flag = true ;
                       }
                   }
                   if ( !flag )
                   {
                       System.out.println("Không có sản phẩm thuộc hàng điện tử!");
                   }
                   System.out.println("Tổng sản phẩm thuộc loại điện tử là: " + tongSanPham);
                   break;
               case 0:
                   break;
               default:
                   System.out.println("Lựa chọn không hợp lệ!");
                   continue;
           }
       }
       while (luaChon != 0 ) ;
    }
    public void thongKeSanPhamTheoNhaCungCap (String maNCC )
    {
        int tongSanPham = 0 ;
        if ( danhSachSanPham.isEmpty())
        {
            System.out.println("Danh sách sản phẩm đang rỗng không thể tra cứu!");
            return;
        }
        System.out.println("Các sản phẩm thuộc nhà cung cấp có mã là " + maNCC );
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( SanPham sp: danhSachSanPham )
        {
            if (sp == null) continue;
            if ( sp.isTonTai() && sp.getMaNhaCungCap() != null && sp.getMaNhaCungCap().equalsIgnoreCase(maNCC) )
            {
                System.out.println(sp);
                tongSanPham++ ;
            }
        }

        System.out.println("Tổng sản phẩm thuộc nhà cung cấp " + maNCC + " là: " + tongSanPham);
    }

    public void thongKeTinhTrang()
    {
        LocalDate homNay = LocalDate.now();
        ArrayList<ThucPham> sanPhamHetHan = new ArrayList<>();
        ArrayList<ThucPham> sanPhamCanHetHan = new ArrayList<>();
        long tongSoLuongTon = 0;
        int soMatHang = 0;

        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for (SanPham sanPham : danhSachSanPham) {
            if (sanPham == null || !sanPham.isTonTai()) continue;
            soMatHang++;
            tongSoLuongTon += sanPham.getSoLuongTonKho();
            if (!(sanPham instanceof ThucPham) || sanPham.getSoLuongTonKho() <= 0) {
                continue;
            }
            ThucPham thucPham = (ThucPham) sanPham;
            LocalDate hanSuDung = thucPham.getHanSuDung();
            if (hanSuDung != null) {
                if (hanSuDung.isBefore(homNay)) sanPhamHetHan.add(thucPham);
                else if (!hanSuDung.isAfter(homNay.plusDays(30))) sanPhamCanHetHan.add(thucPham);
            }
        }

        System.out.printf("%n--- SẢN PHẨM ---%nSố mặt hàng: %d | Tổng tồn kho: %d%n",
                soMatHang, tongSoLuongTon);
        inThongTinThucPham("Hết hạn", sanPhamHetHan);
        inThongTinThucPham("Sắp hết hạn (30 ngày)", sanPhamCanHetHan);

    }

    private void inThongTinThucPham(String nhan, ArrayList<ThucPham> danhSach) {
        System.out.printf("%s còn tồn: %d%n", nhan, danhSach.size());
        danhSach.forEach(sp -> System.out.printf("- %s | %s | Tồn: %d | HSD: %s%n",
                sp.getMaSanPham(), sp.getTenSanPham(), sp.getSoLuongTonKho(), sp.getHanSuDung()));
    }
}
