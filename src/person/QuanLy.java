package person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import dichvu.DanhSachSPKM;
import dichvu.Danhsachdoitac;
import dichvu.Danhsachkhuyenmai;
import dichvu.KhuyenMaiPhanTram;
import dichvu.KhuyenMaiTrucTiep;
import dichvu.Khuyenmai;
import dichvu.Nhacungcap;
import dichvu.SanPhamKhuyenMai;
import sanpham.QuanLySanPham;
import sanpham.SanPham;
import sanpham.ThucPham;
import utils.YeuCau;
import utils.DataBase;
import utils.Validation;

public class QuanLy extends NhanVien {
    private DSNhanVien dsnv = new DSNhanVien();
    private Danhsachkhuyenmai dsKhuyenMai = new Danhsachkhuyenmai();
    private DanhSachSPKM dsSanPhamKhuyenMai = new DanhSachSPKM();

    /** Khởi tạo quản lý với thông tin mặc định. */
    public QuanLy() {
        super();
    }

    /** Khởi tạo quản lý bằng thông tin được truyền vào. */
    public QuanLy(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh,
            String maNV, String vaitro, long luong, long phuCapTrachNhiem) {
        super(email, hoten, sdt, diachi, ngsinh, maNV, vaitro, luong, phuCapTrachNhiem);
    }

    /** Sao chép thông tin nhân viên quản lý từ đối tượng nguồn. */
    public QuanLy(QuanLy ql) {
        super(ql);
    }

    /** Trả về vai trò cố định của lớp là Quản lý. */
    @Override
    public String getVaiTro() {
        return "QuanLy";
    }
    public DSNhanVien getDsNhanVien(){
        return dsnv;
    }

    public void setDSNhanVien( DSNhanVien list)
    {
        dsnv = list; 
    }    public Danhsachkhuyenmai getDanhSachKhuyenMai() {
        return dsKhuyenMai;
    }
    public void setDSKhuyenMai( Danhsachkhuyenmai list)
    {
        dsKhuyenMai = list; 
    }
    public DanhSachSPKM getDanhSachSPKM() {
        return dsSanPhamKhuyenMai;
    }
    public void setDanhSachSPKM( DanhSachSPKM list)
    {
        dsSanPhamKhuyenMai = list; 
    }
    /** Hiển thị menu để thêm, sa thải hoặc sửa nhân viên trong danh sách quản lý. */
    public void quanLyNhanSu(Scanner sc) {
        while (true) {
        System.out.println("\n=== QUẢN LÝ NHÂN SỰ ===");
        System.out.println("1. Thêm nhân viên");
        System.out.println("2. Sa thải nhân viên");
        System.out.println("3. Sửa thông tin nhân viên");
        System.out.println("4. Hoàn tất");
        int c = Validation.inputInt(sc, "Chọn chức năng: ");
            switch (c) {
                case 1:
                    dsnv.themNV(sc);
                    break;
                case 2:
                    String maNV = Validation.inputStringRegex(sc, "Nhập mã nhân viên cần sa thải: ",
                            Validation.MaNV_REGEX, "Mã nhân viên không đúng định dạng (NVxxx).");
                    dsnv.saThai(maNV);
                    break;
                case 3:
                    String maNVSua = Validation.inputStringRegex(sc, "Nhập mã nhân viên cần sửa: ",
                            Validation.MaNV_REGEX, "Mã nhân viên không đúng định dạng (NVxxx).");
                    dsnv.suaNV(maNVSua, sc);
                    break;
                case 4:
                    System.out.println("Đã hoàn tất.");
                    break;
                default:
                    System.out.println("[!] Chọn một chức năng từ 1 đến 4.");
            }
            if (c == 4) {
                break;
            }
        }
    }

    public void getDSNhanVien() {
        // đọc file danh sách nhân viên và lưu và list
    }

