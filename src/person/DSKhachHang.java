package person;

import java.util.Scanner;

import utils.Validation;

public class DSKhachHang {
    private KhachHang[] list;
    private int n; // Số lượng khách hàng hiện tại

    /** Khởi tạo danh sách khách hàng ban đầu rỗng. */
    public DSKhachHang() {
        this.list = new KhachHang[0];
        this.n = 0;
    }

    /** Khởi tạo với số lượng khách hàng xác định trước. */
    public DSKhachHang(int capacity) {
        if (capacity < 0)
            capacity = 0;
        this.list = new KhachHang[capacity];
        this.n = 0;
    }

    /** Khởi tạo từ một mảng truyền vào */
    public DSKhachHang(KhachHang[] arr, int soLuong) {
        if (arr != null && soLuong > 0) {
            this.n = soLuong;
            this.list = new KhachHang[soLuong];
            for (int i = 0; i < soLuong; i++) {
                this.list[i] = arr[i];
            }
        } else {
            this.list = new KhachHang[0];
            this.n = 0;
        }
    }

    /** Khởi tạo sao chép từ một đối tượng DSKhachHang khác. */
    public DSKhachHang(DSKhachHang other) {
        if (other != null) {
            this.n = other.n;
            this.list = new KhachHang[other.n];
            for (int i = 0; i < other.n; i++) {
                this.list[i] = other.list[i];
            }
        } else {
            this.list = new KhachHang[0];
            this.n = 0;
        }
    }

    /** Lấy mảng khách hàng hiện tại. */
    public KhachHang[] getList() {
        return list;
    }

    /** Cập nhật lại mảng khách hàng. */
    public void setList(KhachHang[] newList, int soLuong) {
        this.n = soLuong;
        this.list = new KhachHang[soLuong];
        for (int i = 0; i < soLuong; i++) {
            this.list[i] = newList[i];
        }
    }

    /** Lấy số lượng khách hàng thực tế. */
    public int getSoLuong() {
        return n;
    }

    // Phương thức
    /**
     * Thêm một khách hàng vào mảng
     */
    public void add(KhachHang kh) {
        if (kh == null)
            return;
        KhachHang[] newList = new KhachHang[n + 1];
        for (int i = 0; i < n; i++) {
            newList[i] = list[i];
        }
        newList[n] = kh;
        list = newList;
        n++;
    }

    /** Nhập số lượng rồi nhập thông tin và thêm từng khách hàng vào danh sách. */
    public void taoDS(Scanner sc) {
        int soLuong = Validation.inputInt(sc, "Nhap so khach hang tao danh sach: ");
        for (int i = 0; i < soLuong; i++) {
            System.out.println("\n--- Nhap khach hang thu " + (i + 1) + " ---");
            KhachHang kh = new KhachHang();
            kh.nhapThongTin(sc);
            add(kh);
        }
    }

    /** In thông tin từng khách hàng; thông báo nếu danh sách rỗng. */
    public void xuatDSKH() {
        if (n == 0 || list == null) {
            System.out.println("Danh sach khach hang rong.");
            return;
        }

        System.out.println("\n--- DANH SACH KHACH HANG ---");
        for (int i = 0; i < n; i++) {
            System.out.println(list[i].toString());
        }
    }

    /** Nhập thông tin một khách hàng mới rồi thêm vào danh sách. */
    public void themKH(Scanner sc) {
        KhachHang kh = new KhachHang();
        kh.nhapThongTin(sc);

        // Kiểm tra xem mã khách hàng đã tồn tại trong danh sách chưa
        if (timKH(kh.getMaKhachHang()) != -1) {
            System.out.println("[!] Mã khách hàng đã tồn tại, không thể thêm.");
            return;
        }

        add(kh);
        System.out.println("[+] Da them khach hang " + kh.getMaKhachHang() + " thanh cong!");
    }

