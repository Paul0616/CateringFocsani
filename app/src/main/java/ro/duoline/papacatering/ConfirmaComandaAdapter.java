package ro.duoline.papacatering;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Paul on 28.05.2017.
 */

public class ConfirmaComandaAdapter extends RecyclerView.Adapter<ConfirmaComandaAdapter.ViewHolder> {
    private ArrayList<ConfirmareValues> valoriConfirmare;
    private Context context;
    private TextView totalgeneral;

    public ConfirmaComandaAdapter(Context context, TextView total, ArrayList<ConfirmareValues> valori){
        this.valoriConfirmare = valori;
        this.context = context;
        this.totalgeneral = total;
    }


    @Override
    public void onBindViewHolder(ConfirmaComandaAdapter.ViewHolder viewHolder, int pos) {

        viewHolder.denumire_produs.setText(valoriConfirmare.get(pos).getDenumire());
        viewHolder.cerinte_speciale.setText(valoriConfirmare.get(pos).getCerinte());

        viewHolder.tot.setText(valoriConfirmare.get(pos).getTotal());
        viewHolder.comandaCurenta.setText("x " + valoriConfirmare.get(pos).getBuc_comandate().toString());
        totalgeneral.setText("TOTAL: " + calculTotalGeneral());

    }

    @Override
    public ConfirmaComandaAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.element_adapter_confirmacomanda, viewGroup, false);
        return new ConfirmaComandaAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (null != valoriConfirmare ? valoriConfirmare.size() : 0);
    }

    public float round2(float number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++)
            pow *= 10;
        float tmp = number * pow;
        return (float) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }

    private String calculTotalGeneral(){
        float tgeneral = 0;
        for(int i=0; i<valoriConfirmare.size(); i++){
            String t = valoriConfirmare.get(i).getTotal();
            float tgen = Float.valueOf(t.substring(0, t.length()-4));
            tgeneral += tgen;
        }
        tgeneral = round2(tgeneral, 2);
        return Float.toString(tgeneral) + " RON";
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView denumire_produs, cerinte_speciale;
        private TextView tot;
        private TextView comandaCurenta;
        private Button bDelete;

        public ViewHolder(View view) {
            super(view);

            denumire_produs = (TextView) view.findViewById(R.id.denumire);
            cerinte_speciale = (TextView) view.findViewById(R.id.textCerinte);
            comandaCurenta = (TextView) view.findViewById(R.id.bucati);
            bDelete = (Button) view.findViewById(R.id.buttonDelete);
            tot = (TextView) view.findViewById(R.id.pretProduse);
            bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valoriConfirmare.remove(getAdapterPosition());
                    if(valoriConfirmare.size() == 0){
                        totalgeneral.setText("TOTAL: ");
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }
}
