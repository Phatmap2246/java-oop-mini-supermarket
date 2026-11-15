# Dữ liệu siêu thị

- `hien-tai/`: mười file TXT của phiên hiện tại.
- `ban-truoc/`: bản sao phiên trước để khôi phục.
- `NhanVien.txt`, `KhachHang.txt`: bản xuất từ API nghiệp vụ.
- `YeuCau.txt`: các dòng yêu cầu, lưu ngay khi thêm/duyệt.
- `LichSuCa.txt`: nhật ký ca; các file lịch sử cũ được giữ nguyên.
- `QR/`: ảnh QR thanh toán.

TXT phiên bản 3 dùng UTF-8 và dấu `;`, đọc bằng `split(";", -1)`. Không escape, không bản ghi lồng trong một ô, không footer. Null được ghi thành ô rỗng. Chuỗi mới có `;` hoặc xuống dòng sẽ bị báo lỗi khi lưu. Chỉ đọc/ghi TXT dạng dòng. Không còn bộ đọc XML hoặc bản lưu đồ thị. Nội dung yêu cầu lưu trực tiếp, không dùng Base64.

Xem [hướng dẫn](../../LUU_TRU_DU_LIEU.md), [bảng thay đổi](../../doc/THAY_DOI_DOC_GHI_TXT_DON_GIAN.md), [cấu trúc cột](../../doc/CAU_TRUC_FILE_TXT.md) và [vị trí hàm](../../doc/VI_TRI_HAM_DOC_GHI_TXT.md).

Đã chuyển dữ liệu đang dùng sang TXT. Bản gốc trước khi chuyển nằm riêng ở `out/txt-co-dien/before/src/luutru`. Xem [phạm vi khôi phục](../../doc/DOC_GHI_TXT_CO_DIEN.md).
