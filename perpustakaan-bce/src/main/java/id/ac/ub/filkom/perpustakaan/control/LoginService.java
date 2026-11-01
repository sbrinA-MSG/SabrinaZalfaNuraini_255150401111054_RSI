package id.ac.ub.filkom.perpustakaan.control;

import id.ac.ub.filkom.perpustakaan.entity.Anggota;
import id.ac.ub.filkom.perpustakaan.entity.AnggotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** «control» Masuk Sistem - memeriksa apakah ID anggota terdaftar. */
@Service
public class LoginService {

    private final AnggotaRepository anggotaRepository;

    public LoginService(AnggotaRepository anggotaRepository) {
        this.anggotaRepository = anggotaRepository;
    }

    /** Mengembalikan nama anggota bila ID terdaftar. */
    @Transactional(readOnly = true)
    public String masuk(String idAnggota) {
        if (idAnggota == null || idAnggota.isBlank()) {
            throw new OperasiGagalException("ID anggota wajib diisi");
        }
        Anggota anggota = anggotaRepository.findById(idAnggota.trim().toUpperCase())
                .orElseThrow(() -> new OperasiGagalException("ID anggota tidak terdaftar"));
        return anggota.getNama();
    }
}
