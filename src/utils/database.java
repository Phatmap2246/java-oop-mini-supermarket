package utils;
import hoadon.*;
import sanpham.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

import org.duckdb.DuckDBConnection;
import org.duckdb.DuckDBAppender;
import org.duckdb.DuckDBDriver;
public class database{
   public static DuckDBConnection conn;

    static {
        try {
            Class.forName("org.duckdb.DuckDBDriver");
            conn =  (DuckDBConnection) DriverManager.getConnection("jdbc:duckdb:");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static ResultSet query(String qrs) throws Exception{
       Statement state = conn.createStatement();
       return state.executeQuery(qrs);
    }

    // dùng execute để insert, delete, hoặc các tác vụ sql không trả về kết quả
    public static void execute(String qrs) throws SQLException {
        Statement state = conn.createStatement();
       state.execute(qrs);
    }
    public static void schemas() throws Exception {
        //Bang sanpham
        database.execute("""
               CREATE TABLE sanpham (
               maSanPham VARCHAR PRIMARY KEY,
               loaiSanPham VARCHAR,
               tenSanPham VARCHAR,
               giaGoc DOUBLE,
               giaBan DOUBLE,
               ngayNhap DATE,
               hanSuDung DATE DEFAULT NULL,
               soLuong INTEGER
               );""");
        database.execute("""
                CREATE TABLE thongtinchuyenkhoan(
                tenNganHang VARCHAR,
                soTaiKhoang VARCHAR,
                maGiaoDich VARCHAR PRIMARY KEY,
                noiDungQR VARCHAR,
                );""");
        //Bang hoadon
        //constraint:
        // ma hoa don ban hang "^HD\\d+$"
        // ma hoa don nhap hang "^HDN\\d+$"
        // nha san xuat "^NCC\\d+$""
        //TODO sua table hoa don va ma hoa don
        // phuongThucThanhToan "Chuyen Khoan" va "Tien Mat"
        database.execute("""
                CREATE TABLE hoadon(
                maHoaDon VARCHAR PRIMARY KEY,
                ngayLap DATE,
                tenDoiTac VARCHAR,
                tienKhuyenMai DOUBLE,
                phuongThucThanhToan VARCHAR,
                maGiaoDich VARCHAR REFERENCES thongtinchuyenkhoan(maGiaoDich),
                tienKhachDua DOUBLE,
                tienThua DOUBLE,
                daThanhToan BOOL,
                maNhaCungCap VARCHAR,
                chiPhiVanChuyen DOUBLE,
                thueVAT DOUBLE
                );
                """);
        database.execute("""
                CREATE TABLE cthoadon(
                maHoaDon VARCHAR REFERENCES hoadon(maHoaDon),
                maSanPham VARCHAR REFERENCES sanpham(maSanPham),
                tenSanPham VARCHAR,
                soLuong INTEGER,
                donGia DOUBLE,
                chietKhau DOUBLE
                );
                """);
        database.execute("""
                CREATE TABLE person(
                email VARCHAR,
                hoten VARCHAR,
                sdt VARCHAR,
                diachi STRUCT(soNha VARCHAR, duong VARCHAR, phuongXa VARCHAR),
                ngsinh DATE,
                maNV VARCHAR,
                vaitro VARCHAR,
                luong LONG,
                phuCapTrachNhiem LONG
                );
                """);
    }
    //nạp query vào class sanpham
    public static SanPham napSanPham(ResultSet res) throws Exception {
        if (res.getString("loaiSanPham").equals("ThucPham")) {
            ThucPham sp = new ThucPham();
            sp.setMaSanPham(res.getString("maSanPham"));
            sp.setTenSanPham(res.getString("tenSanPham"));
            sp.setGiaGoc(res.getDouble("giaGoc"));
            sp.setGiaBan(res.getDouble("giaBan"));
            sp.setNgayNhap(res.getDate("ngayNhap").toLocalDate());
            sp.setHanSudung(res.getDate("hanSuDung").toLocalDate());
            sp.setSoLuong(res.getInt("soLuong"));
            sp.setLoaiSanPham(res.getString("loaiSanPham"));
            return sp;

        } else if (res.getString("loaiSanPham").equals("DoGiaDung")) {
            DoGiaDung sp = new DoGiaDung();
            sp.setMaSanPham(res.getString("maSanPham"));
            sp.setTenSanPham(res.getString("tenSanPham"));
            sp.setGiaGoc(res.getDouble("giaGoc"));
            sp.setGiaBan(res.getDouble("giaBan"));
            sp.setNgayNhap(res.getDate("ngayNhap").toLocalDate());
            sp.setSoLuong(res.getInt("soLuong"));
            sp.setLoaiSanPham(res.getString("loaiSanPham"));
            return sp;

        } else if (res.getString("loaiSanPham").equals("DoTieuDung")) {
            DoTieuDung sp = new DoTieuDung();
            sp.setMaSanPham(res.getString("maSanPham"));
            sp.setTenSanPham(res.getString("tenSanPham"));
            sp.setGiaGoc(res.getDouble("giaGoc"));
            sp.setGiaBan(res.getDouble("giaBan"));
            sp.setNgayNhap(res.getDate("ngayNhap").toLocalDate());
            sp.setSoLuong(res.getInt("soLuong"));
            sp.setLoaiSanPham(res.getString("loaiSanPham"));
            return sp;
        }
        System.out.println(res.getString("loaiSanPham"));
        return null;
    }
    public static HoaDon napHoaDon(ResultSet res) throws Exception{
       if(res.getString("maHoaDon").matches(HoaDonNhapHang.MA_HD_NHAP_REGEX)){
           HoaDonNhapHang hd = new HoaDonNhapHang();
           hd.setMaHoaDon(res.getString("maHoaDon"));
           hd.setNgayLap(res.getDate("ngayLap").toLocalDate());
           hd.setTenDoiTac(res.getString("tenDoiTac"));
           hd.setMaNhaCungCap(res.getString("maNhaCungCap"));
           hd.setChiPhiVanChuyen(res.getDouble("chiPhiVanChuyen"));
           hd.setThueVAT(res.getDouble("thueVAT"));
           return hd;
       }
       else if(res.getString("maHoaDon").matches(HoaDonBanHang.MA_HD_BAN_REGEX)){
            HoaDonBanHang hd = new HoaDonBanHang();
            hd.setMaHoaDon(res.getString("maHoaDon"));
            hd.setNgayLap(res.getDate("ngayLap").toLocalDate());
            hd.setTenDoiTac(res.getString("tenDoiTac"));
            hd.setTienKhuyenMai(res.getDouble("tienKhuyenMai"));
            //TODO tìm giải pháp cho chuyển phương thức chuyển khoảng vào sql, sài struct hoặc chứa foreign key vào bảng thông tin chuyển khoảng?
            if(Objects.equals(res.getString("phuongThucThanhToan"), "Chuyen Khoan")){
                PreparedStatement preped = conn.prepareStatement("select * from thongtinchuyenkhoan where maGiaoDich = (?)");
                preped.setString(1,res.getString("maGiaoDich"));
                ResultSet res2 =  preped.executeQuery();
                res2.next();
                hd.setPhuongThucThanhToan(new ThanhToanChuyenKhoan(res2.getString("tenNganHang"),res2.getString("soTaiKhoan")));
            }
            else{
                hd.setPhuongThucThanhToan(new ThanhToanTienMat(res.getDouble("tienKhachDua"),res.getDouble("tienThua")));
            }
                hd.setDaThanhToan(res.getBoolean("daThanhToan"));
            return hd;
       }
       System.out.println("loi");
       return null;
    }
    public static void napVaoBangSanPham(SanPham sp){
        try(DuckDBAppender appender  = conn.createAppender(DuckDBConnection.DEFAULT_SCHEMA,"sanpham")){
            if (sp.getClass() == ThucPham.class) {
                appender.beginRow();
                appender.append(sp.getMaSanPham());
                appender.append("thucPham");
                appender.append(sp.getTenSanPham());
                appender.append(sp.getGiaGoc());
                appender.append(sp.getGiaBan());
                appender.append(sp.getNgayNhap());
                appender.append(((ThucPham) sp).getHanSudung()); //down cast roi lay han su dung
                appender.append(sp.getSoLuong());
                appender.endRow();
            }
            if (sp.getClass() == DoGiaDung.class){
                appender.beginRow();
                appender.append(sp.getMaSanPham());
                appender.append("doGiaDung");
                appender.append(sp.getTenSanPham());
                appender.append(sp.getGiaGoc());
                appender.append(sp.getGiaBan());
                appender.append(sp.getNgayNhap());
                appender.append((LocalDate) null);
                appender.append(sp.getSoLuong());
                appender.endRow();
            }
            if (sp.getClass() == DoTieuDung.class){
                appender.beginRow();
                appender.append(sp.getMaSanPham());
                appender.append("doTieuDung");
                appender.append(sp.getTenSanPham());
                appender.append(sp.getGiaGoc());
                appender.append(sp.getGiaBan());
                appender.append(sp.getNgayNhap());
                appender.append((LocalDate) null);
                appender.append(sp.getSoLuong());
                appender.endRow();
            }
        }
        catch(SQLException e) {
           System.err.println(e.getErrorCode());
        }
    }
    public static ResultSet docFilecsvVaoBang(String tenBang, String pathFile){
       try {
         database.execute("COPY " + tenBang + " FROM " + "'" + pathFile + "'");
         return database.query("SELECT * FROM" + tenBang);
       }
       catch (SQLException e){
           System.err.println(e.getMessage());
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
        System.out.println("loi doc file csv");
       return null;
    }
    public static void ghiVaoFilecsv(String tenBang, String pathFile){
        try {
            database.query("COPY "+ tenBang + " TO "+ "'" + pathFile + "'" + "(HEADER, DELIMITER ',');");
        }
        catch (SQLException e){
            System.err.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //TODO tim cach nap cthoadon vao bang ma khong anh huong toi class
    public static void napVaoBangChiTietHoaDon(CTHoaDon hd){
        try(DuckDBAppender appender = conn.createAppender(DuckDBConnection.DEFAULT_SCHEMA, "cthoadon")){
           appender.beginRow();
           //appender.append(hd.getMa)
        }
        catch(SQLException e){
            System.err.println(e.getErrorCode());
        }
    }
}
