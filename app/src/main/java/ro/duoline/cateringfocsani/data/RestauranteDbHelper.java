package ro.duoline.cateringfocsani.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Paul on 03.06.2017.
 */

public class RestauranteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "restaurante.db";
    private static final int DATABASE_VERSION = 5;

    public RestauranteDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RESTAURANTE_TABLE = "CREATE TABLE " +
                RestauranteContract.RestauranteEntry.TABLE_NAME + " (" +
                RestauranteContract.RestauranteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_ID + " INTEGER NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_REATAURANT_NAME + " TEXT NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_SCOR + " INTEGER NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_VALOARE_MINIMA_LIVRARE + " INTEGER NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_MESAJ_CATERING+ " TEXT NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_MESAJ_REZERVARE + " TEXT NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_LIVRARE + " TEXT NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_DATABASE + " TEXT NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_USER + " TEXT NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_PASS + " TEXT NOT NULL, " +
                RestauranteContract.RestauranteEntry.COLUMN_IP + " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_RESTAURANTE_TABLE);
        final String SQL_CREATE_MESAJE_TABLE = "CREATE TABLE " +
                RestauranteContract.MesajeEntry.TABLE_NAME + " (" +
                RestauranteContract.MesajeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RestauranteContract.MesajeEntry.COLUMN_REATAURANT_ID + " INTEGER NOT NULL, " +
                RestauranteContract.MesajeEntry.COLUMN_DATA_MESAJ + " DATETIME NOT NULL, " +
                RestauranteContract.MesajeEntry.COLUMN_MESAJ+ " TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_MESAJE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RestauranteContract.RestauranteEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RestauranteContract.MesajeEntry.TABLE_NAME);
        onCreate(db);
    }
}
