# Những thay đổi khi đơn giản hóa đọc/ghi TXT

Mục tiêu là dễ đọc, tự viết lại và giải thích trong đồ án. Định dạng mới không cố xử lý mọi loại nội dung chuỗi.

| Trước | Sau |
|---|---|
| `DongDuLieu.Cot`, đọc tuần tự qua `soDai()`, `doiTuong()`... | Từng lớp dùng `String[] p = dong.split(";", -1)` và `Long.parseLong(p[i])` rõ ràng |
| `ghiCot()`/`docCot()` và `toDongFile()` mặc định | Từng lớp tự viết `toDongFile()` và `tuDongFile()` |
| `DinhDangBanGhi` nhận diện mọi loại đối tượng | Xóa lớp này; lớp cha chỉ phân biệt các lớp con của chính nó |
| `BanGhiFile` có nhiều hàm và cách ghép cột mặc định | Interface chỉ có `String toDongFile()` |
| Escape dấu `;`, xuống dòng, gạch chéo ngược và `\N` | Không dùng escape; chuỗi có `;`, CR/LF bị từ chối; gạch chéo ngược là ký tự bình thường |
| Phân biệt null với chuỗi rỗng | Cả hai ghi thành ô rỗng |
| Địa chỉ nhúng thành bản ghi con trong một ô | Trải năm thành phần địa chỉ thành năm cột |
| Danh sách sản phẩm nhà cung cấp/yêu cầu quản lý nhúng trong một ô | Ghi các dòng `SPCC`/`YCQUANLY` riêng |
| Thanh toán nhúng trong một ô hóa đơn | Ghi dòng `THANHTOAN` riêng |
| Callback/lambda cho ghi và đọc từng dòng | Lớp danh sách tạo `ArrayList<String>`; dùng vòng `for` đọc các dòng từ `TepVanBan` |
| Đếm bản ghi và kiểm tra `#KETTHUC` | Bỏ footer và lượt đếm lại file |

Phần chia sẻ còn lại là các hàm nhỏ: ghép chuỗi, đọc ngày, kiểm tra boolean/số, thuộc tính chung của lớp cha và mở file UTF-8. `DataBase.tuDongFile()` chỉ phục vụ API đọc cũ; luồng danh sách mới gọi thẳng `SanPham.tuDongFile()`, `Khuyenmai.tuDongFile()`...

## Những kiểm tra vẫn giữ

- UTF-8, ngày lịch hợp lệ và hỗ trợ ngày hết hạn trong lịch sử.
- Số không làm tròn; từ chối số âm, NaN/vô cực và tỷ lệ ngoài khoảng.
- Giữ ô rỗng cuối bằng `split(";", -1)`, kiểm tra số cột và loại dòng.
- Kiểm tra trùng mã và quan hệ hóa đơn–chi tiết/nhà cung cấp–sản phẩm.
- Đọc toàn bộ thành danh sách tạm; có lỗi không thay danh sách đang dùng.
- Giữ bộ đếm mã; constructor lúc đọc không cấp mã mới, lỗi đọc khôi phục bộ đếm.
- Ghi file tạm rồi thay file cũ. `LuuTruNhieuFile` vẫn lưu đủ mười file theo một phiên và giữ bản trước.
- API ghi đơn đối tượng từ chối hóa đơn, quản lý và nhà cung cấp có danh sách con; phải lưu qua lớp danh sách để tránh bỏ mất dữ liệu liên quan.

## Giới hạn chấp nhận để code gọn hơn

1. Không lưu trực tiếp `;` hoặc xuống dòng trong chuỗi mới. Chương trình báo lỗi, giữ file cũ; không tự cắt bỏ hay thay nội dung.
2. Không phân biệt `null` với chuỗi rỗng. Địa chỉ null được khôi phục thành địa chỉ có năm ô rỗng.
3. Không phát hiện được việc một dòng bản ghi hoàn chỉnh bị xóa khỏi file, vì đã bỏ footer đếm dòng. Dòng còn lại sai cột/sai kiểu vẫn bị báo lỗi.
4. Đọc các dòng vào bộ nhớ rồi mới tạo đối tượng, phù hợp dữ liệu nhỏ của đồ án.
5. Thứ tự cột vẫn cố định. Khi thêm thuộc tính cần sửa đồng bộ hàm ghi và hàm đọc.
6. Thông báo lỗi định dạng ở tầng đối tượng có tên file và nguyên nhân; không còn một bộ theo dõi số dòng vật lý chung cho mọi loại lỗi.

## Chỉ dùng TXT dạng dòng

- Đã bỏ `VanBanDoiTuong`, `XmlDoiTuongCu`, `DocTxt2Cu` và các nhánh đọc bản lưu tổng quát.
- Các hàm `docFile()` của lớp danh sách đọc thẳng TXT theo cột. `DataBase.docDoiTuong()` chỉ điều phối tới bộ TXT hiện tại.
- File dữ liệu đang dùng, bản trước và các file tạm cũ đã được chuyển sang TXT. Các bản gốc nằm ngoài thư mục dữ liệu ứng dụng tại `out/txt-co-dien/before/src/luutru`.
- Yêu cầu được lưu bằng chuỗi bình thường, đã bỏ Base64 trong lớp giữ bản ghi yêu cầu.
- Xem [phạm vi khôi phục và các hàm đã sửa](DOC_GHI_TXT_CO_DIEN.md).

## Kiểm tra và vị trí hàm

- Biên dịch toàn bộ `src` và các test lưu trữ/nghiệp vụ còn khớp API: đạt.
- `DinhDangDongTest`: 72 kiểm tra đạt. `LuuTruChuongTrinhTest`: ghi và đọc qua hai JVM đạt.
- `ChuyenDoiTxtTest`: đọc bộ TXT hiện tại, mở/lưu lần hai và so sánh đủ mười file: đạt.
- `QuanLyInterfaceTest`: 76 kiểm tra trên mười lớp danh sách đạt; `MaDaCapTest`: 22 kiểm tra mã đạt. Các test chạy trên bản sao dữ liệu, không sửa dữ liệu gốc.
- Một số test cũ trong `HoaDonPersistenceTest`, `MainTest_HoaDon`, `person/PersonPackageTest` vẫn tham chiếu API không còn tồn tại; không tính những test đó là đã đạt.

Mở [vị trí hàm và số dòng](VI_TRI_HAM_DOC_GHI_TXT.md) và [cấu trúc cột](CAU_TRUC_FILE_TXT.md) để kiểm tra từng lớp. Dữ liệu TXT dạng dòng hiện nằm ngay trong `src/luutru/hien-tai/`.
