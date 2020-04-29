-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 29 Kwi 2020, 18:41
-- Wersja serwera: 10.1.37-MariaDB
-- Wersja PHP: 7.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `chat`
--
DROP DATABASE IF EXISTS `chat`;
CREATE DATABASE IF NOT EXISTS `chat` DEFAULT CHARACTER SET utf16 COLLATE utf16_polish_ci;
USE `chat`;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `groups`
--

CREATE TABLE `groups` (
  `groupname` varchar(20) COLLATE utf16_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_polish_ci;

--
-- Zrzut danych tabeli `groups`
--

INSERT INTO `groups` (`groupname`) VALUES
('HentaiAddicts');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `membership`
--

CREATE TABLE `membership` (
  `id_membership` int(11) NOT NULL,
  `id_user` varchar(20) COLLATE utf16_polish_ci NOT NULL,
  `id_group` varchar(20) COLLATE utf16_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_polish_ci;

--
-- Zrzut danych tabeli `membership`
--

INSERT INTO `membership` (`id_membership`, `id_user`, `id_group`) VALUES
(1, 'user1', 'HentaiAddicts'),
(2, 'user2', 'HentaiAddicts');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `messagesug`
--

CREATE TABLE `messagesug` (
  `id_wUG` int(11) NOT NULL,
  `fromUser` varchar(20) COLLATE utf16_polish_ci NOT NULL,
  `toGroup` varchar(20) COLLATE utf16_polish_ci NOT NULL,
  `messageTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `messageText` varchar(500) CHARACTER SET utf8 COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_polish_ci;

--
-- Zrzut danych tabeli `messagesug`
--

INSERT INTO `messagesug` (`id_wUG`, `fromUser`, `toGroup`, `messageTime`, `messageText`) VALUES
(37, 'user1', 'HentaiAddicts', '2020-04-17 08:14:24', 'Witam na grupce! :3'),
(38, 'user2', 'HentaiAddicts', '2020-04-17 08:14:24', 'O! No cześć :D\nCo tam??'),
(39, 'user1', 'HentaiAddicts', '2020-04-17 08:14:24', 'Wszystko spoczko. Oglądamy razem jakieś hentai po pracy?'),
(40, 'user1', 'HentaiAddicts', '2020-04-17 08:14:24', 'Jak nie, jak tak :D'),
(41, 'user2', 'HentaiAddicts', '2020-04-19 17:23:01', 'ñļJak nie, jak tak :D');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `messagesuu`
--

CREATE TABLE `messagesuu` (
  `id_wUU` int(11) NOT NULL,
  `fromUser` varchar(20) COLLATE utf16_polish_ci NOT NULL,
  `toUser` varchar(20) COLLATE utf16_polish_ci NOT NULL,
  `messageTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `messageText` varchar(500) COLLATE utf16_polish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_polish_ci;

--
-- Zrzut danych tabeli `messagesuu`
--

INSERT INTO `messagesuu` (`id_wUU`, `fromUser`, `toUser`, `messageTime`, `messageText`) VALUES
(1, 'user1', 'user2', '2020-04-17 07:50:29', 'Ohayoo!\n (>w<)'),
(2, 'user1', 'user2', '2020-04-17 08:17:23', 'Odpisz jak będziesz, onii-chan~<3');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `username` varchar(20) COLLATE utf16_polish_ci NOT NULL,
  `password` varchar(20) COLLATE utf16_polish_ci NOT NULL,
  `privilege` tinyint(1) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_polish_ci;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`username`, `password`, `privilege`, `active`) VALUES
('sekretariat1', 'Qwerty123', 1, 1),
('user1', 'user1!', 0, 1),
('user2', 'user2!', 0, 1);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`groupname`);

--
-- Indeksy dla tabeli `membership`
--
ALTER TABLE `membership`
  ADD PRIMARY KEY (`id_membership`),
  ADD KEY `MemGroup` (`id_group`),
  ADD KEY `MemUser` (`id_user`);

--
-- Indeksy dla tabeli `messagesug`
--
ALTER TABLE `messagesug`
  ADD PRIMARY KEY (`id_wUG`),
  ADD KEY `UserUGF` (`fromUser`),
  ADD KEY `GroupUGT` (`toGroup`);

--
-- Indeksy dla tabeli `messagesuu`
--
ALTER TABLE `messagesuu`
  ADD PRIMARY KEY (`id_wUU`),
  ADD KEY `UserUUF` (`fromUser`),
  ADD KEY `UserUUT` (`toUser`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT dla tabeli `membership`
--
ALTER TABLE `membership`
  MODIFY `id_membership` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT dla tabeli `messagesug`
--
ALTER TABLE `messagesug`
  MODIFY `id_wUG` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT dla tabeli `messagesuu`
--
ALTER TABLE `messagesuu`
  MODIFY `id_wUU` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `membership`
--
ALTER TABLE `membership`
  ADD CONSTRAINT `MemGroup` FOREIGN KEY (`id_group`) REFERENCES `groups` (`groupname`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `MemUser` FOREIGN KEY (`id_user`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `messagesug`
--
ALTER TABLE `messagesug`
  ADD CONSTRAINT `GroupUGT` FOREIGN KEY (`toGroup`) REFERENCES `groups` (`groupname`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `UserUGF` FOREIGN KEY (`fromUser`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Ograniczenia dla tabeli `messagesuu`
--
ALTER TABLE `messagesuu`
  ADD CONSTRAINT `UserUUF` FOREIGN KEY (`fromUser`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `UserUUT` FOREIGN KEY (`toUser`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
