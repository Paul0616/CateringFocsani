package ro.duoline.cateringfocsani;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paul on 28.05.2017.
 */

public class IstoricComenziValues {
    private JSONObject istoric;
    private Boolean child, visible;

    public IstoricComenziValues(JSONObject istoric, Boolean child, Boolean visible){
        this.istoric = istoric;
        this.child = child;
        this.visible = visible;
    }

    public Boolean isChild(){
        if(child) return true;
        return false;
    }

    public Boolean isVisible(){
        if(visible) return true;
        return false;
    }

    public void setVisible(Boolean vis){
        this.visible = vis;
    }

    public int getID(){
        int id = 0;
        try {
            id = istoric.getInt("id");
        } catch(JSONException e){
            e.printStackTrace();
        }
        return id;
    }

    public String getDenumireProdus(){
        if(istoric == null) {
            return null;
        } else {
            String denumire = "";
            try {
                denumire = istoric.getString("denumire");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return denumire;
        }
    }
    public String getBucati(){
        if(istoric == null) {
            return null;
        } else {
            String bucati = "";
            try {
                bucati = istoric.getString("cantitate");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return bucati;
        }
    }

    public String getPret(){
        if(istoric == null) {
            return null;
        } else {
            String pret = "";
            try {
                pret = istoric.getString("pret");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return pret;
        }
    }

    public String getCerinte(){
        if(istoric == null) {
            return null;
        } else {
            String cerinte = "";
            try {
                cerinte = istoric.getString("cerinte");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return cerinte;
        }
    }

    public String getIstoric_total(){
        if(istoric == null) {
            return null;
        } else {
            String total = "";
            try {
                total = istoric.getString("totalGeneral");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return total;
        }
    }

    public String getIstoric_date(){
        if(istoric == null) {
            return null;
        } else {
            String data = "";
            try {
                data = istoric.getString("Data");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return data;
        }
    }

    public String getIstoric_nrproduse(){
        if(istoric == null) {
            return null;
        } else {
            String nrproduse = "";
            try {
                nrproduse = istoric.getString("nrProduse");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return nrproduse;
        }
    }
}
