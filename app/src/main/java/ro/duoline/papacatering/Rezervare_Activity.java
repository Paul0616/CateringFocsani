package ro.duoline.papacatering;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Paul on 27.05.2017.
 */

public class Rezervare_Activity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<String>{
    private ImageView backButR, pers2, pers3, pers4, pers5, pers6, pers7, pers8, pers8plus;
    private RezervareValues setRezervare;
    private Button alege, confirma;
    private TextView sumar;
    private RecyclerView recyclerView;
    private rezervareAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    final Context context = this;
    public String denumireRestaurant;
    public int id;
    public ArrayList<String> al;
    private ProgressBar mLoadingIndicator;
    private static final int RESTAURANT_LOADER_ID = 45;
    private final static String RESTAURANTE_URL_BASE = "http://www.duoline.ro/catering";
    private final static String RESTAURANTE_FILE_PHP_QUERY = "getRestauranteRezervare.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervare);
        getSupportActionBar().hide();
        denumireRestaurant = getIntent().getStringExtra("DENUMIRE");
        id = getIntent().getIntExtra("ID", 0);
        al = getIntent().getExtras().getStringArrayList("dateConectare");
        sumar=(TextView) findViewById(R.id.sumar);
        setRezervare = new RezervareValues();
        mLoadingIndicator = (ProgressBar) findViewById(R.id.progressBarRezervare);
        backButR=(ImageView)findViewById(R.id.backButtonR);
        pers2 = (ImageView)findViewById(R.id.imageView2p);
        pers3 = (ImageView)findViewById(R.id.imageView3p);
        pers4 = (ImageView)findViewById(R.id.imageView4p);
        pers5 = (ImageView)findViewById(R.id.imageView5p);
        pers6 = (ImageView)findViewById(R.id.imageView6p);
        pers7 = (ImageView)findViewById(R.id.imageView7p);
        pers8 = (ImageView)findViewById(R.id.imageView8p);
        pers8plus = (ImageView)findViewById(R.id.imageView8plus);
        alege = (Button) findViewById(R.id.alege);
        confirma = (Button) findViewById(R.id.confirma);
        confirma.setOnClickListener(this);
        alege.setOnClickListener(this);
        pers2.setOnClickListener(this);
        pers3.setOnClickListener(this);
        pers4.setOnClickListener(this);
        pers5.setOnClickListener(this);
        pers6.setOnClickListener(this);
        pers7.setOnClickListener(this);
        pers8.setOnClickListener(this);
        pers8plus.setOnClickListener(this);
        backButR.setOnClickListener(this);
        recyclerView = (RecyclerView)findViewById(R.id.rez_recycler_view);
        makeURLConnection();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageView2p:
                setRezervare.setNrPersoane(2);
                sumar.setText(setRezervare.getFinalString());
                break;
            case R.id.imageView3p:
                setRezervare.setNrPersoane(3);
                sumar.setText(setRezervare.getFinalString());
                break;
            case R.id.imageView4p:
                setRezervare.setNrPersoane(4);
                sumar.setText(setRezervare.getFinalString());
                break;
            case R.id.imageView5p:
                setRezervare.setNrPersoane(5);
                sumar.setText(setRezervare.getFinalString());
                break;
            case R.id.imageView6p:
                setRezervare.setNrPersoane(6);
                sumar.setText(setRezervare.getFinalString());
                break;
            case R.id.imageView7p:
                setRezervare.setNrPersoane(7);
                sumar.setText(setRezervare.getFinalString());
                break;
            case R.id.imageView8p:
                setRezervare.setNrPersoane(8);
                sumar.setText(setRezervare.getFinalString());
                break;
            case R.id.imageView8plus:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+setRezervare.getTelefon()));
                startActivity(intent);
                break;
            case R.id.backButtonR:
                finish();
                break;
            case R.id.confirma:
                if(setRezervare.getLocatie() == ""){
                    Toast.makeText(context,"ALEGE O LOCATIE", Toast.LENGTH_SHORT).show();
                } else if(setRezervare.getDate() == null){
                    Toast.makeText(context,"ALEGE ZIUA SI ORA", Toast.LENGTH_SHORT).show();
                } else if(setRezervare.getNrPersoane() == 0){
                    Toast.makeText(context,"ALEGE NR. PERSOANE", Toast.LENGTH_SHORT).show();
                }
                if(setRezervare.dateComplete()){
                    Intent i=new Intent(Rezervare_Activity.this,ConfirmaRezervarea_Activity.class);
                    i.putExtra("locatie", setRezervare.getLocatie());
                    i.putExtra("dataRezervarii",setRezervare.getDate().getTime());
                    i.putExtra("idRestaurant", id);
                    i.putExtra("nrPersoane", setRezervare.getNrPersoane());
                    i.putStringArrayListExtra("dateConectare", al);
                    startActivity(i);

                }
                break;
            case R.id.alege:
                if(setRezervare.getDate() != null){
                    new SingleDateAndTimePickerDialog.Builder(context)
                            .mustBeOnFuture()
                            .defaultDate(setRezervare.getDate())
                            .minutesStep(30)
                            .mainColor(Color.parseColor("#5a0b09"))
                            .listener(new SingleDateAndTimePickerDialog.Listener() {
                                @Override
                                public void onDateSelected(Date date) {
                                    setRezervare.setDate(date);
                                    sumar.setText(setRezervare.getFinalString());

                                }
                            }).display();
                } else {
                    new SingleDateAndTimePickerDialog.Builder(context)
                            .mustBeOnFuture()
                            .minutesStep(30)
                            .mainColor(Color.parseColor("#5a0b09"))
                            .listener(new SingleDateAndTimePickerDialog.Listener() {
                                @Override
                                public void onDateSelected(Date date) {
                                    setRezervare.setDate(date);
                                    sumar.setText(setRezervare.getFinalString());

                                }
                            }).display();
                }
                break;
            default:
                break;
        }
    }

    private void makeURLConnection(){
        Uri bultUri = Uri.parse(RESTAURANTE_URL_BASE);
        bultUri = Uri.withAppendedPath(bultUri, RESTAURANTE_FILE_PHP_QUERY);
        bultUri = bultUri.buildUpon().appendQueryParameter("idRestaurant",Integer.toString(id)).build();
        URL queryURL = null;
        try {
            queryURL = new URL(bultUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Bundle queryBundle = new Bundle();
        queryBundle.putString("link",queryURL.toString());
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> queryLoader = loaderManager.getLoader(RESTAURANT_LOADER_ID);
        if(queryLoader == null){
            loaderManager.initLoader(RESTAURANT_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(RESTAURANT_LOADER_ID, queryBundle, this);
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String queryURLString = args.getString("link");
                if(queryURLString == null || queryURLString == "") return null;
                try{
                    URL queryURL = new URL(queryURLString);
                    String result = getResponseFromHttpUrl(queryURL);
                    return result;
                } catch (IOException e){
                    e.printStackTrace();
                    return null;
                }

            }

        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        try {

            JSONObject jObj = new JSONObject(data);
            confirma.setText("Confirma rezervarea " + jObj.get("denumire"));
            int nrLocatii = 0;
            if(!jObj.get("Locatia1").equals("")) nrLocatii++;
            if(!jObj.get("Locatia2").equals("")) nrLocatii++;
            if(!jObj.get("Locatia3").equals("")) nrLocatii++;


            if(nrLocatii > 1 && nrLocatii < 4) { //2 sau 3 locatii
                layoutManager = new GridLayoutManager(getApplicationContext(), nrLocatii);
            } else {
                layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false); //pentru 0 sau 1 locatii
            }
            setRezervare.setsrcLocatie1(jObj.get("Locatia1").toString());
            setRezervare.setsrcLocatie2(jObj.get("Locatia2").toString());
            setRezervare.setsrcLocatie3(jObj.get("Locatia3").toString());

            setRezervare.setTelefon(jObj.get("telefon").toString());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new rezervareAdapter(getApplicationContext(), sumar, setRezervare, nrLocatii);//, List<String> texte, List<String> srcs
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
