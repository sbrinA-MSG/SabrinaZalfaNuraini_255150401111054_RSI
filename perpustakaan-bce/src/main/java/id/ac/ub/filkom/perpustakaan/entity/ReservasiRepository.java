package id.ac.ub.filkom.perpustakaan.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/** Akses tabel reservasi. */
public interface ReservasiRepository extends JpaRepository<Reservasi, String> {

    /** Query turunan dari nama method: apakah reservasi yang sama sudah ada? */
    boolean existsByAnggotaAndBukuAndStatus(Anggota anggota, Buku buku, String status);
}
