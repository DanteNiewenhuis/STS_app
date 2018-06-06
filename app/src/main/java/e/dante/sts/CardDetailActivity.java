package e.dante.sts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CardDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        Intent intent = getIntent();
        Card card = (Card) intent.getSerializableExtra("card");

        TextView nameView = findViewById(R.id.card_name_view);
        TextView desView = findViewById(R.id.card_description_view);
        TextView notesView = findViewById(R.id.card_notes_view);

        ImageView cardImgView = findViewById(R.id.card_image_view);

        //TODO implement the grids
        GridView comboCardsView = findViewById(R.id.combo_cards_grid);
        GridView comboRelicsView = findViewById(R.id.combo_relics_grid);

        nameView.setText(card.getName());
        desView.setText(card.getDescription());
        notesView.setText(card.getNotes());

        Picasso.get().load(card.getImgUrl()).into(cardImgView);

        //TODO implement scores
    }
}
