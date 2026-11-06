# Tài liệu dự án
///Update ngày 24/10/2026
## Tổng quan

gồm các lớp Java hỗ trợ lưu thông tin người, địa chỉ, kiểm tra ngày tháng và đọc/ghi danh sách dữ liệu JSON. `Main` cung cấp `Scanner` dùng chung để nhập dữ liệu từ bàn phím. Thư viện Gson được khai báo với phiên bản 2.10.1 trong `pom.xml`.

## Các tệp Java

### `Main.java`

- `Main.sc`: đối tượng `Scanner` dùng chung để các lớp khác đọc dữ liệu từ bàn phím.

### `Person.java`

`Person` là lớp trừu tượng chứa các thông tin chung của một người: email, họ tên, số điện thoại, địa chỉ và ngày sinh.

- `getEmail()`, `getHoten()`, `getSdt()`, `getNgsinh()`: trả về lần lượt email, họ tên, số điện thoại và ngày sinh.
- `setEmail(String)`, `setHoten(String)`, `setSdt(String)`, `setNgsinh(LocalDate)`: gán giá trị được truyền vào cho thuộc tính tương ứng.
- `setEmail()`, `setHoten()`, `setSdt()`: yêu cầu nhập từ bàn phím và lặp lại cho đến khi email, họ tên hoặc số điện thoại khớp mẫu kiểm tra trong `Person.REGEX`.
- `getDiachi()`: trả về địa chỉ dưới dạng chuỗi bằng cách gọi `toString()` của đối tượng `Diachi`.
- `setDiachi()`: yêu cầu đối tượng địa chỉ hiện có nhập các thành phần địa chỉ từ bàn phím.
- `setNgsinh()`: yêu cầu nhập ngày sinh và kiểm tra chuỗi bằng `DateUtil.isValidDate()`.
- `nhapThongTin()`: gọi các hàm nhập họ tên, email, số điện thoại, ngày sinh và địa chỉ.
- `xuatThongTin()`: in các thông tin của người ra màn hình.
- `toString()`: ghép họ tên, email, số điện thoại, ngày sinh và địa chỉ thành một chuỗi.
- `getVaiTro()`, `coQuyenThucHien(String)`: các phương thức trừu tượng; lớp con phải cung cấp vai trò và xác định quyền thực hiện hành động.

`Person` có lớp lồng `REGEX` chứa các mẫu và đối tượng `Pattern` dùng để kiểm tra số điện thoại, email và họ tên.

### `Diachi.java`

Lớp biểu diễn địa chỉ gồm số nhà, đường, phường/xã, quận/huyện và tỉnh/thành phố.

- `Diachi()`: khởi tạo các thành phần địa chỉ thành chuỗi rỗng.
- `Diachi(String, String, String, String, String)`: khởi tạo địa chỉ với đầy đủ năm thành phần.
- `Diachi(Diachi)`: sao chép các thành phần từ một đối tượng địa chỉ khác nếu đối tượng đầu vào khác `null`.
- Các hàm `getSoNha()`, `getDuong()`, `getPhuongXa()`, `getQuanHuyen()`, `getTinhThanhPho()`: lấy từng thành phần địa chỉ.
- Các hàm `setSoNha(String)`, `setDuong(String)`, `setPhuongXa(String)`, `setQuanHuyen(String)`, `setTinhThanhPho(String)`: cập nhật từng thành phần địa chỉ.
- `nhapDiaChi()`: đọc năm thành phần địa chỉ từ bàn phím, loại bỏ khoảng trắng ở đầu và cuối.
- `toString()`: ghép các thành phần địa chỉ thành một dòng, phân cách bằng dấu phẩy.
- `xuatDiaChi()`: in địa chỉ bằng cách gọi `toString()`.

### `DateUtil.java`

Lớp tiện ích xử lý ngày tháng theo định dạng `dd/MM/yyyy`.

