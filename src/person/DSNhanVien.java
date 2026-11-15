package person;

import java.util.HashMap;

import java.util.Map;

import java.util.Locale;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Scanner;

import utils.DataBase;
import utils.Validation;

public class DSNhanVien implements interfaces.IQuanLy<NhanVien> {
    ArrayList<NhanVien> list;

    public ArrayList<NhanVien> getDanhSachNhanVien() {
        return list;
    }

    /** Khởi tạo danh sách nhân viên rỗng. */
    public DSNhanVien() {
        list = new ArrayList<>();
    }

    /** Khởi tạo danh sách bằng danh sách được truyền vào. */
    public DSNhanVien(ArrayList<NhanVien> newList) {
        list = newList == null ? new ArrayList<>() : newList;
    }

    /** Khởi tạo danh sách dùng chung danh sách của đối tượng nguồn. */
    public DSNhanVien(DSNhanVien l) {
        this.list = l == null ? new ArrayList<>() : l.list;
    }
    /** Nhập số lượng rồi nhập thông tin và thêm từng nhân viên vào danh sách. */
    @Override
    public void taoDanhSach(Scanner sc) {
        int n = Validation.inputInt(sc, "Nhập số nhân viên cần tạo: ");
        for (int i = 0; i < n; i++) {
            System.out.printf("%n--- Nhập nhân viên %d/%d ---%n", i + 1, n);
            NhanVien nv = new NhanVien();
            do{nv.nhapThongTin(sc);}
            while(!isTrungMa(nv.getMaNV())); 
            list.add(nv);
        }
        DataBase.ghiVaoFile(new ArrayList<Object>(list), utils.DuongDanLuuTru.file("NhanVien.txt"), true);
    }

    /** In thông tin từng nhân viên; thông báo nếu danh sách rỗng. */
    @Override
    public void xuatDanhSach() {
        if (list.isEmpty()) {
            System.out.println("[!] Danh sách nhân viên đang trống.");
            return;
        }

        System.out.println("\n=== DANH SÁCH NHÂN VIÊN ===");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for (NhanVien nv : list) {
            if (nv != null && !nv.isSaThai()) System.out.println(nv);
        }
    }

    /** Nhập thông tin một nhân viên mới rồi thêm vào danh sách. */
    @Override
    public void them(Scanner sc) {
        NhanVien nv = new NhanVien();
        do{
            nv.nhapThongTin(sc);
        }
        while(!isTrungMa(nv.getMaNV()));         

        list.add(nv);
        DataBase.ghiVaoFile(list.getLast(), utils.DuongDanLuuTru.file("NhanVien.txt"), false);

    }

    /** Nhập thông tin k nhân viên mới rồi thêm vào danh sách. */
    public void them(Scanner sc, int k) {
        for (int i = 1; i <= k; i++) {
            System.out.printf("%n--- Nhập nhân viên %d/%d ---%n", i, k);
            NhanVien nv = new NhanVien();
        do{
            nv.nhapThongTin(sc);
        }
        while(!isTrungMa(nv.getMaNV()));         

            list.add(nv);
            DataBase.ghiVaoFile(list.getLast(), utils.DuongDanLuuTru.file("NhanVien.txt"), false);

        }
    }

