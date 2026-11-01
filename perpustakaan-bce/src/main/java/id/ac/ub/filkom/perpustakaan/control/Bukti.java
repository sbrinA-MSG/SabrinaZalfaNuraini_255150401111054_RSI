package id.ac.ub.filkom.perpustakaan.control;

import id.ac.ub.filkom.perpustakaan.entity.TransaksiPinjam;

import java.time.LocalDate;

/**
 * Data hasil peminjaman yang dikirim control ke boundary.
 * Kelas biasa (bukan entity) supaya template tidak menyentuh entity langsung.
 */
public class Bukti {

    private final String idTransaksi;
    private final String namaAnggota;
    private final String judulBuku;
    private final LocalDate tanggalPinjam;
    private final String status;

    private Bukti(String idTransaksi, String namaAnggota, String judulBuku,
                  LocalDate tanggalPinjam, String status) {
        this.idTransaksi = idTransaksi;
        this.namaAnggota = namaAnggota;
        this.judulBuku = judulBuku;
        this.tanggalPinjam = tanggalPinjam;
        this.status = status;
    }

    /** Membuat Bukti dari sebuah transaksi. */
    public static Bukti dari(TransaksiPinjam t) {
        return new Bukti(t.getIdTransaksi(),
                t.getAnggota().getNama(),
                t.getBuku().getJudul(),
                t.getTanggalPinjam(),
                t.getStatus());
    }

    public String getIdTransaksi()      { return idTransaksi; }
    public String getNamaAnggota()      { return namaAnggota; }
    public String getJudulBuku()        { return judulBuku; }
    public LocalDate getTanggalPinjam() { return tanggalPinjam; }
    public String getStatus()           { return status; }
}
