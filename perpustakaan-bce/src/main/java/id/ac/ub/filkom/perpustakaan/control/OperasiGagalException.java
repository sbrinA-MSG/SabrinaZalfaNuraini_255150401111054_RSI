package id.ac.ub.filkom.perpustakaan.control;

/** Dilempar service (login, reservasi, kelola stok) bila aturan bisnis dilanggar. */
public class OperasiGagalException extends RuntimeException {

    public OperasiGagalException(String pesan) {
        super(pesan);
    }
}
