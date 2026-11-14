# Đọc và ghi dữ liệu chương trình

Chạy `Main` với thư mục làm việc là gốc dự án. Dữ liệu hiện tại được lưu thành các file riêng trong `src/luutru/hien-tai/`:

| File | Đối tượng/nội dung |
|---|---|
| `SanPham.txt` | `QuanLySanPham`: thực phẩm, tiêu dùng, điện tử, tồn kho, thuế, hạn dùng |
| `NhanVien.txt` | `DSNhanVien`: nhân viên thuộc các vai trò, lương, phân ca, trạng thái |
| `KhachHang.txt` | `DSKhachHang`: địa chỉ, điểm, hạng thành viên, trạng thái xóa mềm |
| `DoiTac.txt` | Dịch vụ — `Danhsachdoitac`, gồm các đối tượng `Nhacungcap` |
| `KhuyenMai.txt` | Dịch vụ — `Danhsachkhuyenmai`, gồm ba loại khuyến mãi hiện có |
| `SanPhamKhuyenMai.txt` | Dịch vụ — `DanhSachSPKM`, liên kết mã sản phẩm và mã khuyến mãi |
| `HoaDonBan.txt` | `DanhSachHoaDonBan`, chi tiết, ngày lập và phương thức/trạng thái thanh toán |
| `HoaDonNhap.txt` | `DanhSachHoaDonNhap`, chi tiết, trạng thái duyệt và nhập kho |
| `CongNo.txt` | `DanhSachCongNo`, hạn trả, trạng thái và thông tin thanh toán |
| `NhanVienDangNhap.txt` | Bốn đối tượng vai trò của giao diện, gồm trạng thái tiền đầu ca thu ngân |

Mỗi file chứa danh sách của một nhóm đối tượng, không phải một file cho từng sản phẩm/nhân viên riêng lẻ. Địa chỉ nằm trong đối tượng khách hàng/nhân viên; chi tiết hóa đơn nằm cùng hóa đơn. Hiện chưa có lớp tên `DichVu`; dữ liệu thuộc package `dichvu` được lưu trong ba file dịch vụ ở bảng trên.

## Luồng hoạt động

1. `Main` gọi `GiaoDien.docDuLieu(GiaoDien.THU_MUC_DU_LIEU)` trước khi hiện trang chủ.
2. Chương trình đọc từng file và đưa dữ liệu vào danh sách tương ứng.
3. Các vai trò được nối lại với cùng danh sách chính. Sản phẩm của nhà cung cấp được nối theo mã về danh mục chính; bỏ liên kết tới sản phẩm đã bị xóa hẳn. Xóa mềm vẫn được giữ.
4. Sau khi trở về trang chủ từ menu nghiệp vụ và khi chọn `0` thoát, chương trình gọi `ghiDuLieu(GiaoDien.THU_MUC_DU_LIEU)`.
5. Shutdown hook lưu khi JVM tắt theo cách có chạy hook, chẳng hạn Ctrl+C.

Không bảo đảm lưu được thay đổi đang làm dở khi mất điện hoặc tiến trình bị Kill/Stop cưỡng bức. Nên trở về menu và chọn `0` khi kết thúc phiên làm việc.

## Cách đọc/ghi từng file

Ví dụ bên trong hàm nạp dữ liệu:

```java
QuanLySanPham dsSanPham = DataBase.docDoiTuong(
        QuanLySanPham.class, "src/luutru/hien-tai/SanPham.txt");
```

`DataBase.ghiDoiTuong(...)` và `docDoiTuong(...)` dùng `VanBanDoiTuong` để ghi/đọc văn bản XML UTF-8. Mở file TXT sẽ thấy tên lớp, tên thuộc tính và giá trị, kể cả tiếng Việt. Cấu trúc này giữ được mảng, danh sách, ngày tháng, lớp con và tham chiếu dùng chung. Mỗi file cũng chứa bộ đếm mã để tránh trùng mã khi mở lại. Constructor tạo đối tượng lúc đọc không làm tăng bộ đếm đã lưu.

Chương trình chỉ dùng TXT/XML. Đã bỏ các khai báo `Serializable`, `serialVersionUID` và nhánh đọc DAT nhị phân cũ. Các file TXT hiện có không thay đổi cấu trúc và vẫn được đọc bình thường.

