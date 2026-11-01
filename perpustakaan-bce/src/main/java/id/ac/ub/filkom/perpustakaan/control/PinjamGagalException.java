package id.ac.ub.filkom.perpustakaan.control;

/**
 * Dilempar PinjamService bila peminjaman tidak dapat dilakukan (alur alternatif).
 * Boundary menangkapnya lalu menampilkan pesan ke pengguna.
 */
public class PinjamGagalException extends RuntimeException {

    private final boolean bisaReservasi;

    private PinjamGagalException(String pesan, boolean bisaReservasi) {
        super(pesan);
        this.bisaReservasi = bisaReservasi;
    }

    /** Alur 4a / pesan [13] pesanHabis. */
    public static PinjamGagalException stokHabis() {
        return new PinjamGagalException("Stok buku habis", true);
    }

    /** Alur 4b. */
    public static PinjamGagalException melewatiBatas() {
        return new PinjamGagalException("Melewati batas pinjam", false);
    }

    /** Validasi input: id tidak terdaftar. */
    public static PinjamGagalException tidakDitemukan(String objek) {
        return new PinjamGagalException(objek + " tidak ditemukan", false);
    }

    public boolean isBisaReservasi() {
        return bisaReservasi;
    }
}
