package id.ac.ub.filkom.perpustakaan.entity;

import org.springframework.data.jpa.repository.JpaRepository;

/** Akses tabel buku (implementasi otomatis Spring Data JPA). */
public interface BukuRepository extends JpaRepository<Buku, String> {
}
