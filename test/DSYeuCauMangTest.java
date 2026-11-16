import person.DSYeuCau;
import utils.YeuCau;
import java.nio.file.*;
import java.util.Scanner;

public class DSYeuCauMangTest {
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory("yeu-cau-array-");
        Path file = dir.resolve("YeuCau.txt");
        Files.writeString(file, "old-invalid-line\nNV001|QuanKho|Nhap hang|ChoDuyet\n");
        DSYeuCau ds = new DSYeuCau();
        ds.docFile(file.toString());
        if (ds.getList().length != 1) throw new AssertionError("Load");
        ds.duyetYeuCau(new Scanner("bad\n1\n"));
        if (!ds.timMa("NV001").getTrangThai().equals("DaDuyet")) throw new AssertionError("Approve");
        if (!YeuCau.docDanhSach(file).stream().anyMatch(YeuCau.BanGhiDataBase::laDongLoi))
            throw new AssertionError("Lost old line");
        ds.them(new YeuCau("NV002", "ThuNgan", "Doi ca", "ChoDuyet"));
        ds.ghiFile(file.toString());
        ds.docFile(file.toString());
        ds.duyetYeuCau(new Scanner("0\n"));
        if (!ds.timMa("NV002").getTrangThai().equals("KhongDuyet")) throw new AssertionError("Reject");
        if (!ds.sua("NV002", new Scanner("Noi dung moi\n1\n"))) throw new AssertionError("Edit");
        if (!ds.xoa("NV002") || ds.getList().length != 1) throw new AssertionError("Delete");
        Path invalid = dir.resolve("invalid.txt");
        Files.writeString(invalid, "#SIEUTHI_TXT_3\n#NHOM;YeuCau\nBAD\n");
        try { ds.docFile(invalid.toString()); throw new AssertionError("Expected load failure"); }
        catch (java.io.IOException expected) { }
        if (!ds.xoa("NV001") || ds.getList().length != 0) throw new AssertionError("Source changed after failure");
        System.out.println("PASS: array load/save, approve/reject, invalid input, edit/delete, old-line preservation and failed-load recovery.");
    }
}
