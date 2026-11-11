package sanpham;

import dichvu.Nhacungcap;
import utils.Validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class QuanLySanPham{
    private ArrayList<SanPham> danhSachSanPham;

    public QuanLySanPham() {
        danhSachSanPham = new ArrayList<>();
    }

    public ArrayList<SanPham> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(ArrayList<SanPham> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }
    public void themSanPham(Scanner sc, ArrayList<Nhacungcap> danhSachNhaCungCap) {
        SanPham sanPham;
        System.out.println("Nhập loại sản phẩm cần thêm: ");
        System.out.println("1. Thực Phẩm");
        System.out.println("2. Tiêu Dùng");
        System.out.println("3. Điện tử ");
        int luaChon = Validation.inputInt(sc , "Nhập lựa chọn: ");
        switch (luaChon) {
            case 1:
                if (danhSachNhaCungCap == null) {
                    System.out.println("Vui lòng thêm nhà cung cấp!");
                } else {
                    sanPham = new ThucPham();
                    sanPham.setSanPham(sc);
                    System.out.println("Chọn nhà cung cấp: ");
                    for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                        System.out.println((i + 1) + ". Nhà cung cấp" + danhSachNhaCungCap.get(i).getTenDoiTac() + "!");
                    }
                    int luaChonNhaCungCap = Validation.inputInt(sc , "Nhập lựa chọn: ") ;
                    sanPham.setMaNhaCungCap(danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getMaDoiTac());
                    danhSachSanPham.add(sanPham);
                    System.out.println("Thêm sản phẩm thành công!");
                     danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getSanPhamCungCap().getDanhSachSanPham().add(sanPham) ; // tác vụ đặc biệt nên xem xét lại
                    // hoá đơn đối tác
                }
                break;
            case 2:
                if (danhSachNhaCungCap == null) {
                    System.out.println("Vui lòng thêm nhà cung cấp!");
                } else {
                    sanPham = new TieuDung();
                    sanPham.setSanPham(sc);
                    System.out.println("Chọn nhà cung cấp: ");
                    for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                        System.out.println((i + 1) + ". Nhà cung cấp" + danhSachNhaCungCap.get(i).getTenDoiTac() + "!");
                    }
                    int luaChonNhaCungCap = Validation.inputInt(sc , "Nhập lựa chọn: ") ;
                    sanPham.setMaNhaCungCap(danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getMaDoiTac());
                    danhSachSanPham.add(sanPham);
                    System.out.println("Thêm sản phẩm thành công!");
                    danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getSanPhamCungCap().getDanhSachSanPham().add(sanPham) ;
                    // hoá đơn đối tác
                }
                break;
            case 3:
                if (danhSachNhaCungCap == null) {
                    System.out.println("Vui lòng thêm nhà cung cấp!");
                } else {
                    sanPham = new DienTu();
                    sanPham.setSanPham(sc);
                    System.out.println("Chọn nhà cung cấp: ");
                    for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                        System.out.println((i + 1) + ". Nhà cung cấp" + danhSachNhaCungCap.get(i).getTenDoiTac() + "!");
                    }
                    int luaChonNhaCungCap = Validation.inputInt(sc , "Nhập lựa chọn: ") ;
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

    public void taoDuLieuSanPham(Scanner sc, ArrayList<Nhacungcap> danhSachNhaCungCap) {
        System.out.println("Tạo dữ liệu cho danh sách sản phẩm!");
        boolean tiepTuc = true;
        while (tiepTuc) {
            themSanPham(sc, danhSachNhaCungCap);
            System.out.println("Bạn có muốn tiếp tục(1/0): ");
            int luaChon = sc.nextInt();
            sc.nextLine();
            if (luaChon == 0) {
                tiepTuc = false;
            }
        }
        System.out.println("Tạo dữ liệu thành công!");
    }

    public int timKiemSanPham(String maSanPham) {
        for (int i = 0; i < danhSachSanPham.size(); i++) {
            if (danhSachSanPham.get(i).getMaSanPham().equalsIgnoreCase(maSanPham)) {
                return i;
            }
        }
        return -1;
    }

    public void xoaSanPham(String maSanPham) {
        int viTri = timKiemSanPham(maSanPham);
        if (viTri == -1) {
            System.out.println("Mã sản phẩm không hợp lệ, sản phẩm không tồn tại!");
        } else {
            danhSachSanPham.remove(viTri);
            System.out.println("Xoá sản phẩm thành công!");
        }
    }
    
    public void suaThongTinSanPham(Scanner sc, String maSanPham, ArrayList<Nhacungcap> danhSachNhaCungCap) {
        System.out.println("Sửa thông tin sản phẩm!");
        int index = timKiemSanPham(maSanPham);
        DateTimeFormatter khuon = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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
            int luachon = sc.nextInt();
            sc.nextLine();
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
                    ((ThucPham) sanPham).setNgaySanXuat(LocalDate.parse(sc.nextLine(), khuon));
                    break;
                case 6:
                    System.out.println("Nhập hạn sử dụng mới cho sản phẩm(dd/MM/yyyy): ");
                    ((ThucPham) sanPham).setHanSuDung(LocalDate.parse(sc.nextLine(), khuon));
                    break;
                case 7:
                    ((ThucPham) sanPham).setNhietDoBaoQuan(Validation.inputInt(sc , "Nhập nhiệt độ bảo quản mới cho sản phẩm: "));
                    sc.nextLine();
                    break;
                case 8:
                    System.out.println("Nhập môi trường bảo quản mới cho sản phẩm: ");
                    ((ThucPham) sanPham).setBaoQuan(sc.nextLine());
                    break;
                case 9:
                    System.out.println("Chọn nhà cung cấp: ");
                    for (int i = 0; i < danhSachNhaCungCap.size(); i++) {
                        System.out.println((i + 1) + ". Nhà cung cấp" + danhSachNhaCungCap.get(i).getTenDoiTac() + "!");
                    }
                    int luaChonNhaCungCap = Validation.inputInt(sc , "Nhập lựa chọn: ") ;
                    sanPham.setMaNhaCungCap(danhSachNhaCungCap.get(luaChonNhaCungCap - 1).getMaDoiTac());
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
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
                        return;
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
                    return;
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
                        return;
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
                    return;
            }
        }
        System.out.println("Đã thay đổi thông tin sản phẩm!");
    }
    public void inDanhSachSanPham()
    {
        for ( SanPham sanPham:danhSachSanPham )
            System.out.println(sanPham);
    }
    public SanPham traCuuSanPham( String maSanPham)
    {
        int index = timKiemSanPham(maSanPham);
        if ( index == -1 )
        {
            return null ;
        }
        return danhSachSanPham.get(index) ;
    }
}
