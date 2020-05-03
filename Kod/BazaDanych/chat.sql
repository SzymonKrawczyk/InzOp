-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 03 Maj 2020, 20:07
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
('AnimeGirls'),
('AnimeLovers'),
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
(41, 'user2', 'HentaiAddicts', '2020-04-19 17:23:01', 'ñļJak nie, jak tak :D'),
(42, 'user1', 'HentaiAddicts', '2020-05-03 14:33:42', 'siema'),
(43, 'user1', 'HentaiAddicts', '2020-05-03 14:35:12', 'test!ññtest!!ññtest!!'),
(44, 'user1', 'HentaiAddicts', '2020-05-03 14:35:18', 'działa!'),
(45, 'user1', 'HentaiAddicts', '2020-05-03 15:05:01', 'hej'),
(46, 'user1', 'HentaiAddicts', '2020-05-03 15:05:26', 'hej'),
(47, 'user2', 'HentaiAddicts', '2020-05-03 15:05:28', 'hej'),
(48, 'user1', 'HentaiAddicts', '2020-05-03 15:05:32', 'co tam'),
(49, 'user1', 'HentaiAddicts', '2020-05-03 15:05:33', '?'),
(50, 'user2', 'HentaiAddicts', '2020-05-03 15:05:38', 'działa'),
(51, 'user1', 'HentaiAddicts', '2020-05-03 15:05:43', 'lol xDñdziała? armaty nawet'),
(52, 'user2', 'HentaiAddicts', '2020-05-03 15:07:23', 'hej'),
(53, 'user1', 'HentaiAddicts', '2020-05-03 15:07:37', 'xad'),
(54, 'user2', 'HentaiAddicts', '2020-05-03 15:07:42', 'ok'),
(55, 'user2', 'HentaiAddicts', '2020-05-03 15:09:30', 'hej'),
(56, 'user2', 'HentaiAddicts', '2020-05-03 15:09:45', 'zaśmiecę całą bazę danych'),
(57, 'user2', 'HentaiAddicts', '2020-05-03 15:09:52', 'aaa'),
(58, 'user2', 'HentaiAddicts', '2020-05-03 15:09:55', 'bbb'),
(59, 'user2', 'HentaiAddicts', '2020-05-03 15:41:10', 'ho'),
(60, 'user2', 'HentaiAddicts', '2020-05-03 15:41:31', 'ho'),
(61, 'user2', 'HentaiAddicts', '2020-05-03 15:41:34', 'ho'),
(62, 'user2', 'HentaiAddicts', '2020-05-03 15:49:12', 'aaa'),
(63, 'user2', 'HentaiAddicts', '2020-05-03 15:49:17', 'asda'),
(64, 'user2', 'HentaiAddicts', '2020-05-03 15:49:28', 'aaa'),
(65, 'user2', 'HentaiAddicts', '2020-05-03 15:49:44', 'hhe'),
(66, 'user2', 'HentaiAddicts', '2020-05-03 15:50:40', 'aaa'),
(67, 'user1', 'HentaiAddicts', '2020-05-03 15:50:51', 'siemaaaa'),
(68, 'user2', 'HentaiAddicts', '2020-05-03 15:50:57', 'aaa'),
(69, 'user1', 'HentaiAddicts', '2020-05-03 15:50:57', 'co tam'),
(70, 'user1', 'HentaiAddicts', '2020-05-03 15:50:59', 'Marcinku'),
(71, 'user1', 'HentaiAddicts', '2020-05-03 15:51:00', '?'),
(72, 'user1', 'HentaiAddicts', '2020-05-03 15:51:04', 'xD'),
(73, 'user1', 'HentaiAddicts', '2020-05-03 15:51:07', 'lol'),
(74, 'user2', 'HentaiAddicts', '2020-05-03 15:51:10', 'XDDDDDDD'),
(75, 'user2', 'HentaiAddicts', '2020-05-03 15:51:15', 'XXDDD'),
(76, 'user2', 'HentaiAddicts', '2020-05-03 15:55:37', 'he'),
(77, 'user2', 'HentaiAddicts', '2020-05-03 15:57:19', 'h'),
(78, 'user2', 'HentaiAddicts', '2020-05-03 15:57:34', 'hh'),
(79, 'user1', 'HentaiAddicts', '2020-05-03 15:57:41', 'asdasd'),
(80, 'user2', 'HentaiAddicts', '2020-05-03 15:57:45', 'h'),
(81, 'user2', 'HentaiAddicts', '2020-05-03 15:58:42', 'hh'),
(82, 'user2', 'HentaiAddicts', '2020-05-03 15:58:50', 'hh'),
(83, 'user1', 'HentaiAddicts', '2020-05-03 16:01:15', '1232323'),
(84, 'user2', 'HentaiAddicts', '2020-05-03 16:06:42', 'hi');

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
(2, 'user1', 'user2', '2020-04-17 08:17:23', 'Odpisz jak będziesz, onii-chan~<3'),
(3, 'user1', 'user2', '2020-05-02 18:14:58', 'Test'),
(4, 'user1', 'user2', '2020-05-02 18:45:37', 'hej hej hej hej co tamñżyjesz? tam?'),
(5, 'user1', 'user2', '2020-05-02 18:46:05', 'aaaañbbbb'),
(6, 'user2', 'user1', '2020-05-02 18:47:37', 'asd'),
(7, 'user1', 'user2', '2020-05-02 18:47:51', 'aaañaaa'),
(8, 'user2', 'user1', '2020-05-02 18:48:08', 'asd'),
(9, 'user2', 'user1', '2020-05-02 18:48:18', 'asdqw'),
(10, 'user2', 'user1', '2020-05-02 18:48:24', 'asdf'),
(11, 'user2', 'user1', '2020-05-02 18:48:27', 'asfddaf'),
(12, 'user2', 'Sekretariat1', '2020-05-02 18:48:36', 'ff'),
(13, 'user2', 'user1', '2020-05-02 18:48:41', 'fff'),
(14, 'user2', 'user1', '2020-05-02 18:48:48', 'asd'),
(15, 'user2', 'user1', '2020-05-02 18:49:06', 'aaa'),
(16, 'user2', 'user1', '2020-05-02 18:51:33', 'asd'),
(17, 'user2', 'Sekretariat1', '2020-05-02 18:51:55', 'zxcas'),
(18, 'user2', 'user1', '2020-05-02 18:51:58', 'asda'),
(19, 'user2', 'user1', '2020-05-02 18:52:12', 'asd'),
(20, 'user2', 'user1', '2020-05-02 18:52:21', 'sd'),
(21, 'user2', 'user1', '2020-05-02 18:52:25', 'asd'),
(22, 'user2', 'user1', '2020-05-02 18:52:27', 'asd'),
(23, 'user2', 'user1', '2020-05-02 18:52:38', 'to są notatki'),
(24, 'user2', 'user1', '2020-05-02 18:53:10', 'dsfsd'),
(25, 'user1', 'Sekretariat1', '2020-05-02 18:57:14', 'asddsa'),
(26, 'user1', 'user2', '2020-05-02 18:57:17', 'asdas'),
(27, 'user1', 'user2', '2020-05-02 18:57:21', 'asdasd'),
(28, 'user1', 'user2', '2020-05-02 18:57:23', 'asd'),
(29, 'user1', 'user2', '2020-05-02 18:57:28', 'aaaaaaaaa'),
(30, 'user2', 'user1', '2020-05-02 19:09:58', 'User2123'),
(31, 'user2', 'user1', '2020-05-02 19:10:06', 'fdgdf'),
(32, 'user2', 'user1', '2020-05-02 19:10:27', 'rgrtgrt'),
(33, 'user1', 'user2', '2020-05-02 19:12:32', 'fsd'),
(34, 'user1', 'user2', '2020-05-02 19:23:56', 'asas'),
(35, 'user1', 'user2', '2020-05-02 19:24:01', 'SIEEEEEMA'),
(36, 'user1', 'user2', '2020-05-02 19:24:36', 'HEjo'),
(37, 'user2', 'user1', '2020-05-03 15:00:24', 'aaa'),
(38, 'user2', 'user1', '2020-05-03 15:00:39', 'Hejo'),
(39, 'user2', 'user1', '2020-05-03 15:00:48', 'aaaa'),
(40, 'user2', 'user1', '2020-05-03 15:04:25', 'aaaaa'),
(41, 'user1', 'user2', '2020-05-03 15:04:33', 'asas'),
(42, 'user1', 'user2', '2020-05-03 15:04:41', 'co tam'),
(43, 'user2', 'user1', '2020-05-03 15:04:54', 'gdzie ten błąd jest?'),
(44, 'user2', 'user1', '2020-05-03 15:08:02', 'Hej'),
(45, 'user2', 'user1', '2020-05-03 15:08:09', 'hej2'),
(46, 'user2', 'user1', '2020-05-03 15:08:16', 'hej3'),
(47, 'user1', 'user2', '2020-05-03 15:08:36', ':c'),
(48, 'user2', 'user1', '2020-05-03 15:09:08', 'jesteś nieszczęśliwy że działa'),
(49, 'Sekretariat1', 'user2', '2020-05-03 15:26:41', 'Siema'),
(50, 'Sekretariat1', 'user2', '2020-05-03 15:26:44', 'tu admin'),
(51, 'user2', 'user1', '2020-05-03 15:29:45', 'hej'),
(52, 'user2', 'user1', '2020-05-03 15:30:02', 'hej'),
(53, 'user2', 'user1', '2020-05-03 15:30:07', 'etwgrgergdfg'),
(54, 'user2', 'user1', '2020-05-03 15:30:35', 'hej'),
(55, 'user2', 'user1', '2020-05-03 15:32:48', 'hej'),
(56, 'user2', 'user1', '2020-05-03 15:33:09', 'hej'),
(57, 'user2', 'user1', '2020-05-03 15:33:24', 'hej'),
(58, 'user1', 'user2', '2020-05-03 15:33:45', 'siema'),
(59, 'user1', 'user2', '2020-05-03 15:33:54', 'siema'),
(60, 'user2', 'Sekretariat1', '2020-05-03 15:34:34', 'hej'),
(61, 'user2', 'user1', '2020-05-03 15:34:40', 'hej'),
(62, 'user2', 'user1', '2020-05-03 15:34:48', 'hej'),
(63, 'user2', 'user1', '2020-05-03 15:34:58', 'hej'),
(64, 'user1', 'user2', '2020-05-03 15:35:07', '.'),
(65, 'user1', 'user2', '2020-05-03 15:35:08', '.'),
(66, 'user1', 'user2', '2020-05-03 15:35:08', '.'),
(67, 'user1', 'user2', '2020-05-03 15:35:09', '.'),
(68, 'user2', 'user1', '2020-05-03 15:35:19', 'hej'),
(69, 'user2', 'user1', '2020-05-03 15:35:35', 'hej hej'),
(70, 'user2', 'user1', '2020-05-03 15:36:49', 'hej'),
(71, 'user2', 'user1', '2020-05-03 15:38:08', 'jo'),
(72, 'user2', 'user1', '2020-05-03 15:38:20', 'jojo'),
(73, 'user2', 'user1', '2020-05-03 15:39:26', 'hej'),
(74, 'user2', 'user1', '2020-05-03 15:39:37', 'hehhe'),
(75, 'user2', 'user1', '2020-05-03 15:39:43', 'hgh'),
(76, 'user2', 'user1', '2020-05-03 15:41:46', 'napisałem'),
(77, 'user1', 'user2', '2020-05-03 15:41:50', 'wiem'),
(78, 'user2', 'user1', '2020-05-03 15:49:07', 'aaa'),
(79, 'user2', 'user1', '2020-05-03 15:55:46', 'hehe'),
(80, 'user2', 'user1', '2020-05-03 15:58:47', 'hh'),
(81, 'user2', 'user1', '2020-05-03 16:06:38', 'hi'),
(82, 'user2', 'user1', '2020-05-03 16:06:49', 'hh'),
(83, 'user2', 'user1', '2020-05-03 16:06:57', 'hhh'),
(84, 'user1', 'user2', '2020-05-03 16:06:59', ':3'),
(85, 'user1', 'user2', '2020-05-03 16:11:56', 'to pa');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `username` varchar(20) COLLATE utf16_polish_ci NOT NULL,
  `password` varchar(64) COLLATE utf16_polish_ci NOT NULL,
  `privilege` tinyint(1) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_polish_ci;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`username`, `password`, `privilege`, `active`) VALUES
('panMarek', '97d2fbce95be118151767f28280c159950babe3222496d1ca6c0742a06461ece', 0, 1),
('Sekretariat1', '831799514337b997347fd36e44cf6b1adb00760a507bcfa2cdb9a397da1a8ccd', 1, 1),
('Sekretariat2', 'aa045e9c157aeedb6e063e11627252afc60441a998c929aa233718b0fb2096b2', 1, 1),
('user1', '6152c082751df782c7943730ed2470cb9ffdfbddf04c8edcb9b7aeec2ae137f2', 0, 1),
('user2', '53f92365afd658eb3fc8cef9e8e32de23675293c257b47dc32279656b8df4d67', 0, 1),
('user3', 'e0679bd4d613a1fdd03bb4ab8b9a2703b81543e459fdc2a5436e4f1c51cff016', 0, 1);

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
  MODIFY `id_wUG` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=85;

--
-- AUTO_INCREMENT dla tabeli `messagesuu`
--
ALTER TABLE `messagesuu`
  MODIFY `id_wUU` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=86;

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
