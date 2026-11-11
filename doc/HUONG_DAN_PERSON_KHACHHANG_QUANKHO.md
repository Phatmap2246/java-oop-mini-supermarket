# DOC Hướng dẫn sử dụng folder `person`
*(Bổ sung tài liệu chi tiết cho các lớp: `KhachHang`, `DSKhachHang`, `QuanKho`)*

---

## 1. `KhachHang`
Lớp kế thừa từ `Person`, đại diện và quản lý toàn bộ thông tin của khách hàng thành viên trong siêu thị:

- **Thuộc tính riêng:**
  - `maKhachHang`: Mã khách hàng (bắt đầu bằng `KH` kèm 3-5 chữ số, kiểm tra theo regex).
  - `ngayDangKy`: Ngày đăng ký thành viên (`LocalDate`).
  - `tongChiTieu`: Tổng tiền khách đã mua sắm tích lũy.
  - `hangThanhVien`: Phân hạng thẻ thành viên (`Đồng`, `Bạc`, `Vàng`, `Kim Cương`).
  - `diemTichLuy`: Số điểm thưởng tích lũy được qua các lần mua sắm.
  - `isTonTai`: Trạng thái hoạt động của khách hàng (`true`: đang hoạt động, `false`: đã bị xóa mềm).

- **Phương thức nghiệp vụ:**
  - `capNhatThuHang()`: Tự động cập nhật thứ hạng dựa trên mốc chi tiêu:
    - `>= 10.000.000 VNĐ`: Hạng **Kim Cương**.
    - `>= 5.000.000 VNĐ`: Hạng **Vàng**.
    - `>= 1.000.000 VNĐ`: Hạng **Bạc**.
    - `< 1.000.000 VNĐ`: Hạng **Đồng**.
  - `tinhChietKhau()`: Trả về tỷ lệ chiết khấu giảm giá tương ứng với hạng thẻ (Đồng: 1%, Bạc: 3%, Vàng: 5%, Kim Cương: 10%).
  - `tichLuyChiTieu(long soTien)`: Tăng tổng chi tiêu, cộng điểm tích lũy (cứ mỗi 100.000 VNĐ được 1 điểm) và tự động gọi `capNhatThuHang()`.
  - `nhapThongTin(Scanner sc)`: Nhập mã KH, thông tin cá nhân và ngày đăng ký thành viên.
  - `toString()`: Ghép và hiển thị chi tiết thông tin khách hàng kèm trạng thái hoạt động.

---

## 2. `DSKhachHang`
Lớp quản lý danh sách khách hàng bằng **mảng tĩnh thủ công** (`KhachHang[] list`, quản lý số lượng bằng biến `int n`), không sử dụng các lớp tập hợp thư viện ngoài:

- **Phương thức quản lý và tìm kiếm:**
  - `add(KhachHang kh)`: Thêm một khách hàng vào mảng, tự động nới rộng kích thước mảng nếu bị đầy.
  - `taoDS(Scanner sc)`: Nhập số lượng khách hàng ban đầu và nhập thông tin cho từng người.
  - `themKH(Scanner sc)`: Nhập thông tin và thêm một khách hàng mới, có kiểm tra trùng mã khách hàng.
  - `xuatDSKH()`: In ra màn hình toàn bộ danh sách khách hàng trong hệ thống.
  - `timKH(String maKH)`: Tìm và trả về vị trí chỉ số (index) của khách hàng trong mảng (trả về `-1` nếu không có).
  - `timKiemKH(String maKH)`: Tìm và trả về đối tượng `KhachHang` theo mã (trả về `null` nếu không tìm thấy).
  - `timKiemTheoTen(String hoTen)`: Tìm kiếm gần đúng theo họ tên, trả về mảng thủ công `KhachHang[]`.
  - `suaKH(String maKH, Scanner sc)`: Cung cấp menu lựa chọn để cập nhật từng trường thông tin của khách hàng (Họ tên, SĐT, Email, Ngày sinh, Địa chỉ, Điểm tích lũy,...).
  - `thongKeTheoHang()`: Đếm và thống kê số lượng khách hàng theo từng phân hạng thẻ (Đồng, Bạc, Vàng, Kim Cương).

