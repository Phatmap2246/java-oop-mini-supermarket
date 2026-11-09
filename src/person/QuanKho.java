package person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import sanpham.DienTu;
import sanpham.QuanLySanPham;
import sanpham.SanPham;
import sanpham.ThucPham;
import sanpham.TieuDung;
import utils.DateUtil;
import utils.Validation;

public class QuanKho extends NhanVien {

    // ========================= CONSTRUCTOR =========================

    /** Khởi tạo quản kho với giá trị mặc định. */
    public QuanKho() {
        super();
        this.vaitro = "QuanKho";
    }

    /** Khởi tạo quản kho từ các thông tin được truyền vào. */
    public QuanKho(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh,
            String maNV, long luong, long phuCapTrachNhiem) {
        super(email, hoten, sdt, diachi, ngsinh, maNV, "QuanKho", luong, phuCapTrachNhiem);
    }

    /** Sao chép thông tin từ một nhân viên quản kho khác. */
    public QuanKho(QuanKho qk) {
        super(qk);
    }

    // ========================= VAI TRÒ =========================

    @Override
    public String getVaiTro() {
        return "QuanKho";
    }

    // ========================= HÀM CHECK HSD (HẠN SỬ DỤNG)
    // =========================

    /**
     * Kiểm tra hạn sử dụng của một sản phẩm được truyền vào.
     * Cảnh báo: Đã quá hạn, Hết hạn hôm nay, Sắp hết hạn (<= 7 ngày hoặc <= 30
     * ngày), Còn hạn sử dụng tốt.
     */
    public void checkHSD(SanPham sp) {
        if (sp == null) {
            System.out.println("[!] Sản phẩm không tồn tại!");
            return;
        }

        if (sp instanceof ThucPham) {
            ThucPham tp = (ThucPham) sp;
            LocalDate hsd = tp.getHanSuDung();
            LocalDate homNay = LocalDate.now();

            if (hsd == null) {
                System.out.printf("[!] Sản phẩm [%s - %s] chưa có thông tin HSD.\n",
                        tp.getMaSanPham(), tp.getTenSanPham());
                return;
            }

            if (hsd.isBefore(homNay)) {
                long soNgayQuaHan = ChronoUnit.DAYS.between(hsd, homNay);
                System.out.printf(
                        "[!] CẢNH BÁO QUÁ HẠN: Sản phẩm [%s - %s] ĐÃ HẾT HẠN từ ngày %s (quá hạn %d ngày). Cần lập yêu cầu xuất hủy!\n",
                        tp.getMaSanPham(), tp.getTenSanPham(), DateUtil.formatDate(hsd), soNgayQuaHan);
            } else if (hsd.isEqual(homNay)) {
                System.out.printf("[!] CẢNH BÁO KHẨN CẤP: Sản phẩm [%s - %s] HẾT HẠN HÔM NAY (%s)!\n",
                        tp.getMaSanPham(), tp.getTenSanPham(), DateUtil.formatDate(hsd));
            } else {
                long soNgayConLai = ChronoUnit.DAYS.between(homNay, hsd);
                if (soNgayConLai <= 7) {
                    System.out.printf(
                            "[*] SẮP HẾT HẠN (KHẨN CẤP): Sản phẩm [%s - %s] chỉ còn %d ngày (HSD: %s). Cần ưu tiên giảm giá/xả hàng!\n",
                            tp.getMaSanPham(), tp.getTenSanPham(), soNgayConLai, DateUtil.formatDate(hsd));
                } else if (soNgayConLai <= 30) {
                    System.out.printf("[*] SẮP HẾT HẠN: Sản phẩm [%s - %s] còn %d ngày (HSD: %s).\n",
                            tp.getMaSanPham(), tp.getTenSanPham(), soNgayConLai, DateUtil.formatDate(hsd));
                } else {
                    System.out.printf("[+] HẠN SỬ DỤNG TỐT: Sản phẩm [%s - %s] còn %d ngày (HSD: %s).\n",
                            tp.getMaSanPham(), tp.getTenSanPham(), soNgayConLai, DateUtil.formatDate(hsd));
                }
            }
        } else if (sp instanceof DienTu) {
            System.out.printf("[i] Sản phẩm điện tử [%s - %s] không áp dụng HSD.\n",
                    sp.getMaSanPham(), sp.getTenSanPham());
        } else if (sp instanceof TieuDung) {
            System.out.printf("[i] Sản phẩm tiêu dùng [%s - %s] không áp dụng hạn sử dụng ngắn ngày.\n",
                    sp.getMaSanPham(), sp.getTenSanPham());
        } else {
            System.out.printf("[i] Sản phẩm [%s - %s] không thuộc danh mục hàng có HSD.\n",
                    sp.getMaSanPham(), sp.getTenSanPham());
        }
    }

