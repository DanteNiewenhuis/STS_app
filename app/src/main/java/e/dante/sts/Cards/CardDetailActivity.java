package e.dante.sts.Cards;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import e.dante.sts.R;

public class CardDetailActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String cardName;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_card_detail);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        Intent intent = getIntent();
        Card card = (Card) intent.getSerializableExtra("card");
        cardName = card.getName();

        TextView nameView = findViewById(R.id.card_name_view);
        TextView desView = findViewById(R.id.card_description_view);
        TextInputEditText notesView = findViewById(R.id.card_notes_input);
        RatingBar yourScore = findViewById(R.id.your_card_score);
        RatingBar averageScore = findViewById(R.id.average_card_score);

        ImageView cardImgView = findViewById(R.id.card_image_view);

        //TODO implement the grids
        GridView comboCardsView = findViewById(R.id.combo_cards_grid);
        GridView comboRelicsView = findViewById(R.id.combo_relics_grid);

        nameView.setText(cardName);
        desView.setText(card.getDescription());
        notesView.setText(card.getNotes());
        yourScore.setRating(card.getYourScore());
        averageScore.setRating(card.getAverageScore());

        Picasso.get().load(card.getImgUrl()).into(cardImgView);

        yourScore.setOnRatingBarChangeListener(new CardScoreListener());
    }

    private class CardScoreListener implements RatingBar.OnRatingBarChangeListener {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            mDatabase.child("Cards").child(cardName).child("scores").child(mUser.getUid()).setValue(v);
        }
    }
}
