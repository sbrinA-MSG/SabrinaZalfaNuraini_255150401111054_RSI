package id.ac.ub.filkom.perpustakaan.boundary;

import id.ac.ub.filkom.perpustakaan.control.OperasiGagalException;
import id.ac.ub.filkom.perpustakaan.control.StokService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** «boundary» HalamanStok - use case Kelola Stok Buku (aktor Pustakawan). */
@Controller
@RequestMapping("/stok")
public class HalamanStokController {

    private final StokService stokService;               // «control»

    public HalamanStokController(StokService stokService) {
        this.stokService = stokService;
    }

    @GetMapping
    public String tampilHalaman(Model model) {
        model.addAttribute("daftarBuku", stokService.daftarBuku());
        return "stok";                                   // templates/stok.html
    }

    @PostMapping("/tambah-buku")
    public String tambahBuku(@RequestParam String idBuku,
                             @RequestParam String judul,
                             @RequestParam int stok,
                             RedirectAttributes redirect) {
        try {
            stokService.tambahBuku(idBuku, judul, stok);
            redirect.addFlashAttribute("pesanSukses", "Buku berhasil ditambahkan");
        } catch (OperasiGagalException e) {
            redirect.addFlashAttribute("pesan", e.getMessage());
        }
        return "redirect:/stok";
    }

    @PostMapping("/tambah-stok")
    public String tambahStok(@RequestParam String idBuku,
                             @RequestParam int jumlah,
                             RedirectAttributes redirect) {
        try {
            stokService.tambahStok(idBuku, jumlah);
            redirect.addFlashAttribute("pesanSukses", "Stok berhasil ditambah");
        } catch (OperasiGagalException e) {
            redirect.addFlashAttribute("pesan", e.getMessage());
        }
        return "redirect:/stok";
    }
}
