CREATE TABLE IF NOT EXISTS kupoprodajni_ugovor (
    id SERIAL PRIMARY KEY,
    kupac TEXT NOT NULL,
    broj_ugovora TEXT NOT NULL,
    datum_akontacije DATE NOT NULL,
    rok_isporuke DATE NOT NULL,
    status VARCHAR (50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS artikl (
    id SERIAL PRIMARY KEY,
    naziv TEXT NOT NULL,
    dobavljac TEXT,
    kolicina INT NOT NULL,
    status VARCHAR (50) NOT NULL,
    ugovor_id INT REFERENCES kupoprodajni_ugovor(id)
);