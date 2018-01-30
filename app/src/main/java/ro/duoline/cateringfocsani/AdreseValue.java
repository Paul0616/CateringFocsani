package ro.duoline.cateringfocsani;

/**
 * Created by Paul on 28.05.2017.
 */

public class AdreseValue {
    private String adresa;
    private String localitatea;
    private boolean ckecked;

    public String getAdresa(){return adresa;}
    public String getLocalitatea(){return localitatea;}
    public boolean getChecked(){return ckecked;}

    public void setAdresa(String adresa){this.adresa = adresa;}
    public void setLocalitatea(String localitatea){this.localitatea = localitatea;}
    public void setChecked(boolean checked){this.ckecked = checked;}
}
