package ro.duoline.papacatering;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategoriiMeniu_Activity extends AppCompatActivity implements  GetMeniuStringLists.Listener{
    private RecyclerView recyclerView;
    private categoriiAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView backBut;
    public String denumireRestaurant;
    public int idRestaurant;
    private TextView textHeaderMeniu;
    private ArrayList<Integer> coduriCosProduse = new ArrayList<Integer>();
    private ArrayList<Integer> bucatiCosProduse = new ArrayList<Integer>();
    List<String> catMeniu;
    ArrayList<String> al;
    public List<String> listNumeCategorii = new ArrayList<String>();
    public List<String> listToatePozeleCategoriilor = new ArrayList<String>();
    public List<String> listToateNumeleCategoriilor = new ArrayList<String>();
    private final static String NO_PHOTO = "http://www.ondesign.ro/catering/imagesCategorii/no_photo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoriimeniu);
        getSupportActionBar().hide();
        denumireRestaurant = getIntent().getStringExtra("DENUMIRE");
        idRestaurant = getIntent().getIntExtra("ID", 0);
        al = getIntent().getExtras().getStringArrayList("dateConectare");
        String dateconectare = "" + al.get(0) + "," + al.get(1) + "," + al.get(2);
        initViews();
        textHeaderMeniu.setText("Meniu " + denumireRestaurant.replace("_", " "));
        AsyncTask<String, Void, List<String>> execute = new GetMeniuStringLists(this, 1).execute(dateconectare, al.get(3));//denumireRestaurant.toLowerCase()

        // getActionBar().setDisplayHomeAsUpEnabled(true);
        backBut=(ImageView)findViewById(R.id.backButton);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Intent intent = getIntent();
                //startActivity(intent);
                finish();
            }
        });
    }
    public void callFelMeniuActivity(String categorie){
        ArrayList<String> listaDenumiri = new ArrayList<String>();
        for(int i = 0; i< catMeniu.size(); i++) {

            listaDenumiri.add(catMeniu.get(i));

        }
        Intent i = new Intent(CategoriiMeniu_Activity.this, FeluriMeniu_Activity.class);
        i.putExtra("EXTRA_TEXT", categorie);
        i.putExtra("DENUMIRE", denumireRestaurant);
        i.putExtra("ID", idRestaurant);
        i.putStringArrayListExtra("listaDdenumiri", listaDenumiri);
        i.putStringArrayListExtra("dateConectare", al);
        if (coduriCosProduse.size() > 0) {
            i.putIntegerArrayListExtra("coduriCosProduse", coduriCosProduse);
            i.putIntegerArrayListExtra("bucatiCosProduse", bucatiCosProduse);
        }
       // startActivity(i);
        startActivityForResult(i, 69);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 69){
            if(resultCode == RESULT_OK) {
                coduriCosProduse = data.getIntegerArrayListExtra("coduriCosProduse");
                bucatiCosProduse = data.getIntegerArrayListExtra("bucatiCosProduse");
            }
        }
    }

    private void initViews(){
        SpacesItemDecoration decoration = new SpacesItemDecoration(20);
        recyclerView = (RecyclerView)findViewById(R.id.rez_recycler_view);
        textHeaderMeniu = (TextView) findViewById(R.id.textViewMeniuRestaurant);
        layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
    }




    public void updateLists_Resume(int denumiriSauPoze, List<String> A, List<String> B, List<String> C){
        if(denumiriSauPoze == 1) {
            listNumeCategorii = A;
            AsyncTask<String, Void, List<String>> execute1 = new GetMeniuStringLists(this, 2).execute("","http://www.duoline.ro/catering");
        }
        if(denumiriSauPoze == 2) {
            listToatePozeleCategoriilor = B;
            listToateNumeleCategoriilor = C;
        }
        if(listNumeCategorii.size() != 0 && listToatePozeleCategoriilor.size() != 0 && listToateNumeleCategoriilor.size() != 0){

            ArrayList<CategoriiMeniuValues> categoriiMeniu = new ArrayList<>();
            catMeniu = listNumeCategorii;
            for(int i = 0; i<listNumeCategorii.size(); i++){
                CategoriiMeniuValues catMen = new CategoriiMeniuValues();
                catMen.setCategorii_meniu_nume(listNumeCategorii.get(i));
                catMen.setCategorii_meniu_image_url(NO_PHOTO);
                for(int j = 0; j<listToatePozeleCategoriilor.size(); j++){
                    if(listToateNumeleCategoriilor.get(j).equals(listNumeCategorii.get(i))) {
                        catMen.setCategorii_meniu_image_url(listToatePozeleCategoriilor.get(j));

                        break;
                    }
                }
                categoriiMeniu.add(catMen);
            }

            adapter = new categoriiAdapter(getApplicationContext(), this, categoriiMeniu);
            recyclerView.setAdapter(adapter);
        }




    }

    public void onError() {
        Toast.makeText(this, "Erroare conectare server!\nMeniul de catering pentru " + denumireRestaurant.replace("_", " ") + " este momentan indisponibil.", Toast.LENGTH_LONG).show();
    }
}
