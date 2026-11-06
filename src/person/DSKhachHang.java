package person;

import java.util.ArrayList;
import java.util.Scanner;
import utils.Validation;

public class DSKhachHang {
    ArrayList<KhachHang> list;

    /** Khởi tạo danh sách khách hàng rỗng. */
    public DSKhachHang() {
        list = new ArrayList<>();
    }

    /** Khởi tạo danh sách bằng danh sách được truyền vào. */
    public DSKhachHang(ArrayList<KhachHang> newList) {
        list = newList;
    }

    /** Khởi tạo danh sách dùng chung danh sách của đối tượng nguồn. */
    public DSKhachHang(DSKhachHang l) {
        this.list = l.list;
    }

    /** Trả về danh sách khách hàng. */
    public ArrayList<KhachHang> getList() {
        return list;
    }

    /** Cập nhật danh sách khách hàng. */
    public void setList(ArrayList<KhachHang> list) {
        this.list = list;
    }

    /** Nhập số lượng rồi nhập thông tin và thêm từng khách hàng vào danh sách. */
    public void taoDS(Scanner sc) {
        int n = Validation.inputInt(sc, "Nhap so khach hang tao danh sach: ");
        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Nhap khach hang thu " + (i + 1) + " ---");
            KhachHang kh = new KhachHang();
            kh.nhapThongTin(sc);
            list.add(kh);
        }
    }

    /** In thông tin từng khách hàng; thông báo nếu danh sách rỗng. */
    public void xuatDSKH() {
        if (list == null || list.isEmpty()) {
            System.out.println("Danh sach khach hang rong.");
            return;
        }

        System.out.println("\n--- DANH SACH KHACH HANG ---");
        for (KhachHang kh : list) {
            System.out.println(kh.toString());
        }
    }

    /** Nhập thông tin một khách hàng mới rồi thêm vào danh sách. */
    public void themKH(Scanner sc) {
        KhachHang kh = new KhachHang();
        kh.nhapThongTin(sc);

        // Kiểm tra xem mã khách hàng đã tồn tại trong danh sách chưa
        if (timKH(kh.getMaKhachHang()) != -1) {
            System.out.println("[!] Ma khach hang da ton tai trong danh sach. Khong the them!");
            return;
        }

        list.add(kh);
        System.out.println("[+] Da them khach hang " + kh.getMaKhachHang() + " thanh cong!");
    }

    /** Tìm vị trí khách hàng theo mã; trả về -1 nếu không tìm thấy. */
    public int timKH(String maKH) {
        if (maKH == null || list == null) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            String ma = list.get(i).getMaKhachHang();
            if (ma != null && ma.trim().equalsIgnoreCase(maKH.trim())) {
                return i;
            }
        }
        return -1;
    }

    /** Tìm và trả về đối tượng khách hàng theo mã; trả về null nếu không tìm thấy. */
    public KhachHang timKiemKH(String maKH) {
        int idx = timKH(maKH);
        return (idx != -1) ? list.get(idx) : null;
    }

    /** Tìm danh sách khách hàng theo họ tên (tìm kiếm gần đúng). */
    public ArrayList<KhachHang> timKiemTheoTen(String hoTen) {
        ArrayList<KhachHang> ketQua = new ArrayList<>();
        if (hoTen == null || list == null) return ketQua;
        for (KhachHang kh : list) {
            if (kh.getHoten() != null && kh.getHoten().toLowerCase().contains(hoTen.toLowerCase().trim())) {
                ketQua.add(kh);
            }
        }
        return ketQua;
    }

    /** Xóa khách hàng theo mã hoặc thông báo nếu không tìm thấy. */
    public void xoaKH(String maKH) {
        int idx = timKH(maKH);
        if (idx != -1) {
            list.remove(idx);
            System.out.println("[+] Da xoa khach hang co ma " + maKH);
        } else {
            System.out.println("Khong tim thay trong danh sach");
        }
    }

    /** Cập nhật thông tin khách hàng theo mã; trả về false nếu không tìm thấy. */
    public boolean suaKH(String maKH, Scanner sc) {
        int idx = timKH(maKH);
        if (idx == -1) {
            System.out.println("Khong tim thay trong danh sach");
            return false;
        }

        KhachHang kh = list.get(idx);
        int choice;
        do {
            System.out.println("\n--- SUA THONG TIN KHACH HANG: " + kh.getMaKhachHang() + " ---");
            System.out.println("1. Ma khach hang");
            System.out.println("2. Ho ten");
            System.out.println("3. Email");
            System.out.println("4. So dien thoai");
            System.out.println("5. Ngay sinh");
            System.out.println("6. Dia chi");
            System.out.println("7. Ngay dang ky");
            System.out.println("8. Tong chi tieu");
            System.out.println("9. Diem tich luy");
            System.out.println("0. Hoan tat");
            choice = Validation.inputInt(sc, "Chon thong tin muon sua: ");

            switch (choice) {
                case 1:
                    String maMoi = Validation.inputStringRegex(
                            sc, "Nhap ma khach hang moi: ", Validation.MaKH_REGEX,
                            "Ma khach hang chua dung dinh dang (KHxxx)!");
                    boolean biTrung;
                    do {
                        biTrung = false;
                        for (int i = 0; i < list.size(); i++) {
                            String maTrongDanhSach = list.get(i).getMaKhachHang();
                            if (i != idx && maTrongDanhSach != null && maTrongDanhSach.equalsIgnoreCase(maMoi)) {
                                biTrung = true;
                                maMoi = Validation.inputStringRegex(
                                        sc, "Ma da ton tai, nhap ma khach hang khac: ",
                                        Validation.MaKH_REGEX,
                                        "Ma khach hang chua dung dinh dang (KHxxx)!");
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
                    kh.setNgayDangKy(Validation.inputLocalDateRegex(sc, "Nhap ngay dang ky moi (dd/MM/yyyy): "));
                    break;
                case 8:
                    long tongChiTieuMoi = Validation.inputLong(sc, "Nhap tong chi tieu moi (VND): ");
                    kh.setTongChiTieu(tongChiTieuMoi);
                    kh.capNhatThuHang();
                    break;
                case 9:
                    int diemMoi = Validation.inputInt(sc, "Nhap diem tich luy moi: ");
                    kh.setDiemTichLuy(diemMoi);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le, vui long chon lai.");
            }
        } while (choice != 0);

        return true;
    }

    /** Thống kê số lượng khách hàng theo từng hạng thành viên. */
    public void thongKeTheoHang() {
        if (list == null || list.isEmpty()) {
            System.out.println("Danh sach khach hang rong.");
            return;
        }

        int dong = 0, bac = 0, vang = 0, kimCuong = 0;
        for (KhachHang kh : list) {
            String hang = kh.getHangThanhVien();
            if ("Kim Cương".equalsIgnoreCase(hang)) kimCuong++;
            else if ("Vàng".equalsIgnoreCase(hang)) vang++;
            else if ("Bạc".equalsIgnoreCase(hang)) bac++;
            else dong++;
        }

        System.out.println("\n--- THONG KE HANG THANH VIEN ---");
        System.out.println("Tong so khach hang: " + list.size());
        System.out.println("- Hang Dong: " + dong);
        System.out.println("- Hang Bac: " + bac);
        System.out.println("- Hang Vang: " + vang);
        System.out.println("- Hang Kim Cuong: " + kimCuong);
    }
}

