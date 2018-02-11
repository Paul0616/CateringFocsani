package ro.duoline.papacatering.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 04.06.2017.
 */

public class RestauranteTasks {
    public static SQLiteDatabase mDb;

    public static JSONArray getVectorScor(JSONArray jArray){
        JSONArray jarrayScor = new JSONArray();
        Cursor cursor = getAllRestaurante();
        //int x = cursor.getCount();
        try {
            for (int i = 0; i < jArray.length(); i++) {
                cursor.moveToPosition(i);
                JSONObject jobj = new JSONObject();
                jobj.put("id_restaurant", cursor.getInt(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID)));
                jobj.put("scor", cursor.getInt(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_SCOR)));
                jarrayScor.put(jobj);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        cursor.close();
        return jarrayScor;
    }
    public static void initDatabase(Context context){
        RestauranteDbHelper dbHelper = new RestauranteDbHelper(context);
        mDb = dbHelper.getWritableDatabase();
    }

    synchronized public static List<ContentValues> syncRestauranteDB(Context context, List<ContentValues> listaRestauranteDePeServer){
        initDatabase(context);
        Cursor cursor = getAllRestaurante();
        int x = cursor.getCount();
        RestauranteTasks.stergeRestaurante(cursor,listaRestauranteDePeServer); //sterg din BD restaurantele care sunt in plus fata de cele de pe server
        x = cursor.getCount();
        cursor.close();
        cursor = RestauranteTasks.getAllRestaurante();
        x = cursor.getCount();
        listaRestauranteDePeServer = RestauranteTasks.adaugaRestaurante(cursor, listaRestauranteDePeServer); //adaug in BD restaurantele care lipsesc comparativ cu cele de pe server

        cursor.close();
        return listaRestauranteDePeServer;
    }

    synchronized public static Cursor getAllRestaurante(){ //toate restaurantele sortate dupa campul scor

        return mDb.query(
                RestauranteContract.RestauranteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                RestauranteContract.RestauranteEntry.COLUMN_SCOR + " DESC"
        );
    }

