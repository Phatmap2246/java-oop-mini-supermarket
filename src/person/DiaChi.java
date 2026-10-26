package person; 
import java.util.Scanner;

public class DiaChi {
    // Nếu dự án đã có InputUtil hoặc Main.sc thì nên dùng chung, 
    // còn nếu dùng độc lập thì giữ lại Scanner này:

    private String soNha;
    private String duong;
    private String phuongXa;
    private String quanHuyen;
    private String tinhThanhPho;

    // 1. Constructor mặc định (khởi tạo chuỗi rỗng để tránh lỗi null)
    public DiaChi() {
        this.soNha = "";
        this.duong = "";
        this.phuongXa = "";
        this.quanHuyen = "";
        this.tinhThanhPho = "";
    }

    // 2. Constructor đầy đủ tham số
    public DiaChi(String soNha, String duong, String phuongXa, String quanHuyen, String tinhThanhPho) {
        this.soNha = soNha;
        this.duong = duong;
        this.phuongXa = phuongXa;
        this.quanHuyen = quanHuyen;
        this.tinhThanhPho = tinhThanhPho;
    }

    // 3. Copy Constructor
    public DiaChi(DiaChi dc) {
        if (dc != null) {
            this.soNha = dc.soNha;
            this.duong = dc.duong;
            this.phuongXa = dc.phuongXa;
            this.quanHuyen = dc.quanHuyen;
            this.tinhThanhPho = dc.tinhThanhPho;
        }
    }

    // Getter & Setter
    public String getSoNha() {
        return soNha;
    }

    public void setSoNha(String soNha) {
        this.soNha = soNha;
    }

    public String getDuong() {
        return duong;
    }

    public void setDuong(String duong) {
        this.duong = duong;
    }

    public String getPhuongXa() {
        return phuongXa;
    }

    public void setPhuongXa(String phuongXa) {
        this.phuongXa = phuongXa;
    }

    public String getQuanHuyen() {
        return quanHuyen;
    }

    public void setQuanHuyen(String quanHuyen) {
        this.quanHuyen = quanHuyen;
    }

    public String getTinhThanhPho() {
        return tinhThanhPho;
    }

    public void setTinhThanhPho(String tinhThanhPho) {
        this.tinhThanhPho = tinhThanhPho;
    }

    // Nhập dữ liệu từ bàn phím
    public void nhapDiaChi(Scanner sc) {
        System.out.println("--- Nhập thông tin địa chỉ ---");
        System.out.print("Số nhà: ");
        this.soNha = sc.nextLine().trim();
        System.out.print("Đường: ");
        this.duong = sc.nextLine().trim();
        System.out.print("Phường/Xã: ");
        this.phuongXa = sc.nextLine().trim();
        System.out.print("Quận/Huyện: ");
        this.quanHuyen = sc.nextLine().trim();
        System.out.print("Tỉnh/Thành phố: ");
        this.tinhThanhPho = sc.nextLine().trim();
    }

    // Ghi đè toString giúp in trực tiếp đối tượng: System.out.println(dc);
    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", soNha, duong, phuongXa, quanHuyen, tinhThanhPho);
    }

    public void xuatDiaChi() {
        System.out.println(this.toString());
    }
}