# Cấu trúc TXT đơn giản (phiên bản 3)

Mỗi dòng là một bản ghi, các cột ngăn bởi `;`. Dùng `split(";", -1)` để giữ cột rỗng cuối. `null` ghi thành ô rỗng; không có escape, JSON hoặc bản ghi lồng trong một ô. Chuỗi mới không được chứa `;`, CR hoặc LF.

Ba dòng đầu là chú thích: phiên bản, nhóm dữ liệu và sáu bộ đếm mã. Không có footer:

```text
#SIEUTHI_TXT_3
#NHOM;Danhsachkhuyenmai
#BODEM;9;5;4;7;5;4
KMPHANTRAM;KM001;Giảm theo phần trăm;15/10/2026;13/01/2027;0;true;0.1;50000
```

## Thứ tự cột

| Tiền tố | Lớp | Cột sau tiền tố |
|---|---|---|
| `DIACHI` | `DiaChi` | `soNha`, `duong`, `phuongXa`, `quanHuyen`, `tinhThanhPho` |
| `NHANVIEN` | `NhanVien` | `maNV`, `vaitro`, `luong`, `phuCapTrachNhiem`, `isSaThai`, `email`, `hoten`, `sdt`, `soNha`, `duong`, `phuongXa`, `quanHuyen`, `tinhThanhPho`, `ngsinh` |
| `KHACHHANG` | `KhachHang` | `maKhachHang`, `ngayDangKy`, `tongChiTieu`, `hangThanhVien`, `diemTichLuy`, `isTonTai`, `email`, `hoten`, `sdt`, `soNha`, `duong`, `phuongXa`, `quanHuyen`, `tinhThanhPho`, `ngsinh` |
| `QUANLY` | `QuanLy` | `maNV`, `vaitro`, `luong`, `phuCapTrachNhiem`, `isSaThai`, `email`, `hoten`, `sdt`, `soNha`, `duong`, `phuongXa`, `quanHuyen`, `tinhThanhPho`, `ngsinh` |
| `QUANKHO` | `QuanKho` | `maNV`, `vaitro`, `luong`, `phuCapTrachNhiem`, `isSaThai`, `email`, `hoten`, `sdt`, `soNha`, `duong`, `phuongXa`, `quanHuyen`, `tinhThanhPho`, `ngsinh` |
| `KETOAN` | `KeToan` | `maNV`, `vaitro`, `luong`, `phuCapTrachNhiem`, `isSaThai`, `email`, `hoten`, `sdt`, `soNha`, `duong`, `phuongXa`, `quanHuyen`, `tinhThanhPho`, `ngsinh` |
| `THUNGAN` | `ThuNgan` | `maNV`, `vaitro`, `luong`, `phuCapTrachNhiem`, `isSaThai`, `email`, `hoten`, `sdt`, `soNha`, `duong`, `phuongXa`, `quanHuyen`, `tinhThanhPho`, `ngsinh`, `tongTienDauCa` |
| `THUCPHAM` | `ThucPham` | `maSanPham`, `tenSanPham`, `loaiSanPham`, `maNhaCungCap`, `giaNhap`, `giaBan`, `soLuongTonKho`, `thue`, `tonTai`, `ngaySanXuat`, `hanSuDung`, `nhietDoBaoQuan`, `baoQuan` |
| `DIENTU` | `DienTu` | `maSanPham`, `tenSanPham`, `loaiSanPham`, `maNhaCungCap`, `giaNhap`, `giaBan`, `soLuongTonKho`, `thue`, `tonTai`, `thoiGianBaoHanh`, `chatLieu` |
| `TIEUDUNG` | `TieuDung` | `maSanPham`, `tenSanPham`, `loaiSanPham`, `maNhaCungCap`, `giaNhap`, `giaBan`, `soLuongTonKho`, `thue`, `tonTai`, `thuongHieu`, `noiSanXuat`, `phanLoai`, `luuYAnToan` |
| `NHACUNGCAP` | `Nhacungcap` | `maDoiTac`, `tenDoiTac`, `soDienThoai`, `diaChi`, `email`, `maSoThue`, `loaiDoiTac`, `tonTai`, `nganhHangCungCap`, `hanMucNoCong` |
| `KMPHANTRAM` | `KhuyenMaiPhanTram` | `maKhuyenMai`, `loaiKhuyenMai`, `ngayBatDau`, `ngayKetThuc`, `mucChiToiThieu`, `tonTai`, `phanTramGiamGia`, `mucGiamToiDa` |
| `KMTRUCTIEP` | `KhuyenMaiTrucTiep` | `maKhuyenMai`, `loaiKhuyenMai`, `ngayBatDau`, `ngayKetThuc`, `mucChiToiThieu`, `tonTai`, `soTienGiam` |
| `KMMUATANG` | `KhuyenMaiMuaTang` | `maKhuyenMai`, `loaiKhuyenMai`, `ngayBatDau`, `ngayKetThuc`, `mucChiToiThieu`, `tonTai`, `soLuongMuaToiThieu`, `soLuongTang` |
| `SPKM` | `SanPhamKhuyenMai` | `maSanPham`, `maKhuyenMai`, `tonTai` |
| `HDBAN` | `HoaDonBanHang` | `maHoaDon`, `ngayLap`, `maNhanVien`, `loaiHoaDon`, `maKhachHang`, `daThanhToan`, `chietKhau` |
| `HDNHAP` | `HoaDonNhapHang` | `maHoaDon`, `ngayLap`, `maNhanVien`, `loaiHoaDon`, `maNhaCungCap`, `chiPhiVanChuyen`, `thueVAT`, `Duyet`, `daNhapSP` |
| `CTBAN` | `CTHoaDonBan` | `maSanpham`, `tenSanPham`, `soLuong`, `donGia`, `tonTai`, `thanhTienSauKhuyenMai`, `soLuongTangKem`, `soLuongPhaiTra`, `soTienGiam`, `loaiKhuyenMai` |
| `CTNHAP` | `CTHoaDonNhap` | `maSanpham`, `tenSanPham`, `soLuong`, `donGia` |
| `TIENMAT` | `ThanhToanTienMat` | `tienKhachDua`, `tienThua` |
| `CHUYENKHOAN` | `ThanhToanChuyenKhoan` | `tenNganHang`, `soTaiKhoan`, `maGiaoDich`, `noiDungQR` |
| `YEUCAU` | `YeuCau` | `maNV`, `vaitro`, `noiDung`, `trangThai` |

