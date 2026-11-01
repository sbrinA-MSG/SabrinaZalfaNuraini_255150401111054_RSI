package id.ac.ub.filkom.perpustakaan;

import id.ac.ub.filkom.perpustakaan.entity.Anggota;
import id.ac.ub.filkom.perpustakaan.entity.Buku;
import id.ac.ub.filkom.perpustakaan.entity.Reservasi;
import id.ac.ub.filkom.perpustakaan.entity.TransaksiPinjam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Unit test method entity (tanpa database dan tanpa server). */
class PinjamEntityTest {

    @Test
    void cekStok_dan_kurangiStok() {
        Buku buku = new Buku("B001", "Pemrograman Java", 1);
        assertTrue(buku.cekStok());
        buku.kurangiStok();
        assertEquals(0, buku.getStok());
        assertFalse(buku.cekStok());
        assertThrows(IllegalStateException.class, buku::kurangiStok);
    }

    @Test
    void tambahStok_menolak_jumlah_nol_atau_negatif() {
        Buku buku = new Buku("B001", "Pemrograman Java", 1);
        buku.tambahStok(4);
        assertEquals(5, buku.getStok());
        assertThrows(IllegalArgumentException.class, () -> buku.tambahStok(0));
        assertThrows(IllegalArgumentException.class, () -> buku.tambahStok(-2));
    }

    @Test
    void cekBatasPinjam_menolak_jika_sudah_mencapai_batas() {
        Anggota anggota = new Anggota("A002", "Sari Dewi", 1);
        Buku buku = new Buku("B003", "Basis Data", 2);
        assertTrue(anggota.cekBatasPinjam());

        TransaksiPinjam t = TransaksiPinjam.buat(anggota, buku);
        assertEquals(TransaksiPinjam.DIPINJAM, t.getStatus());
        assertEquals(9, t.getIdTransaksi().length());
        assertFalse(anggota.cekBatasPinjam());
    }

    @Test
    void reservasi_dibuat_dengan_status_menunggu() {
        Anggota anggota = new Anggota("A001", "Budi", 2);
        Buku buku = new Buku("B002", "Rekayasa Perangkat Lunak", 0);
        Reservasi r = Reservasi.buat(anggota, buku);
        assertEquals(Reservasi.MENUNGGU, r.getStatus());
        assertEquals(9, r.getIdReservasi().length());
    }
}