    /** Cập nhật thông tin nhân viên theo mã; trả về false nếu không tìm thấy. */
    @Override
    public boolean sua(String maNV, Scanner sc) {
        try {

            int idx = timViTri(maNV);
            if (idx == -1) {
                System.out.println("[!] Không tìm thấy nhân viên trong danh sách.");
                return false;
            }

            NhanVien nv = list.get(idx);
            int choice;
            do {
                System.out.println("\n=== CẬP NHẬT NHÂN VIÊN: " + nv.getMaNV() + " ===");
                System.out.println("1. Họ tên");
                System.out.println("2. Email");
                System.out.println("3. Số điện thoại");
                System.out.println("4. Ngày sinh");
                System.out.println("5. Địa chỉ");
                System.out.println("6. Vai trò");
                System.out.println("7. Lương");
                System.out.println("8. Phụ cấp trách nhiệm");
                System.out.println("9. Trạng thái làm việc");
                System.out.println("0. Hoàn tất");
                choice = Validation.luaChon(sc, "Chọn thông tin cần sửa (0-10): ", 0, 10);

                switch (choice) {
                    case 1:
                        nv.setHoten(sc);
                        break;
                    case 2:
                        nv.setEmail(sc);
                        break;
                    case 3:
                        nv.setSdt(sc);
                        break;
                    case 4:
                        nv.setNgsinh(sc);
                        break;
                    case 5:
                        nv.setDiachi(sc);
                        break;
                    case 6:
                        String vaiTro = Validation.inputStringRegex(
                                sc, "Nhập vai trò (QuanLy/KeToan/QuanKho/ThuNgan): ",
                                "^(QuanLy|KeToan|QuanKho|ThuNgan)$",
                                "Vai trò không hợp lệ.");
                        nv.setVaiTro(vaiTro);
                        break;
                    case 7:
                        nv.setLuong(sc);
                        break;
                    case 8:
                        nv.setPhuCapTrachNhiem(sc);
                        break;
                    case 9:
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
            DataBase.ghiVaoFile(new ArrayList<Object>(list), utils.DuongDanLuuTru.file("NhanVien.txt"), true);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }

    /** Đánh dấu nhân viên đã sa thải nhưng vẫn giữ hồ sơ trong danh sách. */
    @Override
    public boolean xoa(String maNV) {
        int viTri = timViTri(maNV);
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
        DataBase.ghiVaoFile(new ArrayList<Object>(list), utils.DuongDanLuuTru.file("NhanVien.txt"), true);
        System.out.println("[+] Đã cập nhật trạng thái sa thải cho nhân viên " + maNV + ".");
        return true;
    }

    /** Tìm vị trí nhân viên theo mã; trả về -1 nếu không tìm thấy. */
    @Override
    public int timViTri(String maNV) {
        for (int i = 0; i < list.size(); i++) {
            if (maNV != null && list.get(i) != null && !list.get(i).isSaThai()
                    && maNV.trim().equalsIgnoreCase(list.get(i).getMaNV())) {
                return i;
            }
        }
        System.out.println("Không tìm thấy nhân viên hoặc nhân viên đã bị sa thải!");
        return -1;
    }

    @Override
    public NhanVien timMa(String ma) {
        int viTri = timViTri(ma);
        return viTri == -1 ? null : list.get(viTri);
    }

    @Override
    public NhanVien timTen(String ten) {
        if (ten == null || ten.isBlank()) return null;
        for (NhanVien nv : list) {
            if (nv != null && !nv.isSaThai() && nv.getHoten() != null
                    && nv.getHoten().toLowerCase(Locale.ROOT).contains(ten.trim().toLowerCase(Locale.ROOT))) return nv;
        }
        return null;
    }

    @Override
    public NhanVien timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) return null;
        for (NhanVien nv : list) {
            if (nv != null && !nv.isSaThai()
                    && nv.toString().toLowerCase(Locale.ROOT).contains(khoa.trim().toLowerCase(Locale.ROOT))) return nv;
        }
        return null;
    }

    /** Tìm vị trí nhân viên theo tên chính xác hoặc gần đúng; trả về -1 nếu không tìm thấy. */
    public NhanVien nhapTimKiem(Scanner sc) {
        System.out.print("Nhập mã hoặc tên nhân viên: ");
        String khoa = sc.nextLine().trim().toLowerCase();
        for (NhanVien nhanVien : list) {
            if (nhanVien == null || nhanVien.isSaThai() || nhanVien.getMaNV() == null) continue;
            if (nhanVien.getMaNV().equalsIgnoreCase(khoa)
                    || (nhanVien.getHoten() != null && nhanVien.getHoten().equalsIgnoreCase(khoa))
                    || (nhanVien.getHoten() != null && nhanVien.getHoten().toLowerCase().contains(khoa))) {
                System.out.println("[+] Đã tìm thấy nhân viên.");
                return nhanVien;
            }
        }
        System.out.println("[!] Không tìm thấy nhân viên.");
        return null;
    }

    public void timKhoa(Scanner sc) {
        while (true) {
            System.out.println("================ TÌM KIẾM NHÂN VIÊN ================");
            System.out.println("1. Mã nhân viên");
            System.out.println("2. Họ tên");
            System.out.println("3. Email");
            System.out.println("4. Số điện thoại");
            System.out.println("5. Địa chỉ");
            System.out.println("6. Ngày sinh");
            System.out.println("7. Vai trò");
            System.out.println("8. Lương");
            System.out.println("9. Phụ cấp");
            System.out.println("0. Trở về");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 9);
            if (luaChon == 0) return;
            boolean flag = false;
            System.out.println("Kết quả tra cứu:");
            switch (luaChon) {
                case 1: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã nhân viên cần tìm: ", Validation.MaNV_REGEX, "Mã nhân viên không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (NhanVien nv : list) {
                        if (nv != null && !nv.isSaThai() && ((nv.getMaNV()) != null && nv.getMaNV().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(nv);
                            flag = true;
                        }
                    }
                    break;
                }
                case 2: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập họ tên cần tìm: ", ".+", "Họ tên không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (NhanVien nv : list) {
                        if (nv != null && !nv.isSaThai() && ((nv.getHoten()) != null && (nv.getHoten()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(nv);
                            flag = true;
                        }
                    }
                    break;
                }
                case 3: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập email cần tìm: ", Validation.EMAIL_REGEX, "Email không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (NhanVien nv : list) {
                        if (nv != null && !nv.isSaThai() && ((nv.getEmail()) != null && nv.getEmail().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(nv);
                            flag = true;
                        }
                    }
                    break;
                }
                case 4: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập số điện thoại cần tìm: ", Validation.PHONE_REGEX, "Số điện thoại không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (NhanVien nv : list) {
                        if (nv != null && !nv.isSaThai() && ((nv.getSdt()) != null && nv.getSdt().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(nv);
                            flag = true;
                        }
                    }
                    break;
                }
                case 5: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập địa chỉ cần tìm: ", ".+", "Địa chỉ không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (NhanVien nv : list) {
                        if (nv != null && !nv.isSaThai() && ((nv.getDiaChi() == null ? null : nv.getDiachi()) != null && (nv.getDiaChi() == null ? null : nv.getDiachi()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(nv);
                            flag = true;
                        }
                    }
                    break;
                }
                case 6: {
                    LocalDate tu = Validation.inputLocalDateRegex(sc, "Từ (dd/MM/yyyy): ");
                    LocalDate den = Validation.inputLocalDateRegex(sc, "Đến (dd/MM/yyyy): ");
                    if (tu.isAfter(den)) {
                        System.out.println("Thời gian bắt đầu không được sau thời gian kết thúc!");
                        continue;
                    }
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (NhanVien nv : list) {
                        if (nv != null && !nv.isSaThai() && (nv.getNgsinh() != null && !nv.getNgsinh().isBefore(tu) && !nv.getNgsinh().isAfter(den))) {
                            System.out.println(nv);
                            flag = true;
                        }
                    }
                    break;
                }
                case 7: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập vai trò cần tìm: ", ".+", "Vai trò không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (NhanVien nv : list) {
                        if (nv != null && !nv.isSaThai() && ((nv.getVaiTro()) != null && (nv.getVaiTro()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(nv);
                            flag = true;
                        }
                    }
                    break;
                }
                case 8: {
                    long tu = Validation.inputLong(sc, "Giá trị từ: ");
                    long den = Validation.inputLong(sc, "Giá trị đến: ");
                    if (tu > den) {
                        System.out.println("Giá trị bắt đầu không được lớn hơn giá trị kết thúc!");
                        continue;
                    }
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (NhanVien nv : list) {
                        if (nv != null && !nv.isSaThai() && (nv.getLuong() >= tu && nv.getLuong() <= den)) {
                            System.out.println(nv);
                            flag = true;
                        }
                    }
                    break;
                }
                case 9: {
                    long tu = Validation.inputLong(sc, "Giá trị từ: ");
                    long den = Validation.inputLong(sc, "Giá trị đến: ");
                    if (tu > den) {
                        System.out.println("Giá trị bắt đầu không được lớn hơn giá trị kết thúc!");
                        continue;
                    }
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (NhanVien nv : list) {
                        if (nv != null && !nv.isSaThai() && (nv.getPhuCapTrachNhiem() >= tu && nv.getPhuCapTrachNhiem() <= den)) {
                            System.out.println(nv);
                            flag = true;
                        }
                    }
                    break;
                }
                default:
                    continue;
            }
            if (!flag) System.out.println("Không tìm thấy kết quả phù hợp!");
        }
    }

    public void thongKeTheoVaiTro()
    {
        Map<String, Integer> nhanVienTheoVaiTro = new HashMap<>();
        int soDaSaThai = 0;
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for (NhanVien nhanVien : list) {
            if (nhanVien == null) continue;
            if (nhanVien.isSaThai()) {
                soDaSaThai++;
                continue;
            }
            if (nhanVien.getVaiTro() != null)
                nhanVienTheoVaiTro.merge(nhanVien.getVaiTro(), 1, Integer::sum);
        }

        System.out.println("\n--- NHÂN VIÊN ĐANG LÀM ---");
        System.out.printf("Quản lý: %d | Kế toán: %d | Quản kho: %d | Thu ngân: %d | Đã sa thải: %d%n",
                nhanVienTheoVaiTro.getOrDefault("QuanLy", 0),
                nhanVienTheoVaiTro.getOrDefault("KeToan", 0),
                nhanVienTheoVaiTro.getOrDefault("QuanKho", 0),
                nhanVienTheoVaiTro.getOrDefault("ThuNgan", 0), soDaSaThai);

    }
    public boolean isTrungMa(String maNV){
        for(NhanVien i: list)
            if(i != null && i.getMaNV() != null && i.getMaNV().equalsIgnoreCase(maNV))
                {
                    System.out.println("Ma bi trung, hay nhap lai");
                    return false; 
                } 
        return true; 
    }

    public void phanCaLamViec(Scanner sc) {
        if (list.isEmpty()) {
            System.out.println("[!] Danh sách nhân viên đang trống.");
            return;
        }

        System.out.println("\n=== PHÂN CA LÀM VIỆC ===");
        while (true) {
            System.out.println("1. Ca 1 (06:00-14:00)");
            System.out.println("2. Ca 2 (14:00-22:00)");
            System.out.println("3. Hoàn tất");
            int luaChonCa = Validation.inputInt(sc, "Chọn ca: ");

            switch (luaChonCa) {
                case 1:
                    phanCongCa(sc, "6h-14h");
                    break;
                case 2:
                    phanCongCa(sc, "14h-22h");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("[!] Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void phanCongCa(Scanner sc, String tenCa) {
        int soNhanVienToiDa = 0;
        for (NhanVien nv : list) {
            if (nv == null) continue;
            String vaiTro = nv.getVaiTro();
            if (!nv.isSaThai()
                    && ("KeToan".equals(vaiTro) || "ThuNgan".equals(vaiTro) || "QuanKho".equals(vaiTro))) {
                soNhanVienToiDa++;
            }
        }

        int soNguoi;
        do {
            soNguoi = Validation.inputInt(sc, "Nhập số người cho ca " + tenCa + ": ");
            if (soNguoi < 0 || soNguoi > soNhanVienToiDa) {
                System.out.println("Số người phải từ 0 đến " + soNhanVienToiDa + ".");
            }
        } while (soNguoi < 0 || soNguoi > soNhanVienToiDa);

        ArrayList<NhanVien> nhanVienTrongCa = new ArrayList<>();
        for (int i = 0; i < soNguoi; i++) {
            NhanVien nhanVienDuocChon = null;
            while (nhanVienDuocChon == null) {
                System.out.println("\nChọn vai trò:");
                System.out.println("1. Kế toán");
                System.out.println("2. Thu ngân");
                System.out.println("3. Quản kho");
                int luaChonVaiTro = Validation.inputInt(sc, "Chọn vai trò: ");
                String vaiTro;
                switch (luaChonVaiTro) {
                    case 1:
                        vaiTro = "KeToan";
                        break;
                    case 2:
                        vaiTro = "ThuNgan";
                        break;
                    case 3:
                        vaiTro = "QuanKho";
                        break;
                    default:
                        System.out.println("[!] Vai trò không hợp lệ, vui lòng thử lại.");
                        continue;
                }

                boolean coNhanVienPhuHop = false;
                for (NhanVien nv : list) {
                    if (nv == null) continue;
                    if (!nv.isSaThai() && vaiTro.equals(nv.getVaiTro())
                            && !nhanVienTrongCa.contains(nv)) {
                        System.out.println("- Mã NV: " + nv.getMaNV());
                        coNhanVienPhuHop = true;
                    }
                }
                if (!coNhanVienPhuHop) {
                    System.out.println("[!] Không còn nhân viên vai trò " + vaiTro + " để phân ca.");
                    continue;
                }

                String maNV = Validation.inputStringRegex(
                        sc, "Nhập mã nhân viên cần phân ca: ", Validation.MaNV_REGEX,
                        "Mã nhân viên không đúng định dạng (NVxxx).");
                for (NhanVien nv : list) {
                    if (nv == null) continue;
                    if (maNV.equals(nv.getMaNV())
                            && vaiTro.equals(nv.getVaiTro())
                            && !nv.isSaThai()
                            && !nhanVienTrongCa.contains(nv)){
                        nhanVienDuocChon = nv;
                        break;
                    }
                }
                if (nhanVienDuocChon == null) {
                    System.out.println("[!] Mã nhân viên không tồn tại, sai vai trò hoặc đã được phân ca.");
                }
            }

            nhanVienTrongCa.add(nhanVienDuocChon);
            System.out.println("[+] Đã phân " + nhanVienDuocChon.getMaNV()
                    + " (" + nhanVienDuocChon.getVaiTro() + ") vào ca " + tenCa + ".");
        }

    }

    public void getDSNhanVien() {
        // đọc file danh sách nhân viên và lưu và list
    }
}
