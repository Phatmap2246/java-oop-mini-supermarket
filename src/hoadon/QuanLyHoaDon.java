package hoadon;

import java.util.ArrayList;
import java.util.Scanner;
import  utils.Validation;

public class QuanLyHoaDon {
    private ArrayList<HoaDon> danhSachHoaDon;

    public QuanLyHoaDon(){
        this.danhSachHoaDon = new ArrayList<>();
    }

    public ArrayList<HoaDon> getDanhSachHoaDon(){
        return this.danhSachHoaDon;
    }


    // a. Nhập danh sách n hóa đơn ban đầu
    public void nhapDanhSachHoaDon(Scanner sc) {
        int n = Validation.inputInt(sc, "Nhập số lượng hóa đơn muốn tạo: ");
        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Nhập hóa đơn thứ " + (i + 1) + " ---");
            System.out.println("1. Hóa đơn bán hàng");
            System.out.println("2. Hóa đơn nhập hàng");
            int chon = Validation.inputInt(sc, "Chọn loại hóa đơn (1/2): ");
            
            HoaDon hd = null;
            if (chon == 1) {
                hd = new HoaDonBanHang();
            } else if (chon == 2) {
                hd = new HoaDonNhapHang();
            } else {
                System.out.println("Lựa chọn không hợp lệ, bỏ qua hóa đơn này!");
                continue;
            }

            // Vì hd có kiểu HoaDon, nhưng thực tế là lớp con, 
            // ta cần ép kiểu để gọi hàm nhapHoaDon (nếu hàm này không có trong lớp cha)
            if (hd instanceof HoaDonBanHang) {
                ((HoaDonBanHang) hd).nhapHoaDon(sc);
            } else if (hd instanceof HoaDonNhapHang) {
                ((HoaDonNhapHang) hd).nhapHoaDon(sc);
            }
            
            this.danhSachHoaDon.add(hd);
            System.out.println("Đã thêm hóa đơn thành công!");
        }
    }

    // Xuat danh sach
    public void hienThiDanhSach() {
        if (this.danhSachHoaDon.isEmpty()) {
            System.out.println("[!] Danh sách hóa đơn đang trống!");
            return;
        }
        System.out.println("\n=== DANH SÁCH TOÀN BỘ HÓA ĐƠN ===");
        for (HoaDon hd : this.danhSachHoaDon) {
            hd.hienThiHoaDon(); // Tự động gọi đúng hàm in của lớp con
        }
    }

    // them 1 phan tu
     public void themHoaDon(HoaDon hd) {
        this.danhSachHoaDon.add(hd);
    }

    // tim kiem theo maHD
    public HoaDon timKiemTheoMa(String maHD){
        for(HoaDon hd: this.danhSachHoaDon){
            if (hd.getMaHoaDon().equalsIgnoreCase(maHD)) {
                return hd;
            }
        }
        return null;
    }


        // sua phan tu theo maHD
    public boolean suaHoaDon(String maHD, Scanner sc) {
        HoaDon hd = timKiemTheoMa(maHD);
        if (hd == null) {
            return false;
        }

        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- SỬA HÓA ĐƠN: " + hd.getMaHoaDon() + " ---");
            System.out.println("1. Sửa Mã Hóa Đơn");
            System.out.println("2. Sửa Tên Đối Tác (Khách/NCC)");
            
            // Hiển thị menu riêng tùy theo loại hóa đơn
            if (hd instanceof HoaDonBanHang) {
                System.out.println("3. Sửa Tiền Khách Đưa");
                System.out.println("4. Sửa Tiền Khuyến Mãi"); // THÊM MỚI
            } else if (hd instanceof HoaDonNhapHang) {
                System.out.println("3. Sửa Mã Nhà Cung Cấp");
                System.out.println("4. Sửa Thuế VAT");
                System.out.println("5. Sửa Phí Vận Chuyển"); // THÊM MỚI
            }
            System.out.println("0. Hoàn tất sửa");
            
            choice = Validation.inputInt(sc, "Chọn thông tin muốn sửa: ");

            switch (choice) {
                case 1:
                    String maMoi = Validation.inputStringRegex(sc, 
                        "Nhập mã HĐ mới: ", 
                        (hd instanceof HoaDonBanHang) ? HoaDonBanHang.MA_HD_BAN_REGEX : HoaDonNhapHang.MA_HD_NHAP_REGEX, 
                        "Lỗi: Mã HĐ không đúng định dạng!");
                    hd.setMaHoaDon(maMoi);
                    System.out.println("Đã cập nhật Mã HĐ!");
                    break;

                case 2:
                    String tenMoi = Validation.inputStringRegex(sc, 
                        "Nhập tên đối tác mới: ", 
                        Validation.HOTEN_REGEX, 
                        "Lỗi: Tên không hợp lệ!");
                    hd.setTenDoiTac(tenMoi);
                    System.out.println("Đã cập nhật Tên đối tác!");
                    break;

                case 3:
                    if (hd instanceof HoaDonBanHang) {
                        HoaDonBanHang hdBan = (HoaDonBanHang) hd;
                        double tienDuaMoi = Validation.inputDouble(sc, "Nhập số tiền khách đưa mới: ");
                        hdBan.kiemTraThanhToan(tienDuaMoi);
                        System.out.println("Đã cập nhật tiền khách đưa và tính lại tiền thừa!");
                    } else if (hd instanceof HoaDonNhapHang) {
                        HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
                        String maNCCMoi = Validation.inputStringRegex(sc, 
                            "Nhập mã NCC mới: ", 
                            HoaDonNhapHang.MA_NCC_REGEX, 
                            "Lỗi: Mã NCC không đúng!");
                        hdNhap.setMaNhaCungCap(maNCCMoi);
                        System.out.println("Đã cập nhật Mã NCC!");
                    }
                    break;

                case 4:
                    if (hd instanceof HoaDonBanHang) {
                        HoaDonBanHang hdBan = (HoaDonBanHang) hd;
                        double kmMoi = Validation.inputDouble(sc, "Nhập tiền khuyến mãi mới: ");
                        hdBan.setTienKhuyenMai(kmMoi); // Gọi setter
                        System.out.println("Đã cập nhật Khuyến mãi!");
                    } else if (hd instanceof HoaDonNhapHang) {
                        HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
                        double vatMoi = Validation.inputDouble(sc, "Nhập thuế VAT mới (%): ");
                        hdNhap.setThueVAT(vatMoi);
                        System.out.println("Đã cập nhật Thuế VAT!");
                    }
                    break;

                case 5:
                    if (hd instanceof HoaDonNhapHang) {
                        HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
                        double phiMoi = Validation.inputDouble(sc, "Nhập phí vận chuyển mới: ");
                        hdNhap.setChiPhiVanChuyen(phiMoi); // Gọi setter
                        System.out.println("Đã cập nhật Phí vận chuyển!");
                    }
                    break;

                case 0:
                    System.out.println("Hoàn tất quá trình sửa hóa đơn.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
        return true; 
    }
    // e. Xoá phần tử theo mã HD
    public boolean xoaHoaDon(String maHD) {
        HoaDon hd = timKiemTheoMa(maHD);
        if (hd != null) {
            this.danhSachHoaDon.remove(hd);
            return true;
        }
        return false;
    }

    // Bonus
    // f. Tìm kiếm nhiều khóa (Tìm gần đúng theo Mã HĐ hoặc Tên đối tác)
    public void timKiemNhieuKhoa(String keyword) {
        boolean found = false;
        System.out.println("\n--- KẾT QUẢ TÌM KIẾM CHO: '" + keyword + "' ---");
        for (HoaDon hd : this.danhSachHoaDon) {
            // Tìm gần đúng (contains) trên cả Mã và Tên
            if (hd.getMaHoaDon().toLowerCase().contains(keyword.toLowerCase()) || 
                hd.getTenDoiTac().toLowerCase().contains(keyword.toLowerCase())) {
                hd.hienThiHoaDon();
                found = true;
            }
        }
        if (!found) {
            System.out.println("Không tìm thấy hóa đơn nào phù hợp!");
        }
    }

    // g. Thống kê số liệu theo nhiều khóa
    public void thongKe() {
        if (this.danhSachHoaDon.isEmpty()) {
            System.out.println("[!] Không có dữ liệu để thống kê!");
            return;
        }
        
        int soLuongBan = 0, soLuongNhap = 0;
        double tongDoanhThuBan = 0, tongChiPhiNhap = 0;
        
        for (HoaDon hd : this.danhSachHoaDon) {
            if (hd instanceof HoaDonBanHang) {
                soLuongBan++;
                tongDoanhThuBan += hd.tinhTongTien();
            } else if (hd instanceof HoaDonNhapHang) {
                soLuongNhap++;
                tongChiPhiNhap += hd.tinhTongTien();
            }
        }
        
        System.out.println("\n========== BẢNG THỐNG KÊ ==========");
        System.out.println("Tổng số hóa đơn: " + this.danhSachHoaDon.size());
        System.out.println("Số hóa đơn BÁN: " + soLuongBan + " | Tổng doanh thu: " + tongDoanhThuBan + " VND");
        System.out.println("Số hóa đơn NHẬP: " + soLuongNhap + " | Tổng chi phí: " + tongChiPhiNhap + " VND");
        System.out.println("Lợi nhuận tạm tính: " + (tongDoanhThuBan - tongChiPhiNhap) + " VND");
        System.out.println("===================================");
    }

}
