package ro.duoline.papacatering.data;

import android.provider.BaseColumns;

/**
 * Created by Paul on 03.06.2017.
 */

public class RestauranteContract {

    //subclasa RestauranteEntry este pentru TABELA restaurante
    public static final class RestauranteEntry implements BaseColumns {
        public static final String TABLE_NAME = "restaurante";
        public static final String COLUMN_REATAURANT_ID = "restaurantID";
        public static final String COLUMN_REATAURANT_NAME = "restaurantName";
        public static final String COLUMN_SCOR = "restaurantScor";
        public static final String COLUMN_VALOARE_MINIMA_LIVRARE = "restaurantValMin";
        public static final String COLUMN_MESAJ_CATERING = "restaurantMsgCat";
        public static final String COLUMN_MESAJ_REZERVARE = "restaurantMsgRez";
        public static final String COLUMN_LIVRARE = "livrare";
        public static final String COLUMN_DATABASE = "dbname_ip";
        public static final String COLUMN_USER = "dbname";
        public static final String COLUMN_PASS = "passw";
        public static final String COLUMN_IP = "ip";
        public static final String COLUMN_START_CATERING = "start_catering";
        public static final String COLUMN_END_CATERING = "end_catering";
        public static final String COLUMN_START_REZERVARI = "start_rezervari";
        public static final String COLUMN_END_REZERVARI= "end_rezervari";

    }

    public static final class MesajeEntry implements BaseColumns {
        public static final String TABLE_NAME = "mesaje";
        public static final String COLUMN_REATAURANT_ID = "restaurantID";
        public static final String COLUMN_DATA_MESAJ = "data_mesaj";
        public static final String COLUMN_MESAJ = "mesaj";
    }
}
