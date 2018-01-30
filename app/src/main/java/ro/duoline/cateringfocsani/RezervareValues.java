package ro.duoline.cateringfocsani;

import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Paul on 27.05.2017.
 */

public class RezervareValues {
    private String locatie, telefon;
    private Date datasioraRezervarii;
    private Integer nrPersoane;
    private String srcLocatie1, srcLocatie2, srcLocatie3;

    public RezervareValues(){
        locatie = "";
        datasioraRezervarii = null;
        nrPersoane = 0;
        srcLocatie1 = "";
        srcLocatie2 = "";
        srcLocatie3 = "";
        telefon = "0754 376 891";
    }

    public void setLocatie(String locatie){this.locatie = locatie;}
    public void setTelefon(String telefon){this.telefon = telefon;}
    public void setsrcLocatie1(String locatie){this.srcLocatie1 = locatie;}
    public void setsrcLocatie2(String locatie){this.srcLocatie2 = locatie;}
    public void setsrcLocatie3(String locatie){this.srcLocatie3 = locatie;}


    public void setDate(Date date){this.datasioraRezervarii = date;}
    public void setNrPersoane(Integer nrpersoane){this.nrPersoane = nrpersoane;}
    public String getLocatie(){return locatie;}
    public String getTelefon(){return telefon;}
    public String getsrcLocatie1(){return srcLocatie1;}
    public String getsrcLocatie2(){return srcLocatie2;}
    public String getsrcLocatie3(){return srcLocatie3;}

    public String getFullLocatie(){
        if(locatie == "RESTAURANT"){
            return locatie + " Casa Vranceana";
        } else if(locatie == "BISTRO"){
            return locatie + " Urban";
        } else return locatie;
    }
    public String getFinalString(){
        String txt = "Rezervare:";
        if(locatie != "") txt +=  "\n" + getFullLocatie();
        if(datasioraRezervarii != null) txt += "\n" + getStringDate();
        if(nrPersoane != 0) txt += "\npentru " + getNrPersoane() + " pers.";
        return txt;
    }
    public Date getDate(){return datasioraRezervarii;}
    public String getStringDate(){

        if(datasioraRezervarii != null) {
            String dateF = DateFormat.format("EEEE, d MMM yyyy, HH:mm", datasioraRezervarii).toString();
            return dateF;
        }
        else return "";
    }
    public String getStringDateMYSQL(){

        if(datasioraRezervarii != null) {

            String dateF = DateFormat.format("yyyy-MM-dd HH:mm", datasioraRezervarii).toString();
            return dateF;
        }
        else return "";
    }
    public Integer getNrPersoane(){return nrPersoane;}

    public Boolean dateComplete(){
        if(locatie != "" && datasioraRezervarii !=null && nrPersoane!=0) return true;
        else return false;
    }

    public List<String> getLocatiePoza(String srcLocatie){
        List<String> myList = new ArrayList<String>(Arrays.asList(srcLocatie.split(",")));
        return myList;
    }
}