    /** Kiểm tra HSD theo mã sản phẩm trong kho. */
    public void checkHSD(String maSP) {
        SanPham sp = timSanPham(maSP);
        if (sp == null) {
            System.out.println("[!] Không tìm thấy sản phẩm có mã " + maSP + " trong kho!");
            return;
        }
        checkHSD(sp);
    }

    /** Kiểm tra HSD bằng cách nhập mã sản phẩm từ Scanner. */
    public void checkHSD(Scanner sc) {
        String maSP = Validation.inputStringRegex(sc, "Nhập mã sản phẩm cần kiểm tra HSD (VD: SP001): ",
                "^SP\\d+$", "Mã sản phẩm không đúng định dạng (VD: SP001)!");
        checkHSD(maSP);
    }

    /** Lấy danh sách các sản phẩm thực phẩm đã quá hạn sử dụng trong kho. */
    public ArrayList<ThucPham> danhSachQuaHan() {
        ArrayList<ThucPham> ketQua = new ArrayList<>();
        LocalDate homNay = LocalDate.now();

        for (SanPham sp : new  QuanLySanPham().getDanhSachSanPham()) {
            if (sp instanceof ThucPham) {
                ThucPham tp = (ThucPham) sp;
                if (tp.getHanSuDung() != null && tp.getHanSuDung().isBefore(homNay)) {
                    ketQua.add(tp);
                }
            }
        }

        System.out.println("\n--- DANH SÁCH SẢN PHẨM QUÁ HẠN SỬ DỤNG ---");
        if (ketQua.isEmpty()) {
            System.out.println("[+] Không có sản phẩm nào quá hạn sử dụng trong kho.");
        } else {
            System.out.printf("%-10s | %-25s | %-8s | %-12s | %-12s\n",
                    "Mã SP", "Tên Sản Phẩm", "SL Tồn", "HSD", "Quá Hạn");
            System.out.println("-------------------------------------------------------------------------");
            for (ThucPham tp : ketQua) {
                long soNgay = ChronoUnit.DAYS.between(tp.getHanSuDung(), homNay);
                System.out.printf("%-10s | %-25s | %-8d | %-12s | %d ngày\n",
                        tp.getMaSanPham(), tp.getTenSanPham(), tp.getSoLuongTonKho(),
                        DateUtil.formatDate(tp.getHanSuDung()), soNgay);
            }
        }
        return ketQua;
    }

    /**
     * Lấy danh sách các sản phẩm thực phẩm sắp hết hạn trong vòng số ngày chỉ định.
     */
    public ArrayList<ThucPham> danhSachSapHetHan(int soNgay) {
        ArrayList<ThucPham> ketQua = new ArrayList<>();
        LocalDate homNay = LocalDate.now();

        for (SanPham sp : new QuanLySanPham().getDanhSachSanPham()) {
            if (sp instanceof ThucPham) {
                ThucPham tp = (ThucPham) sp;
                LocalDate hsd = tp.getHanSuDung();
                if (hsd != null && !hsd.isBefore(homNay)) {
                    long soNgayConLai = ChronoUnit.DAYS.between(homNay, hsd);
                    if (soNgayConLai <= soNgay) {
                        ketQua.add(tp);
                    }
                }
            }
        }

        System.out.println("\n--- DANH SÁCH SẢN PHẨM SẮP HẾT HẠN (<= " + soNgay + " NGÀY) ---");
        if (ketQua.isEmpty()) {
            System.out.println("[+] Không có sản phẩm nào sắp hết hạn trong " + soNgay + " ngày tới.");
        } else {
            System.out.printf("%-10s | %-25s | %-8s | %-12s | %-12s\n",
                    "Mã SP", "Tên Sản Phẩm", "SL Tồn", "HSD", "Còn Lại");
            System.out.println("-------------------------------------------------------------------------");
            for (ThucPham tp : ketQua) {
                long conLai = ChronoUnit.DAYS.between(homNay, tp.getHanSuDung());
                System.out.printf("%-10s | %-25s | %-8d | %-12s | %d ngày\n",
                        tp.getMaSanPham(), tp.getTenSanPham(), tp.getSoLuongTonKho(),
                        DateUtil.formatDate(tp.getHanSuDung()), conLai);
            }
        }
        return ketQua;
    }

