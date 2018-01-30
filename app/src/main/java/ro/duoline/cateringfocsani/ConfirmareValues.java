package ro.duoline.cateringfocsani;

/**
 * Created by Paul on 28.05.2017.
 */

public class ConfirmareValues {
    private Integer cod;
    private String denumire;
    private String cerinte;
    private Integer buc_comandate;
    private Float pret_bucata;
    private String total;

    public void setDenumire(String denumire){this.denumire = denumire;}
    public void setCerinte(String cerinte){this.cerinte = cerinte;}
    public void setBuc_comandate(Integer buc){this.buc_comandate = buc;}
    public void setCod(Integer cod){this.cod = cod;}
    public void setPret(Float pret){
        this.pret_bucata = pret;
    }
    public void setTotal(Float pret){this.total = round2((buc_comandate * pret), 2) + " RON";}

    public String getDenumire(){return denumire;}
    public String getCerinte(){return cerinte;}
    public String getTotal() {return total;}
    public Integer getBuc_comandate(){return buc_comandate;}
    public Integer getCod(){return cod;}
    public Float getPret(){
        return pret_bucata;
    }

    public float round2(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
}
