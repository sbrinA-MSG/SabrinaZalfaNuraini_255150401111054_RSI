package id.ac.ub.filkom.perpustakaan.control;

import id.ac.ub.filkom.perpustakaan.entity.Anggota;
import id.ac.ub.filkom.perpustakaan.entity.AnggotaRepository;
import id.ac.ub.filkom.perpustakaan.entity.Buku;
import id.ac.ub.filkom.perpustakaan.entity.BukuRepository;
import id.ac.ub.filkom.perpustakaan.entity.Reservasi;
import id.ac.ub.filkom.perpustakaan.entity.ReservasiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** «control» Ajukan Reservasi - use case «extend» dari Pinjam Buku. */
@Service
public class ReservasiService {

    private final AnggotaRepository anggotaRepository;
    private final BukuRepository bukuRepository;
    private final ReservasiRepository reservasiRepository;

    public ReservasiService(AnggotaRepository anggotaRepository,
                            BukuRepository bukuRepository,
                            ReservasiRepository reservasiRepository) {
        this.anggotaRepository = anggotaRepository;
        this.bukuRepository = bukuRepository;
        this.reservasiRepository = reservasiRepository;
    }

    /** +ajukanReservasi(idAnggota, idBuku) : String (nomor reservasi) */
    @Transactional
    public String ajukanReservasi(String idAnggota, String idBuku) {
        Anggota anggota = anggotaRepository.findById(idAnggota)
                .orElseThrow(() -> new OperasiGagalException("Anggota tidak ditemukan"));
        Buku buku = bukuRepository.findById(idBuku)
                .orElseThrow(() -> new OperasiGagalException("Buku tidak ditemukan"));

        // Aturan: reservasi hanya untuk buku yang stoknya habis
        if (buku.cekStok()) {
            throw new OperasiGagalException("Buku masih tersedia, silakan langsung dipinjam");
        }
        // Aturan: satu anggota tidak boleh mereservasi buku yang sama dua kali
        if (reservasiRepository.existsByAnggotaAndBukuAndStatus(
                anggota, buku, Reservasi.MENUNGGU)) {
            throw new OperasiGagalException("Anda sudah mereservasi buku ini");
        }

        Reservasi reservasi = Reservasi.buat(anggota, buku);
        reservasiRepository.save(reservasi);
        return reservasi.getIdReservasi();
    }
}
