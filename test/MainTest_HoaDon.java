import hoadon.*;
import utils.Validation;
import java.util.Scanner;

public class MainTest_HoaDon {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QuanLyHoaDon ql = new QuanLyHoaDon();
        int choice = -1;
        
        // SỬA: Dùng đường dẫn tương đối để ai cũng chạy được
        String filePath = "hoadon.txt"; 

        while (choice != 0) {
            System.out.println("\n================ MENU QUẢN LÝ HÓA ĐƠN ================");
            System.out.println("1. Nhập danh sách hóa đơn mới");
            System.out.println("2. Hiển thị danh sách hóa đơn");
            System.out.println("3. Tìm kiếm chính xác theo Mã HĐ");
            System.out.println("4. Tìm kiếm gần đúng (Mã hoặc Tên)");
            System.out.println("5. Sửa thông tin hóa đơn");
            System.out.println("6. Xóa hóa đơn theo Mã");
            System.out.println("7. Thống kê doanh thu / chi phí");
            System.out.println("8. Ghi danh sách ra file Text");
            System.out.println("9. Đọc danh sách từ file Text");
            System.out.println("0. Thoát chương trình");
            System.out.println("========================================================");

            choice = Validation.inputInt(sc, "Nhập lựa chọn của bạn: ");

            switch (choice) {
                case 1:
                    ql.nhapDanhSachHoaDon(sc);
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
                    ql.ghiFileText(filePath);
                    break;
                case 9:
                    ql.docFileText(filePath);
                    System.out.println("=> Đã nạp dữ liệu từ file. Hãy chọn chức năng 2 để xem danh sách!");
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