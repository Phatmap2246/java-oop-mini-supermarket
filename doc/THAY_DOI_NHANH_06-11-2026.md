# Thay đổi trên nhánh ngày 06/11/2026

Tài liệu này ghi nhận các thay đổi đang có trên nhánh `feature/hoadon-module` trong ngày 06/11/2026, so với commit gần nhất trước đó `8257565` (ngày 05/11/2026). Các thay đổi được mô tả dưới đây hiện vẫn ở working tree, chưa được commit.

## 1. Hóa đơn bán hàng và tồn kho

- Khi lập hóa đơn bán, mã sản phẩm phải tồn tại trong danh mục. Tên và giá bán lấy từ sản phẩm đã có; số lượng nhập được kiểm tra với tồn kho, bao gồm cả các dòng trùng mã sản phẩm.
- Chỉ trừ tồn sau khi thanh toán thành công. Nếu thanh toán chưa thành công, tồn không đổi; khi thử thanh toán lại, số lượng tồn được kiểm tra lại.
- Khuyến mãi được áp dụng qua [`KhuyenMaiService`](../src/dichvu/KhuyenMaiService.java), gồm giảm theo phần trăm, giảm trực tiếp và mua tặng. Chương trình phải còn hiệu lực và hóa đơn phải đạt mức chi tiêu tối thiểu.
- Dòng hàng tặng được ghi nhận riêng, giá bằng 0; hàng tặng cũng phải có đủ tồn và bị trừ khỏi kho như hàng bán.
- Luồng lập hóa đơn mới không yêu cầu nhập chiết khấu thủ công. Trường chiết khấu cũ vẫn được giữ để đọc/tương thích dữ liệu trước đây.
- [`ThuNgan`](../src/person/ThuNgan.java) chỉ tính hóa đơn đã thanh toán vào tổng cuối ca.

Các thay đổi chính nằm trong [`CTHoaDon`](../src/hoadon/CTHoaDon.java), [`HoaDon`](../src/hoadon/HoaDon.java), [`HoaDonBanHang`](../src/hoadon/HoaDonBanHang.java), [`QuanLyHoaDon`](../src/hoadon/QuanLyHoaDon.java) và lớp mới [`QuanLyTonKho`](../src/sanpham/QuanLyTonKho.java).

## 2. Khuyến mãi

- Thêm dịch vụ dùng chung để tìm liên kết sản phẩm–khuyến mãi, kiểm tra chương trình và tính mức giảm.
- Bổ sung/điều chỉnh kiểm tra thời hạn, giá trị phần trăm, mức giảm tối đa và giảm trực tiếp.
- Tự tạo các dòng hàng tặng theo số lượng mua đạt điều kiện; nếu không đủ tồn cho phần quà thì không thêm dòng quà đó.
- Thêm thao tác trên menu thử nghiệm để tạo chương trình, gắn mã sản phẩm và xem các liên kết.

Các lớp liên quan: [`KhuyenMaiService`](../src/dichvu/KhuyenMaiService.java), [`Khuyenmai`](../src/dichvu/Khuyenmai.java), [`KhuyenMaiPhanTram`](../src/dichvu/KhuyenMaiPhanTram.java), [`KhuyenMaiTrucTiep`](../src/dichvu/KhuyenMaiTrucTiep.java), [`Danhsachkhuyenmai`](../src/dichvu/Danhsachkhuyenmai.java) và [`DanhSachSPKM`](../src/dichvu/DanhSachSPKM.java).

## 3. Quy trình hóa đơn nhập hai giai đoạn

Quy trình nhập hàng được tách thành các vai trò và trạng thái:

1. Quản kho chọn nhà cung cấp, lập phiếu và nhập danh sách hàng; phiếu chuyển sang **Chờ NCC báo phí**.
2. Nhà cung cấp chọn xe từ đơn vị vận chuyển đã đăng ký và khai báo phí; phiếu chuyển sang **Chờ Quản lý duyệt**.
3. Quản lý duyệt phiếu. Chỉ khi duyệt thành công, số lượng mới được cộng vào kho và phiếu chuyển sang **Đã duyệt, đã nhập kho**.

Phiếu không được cộng tồn lặp lại. Khi duyệt, tất cả sản phẩm trên phiếu phải có trong danh mục và số lượng tồn phải cập nhật được trước khi thay đổi kho. Các lớp liên quan là [`HoaDonNhapHang`](../src/hoadon/HoaDonNhapHang.java), [`QuanLyHoaDon`](../src/hoadon/QuanLyHoaDon.java), [`QuanKho`](../src/person/QuanKho.java) và các lớp đối tác/xe trong [`src/dichvu`](../src/dichvu).

## 4. Đối tác, nhân viên và Validation

- Bổ sung thao tác tạo đơn vị vận chuyển và đăng ký xe để phiếu nhập tham chiếu tới xe/đơn vị vận chuyển có sẵn thay vì tạo lại thông tin vận chuyển trên hóa đơn.
- Cập nhật các lớp nhà cung cấp, xe và đơn vị vận chuyển để hỗ trợ bước báo phí.
- Điều chỉnh một số lớp nhân viên, kế toán và quản lý để tương thích các thay đổi tích hợp.
- Chuyển các quy tắc regex dùng chung về [`Validation`](../src/utils/Validation.java) và cập nhật các nơi gọi để sử dụng lại hằng/validator tập trung.

## 5. Menu thử nghiệm và tài liệu

- Mở rộng [`MainTest_HoaDon`](../test/MainTest_HoaDon.java) với các lựa chọn tạo khuyến mãi, gắn khuyến mãi với sản phẩm, lập phiếu nhập theo vai trò, báo phí vận chuyển và duyệt nhập kho.
- Thêm [`HUONG_DAN_HOA_DON.md`](./HUONG_DAN_HOA_DON.md) để giới thiệu cấu trúc, luồng nghiệp vụ và cách chạy phần hóa đơn.

## 6. Kiểm thử và phạm vi hiện tại

Theo kết quả kiểm thử trong phiên làm việc ngày 06/11:

- Biên dịch toàn bộ mã nguồn `src` và `test` thành công. Có cảnh báo metadata JUnit `org.apiguardian` thiếu, không làm thất bại biên dịch.
- Smoke/regression tests cho khuyến mãi, đọc/ghi hóa đơn, kiểm tra tồn khi bán, thanh toán lại, hàng tặng và duyệt phiếu nhập đã qua.
- `git diff --check` không phát hiện lỗi whitespace.

Một số giới hạn còn lại:

- Phiếu nhập chỉ cộng tồn cho mã sản phẩm đã có; chưa tạo sản phẩm mới từ phiếu.
- Hàng tặng hiện là cùng mã sản phẩm với sản phẩm được gắn chương trình mua tặng; mô hình chưa lưu SKU quà riêng.
- Danh sách khuyến mãi và liên kết sản phẩm–khuyến mãi hiện chưa có cơ chế lưu/đọc bền vững riêng.
- Menu hiện là luồng thử nghiệm console, chưa có đăng nhập/phân quyền thực tế.
- Hủy/xóa hóa đơn bán đã thanh toán chưa tự hoàn lại tồn kho.

Khi tích hợp vào ứng dụng chính, cần truyền dùng chung cùng các đối tượng quản lý sản phẩm, danh sách khuyến mãi, liên kết sản phẩm–khuyến mãi và đối tác; nếu tạo danh sách mới cho từng lần gọi thì dữ liệu đã cấu hình sẽ không được dùng lại.
