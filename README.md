# Janji

*Saya Faridchi Trianda Safaraz dengan NIM 2506827 mengerjakan Tugas Praktikum 6 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin*

# Deskripsi & Desain Program

Program ini merupakan implementasi permainan **Flappy Bird** yang dibangun menggunakan **Java Swing GUI**. User mengendalikan player sprite (burung) yang harus terus terbang dan menghindari pipa yang bermunculan dari sisi kanan layar. Program ini dilengkapi menu utama serta musik latar dan efek suara.

Program terdiri dari **7** class, yaitu `App`, `Logic`, `View`, `MainMenu`, `Player`, `Pipe`, dan `Sound`. Program memiliki runnable class `App` yang akan menjalankan program utama (aplikasi, dengan membuka window).

## Struktur Folder

```
TP6DPBO2526C2
├── Dokumentasi
│   └── dokumentasitp6.mp4
├── FlappyBird
│   └── src
│       ├── App.java
│       ├── Logic.java
│       ├── View.java
│       ├── MainMenu.java
│       ├── Player.java
│       ├── Pipe.java
│       ├── Sound.java
│       └── assets
│           ├── background.png
│           ├── bird.png
│           ├── upperPipe.png
│           ├── lowerPipe.png
│           └── sound
│               ├── theme_song.wav
│               ├── flapping_wings.WAV
│               ├── score.WAV
│               ├── every_5.wav
│               └── fail.wav
└── README.md
```

## App.java

Kelas ini adalah kelas utama yang menjalankan program dengan membuat window menggunakan `JFrame`. Window dibuat dengan ukuran tetap (tidak bisa diubah user) dan program akan berhenti ketika window ditutup. Di dalam method `main`, kelas ini membuat objek `Logic` dan `View`, lalu menghubungkan keduanya dengan `setView()` agar `Logic` bisa memperbarui tampilan.

Perpindahan antara menu dan layar permainan dilakukan dengan `CardLayout` yang memiliki dua layar, yaitu `"MENU"` dan `"GAME"`. Objek `MainMenu` dibuat dengan dua `ActionListener`: tombol **Mulai Permainan** akan membuka layar permainan, memindahkan fokus keyboard ke `View`, lalu memanggil `startGame()`, sedangkan tombol **Keluar** menutup program dengan `System.exit(0)`. Seluruh proses ini dijalankan di dalam `SwingUtilities.invokeLater`.

## Logic.java

Kelas ini adalah pengatur seluruh aturan permainan dan mengimplementasikan `ActionListener` (untuk game loop) serta `KeyListener` (untuk input keyboard). Atribut yang dimiliki antara lain:

- Ukuran layar (`frameWidth`, `frameHeight`), ukuran burung, dan ukuran pipa.
- `gravity` untuk percepatan jatuh dan `pipeVelocityX` untuk kecepatan pipa ke kiri.
- Dua `Timer`, yaitu `gameLoop` yang menggerakkan permainan sekitar 60 kali per detik dan `pipesCooldown` yang memunculkan pasangan pipa baru setiap 1,5 detik.
- Objek `player`, daftar `pipes`, objek `sound`, serta penanda kondisi `score`, `gameOver`, dan `gameStarted`.

Method penting pada kelas ini:

- `startGame()` dan `restartGame()` mengatur ulang posisi burung, mengosongkan pipa, mereset skor, lalu menjalankan ulang timer. `restartGame()` juga memutar ulang musik latar.
- `placePipes()` membuat sepasang pipa (atas dan bawah) dengan posisi celah yang acak.
- `collision()` mengecek tabrakan antara burung dan pipa dengan metode kotak pembatas (*bounding box*).
- `move()` dipanggil setiap frame untuk menambah kecepatan jatuh burung dengan gravitasi, mengatur sudut kemiringan burung, menggeser pipa, menambah skor setiap burung melewati sepasang pipa, serta menentukan kondisi game over (menabrak pipa atau menyentuh dasar layar). Batas atas layar hanya menahan burung dan tidak menyebabkan game over.
- `keyPressed()` menangani input: `SPACE` membuat burung melompat dan `R` memulai ulang permainan saat game over.

## View.java

Kelas ini merupakan turunan dari `JPanel` yang bertugas menggambar tampilan permainan. `View` tidak berisi aturan permainan; seluruh data yang digambar diambil dari objek `Logic` yang diterima lewat konstruktor. Atribut yang dimiliki adalah `logic`, `backgroundImage`, dan `scoreLabel` (`JLabel` untuk menampilkan skor di atas-tengah layar yang diperbarui lewat `updateScore()`).

Method `draw()` menggambar elemen secara berurutan: latar belakang, burung (diputar sesuai `angle` sehingga terlihat miring ke atas saat melompat dan miring ke bawah saat jatuh), lalu seluruh pipa. Ketika permainan berakhir, `View` juga menampilkan kotak transparan berisi tulisan **GAME OVER** dan instruksi untuk menekan `R`. Kelas ini juga mendaftarkan `Logic` sebagai `KeyListener` dan memastikan panel mendapat fokus keyboard saat diklik.

