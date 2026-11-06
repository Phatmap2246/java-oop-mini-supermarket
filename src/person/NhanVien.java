package person; 
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Scanner;
import utils.Validation;
import utils.YeuCau;
public  class NhanVien extends Person {
    String maNV; 
    String vaitro; 
    long luong;
    long phuCapTrachNhiem;
    /** Khởi tạo nhân viên với giá trị mặc định. */
    public NhanVien(){
        super(); 
        maNV = null;
        vaitro = null;
        luong = 0;
        phuCapTrachNhiem = 0;
    }
    /** Khởi tạo nhân viên từ các thông tin được truyền vào. */
    public NhanVien(String email,String hoten,String sdt,DiaChi diachi, LocalDate ngsinh,String maNV,String vaitro,long luong,long phuCapTrachNhiem){
        super(email,hoten,sdt,diachi,ngsinh); 
        this.maNV = maNV; 
        this.vaitro = vaitro; 
        this.luong = luong;
        this.phuCapTrachNhiem = phuCapTrachNhiem;
    }
    /** Sao chép thông tin từ một nhân viên khác. */
    public NhanVien(NhanVien nv){
        super(nv);
        maNV = nv.maNV; 
        vaitro = nv.vaitro; 
        luong = nv.luong;
        phuCapTrachNhiem = nv.phuCapTrachNhiem;
    }
    /** Trả về mã nhân viên. */
    public String getMaNV(){
        return maNV; 
    }
    /** Cập nhật mã nhân viên. */
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    /** Nhập mã nhân viên và kiểm tra theo mẫu mã quy định. */
    public void setManv(Scanner sc){
        maNV = Validation.inputStringRegex
        (sc, "Nhap ma nhan vien: ",
        Validation.MaNV_REGEX,
         "Ma nhan vien chua dung dinh dang (NVxxx)!" );

    }
    /** Trả về vai trò của nhân viên. */
    public String getVaiTro(){
        return vaitro;
    }
    /** Cập nhật vai trò của nhân viên. */
    public void setVaiTro(String vt){
        vaitro = vt; 
    }
    /** Nhập và kiểm tra vai trò từ Scanner. */
    public void setVaiTro(Scanner sc){
        vaitro = Validation.inputStringRegex(sc, "Nhap vai tro cho nhan vien:  ", "^QuanLy|KeToan|QuanKho|ThuNgan$", "Nhap sai dinh dang , hay nhap lai ( dinh dang: Carmels)");
    }

    /** Trả về lương cơ bản cộng phụ cấp trách nhiệm. */
    public long getLuong(){
        return luong+ phuCapTrachNhiem;
    }
    /** Cập nhật lương cơ bản. */
    public void setLuong(long luong){
        this.luong = luong;
    }
    /** Nhập lương cơ bản từ bàn phím. */
    public void setLuong(){
        setLuong(new Scanner(System.in));
    }
    /** Nhập và cập nhật lương cơ bản từ Scanner. */
    public void setLuong(Scanner sc){
        setLuong(Validation.inputLong(sc, "Nhap luong: "));
    }
    /** Trả về phụ cấp trách nhiệm. */
    public long getPhuCapTrachNhiem(){
        return phuCapTrachNhiem;
    }
    /** Cập nhật phụ cấp trách nhiệm. */
    public void setPhuCapTrachNhiem(long phuCapTrachNhiem){
        this.phuCapTrachNhiem = phuCapTrachNhiem;
    }
    /** Nhập và cập nhật phụ cấp trách nhiệm từ Scanner. */
    public void setPhuCapTrachNhiem(Scanner sc){
        phuCapTrachNhiem = Validation.inputLong(sc, "Nhap so tien phu cap trach nhiem: ");
    }
    /** Tính tổng lương gồm lương cơ bản và phụ cấp trách nhiệm. */
    public long tinhLuong(){
        return luong + phuCapTrachNhiem;
    }
    /** Nhập thông tin chung và các thông tin riêng của nhân viên. */
    public void nhapThongTin(Scanner sc){
        super.nhapThongTin(sc);
        setManv(sc);
        setLuong(sc);
        setVaiTro(sc);
        setPhuCapTrachNhiem(sc);
    }
    /** Tạo chuỗi biểu diễn thông tin nhân viên để in hoặc hiển thị. */
    public String toString(){
        return super.toString() 
        + " | MaNV: " + maNV + 
        " | Vai tro: " + vaitro +
        " | Luong: " + luong + " | Phu cap trach nhiem: " + phuCapTrachNhiem ;
    }
    public void taoYeuCau(Scanner sc) throws IOException {
        String noiDung;
        do {
            System.out.print("Nhập nội dung yêu cầu (không dùng ký tự |): ");
            noiDung = sc.nextLine().trim();
            if (noiDung.isEmpty() || noiDung.contains("|")) {
                System.out.println("Nội dung không được để trống hoặc chứa ký tự |.");
            }
        } while (noiDung.isEmpty() || noiDung.contains("|"));

        YeuCau yeuCau = new YeuCau(getMaNV(), getVaiTro(), noiDung, "ChoDuyet");
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get("YeuCau.txt"),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(yeuCau.toFileFormat());
            writer.newLine();
        }
        System.out.println("Đã ghi yêu cầu vào YeuCau.txt.");
    }
}
