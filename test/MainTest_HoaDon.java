import hoadon.DanhSachHoaDonBan;
import hoadon.HoaDonBanHang;
import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import sanpham.QuanLySanPham;
import utils.Validation;
import java.util.Scanner;

public class MainTest_HoaDon {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DanhSachHoaDonBan danhSachHoaDonBan = new DanhSachHoaDonBan();
        Danhsachkhuyenmai danhSachKhuyenMai = new Danhsachkhuyenmai();
        DanhSachSPKM danhSachSPKM = new DanhSachSPKM();
        QuanLySanPham quanLySanPham = new QuanLySanPham();

        String fileHoaDonBan = "hoadon_ban.txt";
        int choice = -1;

        while (choice != 0) {
            System.out.println("\n================ MENU HÓA ĐƠN BÁN ================");
            System.out.println("1. Lập hóa đơn bán mới");
            System.out.println("2. Hiển thị danh sách hóa đơn bán");
            System.out.println("3. Tìm hóa đơn theo mã");
            System.out.println("4. Tìm hóa đơn theo số điện thoại khách");
            System.out.println("5. Ghi danh sách hóa đơn bán ra file");
            System.out.println("6. Đọc danh sách hóa đơn bán từ file");
            System.out.println("7. Tạo khuyến mãi");
            System.out.println("8. Gắn khuyến mãi cho sản phẩm");
            System.out.println("0. Thoát chương trình");
            System.out.println("==================================================");

            choice = Validation.inputInt(sc, "Nhập lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                    danhSachHoaDonBan.themHoaDon(sc, danhSachSPKM, danhSachKhuyenMai,
                            quanLySanPham.getDanhSachSanPham());
                    break;
                case 2:
                    danhSachHoaDonBan.hienThiDanhSach();
                    break;
                case 3:
                    String maTim = Validation.inputStringRegex(sc,
                            "Nhập mã hóa đơn cần tìm: ",
                            "^[A-Za-z0-9]+$",
                            "Mã không hợp lệ (chỉ gồm chữ và số)!");
                    HoaDonBanHang hdTim = danhSachHoaDonBan.timMa(maTim);
                    if (hdTim != null) {
                        hdTim.hienThiHoaDon();
                    } else {
                        System.out.println("=> Không tìm thấy hóa đơn có mã: " + maTim);
                    }
                    break;
                case 4:
                    String sdtTim = Validation.inputStringRegex(sc,
                            "Nhập số điện thoại khách: ",
                            Validation.PHONE_REGEX,
                            "Số điện thoại không hợp lệ!");
                    HoaDonBanHang hdTheoSdt = danhSachHoaDonBan.timKiemHoaDonBanHang_SDT(sdtTim);
                    if (hdTheoSdt != null) {
                        hdTheoSdt.hienThiHoaDon();
                    } else {
                        System.out.println("=> Không tìm thấy hóa đơn của số: " + sdtTim);
                    }
                    break;
                case 5:
                    danhSachHoaDonBan.ghiFileText(fileHoaDonBan);
                    break;
                case 6:
                    danhSachHoaDonBan.docFileText(fileHoaDonBan);
                    break;
                case 7:
                    danhSachKhuyenMai.themKhuyenMai(sc);
                    danhSachKhuyenMai.xuatDanhSach();
                    break;
                case 8:
                    danhSachSPKM.themSanPhamKhuyenMai(sc);
                    danhSachSPKM.xuatDanhSach();
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
