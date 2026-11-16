# Đọc/ghi TXT đơn giản

Dữ liệu mới dùng TXT UTF-8. Mỗi đối tượng là một dòng, các thuộc tính ngăn bởi `;`. Ví dụ:

```text
KMPHANTRAM;KM001;Giảm theo phần trăm;15/10/2026;13/01/2027;0;true;0.1;50000
```

## Cách lần theo code

1. `GiaoDien.docDuLieu()` gọi `docFile()` của từng danh sách khi mở chương trình.
2. Lớp danh sách lấy các dòng bằng `TepVanBan.doc()`, duyệt bằng `for` rồi gọi `tuDongFile()` của đối tượng.
3. `tuDongFile()` dùng `split(";", -1)`, đọc `p[0]`, `p[1]`... và chuyển kiểu bằng `Integer.parseInt()`, `Long.parseLong()`, `Double.parseDouble()`.
4. Khi lưu, `toDongFile()` của mỗi lớp ghép các thuộc tính thành dòng. Hàm `thongTin...File()` chỉ dùng chung phần thuộc tính của lớp cha.
5. Lớp danh sách tập hợp các dòng rồi gọi `TepVanBan.ghi()`. `GiaoDien.ghiDuLieu()` gọi lưu đủ bộ dữ liệu.

Ví dụ dùng riêng danh sách khuyến mãi:

```java
Danhsachkhuyenmai ds = new Danhsachkhuyenmai();
ds.docFile("src/luutru/hien-tai/KhuyenMai.txt");
ds.ghiFile("khuyen-mai-xuat.txt");
```

Không còn `DongDuLieu.Cot`, `ghiCot()`, `docCot()`, factory tổng quát `DinhDangBanGhi`, callback đọc/ghi hoặc bản ghi nhúng trong một ô.

## Các file

Giữ thư mục `src/luutru/hien-tai/`, gồm `SanPham.txt`, `NhanVien.txt`, `KhachHang.txt`, `DoiTac.txt`, `KhuyenMai.txt`, `SanPhamKhuyenMai.txt`, `HoaDonBan.txt`, `HoaDonNhap.txt`, `CongNo.txt`, `NhanVienDangNhap.txt`.

`YeuCau.txt` nằm trong `src/luutru/`, lưu ngay khi thêm/duyệt. `NhanVien.txt`, `KhachHang.txt` ngoài `hien-tai/` là bản xuất của các hàm nghiệp vụ. `LichSuCa.txt` vẫn ghi nối tiếp. Ảnh thanh toán vẫn nằm trong `QR/`.

Ba dòng đầu của file mới:

```text
#SIEUTHI_TXT_3
#NHOM;Danhsachkhuyenmai
#BODEM;9;5;4;7;5;4
```

Bộ đếm theo thứ tự: sản phẩm, nhân viên, khách hàng, hóa đơn, khuyến mãi, nhà cung cấp. Bộ đếm giúp tránh dùng lại mã đã xóa. Dòng trống và chú thích `#` được bỏ qua; `#BODEM` được đọc riêng. Không có dòng `#KETTHUC`.

## Thông tin liên quan ghi thành dòng riêng

```text
HDBAN;HD001;2026-11-12T10:30;NV003;Hoá đơn bán;KH001;true;0.0
CTBAN;HD001;SP001;Sữa tươi;2;32000;true;64000;0;2;0;Không áp dụng
THANHTOAN;HD001;TIENMAT;100000;36000
```

Chi tiết được ghép vào hóa đơn theo mã; dòng hóa đơn phải đứng trước. Nhà cung cấp có các dòng `SPCC`, quản lý có thể có các dòng `YCQUANLY`. Địa chỉ được trải thành năm cột, không nhúng thành một chuỗi có escape. Mở [cấu trúc cột đầy đủ](doc/CAU_TRUC_FILE_TXT.md) để đối chiếu.

Vì các dòng thông tin chung không chứa danh sách con, hãy dùng lớp quản lý để lưu hóa đơn, nhà cung cấp và quản lý. API ghi riêng những đối tượng đó sẽ từ chối để tránh mất dữ liệu liên quan.

## Giữ phần cần thiết, chấp nhận giới hạn đơn giản

- UTF-8; ngày `dd/MM/uuuu` kiểm tra lịch nghiêm ngặt; ngày giờ hóa đơn dùng ISO. Hạn sử dụng lịch sử vẫn đọc được.
- Số không làm tròn, không phụ thuộc locale. Giữ kiểm tra số âm, NaN/vô cực, tỷ lệ và số cột.
- Chuỗi mới không chứa `;` hoặc xuống dòng. Có ký tự không hỗ trợ thì báo lỗi và giữ file cũ; không tự sửa nội dung.
- `null` và chuỗi rỗng đều ghi thành ô rỗng.
- Lớp danh sách chỉ thay dữ liệu sau khi đọc thành công toàn bộ file.
- Đọc các dòng vào bộ nhớ, phù hợp dữ liệu nhỏ của đồ án.
- Đã bỏ footer: không tự phát hiện được trường hợp một dòng hợp lệ bị xóa hoàn toàn.

`TepVanBan.ghi()` ghi file tạm rồi thay file cũ. `LuuTruNhieuFile` vẫn ghi đủ mười file trong thư mục tạm, đổi phiên hiện tại sang `ban-truoc`, rồi đưa phiên mới vào `hien-tai`. Nếu bước đổi tên lỗi, khôi phục bộ trước. Không cam kết chống mọi lỗi phần cứng/mất điện.

## Dữ liệu đang dùng

Ứng dụng chỉ đọc/ghi TXT dạng dòng, không còn bộ đọc XML hoặc đồ thị đối tượng. Các file dữ liệu cũ đang chứa XML đã được chuyển sang TXT, đối chiếu đọc/ghi lại trước khi thay vào `src/luutru`. Nội dung yêu cầu lưu trực tiếp bằng tiếng Việt, không dùng Base64.

Bản gốc trước đợt khôi phục nằm tại `out/txt-co-dien/before/src/luutru`. Đây là bản sao riêng, không được ứng dụng nạp. `Main` và logic nghiệp vụ đã tích hợp từ PR #43 được giữ nguyên.

- [Phạm vi khôi phục và các vị trí đã sửa](doc/DOC_GHI_TXT_CO_DIEN.md).
- [Những giới hạn để code đơn giản](doc/THAY_DOI_DOC_GHI_TXT_DON_GIAN.md).
- [Vị trí các hàm và số dòng](doc/VI_TRI_HAM_DOC_GHI_TXT.md).
- [Cấu trúc dòng dữ liệu](doc/CAU_TRUC_FILE_TXT.md).
