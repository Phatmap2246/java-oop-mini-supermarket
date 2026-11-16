"""Tạo bộ TXT riêng; không sửa dữ liệu ứng dụng hoặc ghi đè bộ mẫu đã tồn tại."""
from pathlib import Path
from datetime import date, timedelta
from ten_san_pham import TEN_SAN_PHAM
import calendar
import sys

ROOT = Path(__file__).resolve().parents[1]
DEST = ROOT / (sys.argv[1] if len(sys.argv) > 1 else 'du-lieu-test-day-du')
if DEST.exists():
    raise SystemExit('Thư mục đã tồn tại; hãy chọn tên mới để giữ dữ liệu đã thử.')
seed = ROOT/'src/luutru/hien-tai'
today = date.today()
fmt = lambda d: d.strftime('%d/%m/%Y')
iso = lambda d, time='10:30': d.isoformat()+'T'+time
rows = lambda name: [x.split(';') for x in (seed/name).read_text(encoding='utf-8-sig').splitlines() if x and not x.startswith('#')]
data = DEST/'src/luutru/hien-tai'
data.mkdir(parents=True)
(DEST/'src/Main.java').write_text('// Đánh dấu gốc thư mục dữ liệu thử; chương trình chạy từ class đã biên dịch.\n', encoding='utf-8')
def write(name, group, values, directory=data):
    directory.mkdir(parents=True, exist_ok=True)
    content=['#SIEUTHI_TXT_3','#NHOM;'+group,'#BODEM;101;13;41;121;13;11']
    content += [';'.join(map(str,r)) for r in values]
    (directory/name).write_text('\n'.join(content)+'\n',encoding='utf-8')

products=[]
for i in range(1,101):
    ncc=f'NCC{(i-1)%10+1:03}'
    cost=10000+(i%20)*5000; price=cost+10000
    stock=0 if i%10==0 else 2 if i%10==1 else 5 if i%10==2 else 50+i
    active='false' if i in (97,98,99) else 'true'
    if i<=40:
        expiry=today+timedelta(days=(-2 if i%8==0 else 0 if i%8==1 else 3 if i%8==2 else 90))
        p=['THUCPHAM',f'SP{i:03}',f'Thực phẩm mẫu {i:03}','Thực phẩm',ncc,cost,price,stock,0.08,active,fmt(today-timedelta(days=30)),fmt(expiry),-18 if i%3==0 else 5,'Giữ lạnh']
    elif i<=75:
        p=['TIEUDUNG',f'SP{i:03}',f'Tiêu dùng mẫu {i:03}','Tiêu dùng',ncc,cost,price,stock,0.05,active,'Nhãn Việt','Việt Nam','Gia dụng','Để xa trẻ em']
    else:
        p=['DIENTU',f'SP{i:03}',f'Điện tử mẫu {i:03}','Điện tử',ncc,cost,price,stock,0.05,active,12 if i%2 else 24,'Nhựa và inox']
    p[2] = TEN_SAN_PHAM[p[1]]
    products.append(p)
write('SanPham.txt','QuanLySanPham',products)
nccs=[]
for i in range(1,11):
    code=f'NCC{i:03}'
    nccs.append(['NHACUNGCAP',code,f'Nhà cung cấp mẫu {i:02}',f'09120000{i:02}',f'{i} Nguyễn Trãi',f'ncc{i}@example.com',f'03100000{i:02}','Nhà cung cấp','true','Tổng hợp',100000000])
    nccs += [['SPCC',code]+p for p in products if p[4]==code]
write('DoiTac.txt','Danhsachdoitac',nccs)
staff=rows('NhanVien.txt')
for i in range(5,13):
    p=staff[(i-5)%4].copy(); p[1]=f'NV{i:03}'; p[6]=f'nv{i}@example.com'; p[7]='Nguyễn Văn An'; p[8]=f'09010000{i:02}'
    p[5]='true' if i==12 else 'false'; staff.append(p)