    public void phanCaLamViec(DSNhanVien ds, Scanner sc) {
        if (ds.list.isEmpty()) {
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
                    phanCongCa(ds, sc, "6h-14h");
                    break;
                case 2:
                    phanCongCa(ds, sc, "14h-22h");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("[!] Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void phanCongCa(DSNhanVien ds, Scanner sc, String tenCa) {
        int soNhanVienToiDa = 0;
        for (NhanVien nv : ds.list) {
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
                for (NhanVien nv : ds.list) {
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
                for (NhanVien nv : ds.list) {
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
    public void quanLySanPham(Scanner sc,QuanLySanPham dsSanPham,ArrayList<Nhacungcap> dsNCC) {
        while (true) {
            System.out.println("\n=== QUẢN LÝ SẢN PHẨM ===");
            System.out.println("1. Xem danh sách sản phẩm");
            System.out.println("2. Thêm sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("4. Sửa thông tin sản phẩm");
            System.out.println("5. Điều chỉnh giá bán");
            System.out.println("6. Thoát");
            int luaChon = Validation.luaChon(sc, "Chọn chức năng (1-6): ", 1, 6);

            switch (luaChon) {
                case 1:
                    dsSanPham.inDanhSachSanPham();
                    break;
                case 2:
                    dsSanPham.themSanPham(sc,dsNCC);
                    break;
                case 3:
                    String c =Validation.inputStringRegex(sc, "Nhap ma san pham can sua (vi du: SP001)","^SPd+$", "Sai dinh dang ma san pham, nhap lai ");
                    dsSanPham.xoaSanPham(c);
                    break;
                case 4:
                    String d =Validation.inputStringRegex(sc, "Nhap ma san pham can sua (vi du: SP001)","^SPd+$", "Sai dinh dang ma san pham, nhap lai ");
                    dsSanPham.suaThongTinSanPham(sc,d,dsNCC);
                    break;
                case 5:
                    dieuChinhGiaBan(sc,dsSanPham);
                    break;
                case 6:
                    return;
                default:
                    throw new IllegalStateException("Lựa chọn quản lý sản phẩm không hợp lệ.");
            }
        }
    }

    private void dieuChinhGiaBan(Scanner sc,QuanLySanPham dssp){
        SanPham sanPham = dssp.traCuuSanPham(maNV);
        if(sanPham == null){
            return;
        }
        long giaBanMoi = Validation.inputLong(sc, "Nhập giá bán mới: ");
        sanPham.setGiaBan(giaBanMoi);
        System.out.println("Đã cập nhật giá bán sản phẩm " + sanPham.getTenSanPham() + ".");
    }

    public void duyetYeuCau(Scanner sc) throws IOException{
        Path filePath = Paths.get("YeuCau.txt");
        if (!Files.exists(filePath)) {
            System.out.println("Chưa có file YeuCau.txt.");
            return;
        }

        ArrayList<YeuCau.BanGhiDataBase> dsYeuCau = YeuCau.docDanhSach(filePath);
        boolean coThayDoi = false;
        for (int index = 0; index < dsYeuCau.size(); index++) {
            YeuCau.BanGhiDataBase yeuCau = dsYeuCau.get(index);
            if (yeuCau.laDongLoi()) {
                System.err.println("[!] Dòng yêu cầu sai định dạng, giữ nguyên: "
                        + yeuCau.getDongLoi());
                continue;
            }
            if (!yeuCau.getTrangThai().equals("ChoDuyet")) {
                continue;
            }

            System.out.printf("Yêu cầu: %s | %s | %s%n",
                    yeuCau.getMaNV(), yeuCau.getVaitro(), yeuCau.getNoiDung());
            String quyetDinh = Validation.inputStringRegex(
                    sc, "Duyệt: 1 / Không duyệt: 0: ", "^[01]$",
                    "Chỉ được nhập 0 hoặc 1.");
            yeuCau.setTrangThai(quyetDinh.equals("1") ? "DaDuyet" : "KhongDuyet");
            coThayDoi = true;
        }

        if (coThayDoi) {
            YeuCau.ghiDanhSach(filePath, dsYeuCau);
        }
    }
    public void giamGiaCanHSD(Scanner sc, ArrayList<ThucPham> dscanDate) {
        //ràng điều kiện
        if (sc == null || dscanDate == null) {
            throw new IllegalArgumentException("Scanner và danh sách thực phẩm không được null.");
        }
        if (dscanDate.isEmpty()) {
            System.out.println("Không có thực phẩm cận hạn để tạo khuyến mãi.");
            return;
        }

        LocalDate homNay = LocalDate.now();
        for (ThucPham thucPham : dscanDate) {
            if (thucPham == null || thucPham.getHanSuDung() == null
                    || thucPham.getHanSuDung().isBefore(homNay)) {
                continue;
            }
            if (thucPham.getSoLuongTonKho() <= 0) {
                System.out.println("Bỏ qua " + thucPham.getMaSanPham()
                        + " vì số lượng tồn không còn.");
                continue;
            }
            if (dsSanPhamKhuyenMai.timKiemSanPhamKM(thucPham.getMaSanPham()) != -1) {
                System.out.println("Bỏ qua " + thucPham.getMaSanPham()
                        + " vì sản phẩm đã có khuyến mãi.");
                continue;
            }

            System.out.printf("%s - %s | Tồn: %d | HSD: %s%n",
                    thucPham.getMaSanPham(), thucPham.getTenSanPham(),
                    thucPham.getSoLuongTonKho(), thucPham.getHanSuDung());
            int luaChon = Validation.luaChon(
                    sc, "Chọn loại giảm giá (1: phần trăm, 2: trực tiếp): ", 1, 2);
            Khuyenmai khuyenMai = luaChon == 1
                    ? new KhuyenMaiPhanTram()
                    : new KhuyenMaiTrucTiep();

            do {
                khuyenMai.setKM(sc);
                if (khuyenMai.getNgayBatDau().isBefore(homNay)
                        || khuyenMai.getNgayKetThuc().isAfter(thucPham.getHanSuDung())) {
                    System.out.println("[!] Thời gian khuyến mãi phải bắt đầu từ hôm nay "
                            + "và kết thúc không muộn hơn hạn sử dụng.");
                } else {
                    break;
                }
            } while (true);
// thêm sp giảm cận date vào dsSanPhamKhuyenMais
            dsKhuyenMai.themKhuyenMai(khuyenMai);
            dsSanPhamKhuyenMai.themSanPhamKhuyenMai(
                    new SanPhamKhuyenMai(thucPham.getMaSanPham(),
                            khuyenMai.getMaKhuyenMai()));
            System.out.println("Đã gắn khuyến mãi " + khuyenMai.getMaKhuyenMai()
                    + " cho sản phẩm " + thucPham.getMaSanPham() + ".");
        }
    }
    public void themKhuyenMai(Scanner sc,DanhSachSPKM dspkm){
        while(true)
        {
            System.out.println("Tạo khuyến mãi cho nhóm sản phẩm:");
            System.out.println("1. Thực phẩm");
            System.out.println("2. Gia dụng");
            System.out.println("3. Điện tử");
            System.out.println("4. Thoát");
            int luaChon = Validation.luaChon(sc, "Hãy chọn (1-4): ", 1, 4);
            switch (luaChon) {
                case 1:
                    System.out.println("Đã chọn nhóm Thực phẩm.");
                    dspkm.themSanPhamKhuyenMai(sc);
                    break;
                case 2:
                    System.out.println("Đã chọn nhóm Gia dụng.");
                    dspkm.themSanPhamKhuyenMai(sc);
                    break;
                case 3:
                    System.out.println("Đã chọn nhóm Điện tử.");
                    dspkm.themSanPhamKhuyenMai(sc);
                    break;
                case 4:
                    return;
                default:
                    throw new IllegalStateException("Lựa chọn nhóm sản phẩm không hợp lệ.");
            }
        }
    }
    public void xemLichSuDuyet(String file){
       ArrayList<YeuCau> dsDuyet =  DataBase.docfile(YeuCau.class, file);
       System.out.println("LICH SU YEU CAU DUYET");
       for(YeuCau yc: dsDuyet)
       yc.toString();
    }
    // Thống kê các sản phẩm: Số lượng sản phẩm hiện có, số sản phẩm quá hạn/ gần hết hạn, cái nào?
    // Số lượng nhân viên với mỗi chức vụ
    //Số lượng yêu cầu duyệt được tạo, số đã và chưa duyệt
    public void thongKe(Scanner sc, DSNhanVien dsnv, QuanLySanPham dssp, ArrayList<YeuCau> dYeuCaus){
        if (dsnv == null || dsnv.list == null || dssp == null
                || dssp.getDanhSachSanPham() == null || dYeuCaus == null) {
            throw new IllegalArgumentException("Danh sách nhân viên, sản phẩm và yêu cầu không được null.");
        }

        LocalDate homNay = LocalDate.now();
        ArrayList<ThucPham> sanPhamHetHan = new ArrayList<>();
        ArrayList<ThucPham> sanPhamCanHetHan = new ArrayList<>();
        long tongSoLuongTon = 0;
        int soMatHang = 0;

        for (SanPham sanPham : dssp.getDanhSachSanPham()) {
            if (sanPham == null) continue;
            soMatHang++;
            tongSoLuongTon += sanPham.getSoLuongTonKho();
            if (!(sanPham instanceof ThucPham) || sanPham.getSoLuongTonKho() <= 0) {
                continue;
            }
            ThucPham thucPham = (ThucPham) sanPham;
            LocalDate hanSuDung = thucPham.getHanSuDung();
            if (hanSuDung != null) {
                if (hanSuDung.isBefore(homNay)) sanPhamHetHan.add(thucPham);
                else if (!hanSuDung.isAfter(homNay.plusDays(30))) sanPhamCanHetHan.add(thucPham);
            }
        }

        System.out.println("\n=== THỐNG KÊ TÌNH TRẠNG SIÊU THỊ ===");
        System.out.printf("%n--- SẢN PHẨM ---%nSố mặt hàng: %d | Tổng tồn kho: %d%n",
                soMatHang, tongSoLuongTon);
        inThongTinThucPham("Hết hạn", sanPhamHetHan);
        inThongTinThucPham("Sắp hết hạn (30 ngày)", sanPhamCanHetHan);

        Map<String, Integer> nhanVienTheoVaiTro = new HashMap<>();
        int soDaSaThai = 0;
        for (NhanVien nhanVien : dsnv.list) {
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

        Map<String, Integer> yeuCauTheoTrangThai = new HashMap<>();
        for (YeuCau yeuCau : dYeuCaus) {
            if (yeuCau != null)
                yeuCauTheoTrangThai.merge(yeuCau.getTrangThai(), 1, Integer::sum);
        }

        System.out.println("\n--- YÊU CẦU DUYỆT ---");
        System.out.printf("Tổng: %d | Đã duyệt: %d | Chờ duyệt: %d | Không duyệt: %d%n",
                dYeuCaus.size(),
                yeuCauTheoTrangThai.getOrDefault("DaDuyet", 0),
                yeuCauTheoTrangThai.getOrDefault("ChoDuyet", 0),
                yeuCauTheoTrangThai.getOrDefault("KhongDuyet", 0));
    }

    private void inThongTinThucPham(String nhan, ArrayList<ThucPham> danhSach) {
        System.out.printf("%s còn tồn: %d%n", nhan, danhSach.size());
        danhSach.forEach(sp -> System.out.printf("- %s | %s | Tồn: %d | HSD: %s%n",
                sp.getMaSanPham(), sp.getTenSanPham(), sp.getSoLuongTonKho(), sp.getHanSuDung()));
    }
}
