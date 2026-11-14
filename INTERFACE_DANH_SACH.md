# Interface cho các lớp danh sách

`IQuanLy<T>` kế thừa `ITimKiem<T>`. Cả 10 lớp danh sách hiện thực `IQuanLy`
theo đúng kiểu dữ liệu, nên cũng có các hàm tìm kiếm của `ITimKiem`.

| Lớp danh sách | Kiểu T |
|---|---|
| `DSNhanVien` | `NhanVien` |
| `DSKhachHang` | `KhachHang` |
| `DSYeuCau` | `YeuCau` |
| `Danhsachdoitac` | `Doitac` |
| `Danhsachkhuyenmai` | `Khuyenmai` |
| `DanhSachSPKM` | `SanPhamKhuyenMai` |
| `QuanLySanPham` | `SanPham` |
| `DanhSachHoaDonBan` | `HoaDonBanHang` |
| `DanhSachHoaDonNhap` | `HoaDonNhapHang` |
| `DanhSachCongNo` | `DanhSachCongNo.KhoanCongNo` |

## Tên hàm thống nhất

```java
void taoDanhSach(Scanner sc);
void them(Scanner sc);
boolean sua(String ma, Scanner sc);
boolean xoa(String ma);
void xuatDanhSach();
T timMa(String ma);
T timTen(String ten);
T timKhoa(String khoa);
int timViTri(String ma);
```

Các hàm tạo danh sách tiếp tục cách nhập nhiều phần tử của code cũ, không tự xóa
dữ liệu đang có. `sua`/`xoa` trả `false` khi không có mã hoặc không được phép thao tác.
Các nhóm có trạng thái xóa mềm vẫn giữ cách xóa mềm.

`timMa`, `timTen`, `timKhoa` trả đối tượng đầu tiên phù hợp hoặc `null`.
`timViTri` trả vị trí trong danh sách gốc hoặc `-1`; không trả vị trí trong một bản lọc.
Tìm tên và từ khóa không phân biệt hoa/thường; đối tượng đã xóa mềm không được trả về.

Các menu tìm kiếm cũ được đổi tên thành overload `timKhoa(Scanner sc)`, vẫn cho
chọn nhiều tiêu chí và in nhiều kết quả. `DSKhachHang.timDanhSachTheoTen(...)`
giữ chức năng trả nhiều khách hàng, còn `timTen(...)` lấy kết quả đầu tiên.

## Tên cần tìm trong các nhóm đặc biệt

- Khuyến mãi: tìm theo tên loại khuyến mãi vì model chưa có thuộc tính tên riêng.
- Sản phẩm khuyến mãi: tìm tên sản phẩm trong danh mục sản phẩm dùng chung.
- Hóa đơn bán: tìm tên khách hàng hoặc tên mặt hàng trong chi tiết.
- Hóa đơn nhập: tìm tên nhà cung cấp hoặc tên mặt hàng trong chi tiết.
- Công nợ: mã là mã hóa đơn nhập, tên là tên nhà cung cấp. `KhoanCongNo` là bản
  kết quả tra cứu, không thay cấu trúc các danh sách công nợ đã được lưu trong TXT.
- Yêu cầu: model hiện chỉ có mã nhân viên và nội dung. Mã tra cứu là mã nhân viên;
  nếu nhân viên có nhiều yêu cầu thì lấy yêu cầu đầu tiên. `timTen` tìm nội dung.

## Nguồn dữ liệu liên kết

Các lớp cần dữ liệu bên ngoài có hàm `cauHinh(...)`: sản phẩm dùng nhà cung cấp,
sản phẩm khuyến mãi dùng danh mục/khuyến mãi, hóa đơn dùng sản phẩm/khách hàng/đối tác,
công nợ dùng hóa đơn nhập và đối tác. Các tham chiếu này là `transient`.

`GiaoDien.lienKetDuLieu()` tự cấu hình các nguồn khi tạo giao diện và khi đọc TXT.
Khi sử dụng một danh sách độc lập ngoài giao diện, hãy gọi `cauHinh(...)` trước
các thao tác nhập có liên quan. Không thay thế nguồn bị thiếu bằng danh sách giả.

Yêu cầu vẫn ghi vào `YeuCau.txt`; `DSYeuCau.cauHinhFile(...)` cho phép chọn file riêng
khi kiểm thử. Thêm/sửa/xóa yêu cầu giữ lại các dòng lịch sử cũ sai định dạng.

## Kiểm tra

Biên dịch `src`, `test/TaoDuLieuMau.java`, `test/QuanLyInterfaceTest.java` vào output.
Dùng thư mục làm việc thử bên ngoài dự án để các bản xuất TXT cũ không tác động
đến dữ liệu thật:

```text
java -cp "<output>;<thu-muc-lib>/*" TaoDuLieuMau <thu-muc-du-lieu-thu>
java -cp "<output>;<thu-muc-lib>/*" QuanLyInterfaceTest <thu-muc-du-lieu-thu>
```

Bộ kiểm tra gọi qua biến `IQuanLy<T>` trên cả 10 lớp; kiểm tra mã thiếu, tìm kiếm,
xóa mềm, sửa/xóa hóa đơn và công nợ, lưu yêu cầu và đọc lại TXT sau khi thay đổi.
