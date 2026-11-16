package hoadon;

public enum TrangThaiDuyet {
    CHO_DUYET("Chờ duyệt"), DA_DUYET("Đã duyệt"), TU_CHOI("Từ chối");

    private final String nhan;
    TrangThaiDuyet(String nhan) { this.nhan = nhan; }
    @Override public String toString() { return nhan; }

    public static TrangThaiDuyet tuDuLieuFile(String value) {
        if ("true".equalsIgnoreCase(value)) return DA_DUYET;
        if ("false".equalsIgnoreCase(value)) return CHO_DUYET;
        return valueOf(value);
    }
}
