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
            
            // Kiểm tra trùng Mã hóa đơn
            if (timKiemTheoMa(hd.getMaHoaDon())!=null) {
                System.out.println("[!] LỖI: Mã hóa đơn '" + hd.getMaHoaDon() + "' đã tồn tại! Hóa đơn này sẽ không được thêm vào.");
            }
            else{
                this.danhSachHoaDon.add(hd);
                System.out.println("[+] Đã thêm hóa đơn thành công!");
            }
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

                    // Kiểm tra trùng Mã hóa đơn
                    HoaDon checkTrung = timKiemTheoMa(maMoi);
                    if (checkTrung!=null && checkTrung != hd) {
                      System.out.println("[!] LỖI: Mã HĐ '" + maMoi + "' đã được dùng cho hóa đơn khác!");
                    }
                    else{
                        hd.setMaHoaDon(maMoi);
                        System.out.println("Đã cập nhật Mã HĐ!");
                    }
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

    // Ghi - đọc file
    public void ghiFileText(String tenFile){
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(tenFile),java.nio.charset.StandardCharsets.UTF_8)))
        {
            // Ghi thông tin chung
            for(HoaDon hd : this.danhSachHoaDon){
                if (hd instanceof HoaDonBanHang) {
                    HoaDonBanHang hdBan = (HoaDonBanHang) hd;
                    bw.write("BAN|"+hdBan.getMaHoaDon() +"|"+hdBan.getTenDoiTac()+"|"+hdBan.getTienKhuyenMai());
                    bw.newLine();
                }
                else if (hd instanceof HoaDonNhapHang){
                    HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
                    bw.write("NHAP|"+hdNhap.getMaHoaDon()+"|"+hdNhap.getTenDoiTac()+"|"+hdNhap.getMaNhaCungCap()+"|"+hdNhap.getThueVAT()+"|"+hdNhap.getChiPhiVanChuyen());
                    bw.newLine();
                }

                // ghi chi tiết
                for (CTHoaDon ct : hd.getDanhSachChiTiet()) {
                    bw.write("CT|" + ct.getMaSanPham() + "|" + ct.getTenSanPham() + "|" + 
                             ct.getSoLuong() + "|" + ct.getDonGia() + "|" + ct.getChietKhau());
                    bw.newLine();
                }
            }
                System.out.println("[+] Đã ghi dữ liệu thành công vào file: " + tenFile);
            }
            
        catch (java.io.IOException e){
            System.out.println("[!] Lỗi khi ghi file: " + e.getMessage());
        }
    }
    public void docFileText(String tenFile){
        this.danhSachHoaDon.clear(); // Xóa ds cũ
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(tenFile ,java.nio.charset.StandardCharsets.UTF_8))){
            String line;
            HoaDon hdHienTai = null;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|",-1) ; // cắt dựa vào dấu | và thêm -1 để không bị mất ô trống
                if (parts.length >0){
                    String loai = parts[0]; // lấy ra loại hóa đơn
                    if (loai.equals("BAN") && parts.length ==4){
                        // parts: BAN | MaHD | TenKH | TienKhuyenMai

                        // Kiểm tra Mã hóa đơn trùng
                        if (timKiemTheoMa(parts[1]) == null) {
                            hdHienTai = new HoaDonBanHang(parts[1],parts[2]);
                            ((HoaDonBanHang) hdHienTai).setTienKhuyenMai(Double.parseDouble(parts[3]));
                            this.danhSachHoaDon.add(hdHienTai);
                        }
                        else{
                            System.out.println("[!] Cảnh báo: Bỏ qua hóa đơn trùng mã " + parts[1] + " trong file!");
                            hdHienTai = null; // Bỏ qua !
                        }
                        
                    }

                    else if (loai.equals("NHAP") && parts.length == 6) {
                        // parts: NHAP | MaHD | TenNCC | MaNCC | ThueVAT | PhiVanChuyen

                        // Kiểm tra trùng Mã hóa đơn
                        if (timKiemTheoMa(parts[1]) == null) {
                            hdHienTai = new HoaDonNhapHang(parts[1],parts[2],parts[3],Double.parseDouble(parts[5]),Double.parseDouble(parts[4]));
                            this.danhSachHoaDon.add(hdHienTai);
                        }
                        else{
                            System.out.println("[!] Cảnh báo: Bỏ qua hóa đơn trùng mã " + parts[1] + " trong file!");
                            hdHienTai = null; // Bỏ qua !
                        }
                        
                    }

                    else if (loai.equals("CT") && hdHienTai != null && parts.length ==6){
                        // parts: CT | MaSP | TenSP | SoLuong | DonGia | ChietKhau (thêm chi tiết cho hóa đơn)
                        CTHoaDon ct = new CTHoaDon(parts[1],parts[2], Integer.parseInt(parts[3]),Double.parseDouble(parts[4]),Double.parseDouble(parts[5]));
                        hdHienTai.themChiTiet(ct);
                    }
                }
            }
            System.out.println("[+] Đã đọc dữ liệu thành công từ file: " + tenFile);
        }
        // Catch 1 Bắt lỗi I/O (lỗi không tìm thấy file, lỗi đóng file)
        catch (java.io.IOException e) { 
            System.out.println("[!] Lỗi khi đọc file (Có thể file chưa tồn tại): " + e.getMessage());
        } 
        // Catch 2 Bắt lỗi ép kiểu số (nếu file bị sửa tay sai định dạng)
        catch(NumberFormatException e){
            System.out.println("[!] Lỗi định dạng số: File dữ liệu bị hỏng hoặc bị sửa tay sai!");
        }
    }
}

