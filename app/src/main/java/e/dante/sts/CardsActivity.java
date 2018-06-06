package e.dante.sts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardsActivity extends AppCompatActivity implements CardHelper.Callback{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        new CardHelper().getCards(this);
    }

    @Override
    public void gotCards(ArrayList<Card> cards) {
        ListView list_view = findViewById(R.id.card_list_view);
        list_view.setAdapter(new CardsAdapter(this, R.layout.card_item, cards));
        list_view.setOnItemClickListener(new CardsItemClickListener());
    }

    @Override
    public void gotCardsError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    private class CardsAdapter extends ArrayAdapter<Card> {
        private int resource;
        private ArrayList<Card> cards;

        public CardsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Card> objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.cards = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }

            Card item = cards.get(position);

            String name = item.getName();
            String description = item.getDescription();
            String color = item.getColor();
            String imgUrl = item.getImgUrl();
            String type = item.getType();
            String cost = item.getCost();

            ImageView card_img_view = convertView.findViewById(R.id.potion_img);
            TextView nameView = convertView.findViewById(R.id.potion_name);
            TextView descriptionView = convertView.findViewById(R.id.potion_description);
            TextView costView = convertView.findViewById(R.id.card_cost);

            nameView.setText(name);
            descriptionView.setText(description);
            costView.setText(cost);
            Log.d("gotCards", "img url: " + imgUrl);
            Picasso.get().load(imgUrl).into(card_img_view);

            if (color .equals("Red")) {
                convertView.setBackgroundColor(convertView.getResources().getColor(android.R.color.holo_red_light, getDropDownViewTheme()));
            }
            if (color .equals("Green")) {
                convertView.setBackgroundColor(convertView.getResources().getColor(android.R.color.holo_green_dark, getDropDownViewTheme()));
            }
            if (color .equals("Colorless")) {
                convertView.setBackgroundColor(convertView.getResources().getColor(android.R.color.darker_gray, getDropDownViewTheme()));
            }
            return convertView;
        }
    }

    private class CardsItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //TODO make items clickable
        }
    }
}
