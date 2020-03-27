package com.example.ecv4.Model;

public class Utilizatori
{
    private String nume,telefon,parola,imagine,adresa;


    public Utilizatori() {

    }

    public Utilizatori(String nume, String telefon, String parola, String imagine, String adresa) {
        this.nume = nume;
        this.telefon = telefon;
        this.parola = parola;
        this.imagine = imagine;
        this.adresa = adresa;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getImagine() {
        return imagine;
    }

    public void setImagine(String imagine) {
        this.imagine = imagine;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }
}