    /** Tìm vị trí khách hàng theo mã; trả về -1 nếu không tìm thấy. */
    public int timKH(String maKH) {
        if (maKH == null || n == 0) {
            return -1;
        }
        for (int i = 0; i < n; i++) {
            String ma = list[i].getMaKhachHang();
            if (ma != null && ma.trim().equalsIgnoreCase(maKH.trim())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Tìm và trả về đối tượng khách hàng theo mã; trả về null nếu không tìm thấy.
     */
    public KhachHang timKiemKH(String maKH) {
        int idx = timKH(maKH);
        return (idx != -1) ? list[idx] : null;
    }

    /**
     * Tìm danh sách khách hàng theo họ tên (tìm kiếm gần đúng), trả về mảng thủ
     * công.
     */
    public KhachHang[] timKiemTheoTen(String hoTen) {
        if (hoTen == null || n == 0) {
            return new KhachHang[0];
        }

        // Bước 1: Đếm số lượng khách hàng trùng khớp
        int dem = 0;
        for (int i = 0; i < n; i++) {
            if (list[i].getHoten() != null && list[i].getHoten().toLowerCase().contains(hoTen.toLowerCase().trim())) {
                dem++;
            }
        }

        // Bước 2: Tạo mảng mới đúng kích thước đếm được và gán phần tử bằng vòng lặp
        KhachHang[] ketQua = new KhachHang[dem];
        int viTri = 0;
        for (int i = 0; i < n; i++) {
            if (list[i].getHoten() != null && list[i].getHoten().toLowerCase().contains(hoTen.toLowerCase().trim())) {
                ketQua[viTri] = list[i];
                viTri++;
            }
        }
        return ketQua;
    }

    /**
     * Xóa khách hàng theo mã:
     */
    public void xoaKH(String maKH) {
        int idx = timKH(maKH);
        if (idx == -1) {
            System.out.println("[!] Không tìm thấy khách hàng trong danh sách.");
            return;
        }

        KhachHang[] newList = new KhachHang[n - 1];
        int viTriMoi = 0;
        for (int i = 0; i < n; i++) {
            if (i != idx) {
                newList[viTriMoi] = list[i];
                viTriMoi++;
            }
        }
        list = newList;
        n--;
        System.out.println("[+] Đã xóa khách hàng có mã " + maKH + ".");
    }

    /** Cập nhật thông tin khách hàng */
    public boolean suaKH(String maKH, Scanner sc) {
        int idx = timKH(maKH);
        if (idx == -1) {
            System.out.println("[!] Không tìm thấy khách hàng trong danh sách.");
            return false;
        }

        KhachHang kh = list[idx];
        int choice;
        do {
            System.out.println("\n=== CẬP NHẬT KHÁCH HÀNG: " + kh.getMaKhachHang() + " ===");
            System.out.println("1. Mã khách hàng");
            System.out.println("2. Họ tên");
            System.out.println("3. Email");
            System.out.println("4. Số điện thoại");
            System.out.println("5. Ngày sinh");
            System.out.println("6. Địa chỉ");
            System.out.println("7. Ngày đăng ký");
            System.out.println("8. Tổng chi tiêu");
            System.out.println("9. Điểm tích lũy");
            System.out.println("0. Hoàn tất");
            choice = Validation.luaChon(sc, "Chọn thông tin cần sửa (0-9): ", 0, 9);

            switch (choice) {
                case 1:
                    String maMoi = Validation.inputStringRegex(
                            sc, "Nhập mã khách hàng mới: ", Validation.MaKH_REGEX,
                            "Mã khách hàng không đúng định dạng (KHxxx).");
                    boolean biTrung;
                    do {
                        biTrung = false;
                        for (int i = 0; i < n; i++) {
                            String maTrongDanhSach = list[i].getMaKhachHang();
                            if (i != idx && maTrongDanhSach != null && maTrongDanhSach.equalsIgnoreCase(maMoi)) {
                                biTrung = true;
                                maMoi = Validation.inputStringRegex(
                                        sc, "Mã đã tồn tại, nhập mã khác: ",
                                        Validation.MaKH_REGEX,
                                        "Mã khách hàng không đúng định dạng (KHxxx).");
                                break;
                            }
                        }
                    } while (biTrung);
                    kh.setMaKhachHang(maMoi);
                    break;
                case 2:
                    kh.setHoten(sc);
                    break;
                case 3:
                    kh.setEmail(sc);
                    break;
                case 4:
                    kh.setSdt(sc);
                    break;
                case 5:
                    kh.setNgsinh(sc);
                    break;
                case 6:
                    kh.setDiachi(sc);
                    break;
                case 7:
                    kh.setNgayDangKy(Validation.inputLocalDateRegex(
                            sc, "Nhập ngày đăng ký mới (dd/MM/yyyy): "));
                    break;
                case 8:
                    long tongChiTieuMoi = Validation.inputLong(sc, "Nhập tổng chi tiêu mới (VND): ");
                    kh.setTongChiTieu(tongChiTieuMoi);
                    kh.capNhatThuHang();
                    break;
                case 9:
                    int diemMoi = Validation.inputInt(sc, "Nhập điểm tích lũy mới: ");
                    kh.setDiemTichLuy(diemMoi);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("[!] Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        } while (choice != 0);

        return true;
    }

    /** Thống kê số lượng khách hàng theo từng hạng thành viên. */
    public void thongKeTheoHang() {
        if (n == 0 || list == null) {
            System.out.println("Danh sach khach hang rong.");
            return;
        }

        int dong = 0, bac = 0, vang = 0, kimCuong = 0;
        for (int i = 0; i < n; i++) {
            String hang = list[i].getHangThanhVien();
            if ("Kim Cương".equalsIgnoreCase(hang))
                kimCuong++;
            else if ("Vàng".equalsIgnoreCase(hang))
                vang++;
            else if ("Bạc".equalsIgnoreCase(hang))
                bac++;
            else
                dong++;
        }

        System.out.println("\n--- THONG KE HANG THANH VIEN ---");
        System.out.println("Tong so khach hang: " + n);
        System.out.println("- Hang Dong: " + dong);
        System.out.println("- Hang Bac: " + bac);
        System.out.println("- Hang Vang: " + vang);
        System.out.println("- Hang Kim Cuong: " + kimCuong);
    }
}
