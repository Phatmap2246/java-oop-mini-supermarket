package person;
import java.time.LocalDate;
import java.util.Scanner;
import utils.Validation; 

public class QuanLy extends NhanVien {
    DSNhanVien ds = new DSNhanVien(); 
    /** Khởi tạo quản lý với thông tin mặc định. */
    public QuanLy(){
        super();
    }
    /** Khởi tạo quản lý bằng thông tin được truyền vào. */
    public QuanLy(String email,String hoten,String sdt,DiaChi diachi, LocalDate ngsinh,String maNV,String vaitro,long luong,long phuCapTrachNhiem){
        super(email,hoten,sdt,diachi,ngsinh,maNV,vaitro,luong,phuCapTrachNhiem);
    }
    /** Sao chép thông tin nhân viên quản lý từ đối tượng nguồn. */
    public QuanLy(QuanLy ql){
        super(ql);
    }
    /** Trả về vai trò cố định của lớp là Quản lý. */
    public String getVaiTro(){
        return "QuanLy";
    }
// Quyền quản lý nhân sự (thêm / xóa / sửa)
    /** Hiển thị menu để thêm, xóa hoặc sửa nhân viên trong danh sách quản lý. */
    public void quanLyNhanSu(Scanner sc){
        System.out.println("chon viec can lam: ");
        System.out.println("1.Them nhan vien moi: ");
        System.out.println("2.Xoa nhan vien : ");
        System.out.println("3.Sau thong tin nhan vien: ");
        System.out.println("4.Xong ");
        int c = Validation.inputInt(sc, "Hay chon 1 trong 3"); 
        while(true){
            switch (c) {
                case 1:
                    ds.themNV(sc);
                    break;
                case 2:
                    String MaNV = Validation.inputStringRegex(sc, "Nhap ma nhan vien muon xoa", Validation.MaNV_REGEX,
                     "Ma nhan vien chua dung dinh dang (NVxxx)!,hay nhap lai");
                    ds.xoaNV(MaNV);
                    break;
                case 3:
                    String MaNV_sua = Validation.inputStringRegex(sc, "Nhap ma nhan vien muon sua", Validation.MaNV_REGEX,
                     "Ma nhan vien chua dung dinh dang (NVxxx)!,hay nhap lai");
                    ds.suaNV(MaNV_sua, sc);
                    break;
                case 4:
                    System.out.println("Da thoat");;
                    break;
                default: System.out.println("Chi duoc nhap tu 1 den 4, hay nhap lai");
            }
            if(c == 4) break; 
        }
    }
    
}
