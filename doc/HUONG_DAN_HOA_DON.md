# Hướng dẫn và giới thiệu module hóa đơn

Tài liệu giới thiệu cấu trúc hóa đơn trong project Mini Supermarket, cách phối hợp giữa hóa đơn bán và nhập hàng, cách tính khuyến mãi, cập nhật tồn kho và chạy chương trình thử.

## 1. Cấu trúc chính

| Lớp | Vai trò |
|---|---|
| [`HoaDon`](./src/hoadon/HoaDon.java) | Lớp cơ sở trừu tượng, lưu mã, ngày lập, tên đối tác và các dòng chi tiết. |
| [`CTHoaDon`](./src/hoadon/CTHoaDon.java) | Một dòng hàng trên hóa đơn: mã/tên sản phẩm, số lượng, đơn giá, chiết khấu cũ và cờ nhận diện hàng tặng. |
| [`HoaDonBanHang`](./src/hoadon/HoaDonBanHang.java) | Hóa đơn bán lẻ; tính tiền, áp dụng khuyến mãi và thực hiện thanh toán. |
| [`HoaDonNhapHang`](./src/hoadon/HoaDonNhapHang.java) | Phiếu nhập; lưu nhà cung cấp, VAT, vận chuyển và trạng thái duyệt nhập kho. |
| [`QuanLyHoaDon`](./src/hoadon/QuanLyHoaDon.java) | Tạo, tìm, sửa, xóa, thống kê, lưu/đọc hóa đơn và điều phối các bước nghiệp vụ. |
| [`QuanLyTonKho`](./src/sanpham/QuanLyTonKho.java) | Tìm sản phẩm, kiểm tra tồn, trừ tồn khi bán và cộng tồn khi duyệt phiếu nhập. |
| [`KhuyenMaiService`](./src/dichvu/KhuyenMaiService.java) | Tính giảm giá và hàng tặng từ danh sách chương trình khuyến mãi và liên kết sản phẩm. |
| [`ThuNgan`](./src/person/ThuNgan.java) / [`QuanKho`](./src/person/QuanKho.java) | Điểm gọi nghiệp vụ hóa đơn tương ứng với vai trò thu ngân và quản kho. |

`HoaDon` chứa danh sách `CTHoaDon`. `HoaDonBanHang` và `HoaDonNhapHang` kế thừa hóa đơn cơ sở nhưng có cách tính tiền và trạng thái riêng.

## 2. Hóa đơn bán hàng

Khi lập hóa đơn bán, chương trình:

1. Kiểm tra mã hóa đơn theo dạng `HD` kèm chữ số và từ chối mã đã tồn tại trong danh sách hóa đơn hiện tại.
2. Nhận mã sản phẩm. Mã phải có trong danh mục sản phẩm; tên và giá bán lấy từ sản phẩm trong kho, không nhập lại thủ công.
3. Giới hạn số lượng theo tồn kho còn lại. Nếu thêm cùng sản phẩm ở nhiều dòng, số lượng đã nhập ở các dòng trước được tính vào giới hạn.
4. Áp dụng khuyến mãi phù hợp.
5. Thực hiện thanh toán. Chỉ khi thanh toán thành công mới trừ tồn kho. Nếu thanh toán thất bại, tồn kho không đổi và có thể thử thanh toán lại qua chức năng sửa hóa đơn.

Tổng tiền bán được tính như sau:

```text
Tổng tiền hàng = tổng thành tiền các dòng
Tổng thanh toán = tối đa 0, (tổng tiền hàng - tiền giảm khuyến mãi)
```

### Các loại khuyến mãi

Chương trình kết nối [`Danhsachkhuyenmai`](./src/dichvu/Danhsachkhuyenmai.java) với [`DanhSachSPKM`](./src/dichvu/DanhSachSPKM.java), nơi gắn mã sản phẩm với mã khuyến mãi.

- **Giảm phần trăm:** áp dụng lên tiền hàng của sản phẩm được gắn khuyến mãi; không vượt quá mức giảm tối đa.
- **Giảm trực tiếp:** trừ số tiền cấu hình một lần cho chương trình khuyến mãi áp dụng trên hóa đơn.
- **Mua tặng:** khi đủ số lượng mua tối thiểu, thêm dòng quà cùng mã sản phẩm với đơn giá 0. Hàng tặng cũng cần có tồn kho.
- Chương trình phải đang trong thời hạn và tổng tiền hàng hóa đơn phải đạt mức chi tiêu tối thiểu.
- Nhiều chương trình giảm giá có thể cộng dồn nhưng tổng mức giảm không vượt quá tiền hàng.

Chiết khấu theo từng dòng (`CTHoaDon.chietKhau`) còn để tương thích dữ liệu cũ; luồng bán mới không hỏi người dùng nhập chiết khấu thủ công. Hàng tặng được thể hiện riêng để không bị hiểu nhầm là giảm tiền.

## 3. Hóa đơn nhập hàng

Luồng nhập hàng gồm các bước riêng:

