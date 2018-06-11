package e.dante.sts;

import android.content.Context;
import android.content.Intent;
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
        ListView listView = findViewById(R.id.card_list_view);
        listView.setAdapter(new CardsAdapter(this, R.layout.card_item, cards));

        listView.setOnItemClickListener(new CardsItemClickListener());
    }

    @Override
    public void gotCardsError(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    // TODO make it load less at a time.
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

            //TODO implement type
            String type = item.getType();
            String cost = item.getCost();

            ImageView card_img_view = convertView.findViewById(R.id.card_item_img);
            TextView nameView = convertView.findViewById(R.id.card_item_name);
            TextView descriptionView = convertView.findViewById(R.id.card_item_des);
            TextView costView = convertView.findViewById(R.id.card_item_cost);

            nameView.setText(name);
            descriptionView.setText(description);
            costView.setText(cost);
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

    // TODO this needs to be changed to a listener for specific points in the item and not the whole item!
    private class CardsItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("cards listener", "init");

            Intent intent = new Intent(CardsActivity.this, CardDetailActivity.class);
            intent.putExtra("card", (Card) parent.getItemAtPosition(position));

            Log.d("cards listener", "start intent");
            startActivity(intent);
        }
    }
}
