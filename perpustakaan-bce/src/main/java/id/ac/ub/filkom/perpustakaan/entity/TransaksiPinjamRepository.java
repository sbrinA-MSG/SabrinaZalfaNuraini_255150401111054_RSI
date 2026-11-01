package id.ac.ub.filkom.perpustakaan.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/** Akses tabel transaksi_pinjam. save(transaksi) = method simpan() pada desain. */
public interface TransaksiPinjamRepository extends JpaRepository<TransaksiPinjam, String> {
}
