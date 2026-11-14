package person;

import hoadon.DanhSachHoaDonBan;

import hoadon.DanhSachHoaDonNhap;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import dichvu.DanhSachSPKM;
import dichvu.Danhsachdoitac;
import dichvu.Danhsachkhuyenmai;
import dichvu.Nhacungcap;
import sanpham.QuanLySanPham;
import sanpham.ThucPham;
import utils.YeuCau;
import utils.Validation;

public class QuanLy extends NhanVien {
    private DSYeuCau dsYeuCau = new DSYeuCau();
    private transient DSNhanVien dsnv = new DSNhanVien();
    private transient Danhsachkhuyenmai dsKhuyenMai = new Danhsachkhuyenmai();
    private transient DanhSachSPKM dsSanPhamKhuyenMai = new DanhSachSPKM();

    /** Khởi tạo quản lý với thông tin mặc định. */
    public QuanLy() {
        super();
    }

    /** Khởi tạo quản lý bằng thông tin được truyền vào. */
    public QuanLy(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh,
            String maNV, String vaitro, long luong, long phuCapTrachNhiem) {
        super(email, hoten, sdt, diachi, ngsinh, maNV, vaitro, luong, phuCapTrachNhiem);
    }

    /** Sao chép thông tin nhân viên quản lý từ đối tượng nguồn. */
    public QuanLy(QuanLy ql) {
        super(ql);
    }

    /** Trả về vai trò cố định của lớp là Quản lý. */
    @Override
    public String getVaiTro() {
        return "QuanLy";
    }
    public DSNhanVien getDsNhanVien(){
        return dsnv;
    }

