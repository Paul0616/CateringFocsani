package ro.duoline.cateringfocsani;

/**
 * Created by Paul on 27.05.2017.
 */

public class ProfileValue {
    private String nume;
    private String email;
    private String telefon;
    private boolean ckecked;

    public String getNume(){return nume;}
    public String getEmail(){return email;}
    public String getTelefon(){return telefon;}
    public boolean getChecked(){return ckecked;}

    public void setNume(String nume){this.nume = nume;}
    public void setEmail(String email){this.email = email;}
    public void setTelefon(String telefon){this.telefon = telefon;}
    public void setChecked(boolean checked){this.ckecked = checked;}
}
