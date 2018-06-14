package e.dante.sts.Relics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import e.dante.sts.R;

public class RelicDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_relic_detail);

        Intent intent = getIntent();
        Relic relic = (Relic) intent.getSerializableExtra("relic");

        TextView nameView = findViewById(R.id.relic_name_view);
        TextView desView = findViewById(R.id.relic_description_view);
        TextView notesView = findViewById(R.id.relic_notes_view);

        ImageView relicImgView = findViewById(R.id.relic_image_view);

        //TODO implement the grids
        GridView comboCardsView = findViewById(R.id.combo_cards_grid);
        GridView comboRelicsView = findViewById(R.id.combo_relics_grid);

        nameView.setText(relic.getName());
        desView.setText(relic.getDescription());
        notesView.setText(relic.getNotes());

        Picasso.get().load(relic.getImgUrl()).into(relicImgView);
    }
}
