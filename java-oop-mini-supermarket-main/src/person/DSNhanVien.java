package person;
import java.util.ArrayList;
import java.util.Scanner;

import utils.Validation; 
public class DSNhanVien {
    ArrayList<NhanVien> list; 
    /** Khởi tạo danh sách nhân viên rỗng. */
    public DSNhanVien(){
        list = new ArrayList<>(); 
    }
    /** Khởi tạo danh sách bằng danh sách được truyền vào. */
    public DSNhanVien(ArrayList<NhanVien> newList){
        list = newList; 
    }
    /** Khởi tạo danh sách dùng chung danh sách của đối tượng nguồn. */
    public DSNhanVien(DSNhanVien l ){
        this.list = l.list; 
    }
    /** Nhập số lượng rồi nhập thông tin và thêm từng nhân viên vào danh sách. */
    public void taoDS(Scanner sc){
        int n = Validation.inputInt(sc, "Nhap so nhan vien tao danh sach:"); 
        for(int i = 0; i<n; i++)
        {
            NhanVien nv = new NhanVien(); 
            nv.nhapThongTin(sc);
            list.add(nv); 
        }
    }
    /** In thông tin từng nhân viên; thông báo nếu danh sách rỗng. */
    public void xuatDSNV(){
        if(list.isEmpty()){
            System.out.println("Danh sach nhan vien rong.");
            return;
        }

        System.out.println("\n--- DANH SACH NHAN VIEN ---");
        for(NhanVien nv : list){
            System.out.println(nv.toString());
        }
    }
    /** Nhập thông tin một nhân viên mới rồi thêm vào danh sách. */
    public void themNV(Scanner sc){
        NhanVien nv =new NhanVien(); 
        nv.nhapThongTin(sc);
        list.add(nv); 
    }
    /** Tìm vị trí nhân viên theo mã; trả về -1 nếu không tìm thấy. */
    public int timNV(String MaNV){
        for(int i = 0;i<list.size() ;i++)
            if(MaNV.trim().equals(list.get(i).getMaNV().trim())) return i; 
        return -1; 
    }
    /** Xóa nhân viên theo mã hoặc thông báo nếu không tìm thấy. */
    public void xoaNV(String MaNV){
        int idx = timNV(MaNV);
        if(idx != -1) list.remove(list.get(idx));
        else System.out.println("Khong tim thay trong danh sach");
    }

    /** Cập nhật thông tin nhân viên theo mã; trả về false nếu không tìm thấy. */
    public boolean suaNV(String maNV, Scanner sc){
        int idx = timNV(maNV);
        if(idx == -1){
            System.out.println("Khong tim thay trong danh sach");
            return false;
        }

        NhanVien nv = list.get(idx);
        int choice;
        do{
            System.out.println("\n--- SUA THONG TIN NHAN VIEN: " + nv.getMaNV() + " ---");
            System.out.println("1. Ma nhan vien");
            System.out.println("2. Ho ten");
            System.out.println("3. Email");
            System.out.println("4. So dien thoai");
            System.out.println("5. Ngay sinh");
            System.out.println("6. Dia chi");
            System.out.println("7. Vai tro");
            System.out.println("8. Luong");
            System.out.println("9. Phu cap trach nhiem");
            System.out.println("0. Hoan tat");
            choice = Validation.inputInt(sc, "Chon thong tin muon sua: ");

            switch(choice){
                case 1:
                    String maMoi = Validation.inputStringRegex(
                            sc, "Nhap ma nhan vien moi: ", Validation.MaNV_REGEX,
                            "Ma nhan vien chua dung dinh dang (NVxxx)!");
                    boolean biTrung;
                    do{
                        biTrung = false;
                        for(int i = 0; i < list.size(); i++){
                            String maTrongDanhSach = list.get(i).getMaNV();
                            if(i != idx && maTrongDanhSach != null){
                                biTrung = true;
                                maMoi = Validation.inputStringRegex(
                                        sc, "Ma da ton tai, nhap ma nhan vien khac: ",
                                        Validation.MaNV_REGEX,
                                        "Ma nhan vien chua dung dinh dang (NVxxx)!");
                                break;
                            }
                        }
                    }while(biTrung);
                    nv.setMaNV(maMoi);
                    break;
                case 2:
                    nv.setHoten(sc);
                    break;
                case 3:
                    nv.setEmail(sc);
                    break;
                case 4:
                    nv.setSdt(sc);
                    break;
                case 5:
                    nv.setNgsinh(sc);
                    break;
                case 6:
                    nv.setDiachi(sc);
                    break;
                case 7:
                    String vaiTro = Validation.inputStringRegex(
                            sc, "Nhap vai tro (QuanLy/KeToan/QuanKho/ThuNgan): ",
                            "^(QuanLy|KeToan|QuanKho|ThuNgan)$",
                            "Vai tro khong hop le, vui long nhap lai!");
                    nv.setVaiTro(vaiTro);
                    break;
                case 8:
                    nv.setLuong(sc);
                    break;
                case 9:
                    nv.setPhuCapTrachNhiem(sc);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le, vui long chon lai.");
            }
        }while(choice != 0);

        return true;
    }
//Note: code thêm ghi và đọc danh sách từ file

}