    synchronized public static Cursor getRestaurantById(int id){
        String selection = RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID + "=" + Integer.toString(id);
        return mDb.query(
                RestauranteContract.RestauranteEntry.TABLE_NAME,
                null,
                selection,
                null,
                null,
                null,
                null
        );

    }
    synchronized private static void stergeRestaurante(Cursor cursor, List<ContentValues> listaIncarcata){ //Sterge din DB restaurantele care nu sunt in listaRestauranteDePeServer
        int id_DB;
        List<Integer> rowsForDelete = new ArrayList<Integer>();
        String restName_DB;
        for(int i =0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            id_DB = cursor.getInt(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID));
            restName_DB = cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME));
            if(!elementCursorIsInList(id_DB,restName_DB, listaIncarcata)){
                rowsForDelete.add(cursor.getInt(cursor.getColumnIndex(RestauranteContract.RestauranteEntry._ID)));
            }

        }
        for(int i =0; i<rowsForDelete.size(); i++) {
            mDb.delete(RestauranteContract.RestauranteEntry.TABLE_NAME, RestauranteContract.RestauranteEntry._ID + "=" + rowsForDelete.get(i), null);
        }
    }

    synchronized public static void adaugaMesaje(List<ContentValues> lista){
        mDb.delete(RestauranteContract.MesajeEntry.TABLE_NAME, null, null);
        for(int i=0; i<lista.size(); i++) {
            mDb.insert(RestauranteContract.MesajeEntry.TABLE_NAME, null, lista.get(i));
        }
    }

    synchronized public static List<ContentValues> getAllMesaje(){
        List<ContentValues> list = new ArrayList<ContentValues>();
        String sql = "SELECT " + RestauranteContract.MesajeEntry.TABLE_NAME + ".*, " +
                RestauranteContract.RestauranteEntry.TABLE_NAME + "." + RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME +
                ", " + RestauranteContract.RestauranteEntry.TABLE_NAME + "." + RestauranteContract.RestauranteEntry.COLUMN_LIVRARE +
                ", " + RestauranteContract.RestauranteEntry.TABLE_NAME + "." + RestauranteContract.RestauranteEntry.COLUMN_DATABASE +
                ", " + RestauranteContract.RestauranteEntry.TABLE_NAME + "." + RestauranteContract.RestauranteEntry.COLUMN_USER +
                ", " + RestauranteContract.RestauranteEntry.TABLE_NAME + "." + RestauranteContract.RestauranteEntry.COLUMN_PASS +
                ", " + RestauranteContract.RestauranteEntry.TABLE_NAME + "." + RestauranteContract.RestauranteEntry.COLUMN_IP +
                " FROM " + RestauranteContract.MesajeEntry.TABLE_NAME + " INNER JOIN " + RestauranteContract.RestauranteEntry.TABLE_NAME + " ON " + RestauranteContract.MesajeEntry.TABLE_NAME +
                "." + RestauranteContract.MesajeEntry.COLUMN_REATAURANT_ID + " = " + RestauranteContract.RestauranteEntry.TABLE_NAME + "." + RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID;
       Cursor cursor = mDb.rawQuery(sql, null);
        for(int i = 0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            ContentValues cv = new ContentValues();
            cv.put("id", cursor.getInt(cursor.getColumnIndex(RestauranteContract.MesajeEntry.COLUMN_REATAURANT_ID)));
            cv.put("mesaj", cursor.getString(cursor.getColumnIndex(RestauranteContract.MesajeEntry.COLUMN_MESAJ)));
            cv.put("denumire", cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME)));
            cv.put("livrare", cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_LIVRARE)));

            cv.put(RestauranteContract.RestauranteEntry.COLUMN_DATABASE, cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_DATABASE)));
            cv.put(RestauranteContract.RestauranteEntry.COLUMN_USER, cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_USER)));
            cv.put(RestauranteContract.RestauranteEntry.COLUMN_PASS, cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_PASS)));
            cv.put(RestauranteContract.RestauranteEntry.COLUMN_IP, cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_IP)));
            list.add(cv);
        }

        return list;
    }

    private static Boolean elementCursorIsInList(int id_DB, String restName_DB, List<ContentValues> list){ //verifica daca un anumit id din DB se regaseste intro lista
        int id_list;
        String restName_list;
        Boolean esteInLista = false;
        for(int i = 0; i<list.size(); i++){
            id_list = Integer.parseInt(list.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID).toString());
            restName_list = list.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME).toString();
            if(id_DB == id_list && restName_DB.equals(restName_list)){
                esteInLista = true;
                break;
            }
        }
        return esteInLista;
    }

    private static Boolean elementListIsInCursor(int id_list, String restName_list, Cursor cursor){ //verifica daca un anumit id lista se regaseste in DB
        int id_DB;
        String restName_DB;
        Boolean esteInLista = false;
        for(int i = 0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            id_DB = cursor.getInt(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID));;
            restName_DB = cursor.getString(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME));
            if(id_DB == id_list && restName_DB.equals(restName_list)){
                esteInLista = true;
                break;
            }
        }
        return esteInLista;
    }

    synchronized private static List<ContentValues> adaugaRestaurante(Cursor cursor, List<ContentValues> listaIncarcata){
        int id_list;
        String restName_list;
        ContentValues cv;
        Boolean restaurantNou = false;
        for(int i = 0; i < listaIncarcata.size(); i++){
            id_list = Integer.parseInt(listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID).toString());
            restName_list = listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME).toString();
            if(!elementListIsInCursor(id_list,restName_list,cursor)){
                restaurantNou = true;
            } else {
                restaurantNou = false;
                cv = new ContentValues();
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_VALOARE_MINIMA_LIVRARE,
                        Integer.parseInt(listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_VALOARE_MINIMA_LIVRARE).toString()));
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_CATERING,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_CATERING).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_REZERVARE,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_REZERVARE).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_LIVRARE,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_LIVRARE).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_DATABASE,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_DATABASE).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_USER,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_USER).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_PASS,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_PASS).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_IP,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_IP).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_START_CATERING,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_START_CATERING).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_END_CATERING,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_END_CATERING).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_START_REZERVARI,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_START_REZERVARI).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_END_REZERVARI,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_END_REZERVARI).toString());
                mDb.update(RestauranteContract.RestauranteEntry.TABLE_NAME, cv,
                        RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID + "=" + cursor.getInt(cursor.getColumnIndex(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID)),
                        null);
            }

            if(restaurantNou){
                cv = listaIncarcata.get(i);
                cv.put("nou", 1);
            } else {
                cv = listaIncarcata.get(i);
                cv.put("nou", 0);
            }
        }

        for(int i =0; i<listaIncarcata.size(); i++){

            if(listaIncarcata.get(i).getAsInteger("nou")==1) {
                cv = new ContentValues();
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID,
                        Integer.parseInt(listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID).toString()));
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_SCOR, 0);
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_VALOARE_MINIMA_LIVRARE,
                        Integer.parseInt(listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_VALOARE_MINIMA_LIVRARE).toString()));
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_CATERING,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_CATERING).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_REZERVARE,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_MESAJ_REZERVARE).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_LIVRARE,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_LIVRARE).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_DATABASE,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_DATABASE).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_USER,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_USER).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_PASS,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_PASS).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_IP,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_IP).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_START_CATERING,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_START_CATERING).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_END_CATERING,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_END_CATERING).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_START_REZERVARI,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_START_REZERVARI).toString());
                cv.put(RestauranteContract.RestauranteEntry.COLUMN_END_REZERVARI,
                        listaIncarcata.get(i).get(RestauranteContract.RestauranteEntry.COLUMN_END_REZERVARI).toString());
                mDb.insert(RestauranteContract.RestauranteEntry.TABLE_NAME, null, cv);
            }
        }

        return listaIncarcata;
    }
}