Tham chiếu tới các danh sách dùng chung trong `QuanLy`, `QuanKho`, `ThuNgan` được đánh dấu `transient` để không ghi lặp cả kho dữ liệu vào file nhân viên. Sau khi đọc phải nối lại các danh sách như `GiaoDien.lienKetDuLieu()` đang làm. Hãy dùng `GiaoDien.docDuLieu(...)` để nạp một phiên đầy đủ.

## Giữ dữ liệu khi lưu nhiều file

`LuuTruNhieuFile.ghiDuLieu(...)` ghi đủ 10 file vào thư mục tạm `src/luutru/dang-ghi-...`. Chỉ khi tất cả ghi xong mới đổi `hien-tai` thành `ban-truoc`, rồi đưa bộ mới vào `hien-tai`. Nếu chuyển thư mục lỗi, chương trình khôi phục bộ cũ. Nếu lần chạy trước dừng giữa hai bước đổi tên, lần mở tiếp theo tự khôi phục từ `ban-truoc`.

`src/luutru/ban-truoc/` là bản sao phiên lưu liền trước. Nếu bộ hiện tại thiếu file hoặc file hỏng, chương trình báo lỗi và không tạo dữ liệu rỗng ghi đè. Muốn khôi phục thủ công: đóng chương trình, giữ bản sao bộ lỗi rồi thay cả bộ `hien-tai` bằng `ban-truoc`.

## Dữ liệu cũ và các file khác

Nếu chưa có bộ dữ liệu, chương trình tạo danh sách rỗng. Nếu bộ hiện tại đã có nhưng thiếu một file TXT, chương trình báo rõ file thiếu và không tự thay bằng dữ liệu rỗng. Chương trình không tìm hoặc đọc DAT nữa.

Các hàm xuất văn bản/CSV cũ vẫn được giữ. CSV/TXT cũ không được tự nhập vì chưa lưu đầy đủ đối tượng lồng nhau. `getFilePath()` chỉ cung cấp đường dẫn, không tự thực hiện lưu/nạp.

- `src/luutru/YeuCau.txt`: yêu cầu nhân viên, ghi ngay và duyệt qua `YeuCau`, `DSYeuCau`.
- `src/luutru/LichSuCa.txt`: lịch sử ca, tự tạo và ghi nối tiếp khi thay ca.
- `src/luutru/NhanVien.txt`, `src/luutru/KhachHang.txt`: các bản xuất CSV của nghiệp vụ cũ.
- `src/luutru/QR/*.png`: ảnh QR nếu nghiệp vụ thanh toán chuyển khoản tạo ảnh.

Khi sao lưu, giữ toàn bộ thư mục `src/luutru/`.

## Kiểm tra

Biên dịch toàn bộ `src` và `test/LuuTruChuongTrinhTest.java` vào thư mục output, rồi chạy hai lệnh trong hai JVM với thư mục dữ liệu thử chưa tồn tại:

```text
java -cp "<output>;lib/*" LuuTruChuongTrinhTest write <thu-muc-thu>
java -cp "<output>;lib/*" LuuTruChuongTrinhTest read <thu-muc-thu>
```

Kiểm tra gồm các file riêng, lớp con, tham chiếu dùng chung, địa chỉ, Unicode, hóa đơn/chi tiết, công nợ, bộ đếm mã, danh sách rỗng, file hỏng/thiếu, lỗi ghi một file trong bộ và khôi phục sau bước đổi thư mục.

## Vị trí thực tế trong dự án

Các file đã được tạo trong `src/luutru/hien-tai/`. Các file văn bản cũ cũng đã được chuyển về `src/luutru/`, giữ nguyên nội dung. `LichSuHoaDon.txt` và `YeuCauDuyet.txt` là dữ liệu lịch sử cũ được giữ lại.

`utils.DuongDanLuuTru` tìm thư mục gốc dự án từ thư mục làm việc hoặc vị trí lớp đã biên dịch, rồi dùng `src/luutru`. Nếu chạy bản build độc lập ngoài cây dự án, dùng `src/luutru` bên trong thư mục làm việc. Trong IntelliJ có thể nhấn Reload from Disk/Synchronize để thấy các file mới.