    public void setDSNhanVien( DSNhanVien list)
    {
        if (list == null) throw new IllegalArgumentException("Danh sách không được null.");
        dsnv = list; 
    }    public Danhsachkhuyenmai getDanhSachKhuyenMai() {
        return dsKhuyenMai;
    }
    public void setDSKhuyenMai( Danhsachkhuyenmai list)
    {
        if (list == null) throw new IllegalArgumentException("Danh sách không được null.");
        dsKhuyenMai = list; 
    }
    public DanhSachSPKM getDanhSachSPKM() {
        return dsSanPhamKhuyenMai;
    }
    public void setDanhSachSPKM( DanhSachSPKM list)
    {
        if (list == null) throw new IllegalArgumentException("Danh sách không được null.");
        dsSanPhamKhuyenMai = list; 
    }
    /** Hiển thị menu để thêm, sa thải hoặc sửa nhân viên trong danh sách quản lý. */
    public void quanLyNhanSu(Scanner sc) {
        while (true) {
        System.out.println("\n=== QUẢN LÝ NHÂN SỰ ===");
        System.out.println("1. Thêm nhân viên");
        System.out.println("2. Sa thải nhân viên");
        System.out.println("3. Sửa thông tin nhân viên");
        System.out.println("4. Hoàn tất");
        System.out.println("5. Xuất danh sách nhân viên");
        System.out.println("6. Tìm kiếm nhân viên");
        System.out.println("7. Nhập danh sách nhân viên");
        System.out.println("8. Thêm k nhân viên");
        System.out.println("9. Thống kê nhân viên theo vai trò");
        System.out.println("10. Phân ca làm việc");
        int c = Validation.inputInt(sc, "Chọn chức năng: ");
            switch (c) {
                case 1:
                    dsnv.them(sc);
                    break;
                case 2:
                    String maNV = Validation.inputStringRegex(sc, "Nhập mã nhân viên cần sa thải: ",
                            Validation.MaNV_REGEX, "Mã nhân viên không đúng định dạng (NVxxx).");
                    dsnv.xoa(maNV);
                    break;
                case 3:
                    String maNVSua = Validation.inputStringRegex(sc, "Nhập mã nhân viên cần sửa: ",
                            Validation.MaNV_REGEX, "Mã nhân viên không đúng định dạng (NVxxx).");
                    dsnv.sua(maNVSua, sc);
                    break;
                case 4:
                    System.out.println("Đã hoàn tất.");
                    break;
                case 5:
                    dsnv.xuatDanhSach();
                    break;
                case 6:
                    dsnv.timKhoa(sc);
                    break;
                case 7:
                    dsnv.taoDanhSach(sc);
                    break;
                case 8:
                    int k = Validation.inputInt(sc, "Nhập số nhân viên cần thêm: ");
                    dsnv.them(sc, k);
                    break;
                case 9:
                    dsnv.thongKeTheoVaiTro();
                    break;
                case 10:
                    phanCaLamViec(dsnv, sc);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
            if (c == 4) {
                break;
            }
        }
    }

    public void getDSNhanVien()
    {
        dsnv.getDSNhanVien();
    }

    public void phanCaLamViec(DSNhanVien ds, Scanner sc)
    {
        ds.phanCaLamViec(sc);
    }

    public void quanLySanPham(Scanner sc,QuanLySanPham dsSanPham,ArrayList<Nhacungcap> dsNCC) {
        while (true) {
            System.out.println("\n=== QUẢN LÝ SẢN PHẨM ===");
            System.out.println("1. Xem danh sách sản phẩm");
            System.out.println("2. Thêm sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("4. Sửa thông tin sản phẩm");
            System.out.println("5. Điều chỉnh giá bán");
            System.out.println("6. Thoát");
            System.out.println("7. Nhập danh sách sản phẩm");
            System.out.println("8. Thêm k sản phẩm");
            System.out.println("9. Tìm kiếm sản phẩm");
            System.out.println("10. Thống kê theo loại");
            System.out.println("11. Thống kê theo nhà cung cấp");
            System.out.println("12. Thống kê tình trạng sản phẩm");
            int luaChon = Validation.luaChon(sc, "Chọn chức năng (1-12): ", 1, 12);

            switch (luaChon) {
                case 1:
                    dsSanPham.xuatDanhSach();
                    break;
                case 2:
                    dsSanPham.them(sc,dsNCC);
                    break;
                case 3:
                    String c =Validation.inputStringRegex(sc, "Nhap ma san pham can sua (vi du: SP001)","^SP\\d+$", "Sai dinh dang ma san pham, nhap lai ");
                    dsSanPham.xoa(c);
                    break;
                case 4:
                    String d =Validation.inputStringRegex(sc, "Nhap ma san pham can sua (vi du: SP001)","^SP\\d+$", "Sai dinh dang ma san pham, nhap lai ");
                    dsSanPham.sua(sc,d,dsNCC);
                    break;
                case 5:
                    dieuChinhGiaBan(sc,dsSanPham);
                    break;
                case 6:
                    return;
                case 7:
                    dsSanPham.taoDanhSach(sc, dsNCC);
                    break;
                case 8:
                    int k = Validation.inputInt(sc, "Nhập số sản phẩm cần thêm: ");
                    dsSanPham.themKSanpham(sc, dsNCC, k);
                    break;
                case 9:
                    dsSanPham.timKhoa(sc);
                    break;
                case 10:
                    dsSanPham.thongKeTheoLoai(sc);
                    break;
                case 11:
                    String maNCC = Validation.inputStringRegex(sc, "Nhập mã nhà cung cấp: ", Validation.MaNCC_REGEX, "Mã nhà cung cấp không hợp lệ!");
                    dsSanPham.thongKeSanPhamTheoNhaCungCap(maNCC);
                    break;
                case 12:
                    dsSanPham.thongKeTinhTrang();
                    break;
                default:
                    throw new IllegalStateException("Lựa chọn quản lý sản phẩm không hợp lệ.");
            }
        }
    }

    private void dieuChinhGiaBan(Scanner sc,QuanLySanPham dssp)
    {
        dssp.dieuChinhGiaBan(sc);
    }

    public void duyetYeuCau(Scanner sc) throws IOException
    {
        dsYeuCau.duyetYeuCau(sc);
    }
    public void giamGiaCanHSD(Scanner sc, ArrayList<ThucPham> dscanDate)
    {
        dsSanPhamKhuyenMai.giamGiaCanHSD(sc, dscanDate, dsKhuyenMai);
    }
    public void themKhuyenMai(Scanner sc,DanhSachSPKM dspkm){
        while(true)
        {
            System.out.println("Tạo khuyến mãi cho nhóm sản phẩm:");
            System.out.println("1. Thực phẩm");
            System.out.println("2. Gia dụng");
            System.out.println("3. Điện tử");
            System.out.println("4. Thoát");
            int luaChon = Validation.luaChon(sc, "Hãy chọn (1-4): ", 1, 4);
            switch (luaChon) {
                case 1:
                    System.out.println("Đã chọn nhóm Thực phẩm.");
                    dspkm.them(sc);
                    break;
                case 2:
                    System.out.println("Đã chọn nhóm Gia dụng.");
                    dspkm.them(sc);
                    break;
                case 3:
                    System.out.println("Đã chọn nhóm Điện tử.");
                    dspkm.them(sc);
                    break;
                case 4:
                    return;
                default:
                    throw new IllegalStateException("Lựa chọn nhóm sản phẩm không hợp lệ.");
            }
        }
    }
    public void xemLichSuDuyet(String file)
    {
        dsYeuCau.xemLichSuDuyet(file);
    }
    // Thống kê các sản phẩm: Số lượng sản phẩm hiện có, số sản phẩm quá hạn/ gần hết hạn, cái nào?
    // Số lượng nhân viên với mỗi chức vụ
    //Số lượng yêu cầu duyệt được tạo, số đã và chưa duyệt
    public void thongKe(Scanner sc, DSNhanVien dsnv, QuanLySanPham dssp, ArrayList<YeuCau> dYeuCaus){
        if (dsnv == null || dssp == null || dYeuCaus == null) {
            throw new IllegalArgumentException("Danh sách nhân viên, sản phẩm và yêu cầu không được null.");
        }
        System.out.println("\n=== THỐNG KÊ TÌNH TRẠNG SIÊU THỊ ===");
        dssp.thongKeTinhTrang();
        dsnv.thongKeTheoVaiTro();
        DSYeuCau danhSachYeuCau = new DSYeuCau(dYeuCaus);
        danhSachYeuCau.thongKe();
    }

    public void thucHienNghiepVu(Scanner sc, QuanLySanPham qlsp, Danhsachdoitac dsDoiTac, DanhSachHoaDonNhap dsHoaDonNhap, DanhSachHoaDonBan dsHoaDonBan, DSKhachHang dskh)
    {
        int luaChon ;
        do {
            System.out.println("===================NGHIỆP VỤ QUẢN LÝ===================");
            System.out.println("1. Quản lý nhân sự!");
            System.out.println("2. Quản lý sản phẩm!");
            System.out.println("3. Quản lý đối tác!");
            System.out.println("4. Quản lý khuyến mãi!");
            System.out.println("5. Quản lý sản phẩm khuyến mãi!");
            System.out.println("6. Duyệt hóa đơn nhập!");
            System.out.println("7. Duyệt yêu cầu nhân viên!");
            System.out.println("8. Thống kê tình trạng siêu thị!");
            System.out.println("9. Quản lý khách hàng!");
            System.out.println("10. Giảm giá thực phẩm cận hạn!");
            System.out.println("11. Xem báo cáo hóa đơn bán!");
            System.out.println("12. Xem báo cáo hóa đơn nhập!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 12);
            switch (luaChon)
            {
                case 1: {
                    quanLyNhanSu(sc);
                    break;
                }
                case 2: {
                    quanLySanPham(sc, qlsp, dsDoiTac.layDanhSachNhaCungCap());
                    break;
                }
                case 3: {
                    dsDoiTac.quanLyDanhSach(sc);
                    break;
                }
                case 4: {
                    dsKhuyenMai.quanLyDanhSach(sc);
                    break;
                }
                case 5: {
                    dsSanPhamKhuyenMai.quanLyDanhSach(sc, qlsp, dsKhuyenMai);
                    break;
                }
                case 6: {
                    dsHoaDonNhap.duyetHoaDonNhap(sc);
                    break;
                }
                case 7: {
                    try {
                        duyetYeuCau(sc);
                    } catch (IOException e) {
                        System.out.println("Không thể duyệt yêu cầu: " + e.getMessage());
                    }
                    break;
                }
                case 8: {
                    try {
                        dsYeuCau.docDanhSachYeuCau(utils.DuongDanLuuTru.file("YeuCau.txt"));
                        thongKe(sc, dsnv, qlsp, dsYeuCau.getList());
                    } catch (IOException e) {
                        System.out.println("Không thể đọc danh sách yêu cầu: " + e.getMessage());
                    }
                    break;
                }
                case 9: {
                    dskh.quanLyDanhSach(sc);
                    break;
                }
                case 10: {
                    giamGiaCanHSD(sc, qlsp.danhSachThucPhamSapHetHan());
                    break;
                }
                case 11: {
                    dsHoaDonBan.xemBaoCao(sc);
                    break;
                }
                case 12: {
                    dsHoaDonNhap.xemBaoCao(sc);
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