    // ========================= CẬP NHẬT KHO=========================

    /**
     * Cập nhật hàng vào kho:
     * - Nếu sản phẩm đã có: cộng thêm số lượng (+ số lượng).
     * - Nếu sản phẩm chưa có: nhập thông tin và thêm sản phẩm mới vào kho.
     */
    public void capNhatKho(Scanner sc) {
        System.out.println("\n--- CẬP NHẬT SẢN PHẨM VÀO KHO ---");
        String maSP = Validation.inputStringRegex(sc,
                "Nhập mã sản phẩm (VD: SP001): ",
                "^SP\\d+$",
                "Mã sản phẩm không đúng định dạng (VD: SP001)!");

        SanPham sp = timSanPham(maSP);

        if (sp != null) {
            // Đã tồn tại trong kho -> cộng thêm số lượng (+ số lượng)
            System.out.println("[+] Tìm thấy sản phẩm trong kho: " + sp.getTenSanPham()
                    + " | Số lượng hiện tại: " + sp.getSoLuongTonKho());
            int slThem = Validation.inputInt(sc, "Nhập số lượng cộng thêm vào kho (+ số lượng): ");
            int slCu = sp.getSoLuongTonKho();
            sp.setSoLuongTonKho(slCu + slThem);
            System.out.println("[+] CẬP NHẬT THÀNH CÔNG: Số lượng sản phẩm [" + sp.getTenSanPham()
                    + "] từ " + slCu + " -> " + sp.getSoLuongTonKho());
        } else {
            // Chưa có trong kho -> nhập thông tin và thêm mới
            System.out.println("[!] Sản phẩm mã " + maSP + " chưa tồn tại trong kho. Tiến hành thêm sản phẩm mới!");

            System.out.print("Nhập tên sản phẩm: ");
            String tenSP = sc.nextLine().trim();

            String maNCC = Validation.inputStringRegex(sc,
                    "Nhập mã nhà cung cấp (VD: NCC01): ",
                    "^NCC\\d+$",
                    "Mã NCC phải bắt đầu bằng NCC theo sau là số!");

            long giaNhap = Validation.inputLong(sc, "Nhập giá nhập (VND): ");
            long giaBan = Validation.inputLong(sc, "Nhập giá bán (VND): ");
            int soLuong = Validation.inputInt(sc, "Nhập số lượng ban đầu: ");

            System.out.println("Chọn phân loại sản phẩm:");
            System.out.println("1. Thực phẩm (có hạn sử dụng)");
            System.out.println("2. Điện tử (có số tháng bảo hành)");
            System.out.println("3. Tiêu dùng");
            int loai = Validation.luaChon(sc, "Chọn loại (1-3): ", 1, 3);

            SanPham spMoi = null;
            switch (loai) {
                case 1:
                    LocalDate hsd = Validation.inputLocalDateRegex(sc, "Nhập hạn sử dụng (dd/MM/yyyy): ");
                    spMoi = new ThucPham(maSP, tenSP, maNCC, giaNhap, giaBan, soLuong, hsd) {
                    };
                    break;
                case 2:
                    int soThangBH = Validation.inputInt(sc, "Nhập số tháng bảo hành: ");
                    spMoi = new DienTu(maSP, tenSP, maNCC, giaNhap, giaBan, soLuong, soThangBH);
                    break;
                case 3:
                default:
                    spMoi = new TieuDung(maSP, tenSP, maNCC, giaNhap, giaBan, soLuong);
                    break;
            }

            new QuanLySanPham().getDanhSachSanPham().add(spMoi);
            System.out.println("[+] THÊM MỚI THÀNH CÔNG: Đã thêm sản phẩm [" + tenSP + "] vào kho!");
        }
    }

