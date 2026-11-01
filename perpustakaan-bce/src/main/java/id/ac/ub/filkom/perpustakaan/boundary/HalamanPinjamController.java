package id.ac.ub.filkom.perpustakaan.boundary;

import id.ac.ub.filkom.perpustakaan.control.Bukti;
import id.ac.ub.filkom.perpustakaan.control.PinjamGagalException;
import id.ac.ub.filkom.perpustakaan.control.PinjamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * «boundary» HalamanPinjam (bagian penerima request) - use case Pinjam Buku.
 * Hanya membaca input, memanggil SATU method control, dan memilih template.
 * Tidak ada query dan tidak ada aturan bisnis di sini.
 */
@Controller
@RequestMapping("/pinjam")
public class HalamanPinjamController {

    private final PinjamService pinjamService;           // «control»

    public HalamanPinjamController(PinjamService pinjamService) {
        this.pinjamService = pinjamService;
    }

    /** Langkah 1-2 scenario: tampilkan daftar buku beserta stok. */
    @GetMapping
    public String tampilHalaman(HttpSession session, Model model) {
        String idAnggota = idAnggotaLogin(session);
        if (idAnggota == null) {
            return "redirect:/login";                    // belum masuk
        }
        model.addAttribute("idAnggota", idAnggota);
        model.addAttribute("namaAnggota", session.getAttribute("namaAnggota"));
        model.addAttribute("daftarBuku", pinjamService.daftarBuku());
        model.addAttribute("riwayat", pinjamService.riwayatPinjam(idAnggota));
        return "pinjam";                                 // templates/pinjam.html
    }

    /** [1] pilihBuku(idBuku) dari aktor Anggota. */
    @PostMapping
    public String pilihBuku(@RequestParam String idBuku,
                            HttpSession session, Model model) {
        String idAnggota = idAnggotaLogin(session);
        if (idAnggota == null) {
            return "redirect:/login";
        }
        try {
            // [2] pinjamBuku(idAnggota, idBuku)
            Bukti bukti = pinjamService.pinjamBuku(idAnggota, idBuku);
            return tampilBukti(bukti, model);            // [12] tampilkan bukti
        } catch (PinjamGagalException e) {               // [13] pesanHabis
            model.addAttribute("pesan", e.getMessage());
            model.addAttribute("bisaReservasi", e.isBisaReservasi());
            return tampilHalaman(session, model);        // [14] pesan & reservasi
        }
    }

    /** +tampilBukti() pada class diagram. */
    private String tampilBukti(Bukti bukti, Model model) {
        model.addAttribute("bukti", bukti);
        return "bukti";                                  // templates/bukti.html
    }

    private String idAnggotaLogin(HttpSession session) {
        return (String) session.getAttribute("idAnggota");
    }
}
