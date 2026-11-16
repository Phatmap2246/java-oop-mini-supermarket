package person;

import utils.DongDuLieu;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

import utils.Validation;
import utils.YeuCau;

public class NhanVien extends Person {
    private String maNV;
    private long luong;
    private long phuCapTrachNhiem;
    private boolean isSaThai;
    private static int soThuTu = 1 ;
    /** Khởi tạo nhân viên với giá trị mặc định. */
    private static void capNhatSoThuTuTuMa(String ma) {
        if (ma != null && !ma.isEmpty()) {
            soThuTu = Math.max(soThuTu, DongDuLieu.maKeTiep(ma, "(?i)NV\\d+"));
        }
    }

    protected NhanVien(String vaitroMacDinh) {
        this();
        this.vaitro = vaitroMacDinh;
    }

    public NhanVien() {
        super();
        maNV = Validation.taoMa("NV%03d" , soThuTu) ;
        vaitro = null;
        luong = 0;
        phuCapTrachNhiem = 0;
        isSaThai = false;
        soThuTu++ ;
    }
    /** Khởi tạo nhân viên từ các thông tin được truyền vào. */
    public NhanVien(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh,
            String maNV, String vaitro, long luong, long phuCapTrachNhiem) {
        super(email, hoten, sdt, diachi, ngsinh);
        if (luong < 0) throw new IllegalArgumentException("Luong không được âm.");
        if (phuCapTrachNhiem < 0) throw new IllegalArgumentException("PhuCapTrachNhiem không được âm.");
        this.maNV = maNV;
        this.vaitro = vaitro;
        this.luong = luong;
        this.phuCapTrachNhiem = phuCapTrachNhiem;
        capNhatSoThuTuTuMa(maNV);
    }
    /** Khởi tạo nhân viên từ thông tin được truyền vào, bao gồm trạng thái sa thải. */
    public NhanVien(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh,
            String maNV, String vaitro, long luong, long phuCapTrachNhiem, boolean isSaThai) {
        this(email, hoten, sdt, diachi, ngsinh, maNV, vaitro, luong, phuCapTrachNhiem);
        this.isSaThai = isSaThai;
    }
    /** Sao chép thông tin từ một nhân viên khác. */
    public NhanVien(NhanVien nv) {
        super(nv);
        maNV = nv.maNV;
        vaitro = nv.vaitro;
        luong = nv.luong;
        phuCapTrachNhiem = nv.phuCapTrachNhiem;
        isSaThai = nv.isSaThai;
    }

    public static int getSoThuTu() {
        return soThuTu;
    }

    /** Trả về mã nhân viên. */
    public String getMaNV() {
        return maNV;
    }

    /** Cập nhật mã nhân viên. */
    public void setMaNV(String maNV) {
        if (maNV == null || !maNV.matches(Validation.MaNV_REGEX)) {
            throw new IllegalArgumentException("MaNV không đúng định dạng.");
        }
        capNhatSoThuTuTuMa(maNV);
        this.maNV = maNV;
    }

    /** Nhập mã nhân viên và kiểm tra theo mẫu mã quy định. */
    public void setManv(Scanner sc) {
        setMaNV(Validation.inputStringRegex(
                sc,
                "Nhập mã nhân viên: ",
                Validation.MaNV_REGEX,
                "Mã nhân viên không đúng định dạng (NVxxx)."
        ));
    }

    /** Trả về vai trò của nhân viên. */
    public String getVaiTro() {
        return vaitro;
    }

    /** Cập nhật vai trò của nhân viên. */
    public void setVaiTro(String vt) {
        if (vt == null ) {
            throw new IllegalArgumentException("VaiTro không đúng định dạng.");
        }
        vaitro = vt;
    }

    /** Nhập và kiểm tra vai trò từ Scanner. */
    public void setVaiTro(Scanner sc) {
        vaitro = Validation.inputStringRegex(
                sc,
                "Nhập vai trò nhân viên: ",
                "^(QuanLy|KeToan|QuanKho|ThuNgan)$",
                "Vai trò không hợp lệ."
        );
    }

    /** Trả về lương cơ bản cộng phụ cấp trách nhiệm. */
    public long getLuong() {
        return luong + phuCapTrachNhiem;
    }

    /** Cập nhật lương cơ bản. */
    public void setLuong(long luong) {
        if (luong < 0) {
            throw new IllegalArgumentException("Luong không được âm.");
        }
        this.luong = luong;
    }

    /** Nhập lương cơ bản từ bàn phím. */
    public void setLuong() {
        if (luong < 0) {
            throw new IllegalArgumentException("Luong không được âm.");
        }
        setLuong(new Scanner(System.in));
    }

    /** Nhập và cập nhật lương cơ bản từ Scanner. */
    public void setLuong(Scanner sc) {
        setLuong(Validation.inputLong(sc, "Nhập lương: "));
    }

