package person;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import hoadon.CTHoaDonBan;
import sanpham.DienTu;
import sanpham.QuanLySanPham;
import sanpham.SanPham;
import sanpham.ThucPham;
import sanpham.TieuDung;
import utils.DateUtil;
import utils.Validation;
import utils.YeuCau;

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

        for (SanPham sp : new QuanLySanPham().getDanhSachSanPham()) {
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
                    spMoi = new ThucPham(maSP, tenSP, "ThucPham", giaNhap, giaBan,
                            soLuong, maNCC, 0.08, LocalDate.now(), hsd, 0.0, "");
                    break;
                case 2:
                    int soThangBH = Validation.inputInt(sc, "Nhập số tháng bảo hành: ");
                    spMoi = new DienTu(maSP, tenSP, "DienTu", giaNhap, giaBan,
                            soLuong, maNCC, 0.05, soThangBH, 0, "", LocalDate.now());
                    break;
                case 3:
                default:
                    spMoi = new TieuDung(maSP, tenSP, "TieuDung", giaNhap, giaBan,
                            soLuong, maNCC, 0.05, "", "", "", "");
                    break;
            }

            new QuanLySanPham().getDanhSachSanPham().add(spMoi);
            System.out.println("[+] THÊM MỚI THÀNH CÔNG: Đã thêm sản phẩm [" + tenSP + "] vào kho!");
        }
    }

    /**
     * nhập hàng vào kho:
     * Cho phép Quản kho nhập danh sách các mặt hàng (mã SP, tên, số lượng nhập, đơn
     * giá nhập),
     * cập nhật số lượng tồn trong kho và TRẢ VỀ danh sách các chi tiết nhập
     * (ArrayList<CTHoaDon>)
     * để chuyển giao cho bên Đối tác (Nhà cung cấp ).
     */
    public ArrayList<CTHoaDonBan> nhapHangvaokho(Scanner sc) {
        System.out.println("\n=======================================================");
        System.out.println("                     NHẬP HÀNG VÀO KHO                  ");
        System.out.println("=======================================================");

        ArrayList<CTHoaDonBan> danhSachNhap = new ArrayList<>();

        while (true) {
            String maSP = Validation.inputStringRegex(sc,
                    "Nhập mã sản phẩm cần nhập (VD: SP001): ",
                    "^SP\\d+$",
                    "Mã sản phẩm không đúng định dạng (VD: SP001)!");

            SanPham sp = timSanPham(maSP);
            String tenSP;
            long donGiaNhap;

            if (sp != null) {
                tenSP = sp.getTenSanPham();
                donGiaNhap = sp.getGiaNhap();
                System.out.println("[+] Sản phẩm có sẵn: " + tenSP + " | Tồn kho hiện tại: " + sp.getSoLuongTonKho()
                        + " | Giá nhập chuẩn: " + donGiaNhap + " VND");
            } else {
                System.out.println("[!] Sản phẩm chưa có trong kho, vui lòng nhập thông tin ban đầu:");
                System.out.print("Nhập tên sản phẩm: ");
                tenSP = sc.nextLine().trim();
                donGiaNhap = Validation.inputLong(sc, "Nhập giá nhập (VND): ");
            }

            int soLuongNhap = Validation.inputInt(sc, "Nhập số lượng muốn nhập: ");

            // Cập nhật tồn kho nếu sản phẩm đã có trong kho
            if (sp != null) {
                sp.setSoLuongTonKho(sp.getSoLuongTonKho() + soLuongNhap);
            }

            // Tạo chi tiết nhập (CTHoaDon: maSP, tenSP, soLuong, donGia, chietKhau = 0)
            CTHoaDonBan cthd = new CTHoaDonBan(maSP, tenSP, soLuongNhap, donGiaNhap, 0.0);
            danhSachNhap.add(cthd);

            System.out.printf("[+] Đã thêm vào danh sách nhập: [%s - %s] x %d cái.\n", maSP, tenSP, soLuongNhap);

            int tiepTuc = Validation.inputInt(sc, "Bạn có muốn nhập tiếp sản phẩm khác không? (1: Có / 0: Không): ");
            if (tiepTuc == 0) {
                break;
            }
        }

        System.out.println("\n[+] ĐÃ HOÀN TẤT TÙY CHỌN NHẬP HÀNG! Tổng cộng: " + danhSachNhap.size() + " mặt hàng.");
        return danhSachNhap;
    }

    /**
     * Tùy chọn nhập hàng từ danh sách sản phẩm có rồi
     */
    public ArrayList<CTHoaDonBan> nhapHangTuDanhSach(ArrayList<CTHoaDonBan> danhSachNhap) {
        if (danhSachNhap == null || danhSachNhap.isEmpty()) {
            System.out.println("[!] Danh sách nhập rỗng, không thể cập nhật kho.");
            return new ArrayList<>();
        }

        for (CTHoaDonBan ct : danhSachNhap) {
            SanPham sp = timSanPham(ct.getMaSanPham());
            if (sp != null) {
                sp.setSoLuongTonKho(sp.getSoLuongTonKho() + ct.getSoLuongTonKho());
            }
        }
        System.out.println("[+] Đã cập nhật xong " + danhSachNhap.size() + " mặt hàng vào kho.");
        return danhSachNhap;
    }

    /**
     * Tìm sản phẩm theo mã trong kho (new QuanLySanPham().getDanhSachSanPham()).
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

    /** In danh sách các sản phẩm đang có trong kho */
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

        System.out.print("Nhập nội dung chi tiết yêu cầu: ");
        String nd = sc.nextLine().trim();

        ghiYeuCauVaoFile(maNCC, ht, nd);
    }

    // ========================= NGHIỆP VỤ NHẬP PHIẾU NHẬP KHO
    // =========================

    /**
     * Nghiệp vụ nhập phiếu nhập kho:
     * Khi Nhà Cung Cấp giao hàng tới, Quản kho nhập trên máy:
     * - Hàng này của NCC nào (Mã NCC)
     * - Số lượng bao nhiêu
     * - Giá tiền hàng (đơn giá x số lượng)
     * - Tiền vận chuyển
     * - Tính thành tiền
     * Sau đó cập nhật số lượng vào kho và lưu vào file "LichSuNhapHang.txt" bằng
     * hàm ghiFile.
     */
    public void nhapPhieuNhapKho(Scanner sc) {
        System.out.println("\n=======================================================");
        System.out.println("                 NHẬP PHIẾU NHẬP KHO                   ");
        System.out.println("=======================================================");

        String maPhieu = Validation.taoMa("PNK%03d", (int) (System.currentTimeMillis() % 1000));
        System.out.println("Mã phiếu nhập: " + maPhieu);
        System.out.println("Quản kho thực hiện: " + (getHoten() != null ? getHoten() : "N/A")
                + " (Mã NV: " + (getMaNV() != null ? getMaNV() : "N/A") + ")");

        String maNCC = Validation.inputStringRegex(sc,
                "Nhập mã nhà cung cấp giao hàng (VD: NCC01): ",
                "^NCC\\d+$",
                "Mã NCC phải bắt đầu bằng NCC theo sau là số!");

        String maSP = Validation.inputStringRegex(sc,
                "Nhập mã sản phẩm giao tới (VD: SP001): ",
                "^SP\\d+$",
                "Mã sản phẩm không đúng định dạng (VD: SP001)!");

        SanPham sp = timSanPham(maSP);
        String tenSP;
        long donGia;

        if (sp != null) {
            tenSP = sp.getTenSanPham();
            donGia = sp.getGiaNhap();
            System.out.println("[+] Tìm thấy sản phẩm: " + tenSP + " | Tồn kho hiện tại: " + sp.getSoLuongTonKho());
        } else {
            System.out.print("Sản phẩm mới - Nhập tên sản phẩm: ");
            tenSP = sc.nextLine().trim();
            donGia = Validation.inputLong(sc, "Nhập đơn giá hàng (VND): ");
        }

        int soLuong = Validation.inputInt(sc, "Nhập số lượng hàng giao tới: ");
        long tienHang = donGia * soLuong;
        System.out.println("[i] Giá tiền hàng (SL x Đơn giá): " + String.format("%,d", tienHang) + " VND");

        long tienVanChuyen = Validation.inputLong(sc, "Nhập tiền vận chuyển (VND): ");
        long thanhTien = tienHang + tienVanChuyen;

        System.out.println("-------------------------------------------------------");
        System.out.printf("TỔNG THÀNH TIỀN PHIẾU NHẬP: %,d VND\n", thanhTien);
        System.out.println("-------------------------------------------------------");

        // Cập nhật số lượng vào kho
        if (sp != null) {
            sp.setSoLuongTonKho(sp.getSoLuongTonKho() + soLuong);
        }

        // Lưu vào file LichSuNhapHang.txt bằng hàm ghi file
        ghiFileLichSuNhap(maPhieu, maNCC, maSP, tenSP, soLuong, donGia, tienHang, tienVanChuyen, thanhTien);
    }

    // ========================= Các hàm bổ trợ cho các hàm phương thức ở
    // trên=========
    /**
     * Ghi phiếu nhập kho vào file LichSuNhapHang.txt bằng FileWriter
     */
    public void ghiFileLichSuNhap(String maPhieu, String maNCC, String maSP, String tenSP,
            int soLuong, long donGia, long tienHang, long tienVanChuyen, long thanhTien) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter("src/person/LichSuNhapHang.txt", true);
            java.io.PrintWriter pw = new java.io.PrintWriter(fw);

            String thoiGian = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String dongGhi = "[" + thoiGian + "] | MaPhieu: " + maPhieu
                    + " | MaNV: " + (getMaNV() != null ? getMaNV() : "N/A")
                    + " | MaNCC: " + maNCC
                    + " | MaSP: " + maSP
                    + " | TenSP: " + tenSP
                    + " | SL: " + soLuong
                    + " | DonGia: " + donGia
                    + " | TienHang: " + tienHang
                    + " | TienVanChuyen: " + tienVanChuyen
                    + " | ThanhTien: " + thanhTien;

            pw.println(dongGhi);
            pw.close();
            System.out.println("[+] Đã lưu phiếu nhập vào file LichSuNhapHang.txt thành công!");
        } catch (Exception e) {
            System.out.println("[!] Lỗi khi ghi file lịch sử nhập hàng: " + e.getMessage());
        }
    }

    /**
     * Ghi thông tin yêu cầu của Quản kho vào YeuCau.txt bằng YeuCau.themVaoFile (đồng bộ hệ thống)
     */
    public void ghiYeuCauVaoFile(String maNCC, String hinhThuc, String noiDung) {
        String chiTietYeuCau = "Hình thức: " + hinhThuc
                + " | Mã NCC: " + (maNCC != null && !maNCC.isEmpty() ? maNCC : "KHONG_CO")
                + " | Nội dung: " + noiDung;

        YeuCau yc = new YeuCau((getMaNV() != null ? getMaNV() : "N/A"), getVaiTro(), chiTietYeuCau, "ChoDuyet");
        try {
            YeuCau.themVaoFile(yc, Paths.get("YeuCau.txt"));
            System.out.println("[+] Đã thêm yêu cầu vào YeuCau.txt thành công!");
        } catch (Exception e) {
            System.out.println("[!] Lỗi khi ghi yêu cầu: " + e.getMessage());
        }
    }
}
