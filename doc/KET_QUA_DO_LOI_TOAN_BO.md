# Kết quả dò lỗi trên code hiện tại

Chỉ kiểm tra, không sửa mã nguồn hoặc dữ liệu thật. Toàn bộ src biên dịch được. Các test có ghi file chạy trong out/kiem-tra-toan-bo/run.

## Lỗi tái hiện được

1. **ThucPham: NullPointerException khi gán ngày đầu tiên.** Tạo new ThucPham(), gọi setNgaySanXuat(LocalDate.of(2026,11,1)): setter so sánh với hanSuDung còn null. Gọi setHanSuDung trước cũng có cùng vấn đề. DinhDangDongTest và lượt write của LuuTruChuongTrinhTest dừng ở lỗi này. Cần kiểm tra ngày còn lại khác null trước khi so sánh.
2. **Bàn giao ca cộng lại doanh thu lịch sử.** Một hóa đơn đã trả 10.000; hai thu ngân dùng chung danh sách. Sau a.thayCa(b), b.tinhTienCuoiCa() trả 20.000. tongTienDauCa của b đã nhận 10.000 nhưng phép tính tiếp tục cộng cùng hóa đơn. Cần xác định hóa đơn thuộc ca hoặc mốc bắt đầu ca.
3. **Ghi công nợ cho hóa đơn chưa duyệt/chưa nhập kho.** Phiếu có một chi tiết hợp lệ, Duyet=false và daNhapSP=false; đối chiếu đúng số lượng/giá vẫn tạo được khoản nợ. Luồng hiện không kiểm tra hai trạng thái trước khi ghi nhận. Cần chốt quy tắc công nợ; nếu dựa trên phiếu nhập đã hoàn thành thì đây là thiếu ràng buộc.
4. **VAT nhập theo hai đơn vị khác nhau.** Hai hàm nhập chặn giá trị trên 0.15, trong khi tinhTongTien chia thueVAT cho 100. Nhập 15 theo lời nhắc phần trăm bị từ chối; nhập 0.15 cho tiền hàng 10.000 chỉ cộng 15 tiền thuế, tổng 10.015 thay vì 11.500 nếu ý định là 15%. Cần dùng thống nhất đơn vị phần trăm.
5. **Nhập thêm cùng sản phẩm bị giới hạn bởi tồn kho hiện có.** Kho có SP001 tồn 0; nhập hai dòng SP001, mỗi dòng 1. Lần thứ hai bị yêu cầu giảm số lượng vì tổng 2 lớn hơn tồn 0. Đây là nhập hàng tăng tồn, không phải bán hàng. Đầu vào thử kết thúc tại vòng yêu cầu nhập lại và nhận NoSuchElementException; không coi chính EOF là lỗi, lỗi là điều kiện giới hạn số lượng nhập theo tồn hiện tại.
6. **Helper báo cáo công nợ cũ của KeToan lỗi định dạng printf.** Gọi hienThiDanhSachCongNo với một hóa đơn nhập gây IllegalFormatConversionException. Menu chính hiện dùng DanhSachCongNo nên lỗi này chỉ phát sinh ở đường gọi helper cũ.

## Kết quả các bộ test

- Pr43LogicIntegrationTest: 58 kiểm tra đạt.
- QuanLyInterfaceTest: 76 kiểm tra đạt.
- MaDaCapTest: 22 kiểm tra đạt.
- ChuyenDoiTxtTest: lưu/mở lại đủ bộ TXT đạt.
- DinhDangDongTest: thất bại tại setter ngày nêu ở mục 1; không tính là 72 kiểm tra đã đạt.
- LuuTruChuongTrinhTest write: thất bại tại cùng setter. Read chạy sau write thất bại cũng không hợp lệ; không kết luận lỗi khôi phục bộ đếm độc lập từ lần read đó.
- Biên dịch toàn bộ test cũ: thất bại do HoaDonPersistenceTest, MainTest_HoaDon và person/PersonPackageTest tham chiếu API đã bỏ. Điều này không có nghĩa src ứng dụng không biên dịch.
- AuditCases và AuditMore: các ca dò lỗi riêng; AssertionError là bằng chứng quy tắc mong đợi bị vi phạm, không phải test đạt. Xem results.json và more.log.

## Điều chỉnh kết luận trước

Hàm sửa hóa đơn nhập đã chặn cả isDuyet() và isDaNhapSP() ở bản hiện tại; ca thử xác nhận không sửa được phiếu đã nhập. Ngày sản phẩm đã được bổ sung kiểm tra cặp ngày, nhưng thiếu kiểm tra null. Luồng nhập trùng sản phẩm đã có gộp dòng, nhưng phát sinh điều kiện sai ở mục 5. Giới hạn mã nhân viên đúng ba chữ số là quy ước đã được người dùng xác nhận, không báo là lỗi.

## Vị trí cần xem

- [src/sanpham/ThucPham.java:47](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/sanpham/ThucPham.java:47>): public void setHanSuDung
- [src/sanpham/ThucPham.java:75](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/sanpham/ThucPham.java:75>): public void setNgaySanXuat
- [src/person/ThuNgan.java:70](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/ThuNgan.java:70>): public long tinhTienCuoiCa
- [src/person/ThuNgan.java:80](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/ThuNgan.java:80>): public void thayCa
- [src/hoadon/DanhSachCongNo.java:131](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/DanhSachCongNo.java:131>): public void doiChieuVaGhiNhanCongNo
- [src/hoadon/HoaDonNhapHang.java:85](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/HoaDonNhapHang.java:85>): while (this.thueVAT > 0.15
- [src/hoadon/HoaDonNhapHang.java:102](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/HoaDonNhapHang.java:102>): while (soLuongMoi >
- [src/hoadon/HoaDonNhapHang.java:56](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/HoaDonNhapHang.java:56>): public long tinhTongTien
- [src/person/KeToan.java:204](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/KeToan.java:204>): public void hienThiDanhSachCongNo
