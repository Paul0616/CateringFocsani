package ro.duoline.papacatering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeluriMeniu_Activity extends AppCompatActivity implements  GetFelMeniuStrinList.Listener{
    String categorieFiltru;
    private RecyclerView recyclerView, recyclerView1;
    private FelAdapter adapter;
    private IconCategoriiadapter adapter1;
    private RecyclerView.LayoutManager layoutManager, layoutManager1;
    public TextView categorieCurenta;
    public TextView total;
    public ProgressBar prgBar;
    public ImageView backButton;
    public TextView confirmaComanda, numarProduse, anulare;
    public int[] bucComandateSalvate;
    public String denumireRestaurant;
    public int idRestaurant;
    public ArrayList<String> listaDenumiri, al;
    public Boolean viewCod = false;
    private ArrayList<Integer> coduriCosProduse = new ArrayList<Integer>();
    private ArrayList<Integer> bucatiCosProduse = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_felmeniu);
        getSupportActionBar().hide();
        categorieFiltru = getIntent().getStringExtra("EXTRA_TEXT");
        denumireRestaurant = getIntent().getStringExtra("DENUMIRE");
        idRestaurant = getIntent().getIntExtra("ID", 0);
        listaDenumiri = getIntent().getExtras().getStringArrayList("listaDdenumiri");
        al = getIntent().getExtras().getStringArrayList("dateConectare");
        if (getIntent().hasExtra("coduriCosProduse")) {
            coduriCosProduse = getIntent().getIntegerArrayListExtra("coduriCosProduse");
            bucatiCosProduse = getIntent().getIntegerArrayListExtra("bucatiCosProduse");
        }

        String dateconectare = "" + al.get(0) + "," + al.get(1) + "," + al.get(2);
        initViews();
        bucComandateSalvate = null;
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey("BUC_COMANDATE_CALLBACK")){
                bucComandateSalvate = savedInstanceState.getIntArray("BUC_COMANDATE_CALLBACK");
            }
        }
        prgBar.setVisibility(View.VISIBLE);
        new GetFelMeniuStrinList(this).execute(dateconectare, al.get(3));


        final GestureDetector myGestDetector = new GestureDetector(this, new GestureListener());
        numarProduse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                myGestDetector.onTouchEvent(event);
                return false;
            }

        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                    Intent intent = new Intent();
                    coduriCosProduse.clear();
                    bucatiCosProduse.clear();
                    for(int i=0; i<adapter.felMeniu.size(); i++){
                        if (adapter.felMeniu.get(i).getBucComandate() > 0) {
                            coduriCosProduse.add(adapter.felMeniu.get(i).getCod());
                            bucatiCosProduse.add(adapter.felMeniu.get(i).getBucComandate());
                        }
                    }

                intent.putIntegerArrayListExtra("coduriCosProduse", coduriCosProduse);
                intent.putIntegerArrayListExtra("bucatiCosProduse", bucatiCosProduse);
                setResult(RESULT_OK, intent);


                finish();
            }
        });
        //numarProduse.
        anulare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<adapter.felMeniu.size(); i++){
                    adapter.felMeniu.get(i).setBucComandate(0);
                }
                adapter.notifyDataSetChanged();
            }
        });
        confirmaComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                ArrayList<FelMeniuValues> vct = adapter.getVector();
                ArrayList<Integer> cod = new ArrayList<Integer>();
                ArrayList<String> denumiri = new ArrayList<String>();
                ArrayList<String> cerinte = new ArrayList<String>();
                ArrayList<Integer> cantitate = new ArrayList<Integer>();
                ArrayList<Float> pret = new ArrayList<Float>();



                int j = 0;
                for(int i = 0; i<vct.size(); i++){
                    if(vct.get(i).getBucComandate() > 0){
                        cod.add(vct.get(i).getCod());
                        denumiri.add(vct.get(i).getDenumire());
                        cantitate.add(vct.get(i).getBucComandate());
                        cerinte.add(vct.get(i).getCerinte());
                        pret.add(vct.get(i).getPret());
                        j++;
                    }
                }

                Intent i=new Intent(FeluriMeniu_Activity.this,ConfirmaComanda_Activity.class);
                i.putStringArrayListExtra("dateconectare", al);
                i.putStringArrayListExtra("denumiri", denumiri);
                i.putStringArrayListExtra("cerinte", cerinte);
                float[] pretF = new float[pret.size()];
                int k = 0;

                for (Float f : pret) {
                    pretF[k++] = (f != null ? f : Float.NaN); // Or whatever default you want.
                }
                int[] cantitateF = new int[cantitate.size()];
                k = 0;

                for (Integer f : cantitate) {
                    cantitateF[k++] = f; // Or whatever default you want.
                }

                int[] codF = new int[cod.size()];
                k = 0;

                for (Integer f : cod) {
                    codF[k++] = f; // Or whatever default you want.
                }


                i.putExtra("cantitate", cantitateF);
                i.putExtra("cod",codF);
                i.putExtra("pret", pretF);
                i.putExtra("ID", idRestaurant);
                startActivityForResult(i, 1);
                //  adapter.filter("");
            }
        });
    }

    private void initViews(){
        prgBar = (ProgressBar) findViewById(R.id.prgBar);
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.addItemDecoration(new LineItemDecoration(this));
        recyclerView1 = (RecyclerView)findViewById(R.id.categorii_recycler_view);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);
        categorieCurenta = (TextView) findViewById(R.id.titlu_categorie);
        anulare = (TextView) findViewById(R.id.anulareBtn);
        total = (TextView) findViewById(R.id.total);
        backButton = (ImageView) findViewById((R.id.backButton));
        confirmaComanda = (TextView) findViewById((R.id.confirma_comanda));
        numarProduse = (TextView) findViewById(R.id.textNumarProduse);

    }

    public class GestureListener implements GestureDetector.OnGestureListener {

        public GestureListener() {
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            viewCod = !viewCod;
            toggleViewCod(viewCod);
        }
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {

            return false;
        }
    }

    public void setAdapterFilter(String text){
        adapter.filter(text);
    }

    public void toggleViewCod(Boolean viewCod){adapter.toggleCod(viewCod);}

    @Override
    public void onError() {
        Toast.makeText(this, "Erroare conectare server!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String strJSON = data.getStringExtra("ValoriConfirmate");
                try{
                    JSONObject obj = new JSONObject(strJSON);

                    JSONArray coduri = null;
                    JSONArray bucati = null;

                    if(obj.optJSONArray("cod") == null){
                        if(obj.opt("cod") != null){
                            coduri = new JSONArray();
                            bucati = new JSONArray();
                            //cerinte = new JSONArray();
                            coduri.put(obj.getInt("cod"));
                            bucati.put(obj.getInt("bucati"));
                            //   cerinte.put(obj.getString("cerinte"));
                        }
                    } else {
                        coduri = obj.getJSONArray("cod");
                        bucati = obj.getJSONArray("bucati");

                    }


                    ArrayList<FelMeniuValues> vector = adapter.getVector();
                    if(coduri != null) {
                        for (int i = 0; i < vector.size(); i++) {
                            int codV = vector.get(i).getCod();
                            int j = 0;
                            while (j < coduri.length()) {
                                if (Integer.parseInt(coduri.get(j).toString()) == codV) { //codul produsului din lista completa se afla printre valorile confirmate
                                    vector.get(i).setBucComandate(Integer.parseInt(bucati.get(j).toString()));

                                    break;
                                } else {
                                    vector.get(i).setBucComandate(0);
                                    vector.get(i).setCerinte("");

                                }
                                j++;
                            }
                        }
                    } else {
                        for (int i = 0; i < vector.size(); i++) {
                            vector.get(i).setBucComandate(0);
                            vector.get(i).setCerinte("");
                        }
                    }
                    adapter.setVector(vector);
                } catch(JSONException e){
                    e.printStackTrace();
                }
                //
                // obj.getString('produs');
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        ArrayList<FelMeniuValues> vector = adapter.getVector();
        int[] bucComandateCallback = new int[vector.size()];
        FelMeniuValues elementVector;
        for(int i = 0; i<vector.size(); i++){
            elementVector = vector.get(i);
            bucComandateCallback[i] = elementVector.getBucComandate();
        }
        outState.putIntArray("BUC_COMANDATE_CALLBACK", bucComandateCallback);


    }

    public void createAdapter(List<String> NUME_CATEGORIE, List<String> DENUMIRE_PRODUS, List<String> DESCRIERE_PRODUS, List<Float> PRET_BUCATA,
                              List<Integer> COD, List<String> UM, List<String> POZA ){
        prgBar.setVisibility(View.INVISIBLE);
        ArrayList<FelMeniuValues> felMeniu = new ArrayList<>();

        for(int i=0; i<NUME_CATEGORIE.size(); i++){
            FelMeniuValues felMen = new FelMeniuValues();
            felMen.setCategorie(NUME_CATEGORIE.get(i));
            felMen.setDenumire(DENUMIRE_PRODUS.get(i));
            felMen.setDescriere(DESCRIERE_PRODUS.get(i));
            felMen.setUM(UM.get(i));
            felMen.setPret((Float) PRET_BUCATA.get(i));
            felMen.setCod((Integer) COD.get(i));
            felMen.setPozaURL(POZA.get(i));
            felMen.setCerinte("");
            if(bucComandateSalvate != null) {
                felMen.setBucComandate(bucComandateSalvate[i]);
            } else {
                felMen.setBucComandate(0);
            }
            felMeniu.add(felMen);
        }
        adapter1 = new IconCategoriiadapter(this, listaDenumiri);
        recyclerView1.setAdapter(adapter1);
        if(coduriCosProduse.size() > 0){
            for(int i=0; i<felMeniu.size(); i++){
                for(int j=0; j<coduriCosProduse.size(); j++) {
                    if (felMeniu.get(i).getCod().equals(coduriCosProduse.get(j))){
                        felMeniu.get(i).setBucComandate(bucatiCosProduse.get(j));
                    }
                }
            }
        }
        adapter = new FelAdapter(getApplicationContext(), total, categorieCurenta, numarProduse, felMeniu, this);
        recyclerView.setAdapter(adapter);
        adapter.filter(categorieFiltru);
    }

}