- `DATE_FORMATTER`: bộ định dạng ngày.
- `DATE_TIME_FORMATTER`: bộ định dạng ngày giờ theo mẫu `dd/MM/yyyy HH:mm:ss`.
- `DateUtil()`: hàm khởi tạo rỗng; các thao tác chính của lớp được cung cấp dưới dạng phương thức tĩnh.
- `parseDate(String)`: chuyển chuỗi ngày hợp lệ thành `LocalDate`; trả về `null` nếu chuỗi không phân tích được.
- `formatDate(LocalDate)`: định dạng ngày thành chuỗi `dd/MM/yyyy`; trả về chuỗi rỗng nếu đầu vào là `null`.
- `isValidDate(String)`: trả về `true` nếu chuỗi ngày có thể được phân tích theo định dạng quy định.

### `JsonUtil.java`

Lớp tiện ích đọc và ghi danh sách đối tượng JSON bằng Gson. Ngày `LocalDate` được chuyển đổi theo định dạng `dd/MM/yyyy`, và JSON khi ghi được trình bày dễ đọc.

- `ghiFile(String, List<T>)`: ghi danh sách vào tệp JSON; trả về `true` khi ghi thành công, `false` khi gặp lỗi I/O.
- `docFile(String, Type)`: đọc tệp JSON theo kiểu danh sách được cung cấp; trả về danh sách rỗng nếu dữ liệu đọc được là `null` hoặc xảy ra lỗi I/O.
- Hàm khởi tạo `JsonUtil()` là riêng tư để sử dụng lớp qua các phương thức tĩnh.

### `REGEX.java`

Tệp hiện khai báo lớp `REGEX` riêng nhưng chưa có mẫu biểu thức chính quy hoặc phương thức nào. Các mẫu kiểm tra đang được khai báo trong lớp lồng `Person.REGEX`.

## Tệp dữ liệu và cấu hình

- `data.json`: dữ liệu JSON mẫu gồm `name`, `age` và `email`.
- `pom.xml`: khai báo dependency Gson phiên bản `2.10.1`.

## Lưu ý

- `Person.setDiachi()` và `Person.getDiachi()` yêu cầu thuộc tính `diachi` đã được khởi tạo trước đó; `Person` hiện không tự khởi tạo địa chỉ.
- `DateUtil.parseDate()` từ chối chuỗi `null`, sai định dạng và ngày không tồn tại; `Person.setNgsinh()` lưu ngày hợp lệ vào thuộc tính `ngsinh`.
- Mẫu email trong `Person.REGEX` chấp nhận dấu chấm phân cách tên miền, ví dụ `vana@example.com`.

## Chạy kiểm thử

`tests\ProjectTests.java` là bộ kiểm thử độc lập, không dùng JUnit. Cần JDK và tệp thư viện Gson 2.10.1. Trong PowerShell, thay giá trị `$gson` bằng đường dẫn tới tệp JAR Gson trên máy:

```powershell
$gson = "duong-dan-den\gson-2.10.1.jar"
New-Item -ItemType Directory -Force .test-output | Out-Null
javac -cp $gson -d .test-output DateUtil.java Diachi.java JsonUtil.java Main.java Person.java REGEX.java tests\ProjectTests.java
if ($LASTEXITCODE -eq 0) {
    java -cp ".test-output;$gson" ProjectTests
}
```

Chương trình kiểm thử in kết quả từng ca `PASS`/`FAIL` và trả mã lỗi khác 0 nếu có ca thất bại. Lần kiểm tra gần nhất có **14 ca đạt, 0 ca thất bại**. `pom.xml` hiện chỉ chứa một đoạn khai báo dependency, chưa phải cấu hình Maven đầy đủ; vì vậy hiện chưa thể chạy bộ test bằng `mvn test`.
## Cập nhật ngày 26/10/2026

