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
        database.execute("""
               CREATE TABLE products (
               IDSP INTEGER PRIMARY KEY,
               loaiSanPham VARCHAR,
               tenSanPham VARCHAR,
               giaGoc INTEGER,
               giaBan INTEGER,
               ngayNhap DATE,
               hanSuDung DATE DEFAULT NULL,
               soLuong INTEGER 
               );""");
    }
    //nạp query vào class sanpham
    public static sanPham napSanPham(ResultSet res) throws Exception {
        if (res.getString("loaiSanPham").equals("thucPham")) {
            thucPham sp = new thucPham();
            sp.setIDSP(res.getInt("IDSP"));
            sp.setTenSanPham(res.getString("tenSanPham"));
            sp.setGiaGoc(res.getInt("giaGoc"));
            sp.setGiaBan(res.getInt("giaBan"));
            sp.setNgayNhap(res.getDate("ngayNhap").toLocalDate());
            sp.setHanSudung(res.getDate("hanSuDung").toLocalDate());
            sp.setSoLuong(res.getInt("soLuong"));
            sp.setLoaiSanPham(res.getString("loaiSanPham"));
            return sp;

        } else if (res.getString("loaiSanPham").equals("doGiaDung")) {
            doGiaDung sp = new doGiaDung();
            sp.setIDSP(res.getInt("IDSP"));
            sp.setTenSanPham(res.getString("tenSanPham"));
            sp.setGiaGoc(res.getInt("giaGoc"));
            sp.setGiaBan(res.getInt("giaBan"));
            sp.setNgayNhap(res.getDate("ngayNhap").toLocalDate());
            sp.setSoLuong(res.getInt("soLuong"));
            sp.setLoaiSanPham(res.getString("loaiSanPham"));
            return sp;

        } else if (res.getString("loaiSanPham").equals("doTieuDung")) {
            doTieuDung sp = new doTieuDung();
            sp.setIDSP(res.getInt("IDSP"));
            sp.setTenSanPham(res.getString("tenSanPham"));
            sp.setGiaGoc(res.getInt("giaGoc"));
            sp.setGiaBan(res.getInt("giaBan"));
            sp.setNgayNhap(res.getDate("ngayNhap").toLocalDate());
            sp.setSoLuong(res.getInt("soLuong"));
            sp.setLoaiSanPham(res.getString("loaiSanPham"));
            return sp;
        }

        return null;
    }

}
