package e.dante.sts.Relics;

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

public class RelicsAdapter extends RecyclerView.Adapter<RelicsAdapter.RelicViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Relic> relics;
    private e.dante.sts.Relics.RelicsAdapter.ItemClickListener mClickListener;
    private FirebaseUser mUser;

    RelicsAdapter(Context context, ArrayList<Relic> data) {
//        Log.d("RelicAdapter", "Constructor");
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.relics = data;
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public RelicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_relic, parent, false);

        ViewGroup.LayoutParams p = view.getLayoutParams();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        float density = displayMetrics.density;
        p.width = Math.round((displayMetrics.widthPixels - (30 * density)) / 3);

        view.setLayoutParams(p);

        return new RelicViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(RelicViewHolder holder, int position) {
//        Log.d("RelicAdapter", "onBindViewHolder");
        Relic relic = relics.get(position);
        holder.desView.setText(relic.getDescription());
        holder.nameView.setText(relic.getName());

        Picasso.get().load(relic.getImgUrl() + "scale-to-width-down/200").into(holder.mImage);

        if (mUser != null) {
            if (relic.getYourScore() == 0.0) {
                holder.mYourScore.setText("Vote Now");
                holder.mStarImage.setImageResource(R.drawable.star_empty);
            } else if ((relic.getYourScore() % 1) == 0) {
                holder.mYourScore.setText((int) relic.getYourScore() + "/5");
                holder.mStarImage.setImageResource(R.drawable.star);
            } else {
                holder.mYourScore.setText(relic.getYourScore() + "/5");
                holder.mStarImage.setImageResource(R.drawable.star);
            }

            if ((relic.getAverageScore() % 1) == 0) {
                holder.mAverageScore.setText((int) relic.getAverageScore() + "/5");
            } else {
                holder.mAverageScore.setText(relic.getAverageScore() + "/5");
            }
        } else {
            holder.mYourScore.setText("Log In");
            holder.mStarImage.setImageResource(R.drawable.star_empty);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return relics.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class RelicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mYourScore;
        TextView mAverageScore;
        TextView desView;
        TextView nameView;
        ImageView mImage;
        ImageView mStarImage;
        LinearLayout mStar;

        RelicViewHolder(View itemView) {
            super(itemView);
            desView = itemView.findViewById(R.id.relic_grid_description);
            nameView = itemView.findViewById(R.id.relic_grid_name);

            mImage = itemView.findViewById(R.id.relic_grid_item_image);
            mImage.setOnClickListener(this);
            mStarImage = itemView.findViewById(R.id.your_relic_item_star);
            mYourScore = itemView.findViewById(R.id.your_relic_item_score);
            mAverageScore = itemView.findViewById(R.id.average_relic_item_score);
            mStar = itemView.findViewById(R.id.your_relic_item_score_layout);

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
    void setClickListener(e.dante.sts.Relics.RelicsAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // convenience method for getting data at click position
    Relic getItem(int id) {
        return relics.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onRatingClick(View view, int position);
    }

    public void updateList(ArrayList<Relic> relics) {
        this.relics = relics;
        notifyDataSetChanged();
    }
}
