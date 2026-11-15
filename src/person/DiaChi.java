package person;

import java.util.Scanner;

import interfaces.TrongFile;

public class DiaChi implements TrongFile {
    private String soNha;
    private String duong;
    private String phuongXa;
    private String quanHuyen;
    private String tinhThanhPho;

    /** Khởi tạo địa chỉ với các thành phần là chuỗi rỗng. */
    public DiaChi() {
        this.soNha = "";
        this.duong = "";
        this.phuongXa = "";
        this.quanHuyen = "";
        this.tinhThanhPho = "";
    }

    /** Khởi tạo địa chỉ với năm thành phần được truyền vào. */
    public DiaChi(String soNha, String duong, String phuongXa, String quanHuyen, String tinhThanhPho) {
        this.soNha = soNha;
        this.duong = duong;
        this.phuongXa = phuongXa;
        this.quanHuyen = quanHuyen;
        this.tinhThanhPho = tinhThanhPho;
    }

    /** Sao chép các thành phần từ địa chỉ nguồn nếu nguồn khác null. */
    public DiaChi(DiaChi dc) {
        if (dc != null) {
            this.soNha = dc.soNha;
            this.duong = dc.duong;
            this.phuongXa = dc.phuongXa;
            this.quanHuyen = dc.quanHuyen;
            this.tinhThanhPho = dc.tinhThanhPho;
        }
    }

    /** Trả về số nhà. */
    public String getSoNha() {
        return soNha;
    }

    /** Cập nhật số nhà. */
    public void setSoNha(String soNha) {
        if (soNha == null || soNha.isBlank()) {
            throw new IllegalArgumentException("SoNha không được để trống.");
        }
        this.soNha = soNha;
    }

    /** Trả về tên đường. */
    public String getDuong() {
        return duong;
    }

    /** Cập nhật tên đường. */
    public void setDuong(String duong) {
        if (duong == null || duong.isBlank()) {
            throw new IllegalArgumentException("Duong không được để trống.");
        }
        this.duong = duong;
    }

    /** Trả về phường hoặc xã. */
    public String getPhuongXa() {
        return phuongXa;
    }

    /** Cập nhật phường hoặc xã. */
    public void setPhuongXa(String phuongXa) {
        if (phuongXa == null || phuongXa.isBlank()) {
            throw new IllegalArgumentException("PhuongXa không được để trống.");
        }
        this.phuongXa = phuongXa;
    }

    /** Trả về quận hoặc huyện. */
    public String getQuanHuyen() {
        return quanHuyen;
    }

    /** Cập nhật quận hoặc huyện. */
    public void setQuanHuyen(String quanHuyen) {
        if (quanHuyen == null || quanHuyen.isBlank()) {
            throw new IllegalArgumentException("QuanHuyen không được để trống.");
        }
        this.quanHuyen = quanHuyen;
    }

    /** Trả về tỉnh hoặc thành phố. */
    public String getTinhThanhPho() {
        return tinhThanhPho;
    }

    /** Cập nhật tỉnh hoặc thành phố. */
    public void setTinhThanhPho(String tinhThanhPho) {
        if (tinhThanhPho == null || tinhThanhPho.isBlank()) {
            throw new IllegalArgumentException("TinhThanhPho không được để trống.");
        }
        this.tinhThanhPho = tinhThanhPho;
    }

    /** Nhập năm thành phần địa chỉ từ Scanner. */
    public void nhapDiaChi(Scanner sc) {
        System.out.println("\n=== NHẬP ĐỊA CHỈ ===");
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

    /** Ghép các thành phần địa chỉ thành chuỗi phân cách bằng dấu phẩy. */
    @Override
    public String toString() {
        return String.join(", ", soNha, duong, phuongXa, quanHuyen, tinhThanhPho);
    }
    @Override
    public String getFilePath(){
        return utils.DuongDanLuuTru.file("DiaChi.csv");
    }
    /** In địa chỉ ra console. */
    public void xuatDiaChi() {
        System.out.println("Địa chỉ: " + this);
    }

}
