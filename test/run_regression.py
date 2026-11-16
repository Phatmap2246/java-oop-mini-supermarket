from pathlib import Path
import subprocess, tempfile, shutil
root=Path(__file__).resolve().parents[1]
work=Path(tempfile.mkdtemp(prefix='supermarket-regression-'))
shutil.copytree(root/'src',work/'src')
(work/'test').mkdir()
classes=work/'classes';classes.mkdir()
cp=str(classes)+';'+str(root/'lib/*')
def call(args):
    return subprocess.run(args,cwd=work,capture_output=True,text=True,encoding='utf-8',errors='replace',timeout=90)
r=call(['javac','-encoding','UTF-8','-cp',str(root/'lib/*'),'-d',str(classes)]+[str(p) for p in (work/'src').rglob('*.java')])
assert r.returncode==0,r.stderr
for p in (root/'test').rglob('*.java'):
    if p.stem in ('TaoDuLieuMau','MainTest_HoaDon'): continue
    r=call(['javac','-encoding','UTF-8','-cp',cp,'-d',str(classes),str(p)])
    if r.returncode:
        (work/(p.stem+'-compile.log')).write_text(r.stderr,encoding='utf-8');print(p.stem,'COMPILE FAIL');continue
    fixture=work/(p.stem+'-data')
    shutil.copytree(root/'src/luutru',fixture)
    if p.stem=='UnitTest':
        args=['org.junit.platform.console.ConsoleLauncher','execute','--select-class','UnitTest']
    else:
        args=[('person.' if p.parent.name=='person' else '')+p.stem]
        if p.stem=='LuuTruChuongTrinhTest': args+=['write',str(work/'roundtrip')]
        elif p.stem=='ChuyenDoiTxtTest': args += [str(work/'src/luutru'),str(work/'converted')]
        elif p.stem in ('MaDaCapTest','QuanLyInterfaceTest'): args += [str(fixture)]
        else: args += [str(work)]
    r=call(['java','-Dstdout.encoding=UTF-8','-Dstderr.encoding=UTF-8','-Dsieuthi.dataDir='+str(fixture),'-cp',cp]+args)
    (work/(p.stem+'.log')).write_text(r.stdout+r.stderr,encoding='utf-8')
    print(p.stem,'PASS' if r.returncode==0 and 'FAIL:' not in r.stdout else 'FAIL',flush=True)
    if p.stem=='LuuTruChuongTrinhTest' and r.returncode==0:
        r=call(['java','-Dstdout.encoding=UTF-8','-Dstderr.encoding=UTF-8','-Dsieuthi.dataDir='+str(fixture),'-cp',cp,p.stem,'read',str(work/'roundtrip')])
        (work/'LuuTruChuongTrinhTest-read.log').write_text(r.stdout+r.stderr,encoding='utf-8')
        print(p.stem,'read','PASS' if r.returncode==0 else 'FAIL',flush=True)
print('Logs:',work)
