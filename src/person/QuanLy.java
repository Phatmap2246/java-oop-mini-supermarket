package person;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public void getDSNhanVien(){
        // đọc file danh sách nhân viên và lưu và list
    }
    public void phanCaLamViec(DSNhanVien ds,Scanner sc){
        if(ds.list.isEmpty()){
            System.out.println("Danh sach nhan vien rong.");
            return;
        }

        System.out.println("Phan cong ca lam:");
        while(true){
            System.out.println("1. Ca 1 (6h-14h)");
            System.out.println("2. Ca 2 (14h-22h)");
            System.out.println("3. Hoan tat");
            int luaChonCa = Validation.inputInt(sc, "Chon ca can phan cong: ");

            switch(luaChonCa){
                case 1:
                    phanCongCa(ds, sc, "6h-14h");
                    break;
                case 2:
                    phanCongCa(ds, sc, "14h-22h");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lua chon khong hop le, vui long chon lai.");
            }
        }
    }

    private void phanCongCa(DSNhanVien ds, Scanner sc, String tenCa){
        int soNhanVienToiDa = 0;
        for(NhanVien nv : ds.list){
            String vaiTro = nv.getVaiTro();
            if("KeToan".equals(vaiTro) || "ThuNgan".equals(vaiTro) || "QuanKho".equals(vaiTro)){
                soNhanVienToiDa++;
            }
        }

        int soNguoi;
        do{
            soNguoi = Validation.inputInt(sc, "Nhap so nguoi cho ca " + tenCa + ": ");
            if(soNguoi < 0 || soNguoi > soNhanVienToiDa){
                System.out.println("So nguoi phai tu 0 den " + soNhanVienToiDa + ".");
            }
        }while(soNguoi < 0 || soNguoi > soNhanVienToiDa);

        ArrayList<NhanVien> nhanVienTrongCa = new ArrayList<>();
        for(int i = 0; i < soNguoi; i++){
            NhanVien nhanVienDuocChon = null;
            while(nhanVienDuocChon == null){
                System.out.println("Chon vai tro:");
                System.out.println("1. KeToan");
                System.out.println("2. ThuNgan");
                System.out.println("3. QuanKho");
                int luaChonVaiTro = Validation.inputInt(sc, "Chon vai tro: ");
                String vaiTro;
                switch(luaChonVaiTro){
                    case 1:
                        vaiTro = "KeToan";
                        break;
                    case 2:
                        vaiTro = "ThuNgan";
                        break;
                    case 3:
                        vaiTro = "QuanKho";
                        break;
                    default:
                        System.out.println("Vai tro khong hop le, vui long chon lai.");
                        continue;
                }

                boolean coNhanVienPhuHop = false;
                for(NhanVien nv : ds.list){
                    if(vaiTro.equals(nv.getVaiTro()) && !nhanVienTrongCa.contains(nv)){
                        System.out.println("MaNV: " + nv.getMaNV());
                        coNhanVienPhuHop = true;
                    }
                }
                if(!coNhanVienPhuHop){
                    System.out.println("Khong con nhan vien vai tro " + vaiTro + " co the phan cong.");
                    continue;
                }

                String maNV = Validation.inputStringRegex(
                        sc, "Nhap MaNV muon phan vao ca: ", Validation.MaNV_REGEX,
                        "Ma nhan vien chua dung dinh dang (NVxxx)!");
                for(NhanVien nv : ds.list){
                    if(maNV.equals(nv.getMaNV())
                            && vaiTro.equals(nv.getVaiTro())
                            && !nhanVienTrongCa.contains(nv)){
                        nhanVienDuocChon = nv;
                        break;
                    }
                }
                if(nhanVienDuocChon == null){
                    System.out.println("MaNV khong thuoc vai tro da chon, khong ton tai, hoac da duoc phan trong ca nay.");
                }
            }

            nhanVienTrongCa.add(nhanVienDuocChon);
            System.out.println("Da phan " + nhanVienDuocChon.getMaNV()
                    + " (" + nhanVienDuocChon.getVaiTro() + ") vao ca " + tenCa + ".");
        }

    }
    public void quanLySanPham(){
        //Các phương thức thêm xóa sửa sản phẩm. Điều chỉnh giá bán. Thêm Khuyến Mãi, set thời gian khuyến mãi
        
    }
    public void duyetYeuCau(){
        //Khi quản kho, thu ngân , ke toán gửi yêu cầu liên file text, quản lý lấy dữ liệu từ file đó và ra
        //quyết định có/không
    }
    public void themSuKien(){
        //Tao cac chiec dich khuyen mai, giam gia
    }

}
