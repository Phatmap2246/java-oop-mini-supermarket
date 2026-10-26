package person; 
import java.time.LocalDate;
import java.util.Scanner;
import utils.Validation;
public class NhanVien extends Person {
    String maNV; 
    String vaitro; 
    public NhanVien(){
        super(); 
        maNV = null;
        vaitro = null;
    }
    public NhanVien(String email,String hoten,String sdt,DiaChi diachi, LocalDate ngsinh,String maNV,String vaitro){
        super(email,hoten,sdt,diachi,ngsinh); 
        this.maNV = maNV; 
        this.vaitro = vaitro; 
    }
    public NhanVien(NhanVien nv){
        super(nv);
        maNV = nv.maNV; 
        vaitro = nv.vaitro; 
    }
    public String getMaNV(){
        return maNV; 
    }
    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    public void setManv(Scanner sc){
        maNV = Validation.inputStringRegex
        (sc, "Nhap ma nhan vien: ",
         "^NV[0-9]+d{3}$",
         "Ma nhan vien chua dung dinh dang (NVxxx)!" );

    }
    public String getVaiTro(){
        return vaitro;
    }

}
