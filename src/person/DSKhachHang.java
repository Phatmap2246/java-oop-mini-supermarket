package person;

import java.util.Locale;

import java.time.LocalDate;

import java.util.Scanner;

import utils.DataBase;
import utils.Validation;

public class DSKhachHang implements interfaces.IQuanLy<KhachHang> {
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
            this.n = Math.min(soLuong, arr.length);
            soLuong = this.n;
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
        if (newList == null || soLuong < 0 || soLuong > newList.length) {
            throw new IllegalArgumentException("Danh sách hoặc số lượng khách hàng không hợp lệ.");
        }
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

    /** Nhập số lượng rồi nhập thông tin và thêm từng khách hàng vào danh sách. */
    @Override
    public void taoDanhSach(Scanner sc) {
        int soLuong = Validation.inputInt(sc, "Nhap so khach hang tao danh sach: ");
        for (int i = 0; i < soLuong; i++) {
            System.out.println("\n--- Nhap khach hang thu " + (i + 1) + " ---");
            KhachHang kh = new KhachHang();
            kh.nhapThongTin(sc);
            add(kh);
        }
    }

    /** In thông tin từng khách hàng; thông báo nếu danh sách rỗng. */
    @Override
    public void xuatDanhSach() {
        if (n == 0 || list == null) {
            System.out.println("Danh sach khach hang rong.");
            return;
        }

        System.out.println("\n--- DANH SACH KHACH HANG ---");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for (int i = 0; i < n; i++) {
            if (list[i] != null && list[i].isTonTai()) System.out.println(list[i]);
        }
    }

    // Phương thức
    /**
     * Thêm một khách hàng vào mảng
     */
    public void add(KhachHang kh) {
        if (kh == null) return;
        for (int i = 0; i < n; i++) {
            if (list[i] != null && kh.getMaKhachHang() != null
                    && kh.getMaKhachHang().equalsIgnoreCase(list[i].getMaKhachHang())) {
                System.out.println("Mã khách hàng đã tồn tại trong hồ sơ!");
                return;
            }
        }
        KhachHang[] newList = new KhachHang[n + 1];
        for (int i = 0; i < n; i++) {
            newList[i] = list[i];
        }
        newList[n] = kh;
        list = newList;
        n++;
    }

    /** Nhập thông tin một khách hàng mới rồi thêm vào danh sách. */
    @Override
    public void them(Scanner sc) {
        KhachHang kh = new KhachHang();
        kh.nhapThongTin(sc);

        // Kiểm tra xem mã khách hàng đã tồn tại trong danh sách chưa
        if (timViTri(kh.getMaKhachHang()) != -1) {
            System.out.println("[!] Mã khách hàng đã tồn tại, không thể thêm.");
            return;
        }

        add(kh);
        System.out.println("[+] Da them khach hang " + kh.getMaKhachHang() + " thanh cong!");
    }

    /** Cập nhật thông tin khách hàng */
    @Override
    public boolean sua(String maKH, Scanner sc) {
        try {

            int idx = timViTri(maKH);
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
                                if (list[i] == null) continue;
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
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa khách hàng theo mã:
     * Cập nhật trạng thái isTonTai = false (xóa mềm), không xóa khỏi mảng/database
     * và ghi đè danh sách cập nhật vào file KhachHang.txt.
     */
    @Override
    public boolean xoa(String maKH) {
        int idx = timViTri(maKH);
        if (idx == -1) {
            System.out.println("[!] Không tìm thấy khách hàng trong danh sách.");
            return false;
        }

        list[idx].setTonTai(false);
        ghiFileKhachHang();
        System.out.println("[+] Da cap nhat trang thai da xoa (isTonTai = false) cho khach hang " + maKH);
        return true;
    }

    /** Tìm vị trí khách hàng theo mã; trả về -1 nếu không tìm thấy. */
    @Override
    public int timViTri(String maKH) {
        if (maKH == null || n == 0) {
            System.out.println("Không tìm thấy khách hàng hoặc khách hàng đã bị xóa!");
        return -1;
        }
        for (int i = 0; i < n; i++) {
            if (list[i] == null || !list[i].isTonTai()) continue;
            String ma = list[i].getMaKhachHang();
            if (ma != null && ma.trim().equalsIgnoreCase(maKH.trim())) {
                return i;
            }
        }
        System.out.println("Không tìm thấy khách hàng hoặc khách hàng đã bị xóa!");
        return -1;
    }

    /**
     * Tìm và trả về đối tượng khách hàng theo mã; trả về null nếu không tìm thấy.
     */
    @Override
    public KhachHang timMa(String maKH) {
        int idx = timViTri(maKH);
        return (idx != -1) ? list[idx] : null;
    }

    /**
     * Tìm danh sách khách hàng theo họ tên (tìm kiếm gần đúng), trả về mảng thủ
     * công.
     */
    public KhachHang[] timDanhSachTheoTen(String hoTen) {
        if (hoTen == null || n == 0) {
            return new KhachHang[0];
        }

        // Bước 1: Đếm số lượng khách hàng trùng khớp
        int dem = 0;
        for (int i = 0; i < n; i++) {
            if (list[i] != null && list[i].isTonTai() && list[i].getHoten() != null && list[i].getHoten().toLowerCase().contains(hoTen.toLowerCase().trim())) {
                dem++;
            }
        }

        // Bước 2: Tạo mảng mới đúng kích thước đếm được và gán phần tử bằng vòng lặp
        KhachHang[] ketQua = new KhachHang[dem];
        int viTri = 0;
        for (int i = 0; i < n; i++) {
            if (list[i] != null && list[i].isTonTai() && list[i].getHoten() != null && list[i].getHoten().toLowerCase().contains(hoTen.toLowerCase().trim())) {
                ketQua[viTri] = list[i];
                viTri++;
            }
        }
        return ketQua;
    }

    @Override
    public KhachHang timTen(String ten) {
        if (ten == null || ten.isBlank()) return null;
        KhachHang[] ketQua = timDanhSachTheoTen(ten);
        return ketQua.length == 0 ? null : ketQua[0];
    }

    @Override
    public KhachHang timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) return null;
        for (int i = 0; i < n; i++) {
            KhachHang kh = list[i];
            if (kh != null && kh.isTonTai()
                    && kh.toString().toLowerCase(Locale.ROOT).contains(khoa.trim().toLowerCase(Locale.ROOT))) return kh;
        }
        return null;
    }

