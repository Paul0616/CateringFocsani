package ro.duoline.papacatering;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Paul on 28.05.2017.
 */

public class FelAdapter extends RecyclerView.Adapter<FelAdapter.ViewHolder> {
    public ArrayList<FelMeniuValues> felMeniu;
    private ArrayList<FelMeniuValues> felMeniuFitrata;
    private Context context;
    private TextView total;
    private TextView categorieCurenta, numarProduse;
    private AppCompatActivity mInstance;
    private Boolean viewCod = false;

    public FelAdapter(Context context, TextView total, TextView categorie, TextView numarProduse, ArrayList<FelMeniuValues> felMeniu, AppCompatActivity parentActivity){
        this.felMeniu = new ArrayList<FelMeniuValues>();
        this.felMeniuFitrata = new ArrayList<FelMeniuValues>();
        this.felMeniu.addAll(felMeniu);
        this.context = context;
        this.felMeniuFitrata.addAll(this.felMeniu); //copiez lista originala
        this.total = total;
        this.categorieCurenta = categorie;
        this.numarProduse = numarProduse;
        this.mInstance = parentActivity;
    }

    @Override
    public FelAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_card_produse, viewGroup, false);
        return new FelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FelAdapter.ViewHolder viewHolder, int pos) {

        viewHolder.denumire_produs.setText(felMeniuFitrata.get(pos).getDenumire());
        viewHolder.descriere_produs.setText(felMeniuFitrata.get(pos).getDescriere());
        viewHolder.pret.setText("Pret: " + felMeniuFitrata.get(pos).getPret().toString() + " RON");
        String temp = "x " + felMeniuFitrata.get(pos).getBucComandate().toString();
        viewHolder.comandaCurenta.setText(temp);

        if(felMeniuFitrata.get(pos).getCerinte().equals("")) viewHolder.cerinteSpeciale.setBackground(context.getResources().getDrawable(R.drawable.border_top_bottom));
        else viewHolder.cerinteSpeciale.setBackground(context.getResources().getDrawable(R.drawable.border_top_bottom_selectat));
        if(felMeniuFitrata.get(pos).getBucComandate() > 0){
            viewHolder.comandaCurenta.setVisibility(View.VISIBLE);
        } else {
            viewHolder.comandaCurenta.setVisibility(View.INVISIBLE);
        }
        if(felMeniuFitrata.get(pos).getPozaURL().equals("")) {
            viewHolder.img_produs.setImageResource(R.drawable.no_photo);
        }else{
            Picasso.with(context).load(felMeniuFitrata.get(pos).getPozaURL()).error(R.drawable.no_photo).into(viewHolder.img_produs);
        }

        viewHolder.mCod.setText(felMeniuFitrata.get(pos).getCod().toString());
        if(viewCod) {
            viewHolder.mCod.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mCod.setVisibility(View.INVISIBLE);
        }
        total.setText("TOTAL: " + viewHolder.calculTotal() + " RON");
    }

    @Override
    public int getItemCount() {
        return (null != felMeniuFitrata ? felMeniuFitrata.size() : 0);
    }

    public void toggleCod(Boolean viewCod){
        this.viewCod = viewCod;
        notifyDataSetChanged();
    }

    public void filter(final String categorieAfisata){

        felMeniuFitrata.clear(); // golim lista
        if(TextUtils.isEmpty(categorieAfisata)){ //Daca categoria nu e specificata se restaureaza lista originala
            felMeniuFitrata.addAll(felMeniu);
            categorieCurenta.setText("Toate");
        } else {
            //se itereaza lista originala si se adauga la lista filtrata
            for(int i=0; i<felMeniu.size(); i++){
                if(felMeniu.get(i).getCategorie().equals(categorieAfisata)){
                    felMeniuFitrata.add(felMeniu.get(i));
                }
            }
            categorieCurenta.setText(categorieAfisata);
            numarProduse.setText(Integer.toString(felMeniuFitrata.size()) + " produse");
        }


        notifyDataSetChanged();
    }

    public ArrayList<FelMeniuValues> getVector(){return felMeniu;}

    public void setVector(ArrayList<FelMeniuValues> vec){
        felMeniu = vec;
        filter(categorieCurenta.getText().toString());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView denumire_produs;
        private TextView descriere_produs;
        private TextView tot;
        private TextView pret, cerinteSpeciale;
        private TextView comandaCurenta, bPlus, bMinus, mCod;
        private ImageView img_produs;
        public ViewHolder(View view) {
            super(view);

            denumire_produs = (TextView)view.findViewById(R.id.denumire);
            descriere_produs = (TextView) view.findViewById(R.id.descriere_produs);
            pret = (TextView) view.findViewById(R.id.pret);
            cerinteSpeciale = (TextView) view.findViewById(R.id.cerinteSpeciale);
            comandaCurenta = (TextView) view.findViewById(R.id.comanda_curenta);
            img_produs = (ImageView) view.findViewById(R.id.poza);
            mCod = (TextView) view.findViewById(R.id.textCod);
            bPlus = (TextView) view.findViewById(R.id.textPlus);
            bMinus = (TextView) view.findViewById(R.id.textMinus);
            tot = (TextView) view.findViewById(R.id.total);
            bPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int posInOriginal = felMeniu.indexOf(felMeniuFitrata.get(getAdapterPosition()));
                    int comandate = felMeniuFitrata.get(getAdapterPosition()).getBucComandate() + 1;
                    felMeniuFitrata.get(getAdapterPosition()).setBucComandate(comandate);
                    felMeniu.get(posInOriginal).setBucComandate(comandate);
                    if(felMeniuFitrata.get(getAdapterPosition()).getBucComandate() > 0){
                        comandaCurenta.setVisibility(View.VISIBLE);
                    }
                    total.setText("TOTAL: " + calculTotal() + " RON");
                    notifyDataSetChanged();
                }
            });
            bMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int posInOriginal = felMeniu.indexOf(felMeniuFitrata.get(getAdapterPosition()));
                    int comandate = felMeniuFitrata.get(getAdapterPosition()).getBucComandate() - 1;
                    if(felMeniuFitrata.get(getAdapterPosition()).getBucComandate() > 0) {
                        felMeniuFitrata.get(getAdapterPosition()).setBucComandate(comandate);
                        felMeniu.get(posInOriginal).setBucComandate(comandate);
                        if(felMeniuFitrata.get(getAdapterPosition()).getBucComandate() == 0){
                            comandaCurenta.setVisibility(View.INVISIBLE);
                        }
                        total.setText("TOTAL: " + calculTotal() + " RON");
                        notifyDataSetChanged();
                    }
                }
            });
            cerinteSpeciale.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    createAlertDialogBuilder("Poti trimite o cerinta speciala; (ex. 'fara cascaval')");
                }
            });


        }


        public void createAlertDialogBuilder(String message){

            // Set up the input
            final EditText input = new EditText(context);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(felMeniuFitrata.get(getAdapterPosition()).getCerinte());
            new AlertDialog.Builder(mInstance)
                    .setMessage(message)
                    .setCancelable(true)
                    .setTitle("Cerinte Speciale")
                    .setView(input)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int posInOriginal = felMeniu.indexOf(felMeniuFitrata.get(getAdapterPosition()));
                            String txt =  input.getText().toString();
                            felMeniuFitrata.get(getAdapterPosition()).setCerinte(txt);
                            felMeniu.get(posInOriginal).setCerinte(txt);
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            dialog.cancel();
                        }
                    })
                    .create().show();
            //return m_text;
        }

        public float calculTotal() {
            float res = 0;
            for(int i=0; i<felMeniu.size(); i++){
                res += (felMeniu.get(i).getBucComandate() * felMeniu.get(i).getPret());
            }
            return round2(res, 2);
        }
        public float round2(float number, int scale) {
            int pow = 10;
            for (int i = 1; i < scale; i++)
                pow *= 10;
            float tmp = number * pow;
            return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
        }
    }
}
