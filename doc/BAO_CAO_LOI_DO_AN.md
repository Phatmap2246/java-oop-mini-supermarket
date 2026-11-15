# Báo cáo rà soát lỗi đồ án siêu thị

Ngày rà soát: 14/11/2026. Phạm vi: mã nguồn `src`, các bài kiểm thử `test`, đọc/ghi
TXT/XML, sản phẩm, khuyến mãi, kho, hóa đơn, nhân sự và công nợ. Chưa sửa các lỗi
bên dưới. Danh sách ghi lại 38 vấn đề xác định được trong lần rà soát này, không
khẳng định rằng kiểm thử hữu hạn có thể phát hiện mọi lỗi của chương trình.

Cập nhật sau lượt sửa nhập mã: regex NCC ở E02 đã được sửa; các chỗ nhập mã để
truy vấn đã dùng kiểm tra bộ đếm 5 tham số. Các mục bên dưới ghi nhận hiện trạng
tại thời điểm rà soát, không phải tất cả đều còn tồn tại sau các lượt sửa tiếp theo.

## Kết quả kiểm tra

- Mã nguồn chính biên dịch được. Các cảnh báo `this-escape` khi gọi hàm có thể
  override trong constructor chưa được tính là lỗi thực thi đã xác nhận.
- `test/UnitTest.java`: 4 kiểm thử JUnit qua.
- `test/QuanLyInterfaceTest.java`: 76 kiểm tra qua.
- `test/LuuTruChuongTrinhTest.java`: ghi và đọc lại qua hai JVM riêng đều qua.
- Ba bài kiểm thử cũ có tổng cộng 25 lỗi biên dịch: `HoaDonPersistenceTest.java`,
  `MainTest_HoaDon.java`, `person/PersonPackageTest.java`.
- Đã viết/chạy các tình huống tái hiện riêng trong thư mục tạm cho lỗi tiền,
  khuyến mãi, kho, khoảng ngày, phân ca và lưu trữ. Không dùng dữ liệu thật để
  thực hiện các thao tác sửa/xóa/thanh toán trong các thử nghiệm này.
- Các kiểm tra interface/lưu trữ qua không có nghĩa mọi quy tắc nghiệp vụ đã đúng.

P1: nên sửa trước khi thử mua bán, nhập kho hoặc dùng số liệu báo cáo.
P2: sai chức năng, dữ liệu hoặc xử lý đầu vào; cần sửa để hoàn thiện bài.
Những hàm TXT/CSV cũ được ghi rõ vì không phải luồng lưu XML chính của `Main`.

## 1. Biên dịch và kiểm thử

| Mã | Mức | Vị trí | Lỗi và bằng chứng |
|---|---|---|---|
| E01 | P2 | `test/HoaDonPersistenceTest.java:15`, `test/MainTest_HoaDon.java:37`, `test/person/PersonPackageTest.java:142` | Tổng cộng 25 lỗi biên dịch do dùng API cũ: constructor hóa đơn hai tham số, chi tiết hóa đơn bốn tham số, các hàm `themChiTiet`, `getSdtDoiTac`, `getDanhSachChiTiet`, tìm hóa đơn theo SĐT, các hàm kế toán nhận số ngày và một số hàm kho cũ. Không phải 25 lỗi trong `Main`; đây là các test không còn tương thích. |

## 2. Nhập hàng và tồn kho

