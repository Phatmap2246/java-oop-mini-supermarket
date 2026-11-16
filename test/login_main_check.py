"""Run Main against disposable copies; never write the real data directory."""
from pathlib import Path
import tempfile, shutil, subprocess, sys

root = Path(__file__).resolve().parents[1]
data = Path(tempfile.mkdtemp(prefix='supermarket-login-'))
shutil.copytree(root/'src/luutru/hien-tai', data/'hien-tai')
cp = str(root/(sys.argv[1] if len(sys.argv)>1 else 'out/login-check')) + ';' + str(root/'lib/*')
def run(inputs):
    result = subprocess.run(['java', '-Dfile.encoding=UTF-8', '-Dstdout.encoding=UTF-8', '-Dstderr.encoding=UTF-8', '-Dsieuthi.dataDir='+str(data),
                             '-cp', cp, 'Main'], input=inputs, text=True, encoding='utf-8',
                            capture_output=True, timeout=40)
    assert result.returncode == 0, result.stderr
    assert 'Không mở được' not in result.stderr and 'Chưa lưu được' not in result.stderr, result.stderr
    return result.stdout
roles = [('QuanLySieuThi','quanly@123','NV001'), ('QuanKhoSieuThi','quankho@123','NV002'),
         ('ThuNganSieuThi','thungan@123','NV003'), ('KeToanSieuThi','ketoan@123','NV004')]
original = (data/'hien-tai/NhanVien.txt').read_text(encoding='utf-8')
out=run(''.join(t+'\n'+p+'\n0\n' for t,p,_ in roles)+'0\n')
for _,_,code in roles: assert 'Đăng nhập thành công: '+code in out
assert 'Số lần đăng nhập sai' not in out
out=run('thungansieuthi\nWRONG\nTHUNGANSIEUTHI\nthungan@123\n0\n0\n')
assert 'Số lần đăng nhập sai: 1' in out and 'Đăng nhập thành công: NV003' in out
count=lambda: sum(not x.startswith('#') and not x.startswith('YCQUANLY;') for x in (data/'hien-tai/NhanVien.txt').read_text(encoding='utf-8').splitlines())
n=count();run('0\n');run('0\n');assert count()==n
old_rows={f[1]:f for line in original.splitlines() if not line.startswith('#') and (f:=line.split(';'))[0] != 'YCQUANLY'}
for line in (data/'hien-tai/NhanVien.txt').read_text(encoding='utf-8').splitlines():
    f=line.split(';')
    if f[0].startswith('#') or f[0]=='YCQUANLY': continue
    assert f[:15]+f[17:] == old_rows[f[1]], 'Migration changed personnel data'
run('');run('ThuNganSieuThi\n');run('ThuNganSieuThi\nthungan@123\n')
p=data/'hien-tai/NhanVien.txt';s=p.read_text(encoding='utf-8')
lines=s.splitlines()
for i,line in enumerate(lines):
    fields=line.split(';')
    if len(fields)>5 and fields[1]=='NV003': fields[5]='true';lines[i]=';'.join(fields)
p.write_text('\n'.join(lines)+'\n',encoding='utf-8')
out=run('ThuNganSieuThi\nthungan@123\n0\n')
assert 'Đăng nhập thành công' not in out and 'Số lần đăng nhập sai: 1' in out
print('PASS: four roles, logout/switch, case-insensitive username, wrong password retry, dismissal, EOF, legacy migration and repeated restart')
print('Test data:',data)
# Give existing employees separate accounts in the disposable fixture.
lines=p.read_text(encoding='utf-8').splitlines()
for i,line in enumerate(lines):
    f=line.split(';')
    if len(f)>16 and f[1] in ('NV003','NV007','NV006'):
        f[5]='false'
        if f[1]=='NV007': f[15:17]=['cashB','passB']
        if f[1]=='NV006': f[15:17]=['stockB','passB']
        lines[i]=';'.join(f)
p.write_text('\n'.join(lines)+'\n',encoding='utf-8')
def invoice_ids(name):
    return {f[1]:f[3] for line in (data/'hien-tai'/name).read_text(encoding='utf-8').splitlines()
            if (f:=line.split(';'))[0] in ('HDBAN','HDNHAP')}
before=invoice_ids('HoaDonBan.txt')
sale='1\nKH001\nSP003\n1\n0\n1\n1000000\n0\n'
out=run('ThuNganSieuThi\nthungan@123\n'+sale+'cashB\npassB\n'+sale+'0\n')
(data/'sale-output.txt').write_text(out,encoding='utf-8')
after=invoice_ids('HoaDonBan.txt')
assert sorted(v for k,v in after.items() if k not in before)==['NV003','NV007'], (before,after,out[-2000:])
before=invoice_ids('HoaDonNhap.txt')
stock='9\n3\nNCC001\n0\n0\nSP003\n1\nkhong\n0\n0\n'
out=run('QuanKhoSieuThi\nquankho@123\n'+stock+'stockB\npassB\n'+stock+'0\n')
(data/'stock-output.txt').write_text(out,encoding='utf-8')
after=invoice_ids('HoaDonNhap.txt')
assert sorted(v for k,v in after.items() if k not in before)==['NV002','NV006'], (before,after,out[-2000:])
print('PASS: two cashiers and two warehouse employees create invoices with their own IDs through Main')
lines=p.read_text(encoding='utf-8').splitlines()
for i,line in enumerate(lines):
    f=line.split(';')
    if len(f)>16 and f[1]=='NV006': f[0]='NHANVIEN';lines[i]=';'.join(f)
p.write_text('\n'.join(lines)+'\n',encoding='utf-8')
out=run('stockB\npassB\n0\n')
assert 'Kiểu nhân viên chưa có menu: TaiKhoanNhanVien' in out
print('PASS: unsupported employee type returns to login')
# Create a cashier, reject a duplicate username, edit it, then authenticate.
create='QuanLySieuThi\nquanly@123\n1\n1\n1\nNguyen Van Test\ntest@example.com\n0912345678\n01/01/1995\n10\nTest\nTest\nTest\nTest\n5000000\n0\nTHUNGANSIEUTHI\nnewCash\nnewPass\n4\n0\n0\n'
out=run(create)
(data/'create-output.txt').write_text(out,encoding='utf-8')
rows=[line.split(';') for line in p.read_text(encoding='utf-8').splitlines()]
created=[f for f in rows if len(f)>16 and f[15]=='newCash']
assert len(created)==1 and created[0][0]=='THUNGAN', out[-3000:]
code=created[0][1]
edit='QuanLySieuThi\nquanly@123\n1\n3\n'+code+'\n10\nTHUNGANSIEUTHI\neditedCash\neditedPass\n0\n4\n0\neditedCash\neditedPass\n0\n0\n'
out=run(edit)
(data/'edit-output.txt').write_text(out,encoding='utf-8')
assert 'Tên đăng nhập đã được sử dụng' in out and 'Đăng nhập thành công: '+code in out, out[-3000:]
print('PASS: create/edit account via Main, duplicate rejection, new cashier login')
