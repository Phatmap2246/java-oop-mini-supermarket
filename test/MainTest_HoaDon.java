import hoadon.*;
import utils.Validation;
import java.util.Scanner;
import dichvu.Danhsachdoitac;
import dichvu.Danhsachkhuyenmai;
import dichvu.DanhSachSPKM;
import sanpham.QuanLySanPham;

public class MainTest_HoaDon {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QuanLyHoaDon ql = new QuanLyHoaDon();
        Danhsachdoitac danhSachDoiTac = new Danhsachdoitac();
        Danhsachkhuyenmai danhSachKhuyenMai = new Danhsachkhuyenmai();
        DanhSachSPKM danhSachSPKM = new DanhSachSPKM();
        QuanLySanPham quanLySanPham = new QuanLySanPham();
        int choice = -1;
        
        String fileHoaDonBan = "hoadon_ban.txt";
        String fileHoaDonNhap = "hoadon_nhap.txt";

        while (choice != 0) {
            System.out.println("\n================ MENU QUẢN LÝ HÓA ĐƠN ================");
            System.out.println("1. Nhập danh sách hóa đơn mới");
            System.out.println("2. Hiển thị danh sách hóa đơn");
            System.out.println("3. Tìm kiếm chính xác theo Mã HĐ");
            System.out.println("4. Tìm kiếm gần đúng (Mã hoặc Tên)");
            System.out.println("5. Sửa thông tin hóa đơn");
            System.out.println("6. Xóa hóa đơn theo Mã");
            System.out.println("7. Thống kê doanh thu / chi phí");
            System.out.println("8. Ghi hóa đơn bán ra file");
            System.out.println("9. Đọc hóa đơn bán từ file");
            System.out.println("10. Ghi hóa đơn nhập ra file");
            System.out.println("11. Đọc hóa đơn nhập từ file");
            System.out.println("12. Tạo dữ liệu nhà cung cấp/đơn vị vận chuyển");
            System.out.println("13. Đăng ký xe cho đơn vị vận chuyển");
            System.out.println("14. Quản kho lập phiếu nhập");
            System.out.println("15. Nhà cung cấp báo xe và phí vận chuyển");
            System.out.println("16. Tạo khuyến mãi");
            System.out.println("17. Gắn khuyến mãi cho sản phẩm");
            System.out.println("18. Xem khuyến mãi và sản phẩm áp dụng");
            System.out.println("19. Quản lý duyệt hóa đơn nhập và cập nhật tồn kho");
            System.out.println("20. Đọc dữ liệu từ file gộp cũ hoadon.txt");
            System.out.println("0. Thoát chương trình");
            System.out.println("========================================================");

            choice = Validation.inputInt(sc, "Nhập lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                    ql.nhapDanhSachHoaDon(sc, danhSachDoiTac,
                            danhSachSPKM, danhSachKhuyenMai, quanLySanPham);
                    break;
                case 2:
                    ql.hienThiDanhSach();
                    break;
                case 3:
                    String maTim = Validation.inputStringRegex(sc, 
                        "Nhập mã HĐ cần tìm: ", 
                        "^[A-Z0-9]+$",
                        "Mã không hợp lệ (chỉ gồm chữ in hoa và số)!");
                    HoaDon hdTim = ql.timKiemTheoMa(maTim);
                    if (hdTim != null) {
                        System.out.println("=> Tìm thấy hóa đơn:");
                        hdTim.hienThiHoaDon();
                    } else {
                        System.out.println("=> Không tìm thấy hóa đơn có mã: " + maTim);
                    }
                    break;
                case 4:
                    System.out.print("Nhập từ khóa tìm kiếm (VD: HD, Nguyễn...): ");
                    String keyword = sc.nextLine();
                    ql.timKiemNhieuKhoa(keyword);
                    break;
                case 5:
                    String maSua = Validation.inputStringRegex(sc, 
                        "Nhập mã HĐ cần sửa: ", 
                        "^[A-Z0-9]+$",
                        "Mã không hợp lệ!");
                    if (ql.suaHoaDon(maSua, sc)) {
                        System.out.println("=> Đã hoàn tất quá trình sửa!");
                    } else {
                        System.out.println("=> Không tìm thấy hóa đơn để sửa!");
                    }
                    break;
                case 6:
                    String maXoa = Validation.inputStringRegex(sc, 
                        "Nhập mã HĐ cần xóa: ", 
                        "^[A-Z0-9]+$",
                        "Mã không hợp lệ!");
                    if (ql.xoaHoaDon(maXoa)) {
                        System.out.println("=> Đã xóa hóa đơn thành công!");
                    } else {
                        System.out.println("=> Không tìm thấy hóa đơn để xóa!");
                    }
                    break;
                case 7:
                    ql.thongKe();
                    break;
                case 8:
                    try {
                        ql.ghiHoaDonBanText(fileHoaDonBan);
                        System.out.println("[+] Đã ghi hóa đơn bán vào " + fileHoaDonBan);
                    } catch (java.io.IOException e) {
                        System.err.println("[!] Không thể ghi hóa đơn bán: " + e.getMessage());
                    }
                    break;
                case 9:
                    try {
                        ql.docHoaDonBanText(fileHoaDonBan);
                        System.out.println("[+] Đã đọc hóa đơn bán từ " + fileHoaDonBan);
                    } catch (java.io.IOException e) {
                        System.err.println("[!] Không thể đọc hóa đơn bán: " + e.getMessage());
                    }
                    break;
                case 10:
                    try {
                        ql.ghiHoaDonNhapText(fileHoaDonNhap);
                        System.out.println("[+] Đã ghi hóa đơn nhập vào " + fileHoaDonNhap);
                    } catch (java.io.IOException e) {
                        System.err.println("[!] Không thể ghi hóa đơn nhập: " + e.getMessage());
                    }
                    break;
                case 11:
                    try {
                        ql.docHoaDonNhapText(fileHoaDonNhap);
                        System.out.println("[+] Đã đọc hóa đơn nhập từ " + fileHoaDonNhap);
                    } catch (java.io.IOException e) {
                        System.err.println("[!] Không thể đọc hóa đơn nhập: " + e.getMessage());
                    }
                    break;
                case 12:
                    danhSachDoiTac.taoDuLieuDoiTac(sc);
                    break;
                case 13:
                    danhSachDoiTac.taoDanhSachXeChoDonViVanChuyen(sc);
                    break;
                case 14:
                    ql.taoHoaDonNhapHang(sc, danhSachDoiTac);
                    break;
                case 15:
                    String maHoaDonNhap = Validation.inputStringRegex(sc,
                            "Nhập mã hóa đơn nhập cần báo phí: ",
                            HoaDonNhapHang.MA_HD_NHAP_REGEX,
                            "Mã hóa đơn nhập không đúng định dạng!");
                    ql.nhaCungCapBaoPhiVanChuyen(sc, maHoaDonNhap, danhSachDoiTac);
                    break;
                case 16:
                    danhSachKhuyenMai.themKhuyenMai(sc);
                    danhSachKhuyenMai.inDanhSachKhuyenMai();
                    break;
                case 17:
                    danhSachSPKM.themSanPhamKhuyenMai(sc);
                    break;
                case 18:
                    danhSachKhuyenMai.inDanhSachKhuyenMai();
                    danhSachSPKM.inDanhSachSanPhamKM();
                    break;
                case 19:
                    String maDuyet = Validation.inputStringRegex(sc,
                            "Nhập mã hóa đơn nhập cần duyệt: ",
                            HoaDonNhapHang.MA_HD_NHAP_REGEX,
                            "Mã hóa đơn nhập không đúng định dạng!");
                    ql.duyetHoaDonNhapHang(maDuyet, quanLySanPham);
                    break;
                case 20:
                    ql.docFileText("hoadon.txt");
                    System.out.println("Đã nạp dữ liệu file cũ. Hãy lưu từng loại bằng các mục 8-11.");
                    break;
                case 0:
                    System.out.println("Tạm biệt và hẹn gặp lại!");
                    break;
                default:
                    System.out.println("[!] Lựa chọn không hợp lệ. Vui lòng chọn lại!");
            }
        }
        sc.close();
    }
}