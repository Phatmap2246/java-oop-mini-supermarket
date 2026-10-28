package dichvu;
import utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Doitac {
    private String maDoiTac , tenDoiTac , soDienThoai , diaChi , email , maSoThue , loaiDoiTac;
    public Doitac()
    {
        maDoiTac = tenDoiTac = soDienThoai = diaChi = maSoThue = email = loaiDoiTac = " " ;
    }
    public Doitac ( String maDoiTac , String tenDoiTac , String soDienThoai , String diaChi , String maSoThue , String email , String loaiDoiTac)
    {
        this.maDoiTac = maDoiTac;
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
        this.loaiDoiTac = loaiDoiTac;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
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
        this.maDoiTac = maDoiTac;
    }

    public void setTenDoiTac(String tenDoiTac) {
        this.tenDoiTac = tenDoiTac;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setMaSoThue(String maSoThue) {
        this.maSoThue = maSoThue;
    }
    public void setDoiTac ( Scanner sc)
    {
        maDoiTac = Validation.inputStringRegex(sc , "Nhập mã đối tác: " , "DT\\d{3}" , "Mã đối tác nhập sai định dạng vui lòng nhập lại(VD:DT00X)!") ;
        System.out.println("Nhập tên công ty đối tác: ");
        tenDoiTac = sc.nextLine() ;
        soDienThoai = Validation.inputStringRegex(sc , "Nhập số điện thoại: " , Validation.phoneRegex , "Nhập sai định dạng số điện thoại vui lòng nhập lại(VD:0123456789)!") ;
        System.out.println("Nhập địa chỉ: ");
        diaChi = sc.nextLine() ;
        System.out.println("Nhập Email: ");
        email = Validation.inputStringRegex(sc , "Nhập Email: " , Validation.emailRegex , "Nhập sai định dạng email vui lòng nhập lại!");
        System.out.println("Nhập mã số thuế: ");
        maSoThue = sc.nextLine() ;
    }
    @Override
    public String toString() {
        return "Mã đối tác: " + maDoiTac + " | Tên đối tác: " + tenDoiTac + " | Số điện thoại: " + soDienThoai + " | Địa chỉ: " + diaChi + " | Email: " + email + " | Mã số thuế: " + maSoThue ;
    }
}
