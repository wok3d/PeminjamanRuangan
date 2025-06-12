# PeminjamanRuangan
Aplikasi Peminjaman Ruangan Gedung Dewi Sartika


## Database
### Pertama masukan query dari database di bawah ini

Create database peminjaman_ruangan;

use peminjaman_ruangan;

CREATE TABLE `bookings` (
  `id` int(11) NOT NULL,
  `room_name` varchar(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `timeslot` varchar(20) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `rooms` (
  `id` int(11) NOT NULL,
  `name` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `bookings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `rooms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

## Buka Di Intellij
### Tahap 1: Download
https://downloads.mysql.com/archives/get/p/3/file/mysql-connector-j-9.2.0.tar.gz

### Tahap 2: Ekstrak File

### Tahap 3: Open Intellij
- ctrl + shift + alt + s
- Buka SDKs
- copy JDK home path

- Buka Libraries
- Tambah (+)
- Java
- Paste JDK home path 
- OK
- Pilih Jar Directory
- OK

- Tambah (+)
- Java
- Paste path dari mysql-connector-j-9.2.0 yang sudah di ekstrak