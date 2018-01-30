package ro.duoline.papacatering;

/**
 * Created by Paul on 28.05.2017.
 */

public class FelMeniuValues {
    private String categorie;
    private String denumire_produs;
    private String descriere_produs;
    private Float pret_bucata;
    private Integer cod;
    private String um;
    private String poza_url;
    private Integer buc_comandate;
    private String  cerinte_spaeciale;

    public String getCerinte(){
        return cerinte_spaeciale;
    }

    public void setCerinte(String cerinte){
        this.cerinte_spaeciale = cerinte;
    }

    public String getCategorie(){
        return categorie;
    }

    public void setCategorie(String categorie){
        this.categorie = categorie;
    }

    public String getDenumire(){
        return denumire_produs;
    }

    public void setDenumire(String denumire){
        this.denumire_produs = denumire;
    }

    public String getDescriere(){
        return descriere_produs;
    }

    public void setDescriere(String descriere){
        this.descriere_produs = descriere;
    }

    public Float getPret(){
        return pret_bucata;
    }

    public void setPret(Float pret){
        this.pret_bucata = pret;
    }

    public String getUM(){
        return um;
    }

    public void setUM(String um){
        this.um = um;
    }

    public Integer getCod(){
        return cod;
    }

    public void setCod(Integer cod){
        this.cod = cod;
    }

    public String getPozaURL(){
        return poza_url;
    }

    public void setPozaURL(String poza_url){
        this.poza_url = poza_url;
    }

    public Integer getBucComandate(){
        return buc_comandate;
    }

    public void setBucComandate(Integer buc_comandate){
        this.buc_comandate = buc_comandate;
    }
}
