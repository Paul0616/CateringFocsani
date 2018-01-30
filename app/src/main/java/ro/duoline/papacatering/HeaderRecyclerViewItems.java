package ro.duoline.papacatering;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Paul on 27.05.2017.
 */

public class HeaderRecyclerViewItems {
    private String header;
    private JSONObject object;

    public HeaderRecyclerViewItems (JSONObject object, String header){
        this.object = object;
        this.header = header;
    }



    public String getHeader(){
        return header;
    }

    public String getDenumireRestaurant(){
        if(object == null) {
            return "";
        } else {
            String denumire = "";
            try {
                denumire = object.getString("denumire");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return denumire;
        }
    }

    public ArrayList<String> getDateConectare(){
        if(object == null) {
            return null;
        } else {
            ArrayList<String> stringIP = new ArrayList<String>();
            try {
                String string = object.getString("dbname_ip");
                stringIP.add(string);
                string = object.getString("dbname");
                stringIP.add(string);
                string = object.getString("passw");
                stringIP.add(string);
                string = object.getString("ip");
                stringIP.add(string);

            }catch(JSONException e){
                e.printStackTrace();
            }
            return stringIP;
        }
    }
    public String getLocatiiLivrare(){
        if(object == null) {
            return "";
        } else {
            String livrare = "";
            try {
                livrare = object.getString("locatii_livrare");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return livrare;
        }
    }

    public String getPoza(){
        if(object == null) {
            return "";
        } else {
            String poza = "";
            try {
                poza = object.getString("poza");
            }catch(JSONException e){
                e.printStackTrace();
            }
            return poza;
        }
    }

    public Boolean areCatering(){
        if(object == null) {
            return false;
        } else {
            Boolean catering = false;
            try {

                if (object.getString("catering").equals("1")) {
                    catering = true;
                } else {
                    catering =  false;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return catering;

        }
    }

    public Boolean areRezervari(){
        if(object == null) {
            return false;
        } else {
            Boolean rezervari = false;
            try {
                if (object.getString("rezervari").equals("1")) {
                    rezervari = true;
                } else {
                    rezervari =  false;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return rezervari;
        }
    }

    public int getRestaurantId(){
        if(object == null) {
            return 0;
        } else {
            int id = 0;
            try {
                id = Integer.parseInt(object.getString("id"));
            } catch (JSONException e){
                e.printStackTrace();
            }
            return id;
        }
    }
}