## Các dòng quan hệ

| Dòng | Ý nghĩa |
|---|---|
| `CTNHAP;maHoaDon;maSP;tenSP;soLuong;donGia` | Chi tiết nhập, sau dòng `HDNHAP` tương ứng |
| `CTBAN;maHoaDon;...các cột chi tiết bán...` | Chi tiết bán, sau dòng `HDBAN` tương ứng |
| `THANHTOAN;maHoaDon;TIENMAT;tienKhachDua;tienThua` | Thanh toán tiền mặt |
| `THANHTOAN;maHoaDon;CHUYENKHOAN;tenNganHang;soTaiKhoan;maGiaoDich;noiDungQR` | Thanh toán chuyển khoản |
| `SPCC;maNCC;THUCPHAM/DIENTU/TIEUDUNG;...các cột sản phẩm...` | Sản phẩm cung cấp, sau dòng nhà cung cấp |
| `YCQUANLY;maQuanLy;YEUCAU;maNV;vaitro;noiDung;trangThai` | Yêu cầu do đối tượng quản lý đang giữ, sau dòng quản lý |
| `CONGNO;maHoaDon;maPhieuNhap;maNCC;tenNCC;soTien;ngayDenHan;ngayThanhToan;nganHang;daThanhToan` | Một khoản công nợ |

Ví dụ hóa đơn:

```text
HDNHAP;HD004;2026-10-05T09:00;NV002;Hoá đơn nhập;NCC002;0;0.0;true;true
CTNHAP;HD004;SP004;Nước giặt 3 lít;50;110000
```

`toDongFile()` của hóa đơn, nhà cung cấp và quản lý tạo dòng thông tin chung. Lớp danh sách chịu trách nhiệm ghi thêm những dòng quan hệ; không dùng API ghi riêng một đối tượng để lưu các danh sách con này.

Ngày là `dd/MM/uuuu`, ngày giờ là ISO. Số dùng `toString()`, không làm tròn hoặc phụ thuộc locale. Thuế sản phẩm và tỷ lệ giảm dùng 0..1, VAT hóa đơn nhập dùng 0..100 theo nghiệp vụ hiện tại.

Dự án chưa có `sanPhamMoi`, thuế nhập hoặc hạn sử dụng riêng trong `CTHoaDonNhap`; cấu trúc giữ đúng thuộc tính hiện có.

File yêu cầu dùng thêm `LOICU;noiDungDongLoi` để giữ dòng lịch sử đã sai định dạng. Riêng dòng này lấy toàn bộ phần sau tiền tố, nên vẫn giữ được dấu `;` của dòng cũ.
