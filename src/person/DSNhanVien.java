package person;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import interfaces.IQuanLy;
import utils.DataBase;
import utils.Validation;

public class DSNhanVien implements IQuanLy<NhanVien> {
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
    public boolean isTrungMa(String maNV){
        for(NhanVien i: list)
            if(i.getMaNV().equalsIgnoreCase(maNV))
                {
                    System.out.println("Ma bi trung, hay nhap lai");
                    return false; 
                } 
        return true; 
    }
    /** Nhập số lượng rồi nhập thông tin và thêm từng nhân viên vào danh sách. */
    public void taoDS(Scanner sc) {
        int n = Validation.inputInt(sc, "Nhập số nhân viên cần tạo: ");
        for (int i = 0; i < n; i++) {
            System.out.printf("%n--- Nhập nhân viên %d/%d ---%n", i + 1, n);
            NhanVien nv = new NhanVien();
            do{nv.nhapThongTin(sc);}
            while(isTrungMa(nv.getMaNV())); 
            list.add(nv);
        }
        DataBase.ghiVaoFile(list, "NhanVien.txt", true);
    }

    /** In thông tin từng nhân viên; thông báo nếu danh sách rỗng. */
    public void taoDanhSach(Scanner sc) {
        taoDS(sc);
    }

    public void xuat() {
        if (list.isEmpty()) {
            System.out.println("[!] Danh sách nhân viên đang trống.");
            return;
        }

        System.out.println("\n=== DANH SÁCH NHÂN VIÊN ===");
        for (NhanVien nv : list) {
            System.out.println(nv);
        }
    }

    @Override
    public void xuatDanhSach() {
        xuat();
    }

    /** Nhập thông tin một nhân viên mới rồi thêm vào danh sách. */
    @Override
    public void them(Scanner sc) {
        NhanVien nv = new NhanVien();
        do{
            nv.nhapThongTin(sc);
        }
        while(isTrungMa(nv.getMaNV()));         

        list.add(nv);
        DataBase.ghiVaoFile(list.getLast(), "NhanVien.txt", false);

    }

    /** Nhập thông tin k nhân viên mới rồi thêm vào danh sách. */
    public void them(Scanner sc, int k) {
        for (int i = 1; i <= k; i++) {
            System.out.printf("%n--- Nhập nhân viên %d/%d ---%n", i, k);
            NhanVien nv = new NhanVien();
        do{
            nv.nhapThongTin(sc);
        }
        while(isTrungMa(nv.getMaNV()));         

            list.add(nv);
            DataBase.ghiVaoFile(list.getLast(), "NhanVien.txt", false);

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

    @Override
    public int timViTri(String ma) {
        return timNV(ma);
    }

    @Override
    public NhanVien timMa(String ma) {
        int viTri = timViTri(ma);
        return viTri == -1 ? null : list.get(viTri);
    }

    @Override
    public NhanVien timTen(String ten) {
        if (ten == null || ten.isBlank()) {
            return null;
        }
        String khoa = ten.trim().toLowerCase(Locale.ROOT);
        for (NhanVien nhanVien : list) {
            String hoTen = nhanVien.getHoten();
            if (hoTen != null && hoTen.toLowerCase(Locale.ROOT).contains(khoa)) {
                return nhanVien;
            }
        }
        return null;
    }

    @Override
    public NhanVien timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) {
            return null;
        }
        String tuKhoa = khoa.trim().toLowerCase(Locale.ROOT);
        for (NhanVien nhanVien : list) {
            if (containsIgnoreCase(nhanVien.getMaNV(), tuKhoa)
                    || containsIgnoreCase(nhanVien.getHoten(), tuKhoa)
                    || containsIgnoreCase(nhanVien.getEmail(), tuKhoa)
                    || containsIgnoreCase(nhanVien.getSdt(), tuKhoa)) {
                return nhanVien;
            }
        }
        return null;
    }

    private static boolean containsIgnoreCase(String value, String lowerCaseQuery) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(lowerCaseQuery);
    }

    /** Tìm vị trí nhân viên theo tên chính xác hoặc gần đúng; trả về -1 nếu không tìm thấy. */
    public NhanVien timNV(Scanner sc) {
        System.out.print("Nhập mã hoặc tên nhân viên: ");
        NhanVien nhanVien = timKhoa(sc.nextLine());
        if (nhanVien == null) {
            System.out.println("[!] Không tìm thấy nhân viên.");
            return null;
        }
        System.out.println("[+] Đã tìm thấy nhân viên.");
        return nhanVien;
    }

    /** Đánh dấu nhân viên đã sa thải nhưng vẫn giữ hồ sơ trong danh sách. */
    public boolean saThai(String maNV) {
        int viTri = timNV(maNV);
        if (viTri == -1) {
            System.out.println("[!] Không tìm thấy nhân viên trong danh sách.");
            return false;
        }

        NhanVien nv = list.get(viTri);
        if (nv.isSaThai()) {
            System.out.println("[!] Nhân viên này đã được sa thải.");
            return false;
        }

        nv.setSaThai(true);
        DataBase.ghiVaoFile(list, "NhanVien.txt", true);
        System.out.println("[+] Đã cập nhật trạng thái sa thải cho nhân viên " + maNV + ".");
        DataBase.ghiVaoFile(list, "NhanVien.txt", true);
        return true;
    }

    @Override
    public boolean xoa(String ma) {
        return saThai(ma);
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
            System.out.println("10. Trạng thái làm việc");
            System.out.println("0. Hoàn tất");
            choice = Validation.luaChon(sc, "Chọn thông tin cần sửa (0-10): ", 0, 10);

            switch (choice) {
                case 1:
                    String maMoi ;
                    do{
                             maMoi = Validation.inputStringRegex(
                            sc, "Nhập mã nhân viên mới: ", Validation.MaNV_REGEX,
                            "Mã nhân viên không đúng định dạng (NVxxx).");                        
                    }
                    while(isTrungMa(maMoi));         
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
                case 10:
                    int trangThai = Validation.luaChon(
                            sc, "Chọn trạng thái: 1. Đang làm | 2. Đã sa thải: ", 1, 2);
                    nv.setSaThai(trangThai == 2);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("[!] Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        } while (choice != 0);
        DataBase.ghiVaoFile(list, "NhanVien.txt", true);
        return true;
    }

    @Override
    public boolean sua(String ma, Scanner sc) {
        return suaNV(ma, sc);
    }
}
