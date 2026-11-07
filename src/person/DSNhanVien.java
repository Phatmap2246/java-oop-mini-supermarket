package person;

import java.util.ArrayList;
import java.util.Scanner;

import utils.Validation;

public class DSNhanVien {
    ArrayList<NhanVien> list;

    /** Khởi tạo danh sách nhân viên rỗng. */
    public DSNhanVien() {
        list = new ArrayList<>();
    }

    /** Khởi tạo danh sách bằng danh sách được truyền vào. */
    public DSNhanVien(ArrayList<NhanVien> newList) {
        list = newList;
    }

    /** Khởi tạo danh sách dùng chung danh sách của đối tượng nguồn. */
    public DSNhanVien(DSNhanVien l) {
        this.list = l.list;
    }

    /** Nhập số lượng rồi nhập thông tin và thêm từng nhân viên vào danh sách. */
    public void taoDS(Scanner sc) {
        int n = Validation.inputInt(sc, "Nhập số nhân viên cần tạo: ");
        for (int i = 0; i < n; i++) {
            System.out.printf("%n--- Nhập nhân viên %d/%d ---%n", i + 1, n);
            NhanVien nv = new NhanVien();
            nv.nhapThongTin(sc);
            list.add(nv);
        }
    }

    /** In thông tin từng nhân viên; thông báo nếu danh sách rỗng. */
    public void xuatDSNV() {
        if (list.isEmpty()) {
            System.out.println("[!] Danh sách nhân viên đang trống.");
            return;
        }

        System.out.println("\n=== DANH SÁCH NHÂN VIÊN ===");
        for (NhanVien nv : list) {
            System.out.println(nv);
        }
    }

    /** Nhập thông tin một nhân viên mới rồi thêm vào danh sách. */
    public void themNV(Scanner sc) {
        NhanVien nv = new NhanVien();
        nv.nhapThongTin(sc);
        list.add(nv);
    }

    /** Nhập thông tin k nhân viên mới rồi thêm vào danh sách. */
    public void themNV(Scanner sc, int k) {
        for (int i = 1; i <= k; i++) {
            System.out.printf("%n--- Nhập nhân viên %d/%d ---%n", i, k);
            NhanVien nv = new NhanVien();
            nv.nhapThongTin(sc);
            list.add(nv);
        }
    }

    /** Tìm vị trí nhân viên theo mã; trả về -1 nếu không tìm thấy. */
    public int timNV(String maNV) {
        for (int i = 0; i < list.size(); i++) {
            if (maNV.trim().equals(list.get(i).getMaNV().trim())) {
                return i;
            }
        }
        return -1;
    }

    /** Tìm vị trí nhân viên theo tên chính xác hoặc gần đúng; trả về -1 nếu không tìm thấy. */
    public NhanVien timNV(Scanner sc) {
        System.out.print("Nhập mã hoặc tên nhân viên: ");
        String khoa = sc.nextLine().trim().toLowerCase();
        for (NhanVien nhanVien : list) {
            if (nhanVien.getMaNV().equalsIgnoreCase(khoa)
                    || nhanVien.getMaNV().contains(khoa)
                    || nhanVien.getHoten().equalsIgnoreCase(khoa)
                    || nhanVien.getHoten().contains(khoa)) {
                System.out.println("[+] Đã tìm thấy nhân viên.");
                return nhanVien;
            }
        }
        System.out.println("[!] Không tìm thấy nhân viên.");
        return null;
    }

    /** Xóa nhân viên theo mã hoặc thông báo nếu không tìm thấy. */
    public void xoaNV(String maNV) {
        int viTri = timNV(maNV);
        if (viTri != -1) {
            list.remove(viTri);
            System.out.println("[+] Đã xóa nhân viên " + maNV + ".");
        } else {
            System.out.println("[!] Không tìm thấy nhân viên trong danh sách.");
        }
    }

    /** Cập nhật thông tin nhân viên theo mã; trả về false nếu không tìm thấy. */
    public boolean suaNV(String maNV, Scanner sc) {
        int idx = timNV(maNV);
        if (idx == -1) {
            System.out.println("[!] Không tìm thấy nhân viên trong danh sách.");
            return false;
        }

        NhanVien nv = list.get(idx);
        int choice;
        do {
            System.out.println("\n=== CẬP NHẬT NHÂN VIÊN: " + nv.getMaNV() + " ===");
            System.out.println("1. Mã nhân viên");
            System.out.println("2. Họ tên");
            System.out.println("3. Email");
            System.out.println("4. Số điện thoại");
            System.out.println("5. Ngày sinh");
            System.out.println("6. Địa chỉ");
            System.out.println("7. Vai trò");
            System.out.println("8. Lương");
            System.out.println("9. Phụ cấp trách nhiệm");
            System.out.println("0. Hoàn tất");
            choice = Validation.luaChon(sc, "Chọn thông tin cần sửa (0-9): ", 0, 9);

            switch (choice) {
                case 1:
                    String maMoi = Validation.inputStringRegex(
                            sc, "Nhập mã nhân viên mới: ", Validation.MaNV_REGEX,
                            "Mã nhân viên không đúng định dạng (NVxxx).");
                    boolean biTrung;
                    do {
                        biTrung = false;
                        for (int i = 0; i < list.size(); i++) {
                            String maTrongDanhSach = list.get(i).getMaNV();
                            if (i != idx && maTrongDanhSach != null && maTrongDanhSach.equalsIgnoreCase(maMoi)) {
                                biTrung = true;
                                maMoi = Validation.inputStringRegex(sc, "Mã đã tồn tại, nhập mã khác: ",
                                    Validation.MaNV_REGEX,
                                    "Mã nhân viên không đúng định dạng (NVxxx).");
                                break;
                            }
                        }
                    } while (biTrung);
                    nv.setMaNV(maMoi);
                    break;
                case 2:
                    nv.setHoten(sc);
                    break;
                case 3:
                    nv.setEmail(sc);
                    break;
                case 4:
                    nv.setSdt(sc);
                    break;
                case 5:
                    nv.setNgsinh(sc);
                    break;
                case 6:
                    nv.setDiachi(sc);
                    break;
                case 7:
                    String vaiTro = Validation.inputStringRegex(
                            sc, "Nhập vai trò (QuanLy/KeToan/QuanKho/ThuNgan): ",
                            "^(QuanLy|KeToan|QuanKho|ThuNgan)$",
                            "Vai trò không hợp lệ.");
                    nv.setVaiTro(vaiTro);
                    break;
                case 8:
                    nv.setLuong(sc);
                    break;
                case 9:
                    nv.setPhuCapTrachNhiem(sc);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("[!] Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        } while (choice != 0);

        return true;
    }
    // TODO: Bổ sung chức năng lưu và đọc danh sách nhân viên.
}
