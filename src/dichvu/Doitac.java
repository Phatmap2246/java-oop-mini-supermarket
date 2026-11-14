package dichvu;
import utils.Validation;

import java.util.Scanner;

public abstract class Doitac {
    // 11/11/2026: Xóa bằng trạng thái để giữ dữ liệu trong danh sách.
    private boolean tonTai = true;

    public boolean isTonTai() { return tonTai; }

    public void setTonTai(boolean tonTai) { this.tonTai = tonTai; }

    private String maDoiTac , tenDoiTac , soDienThoai , diaChi , email , maSoThue , loaiDoiTac;

    public Doitac()
    {
        maDoiTac = tenDoiTac = soDienThoai = diaChi = maSoThue = email = loaiDoiTac = " " ;
    }
    public Doitac (  String maDoiTac , String tenDoiTac , String soDienThoai , String diaChi , String maSoThue , String email , String loaiDoiTac)
    {
        this.maDoiTac = maDoiTac ;
        this.tenDoiTac = tenDoiTac;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.maSoThue = maSoThue;
        this.email = email;
        this.loaiDoiTac = loaiDoiTac;
    }



    public String getLoaiDoiTac() {
        return loaiDoiTac;
    }

    public void setLoaiDoiTac(String loaiDoiTac) {
        if (loaiDoiTac == null || loaiDoiTac.isBlank()) {
            throw new IllegalArgumentException("LoaiDoiTac không được để trống.");
        }
        this.loaiDoiTac = loaiDoiTac;
    }

    public void setSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || !soDienThoai.matches(Validation.PHONE_REGEX)) {
            throw new IllegalArgumentException("SoDienThoai không đúng định dạng.");
        }
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        if (diaChi == null || diaChi.isBlank()) {
            throw new IllegalArgumentException("DiaChi không được để trống.");
        }
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getMaDoiTac() {
        return maDoiTac;
    }

    public String getMaSoThue() {
        return maSoThue;
    }

    public String getTenDoiTac() {
        return tenDoiTac;
    }

    public void setMaDoiTac(String maDoiTac) {
        if (maDoiTac == null || !maDoiTac.matches("(?i)(NCC|DT)\\d+")) {
            throw new IllegalArgumentException("MaDoiTac không đúng định dạng.");
        }
        this.maDoiTac = maDoiTac;
    }

    public void setTenDoiTac(String tenDoiTac) {
        if (tenDoiTac == null || tenDoiTac.isBlank()) {
            throw new IllegalArgumentException("TenDoiTac không được để trống.");
        }
        this.tenDoiTac = tenDoiTac;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches(Validation.EMAIL_REGEX)) {
            throw new IllegalArgumentException("Email không đúng định dạng.");
        }
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setMaSoThue(String maSoThue) {
        if (maSoThue == null || maSoThue.isBlank()) {
            throw new IllegalArgumentException("MaSoThue không được để trống.");
        }
        this.maSoThue = maSoThue;
    }
    public void setDoiTac ( Scanner sc)
    {
        System.out.println("Nhập tên công ty đối tác: ");
        tenDoiTac = Validation.inputStringRegex(sc, "Nhập thông tin: ", "(?s).+", "Thông tin không được để trống!");
        soDienThoai = Validation.inputStringRegex(sc , "Nhập số điện thoại: " , Validation.PHONE_REGEX , "Nhập sai định dạng số điện thoại vui lòng nhập lại(VD:0123456789)!") ;
        System.out.println("Nhập địa chỉ: ");
        diaChi = Validation.inputStringRegex(sc, "Nhập thông tin: ", "(?s).+", "Thông tin không được để trống!");
        System.out.println("Nhập Email: ");
        email = Validation.inputStringRegex(sc , "Nhập Email: " , Validation.EMAIL_REGEX , "Nhập sai định dạng email vui lòng nhập lại!");
        System.out.println("Nhập mã số thuế: ");
        maSoThue = Validation.inputStringRegex(sc, "Nhập thông tin: ", "(?s).+", "Thông tin không được để trống!");
    }
    @Override
    public String toString() {
        return "Mã đối tác: " + maDoiTac + " | Tên đối tác: " + tenDoiTac + " | Số điện thoại: " + soDienThoai + " | Địa chỉ: " + diaChi + " | Email: " + email + " | Mã số thuế: " + maSoThue ;
    }
}