- `Person` là lớp trừu tượng chứa thông tin chung; cung cấp nhập liệu, getter/setter và `toString()` để biểu diễn các thông tin chung. Lớp con cài đặt `getVaiTro()`.
- `NhanVien` kế thừa `Person`, bổ sung mã nhân viên, vai trò, lương cơ bản và phụ cấp trách nhiệm. `tinhLuong()` tính tổng lương; `toString()` ghép thông tin chung và riêng của nhân viên.
- `DSNhanVien` quản lý danh sách bằng `ArrayList<NhanVien>`. `taoDS()` nhập nhiều nhân viên, `themNV()` nhập rồi thêm một nhân viên, `xuatDSNV()` in danh sách qua `toString()`, `timNV()` tìm vị trí theo mã, `xoaNV()` xóa theo mã và `suaNV()` cho phép sửa các trường của nhân viên.
- `DiaChi` lưu năm thành phần địa chỉ, hỗ trợ khởi tạo, sao chép, nhập, truy xuất/cập nhật từng thành phần và in địa chỉ qua `toString()`.
- `QuanLy` kế thừa `NhanVien`, trả về vai trò `QuanLy` và cung cấp menu quản lý nhân sự để thêm, xóa, sửa nhân viên.

### Lưu ý

- `DSNhanVien.themNV(Scanner)` tự tạo đối tượng `NhanVien`, nhập thông tin rồi thêm vào danh sách. `DSNhanVien(ArrayList<NhanVien>)` và copy constructor sử dụng danh sách được truyền vào/chia sẻ tham chiếu, không tạo bản sao độc lập.
- `NhanVien.getLuong()` trả về lương cơ bản cộng phụ cấp; `NhanVien.tinhLuong()` cũng tính tổng này. `toString()` hiện hiển thị riêng lương cơ bản và phụ cấp.
- Các thao tác nhập liệu phụ thuộc vào `Scanner` được truyền vào và các hàm kiểm tra trong `Validation`.
- `QuanLy.quanLyNhanSu()` đang điều khiển menu thêm, xóa, sửa danh sách nội bộ của đối tượng quản lý; danh sách này không được chia sẻ tự động với các đối tượng `DSNhanVien` khác.

## Cập nhật ngày 1/11/2026

### `NhanVien.java`

- `taoYeuCau(Scanner)` nhập nội dung yêu cầu, không chấp nhận nội dung rỗng hoặc chứa ký tự `|`, rồi ghi thêm yêu cầu vào `YeuCau.txt`.
- Mỗi dòng có định dạng `maNV|vaiTro|noiDung|ChoDuyet`; yêu cầu được ghi bằng UTF-8 và bổ sung vào cuối tệp.

### `KeToan.java`

- Các danh sách doanh thu và chi phí được dùng để tính tổng doanh thu, tổng chi phí và lợi nhuận. `tinhLoiNhuanRong()` hiện tính lợi nhuận nhân với tỷ lệ VAT 8% và thuế TNDN 20% được khai báo trong lớp.
- `thanhToanCongNo(Scanner, QuanLyHoaDon)` cung cấp menu đối chiếu hóa đơn nhập, xem công nợ nhà cung cấp và thanh toán khoản đến hạn.
- Khi đối chiếu, kế toán nhập phiếu nhập cùng các mặt hàng thực nhận; số lượng và đơn giá được so sánh với hóa đơn trước khi ghi nhận công nợ.
- Công nợ lưu thông tin hóa đơn, phiếu nhập, nhà cung cấp, số tiền, ngày nhận hàng, kỳ hạn 7/15/30 ngày và trạng thái thanh toán trong bộ nhớ của đối tượng `KeToan`.
- Thanh toán hiện được mô phỏng bằng cách nhập ngân hàng, số tài khoản và xác nhận đã chuyển khoản; trạng thái công nợ sau đó được cập nhật trong bộ nhớ.

### `ThuNgan.java`

- `taoHoaDonThanhToan(Scanner)` nhập hóa đơn bán hàng và thêm vào danh sách hóa đơn của thu ngân.
- `tinhTienCuoiCa()` cộng tiền đầu ca với tổng tiền các hóa đơn trong danh sách.
- `thayCa(ThuNgan)` ghi các hóa đơn trong ca ra `hoadon.LichSuHoaDon` theo định dạng các trường phân cách bằng dấu chấm phẩy nếu tệp đã tồn tại, sau đó cập nhật tiền đầu ca cho thu ngân nhận ca.
- Thu ngân kế thừa `taoYeuCau(Scanner)` từ `NhanVien` để gửi yêu cầu vào `YeuCau.txt`; không cần định nghĩa lại hàm này trong `ThuNgan`.