| Mã | Mức | Vị trí | Lỗi và cách tái hiện |
|---|---|---|---|
| E02 | P1 | `src/hoadon/HoaDonNhapHang.java:74`, `nhapHoaDon(Scanner, QuanLySanPham, Danhsachdoitac)` | Regex `NCC\\%d03` không khớp `NCC001`. `%d` là cú pháp định dạng số, không phải regex chữ số. Chức năng thêm hóa đơn nhập thông thường không vượt qua bước nhập NCC với mã hợp lệ. Cần dùng quy tắc mã NCC đang có. |
| E03 | P1 | `src/hoadon/DanhSachHoaDonNhap.java:919`, `capNhatKho` | Không bỏ qua hóa đơn `isDaNhapSP() == true`. Thử tồn 10, hóa đơn đã nhập số lượng 3: gọi cập nhật lần một thành 13, lần hai thành 16. Chạy lại chức năng làm tăng kho lần nữa. |
| E04 | P1 | `src/hoadon/DanhSachHoaDonNhap.java:919`, `capNhatKho`; `:688`, `ktraHoaDonDaDuyet` | Không kiểm tra `isDuyet()` trước khi cộng kho. `ktraHoaDonDaDuyet()` chỉ in danh sách, không lọc hoặc chặn. Đơn chưa duyệt/chưa nhập số lượng 2 vẫn làm tồn 10 thành 12. |
| E22 | P2 | `src/hoadon/DanhSachHoaDonNhap.java:697`, `kiemTraThongTinSanPham` | `break` đặt ngoài điều kiện mã sản phẩm, nên chỉ kiểm tra dòng chi tiết đầu tiên của mỗi hóa đơn. Nếu SP002 nằm ở dòng thứ hai thì không hiện lịch sử nhập SP002. |
| E29 | P2 | `src/sanpham/QuanLySanPham.java:237`, `:295`, `:342` | Sửa NCC của sản phẩm chỉ thay `maNhaCungCap`, không chuyển sản phẩm giữa hai danh sách `sanPhamCungCap`. NCC cũ vẫn chứa sản phẩm, NCC mới chưa có. Bước nối lại dữ liệu khi mở chương trình không xây dựng lại quan hệ theo mã NCC mới. |
| E38 | P2 | `src/sanpham/TieuDung.java:8`, `src/sanpham/DienTu.java:10` | Constructor mặc định không gán `loaiSanPham`; phần nhập sản phẩm cũng không bổ sung. Đã kiểm tra đối tượng mới của cả hai lớp có loại là chuỗi rỗng. Bộ dữ liệu mẫu che được lỗi này vì công cụ tạo mẫu tự gán loại. |

## 3. Thanh toán, khuyến mãi và tính tiền

| Mã | Mức | Vị trí | Lỗi và cách tái hiện |
|---|---|---|---|
| E05 | P1 | `src/hoadon/HoaDonBanHang.java:114`, `:123` | Gọi thanh toán trước khi gán chiết khấu thành viên. Khách Bạc mua 10.000đ: hệ thống thu 10.000đ, sau đó tổng hóa đơn trở thành 9.700đ. Tiền thu, tiền thừa và số tiền tích lũy không cùng một cơ sở tính. |
| E06 | P1 | `src/hoadon/CTHoaDonBan.java:84`, `:101` | Giảm trực tiếp không chặn theo thành tiền dòng. Mặt hàng 100đ, giảm 500đ cho ra -400đ. Luồng tính tiền có thể nhận tổng âm dù đầu vào giá/số lượng đều không âm. |
| E07 | P1 | `src/hoadon/CTHoaDonBan.java:89` | Khuyến mãi mua tặng không đọc `getSoLuongTang()`. Đổi cấu hình từ mua 2 tặng 1 sang mua 2 tặng 3 cho cùng kết quả. Công thức còn coi hàng tặng như khoản giảm trong lượng đã nhập, chưa phân biệt số lượng mua, tặng và thực xuất kho. |
| E08 | P2 | `src/hoadon/CTHoaDonBan.java:74` | Dùng `>` với `mucChiToiThieu`. Hóa đơn đạt đúng mức tối thiểu vẫn không được áp dụng. Đã tái hiện mức tối thiểu 100đ, mua đúng 100đ không giảm. Nếu đúng ý nghĩa “tối thiểu”, điều kiện phải bao gồm bằng. |
| E09 | P2 | `src/hoadon/CTHoaDonBan.java:153`, `:166`; `src/hoadon/HoaDonBanHang.java:132` | `setSoLuong`, `setDonGia` không cập nhật `thanhTienSauKhuyenMai`, trong khi tổng hóa đơn đọc giá trị cache này. Tạo dòng 1 × 100 rồi đổi số lượng thành 2, tổng vẫn là 100 thay vì 200 nếu chưa gọi tính lại. |
| E10 | P2 | `src/hoadon/HoaDonBanHang.java:48`, `setChietKhau` | Không kiểm tra hữu hạn hoặc khoảng 0–1. `setChietKhau(2)` làm tổng âm; giá trị âm hoặc `NaN` cũng có thể đi vào tính toán. |
| E11 | P2 | `src/hoadon/CTHoaDonNhap.java:38`, `thanhTien`; các phép nhân/cộng tiền tương tự | Phép tính `long` không kiểm tra tràn số. Đã tái hiện số lượng 2, đơn giá `Long.MAX_VALUE` làm thành tiền âm dù cả hai đầu vào dương. Cần giới hạn đầu vào hoặc dùng phép tính có kiểm tra. |
| E12 | P2 | `src/hoadon/HoaDonBanHang.java:127` | In “Thanh toán thành công!” kể cả khi thanh toán tiền mặt trả `false`. Đã thử khách đưa 0đ cho hóa đơn có tiền: hàm trả thất bại nhưng log vẫn báo thành công. |
| E16 | P1 | `src/person/ThuNgan.java:68`; `src/hoadon/DanhSachHoaDonBan.java:517` | Tiền cuối ca cộng tất cả hóa đơn đã thanh toán trong toàn danh sách: không giới hạn ca/ngày/thu ngân, không loại chuyển khoản. Hóa đơn chuyển khoản năm 2000 của NV999 vẫn được tính vào tiền cuối ca của thu ngân hiện tại. |

