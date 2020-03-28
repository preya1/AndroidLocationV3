package com.example.ecv4.Model;

public class Cos
{
    private String produsid,produsnume,pret,cantitate,discount;

    public Cos() {
    }

    public Cos(String produsid, String produsnume, String pret, String cantitate, String discount) {
        this.produsid = produsid;
        this.produsnume = produsnume;
        this.pret = pret;
        this.cantitate = cantitate;
        this.discount = discount;
    }

    public String getProdusid() {
        return produsid;
    }

    public void setProdusid(String produsid) {
        this.produsid = produsid;
    }

    public String getProdusnume() {
        return produsnume;
    }

    public void setProdusnume(String produsnume) {
        this.produsnume = produsnume;
    }

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

    public String getCantitate() {
        return cantitate;
    }

    public void setCantitate(String cantitate) {
        this.cantitate = cantitate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
