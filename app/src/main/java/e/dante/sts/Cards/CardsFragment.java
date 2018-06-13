package e.dante.sts.Cards;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import e.dante.sts.R;

public class CardsFragment extends Fragment implements CardHelper.Callback{
    private View myView;
    private ArrayList<Card> cards;
    private ArrayList<Card> filteredCards;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_cards, container, false);

        new CardHelper().getCards(this);

        myView.findViewById(R.id.checkbox_green_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_red_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_colorless_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_name).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_color).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_type).setOnClickListener(new OnCheckBoxClickListener());

        myView.findViewById(R.id.options_button).setOnClickListener(new OptionsButtonClickListener());
        EditText searchView = myView.findViewById(R.id.search_input);
        searchView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                makeList();
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return true;
            }
        });
        return myView;
    }


    private class OptionsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (myView.findViewById(R.id.options_layout).getVisibility() == View.VISIBLE) {
                myView.findViewById(R.id.options_layout).setVisibility(View.GONE);
                myView.findViewById(R.id.search_layout).setVisibility(View.GONE);
                return;
            }

            if (myView.findViewById(R.id.options_layout).getVisibility() == View.GONE) {
                myView.findViewById(R.id.options_layout).setVisibility(View.VISIBLE);
                myView.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void gotCards(ArrayList<Card> cards) {
        this.cards = cards;

        makeList();
    }

    private class OnCheckBoxClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            makeList();
        }
    }

    private void makeList() {
        ListView listView = myView.findViewById(R.id.card_list_view);

        filteredCards = filterArrayList();
        listView.setAdapter(new CardsAdapter(getContext(), R.layout.card_item, filteredCards));

        listView.setOnItemClickListener(new CardsItemClickListener());
    }
    @Override
    public void gotCardsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private ArrayList<Card> filterArrayList() {
        ArrayList<Card> result = new ArrayList<>();

        for (Card item: this.cards) {
            if (filterMatch(item)) {
                result.add(item);
            }
        }

        RadioGroup sortGroup = myView.findViewById(R.id.sort_group);
        RadioButton checked = myView.findViewById(sortGroup.getCheckedRadioButtonId());
        final String sortMethod = checked.getText().toString();

        Collections.sort(result, new Comparator<Card>() {
            @Override
            public int compare(Card card, Card t1) {
                switch(sortMethod) {
                    case "Color": return card.getColor().compareTo(t1.getColor());
                    case "Name": return card.getName().compareTo(t1.getName());
                    case "Type": return card.getType().compareTo(t1.getType());
                }
                return 0;
            }
        });

        return result;
    }

    private Boolean filterMatch(Card item) {
        CheckBox greenCheck = myView.findViewById(R.id.checkbox_green_cards);
        CheckBox redCheck = myView.findViewById(R.id.checkbox_red_cards);
        CheckBox colorlessCheck = myView.findViewById(R.id.checkbox_colorless_cards);
        EditText searchView = myView.findViewById(R.id.search_input);
        String searchText = searchView.getText().toString();

        if (!(item.getName().toLowerCase().contains(searchText.toLowerCase())) &&
                !(item.getDescription().toLowerCase().contains(searchText.toLowerCase()))) {
            return false;
        }

        ArrayList<String> colorList = new ArrayList<>();
        if (greenCheck.isChecked()) colorList.add("Green");
        if (redCheck.isChecked()) colorList.add("Red");
        if (colorlessCheck.isChecked()) colorList.add("Colorless");

        return (colorList.contains(item.getColor()));
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
//            Log.d("cards listener", "init");
//
//            Intent intent = new Intent(CardsActivity.this, CardDetailActivity.class);
//            intent.putExtra("card", (Card) parent.getItemAtPosition(position));
//
//            Log.d("cards listener", "start intent");
//            startActivity(intent);
        }
    }
}
