package id.ac.ub.filkom.perpustakaan.boundary;

import id.ac.ub.filkom.perpustakaan.control.LoginService;
import id.ac.ub.filkom.perpustakaan.control.OperasiGagalException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** «boundary» HalamanLogin - bagian penerima request. */
@Controller
public class LoginController {

    private final LoginService loginService;    // «control»

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String beranda() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String tampilLogin() {
        return "login";                          // templates/login.html
    }

    @PostMapping("/login")
    public String masuk(@RequestParam String idAnggota,
                        HttpSession session, Model model) {
        try {
            String nama = loginService.masuk(idAnggota);
            session.setAttribute("idAnggota", idAnggota.trim().toUpperCase());
            session.setAttribute("namaAnggota", nama);
            return "redirect:/pinjam";
        } catch (OperasiGagalException e) {
            model.addAttribute("pesan", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String keluar(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