## 4. Báo cáo kế toán và công nợ

| Mã | Mức | Vị trí | Lỗi và cách tái hiện |
|---|---|---|---|
| E13 | P2 | `src/person/KeToan.java:85`; đối chiếu `src/hoadon/DanhSachHoaDonBan.java:487` | Hai báo cáo doanh thu không cùng quy tắc. Kế toán tính cả hóa đơn chưa thanh toán; báo cáo của danh sách hóa đơn chỉ tính đã thanh toán. Đã tái hiện đơn chưa trả 100đ được cộng vào doanh thu kế toán. Cần xác định doanh thu ghi nhận hay tiền thực thu rồi thống nhất tên/cách tính. |
| E14 | P2 | `src/person/KeToan.java:102` | Chi phí nhập gồm cả hóa đơn chưa duyệt và chưa nhập kho, chưa tách chi phí dự kiến/thực tế. Đã tái hiện đơn đang chờ vẫn được cộng 200đ vào báo cáo. |
| E15 | P2 | `src/hoadon/DanhSachCongNo.java:111`, `doiChieuVaGhiNhanCongNo` | Chỉ kiểm tra tồn tại hóa đơn và đối chiếu số lượng/giá, không kiểm tra duyệt hoặc trạng thái nhập. Đã tạo được công nợ cho hóa đơn cả `isDuyet()` và `isDaNhapSP()` đều `false`, tạo đường đi vòng qua trạng thái nghiệp vụ. |
| E17 | P2 | `src/person/KeToan.java:198`, `:211` | Hai lệnh `printf` ở luồng công nợ cũ thừa placeholder hoặc sai thứ tự/kiểu đối số. Gọi `hienThiDanhSachCongNo` với một hóa đơn nhập gây `IllegalFormatException`. Menu kế toán hiện gọi `DanhSachCongNo` nên không gặp trực tiếp lỗi này, nhưng các hàm công khai cũ vẫn lỗi. |

## 5. Tra cứu khoảng thời gian và hạn sử dụng

| Mã | Mức | Vị trí | Lỗi và cách tái hiện |
|---|---|---|---|
| E18 | P2 | `src/hoadon/DanhSachHoaDonNhap.java:529`; `src/hoadon/DanhSachHoaDonBan.java:283`, `:299` | Tra cứu theo `LocalDate` chưa bao gồm đúng hai ngày biên. Bản nhập dùng `isAfter/isBefore` trên ngày nên loại cả ngày bắt đầu/kết thúc; bản bán kết thúc tại 00:00 ngày cuối nên bỏ phần còn lại của ngày cuối. Bản bán theo ngày giờ cũng loại đúng mốc đầu/cuối. Bản nhập theo `LocalDateTime` hiện đã bao gồm biên nên không tính là lỗi còn tồn tại ở nhánh đó. |
| E19 | P2 | `src/hoadon/DanhSachHoaDonNhap.java:810`, `tongTienNhap`; `src/hoadon/DanhSachHoaDonBan.java:487`, `thongKeDoanhThuTheoKhoang` | Báo cáo khoảng ngày dùng `den.atStartOfDay()` với so sánh mở. Chọn cùng một ngày cho đầu/cuối trả tổng 0 dù trong ngày có hóa đơn. Ngày cuối của khoảng cũng bị loại. |
| E20 | P2 | `src/hoadon/DanhSachHoaDonNhap.java:582`, `timKhoa(Scanner)` | Các nhánh khoảng thời gian đã gọi hàm tra cứu nhưng không cập nhật `flag`. Đã tái hiện cùng một lần tìm: hóa đơn HD999 được in ra, ngay sau đó vẫn báo “Không tìm thấy kết quả phù hợp!”. Hàm tra cứu đang trả `void` nên menu chưa nhận được trạng thái. |
| E21 | P2 | `src/hoadon/DanhSachHoaDonNhap.java:548` | Gọi `hdn.getNgayLap().toLocalDate()` trước khi kiểm tra null. Hóa đơn có `ngayLap == null` gây `NullPointerException`. Constructor đủ tham số và file XML hiện đều có thể tạo dữ liệu ngày null. |
| E23 | P2 | `src/sanpham/QuanLySanPham.java:617`; `src/sanpham/ThucPham.java:96` | Thực phẩm đã hết hạn vẫn xuất hiện trong danh sách sắp hết hạn. Số ngày còn lại bị ép tối thiểu 0 nên thực phẩm hết hạn cũng vượt qua điều kiện tỷ lệ. Đã tái hiện một thực phẩm hết hạn hôm qua nằm trong cả hai nhóm. |
| E24 | P2 | `src/sanpham/ThucPham.java:43`, `:68`; các nhánh sửa ngày của `QuanLySanPham` | Hai setter chỉ chặn null, không ràng buộc ngày sản xuất ≤ hạn dùng. Khi sửa có thể tạo ngày sản xuất sau hạn dùng, trong khi luồng nhập mới lại chặn trường hợp này. |
| E25 | P2 | `src/sanpham/ThucPham.java:89`; `src/utils/Validation.java:39` | Nhập nhiệt độ dùng hàm số nguyên không âm và tiếp tục chặn nhiệt độ âm. Setter lại nhận `double` và cho phép âm. UI không nhập được -5 hay 2.5 dù mô hình có thể lưu các giá trị này; không phù hợp nếu muốn quản lý hàng đông lạnh. |

