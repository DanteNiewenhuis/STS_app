package e.dante.sts.Cards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import e.dante.sts.R;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardViewHolder>{
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Card> cards;
    private ItemClickListener mClickListener;

    CardsAdapter(Context context, ArrayList<Card> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.cards = data;
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = cards.get(position);
        Picasso.get().load(card.getImgUrl() + "scale-to-width-down/200").into(holder.mImage);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return cards.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;
        ImageView mImage;

        CardViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.card_grid_item_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // convenience method for getting data at click position
    Card getItem(int id) {
        return cards.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void updateList(ArrayList<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }
}
