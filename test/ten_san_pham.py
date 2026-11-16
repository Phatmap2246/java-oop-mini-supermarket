"""Tên hàng mẫu dùng chung cho bộ dữ liệu siêu thị."""

THUC_PHAM = [
    "Sữa tươi không đường 1 lít", "Sữa chua có đường lốc 4 hộp",
    "Cá basa phi lê đông lạnh 500 g", "Phô mai lát 200 g",
    "Xúc xích heo 500 g", "Tôm sú đông lạnh 500 g",
    "Đậu hũ non 300 g", "Bánh flan caramel hộp 4 cái",
    "Bò viên đông lạnh 500 g", "Nước cam ép 1 lít",
    "Kim chi cải thảo 500 g", "Mực ống đông lạnh 500 g",
    "Bơ lạt 200 g", "Sữa tươi có đường 1 lít",
    "Há cảo tôm đông lạnh 500 g", "Chả lụa heo 500 g",
    "Sữa chua uống dâu 700 ml", "Đùi gà đông lạnh 1 kg",
    "Nấm kim châm 150 g", "Rau xà lách túi 300 g",
    "Thịt heo xay đông lạnh 500 g", "Nước táo ép 1 lít",
    "Đậu hũ trứng 200 g", "Cá hồi đông lạnh 300 g",
    "Mì tươi trứng 500 g", "Bánh bao nhân thịt 400 g",
    "Chả giò đông lạnh 500 g", "Sữa đậu nành 1 lít",
    "Salad rau trộn hộp 250 g", "Bắp ngọt đông lạnh 500 g",
    "Bánh pudding sữa 200 g", "Nước dừa tươi 500 ml",
    "Khoai tây chiên đông lạnh 1 kg", "Phô mai mozzarella 200 g",
    "Sữa chua nha đam lốc 4 hộp", "Cá viên đông lạnh 500 g",
    "Bánh su kem hộp 200 g", "Nấm đùi gà 200 g",
    "Ức gà đông lạnh 500 g", "Nước ép ổi 1 lít",
]
TIEU_DUNG = [
    "Nước rửa chén chanh 750 ml", "Bột giặt hương hoa 3 kg",
    "Nước lau sàn sả 1 lít", "Khăn giấy hộp 180 tờ",
    "Giấy vệ sinh lốc 10 cuộn", "Bàn chải đánh răng mềm",
    "Kem đánh răng bạc hà 180 g", "Dầu gội bưởi 650 ml",
    "Sữa tắm hoa hồng 650 ml", "Nước rửa tay 500 ml",
    "Túi rác cuộn cỡ trung", "Màng bọc thực phẩm 30 m",
    "Giấy bạc nướng thực phẩm 5 m", "Miếng rửa chén bộ 5 cái",
    "Găng tay cao su cỡ M", "Chổi quét nhà cán dài",
    "Cây lau nhà xoay 360 độ", "Thau nhựa tròn 10 lít",
    "Rổ nhựa đựng rau 25 cm", "Hộp đựng thực phẩm 1 lít",
    "Bình nước nhựa 700 ml", "Ly thủy tinh bộ 6 cái",
    "Chén sứ trắng bộ 6 cái", "Đĩa sứ tròn 20 cm",
    "Đũa tre bộ 10 đôi", "Muỗng inox bộ 6 cái",
    "Dao gọt trái cây", "Thớt nhựa kháng khuẩn",
    "Khăn lau bếp bộ 3 cái", "Móc áo nhựa bộ 10 cái",
    "Kẹp phơi quần áo bộ 20 cái", "Nước xả vải 1,8 lít",
    "Xà phòng giặt 200 g", "Bông tẩy trang 100 miếng",
    "Khẩu trang y tế hộp 50 cái",
]
DIEN_TU = [
    "Ấm đun nước siêu tốc 1,8 lít", "Nồi cơm điện 1,8 lít",
    "Máy xay sinh tố 1,5 lít", "Quạt bàn 30 cm",
    "Máy sấy tóc 1200 W", "Bàn ủi khô 1000 W",
    "Bếp điện từ đơn 2000 W", "Nồi chiên không dầu 4 lít",
    "Máy đánh trứng cầm tay", "Máy ép cam 40 W",
    "Đèn bàn học LED 8 W", "Bóng đèn LED 9 W",
    "Ổ cắm điện 3 chấu dây 3 m", "Dây nối điện 5 m",
    "Sạc điện thoại USB-C 20 W", "Cáp sạc USB-C 1 m",
    "Pin sạc dự phòng 10000 mAh", "Tai nghe có dây 3,5 mm",
    "Loa Bluetooth mini", "Chuột máy tính không dây",
    "Bàn phím USB", "Đồng hồ báo thức điện tử",
    "Cân nhà bếp điện tử 5 kg", "Đèn pin sạc LED",
    "Quạt tích điện mini",
]

TEN_SAN_PHAM = {
    f"SP{i:03}": ten
    for i, ten in enumerate(THUC_PHAM + TIEU_DUNG + DIEN_TU, 1)
}