## Cập nhật ngày 6/11/2026

### `QuanLy.java`

- `duyetYeuCau(Scanner)` đọc các dòng UTF-8 từ `YeuCau.txt`, dùng cùng định dạng bốn trường phân cách bằng `|` như `NhanVien.taoYeuCau(Scanner)`.
- Chỉ yêu cầu có trạng thái `ChoDuyet` được đưa ra duyệt. Quản lý nhập `1` để đặt trạng thái `DaDuyet` hoặc `0` để đặt `KhongDuyet`.
- Các dòng sai định dạng được báo lỗi và giữ nguyên; yêu cầu đã xử lý cũng được giữ nguyên. Tệp chỉ được ghi lại khi có yêu cầu được duyệt.
- Nếu chưa có `YeuCau.txt`, hàm thông báo và kết thúc; lỗi I/O được khai báo để nơi gọi xử lý.

### `NhanVien.java`, `ThuNgan.java` và `KeToan.java`

- Hàm tạo yêu cầu được đặt ở lớp cơ sở `NhanVien`, vì vậy các vai trò kế thừa như thu ngân và kế toán dùng chung luồng nhập, định dạng và tệp yêu cầu.
- Mã nhân viên và vai trò được lấy từ đối tượng nhân viên; người dùng chỉ nhập nội dung yêu cầu. Trạng thái ban đầu là `ChoDuyet`.
- Kế toán có thể gửi yêu cầu qua hàm kế thừa, ví dụ đề nghị duyệt thanh toán công nợ hoặc xin hướng xử lý chênh lệch khi đối chiếu hàng nhận. Nghiệp vụ kế toán hiện chưa tự động tạo yêu cầu khi phát sinh các trường hợp này.

## Kiểm thử và đánh giá trước commit

- `test/person/PersonPackageTest.java` là bộ kiểm thử Java độc lập, không cần JUnit. Bộ test bao phủ các luồng chính của 10 lớp Java trong thư mục `person`: địa chỉ, thông tin người/nhân viên, danh sách nhân viên và khách hàng, hạng thành viên/giỏ hàng, tính toán kế toán, tra cứu kho, tạo/duyệt yêu cầu và bàn giao ca thu ngân.
- Chạy từ thư mục gốc dự án bằng PowerShell. Test được chạy trong thư mục tạm riêng để không tạo hoặc ghi đè các tệp dữ liệu của dự án:

```powershell
$repoRoot = (Get-Location).Path
$outDir = Join-Path $env:TEMP "person-package-test-classes"
$runDir = Join-Path $env:TEMP "person-package-test-run"
New-Item -ItemType Directory -Force $outDir, $runDir | Out-Null
javac -encoding UTF-8 -sourcepath src -cp "lib/*" -d $outDir test\person\PersonPackageTest.java
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
Push-Location $runDir
java -cp "$outDir;$repoRoot\lib\*" person.PersonPackageTest
$testResult = $LASTEXITCODE
Pop-Location
if ($testResult -ne 0) { exit $testResult }
```

- Kết quả gần nhất: **60 assertions đạt**. Các test đã phát hiện và sửa lỗi ở `Person.setEmail(String)`, `setHoten(String)` và `setSdt(String)`: trước đây các hàm kiểm tra giá trị cũ, khiến không thể gán giá trị mới khi đối tượng chưa có thông tin.
- Bộ test bao phủ các luồng trọng yếu, không thay thế kiểm thử mọi nhánh nhập liệu tương tác hoặc toàn bộ nghiệp vụ cập nhật kho, hóa đơn và công nợ.
- Biên dịch toàn bộ `src` hiện vẫn thất bại do dấu xung đột Git chưa xử lý trong `src/dichvu/Danhsachdoitac.java` (khoảng dòng 55–62). Đây là vấn đề ngoài phạm vi bộ test `person`; cần giải quyết trước khi xác nhận toàn dự án sẵn sàng build/commit.
- `git diff --check` cũng đang báo khoảng trắng thừa trên một số dòng trong phần thay đổi hiện có của `ThuNgan.java`; cần dọn trước khi commit nếu muốn diff sạch.