## 6. Mã định danh, liên kết dữ liệu và nhân sự

| Mã | Mức | Vị trí | Lỗi và cách tái hiện |
|---|---|---|---|
| E26 | P2 | `src/person/KhachHang.java:23` | Định dạng `KH%d03` sai vị trí padding. Số thứ tự 1 tạo `KH103`, không phải `KH001`. Cần đối chiếu với mẫu `KH%03d` đã dùng ở các loại mã khác. |
| E27 | P2 | `src/utils/Validation.java:125`; các chỗ dùng overload kiểm tra bộ đếm | Dùng giá trị số trong mã `< soThuTu` thay cho kiểm tra tồn tại. Khách hàng được nhập mã tùy ý như KH999 có thể có trong danh sách nhưng bị luồng lập hóa đơn từ chối vì bộ đếm nhỏ hơn 999. Ngược lại, số thứ tự nhỏ không chứng minh mã đang tồn tại/đang hoạt động. |
| E28 | P2 | `src/person/DSKhachHang.java:195` | Đổi mã khách hàng không cập nhật tham chiếu trong hóa đơn. Đã đổi KH001 thành KH099; HD001 vẫn mang KH001 và không tìm được khách hàng tương ứng. Cần khóa mã hoặc có quy tắc đổi mã/cập nhật liên kết rõ ràng. |
| E30 | P2 | `src/person/DSNhanVien.java:146`; `src/person/KeToan.java:35` và các lớp con có getter vai trò cố định | Sửa vai trò chỉ thay trường của `NhanVien`, còn getter lớp con trả chuỗi cố định. `new KeToan().setVaiTro("QuanKho")` vẫn có `getVaiTro()` là `KeToan`, trong khi `toString()` hiển thị `QuanKho`. Thống kê/phân ca có thể khác thông tin đang in. |
| E31 | P2 | `src/person/DSNhanVien.java:460`, `:479` | Phân ca chỉ thêm người vào biến cục bộ `nhanVienTrongCa`, rồi bỏ khi hàm kết thúc. Ghi dữ liệu trước/sau phân ca cho nội dung như nhau; không có lịch phân ca để xem lại hoặc khôi phục khi mở chương trình. |

## 7. Đọc/ghi file và đầu vào

