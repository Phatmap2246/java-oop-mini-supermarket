package person; 
import java.time.LocalDate;
import utils.DateUtil;
import java.util.Scanner;
import utils.Validation;
public abstract class Person {    
    private String email, hoten, sdt;
    private DiaChi diachi; 
    private LocalDate ngsinh;
    // Constructor --> dùng cho các class con
    public Person(){
         email = null;
         hoten = null;
         sdt = null;
         diachi = new DiaChi(); 
         ngsinh = LocalDate.now();        
    }
    public Person(String email,String hoten,String sdt,DiaChi diachi, LocalDate ngsinh){
         this.email = email;
         this.hoten = hoten;
         this.sdt = sdt;
         this.diachi = diachi; 
         this.ngsinh = ngsinh;
    }
    public Person(Person p){
         email = p.email;
         hoten = p.hoten;
         sdt = p.sdt;
         diachi = p.diachi; 
         ngsinh = p.ngsinh;
    }
    // Các phương thức getter và setter
    public String getEmail(){
        return email; 
    }
    public void setEmail(String newEmail){
        email = newEmail; 
    }
    public void setEmail(Scanner sc){
        email = Validation.inputStringRegex(
                sc,
                "Nhap email: ",
                Validation.EMAIL_REGEX,
                "Email khong hop le, hay nhap lai!"
        );
    }
    public String getHoten(){
        return hoten; 
    }
    public void setHoten(String newhoten){
        hoten = newhoten; 
    }
    public void setHoten(Scanner sc){
        hoten = Validation.inputStringRegex(
                sc,
                "Nhap ho ten: ",
                Validation.HOTEN_REGEX,
                "Ho ten khong hop le, hay nhap lai!"
        );
    }
    public String getSdt(){
        return sdt; 
    }
    public void setSdt(String newSdt){
        sdt = newSdt; 
    }
    public void setSdt(Scanner sc){
        sdt = Validation.inputStringRegex(
                sc,
                "Nhap so dien thoai: ",
                Validation.PHONE_REGEX,
                "So dien thoai khong hop le, hay nhap lai!"
        );
    }
    public String getDiachi(){
        return diachi.toString();
    }
    public void setDiachi(Scanner sc){
        diachi.nhapDiaChi(sc);
    }
    public LocalDate getNgsinh(){
        return ngsinh;
    }
    public void setNgsinh(LocalDate newNgsinh)
    {
        ngsinh = newNgsinh;
    }
    public void setNgsinh(Scanner sc){
        System.out.println("Nhap ngay sinh (DD/MM/YYYY): ");
        String ngsinhStr = sc.nextLine();
        LocalDate ngaySinh = DateUtil.parseDate(ngsinhStr);
        while(ngaySinh == null) {
            System.out.println("Ngay sinh sai dinh dang, hay nhap lai!. ");
            ngsinhStr = sc.nextLine();
            ngaySinh = DateUtil.parseDate(ngsinhStr);
        } 
        ngsinh = ngaySinh;
    }
    public void nhapThongTin(Scanner sc){
        System.out.println("Nhap thong tin cua nguoi: ");
        setHoten(sc); setEmail(sc);setSdt(sc);setNgsinh(sc);setDiachi(sc);
    }
    public void xuatThongTin(){
        System.out.println("Thong tin: ");
        System.out.println(">Ho ten: " + getHoten());
        System.out.println(">Email: " + getEmail());
        System.out.println(">So dien thoai: " + getSdt());
        System.out.println(">Ho ten: " + getHoten());
        System.out.println(">Ngay sinh: " + getNgsinh());
        System.out.println(">Dia chi: " + getDiachi());
    }
    @Override
    public String toString() {
        String ngaySinhStr = ngsinh != null ? ngsinh.format(DateUtil.DATE_FORMATTER) : "N/A";
        return "Họ tên: " + hoten +
                " | SĐT: " + sdt +
                " | Email: " + email +
                " | Địa chỉ: " + diachi +
                " | Ngày sinh: " + ngaySinhStr;
    }
    public abstract String getVaiTro();
    //public abstract boolean coQuyenThucHien(String hanhDong);

}