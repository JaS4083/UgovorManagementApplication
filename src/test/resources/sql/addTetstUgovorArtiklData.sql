INSERT INTO kupoprodajni_ugovor (id, kupac, broj_ugovora, datum_akontacije, rok_isporuke, status, is_active, is_deleted) VALUES
(1, 'Petra Kranjčar', '1/2024', '2024-01-04', '2024-04-20', 'KREIRANO', TRUE, FALSE),
(2, 'Franko Kasun', '2/2024', '2024-03-01', '2024-05-01', 'ISPORUCENO', FALSE, FALSE),
(3, 'Stjepan Babić', '3/2024', '2024-03-03', '2024-04-15', 'NARUCENO', TRUE, FALSE),
(4, 'Tia Janković', '4/2024', '2024-03-14', '2024-08-13', 'KREIRANO', TRUE, FALSE);

INSERT INTO artikl (id, naziv, dobavljac, kolicina, status, ugovor_id) VALUES
(101, 'Perilica posuđa ugradbena Electrolux EEA27200L', 'Sancta Domenica', 1, 'KREIRANO', 1),
(102, 'Perilica posuđa ugradbena Electrolux EEA27200L', 'Sancta Domenica', 1, 'KREIRANO', 4),
(103, 'Napa ugradbena Gorenje TH60E3X', 'Sancta Domenica', 1, 'NARUCENO', 3),
(104, 'Ploča ugradbena kombinirana Gorenje GCE691BSC', 'Bijela tehnika', 1, 'ISPORUCENO', 2);