| Mã | Mức | Vị trí | Lỗi và cách tái hiện |
|---|---|---|---|
| E32 | P2 | `src/utils/VanBanDoiTuong.java:176`; `src/utils/DataBase.java`, `docDoiTuong` | Đọc XML gán trực tiếp bằng reflection, chỉ kiểm tra cấu trúc/kiểu chứ chưa kiểm tra bất biến nghiệp vụ. Đổi giá trị tồn kho trong TXT từ 10 thành -1, file vẫn nạp thành công dù setter chặn số âm. Cũng chưa có bước kiểm tra thống nhất mã duy nhất và quan hệ giữa các file. Đây là điểm trong bộ lưu trữ được bổ sung trước đó. |
| E33 | P1 khi dùng hàm cũ | `src/hoadon/DanhSachHoaDonBan.java:618`, `docFileText` | Xóa danh sách hiện tại trước khi mở file. Đã thử đọc file không tồn tại: hàm báo lỗi nhưng toàn bộ hóa đơn đang giữ đã bị xóa khỏi bộ nhớ. Không phải nhánh nạp XML chính của `Main`. |
| E34 | P2 | `src/hoadon/DanhSachHoaDonBan.java:596`, `:618` | Cặp TXT thủ công cũ không lưu đủ ngày lập, mã NV, phương thức thanh toán, chiết khấu và loại khuyến mãi. Khi đọc, ngày lập bị thay bằng hiện tại, phương thức mất và chiết khấu về 0. Ghi/đọc một hóa đơn có thông tin đầy đủ đã tái hiện sự mất dữ liệu này. Không phải định dạng TXT/XML chính. |
| E35 | P2 | `src/utils/DataBase.java:137`, `getCSVLine`; `taoObject`, `getDataType` | CSV cũ nối bằng dấu phẩy nhưng không escape dấu phẩy/xuống dòng và không khôi phục đối tượng lồng nhau. Địa chỉ có nhiều thành phần làm lệch cột; đã thử xuất/đọc khách hàng có địa chỉ thì không dựng lại được. Các bản xuất nhân viên/khách hàng cũ vẫn dùng cơ chế này. |
| E36 | P2 | `src/utils/DataBase.java:265`, `:283`; `src/person/DSKhachHang.java:655` | Hàm ghi CSV bắt lỗi rồi chỉ in thông báo, không trả kết quả thất bại. Lớp gọi vẫn có thể thông báo ghi thành công dù file không ghi được. Bộ ghi TXT/XML chính có cơ chế báo lỗi khác nên không nên gộp hai luồng này. |
| E37 | P2 | `src/utils/Validation.java:39`, `:57`, `:115`, `:202` | Các hàm nhập không xử lý kết thúc input thống nhất. `luaChon(new Scanner(""),...)` ném `NoSuchElementException`; các hàm nhập số có thể tiếp tục gọi `nextLine()` trong catch sau khi input đã hết. EOF hoặc một bài kiểm thử thiếu input có thể làm dừng menu bằng exception. |

## Những điểm cần thống nhất yêu cầu, chưa kết luận là lỗi chắc chắn

1. **Hạn sử dụng đúng ngày hôm nay:** `kiemTraSanPhamHong()` coi hôm nay đã hỏng,
   trong khi thống kê tình trạng dùng quy tắc quá hạn trước hôm nay. Cần quyết định
   hàng được dùng hết ngày ghi trên nhãn hay không, rồi thống nhất bán hàng và báo cáo.
2. **Tài khoản nhân viên:** `GiaoDien` dùng tài khoản theo vai trò, mật khẩu hard-code
   và so sánh mật khẩu không phân biệt hoa/thường. Sa thải nhân viên không vô hiệu hóa
   tài khoản vai trò. Nếu mục tiêu chỉ là demo chọn vai trò thì ghi rõ; nếu là đăng nhập
   nhân viên thực thì thiếu liên kết tài khoản–hồ sơ–quyền.
3. **Chuyển khoản:** nhấn Enter là giả lập thành công, không có xác nhận ngân hàng.
   Đây là chức năng mô phỏng đã ghi ngay trong code, không phải tích hợp thanh toán thật.
4. **Hạn dùng theo lô:** mỗi SKU có một hạn dùng và một tồn tổng; nhập lô mới không
   mang ngày sản xuất/hạn dùng riêng. Nếu yêu cầu nhiều lô thì mô hình hiện tại chưa đủ.
5. **Lợi nhuận:** cách tính hiện tại lấy doanh thu trừ toàn bộ tiền nhập hàng trong
   khoảng. Nếu yêu cầu lợi nhuận theo hàng đã bán thì cần mô hình giá vốn; không thể
   kết luận công thức này đúng cho mọi định nghĩa báo cáo mà chưa chốt yêu cầu.

## Thứ tự sửa đề nghị

1. Chặn nhập kho lặp/chưa duyệt; sửa regex NCC.
2. Tính chốt tổng trước khi thu tiền; sửa giảm trực tiếp/mua tặng và kiểm tra tổng không âm.
3. Thống nhất doanh thu, chi phí và tiền cuối ca.
4. Sửa khoảng ngày, `flag` kết quả tìm, lịch sử dòng chi tiết và hạn sử dụng.
5. Sửa mã/liên kết, phân ca, vai trò và kiểm tra dữ liệu đọc từ TXT.
6. Cập nhật hoặc lưu riêng các test và hàm TXT/CSV cũ.

Các lỗi ở phần bộ ghi/đọc XML và một số hàm interface là những điểm còn thiếu trong
phần được bổ sung trước đó; các kiểm thử hiện có chưa bao phủ hết. Báo cáo không quy
mọi lỗi cho code ban đầu, cũng không coi các file dữ liệu đang có là đã bị mất/hỏng.