    /** Trả về phụ cấp trách nhiệm. */
    public long getPhuCapTrachNhiem() {
        return phuCapTrachNhiem;
    }

    /** Cập nhật phụ cấp trách nhiệm. */
    public void setPhuCapTrachNhiem(long phuCapTrachNhiem) {
        if (phuCapTrachNhiem < 0) {
            throw new IllegalArgumentException("PhuCapTrachNhiem không được âm.");
        }
        this.phuCapTrachNhiem = phuCapTrachNhiem;
    }

    /** Nhập và cập nhật phụ cấp trách nhiệm từ Scanner. */
    public void setPhuCapTrachNhiem(Scanner sc) {
        phuCapTrachNhiem = Validation.inputLong(sc, "Nhập phụ cấp trách nhiệm: ");
    }

    /** Trả về trạng thái sa thải của nhân viên. */
    public boolean isSaThai() {
        return isSaThai;
    }

    /** Cập nhật trạng thái sa thải của nhân viên. */
    public void setSaThai(boolean isSaThai) {
        this.isSaThai = isSaThai;
    }

    /** Tính tổng lương gồm lương cơ bản và phụ cấp trách nhiệm. */
    public long tinhLuong() {
        return luong + phuCapTrachNhiem;
    }

    /** Nhập thông tin chung và các thông tin riêng của nhân viên. */
    public void nhapThongTin(Scanner sc) {
        super.nhapThongTin(sc);
        setLuong(sc);
        setVaiTro(sc);
        setPhuCapTrachNhiem(sc);
    }

    /** Tạo chuỗi biểu diễn thông tin nhân viên để in hoặc hiển thị. */
    @Override
    public String toString() {
        return utils.HienThi.canLe(super.toString()
                + " | Mã NV: " + maNV
                + " | Vai trò: " + vaitro
                + " | Lương: " + luong
                + " | Phụ cấp trách nhiệm: " + phuCapTrachNhiem
                + " | Trạng thái: " + (isSaThai ? "Đã sa thải" : "Đang làm"));
    }

    public void taoYeuCau(Scanner sc) throws IOException {
        String noiDung;
        do {
            System.out.print("Nhập nội dung yêu cầu: ");
            noiDung = sc.nextLine().trim();
            if (noiDung.isEmpty()) {
                System.out.println("Nội dung không được để trống.");
            }
        } while (noiDung.isEmpty());

        YeuCau yeuCau = new YeuCau(getMaNV(), getVaiTro(), noiDung, "ChoDuyet");
        YeuCau.themVaoFile(yeuCau, Paths.get(utils.DuongDanLuuTru.file("YeuCau.txt")));
        System.out.println("Đã ghi yêu cầu vào YeuCau.txt.");
    }

    // ================= ĐỌC / GHI FILE TXT =================

    protected String thongTinNhanVienFile() {
        return DongDuLieu.ghep(maNV, vaitro, luong, phuCapTrachNhiem, isSaThai) + ";" + thongTinNguoiFile();
    }

    protected void docThongTinNhanVienFile(String[] p) {
        maNV = p[1];
        vaitro = p[2];
        luong = Long.parseLong(p[3]);
        phuCapTrachNhiem = Long.parseLong(p[4]);
        isSaThai = DongDuLieu.logic(p[5]);
        docThongTinNguoiFile(p, 6);
    }

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "NHANVIEN;" + thongTinNhanVienFile();
    }

    public static NhanVien tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        if ("QUANLY".equals(p[0])) return QuanLy.tuDongFile(dong);
        if ("QUANKHO".equals(p[0])) return QuanKho.tuDongFile(dong);
        if ("KETOAN".equals(p[0])) return KeToan.tuDongFile(dong);
        if ("THUNGAN".equals(p[0])) return ThuNgan.tuDongFile(dong);
        DongDuLieu.yeuCau(p.length == 15 && "NHANVIEN".equals(p[0]), "Sai dòng NHANVIEN (cần 15 cột).");
        int dem = NhanVien.getSoThuTu();
        NhanVien obj = new NhanVien();
        NhanVien.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinNhanVienFile(p);
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.khongAm(luong, "luong");
        DongDuLieu.khongAm(phuCapTrachNhiem, "phuCapTrachNhiem");
        DongDuLieu.maKeTiep(maNV, "(?i)NV\\d+");
    }

    public static void datBoDemFile(int value) {
        DongDuLieu.yeuCau(value > 0, "Bộ đếm mã phải dương.");
        soThuTu = value;
    }

    public void capNhatMaFile() {
        soThuTu = Math.max(soThuTu, DongDuLieu.maKeTiep(maNV, "(?i)NV\\d+"));
    }
}
