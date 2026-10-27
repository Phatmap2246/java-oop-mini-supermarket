package utils;
import hoadon.*;
import sanpham.*;
import java.sql.*;
import java.time.LocalDate;
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
        //Bang hoadon
        //constraint:
        // ma hoa don ban hang "^HD\\d+$"
        // ma hoa don nhap hang "^HDN\\d+$"
        // nha san xuat "^NCC\\d+$"
        database.execute("""
                CREATE TABLE hoadon(
                maHoaDon VARCHAR PRIMARY KEY,
                ngayLap DATE,
                tenDoiTac VARCHAR,
                tienKhachDua DOUBLE,
                tienThua DOUBLE,
                tienKhuyenMai DOUBLE,
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
                )
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
       if(res.getString("maHoaDon").matches("^HDN\\d+$")){
           HoaDonNhapHang hd = new HoaDonNhapHang();
           hd.setMaHoaDon(res.getString("maHoaDon"));
           hd.setNgayLap(res.getDate("ngayLap").toLocalDate());
           hd.setTenDoiTac(res.getString("tenDoiTac"));
           hd.setMaNhaCungCap(res.getString("maNhaCungCap"));
           hd.setChiPhiVanChuyen(res.getDouble("chiPhiVanChuyen"));
           hd.setThueVAT(res.getDouble("thueVAT"));
           return hd;
       }
       else if(res.getString("maHoaDon").matches("^HD\\d+$")){
            HoaDonBanHang hd = new HoaDonBanHang();
            hd.setMaHoaDon(res.getString("maHoaDon"));
            hd.setNgayLap(res.getDate("ngayLap").toLocalDate());
            hd.setTenDoiTac(res.getString("tenDoiTac"));
            hd.setTienKhachDua(res.getDouble("tienKhachDua"));
            hd.setTienThua(res.getDouble("tienThua"));
            hd.setTienKhuyenMai(res.getDouble("tienKhuyenMai"));
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
}
