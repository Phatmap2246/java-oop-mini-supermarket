package dichvu ;

import java.util.Locale;

import utils.Validation;

import java.time.LocalDate;
import java.util.Scanner;

public class Danhsachkhuyenmai implements interfaces.IQuanLy<Khuyenmai> {
    @Override
    public Khuyenmai timTen(String ten) {
        if (ten == null || ten.isBlank()) return null;
        for (Khuyenmai km : khuyenmais) {
            if (km != null && km.isTonTai() && km.getLoaiKhuyenMai() != null
                    && km.getLoaiKhuyenMai().toLowerCase(Locale.ROOT).contains(ten.trim().toLowerCase(Locale.ROOT))) return km;
        }
        return null;
    }

    @Override
    public Khuyenmai timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) return null;
        for (Khuyenmai km : khuyenmais) {
            if (km != null && km.isTonTai()
                    && km.toString().toLowerCase(Locale.ROOT).contains(khoa.trim().toLowerCase(Locale.ROOT))) return km;
        }
        return null;
    }
    private Khuyenmai[] khuyenmais;
    private int soLuongKhuyenMai;
    public Danhsachkhuyenmai()
    {
        soLuongKhuyenMai = 0;
        khuyenmais = new Khuyenmai[soLuongKhuyenMai];
    }

    public void setKhuyenmais(Khuyenmai[] khuyenmais) {
        if (khuyenmais == null) {
            throw new IllegalArgumentException("Danh sách không được null.");
        }
        for (Object phanTu : khuyenmais) {
            if (phanTu == null) throw new IllegalArgumentException("Danh sách có phần tử null.");
        }

        this.khuyenmais = khuyenmais;
        this.soLuongKhuyenMai = khuyenmais.length;
    }

    public Khuyenmai[] getKhuyenmais() {
        return khuyenmais;
    }
    public int getSoLuongKhuyenMai() {
        return soLuongKhuyenMai;
    }
    @Override
    public void taoDanhSach(Scanner sc )
    {
        System.out.println("Tạo các khuyến mãi!");
        while (true)
        {
            them(sc);
            System.out.println("Bạn có muốn tiếp tục thêm khuyến mãi(1/0): ");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 1);
            if ( luaChon == 0 ) break;
        }
        System.out.println("Tạo dữ liệu sản phẩm thành công!");
    }
    @Override
    public void xuatDanhSach()
    {
        System.out.println("Danh sách khuyến mãi!");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for ( Khuyenmai km: khuyenmais )
            if (km != null && km.isTonTai()) System.out.println(km);
    }

    public void them(Khuyenmai khuyenMai) {
        if (khuyenMai == null) {
            System.out.println("Không thể thêm đối tượng null!");
            return;
        }
        int newSoLuongKhuyenMai = soLuongKhuyenMai + 1;
        Khuyenmai[] tam = new Khuyenmai[newSoLuongKhuyenMai];
        System.arraycopy(khuyenmais, 0, tam, 0, soLuongKhuyenMai);
        tam[soLuongKhuyenMai] = khuyenMai;
        khuyenmais = tam;
        soLuongKhuyenMai = newSoLuongKhuyenMai;
    }

    @Override
    public void them(Scanner sc )
    {
        Khuyenmai new_km ;
        System.out.println("Chọn loại khuyến mãi cần thêm: ");
        System.out.println("1. Khuyến mãi theo phần trăm!");
        System.out.println("2. Khuyến mãi trực tiếp!");
        System.out.println("3. Khuyến mãi mua tặng!");
        int Luachon = Validation.inputInt(sc , "Nhập lựa chọn: ") ;
        switch (Luachon)
        {
            case 1:
                new_km = new KhuyenMaiPhanTram() ;
                new_km.setKM(sc);
                them(new_km);
                break;
            case 2:
                new_km = new KhuyenMaiTrucTiep() ;
                new_km.setKM(sc);
                them(new_km);
                break;
            case 3:
                new_km = new KhuyenMaiMuaTang() ;
                new_km.setKM(sc);
                them(new_km);
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
    }
    public void themKSanPhamKM( Scanner sc , int kSanPham )
    {
        System.out.println("Nhập sản phẩm khuyến mãi muốn thêm!");
        for ( int i = 0 ; i < kSanPham ; i++ )
        {
            them(sc);
        }
        System.out.println("Thêm k sản phẩm khuyến mãi thành công!");
    }
    @Override
    public boolean sua( String maKhuyenMai , Scanner sc )
    {
        try {

            int index = timViTri(maKhuyenMai) ;
            if ( index == -1 )
            {
                System.out.println("Mã khuyến mãi không tồn tại!");
                return false;
            }
            Khuyenmai km = khuyenmais[index];
            int luaChon ;
            System.out.println("Chọn các giá trị cần sửa thông tin!");
            System.out.println("1. Ngày bắt đầu!");
            System.out.println("2. Ngày kết thúc!");
            System.out.println("3. Mức chi tối thiểu!");
            if ( km instanceof  KhuyenMaiPhanTram)
            {
                System.out.println("4. Phần trăm giảm giá!");
                System.out.println("5. Mức giảm giá tối đa!");
                luaChon = Validation.inputInt( sc , "Nhập lựa chọn: ") ;
                switch (luaChon)
                {
                    case 1:
                        km.setNgayBatDau(Validation.inputLocalDateRegex(sc , "Nhập ngày bắt đầu giảm giá mới: "));
                        break;
                    case 2:
                        km.setNgayKetThuc(Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc mới: "));
                        break;
                    case 3:
                        km.setMucChiToiThieu(Validation.inputLong(sc , "Nhập mức chi tiêu tối thiểu mới: "));
                        break;
                    case 4:
                        double phanTramMoi = Validation.inputDouble(sc,
                                "Nhập phần trăm khuyến mãi mới: ");
                        while (phanTramMoi > 100) {
                            System.out.println("[!] Phần trăm giảm giá không được vượt quá 100%.");
                            phanTramMoi = Validation.inputDouble(sc,
                                    "Nhập phần trăm khuyến mãi mới: ");
                        }
                        ((KhuyenMaiPhanTram) km).setPhanTramGiamGia(phanTramMoi / 100.0);
                        break;
                    case 5:
                        ((KhuyenMaiPhanTram) km).setMucGiamToiDa(Validation.inputLong(sc , "Nhập mức giảm tối đa mới: "));
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        return false;
                }
            } else if ( km instanceof  KhuyenMaiTrucTiep) {
                System.out.println("4. Số tiền giảm!");
                luaChon = Validation.inputInt( sc , "Nhập lựa chọn: ") ;
                switch (luaChon )
                {
                    case 1:
                        km.setNgayBatDau(Validation.inputLocalDateRegex(sc , "Nhập ngày bắt đầu giảm giá mới: "));
                        break;
                    case 2:
                        km.setNgayKetThuc(Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc mới: "));
                        break;
                    case 3:
                        km.setMucChiToiThieu(Validation.inputLong(sc , "Nhập mức chi tiêu tối thiểu mới: "));
                        break;
                    case 4:
                        ((KhuyenMaiTrucTiep) km).setSoTienGiam(Validation.inputLong(sc , "Nhập số tiền giảm giá mới: "));
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        return false;
                }
            } else if ( km instanceof  KhuyenMaiMuaTang) {
                System.out.println("4. Số lượng mua tối thiểu!");
                System.out.println("5. Số lượng tặng!");
                luaChon = Validation.inputInt( sc , "Nhập lựa chọn: ") ;
                switch (luaChon )
                {
                    case 1:
                        while (true)
                        {
                            try{
                                km.setNgayBatDau(Validation.inputLocalDateRegex(sc , "Nhập ngày bắt đầu giảm giá mới: "));
                                break;
                            }
                            catch (IllegalArgumentException e )
                            {
                                System.out.println(e.getMessage());
                            }
                        }
                        break;
                    case 2:
                        while (true)
                        {
                            try{
                                km.setNgayKetThuc(Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc giảm giá mới: "));
                                break;
                            }
                            catch (IllegalArgumentException e )
                            {
                                System.out.println(e.getMessage());
                            }
                        }
                        break;
                    case 3:
                        km.setMucChiToiThieu(Validation.inputLong(sc , "Nhập mức chi tiêu tối thiểu mới: "));
                        break;
                    case 4:
                        ((KhuyenMaiMuaTang) km).setSoLuongMuaToiThieu(Validation.inputInt(sc , "Nhập số lượng mua tối thiểu mới: "));
                        break;
                    case 5:
                        ((KhuyenMaiMuaTang) km).setSoLuongTang(Validation.inputInt( sc , "Nhập số lượng tặng mới: "));
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        return false;
                }
            } else return false;
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean xoa(String maKhuyenmai )
    {
        int index = timViTri(maKhuyenmai);
        if (index == -1) return false;
        khuyenmais[index].setTonTai(false);
        System.out.println("Xóa khuyến mãi thành công!");
        return true;
    }
    public void inDanhSachKhuyenMaiHetHan( Scanner sc)
    {
        System.out.println("Danh sách khuyến mãi hết hạn!");
        for ( Khuyenmai km: khuyenmais )
            if (km != null && km.isTonTai() && km.kiemTraHetHanKhuyenMai() ) System.out.println(km);
        if ( Validation.inputCoKhong(sc , "Bạn có muốn xoá các khuyến mãi không(Có/Không): "))
        {
            System.out.println("Nhập yêu cầu bạn muốn!");
            System.out.println("1. Xoá hết khuyến mãi hết hạn!");
            System.out.println("2. Xoá từng khuyến mãi hết hạn!");
            int Luachon = Validation.inputInt(sc ,"Nhập lựa chọn: " ) ;
            switch (Luachon)
            {
                case 1:
                    for (int i = soLuongKhuyenMai - 1; i >= 0; i--) {
                        if (khuyenmais[i] != null && khuyenmais[i].isTonTai() && khuyenmais[i].kiemTraHetHanKhuyenMai()) {
                            xoa(khuyenmais[i].getMaKhuyenMai());
                        }
                    }
                    System.out.println("Xoá tất cả khuyến mãi hết hạn thành công!");
                    break;
                case 2:
                    while (true)
                    {

                        String maKhuyenMai = Validation.inputStringRegex( sc , "Nhập mã khuyến mãi bạn muốn xoá: ", "KM\\d{3}" , "Mã khuyến mãi không đúng định dạng vui lòng nhập lại(ví dụ hợp lệ: KM001): ", Khuyenmai.getSoThuTu());
                        int index = timViTri(maKhuyenMai) ;
                        if ( index == -1 )
                        {
                            System.out.println("Mã khuyến mãi không tồn tại!");
                            continue;
                        }
                        else if ( !khuyenmais[index].kiemTraHetHanKhuyenMai() )
                        {
                            if ( Validation.inputCoKhong(sc , "Khuyến mãi vẫn còn thời hạn bạn vẫn muốn xoá?(Có/Không): " ))
                                xoa(maKhuyenMai);
                        }
                        else xoa(maKhuyenMai);
                        System.out.println("Xoá khuyến mãi thành công!");
                        if ( !Validation.inputCoKhong(sc ,"Bạn có muốn tiếp tục(Có/Không): " )) break;
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        }
    }
    @Override
    public int timViTri( String maKhuyenMai )
    {
        if (maKhuyenMai == null || maKhuyenMai.isBlank()) return -1;
        maKhuyenMai = maKhuyenMai.trim();
        for ( int i = 0 ; i < soLuongKhuyenMai ; i++ )
        {
            if (khuyenmais[i] != null && khuyenmais[i].isTonTai() && (maKhuyenMai != null && maKhuyenMai.equalsIgnoreCase(khuyenmais[i].getMaKhuyenMai())))
                return i ;
        }
        System.out.println("Không tìm thấy khuyến mãi hoặc khuyến mãi đã bị xóa!");
        return -1 ;
    }
    public void timKiemKhuyenMai( String maKhuyenMai)
    {
        int index = timViTri(maKhuyenMai) ;
        if ( index == -1 )
        {
            System.out.println("Mã khuyến mãi không tồn tại!");
            return;
        }
        System.out.println("Đang tra cứu...");
        Validation.danghoatdong(1);
        System.out.println("Khuyến mãi cần tìm!");
        System.out.println(khuyenmais[index]);
    }
    public void timKiemNhieuKhoa ( String message )
    {
        if ( message == null || message.isBlank() )
        {
            System.out.println("Vui lòng nhập từ khoá!");
            return;
        }
        boolean flag = false ;
        System.out.println("Kết quả tìm kiếm trả về: ");
        System.out.println("Đang tra cứu...");
        Validation.danghoatdong(1);
        for ( Khuyenmai km: khuyenmais)
        {
            if (km != null && km.isTonTai() && ((km.getMaKhuyenMai() != null && km.getMaKhuyenMai().equalsIgnoreCase(message.trim())) || (km.getLoaiKhuyenMai() != null && km.getLoaiKhuyenMai().toLowerCase(java.util.Locale.ROOT).contains(message.trim().toLowerCase(java.util.Locale.ROOT)))))
            {
                System.out.println(km);
                flag = true ;
            }
        }
        if (!flag)
            System.out.println("Không tìm thấy khuyến mãi phù hợp!");
    }
    public void timKiemTheoKhoa( LocalDate time )
    {
        if ( time == null )
        {
            System.out.println("Vui lòng nhập từ khoá!");
            return;
        }
        boolean flag = false ;
        System.out.println("Đang tra cứu...");
        Validation.danghoatdong(1);
        for ( Khuyenmai km: khuyenmais )
        {
            if (km != null && km.isTonTai() && (time.equals(km.getNgayBatDau()) || time.equals(km.getNgayKetThuc())))
            {
                System.out.println(km);
                flag = true ;
            }
        }
        if (!flag ) System.out.println("Không tìm thấy khuyến mãi phù hợp!");
    }
    @Override
    public Khuyenmai timMa ( String maKhuyenMai )
    {
        int index = timViTri(maKhuyenMai) ;
        if ( index == -1 )
        {
            System.out.println("Mã khuyến mãi không tồn tại!");
            return null ;
        }
        return khuyenmais[index] ;
    }

    public void timKhoa(Scanner sc) {
        while (true) {
            System.out.println("================ TÌM KIẾM KHUYẾN MÃI ================");
            System.out.println("1. Mã khuyến mãi");
            System.out.println("2. Loại khuyến mãi");
            System.out.println("3. Nhập khoảng thời gian khuyến mãi");
            System.out.println("4. Mức chi tối thiểu");
            System.out.println("0. Trở về");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 5);
            if (luaChon == 0) return;
            boolean flag = false;
            System.out.println("Kết quả tra cứu:");
            switch (luaChon) {
                case 1: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã khuyến mãi cần tìm: ", Validation.MaKM_REGEX, "Mã khuyến mãi không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Khuyenmai km : khuyenmais) {
                        if (km != null && km.isTonTai() && ((km.getMaKhuyenMai()) != null && km.getMaKhuyenMai().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(km);
                            flag = true;
                        }
                    }
                    break;
                }
                case 2: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập loại khuyến mãi cần tìm: ", ".+", "Loại khuyến mãi không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Khuyenmai km : khuyenmais) {
                        if (km != null && km.isTonTai() && ((km.getLoaiKhuyenMai()) != null && (km.getLoaiKhuyenMai()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(km);
                            flag = true;
                        }
                    }
                    break;
                }
                case 3: {
                    LocalDate tu = Validation.inputLocalDateRegex(sc, "Từ (dd/MM/yyyy): ");
                    LocalDate den = Validation.inputLocalDateRegex(sc, "Đến (dd/MM/yyyy): ");
                    if (tu.isAfter(den)) {
                        System.out.println("Thời gian bắt đầu không được sau thời gian kết thúc!");
                        continue;
                    }
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Khuyenmai km : khuyenmais) {
                        if (km != null && km.isTonTai() && (km.getNgayBatDau() != null && !km.getNgayBatDau().isBefore(tu) && !km.getNgayBatDau().isAfter(den))) {
                            System.out.println(km);
                            flag = true;
                        }
                    }
                    break;
                }
                case 4: {
                    long tu = Validation.inputLong(sc, "Giá trị từ: ");
                    long den = Validation.inputLong(sc, "Giá trị đến: ");
                    if (tu > den) {
                        System.out.println("Giá trị bắt đầu không được lớn hơn giá trị kết thúc!");
                        continue;
                    }
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Khuyenmai km : khuyenmais) {
                        if (km != null && km.isTonTai() && (km.getMucChiToiThieu() >= tu && km.getMucChiToiThieu() <= den)) {
                            System.out.println(km);
                            flag = true;
                        }
                    }
                    break;
                }
                default:
                    continue;
            }
            if (!flag) System.out.println("Không tìm thấy kết quả phù hợp!");
        }
    }
    public void thongKeCacloaiKhuyenMai( String Loai )
    {
        int tongKhuyenMai = 0 ;
        boolean flag = false ;
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for ( Khuyenmai km: khuyenmais )
        {
            if (km == null) continue;
            if ( km.isTonTai() && km.dangHoatDong(LocalDate.now()) && km.getLoaiKhuyenMai() != null && km.getLoaiKhuyenMai().equalsIgnoreCase(Loai))
            {
                System.out.println(km);
                tongKhuyenMai++ ;
                flag = true ;
            }
        }
        System.out.println("Tổng khuyến mãi thuộc loại " + Loai + " là: " + tongKhuyenMai);
        if ( !flag )
        {
            System.out.println("Không tồn tại khuyến mãi nào thuộc loại " + Loai );
            return;
        }
    }
    public void thongKeCacLoaiKhuyenMai( Scanner sc )
    {
        int luaChon ;
        do {
            System.out.println("===================THỐNG KÊ LOẠI KHUYẾN MÃI====================");
            System.out.println("1. Khuyến mãi theo hình thức giảm giá phần trăm!");
            System.out.println("2. Khuyến mãi thep hình thức giảm giá trực tiếp!");
            System.out.println("3. Khuyến mãi theo hình thức mua tặng! ");
            System.out.println("0. Quay lại.");
            luaChon = Validation.luaChon(sc , "Nhập lựa chọn: " , 0 , 3 ) ;
            switch (luaChon)
            {
                case 1:
                    System.out.println("Danh sách khuyến mãi giảm giá theo phần trăm!");
                    thongKeCacloaiKhuyenMai("Giảm theo phần trăm");
                    break;
                case 2:
                    System.out.println("Danh sách khuyến mãi giảm giá trực tiếp!");
                    thongKeCacloaiKhuyenMai("Giảm theo trực tiếp!");
                    break;
                case 3:
                    System.out.println("Danh sách khuyến mãi mua tặng!");
                    thongKeCacloaiKhuyenMai("Khuyến mãi mua tặng");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        }
        while ( luaChon != 0 ) ;
    }
    public void thongKeTrangThaiKhuyenMai( Scanner sc )
    {
        boolean flag ;
        int tongKhuyenMai ;
        int luaChon ;
        do {
            System.out.println("===================THỐNG KÊ TRẠNG THÁI KHUYẾN MÃI====================");
            System.out.println("1. Danh sách khuyến mãi đang hoạt động!");
            System.out.println("2. Danh sách khuyến mãi đã hết hạn!");
            System.out.println("3. Danh sách khuyến mãi chưa bắt đầu! ");
            System.out.println("0. Quay lại.");
            luaChon = Validation.luaChon(sc , "Nhập lựa chọn: " , 0 , 3 ) ;
            switch (luaChon)
            {
                case 1:
                    System.out.println("Danh sách khuyến mãi đang hoạt động!");
                    flag = false ;
                    tongKhuyenMai = 0 ;
                    System.out.println("Đang thống kê...");
                    Validation.danghoatdong(1);
                    for( Khuyenmai km : khuyenmais)
                    {
                        if (km != null && km.dangHoatDong(LocalDate.now()))
                        {
                            System.out.println(km);
                            tongKhuyenMai++ ;
                            flag = true;
                        }
                    }
                    if ( !flag)
                    {
                        System.out.println("Không có khuyến mãi đang hoạt động!");
                    }
                    System.out.println("Tổng khuyến mãi đang hoạt động là: " + tongKhuyenMai);
                    break;
                case 2:
                    System.out.println("Danh sách khuyến mãi đã hết hạn!");
                    flag = false ;
                    tongKhuyenMai = 0 ;
                    System.out.println("Đang thống kê...");
                    Validation.danghoatdong(1);
                    for( Khuyenmai km : khuyenmais)
                    {
                        if (km != null && km.isTonTai() && km.kiemTraHetHanKhuyenMai())
                        {
                            System.out.println(km);
                            tongKhuyenMai++ ;
                            flag = true ;
                        }
                    }
                    if ( !flag)
                    {
                        System.out.println("Không có khuyến mãi đã hết hạn!");
                    }
                    System.out.println("Tổng khuyến mãi đã hết hạn là: " + tongKhuyenMai);
                    break;
                case 3:
                    System.out.println("Danh sách khuyến mãi chưa bắt đầu!");
                    flag = false ;
                    tongKhuyenMai = 0 ;
                    System.out.println("Đang thống kê...");
                    Validation.danghoatdong(1);
                    for( Khuyenmai km : khuyenmais)
                    {
                        if (km != null && km.isTonTai() && km.chuaHoatDong())
                        {
                            System.out.println(km);
                            tongKhuyenMai++ ;
                            flag = true ;
                        }
                    }
                    if ( !flag)
                    {
                        System.out.println("Không có khuyến mãi chưa bắt đầu!");
                    }
                    System.out.println("Tổng khuyến mãi chưa bắt đầu là: " + tongKhuyenMai);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        }while (luaChon != 0 ) ;
    }

    public void quanLyDanhSach(Scanner sc)
    {
        int luaChon ;
        do {
            System.out.println("===================QUẢN LÝ KHUYẾN MÃI===================");
            System.out.println("1. Nhập danh sách khuyến mãi!");
            System.out.println("2. Xuất danh sách khuyến mãi!");
            System.out.println("3. Thêm khuyến mãi!");
            System.out.println("4. Thêm k khuyến mãi!");
            System.out.println("5. Sửa khuyến mãi!");
            System.out.println("6. Xóa khuyến mãi!");
            System.out.println("7. Tìm kiếm khuyến mãi!");
            System.out.println("8. Thống kê theo loại!");
            System.out.println("9. Thống kê theo trạng thái!");
            System.out.println("10. Xử lý khuyến mãi hết hạn!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 10);
            switch (luaChon)
            {
                case 1: {
                    taoDanhSach(sc);
                    break;
                }
                case 2: {
                    xuatDanhSach();
                    break;
                }
                case 3: {
                    them(sc);
                    break;
                }
                case 4: {
                    int k = Validation.inputInt(sc, "Nhập số phần tử cần thêm: ");
                    themKSanPhamKM(sc, k);
                    break;
                }
                case 5: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã khuyến mãi: ", Validation.MaKM_REGEX, "Mã khuyến mãi không đúng định dạng!");
                    sua(ma, sc);
                    break;
                }
                case 6: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã khuyến mãi: ", Validation.MaKM_REGEX, "Mã khuyến mãi không đúng định dạng!");
                    xoa(ma);
                    break;
                }
                case 7: {
                    timKhoa(sc);
                    break;
                }
                case 8: {
                    thongKeCacLoaiKhuyenMai(sc);
                    break;
                }
                case 9: {
                    thongKeTrangThaiKhuyenMai(sc);
                    break;
                }
                case 10: {
                    inDanhSachKhuyenMaiHetHan(sc);
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        } while (luaChon != 0);
    }
}
