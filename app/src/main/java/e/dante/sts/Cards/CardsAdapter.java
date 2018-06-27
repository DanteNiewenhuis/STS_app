package e.dante.sts.Cards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import e.dante.sts.R;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardViewHolder>{
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Card> cards;
    private ItemClickListener mClickListener;
    private FirebaseUser mUser;

    CardsAdapter(Context context, ArrayList<Card> data) {
//        Log.d("CardAdapter", "Constructor");
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.cards = data;
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_card, parent, false);

//        ViewGroup.LayoutParams p = view.getLayoutParams();
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//
//        p.width = (displayMetrics.widthPixels - 30) / 3;
//
//        view.setLayoutParams(p);

        return new CardViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
//        Log.d("CardAdapter", "onBindViewHolder");
        Card card = cards.get(position);
        Picasso.get().load(card.getImgUrl() + "scale-to-width-down/200").into(holder.mImage);

        if (mUser != null) {
            if (card.getYourScore() == 0.0) {
                holder.mYourScore.setText("Vote Now");
                holder.mStarImage.setImageResource(R.drawable.star_empty);
            }
            else if ((card.getYourScore() % 1) == 0) {
                holder.mYourScore.setText((int) card.getYourScore() + "/5");
                holder.mStarImage.setImageResource(R.drawable.star);
            } else {
                holder.mYourScore.setText(card.getYourScore() + "/5");
                holder.mStarImage.setImageResource(R.drawable.star);
            }

            if ((card.getAverageScore() % 1) == 0) {
                holder.mAverageScore.setText((int) card.getAverageScore() + "/5");
            } else {
                holder.mAverageScore.setText(card.getAverageScore() + "/5");
            }
        }
        else {
            holder.mYourScore.setText("Log In");
            holder.mStarImage.setImageResource(R.drawable.star_empty);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return cards.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mYourScore;
        TextView mAverageScore;
        ImageView mImage;
        ImageView mStarImage;
        LinearLayout mStar;

        CardViewHolder(View itemView) {
            super(itemView);
//            Log.d("CardAdapter", "ViewHolder");
            mImage = itemView.findViewById(R.id.card_grid_item_image);
            mImage.setOnClickListener(this);
            mYourScore = itemView.findViewById(R.id.your_card_item_score);
            mAverageScore = itemView.findViewById(R.id.average_card_item_score);
            mStar = itemView.findViewById(R.id.your_card_item_score_layout);

            mStarImage = itemView.findViewById(R.id.your_card_item_star);
            //TODO implement not being able to vote when not logged in
            if (mUser != null) {
                mStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onRatingClick(view, getAdapterPosition());
                    }
                });
            }
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
        void onRatingClick(View view, int position);
    }

    public void updateList(ArrayList<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }
}
