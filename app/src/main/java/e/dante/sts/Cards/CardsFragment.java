package e.dante.sts.Cards;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import e.dante.sts.GlobalFunctions;
import e.dante.sts.InfoFragment;
import e.dante.sts.InfoHelper;
import e.dante.sts.R;
import e.dante.sts.RatingFragment;

public class CardsFragment extends Fragment implements CardHelper.Callback, CardsAdapter.ItemClickListener {
    private View myView;
    private ArrayList<Card> cards;
    private ArrayList<Card> filteredCards;
    private FragmentManager fragmentManager;
    private CardsAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_cards, container, false);

        ArrayList<Card> items = new ArrayList<>();
        recyclerView = myView.findViewById(R.id.card_recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),3));

        adapter = new CardsAdapter(getContext(), items);
        adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);

        new CardHelper().getCards(this);

        myView.findViewById(R.id.checkbox_silent_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_ironclad_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_defect_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_neutral_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_name).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_color).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_type).setOnClickListener(new OnCheckBoxClickListener());

        myView.findViewById(R.id.options_button).setOnClickListener(new OptionsButtonClickListener());
        EditText searchView = myView.findViewById(R.id.search_input);
        searchView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                makeList();
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                return true;
            }
        });

        fragmentManager = getFragmentManager();
        return myView;
    }

    @Override
    public void gotCards(ArrayList<Card> cards) {
        Log.d("gotCards", "length of cards: " + cards.size());
        this.cards = cards;

        makeList();
    }

    private void makeList() {
        Log.d("makeList", "init");
        filteredCards = filterArrayList();
        Log.d("makeList", "filtered data: " + filteredCards.size());
        adapter.updateList(filteredCards);
        Log.d("makeList", "done");
    }

    @Override
    public void gotCardsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private ArrayList<Card> filterArrayList() {
        ArrayList<Card> result = new ArrayList<>();

        for (Card item : this.cards) {
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
                switch (sortMethod) {
                    case "Color":
                        return card.getHero().compareTo(t1.getHero());
                    case "Name":
                        return card.getName().compareTo(t1.getName());
                    case "Type":
                        return card.getType().compareTo(t1.getType());
                }
                return 0;
            }
        });

        return result;
    }

    private Boolean filterMatch(Card item) {
        CheckBox silentCheck = myView.findViewById(R.id.checkbox_silent_cards);
        CheckBox ironcladCheck = myView.findViewById(R.id.checkbox_ironclad_cards);
        CheckBox defectCheck = myView.findViewById(R.id.checkbox_defect_cards);
        CheckBox neutralCheck = myView.findViewById(R.id.checkbox_neutral_cards);
        EditText searchView = myView.findViewById(R.id.search_input);
        String searchText = searchView.getText().toString();

        if (!(item.getName().toLowerCase().contains(searchText.toLowerCase())) &&
                !(item.getDescription().toLowerCase().contains(searchText.toLowerCase()))) {
            return false;
        }

        ArrayList<String> colorList = new ArrayList<>();
        if (silentCheck.isChecked()) colorList.add("Silent");
        if (ironcladCheck.isChecked()) colorList.add("Ironclad");
        if (defectCheck.isChecked()) colorList.add("Defect");
        if (neutralCheck.isChecked()) colorList.add("Neutral");

        return (colorList.contains(item.getHero()));
    }

    private class OptionsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.load(getContext(), R.layout.fragment_cards);
            if (myView.findViewById(R.id.options_layout).getVisibility() == View.VISIBLE) {
                myView.findViewById(R.id.options_layout).setVisibility(View.GONE);
                myView.findViewById(R.id.search_layout).setVisibility(View.GONE);
                constraintSet.connect(R.id.options_button, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                return;
            }

            if (myView.findViewById(R.id.options_layout).getVisibility() == View.GONE) {
                myView.findViewById(R.id.options_layout).setVisibility(View.VISIBLE);
                myView.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
                constraintSet.connect(R.id.options_button, ConstraintSet.TOP, R.id.options_layout, ConstraintSet.BOTTOM, 0);
            }
        }
    }

    private class OnCheckBoxClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            makeList();
        }
    }

    public void onItemClick(View view, int position) {
        String name = adapter.getItem(position).getName();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        CardDetailFragment fragment = new CardDetailFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("tag").commit();
    }

    public void onRatingClick(View view, int position) {
        Card item = adapter.getItem(position);
        DialogFragment dialog = new RatingFragment();
        Bundle extra = new Bundle();
        extra.putString("name", item.getName());
        extra.putFloat("score", item.getYourScore());
        dialog.setArguments(extra);
        dialog.show(fragmentManager, "dialog");
    }
}