write('NhanVien.txt','DSNhanVien',staff)
write('NhanVienDangNhap.txt','VaiTro',staff[:4])
customers=[]
for i in range(1,41):
    spend=[250000,1500000,5500000,11000000][(i-1)%4]
    customers.append(['KHACHHANG',f'KH{i:03}',fmt(today-timedelta(days=400)),spend,['Đồng','Bạc','Vàng','Kim Cương'][(i-1)%4],spend//100000,'false' if i==40 else 'true',f'kh{i}@example.com','Nguyễn Minh Anh',f'09330000{i:02}',str(i),'Nguyễn Trãi','Bến Thành','Quận Một','Hồ Chí Minh','10/06/1995'])
write('KhachHang.txt','DSKhachHang',customers)
promos=[]
for i in range(1,13):
    start=today-timedelta(days=10); end=today+timedelta(days=120)
    if i in (4,5,6): end=today-timedelta(days=1)
    if i in (7,8,9): start=today+timedelta(days=10)
    tag=['KMPHANTRAM','KMMUATANG','KMTRUCTIEP'][(i-1)%3]
    label=['Giảm theo phần trăm','Mua 2 tặng 1','Giảm giá trực tiếp'][(i-1)%3]
    extra=[0.1,50000] if tag=='KMPHANTRAM' else [2,1 if i==2 else 2] if tag=='KMMUATANG' else [20000]
    promos.append([tag,f'KM{i:03}',label,fmt(start),fmt(end),50000 if i in (10,11,12) else 0,'false' if i==12 else 'true']+extra)
write('KhuyenMai.txt','Danhsachkhuyenmai',promos)
write('SanPhamKhuyenMai.txt','DanhSachSPKM',[['SPKM',p[1],f'KM{(i%12)+1:03}','false' if i==98 else 'true'] for i,p in enumerate(products) if i%5!=0])
sales=[]
for i in range(1,101):
    code=f'HD{i:03}'; paid=i<=75
    d=today-timedelta(days=((i-1)*7)%500)
    if i==1:d=today
    if i==2:d=today.replace(day=1)
    if i==3:d=today.replace(day=calendar.monthrange(today.year,today.month)[1])
    rate=[0.01,0.03,0.05,0.1][(i-1)%4]; customer=f'KH{(i-1)%39+1:03}'
    sales.append(['HDBAN',code,iso(d),f'NV{3 if i%2 else 7:03}','Hoá đơn bán',customer,str(paid).lower(),rate])
    total=0
    for j in range(1+(i%3)):
        p=products[(i+j*13-1)%96]; qty=3 if i%4==0 else 2; price=int(p[6]); gift=1 if i%4==0 else 0
        discount=price*gift if gift else min(20000,price*qty) if i%4==1 else int(price*qty*0.1) if i%4==2 else 0
        net=price*qty-discount; total+=net
        sales.append(['CTBAN',code,p[1],p[2],qty,price,'true',net,gift,qty-gift,discount,'Mua 2 tặng 1' if gift else 'Giảm giá trực tiếp' if i%4==1 else 'Giảm theo phần trăm' if i%4==2 else 'Không áp dụng'])
    due=int(total*(1-rate))
    if paid:
        sales.append(['THANHTOAN',code,'TIENMAT',due+50000,50000] if i%2 else ['THANHTOAN',code,'CHUYENKHOAN','Ngân hàng mẫu','0123456789',f'FT{i:06}',f'Thanh toán thử {code}'])
write('HoaDonBan.txt','DanhSachHoaDonBan',sales)
imports=[]; debts=[]
for i in range(1,21):
    code=f'HD{100+i:03}'; supplier=f'NCC{(i-1)%10+1:03}'
    approved=i<=15; stocked=i<=10; d=today-timedelta(days=i*4)
    imports.append(['HDNHAP',code,iso(d,'09:00'),'NV002','Hoá đơn nhập',supplier,10000,8.0,str(approved).lower(),str(stocked).lower()])
    subtotal=0
    for p in [p for p in products if p[4]==supplier][:3]:
        qty=10+i; subtotal+=qty*int(p[5]); imports.append(['CTNHAP',code,p[1],p[2],qty,p[5]])
    if stocked:
        paid=i%3==0; deadline=today+timedelta(days=-10 if i%2 else 20)
        debts.append(['CONGNO',code,f'PNK{i:03}',supplier,f'Nhà cung cấp mẫu {(i-1)%10+1:02}',subtotal+int(subtotal*.08)+10000,fmt(deadline),fmt(today-timedelta(days=1)) if paid else '', 'Ngân hàng mẫu' if paid else '',str(paid).lower()])
write('HoaDonNhap.txt','DanhSachHoaDonNhap',imports)
write('CongNo.txt','DanhSachCongNo',debts)
write('YeuCau.txt','YeuCau',[['YEUCAU',f'NV{(i%11)+1:03}',['QuanKho','ThuNgan','KeToan'][i%3],f'Yêu cầu kiểm thử {i:02}',['ChoDuyet','DaDuyet','KhongDuyet'][i%3]] for i in range(1,19)], data.parent)
(DEST/'CHAY_THU.ps1').write_text('''$ErrorActionPreference = 'Stop'
$gocDoAn = Split-Path -Parent $PSScriptRoot
$lopThu = Join-Path $PSScriptRoot 'classes'
$thuVien = Join-Path $gocDoAn 'lib/*'
$nguonJava = @(Get-ChildItem -LiteralPath (Join-Path $gocDoAn 'src') -Recurse -Filter *.java | ForEach-Object { $_.FullName })
& javac -encoding UTF-8 -cp $thuVien -d $lopThu $nguonJava
if ($LASTEXITCODE -ne 0) { throw 'Biên dịch thất bại.' }
Push-Location $PSScriptRoot
try { & java -cp "$lopThu;$thuVien" Main } finally { Pop-Location }
''',encoding='utf-8-sig')
print('Created:',DEST)
print('100 products, 100 sales, 20 imports, 10 suppliers, 12 promotions, 40 customers, 12 staff, 10 debts, 18 requests')
