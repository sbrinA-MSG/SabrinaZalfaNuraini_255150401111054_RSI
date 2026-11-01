package id.ac.ub.filkom.perpustakaan;

import id.ac.ub.filkom.perpustakaan.control.Bukti;
import id.ac.ub.filkom.perpustakaan.control.OperasiGagalException;
import id.ac.ub.filkom.perpustakaan.control.PinjamGagalException;
import id.ac.ub.filkom.perpustakaan.control.PinjamService;
import id.ac.ub.filkom.perpustakaan.control.ReservasiService;
import id.ac.ub.filkom.perpustakaan.control.StokService;
import id.ac.ub.filkom.perpustakaan.entity.BukuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pengujian skenario UC-01 sampai UC-03 memakai data dari data.sql.
 * @Transactional pada kelas test: setiap test di-rollback, data kembali seperti awal.
 */
@SpringBootTest
@Transactional
class PinjamServiceTest {

    @Autowired PinjamService pinjamService;
    @Autowired ReservasiService reservasiService;
    @Autowired StokService stokService;
    @Autowired BukuRepository bukuRepository;

    @Test
    void stokTersedia_bukti_dibuat_dan_stok_berkurang() {
        Bukti bukti = pinjamService.pinjamBuku("A001", "B001");
        assertEquals("DIPINJAM", bukti.getStatus());
        assertEquals("Pemrograman Java", bukti.getJudulBuku());
        assertEquals(2, bukuRepository.findById("B001").orElseThrow().getStok());
    }

    @Test
    void stokHabis_ditolak_dan_boleh_reservasi() {
        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> pinjamService.pinjamBuku("A001", "B002"));
        assertEquals("Stok buku habis", e.getMessage());
        assertTrue(e.isBisaReservasi());
    }

    @Test
    void melewatiBatas_pinjaman_ketiga_ditolak() {
        pinjamService.pinjamBuku("A001", "B001");
        pinjamService.pinjamBuku("A001", "B003");
        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> pinjamService.pinjamBuku("A001", "B004"));
        assertEquals("Melewati batas pinjam", e.getMessage());
    }

    @Test
    void idBukuTidakTerdaftar_ditolak() {
        PinjamGagalException e = assertThrows(PinjamGagalException.class,
                () -> pinjamService.pinjamBuku("A001", "XXXX"));
        assertEquals("Buku tidak ditemukan", e.getMessage());
    }

    @Test
    void reservasi_hanya_untuk_buku_habis_dan_tidak_boleh_ganda() {
        // buku masih tersedia -> ditolak
        assertThrows(OperasiGagalException.class,
                () -> reservasiService.ajukanReservasi("A001", "B001"));

        // buku habis -> berhasil, nomor diawali R
        String nomor = reservasiService.ajukanReservasi("A001", "B002");
        assertTrue(nomor.startsWith("R"));

        // reservasi yang sama untuk kedua kalinya -> ditolak
        assertThrows(OperasiGagalException.class,
                () -> reservasiService.ajukanReservasi("A001", "B002"));
    }

    @Test
    void kelolaStok_tambahBuku_dan_tambahStok() {
        stokService.tambahBuku("B005", "Jaringan Komputer", 2);
        stokService.tambahStok("B005", 3);
        assertEquals(5, bukuRepository.findById("B005").orElseThrow().getStok());

        assertThrows(OperasiGagalException.class,
                () -> stokService.tambahBuku("B005", "Duplikat", 1));
        assertThrows(OperasiGagalException.class,
                () -> stokService.tambahBuku("B006", "Stok Negatif", -1));
        assertThrows(OperasiGagalException.class,
                () -> stokService.tambahStok("B005", 0));
    }
}
