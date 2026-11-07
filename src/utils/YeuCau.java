package utils;

import java.util.Scanner;

public class YeuCau {
    private String maNV;
    private String vaitro;
    private String noiDung;
    private String trangThai;

    // Constructor
    public YeuCau(String maNV, String vaitro, String noiDung, String trangThai) {
        this.maNV = maNV;
        this.vaitro = vaitro;
        this.noiDung = noiDung;
        this.trangThai = trangThai;
    }

    // Phương thức gộp dữ liệu thành chuỗi để ghi ra file txt
    public String toFileFormat() {
        return maNV + "|" + vaitro + "|" + noiDung + "|" + trangThai;
    }

    public static YeuCau taoYeuCau(Scanner sc) {
        System.out.print("Nhập mã nhân viên: ");
        String maNV = sc.nextLine().trim();

        System.out.print("Nhập vai trò: ");
        String vaitro = sc.nextLine().trim();

        System.out.print("Nhập nội dung yêu cầu: ");
        String noiDung = sc.nextLine().trim();

        return new YeuCau(maNV, vaitro, noiDung, "ChoDuyet");
    }

    // Các Getter/Setter, toString() (bạn tự thêm)
}