## MainMenu.java

Kelas tambahan yang merupakan turunan dari `JPanel` untuk menampilkan menu utama sebelum permainan dimulai. Tampilannya ditata dengan `GridBagLayout` dan berisi latar belakang, judul **FLAPPY BIRD**, ikon burung, tombol **Mulai Permainan** (`startButton`), dan tombol **Keluar** (`exitButton`).

Aksi kedua tombol tidak ditulis di dalam kelas ini, melainkan diterima lewat konstruktor `MainMenu(ActionListener onStart, ActionListener onExit)` dari `App`. Dengan begitu `MainMenu` hanya fokus pada tampilan. Kelas ini juga menyediakan `getStartButton()` dan `getExitButton()`.

## Player.java

Kelas ini merepresentasikan player sprite dalam permainan, bertanggung jawab menyimpan data posisi, ukuran, gambar, kecepatan, dan sudut burung. Atributnya adalah:

- `posX` dan `posY` untuk posisi, serta `width` dan `height` untuk ukuran.
- `image` untuk gambar sprite burung.
- `velocityY` untuk kecepatan vertikal yang dipengaruhi gravitasi atau lompatan dari user.
- `angle` sebagai atribut tambahan untuk sudut kemiringan burung. Nilainya diatur oleh `Logic` dan dipakai `View` untuk memutar gambar burung saat digambar.

Konstruktor kelas ini menginisialisasi semua atribut (dengan `velocityY` dan `angle` bernilai 0 di awal), dan setiap atribut memiliki getter serta setter.

## Pipe.java

Kelas ini merepresentasikan rintangan berupa pipa yang bergerak dari kanan ke kiri layar. Dalam satu kali kemunculan, `Logic` membuat dua objek `Pipe` (pipa atas dan pipa bawah) yang membentuk celah untuk dilewati burung. Atributnya adalah:

- `posX` dan `posY` untuk posisi, serta `width` dan `height` untuk ukuran.
- `image` untuk gambar pipa (atas atau bawah).
- `velocityX` untuk kecepatan horizontal. Kecepatan gerak pipa yang sebenarnya diatur dari `Logic` lewat `pipeVelocityX`.
- `passed` untuk menandai apakah pipa sudah dilewati burung, sehingga skor hanya bertambah sekali untuk setiap pasang pipa.

Kelas ini juga menyediakan konstruktor serta getter dan setter, termasuk `isPassed()` dan `setPassed()`.

## Sound.java

Kelas tambahan yang mengelola seluruh audio permainan menggunakan `Clip` dari `javax.sound.sampled`. Atributnya adalah:

- `themeSong` untuk musik latar yang diputar berulang.
- `flapClip` untuk suara setiap burung melompat.
- `scoreClip` untuk suara saat skor bertambah.
- `every5Clip` untuk suara khusus saat skor mencapai kelipatan 5.
- `failClip` untuk suara saat game over.

Pada konstruktor, kelima file audio dimuat dari folder `assets/sound` oleh method `loadClip()`, lalu volume theme song diturunkan agar tidak menutupi efek suara. Jika sebuah file audio gagal dimuat, program tetap berjalan tanpa suara tersebut. Method `playThemeSong()`, `stopThemeSong()`, `playFlap()`, `playScore()`, `playEvery5()`, dan `playFail()` dipanggil oleh `Logic` pada saat kejadian yang sesuai.

# Penjelasan

Setelah program dijalankan, akan muncul `JFrame` window yang menampilkan menu utama permainan **Flappy Bird** dan musik latar mulai diputar. Pada menu utama terdapat judul permainan, tombol **Mulai Permainan**, dan tombol **Keluar**. Sesaat setelah tombol mulai diklik, permainan dimulai dan user harus mempertahankan posisi player sprite agar tidak mencapai kondisi **game over**.

Selama permainan, user dapat menekan tombol `space` untuk membuat player sprite mengepakkan sayapnya dan melompat agar tidak terjatuh, disertai efek suara. Skor selama permainan dapat dilihat di atas-tengah layar dan bertambah setiap player melewati sepasang pipa, dengan efek suara yang berbeda pada setiap kelipatan 5.

Ketika player sprite bertemu pada kondisi game over (menabrak pipa atau menyentuh dasar layar), musik latar berhenti, efek suara *fail* dimainkan, dan window menampilkan pesan game over. Selain itu, terdapat instruksi untuk menekan karakter `R` untuk mengulang/restart permainan.

# Dokumentasi

## Screen Record

[Klik di sini untuk menonton screen record permainan](https://github.com/safarazzz/TP6DPBO2526C2/blob/main/Dokumentasi/dokumentasitp6.mp4)