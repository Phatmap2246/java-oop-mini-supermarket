# Rút gọn đọc ghi TXT và Main

- Main chỉ gọi GiaoDien.chayChuongTrinh().
- GiaoDien chịu trách nhiệm nạp dữ liệu, mở menu và lưu trong finally khi thoát hoặc EOF.
- Bỏ shutdown hook để luồng chạy dễ theo dõi; đóng cưỡng bức tiến trình không bảo đảm lưu.
- TepVanBan.ghi() kiểm tra các dòng trước, mở BufferedWriter UTF-8 rồi write/newLine; bỏ file tạm và các nhánh đổi tên ở cấp một file.
- Giữ cách chia thuộc tính bằng dấu ;, các hàm toDongFile/tuDongFile và định dạng dữ liệu đang có. Không thêm XML.
- Giữ kiểm tra cơ bản: số cột, số âm, ngày, mã trùng, UTF-8; giữ đọc vào danh sách tạm và cơ chế lưu đủ bộ dữ liệu.
- Khi ghi riêng một file mà ổ đĩa lỗi giữa chừng, file có thể ghi dở. Khi lưu cả chương trình vẫn dùng thư mục tạm của LuuTruNhieuFile.
- Không hỗ trợ dấu ; hoặc xuống dòng trong thuộc tính; null và chuỗi rỗng dùng chung ô rỗng như trước.
- Gom các hàm đọc ghi xuống cuối lớp. KhoanCongNo là lớp lồng: các hàm của nó giữ trong chính lớp lồng để không sai phạm vi.
- Bản sao trước sửa: out/txt-gon/before/src. Dữ liệu thật không bị sửa khi kiểm thử.

## Vị trí nhóm hàm ở cuối lớp

| Lớp | Dòng bắt đầu nhóm |
|---|---|
| `src/GiaoDien.java` | [241](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/GiaoDien.java:241>) |
| `src/dichvu/Danhsachdoitac.java` | [404](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/Danhsachdoitac.java:404>) |
| `src/dichvu/Danhsachkhuyenmai.java` | [698](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/Danhsachkhuyenmai.java:698>) |
| `src/dichvu/DanhSachSPKM.java` | [442](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/DanhSachSPKM.java:442>) |
| `src/dichvu/Doitac.java` | [126](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/Doitac.java:126>) |
| `src/dichvu/Khuyenmai.java` | [168](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/Khuyenmai.java:168>) |
| `src/dichvu/KhuyenMaiMuaTang.java` | [90](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/KhuyenMaiMuaTang.java:90>) |
| `src/dichvu/KhuyenMaiPhanTram.java` | [86](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/KhuyenMaiPhanTram.java:86>) |
| `src/dichvu/KhuyenMaiTrucTiep.java` | [54](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/KhuyenMaiTrucTiep.java:54>) |
| `src/dichvu/Nhacungcap.java` | [102](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/Nhacungcap.java:102>) |
| `src/dichvu/SanPhamKhuyenMai.java` | [85](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/SanPhamKhuyenMai.java:85>) |
| `src/hoadon/CTHoaDon.java` | [67](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/CTHoaDon.java:67>) |
| `src/hoadon/CTHoaDonBan.java` | [232](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/CTHoaDonBan.java:232>) |
| `src/hoadon/CTHoaDonNhap.java` | [45](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/CTHoaDonNhap.java:45>) |
| `src/hoadon/DanhSachHoaDonBan.java` | [648](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/DanhSachHoaDonBan.java:648>) |
| `src/hoadon/DanhSachHoaDonNhap.java` | [1063](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/DanhSachHoaDonNhap.java:1063>) |
| `src/hoadon/HoaDon.java` | [95](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/HoaDon.java:95>) |
| `src/hoadon/HoaDonBanHang.java` | [177](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/HoaDonBanHang.java:177>) |
| `src/hoadon/HoaDonNhapHang.java` | [169](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/HoaDonNhapHang.java:169>) |
| `src/hoadon/ThanhToanChuyenKhoan.java` | [127](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/ThanhToanChuyenKhoan.java:127>) |
| `src/hoadon/ThanhToanTienMat.java` | [47](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/ThanhToanTienMat.java:47>) |
| `src/person/DiaChi.java` | [140](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/DiaChi.java:140>) |
| `src/person/DSKhachHang.java` | [727](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/DSKhachHang.java:727>) |
| `src/person/DSNhanVien.java` | [548](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/DSNhanVien.java:548>) |
| `src/person/DSYeuCau.java` | [189](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/DSYeuCau.java:189>) |
| `src/person/KeToan.java` | [336](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/KeToan.java:336>) |
| `src/person/KhachHang.java` | [212](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/KhachHang.java:212>) |
| `src/person/NhanVien.java` | [213](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/NhanVien.java:213>) |
| `src/person/Person.java` | [193](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/Person.java:193>) |
| `src/person/QuanKho.java` | [202](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/QuanKho.java:202>) |
| `src/person/QuanLy.java` | [356](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/QuanLy.java:356>) |
| `src/person/ThuNgan.java` | [160](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/ThuNgan.java:160>) |
| `src/sanpham/DienTu.java` | [78](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/sanpham/DienTu.java:78>) |
| `src/sanpham/QuanLySanPham.java` | [855](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/sanpham/QuanLySanPham.java:855>) |
| `src/sanpham/SanPham.java` | [180](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/sanpham/SanPham.java:180>) |
| `src/sanpham/ThucPham.java` | [138](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/sanpham/ThucPham.java:138>) |
| `src/sanpham/TieuDung.java` | [92](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/sanpham/TieuDung.java:92>) |

Xem thêm [vị trí từng hàm đọc ghi](VI_TRI_HAM_DOC_GHI_TXT.md).