    public void timKhoa(Scanner sc) {
        while (true) {
            System.out.println("================ TÌM KIẾM KHÁCH HÀNG ================");
            System.out.println("1. Họ tên");
            System.out.println("2. Email");
            System.out.println("3. Số điện thoại");
            System.out.println("4. Địa chỉ");
            System.out.println("5. Ngày sinh");
            System.out.println("6. Hạng thành viên");
            System.out.println("7. Ngày đăng ký");
            System.out.println("8. Tổng chi tiêu");
            System.out.println("9. Điểm tích lũy");
            System.out.println("0. Trở về");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 10);
            if (luaChon == 0) return;
            boolean flag = false;
            System.out.println("Kết quả tra cứu:");
            switch (luaChon) {
                case 1: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập họ tên cần tìm: ", ".+", "Họ tên không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (int i = 0; i < n; i++) {
                        KhachHang kh = list[i];
                        if (kh != null && kh.isTonTai() && ((kh.getHoten()) != null && (kh.getHoten()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(kh);
                            flag = true;
                        }
                    }
                    break;
                }
                case 2: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập email cần tìm: ", Validation.EMAIL_REGEX, "Email không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (int i = 0; i < n; i++) {
                        KhachHang kh = list[i];
                        if (kh != null && kh.isTonTai() && ((kh.getEmail()) != null && kh.getEmail().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(kh);
                            flag = true;
                        }
                    }
                    break;
                }
                case 3: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập số điện thoại cần tìm: ", Validation.PHONE_REGEX, "Số điện thoại không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (int i = 0; i < n; i++) {
                        KhachHang kh = list[i];
                        if (kh != null && kh.isTonTai() && ((kh.getSdt()) != null && kh.getSdt().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(kh);
                            flag = true;
                        }
                    }
                    break;
                }
                case 4: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập địa chỉ cần tìm: ", ".+", "Địa chỉ không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (int i = 0; i < n; i++) {
                        KhachHang kh = list[i];
                        if (kh != null && kh.isTonTai() && ((kh.getDiaChi() == null ? null : kh.getDiachi()) != null && (kh.getDiaChi() == null ? null : kh.getDiachi()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(kh);
                            flag = true;
                        }
                    }
                    break;
                }
                case 5: {
                    LocalDate tu = Validation.inputLocalDateRegex(sc, "Từ (dd/MM/yyyy): ");
                    LocalDate den = Validation.inputLocalDateRegex(sc, "Đến (dd/MM/yyyy): ");
                    if (tu.isAfter(den)) {
                        System.out.println("Thời gian bắt đầu không được sau thời gian kết thúc!");
                        continue;
                    }
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (int i = 0; i < n; i++) {
                        KhachHang kh = list[i];
                        if (kh != null && kh.isTonTai() && (kh.getNgsinh() != null && !kh.getNgsinh().isBefore(tu) && !kh.getNgsinh().isAfter(den))) {
                            System.out.println(kh);
                            flag = true;
                        }
                    }
                    break;
                }
                case 6: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập hạng thành viên cần tìm: ", ".+", "Hạng thành viên không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (int i = 0; i < n; i++) {
                        KhachHang kh = list[i];
                        if (kh != null && kh.isTonTai() && ((kh.getHangThanhVien()) != null && (kh.getHangThanhVien()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(kh);
                            flag = true;
                        }
                    }
                    break;
                }
                case 7: {
                    LocalDate tu = Validation.inputLocalDateRegex(sc, "Từ (dd/MM/yyyy): ");
                    LocalDate den = Validation.inputLocalDateRegex(sc, "Đến (dd/MM/yyyy): ");
                    if (tu.isAfter(den)) {
                        System.out.println("Thời gian bắt đầu không được sau thời gian kết thúc!");
                        continue;
                    }
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (int i = 0; i < n; i++) {
                        KhachHang kh = list[i];
                        if (kh != null && kh.isTonTai() && (kh.getNgayDangKy() != null && !kh.getNgayDangKy().isBefore(tu) && !kh.getNgayDangKy().isAfter(den))) {
                            System.out.println(kh);
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
                    for (int i = 0; i < n; i++) {
                        KhachHang kh = list[i];
                        if (kh != null && kh.isTonTai() && (kh.getTongChiTieu() >= tu && kh.getTongChiTieu() <= den)) {
                            System.out.println(kh);
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
                    for (int i = 0; i < n; i++) {
                        KhachHang kh = list[i];
                        if (kh != null && kh.isTonTai() && (kh.getDiemTichLuy() >= tu && kh.getDiemTichLuy() <= den)) {
                            System.out.println(kh);
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

    /** Thống kê số lượng khách hàng theo từng hạng thành viên. */
    public void thongKeTheoHang() {
        if (n == 0 || list == null) {
            System.out.println("Danh sach khach hang rong.");
            return;
        }

        int dong = 0, bac = 0, vang = 0, kimCuong = 0;
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for (int i = 0; i < n; i++) {
            if (list[i] == null || !list[i].isTonTai()) continue;
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
        System.out.println("Tong so khach hang: " + (dong + bac + vang + kimCuong));
        System.out.println("- Hang Dong: " + dong);
        System.out.println("- Hang Bac: " + bac);
        System.out.println("- Hang Vang: " + vang);
        System.out.println("- Hang Kim Cuong: " + kimCuong);
    }
    public void thongKeKhachHangTheoHang( String Hang )
    {
        int tongKhachHang = 0 ;
        System.out.println("Danh sách khách hàng có hạng: " + Hang + " là: ");
        boolean flag = false ;
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( KhachHang kh: list)
        {
            if ( kh != null && kh.isTonTai() && Hang != null && Hang.equalsIgnoreCase(kh.getHangThanhVien()))
            {
                System.out.println(kh);
                tongKhachHang++ ;
                flag = true ;
            }
        }
        if ( !flag)
            System.out.println("Không tồn tại khách hàng có hạng " + Hang );
        System.out.println("Tổng khách hàng có hạng " + Hang + " là: " + tongKhachHang);
    }
    public void thongKeTheoMucChiTieu( long nguongDuoi , long nguongTren )
    {
        int tongKhachHang = 0 ;
        System.out.println("Danh sách khách hàng có ngưỡng chi tiêu từ " + nguongDuoi + " đến "+ nguongTren + "VNĐ là: ");
        boolean flag = false ;
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( KhachHang kh: list )
        {
            if ( kh != null && kh.isTonTai() && kh.getTongChiTieu() >= nguongDuoi && kh.getTongChiTieu() <= nguongTren )
            {
                System.out.println(kh);
                tongKhachHang++ ;
                flag = true ;
            }
        }
        if (!flag) System.out.println("Không có khách hàng nào có mức tiêu dùng từ " + nguongDuoi + " đến " + nguongTren + "VNĐ");
        System.out.println("Tổng khách hàng có mức chi tiêu từ " + nguongDuoi + " đến " + nguongTren + " VNĐ là: " + tongKhachHang);
    }
    public void thongKeTheoDiemTichluy( long nguongDuoi , long nguongTren )
    {
        int tongKhachHang = 0 ;
        System.out.println("Danh sách khách hàng có ngưỡng điểm tích luỹ từ " + nguongDuoi + " đến "+ nguongTren + " là: ");
        boolean flag = false ;
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( KhachHang kh: list )
        {
            if ( kh != null && kh.isTonTai() && kh.getDiemTichLuy() >= nguongDuoi && kh.getDiemTichLuy() <= nguongTren )
            {
                System.out.println(kh);
                tongKhachHang++ ;
                flag = true ;
            }
        }
        if (!flag) System.out.println("Không có khách hàng nào có mức điểm tích luỹ từ " + nguongDuoi + " đến " + nguongTren + " điểm");
        System.out.println("Tổng khách hàng có điểm tích luỹ từ " + nguongDuoi + " đến " + nguongTren + " là: " + tongKhachHang);
    }
    public void thongKeKhachHang()
    {
        thongKeKhachHang(new Scanner(System.in));
    }
    public void thongKeKhachHang(Scanner sc)
    {
        int luaChon;
        do {
            System.out.println("===================THỐNG KÊ KHÁCH HÀNG===================");
            System.out.println("1. Thống kê theo hạng thành viên!");
            System.out.println("2. Thống kê theo tổng tiền chi tiêu!");
            System.out.println("3. Thống kê theo điểm tích lũy!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 3);
            switch (luaChon)
            {
                case 1:
                    System.out.println("1. Hạng Đồng");
                    System.out.println("2. Hạng Bạc");
                    System.out.println("3. Hạng Vàng");
                    System.out.println("4. Hạng Kim Cương");
                    System.out.println("0. Trở về.");
                    int chonHang = Validation.luaChon(sc, "Chọn hạng thành viên: ", 0, 4);
                    switch (chonHang)
                    {
                        case 1:
                            thongKeKhachHangTheoHang("Đồng");
                            break;
                        case 2:
                            thongKeKhachHangTheoHang("Bạc");
                            break;
                        case 3:
                            thongKeKhachHangTheoHang("Vàng");
                            break;
                        case 4:
                            thongKeKhachHangTheoHang("Kim Cương");
                            break;
                        case 0:
                            break;
                    }
                    break;
                case 2:
                    long tienTu = Validation.inputLong(sc, "Nhập tổng tiền chi tiêu từ (VNĐ): ");
                    long tienDen = Validation.inputLong(sc, "Nhập tổng tiền chi tiêu đến (VNĐ): ");
                    while (tienDen < tienTu)
                    {
                        System.out.println("Ngưỡng trên không được nhỏ hơn ngưỡng dưới!");
                        tienDen = Validation.inputLong(sc, "Nhập lại tổng tiền chi tiêu đến (VNĐ): ");
                    }
                    thongKeTheoMucChiTieu(tienTu, tienDen);
                    break;
                case 3:
                    int diemTu = Validation.inputInt(sc, "Nhập điểm tích lũy từ: ");
                    int diemDen = Validation.inputInt(sc, "Nhập điểm tích lũy đến: ");
                    while (diemDen < diemTu)
                    {
                        System.out.println("Ngưỡng trên không được nhỏ hơn ngưỡng dưới!");
                        diemDen = Validation.inputInt(sc, "Nhập lại điểm tích lũy đến: ");
                    }
                    thongKeTheoDiemTichluy(diemTu, diemDen);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        } while (luaChon != 0);
    }

    /**
     * Ghi đè toàn bộ danh sách khách hàng vào file KhachHang.txt bằng DataBase.ghiVaoFile
     */
    public void ghiFileKhachHang() {
        if (n == 0 || list == null) {
            System.out.println("[!] Danh sach khach hang rong, khong co du lieu de ghi file.");
            return;
        }
        java.util.ArrayList<Object> objs = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) {
            objs.add(list[i]);
        }

        DataBase.ghiVaoFile(objs, utils.DuongDanLuuTru.file("KhachHang.txt"), true);
        System.out.println("[+] Da ghi de danh sach vao file KhachHang.txt bang DataBase thanh cong!");
    }

    public void quanLyDanhSach(Scanner sc)
    {
        int luaChon ;
        do {
            System.out.println("===================QUẢN LÝ KHÁCH HÀNG===================");
            System.out.println("1. Nhập danh sách khách hàng!");
            System.out.println("2. Xuất danh sách khách hàng!");
            System.out.println("3. Thêm khách hàng!");
            System.out.println("4. Sửa khách hàng!");
            System.out.println("5. Xóa khách hàng!");
            System.out.println("6. Tìm kiếm khách hàng!");
            System.out.println("7. Thống kê khách hàng!");
            System.out.println("8. Thống kê số lượng theo hạng!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 8);
            switch (luaChon)
            {
                case 1: {
                    taoDanhSach(sc);
                    break;
                }
                case 2: {
                    xuatDanhSach();
                    break;
                }
                case 3: {
                    them(sc);
                    break;
                }
                case 4: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã khách hàng: ", Validation.MaKH_REGEX, "Mã khách hàng không đúng định dạng!");
                    sua(ma, sc);
                    break;
                }
                case 5: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã khách hàng: ", Validation.MaKH_REGEX, "Mã khách hàng không đúng định dạng!");
                    xoa(ma);
                    break;
                }
                case 6: {
                    timKhoa(sc);
                    break;
                }
                case 7: {
                    thongKeKhachHang(sc);
                    break;
                }
                case 8: {
                    thongKeTheoHang();
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        } while (luaChon != 0);
    }
}
