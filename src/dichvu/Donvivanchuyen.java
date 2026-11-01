package dichvu;
import utils.Validation;

import java.util.ArrayList;
import java.util.Scanner;

public class Donvivanchuyen extends Doitac {
    private ArrayList<Xe> danhSachXe ;
    public Donvivanchuyen( )
    {
        danhSachXe = new ArrayList<>() ;
        super.setLoaiDoiTac("Đơn vị vận chuyển");
        super.setMaDoiTac(Validation.taoMa("DVVC%03d" , Doitac.getSoThuTuDVVC()));
        Doitac.setSoThuTuDVVC(Doitac.getSoThuTuDVVC()+1);
    }
    public Donvivanchuyen( String maDoiTac , String tenDoiTac , String soDienThoai , String diaChi , String maSoThue , String email , String loaiDoiTac , ArrayList<Xe> danhSachXe  )
    {
        super(maDoiTac, tenDoiTac, soDienThoai, diaChi, maSoThue, email , loaiDoiTac);
        this.danhSachXe = danhSachXe;
    }

    public ArrayList<Xe> getDanhSachXe() {
        return danhSachXe;
    }

    public void setDanhSachXe( ArrayList<Xe> danhSachXe) {
        this.danhSachXe = danhSachXe;
    }
    public void taoDanhSachXe( Scanner sc) {
        long soLuongXe = Validation.inputLong(sc , "Nhập số lượng xe: ") ;
        for (int i = 0; i < soLuongXe; i++) {
            System.out.println("Nhập thông tin cho xe " + (i + 1) + " ! ");
            Xe newXe = new Xe();
            newXe.setXe(sc );
            kiemTraTrung( sc , newXe );
            danhSachXe.add(newXe);
            System.out.println("Thêm xe thành công!");
        }
    }
    public void themXe( Scanner sc)
    {
        System.out.println("Nhập thông tin cho xe! ");
        Xe newXe = new Xe() ;
        newXe.setXe(sc);
        kiemTraTrung( sc , newXe);
        danhSachXe.add(newXe) ;
        System.out.println("Thêm xe thành công!");
    }
    public void xoaXe( String maXe )
    {
        int index = traCuuXe(maXe) ;
        if ( index == -1 )
        {
            System.out.println("Mã xe không tồn tại!");
            return;
        }
        danhSachXe.remove(index) ;
        System.out.println("Xoá phương tiện thành công!");
    }
    public void chinhSuaXe (  Scanner sc , String maXe )
    {
        int index = traCuuXe(maXe) ;
        if ( index == -1 )
        {
            System.out.println("Mã xe khôg tồn tại!");
            return;
        }
        boolean tiepTuc = true ;
        while (tiepTuc)
        {
            System.out.println("Nhập thông số cần sửa: ");
            System.out.println("1.Mã xe!");
            System.out.println("2.Tên xe!");
            System.out.println("3.Loại xe!");
            System.out.println("4.Giá trên 1 km!");
            System.out.println("5.Sức chứa tối đa!");
            System.out.println("6.Giá vận chuyển cơ bản!");
            int luaChon = Validation.luaChon(sc , "Nhập lựa chọn: " , 1 , 6) ;
            switch ( luaChon )
            {
                case 1:
                    String maXeMoi = utils.Validation.inputStringRegex(sc , "Nhập mã số xe: " , "XE\\d{3}" , "Mã xe không đúng định dạng vui lòng nhập lại(VD: XE001)!") ;
                    while ( traCuuXe(maXeMoi) != -1 )
                    {
                        System.out.println("Mã xe đã tồn tại vui lòng nhập lại!");
                        maXeMoi = utils.Validation.inputStringRegex(sc , "Nhập mã số xe: " , "XE\\d{3}" , "Mã xe không đúng định dạng vui lòng nhập lại(VD: XE001)!") ;
                    }
                    danhSachXe.get(index).setMaXe(maXeMoi);
                    break;
                case 2:
                    System.out.println("Nhập tên xe mới: ");
                    danhSachXe.get(index).setTenXe(sc.nextLine());
                    break;
                case 3:
                    System.out.println("Nhập loại xe mới: ");
                    danhSachXe.get(index).setLoaiXe(sc.nextLine());
                    break;
                case 4:
                    danhSachXe.get(index).setGiaMotKm(utils.Validation.inputInt(sc , "Nhập giá trên 1km: "));
                    break;
                case 5:
                    danhSachXe.get(index).setSucChuaToiDa(utils.Validation.inputInt(sc , "Nhập sức chứa tối đa mới: "));
                    break;
                case 6:
                    danhSachXe.get(index).setGiaVanChuyenCoBan(utils.Validation.inputInt(sc , "Nhập chi phí vận chuyển cơ bản mới: "));
                    break;
                default:
                    System.out.println("Lựa chọn nhập không hợp lệ!");
                    break;
            }
            System.out.println("Bạn có muốn tiếp tục chỉnh sửa phương tiện không(1/0): ");
            int luaChonTiepTuc = utils.Validation.inputInt(sc , "Bạn có muốn tiếp tục chỉnh sửa phương tiện không(1/0):" ) ;
            if ( luaChonTiepTuc == 0 )
            {
                tiepTuc = false ;
            }
        }
        System.out.println("Sửa thông tin cho phương tiện thành công!");
    }
    public int traCuuXe ( String maXe )
    {
        for ( int i = 0 ; i < danhSachXe.size() ; i++ )
            if( danhSachXe.get(i).getMaXe().equals(maXe)) return i ;
        return -1 ;
    }
    public void timXe ( String maXe )
    {
        int index = traCuuXe(maXe) ;
        if ( index == -1 )
        {
            System.out.println("Mã xe không tồn tại!");
            return;
        }
        System.out.println("Phương tiện cần tìm: ");
        System.out.println(danhSachXe.get(index));
    }
    public void inBangGia () // Danh sách xe
    {
        System.out.println("Danh sách bảng giá của từng xe: ");
        for (Xe xe:danhSachXe)
        {
            System.out.println(xe);
        }
    }
    public void kiemTraTrung( Scanner sc , Xe xe )
    {
        while( traCuuXe(xe.getMaXe()) != -1 )
        {
            System.out.println("Mã xe đã tồn tại vui lòng nhập mã mới!");
            xe.setMaXe(utils.Validation.inputStringRegex(sc , "Nhập mã xe: " , "XE\\d{3}" , "Mã xe không đúng định dạng vui lòng nhập lại!(VD hợp lệ: XE001"));
        }
    }
}
