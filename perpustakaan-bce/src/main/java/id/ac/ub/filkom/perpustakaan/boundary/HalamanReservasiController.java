package id.ac.ub.filkom.perpustakaan.boundary;

import id.ac.ub.filkom.perpustakaan.control.OperasiGagalException;
import id.ac.ub.filkom.perpustakaan.control.ReservasiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** «boundary» penerima request untuk use case Ajukan Reservasi. */
@Controller
@RequestMapping("/reservasi")
public class HalamanReservasiController {

    private final ReservasiService reservasiService;     // «control»

    public HalamanReservasiController(ReservasiService reservasiService) {
        this.reservasiService = reservasiService;
    }

    @PostMapping
    public String ajukan(@RequestParam String idBuku,
                         HttpSession session, RedirectAttributes redirect) {
        String idAnggota = (String) session.getAttribute("idAnggota");
        if (idAnggota == null) {
            return "redirect:/login";
        }
        try {
            String nomor = reservasiService.ajukanReservasi(idAnggota, idBuku);
            redirect.addFlashAttribute("pesanSukses",
                    "Reservasi berhasil diajukan. Nomor reservasi: " + nomor);
        } catch (OperasiGagalException e) {
            redirect.addFlashAttribute("pesan", e.getMessage());
        }
        return "redirect:/pinjam";                       // pola Post-Redirect-Get
    }
}