- **Phương thức xóa mềm & lưu trữ dữ liệu:**
  - `xoaKH(String maKH)`: Thực hiện xóa mềm bằng cách cập nhật `list[idx].setTonTai(false)` (không xóa vĩnh viễn mất dữ liệu khỏi database) và tự động gọi hàm ghi file.
  - `ghiFileKhachHang()`: Đóng gói danh sách đối tượng khách hàng và ghi đè vào file `src/person/KhachHang.txt` thông qua hàm dùng chung `DataBase.ghiVaoFile`.

---

## 3. `QuanKho`
Lớp kế thừa từ `NhanVien`, chịu trách nhiệm toàn bộ các nghiệp vụ kho bãi, kiểm kê, nhập hàng và làm việc với Nhà cung cấp:

- **Kiểm tra hạn sử dụng (HSD):**
  - `checkHSD(SanPham sp)` / `checkHSD(String maSP)` / `checkHSD(Scanner sc)`: Kiểm tra HSD của sản phẩm thực phẩm và đưa ra cảnh báo:
    - *Đã quá hạn*: Báo động cần lập yêu cầu xuất hủy.
    - *Hết hạn hôm nay*: Cảnh báo khẩn cấp.
    - *Sắp hết hạn (`<= 7` ngày hoặc `<= 30` ngày)*: Cần ưu tiên giảm giá/xả hàng.
    - *Còn HSD tốt*: Đang lưu hành an toàn.
  - `danhSachQuaHan()`: Lọc và in bảng danh sách các sản phẩm thực phẩm đã quá hạn sử dụng trong kho.
  - `danhSachSapHetHan(int soNgay)`: Lọc và in bảng danh sách các sản phẩm thực phẩm sắp hết hạn trong vòng `soNgay` chỉ định.

- **Quản lý tồn kho & tùy chọn nhập hàng:**
  - `inDanhSachKho()`: In bảng chi tiết tất cả sản phẩm đang có trong kho (Mã SP, Tên SP, Loại, Số lượng tồn, Giá nhập, Giá bán, HSD/Bảo hành).
  - `timSanPham(String maSP)`: Tìm kiếm sản phẩm theo mã trong kho hàng dùng chung `QuanLySanPham.danhSachSanPham`.
  - `capNhatKho(Scanner sc)`: Nhập hàng vào kho; nếu sản phẩm đã có sẵn thì cộng thêm số lượng tồn kho, nếu chưa có thì nhập thông tin chi tiết để thêm mới.
  - `nhapHangTuyChon(Scanner sc)`: Cho phép chọn/nhập danh sách các mặt hàng cần nhập và trả về `ArrayList<CTHoaDon>` để bàn giao cho các bên đối tác (Nhà cung cấp/Kế toán).
  - `nhapHangTuDanhSach(ArrayList<CTHoaDon> danhSachNhap)`: Nhận trực tiếp danh sách chi tiết từ đối tác giao sang và tự động cộng dồn số lượng tồn vào kho.

- **Nghiệp vụ phiếu nhập kho & yêu cầu duyệt:**
  - `nhapPhieuNhapKho(Scanner sc)`: Khi Nhà cung cấp giao hàng tới, Quản kho nhập mã phiếu, mã NCC, mã SP, tên SP, số lượng, đơn giá. Hệ thống tự động tính tiền hàng ($= \text{SL} \times \text{Đơn giá}$) và tổng thành tiền ($= \text{Tiền hàng} + \text{Vận chuyển}$), cập nhật số lượng tồn kho `sp.setSoLuongTonKho(...)` và gọi hàm lưu lịch sử.
  - `ghiFileLichSuNhap(...)`: Lưu thông tin phiếu nhập kho vào file `src/person/LichSuNhapHang.txt` bằng `FileWriter(..., true)` và `PrintWriter` để ghi nối tiếp (append log) theo thời gian thực.
  - `yeuCau(String maNCC, Scanner sc)`: Lập yêu cầu duyệt (nhập thêm hàng mới, đổi trả hàng lỗi NCC, xuất hủy hàng hết HSD,...) kèm mã nhà cung cấp liên quan gửi lên Quản lý.
  - `ghiYeuCauVaoFile(String maNCC, String hinhThuc, String noiDung)`: Tạo đối tượng `utils.YeuCau` và sử dụng `utils.YeuCau.themVaoFile(yc, Paths.get("YeuCau.txt"))` để ghi nối tiếp vào file `YeuCau.txt` dùng chung của hệ thống (phục vụ chức năng duyệt yêu cầu của `QuanLy`).

