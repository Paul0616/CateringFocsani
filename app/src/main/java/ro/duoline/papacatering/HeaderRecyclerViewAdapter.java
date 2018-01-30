package ro.duoline.papacatering;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;

/**
 * Created by Paul on 27.05.2017.
 */

public class HeaderRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<HeaderRecyclerViewItems> itemObjects;
    private Context context;


    public HeaderRecyclerViewAdapter(Context context, List<HeaderRecyclerViewItems> itemObjects){
        this.itemObjects = itemObjects;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout, parent, false);
            return new HeaderViewHolder(layoutView);
        } else if (viewType == TYPE_ITEM){
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(layoutView);
        }
        throw new RuntimeException("No match for " + viewType + ".");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HeaderRecyclerViewItems mObject = itemObjects.get(position);
        if(holder instanceof HeaderViewHolder){
            ((HeaderViewHolder) holder).headerTitle.setText(mObject.getHeader());
        } else if (holder instanceof ItemViewHolder){
            ((ItemViewHolder) holder).denumireRestaurant.setText(mObject.getDenumireRestaurant());
            Picasso.with(context).load(mObject.getPoza()).into(((ItemViewHolder) holder).pozaRestaurant);//.error(R.drawable.eating)
            if(mObject.areCatering()){
                ((ItemViewHolder) holder).butCatering.setVisibility(View.VISIBLE);
            } else  {
                ((ItemViewHolder) holder).butCatering.setVisibility(View.INVISIBLE);
            }
            if(mObject.areRezervari()){
                ((ItemViewHolder) holder).butRezervari.setVisibility(View.VISIBLE);
            } else  {
                ((ItemViewHolder) holder).butRezervari.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemObjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)) return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView headerTitle;

        public HeaderViewHolder(View itemView){
            super(itemView);
            headerTitle = (TextView)itemView.findViewById(R.id.header_id);
            Typeface face= Typeface.createFromAsset(itemView.getContext().getAssets(), "font/PoiretOne-Regular.ttf");
            headerTitle.setTypeface(face);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView denumireRestaurant;
        public TextView butCatering;
        public TextView butRezervari;
        public ImageView pozaRestaurant;


        public ItemViewHolder(View itemView){
            super(itemView);
            denumireRestaurant = (TextView) itemView.findViewById(R.id.denumire_restaurant);
            butCatering = (TextView) itemView.findViewById(R.id.catering_button);
            butRezervari = (TextView) itemView.findViewById(R.id.rezervari_button);
            pozaRestaurant = (ImageView) itemView.findViewById(R.id.poza_restaurant);
            butCatering.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int restaurantID = itemObjects.get(getAdapterPosition()).getRestaurantId();
                    String denumireRestaurant = itemObjects.get(getAdapterPosition()).getDenumireRestaurant();
                    ArrayList<String> al = itemObjects.get(getAdapterPosition()).getDateConectare();
                    String livrare = itemObjects.get(getAdapterPosition()).getLocatiiLivrare();
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("DENUMIRE", denumireRestaurant);
                    i.putExtra("ID", restaurantID);
                    i.putExtra("LIVRARE", livrare);
                    i.putStringArrayListExtra("dateConectare", al);/* */
                    v.getContext().startActivity(i);
                }
            });
            butRezervari.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int restaurantID = itemObjects.get(getAdapterPosition()).getRestaurantId();
                    String denumireRestaurant = itemObjects.get(getAdapterPosition()).getDenumireRestaurant();
                    ArrayList<String> al = itemObjects.get(getAdapterPosition()).getDateConectare();
                    Intent i = new Intent(context, Rezervare_Activity.class);
                    i.putExtra("DENUMIRE", denumireRestaurant);
                    i.putExtra("ID", restaurantID);
                    i.putStringArrayListExtra("dateConectare", al);
                    v.getContext().startActivity(i);
                }
            });
        }
    }

}
