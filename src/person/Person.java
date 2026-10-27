package person; 
import java.time.LocalDate;
import utils.DateUtil;
import java.util.Scanner;
import utils.Validation;
public abstract class Person {    
    private String email, hoten, sdt;
    private DiaChi diachi; 
    private LocalDate ngsinh;
    /** Khởi tạo người với thông tin mặc định và địa chỉ rỗng. */
    public Person(){
         email = null;
         hoten = null;
         sdt = null;
         diachi = new DiaChi(); 
         ngsinh = LocalDate.now();        
    }
    /** Khởi tạo người từ các thông tin được truyền vào. */
    public Person(String email,String hoten,String sdt,DiaChi diachi, LocalDate ngsinh){
         this.email = email;
         this.hoten = hoten;
         this.sdt = sdt;
         this.diachi = diachi; 
         this.ngsinh = ngsinh;
    }
    /** Sao chép thông tin từ một đối tượng Person khác. */
    public Person(Person p){
         email = p.email;
         hoten = p.hoten;
         sdt = p.sdt;
         diachi = p.diachi; 
         ngsinh = p.ngsinh;
    }
    /** Trả về email của người. */
    public String getEmail(){
        return email; 
    }
    /** Cập nhật email nếu email hiện tại khớp định dạng quy định. */
    public void setEmail(String newEmail){
        if(email != null && email.matches(Validation.EMAIL_REGEX))
        email = newEmail; 
    }
    /** Nhập và kiểm tra email từ Scanner. */
    public void setEmail(Scanner sc){
        email = Validation.inputStringRegex(
                sc,
                "Nhap email: ",
                Validation.EMAIL_REGEX,
                "Email khong hop le, hay nhap lai!"
        );
    }
    /** Trả về họ tên của người. */
    public String getHoten(){
        return hoten; 
    }
    /** Cập nhật họ tên nếu họ tên hiện tại khớp định dạng quy định. */
    public void setHoten(String newhoten){
        if(hoten != null && hoten.matches(Validation.HOTEN_REGEX))
        hoten = newhoten; 
    }
    /** Nhập và kiểm tra họ tên từ Scanner. */
    public void setHoten(Scanner sc){
        hoten = Validation.inputStringRegex(
                sc,
                "Nhap ho ten: ",
                Validation.HOTEN_REGEX,
                "Ho ten khong hop le, hay nhap lai!"
        );
    }
    /** Trả về số điện thoại của người. */
    public String getSdt(){
        return sdt; 
    }
    /** Cập nhật số điện thoại nếu giá trị hiện tại khớp định dạng quy định. */
    public void setSdt(String newSdt){
        if(sdt != null && sdt.matches(Validation.PHONE_REGEX))
        sdt = newSdt; 
    }
    /** Nhập và kiểm tra số điện thoại từ Scanner. */
    public void setSdt(Scanner sc){
        sdt = Validation.inputStringRegex(
                sc,
                "Nhap so dien thoai: ",
                Validation.PHONE_REGEX,
                "So dien thoai khong hop le, hay nhap lai!"
        );
    }
    /** Trả về địa chỉ dưới dạng chuỗi. */
    public String getDiachi(){
        return diachi.toString();
    }
    /** Nhập các thành phần địa chỉ từ Scanner. */
    public void setDiachi(Scanner sc){
        diachi.nhapDiaChi(sc);
    }
    /** Trả về ngày sinh. */
    public LocalDate getNgsinh(){
        return ngsinh;
    }
    /** Cập nhật ngày sinh nếu chuỗi có định dạng ngày hợp lệ. */
    public void setNgsinh(String newNgsinh)
    {
        if(DateUtil.isValidDate(newNgsinh))
        ngsinh = DateUtil.parseDate(newNgsinh);
    }
    /** Nhập ngày sinh từ Scanner và yêu cầu nhập lại nếu ngày không hợp lệ. */
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
    /** Nhập lần lượt họ tên, email, số điện thoại, ngày sinh và địa chỉ. */
    public void nhapThongTin(Scanner sc){
        System.out.println("Nhap thong tin cua nguoi: ");
        setHoten(sc); setEmail(sc);setSdt(sc);setNgsinh(sc);setDiachi(sc);
    }
    /** Tạo chuỗi chứa các thông tin chung của người. */
    @Override
    public String toString() {
        String ngaySinhStr = ngsinh != null ? ngsinh.format(DateUtil.DATE_FORMATTER) : "N/A";
        return "Họ tên: " + hoten +
                " | SĐT: " + sdt +
                " | Email: " + email +
                " | Địa chỉ: " + diachi +
                " | Ngày sinh: " + ngaySinhStr;
    }
    /** Trả về vai trò do lớp con định nghĩa. */
    public abstract String getVaiTro();
    //public abstract boolean coQuyenThucHien(String hanhDong);

}