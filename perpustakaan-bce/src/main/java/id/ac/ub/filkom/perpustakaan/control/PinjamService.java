package id.ac.ub.filkom.perpustakaan.control;

import id.ac.ub.filkom.perpustakaan.entity.Anggota;
import id.ac.ub.filkom.perpustakaan.entity.AnggotaRepository;
import id.ac.ub.filkom.perpustakaan.entity.Buku;
import id.ac.ub.filkom.perpustakaan.entity.BukuRepository;
import id.ac.ub.filkom.perpustakaan.entity.TransaksiPinjam;
import id.ac.ub.filkom.perpustakaan.entity.TransaksiPinjamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * «control» PinjamController pada desain.
 * Di Spring Boot direalisasikan sebagai @Service (BUKAN @Controller),
 * karena tugasnya mengoordinasikan alur bisnis satu use case.
 */
@Service
public class PinjamService {

    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;
    private final TransaksiPinjamRepository transaksiRepository;

    public PinjamService(AnggotaRepository anggotaRepository,
                         BukuRepository bukuRepository,
                         TransaksiPinjamRepository transaksiRepository) {
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
        this.transaksiRepository = transaksiRepository;
    }

    /** Langkah 1-2 scenario: daftar semua buku beserta stok. */
    @Transactional(readOnly = true)
    public List<Buku> daftarBuku() {
        return bukuRepository.findAll();
    }

    /** Riwayat peminjaman milik satu anggota (ditampilkan di halaman pinjam). */
    @Transactional(readOnly = true)
    public List<Bukti> riwayatPinjam(String idAnggota) {
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() -> PinjamGagalException.tidakDitemukan("Anggota"));
        return anggota.getDaftarPinjam().stream()
                .map(Bukti::dari)
                .toList();
    }

    /** +pinjamBuku(idAnggota : String, idBuku : String) : Bukti */
    @Transactional
    public Bukti pinjamBuku(String idAnggota, String idBuku) {
        // Persiapan: ambil objek entity dari database
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() -> PinjamGagalException.tidakDitemukan("Anggota"));
        Buku buku = bukuRepository.findById(idBuku)
                .orElseThrow(() -> PinjamGagalException.tidakDitemukan("Buku"));

        // [3] cekBatasPinjam() -> [4] boleh pinjam
        if (!anggota.cekBatasPinjam()) {
            throw PinjamGagalException.melewatiBatas();        // alur 4b
        }

        // [5] cekStok() -> [6] stok tersedia / habis
        if (!buku.cekStok()) {
            throw PinjamGagalException.stokHabis();            // [13] pesanHabis
        }

        // alt [stok tersedia]
        // [7] buat(idAnggota, idBuku) -> [8] transaksi tersimpan
        TransaksiPinjam transaksi = TransaksiPinjam.buat(anggota, buku);
        transaksiRepository.save(transaksi);

        // [9] kurangiStok() -> [10] ok
        buku.kurangiStok();
        bukuRepository.save(buku);

        return Bukti.dari(transaksi);                          // [11] bukti peminjaman
    }
}
