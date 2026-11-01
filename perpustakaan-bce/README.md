# Perpustakaan BCE (Spring Boot)

Aplikasi perpustakaan sederhana dengan pola Boundary-Control-Entity.

## Menjalankan
1. Pasang JDK 17+ dan Maven (atau buka folder ini di IntelliJ / VS Code / Eclipse).
2. `mvn spring-boot:run`
3. Buka http://localhost:8080 lalu masuk dengan ID `A001` (batas 2) atau `A002` (batas 1).
4. Halaman pustakawan: http://localhost:8080/stok
5. Konsol database: http://localhost:8080/h2-console  (JDBC URL `jdbc:h2:mem:perpustakaan`, user `sa`, tanpa password)
6. Unit test: `mvn test`

## Struktur
- `boundary/` : @Controller (penerima request) + template di `resources/templates`
- `control/`  : @Service (alur use case), Bukti, exception
- `entity/`   : @Entity + Repository
