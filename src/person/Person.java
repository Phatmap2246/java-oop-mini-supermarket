package person;

import java.time.LocalDate;
import java.util.Scanner;

import utils.DateUtil;
import utils.Validation;

public abstract class Person {
    private String email, hoten, sdt;
    private DiaChi diachi;
    private LocalDate ngsinh;
    /** Khởi tạo người với thông tin mặc định và địa chỉ rỗng. */
    public Person() {
        email = null;
        hoten = null;
        sdt = null;
        diachi = new DiaChi();
        ngsinh = LocalDate.now();
    }

    /** Khởi tạo người từ các thông tin được truyền vào. */
    public Person(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh) {
        this.email = email;
        this.hoten = hoten;
        this.sdt = sdt;
        this.diachi = diachi;
        this.ngsinh = ngsinh;
    }

    /** Sao chép thông tin từ một đối tượng Person khác. */
    public Person(Person p) {
        if (p == null) throw new IllegalArgumentException("Người cần sao chép không được null.");
        email = p.email;
        hoten = p.hoten;
        sdt = p.sdt;
        diachi = p.diachi;
        ngsinh = p.ngsinh;
    }

    /** Trả về email của người. */
    public String getEmail() {
        return email;
    }

    /** Cập nhật email nếu địa chỉ mới khớp định dạng quy định. */
    public void setEmail(String newEmail) {
        if (newEmail == null || newEmail.isBlank() || !newEmail.matches(Validation.EMAIL_REGEX)) {
            throw new IllegalArgumentException("Email không hợp lệ.");
        }
        email = newEmail;
    }

    /** Nhập và kiểm tra email từ Scanner. */
    public void setEmail(Scanner sc) {
        email = Validation.inputStringRegex(
                sc,
                "Nhập email: ",
                Validation.EMAIL_REGEX,
                "Email không hợp lệ, vui lòng nhập lại."
        );
    }

    /** Trả về họ tên của người. */
    public String getHoten() {
        return hoten;
    }

    /** Cập nhật họ tên nếu tên mới khớp định dạng quy định. */
    public void setHoten(String newhoten) {
        if (newhoten == null || newhoten.isBlank() || !newhoten.matches(Validation.HOTEN_REGEX)) {
            throw new IllegalArgumentException("Hoten không hợp lệ.");
        }
        hoten = newhoten;
    }

    public void setDiachi(DiaChi diachi) {
        if (diachi == null) {
            throw new IllegalArgumentException("Địa chỉ không được null.");
        }
        this.diachi = diachi;
    }

    public void setNgsinh(LocalDate ngsinh) {
        if (ngsinh == null) {
            throw new IllegalArgumentException("Ngày sinh không được null.");
        }
        this.ngsinh = ngsinh;
    }

    /** Nhập và kiểm tra họ tên từ Scanner. */
    public void setHoten(Scanner sc) {
        hoten = Validation.inputStringRegex(
                sc,
                "Nhập họ tên: ",
                Validation.HOTEN_REGEX,
                "Họ tên không hợp lệ, vui lòng nhập lại."
        );
    }

    /** Trả về số điện thoại của người. */
    public String getSdt() {
        return sdt;
    }

    /** Cập nhật số điện thoại nếu số mới khớp định dạng quy định. */
    public void setSdt(String newSdt) {
        if (newSdt == null || newSdt.isBlank() || !newSdt.matches(Validation.PHONE_REGEX)) {
            throw new IllegalArgumentException("Sdt không hợp lệ.");
        }
        sdt = newSdt;
    }

    /** Nhập và kiểm tra số điện thoại từ Scanner. */
    public void setSdt(Scanner sc) {
        sdt = Validation.inputStringRegex(
                sc,
                "Nhập số điện thoại: ",
                Validation.PHONE_REGEX,
                "Số điện thoại không hợp lệ, vui lòng nhập lại."
        );
    }
    /** Trả về địa chỉ dưới dạng chuỗi. */
    public String getDiachi() {
        return diachi == null ? "" : diachi.toString();
    }

    /** Trả về đối tượng địa chỉ để lưu hoặc xử lý từng thành phần. */
    public DiaChi getDiaChi() {
        return diachi;
    }

    /** Nhập các thành phần địa chỉ từ Scanner. */
    public void setDiachi(Scanner sc) {
        if (diachi == null) diachi = new DiaChi();
        diachi.nhapDiaChi(sc);
    }

    /** Trả về ngày sinh. */
    public LocalDate getNgsinh() {
        return ngsinh;
    }

    /** Cập nhật ngày sinh nếu chuỗi có định dạng ngày hợp lệ. */
    public void setNgsinh(String newNgsinh) {
        if (!DateUtil.isValidDate(newNgsinh)) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ.");
        }
        if (DateUtil.isValidDate(newNgsinh)) {
            ngsinh = DateUtil.parseDate(newNgsinh);
        }
    }

    /** Nhập ngày sinh từ Scanner và yêu cầu nhập lại nếu ngày không hợp lệ. */
    public void setNgsinh(Scanner sc) {
        System.out.print("Nhập ngày sinh (dd/MM/yyyy): ");
        String ngsinhStr = sc.nextLine();
        LocalDate ngaySinh = DateUtil.parseDate(ngsinhStr);
        while (ngaySinh == null) {
            System.out.print("Ngày sinh không hợp lệ. Nhập lại (dd/MM/yyyy): ");
            ngsinhStr = sc.nextLine();
            ngaySinh = DateUtil.parseDate(ngsinhStr);
        }
        ngsinh = ngaySinh;
    }

    /** Nhập lần lượt họ tên, email, số điện thoại, ngày sinh và địa chỉ. */
    public void nhapThongTin(Scanner sc) {
        System.out.println("\n=== THÔNG TIN CÁ NHÂN ===");
        setHoten(sc);
        setEmail(sc);
        setSdt(sc);
        setNgsinh(sc);
        setDiachi(sc);
    }

    /** Tạo chuỗi chứa các thông tin chung của người. */
    @Override
    public String toString() {
        String ngaySinhStr = ngsinh != null ? ngsinh.format(DateUtil.DATE_FORMATTER) : "N/A";
        return "Họ tên: " + hoten
                + " | SĐT: " + sdt
                + " | Email: " + email
                + " | Địa chỉ: " + diachi
                + " | Ngày sinh: " + ngaySinhStr;
    }

    /** Trả về vai trò do lớp con định nghĩa. */
    public abstract String getVaiTro();
}
