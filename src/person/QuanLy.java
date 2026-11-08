package person;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import dichvu.Danhsachkhuyenmai;
import dichvu.KhuyenMaiPhanTram;
import dichvu.KhuyenMaiTrucTiep;
import dichvu.Khuyenmai;
import sanpham.QuanLySanPham;
import sanpham.SanPham;
import sanpham.ThucPham;
import utils.Validation; 

public class QuanLy extends NhanVien {
    DSNhanVien ds = new DSNhanVien(); 
    private final Danhsachkhuyenmai dsKhuyenMai = new Danhsachkhuyenmai();
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
    public Danhsachkhuyenmai getDanhSachKhuyenMai(){
        return dsKhuyenMai;
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
    // TODO: Sau khi co danh sach sp + dieu chinh se sua lại
    public void quanLySanPham(){
        quanLySanPham(new Scanner(System.in));
    }

    public void quanLySanPham(Scanner sc){
        while(true){
            System.out.println("\n--- QUẢN LÝ SẢN PHẨM ---");
            System.out.println("1. Xem danh sách sản phẩm");
            System.out.println("2. Thêm sản phẩm");
            System.out.println("3. Xóa sản phẩm");
            System.out.println("4. Sửa thông tin sản phẩm");
            System.out.println("5. Điều chỉnh giá bán");
            System.out.println("6. Thoát");
            int luaChon = Validation.luaChon(sc, "Chọn chức năng (1-6): ", 1, 6);

            switch(luaChon){
                case 1:
                    QuanLySanPham.inDanhSachSanPham();
                    break;
                case 2:
                    new QuanKho().capNhatKho(sc);
                    break;
                case 3:
                    xoaSanPham(sc);
                    break;
                case 4:
                    suaThongTinSanPham(sc);
                    break;
                case 5:
                    dieuChinhGiaBan(sc);
                    break;
                case 6:
                    return;
                default:
                    throw new IllegalStateException("Lựa chọn quản lý sản phẩm không hợp lệ.");
            }
        }
    }

    private void xoaSanPham(Scanner sc){
        ArrayList<SanPham> danhSach = QuanLySanPham.danhSachSanPham;
        if(danhSach.isEmpty()){
            System.out.println("Danh sách sản phẩm đang trống.");
            return;
        }

        QuanLySanPham.inDanhSachSanPham();
        String maSanPham = Validation.inputStringRegex(
                sc, "Nhập mã sản phẩm cần xóa: ", "^SP\\d+$",
                "Mã sản phẩm phải có dạng SP theo sau là chữ số.");
        int viTri = timViTriSanPham(danhSach, maSanPham);
        if(viTri == -1){
            System.out.println("Không tìm thấy sản phẩm có mã " + maSanPham + ".");
            return;
        }

        SanPham sanPham = danhSach.remove(viTri);
        System.out.println("Đã xóa sản phẩm " + sanPham.getTenSanPham() + ".");
    }

    private void suaThongTinSanPham(Scanner sc){
        SanPham sanPham = chonSanPham(sc);
        if(sanPham == null){
            return;
        }

        System.out.println("1. Sửa tên sản phẩm");
        System.out.println("2. Sửa mã nhà cung cấp");
        System.out.println("3. Sửa giá nhập");
        System.out.println("4. Sửa số lượng");
        if(sanPham instanceof ThucPham){
            System.out.println("5. Sửa hạn sử dụng");
            System.out.println("6. Quay lại");
        }else{
            System.out.println("5. Quay lại");
        }

        int soLuaChon = sanPham instanceof ThucPham ? 6 : 5;
        int luaChon = Validation.luaChon(sc, "Chọn thông tin cần sửa: ", 1, soLuaChon);
        switch(luaChon){
            case 1:
                String tenSanPham;
                do{
                    System.out.print("Nhập tên sản phẩm mới: ");
                    tenSanPham = sc.nextLine().trim();
                    if(tenSanPham.isEmpty()){
                        System.out.println("Tên sản phẩm không được để trống.");
                    }
                }while(tenSanPham.isEmpty());
                sanPham.setTenSanPham(tenSanPham);
                break;
            case 2:
                sanPham.setMaNhaCungCap(Validation.inputStringRegex(
                        sc, "Nhập mã nhà cung cấp mới: ", "^NCC\\d+$",
                        "Mã nhà cung cấp phải có dạng NCC theo sau là chữ số."));
                break;
            case 3:
                sanPham.setGiaNhap(nhapSoKhongAm(sc, "Nhập giá nhập mới: "));
                break;
            case 4:
                sanPham.setSoLuong(nhapSoLuongKhongAm(sc));
                break;
            case 5:
                if(!(sanPham instanceof ThucPham)){
                    return;
                }
                ((ThucPham) sanPham).setHanSuDung(
                        Validation.inputLocalDateRegex(sc, "Nhập hạn sử dụng mới (dd/MM/yyyy): "));
                break;
            default:
                return;
        }
        System.out.println("Đã cập nhật thông tin sản phẩm.");
    }

    private void dieuChinhGiaBan(Scanner sc){
        SanPham sanPham = chonSanPham(sc);
        if(sanPham == null){
            return;
        }
        long giaBanMoi = nhapSoKhongAm(sc, "Nhập giá bán mới: ");
        sanPham.setGiaBan(giaBanMoi);
        System.out.println("Đã cập nhật giá bán sản phẩm " + sanPham.getTenSanPham() + ".");
    }

    private SanPham chonSanPham(Scanner sc){
        ArrayList<SanPham> danhSach = QuanLySanPham.danhSachSanPham;
        if(danhSach.isEmpty()){
            System.out.println("Danh sách sản phẩm đang trống.");
            return null;
        }

        QuanLySanPham.inDanhSachSanPham();
        String maSanPham = Validation.inputStringRegex(
                sc, "Nhập mã sản phẩm: ", "^SP\\d+$",
                "Mã sản phẩm phải có dạng SP theo sau là chữ số.");
        int viTri = timViTriSanPham(danhSach, maSanPham);
        if(viTri == -1){
            System.out.println("Không tìm thấy sản phẩm có mã " + maSanPham + ".");
            return null;
        }
        return danhSach.get(viTri);
    }

    private int timViTriSanPham(ArrayList<SanPham> danhSach, String maSanPham){
        for(int i = 0; i < danhSach.size(); i++){
            if(maSanPham.equalsIgnoreCase(danhSach.get(i).getMaSanPham())){
                return i;
            }
        }
        return -1;
    }

    private long nhapSoKhongAm(Scanner sc, String thongBao){
        long giaTri;
        do{
            giaTri = Validation.inputLong(sc, thongBao);
            if(giaTri < 0){
                System.out.println("Giá trị không được âm.");
            }
        }while(giaTri < 0);
        return giaTri;
    }

    private int nhapSoLuongKhongAm(Scanner sc){
        int soLuong;
        do{
            soLuong = Validation.inputInt(sc, "Nhập số lượng mới: ");
            if(soLuong < 0){
                System.out.println("Số lượng không được âm.");
            }
        }while(soLuong < 0);
        return soLuong;
    }
    public void duyetYeuCau(Scanner sc) throws IOException{
        Path filePath = Paths.get("YeuCau.txt");
        if (!Files.exists(filePath)) {
            System.out.println("Chưa có file YeuCau.txt.");
            return;
        }

        ArrayList<String> dsYeuCau = new ArrayList<>(
                Files.readAllLines(filePath, StandardCharsets.UTF_8));
        boolean coThayDoi = false;
        for (int index = 0; index < dsYeuCau.size(); index++) {
            String[] data = dsYeuCau.get(index).split("\\|", -1);
            if (data.length != 4) {
                System.err.println("[!] Dòng yêu cầu sai định dạng, giữ nguyên: " + dsYeuCau.get(index));
                continue;
            }
            if (!data[3].equals("ChoDuyet")) {
                continue;
            }

            System.out.println("Yêu cầu: " + dsYeuCau.get(index));
            String quyetDinh = Validation.inputStringRegex(
                    sc, "Duyệt: 1 / Không duyệt: 0: ", "^[01]$",
                    "Chỉ được nhập 0 hoặc 1.");
            data[3] = quyetDinh.equals("1") ? "DaDuyet" : "KhongDuyet";
            dsYeuCau.set(index, String.join("|", data));
            coThayDoi = true;
        }

        if (coThayDoi) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    filePath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {
                for (String yeuCau : dsYeuCau) {
                    writer.write(yeuCau);
                    writer.newLine();
                }
            }
        }
    }
    public void giamGiaCanHSD(Scanner sc){
        QuanKho qk = new QuanKho(); 
        ArrayList<ThucPham> dscanDate = qk.danhSachSapHetHan(7);  
        dscanDate.toString(); 
        System.out.println("Thêm giảm giá với thực phẩm cận hạn sử dụng: ");
        for(ThucPham i : dscanDate)
        {
            System.out.println("1.Chọn giảm Phần Trăm");
            System.out.println("2.Chọn giảm trực tiếp");
            int luaChon = Validation.inputInt(sc, "Hãy chọn"); 
            if(luaChon == 1) {

                Khuyenmai km = new KhuyenMaiPhanTram(); 
                km.setKM(sc);
            }
            else{
                Khuyenmai km = new KhuyenMaiTrucTiep(); 
                km.setKM(sc);
            }
        }

                


    }
    public void themKhuyenMai(Scanner sc){
        while(true)
        {
            System.out.println("Tạo khuyến mãi cho nhóm sản phẩm:");
            System.out.println("1. Thực phẩm");
            System.out.println("2. Gia dụng");
            System.out.println("3. Điện tử");
            System.out.println("4. Thoát");
            int luaChon = Validation.luaChon(sc, "Hãy chọn (1-4): ", 1, 4);
            switch (luaChon) {
                case 1:
                    System.out.println("Đã chọn nhóm Thực phẩm.");
                    dsKhuyenMai.themKhuyenMai(sc);
                    break;
                case 2:
                    System.out.println("Đã chọn nhóm Gia dụng.");
                    dsKhuyenMai.themKhuyenMai(sc);
                    break;
                case 3:
                    System.out.println("Đã chọn nhóm Điện tử.");
                    dsKhuyenMai.themKhuyenMai(sc);
                    break;
                case 4:
                    return;
                default:
                    throw new IllegalStateException("Lựa chọn nhóm sản phẩm không hợp lệ.");
            }
        }
    }

}
