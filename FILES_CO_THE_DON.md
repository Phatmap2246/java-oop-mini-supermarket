# Các file có thể dọn để dự án gọn hơn

Rà soát theo mã nguồn hiện tại, các chỗ gọi hàm, đường dẫn đọc/ghi và kiểm tra biên dịch.
Danh sách này là đề xuất; chưa xóa các file bên dưới.

## Không có tác dụng trong chương trình hiện tại

| File/thư mục | Kết quả kiểm tra | Cách dọn |
|---|---|---|
| `src/hoadon/QuanLyHoaDon.java` | Toàn bộ nội dung đã comment, không có khai báo lớp đang hoạt động, không được mã nguồn khác gọi | Có thể bỏ khỏi bản nộp; đang dùng `DanhSachHoaDonBan` và `DanhSachHoaDonNhap` |
| `src/sanpham/QuanLyTonKho.java` | Không có lời gọi tới lớp; chỉ còn một import trong `HoaDonBanHang.java` | Nếu bỏ file này, bỏ luôn import `sanpham.QuanLyTonKho` ở `HoaDonBanHang.java` để biên dịch được |
| `src/luutru/.gitkeep` | Chứa đoạn code nhập hàng cũ dưới dạng văn bản, không phải file Java, không được đọc | Có thể bỏ; thư mục đã có nhiều file thật |
| `src/thanhtoan/.gitkeep` | File rỗng, thư mục không có mã nguồn | Có thể bỏ cả thư mục rỗng; các lớp thanh toán hiện ở `src/hoadon` |
| `src/dichvu/.gitkeep`, `src/hoadon/.gitkeep`, `src/interfaces/.gitkeep`, `src/person/.gitkeep`, `src/sanpham/.gitkeep`, `doc/.gitkeep` | Chỉ giữ thư mục rỗng trong Git; các thư mục này đã có nội dung | Có thể bỏ các file giữ chỗ |
| `out/` | Bytecode do IntelliJ sinh ra, không phải mã nguồn | Có thể dọn; IDE sẽ tạo lại khi build |

## Dữ liệu lịch sử cũ không được luồng hiện tại đọc/ghi

| File | Kết quả kiểm tra |
|---|---|
| `src/luutru/LichSuHoaDon.txt` | File hiện rỗng; code ghi lịch sử ca vào `LichSuCa.txt` |
| `src/luutru/YeuCauDuyet.txt` | Có dữ liệu yêu cầu mẫu cũ nhưng không có đường dẫn đọc file này trong code hiện tại; code dùng `YeuCau.txt` |

Hai file này không ảnh hưởng việc chạy hiện tại. Có thể chuyển sang một thư mục
`luu-cu` nếu muốn giữ lịch sử thay vì xóa nội dung.

## Các chương trình kiểm thử cũ cần cập nhật hoặc lưu riêng

Biên dịch riêng ba file sau với các class hiện tại cho ra tổng cộng 25 lỗi.
Chúng không được `Main` gọi, nhưng không nên coi mọi file kiểm thử là file thừa.

| File | Ví dụ không còn khớp với API hiện tại |
|---|---|
| `test/HoaDonPersistenceTest.java` | Constructor `HoaDonBanHang(String, String)`, `CTHoaDonBan` bốn tham số; `getSdtDoiTac`, `getDanhSachChiTiet` |
| `test/MainTest_HoaDon.java` | Các hàm `themHoaDon`, `hienThiDanhSach`, tìm theo số điện thoại và một số tên hàm cũ |
| `test/person/PersonPackageTest.java` | Constructor hóa đơn cũ; các hàm kế toán nhận số ngày thay vì hai mốc ngày; một số hàm quản kho cũ |

Có thể chuyển nhóm này sang tài liệu kiểm thử cũ hoặc cập nhật sau. Không cần
thay đổi code nghiệp vụ đang chạy chỉ để khớp các bài kiểm thử cũ.

## Không nên dọn nhầm

- Giữ `src/luutru/hien-tai/`: dữ liệu TXT/XML chính.
- Giữ `src/luutru/ban-truoc/` nếu cần bản phục hồi lần lưu trước.
- Giữ `YeuCau.txt`, `LichSuCa.txt` trong `src/luutru`: vẫn có hàm đọc/ghi.
- `src/luutru/NhanVien.txt`, `KhachHang.txt` là bản xuất CSV cũ, vẫn được các lớp
  danh sách ghi khi thay đổi dữ liệu. Không phải bản XML chính nhưng cũng chưa phải file không dùng.
- Giữ `DataBase`, `VanBanDoiTuong`, `LuuTruNhieuFile`, `DuongDanLuuTru`, `DateUtil`,
  `Validation`, `YeuCau`: đang phục vụ lưu trữ và nghiệp vụ.
- Giữ `TrongFile`: vẫn được các lớp triển khai và `DataBase` tham chiếu.
- Giữ `QRCodeGenerator` cùng hai JAR ZXing: dùng cho thanh toán chuyển khoản.
- `test/LuuTruChuongTrinhTest.java`, `test/QuanLyInterfaceTest.java`,
  `test/TaoDuLieuMau.java` vẫn dùng được. `test/UnitTest.java` còn biên dịch được
  và dùng JUnit để kiểm tra bộ ghi/đọc CSV.
- Các file `.md`, `src/person/README.txt`, tài liệu trong `doc/` không được JVM
  thực thi nhưng có giá trị hướng dẫn. Có thể gom tài liệu về `doc/` để cây `src` gọn hơn.
- `.idea/`, `.iml` là cấu hình IDE, không phải mã nghiệp vụ. Giữ nếu còn dùng IntelliJ.

## Phần lưu trữ đã dọn trong lần này

Đã bỏ 41 khai báo `serialVersionUID`, bỏ đánh dấu/kiểu tham số `Serializable`,
nhánh đọc `ObjectInputStream`, đường dẫn file gộp và phương án dự phòng DAT.
Đọc/ghi chính chỉ còn TXT/XML; giữ `transient` vì bộ ghi XML vẫn dùng nó để
bỏ qua những tham chiếu cần được nối lại sau khi nạp dữ liệu.