1. **Quản kho lập phiếu:** chọn nhà cung cấp, nhập mã `HDN...`, VAT và các mặt hàng/số lượng/giá nhập. Phiếu chuyển sang trạng thái **Chờ NCC báo phí**.
2. **Nhà cung cấp báo vận chuyển:** nhập mã phiếu, chọn xe có sẵn từ các đơn vị vận chuyển và nhập phí. Phiếu chuyển sang **Chờ Quản lý duyệt**.
3. **Quản lý duyệt nhập kho:** chọn chức năng duyệt. Nếu mọi mã sản phẩm trên phiếu đã có trong danh mục và tồn kho có thể cập nhật, số lượng được cộng vào kho và trạng thái chuyển sang **Đã duyệt, đã nhập kho**.

Tổng tiền nhập được tính như sau:

```text
Tổng tiền nhập = tiền hàng + VAT + phí vận chuyển
```

Lập phiếu hoặc báo phí vận chuyển chưa làm thay đổi tồn. Chỉ bước duyệt cuối mới cộng tồn, và phiếu đã duyệt không thể cộng tồn lần nữa.

## 4. Chuẩn bị dữ liệu sản phẩm

Hóa đơn bán yêu cầu danh mục sản phẩm có sẵn. Tồn kho hiện được lấy từ `QuanLySanPham.danhSachSanPham`; menu hóa đơn thử nghiệm hiện không có chức năng tạo sản phẩm. Module sản phẩm cần nạp/thêm dữ liệu trước khi bán.

Ví dụ thêm một sản phẩm tiêu dùng vào danh sách trong mã khởi tạo ứng dụng:

```java
QuanLySanPham.danhSachSanPham.add(
    new TieuDung("SP001", "Nước suối", "NCC001", 5000, 10000, 20)
);
```

Tham số ví dụ lần lượt là mã sản phẩm, tên, mã nhà cung cấp, giá nhập, giá bán và số lượng tồn ban đầu. Chỉ khởi tạo dữ liệu mẫu một lần; không thêm lại sản phẩm mỗi lần mở menu.

Đối với phiếu nhập, mã sản phẩm cũng cần có trong danh mục trước khi duyệt. Hiện tại phiếu nhập ghi tên và giá nhập do người lập nhập; bước duyệt chỉ cộng số lượng vào sản phẩm đã có, không tự tạo sản phẩm hoặc sửa giá nhập trong danh mục.

## 5. Chạy menu hóa đơn thử nghiệm

Từ thư mục gốc project trên macOS/Linux:

```bash
javac -cp "lib/*" -d out $(find src test -name '*.java' -print)
java -cp "out:lib/*" MainTest_HoaDon
```

Các lựa chọn nghiệp vụ trên menu trong [`MainTest_HoaDon`](./test/MainTest_HoaDon.java):

| Lựa chọn | Chức năng |
|---:|---|
| 1 | Tạo hóa đơn bán hoặc nhập |
| 2 | Hiển thị hóa đơn |
| 3–6 | Tìm, sửa hoặc xóa hóa đơn |
| 7 | Thống kê doanh thu/chi phí |
| 8–9 | Lưu hóa đơn ra `hoadon.txt` / đọc hóa đơn từ file |
| 10–11 | Tạo nhà cung cấp/đơn vị vận chuyển và đăng ký xe |
| 12–13 | Quản kho lập phiếu / nhà cung cấp báo xe và phí |
| 14–16 | Tạo khuyến mãi, gắn khuyến mãi với sản phẩm, xem danh sách |
| 17 | Quản lý duyệt phiếu nhập và cộng tồn |
| 0 | Thoát |

Để thử bán, cần chuẩn bị danh mục sản phẩm có tồn kho trước rồi chọn **1 → Hóa đơn bán hàng**. Để thử nhập, cần tạo nhà cung cấp, đơn vị vận chuyển và xe trước; sau đó thực hiện **12 → 13 → 17**.

## 6. Lưu trữ và lưu ý khi tích hợp

- Hóa đơn được ghi bằng file text UTF-8 tại `hoadon.txt` trong thư mục chạy chương trình. Bộ đọc hỗ trợ một số định dạng hóa đơn cũ; file hiện lưu chi tiết hàng, khuyến mãi và thông tin vận chuyển.
- Danh sách sản phẩm/tồn kho, danh sách đối tác/xe và danh sách chương trình khuyến mãi không được lưu bởi file hóa đơn này. Khởi động lại ứng dụng cần nạp lại các dữ liệu đó từ module lưu trữ tương ứng.
- Đọc hóa đơn từ file không tự cập nhật tồn kho. Tránh duyệt lại hóa đơn nhập đã lưu ở trạng thái đã duyệt nếu ứng dụng chưa khôi phục trạng thái/tồn kho nhất quán.
- Hủy hoặc xóa hóa đơn bán đã thanh toán hiện chưa tự hoàn lại tồn kho. Các thao tác điều chỉnh sau thanh toán cần có quy trình hoàn/hủy riêng.
- Menu thử nghiệm mô phỏng vai trò; chưa có đăng nhập và phân quyền người dùng thật.
- Các overload `nhapHoaDon`/`nhapDanhSachHoaDon` cũ được giữ để tương thích. Khi tích hợp vào ứng dụng chính, truyền cùng một `QuanLySanPham`, `DanhSachSPKM` và `Danhsachkhuyenmai` dùng chung cho các vai trò; tránh tạo danh sách mới cho từng hóa đơn vì như vậy không thấy dữ liệu đã cấu hình.
