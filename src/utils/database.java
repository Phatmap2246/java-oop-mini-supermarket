package utils;
import sanpham.*;
import java.sql.*;
public class database{
   public static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection("jdbc:duckdb:");
        } catch (SQLException e) {
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
               maSanPham INTEGER PRIMARY KEY,
               loaiSanPham VARCHAR,
               tenSanPham VARCHAR,
               giaGoc DOUBLE,
               giaBan DOUBLE,
               ngayNhap DATE,
               hanSuDung DATE DEFAULT NULL,
               soLuong INTEGER 
               );""");
        //Bang hoadon
        database.execute("""
                CREATE TABLE hoadon(
                maHoaDon INTEGER PRIMARY KEY,
                ngayLap DATE,
                tenDoiTac VARCHAR,
                tienKhachDua DOUBLE,
                tienThua DOUBLE,
                maNhaCungCap VARCHAR,
                thueVAT DOUBLE
                );
                """);
        database.execute("""
                CREATE TABLE cthoadon(
                maHoaDon INTEGER REFERENCES hoadon(maHoaDon),
                maSanPham INTEGER REFERENCES sanpham(maSanPham),
                tenSanPham VARCHAR,
                soLuong INTEGER,
                donGia DOUBLE
                )
                """);
    }
    //nạp query vào class sanpham
    public static SanPham napSanPham(ResultSet res) throws Exception {
        if (res.getString("loaiSanPham").equals("ThucPham")) {
            ThucPham sp = new ThucPham();
            sp.setMaSanPham(res.getInt("maSanPham"));
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
            sp.setMaSanPham(res.getInt("maSanPham"));
            sp.setTenSanPham(res.getString("tenSanPham"));
            sp.setGiaGoc(res.getDouble("giaGoc"));
            sp.setGiaBan(res.getDouble("giaBan"));
            sp.setNgayNhap(res.getDate("ngayNhap").toLocalDate());
            sp.setSoLuong(res.getInt("soLuong"));
            sp.setLoaiSanPham(res.getString("loaiSanPham"));
            return sp;

        } else if (res.getString("loaiSanPham").equals("DoTieuDung")) {
            DoTieuDung sp = new DoTieuDung();
            sp.setMaSanPham(res.getInt("maSanPham"));
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

}
