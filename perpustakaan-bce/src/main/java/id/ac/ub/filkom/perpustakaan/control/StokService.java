package id.ac.ub.filkom.perpustakaan.control;

import id.ac.ub.filkom.perpustakaan.entity.Buku;
import id.ac.ub.filkom.perpustakaan.entity.BukuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** «control» Kelola Stok Buku - dipakai aktor Pustakawan. */
@Service
public class StokService {

    private final BukuRepository bukuRepository;

    public StokService(BukuRepository bukuRepository) {
        this.bukuRepository = bukuRepository;
    }

    @Transactional(readOnly = true)
    public List<Buku> daftarBuku() {
        return bukuRepository.findAll();
    }

    /** +tambahBuku(idBuku, judul, stok) : void */
    @Transactional
    public void tambahBuku(String idBuku, String judul, int stok) {
        if (idBuku == null || idBuku.isBlank() || idBuku.trim().length() > 10) {
            throw new OperasiGagalException("ID buku wajib diisi (maksimal 10 karakter)");
        }
        if (judul == null || judul.isBlank()) {
            throw new OperasiGagalException("Judul buku wajib diisi");
        }
        if (stok < 0) {
            throw new OperasiGagalException("Stok tidak boleh negatif");
        }
        String id = idBuku.trim().toUpperCase();
        if (bukuRepository.existsById(id)) {
            throw new OperasiGagalException("ID buku " + id + " sudah dipakai");
        }
        bukuRepository.save(new Buku(id, judul.trim(), stok));
    }

    /** +tambahStok(idBuku, jumlah) : void */
    @Transactional
    public void tambahStok(String idBuku, int jumlah) {
        if (jumlah <= 0) {
            throw new OperasiGagalException("Jumlah tambahan harus lebih dari 0");
        }
        Buku buku = bukuRepository.findById(idBuku)
                .orElseThrow(() -> new OperasiGagalException("Buku tidak ditemukan"));
        buku.tambahStok(jumlah);
        bukuRepository.save(buku);
    }
}