    /**
     * Tìm sản phẩm theo mã trong kho(file này là kho đk :
     * new QuanLySanPham().getDanhSachSanPham()).
     */
    public SanPham timSanPham(String maSP) {
        if (maSP == null)
            return null;
        for (SanPham sp : new QuanLySanPham().getDanhSachSanPham()) {
            if (sp.getMaSanPham() != null && sp.getMaSanPham().trim().equalsIgnoreCase(maSP.trim())) {
                return sp;
            }
        }
        return null;
    }

    /** In danh sách các sản phẩm đang có trong kho duy nhất. */
    public void inDanhSachKho() {
        System.out.println("\n=========================== DANH SÁCH SẢN PHẨM TRONG KHO ===========================");
        if (new QuanLySanPham().getDanhSachSanPham().isEmpty()) {
            System.out.println("                                [ Kho hiện đang trống ]");
            System.out.println("=====================================================================================");
            return;
        }

        System.out.printf("%-8s | %-22s | %-10s | %-6s | %-12s | %-12s | %-15s\n",
                "Mã SP", "Tên Sản Phẩm", "Loại", "SL", "Giá Nhập", "Giá Bán", "HSD/Bảo Hành");
        System.out.println("-------------------------------------------------------------------------------------");

        for (SanPham sp : new QuanLySanPham().getDanhSachSanPham()) {
            String thongTinPhu = "-";
            if (sp instanceof ThucPham) {
                LocalDate hsd = ((ThucPham) sp).getHanSuDung();
                thongTinPhu = "HSD: " + (hsd != null ? DateUtil.formatDate(hsd) : "N/A");
            }

            System.out.printf("%-8s | %-22s | %-10s | %-6d | %-12d | %-12d | %-15s\n",
                    sp.getMaSanPham(), sp.getTenSanPham(), sp.getLoaiSanPham(),
                    sp.getSoLuongTonKho(), sp.getGiaNhap(), sp.getGiaBan(), thongTinPhu);
        }
        System.out.println("=====================================================================================");
    }

    /**
     * Nhận maNCC được truyền vào, nhập hình thức và nội dung yêu cầu của Quản kho,
     * sau đó ghi vào file YeuCauDuyet.text để Quản lý duyệt.
     */
    public void yeuCau(String maNCC, Scanner sc) {
        System.out.println("\n--- TẠO YÊU CẦU DUYỆT (QUẢN KHO) ---");
        System.out.println("Mã nhân viên tạo: " + ((getMaNV() != null) ? getMaNV() : "N/A"));
        System.out.println("Mã nhà cung cấp: " + ((maNCC != null && !maNCC.isEmpty()) ? maNCC : "KHONG_CO"));

        System.out.print("Nhập hình thức yêu cầu (VD: Nhập hàng, Đổi trả NCC, Hủy hàng): ");
        String ht = sc.nextLine().trim();

    //     System.out.print("Nhập nội dung chi tiết yêu cầu: ");
    String nd = sc.nextLine().trim();

        ghiYeuCauVaoFile(maNCC, ht, nd);
    }

    // ========================= Các hàm bổ trợ cho các hàm phương thức ở trên
    // =========================

    /** Ghi thông tin yêu cầu của Quản kho vào file YeuCauDuyet.text */
    public void ghiYeuCauVaoFile(String maNCC, String hinhThuc, String noiDung) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter("src/person/YeuCauDuyet.text", true);
            java.io.PrintWriter pw = new java.io.PrintWriter(fw);

            String thoiGian = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String dongGhi = "[" + thoiGian + "] | MaNV: " + (getMaNV() != null ? getMaNV() : "N/A")
                    + " | VaiTro: QuanKho"
                    + " | MaNCC: " + (maNCC != null && !maNCC.isEmpty() ? maNCC : "KHONG_CO")
                    + " | HinhThuc: " + hinhThuc
                    + " | NoiDung: " + noiDung
                    + " | TrangThai: ChoDuyet";

            pw.println(dongGhi);
            pw.close();
            System.out.println("[+] Đã gửi yêu cầu thành công vào file YeuCauDuyet.text!");
        } catch (Exception e) {
            System.out.println("[!] Lỗi ghi file yêu cầu: " + e.getMessage());
        }
    }
}
