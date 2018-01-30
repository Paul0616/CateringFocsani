package ro.duoline.papacatering;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 28.05.2017.
 */

public class istoricAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IstoricComenziValues> istoric;
    private List<IstoricComenziValues> istoricFiltrat;
    private Context context;
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;

    // Provide a suitable constructor (depends on the kind of dataset)
    public istoricAdapter(Context context, List<IstoricComenziValues> istoric ) {
        this.istoric = istoric;
        this.istoricFiltrat = new ArrayList<>();
        for(int i=0; i<istoric.size(); i++){
            if (istoric.get(i).isVisible()) istoricFiltrat.add(istoric.get(i));
        }
        this.context = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_PARENT){
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.istori_element_adapter, parent, false);
            return new ParentViewHolder(v);
        }
        if(viewType == TYPE_CHILD){
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.istori_element_adapter_child, parent, false);
            return new ChildViewHolder(v);
        }
        throw new RuntimeException("No match for " + viewType + ".");

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ParentViewHolder) {
            if(istoricFiltrat.get(position).getIstoric_total() != null) {
                ((ParentViewHolder) holder).mTotal.setText(istoricFiltrat.get(position).getIstoric_total());
            } else {
                ((ParentViewHolder) holder).mTotal.setText("");
            }
            if(istoricFiltrat.get(position).getIstoric_date() != null) {
                ((ParentViewHolder) holder).mData.setText("Comanda din data " + istoricFiltrat.get(position).getIstoric_date());
            } else {
                ((ParentViewHolder) holder).mData.setText("Comanda din data ");
            }
            if(istoricFiltrat.get(position).getIstoric_nrproduse() != null) {
                ((ParentViewHolder) holder).mNrProduse.setText(istoricFiltrat.get(position).getIstoric_nrproduse() + " produse");
            } else {
                ((ParentViewHolder) holder).mNrProduse.setText("");
            }
        } else if (holder instanceof  ChildViewHolder){
            if(istoricFiltrat.get(position).getDenumireProdus() != null) {
                ((ChildViewHolder) holder).mDenumireProdus.setText(istoricFiltrat.get(position).getDenumireProdus());
            } else {
                ((ChildViewHolder) holder).mDenumireProdus.setText("");
            }
            if(istoricFiltrat.get(position).getBucati() != null) {
                ((ChildViewHolder) holder).mBucati.setText(istoricFiltrat.get(position).getBucati());
            } else {
                ((ChildViewHolder) holder).mBucati.setText("");
            }
            if(istoricFiltrat.get(position).getPret() != null) {
                ((ChildViewHolder) holder).mPret.setText(istoricFiltrat.get(position).getPret()+" RON");
            } else {
                ((ChildViewHolder) holder).mPret.setText("");
            }
            if(istoricFiltrat.get(position).getCerinte() != null) {
                ((ChildViewHolder) holder).mCerinte.setText(istoricFiltrat.get(position).getCerinte());
            } else {
                ((ChildViewHolder) holder).mCerinte.setText("");
            }
        }

    }

    private IstoricComenziValues getItem(int position){
        return istoric.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return istoricFiltrat.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(istoricFiltrat.get(position).isChild()) return TYPE_CHILD;
        return TYPE_PARENT;
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder{
        public TextView mData, mTotal, mNrProduse;

        public ParentViewHolder(View itemView){
            super(itemView);
            mData = (TextView) itemView.findViewById(R.id.data);
            mNrProduse = (TextView) itemView.findViewById(R.id.nrproduse);
            mTotal = (TextView) itemView.findViewById(R.id.totalproduse);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int id = istoricFiltrat.get(getAdapterPosition()).getID();
                    istoricFiltrat.clear();
                    allChildrensInvisible();
                    for(int i=0; i<istoric.size(); i++){
                        if(istoric.get(i).getID() == id) istoric.get(i).setVisible(true);
                        if(istoric.get(i).isVisible()) istoricFiltrat.add(istoric.get(i));
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void allChildrensInvisible(){
        for(int i=0; i<istoric.size(); i++){
            if(istoric.get(i).isChild()) istoric.get(i).setVisible(false);
        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder{
        public TextView mDenumireProdus, mBucati, mPret, mCerinte;

        public ChildViewHolder(View itemView){
            super(itemView);
            mDenumireProdus = (TextView) itemView.findViewById(R.id.denumireProdusIstoric);
            mBucati = (TextView) itemView.findViewById(R.id.bucatiIstoric);
            mPret = (TextView) itemView.findViewById(R.id.pretProduseIstoric);
            mCerinte = (TextView) itemView.findViewById(R.id.textCerinteIstoric);
        }
    }
}
