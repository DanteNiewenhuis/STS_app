package e.dante.sts.Cards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import e.dante.sts.R;
import e.dante.sts.RatingFragment;

public class CardsFragment extends Fragment implements CardHelper.Callback, CardsAdapter.ItemClickListener {
    private View myView;
    private ArrayList<Card> cards;
    private ArrayList<Card> filteredCards;
    private FragmentManager fragmentManager;
    private CardsAdapter adapter;
    private RecyclerView recyclerView;
    private String searchFilter = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        myView = inflater.inflate(R.layout.fragment_cards, container, false);

        getActivity().setTitle("Cards");

        ArrayList<Card> items = new ArrayList<>();
        recyclerView = myView.findViewById(R.id.card_recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),3));

        adapter = new CardsAdapter(getContext(), items);
        adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);

        new CardHelper().getCards(this);

        // init all clicklisteners so the layout is remade when clicked
        myView.findViewById(R.id.checkbox_silent_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_ironclad_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_defect_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_neutral_cards).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.reverse_check).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_name).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_class).setOnClickListener(new OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_type).setOnClickListener(new OnCheckBoxClickListener());

        // create the search onclicklistener
        myView.findViewById(R.id.options_button).setOnClickListener(new OptionsButtonClickListener());

        fragmentManager = getFragmentManager();
        return myView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilter = newText;
                makeList();
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void gotCards(ArrayList<Card> cards) {
        Log.d("gotCards", "length of cards: " + cards.size());
        this.cards = cards;

        makeList();
    }

    //TODO try to cut this into two parts, one for the search and one for the filter
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

        // add items to the resulting list if match the filter
        for (Card item : this.cards) {
            if (filterMatch(item)) {
                result.add(item);
            }
        }

        // get the chosen sort method and sort accordingly
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

        // reverse the arraylist if needed
        CheckBox reverseCheck = myView.findViewById(R.id.reverse_check);
        if (reverseCheck.isChecked()) {
            Collections.reverse(result);
        }

        return result;
    }

    private Boolean filterMatch(Card item) {
        CheckBox silentCheck = myView.findViewById(R.id.checkbox_silent_cards);
        CheckBox ironcladCheck = myView.findViewById(R.id.checkbox_ironclad_cards);
        CheckBox defectCheck = myView.findViewById(R.id.checkbox_defect_cards);
        CheckBox neutralCheck = myView.findViewById(R.id.checkbox_neutral_cards);

        if (!matchesText(item)) {
            return false;
        }

        ArrayList<String> colorList = new ArrayList<>();
        if (silentCheck.isChecked()) colorList.add("Silent");
        if (ironcladCheck.isChecked()) colorList.add("Ironclad");
        if (defectCheck.isChecked()) colorList.add("Defect");
        if (neutralCheck.isChecked()) colorList.add("Neutral");

        return (colorList.contains(item.getHero()));
    }

    private boolean matchesText(Card item) {
        String[] splitText = searchFilter.split("\\s+");

        for (String word: splitText) {
            word = word.toLowerCase();
            if (!((item.getName().toLowerCase().contains(word)) || (item.getDescription().toLowerCase().contains(word)) ||
                    (item.getUpgradeDescription().toLowerCase().contains(word)))){
                return false;
            }
        }

        return true;
    }

    private class OptionsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.d("OptionsButton::onClick", "init");
            if (myView.findViewById(R.id.options_layout).getVisibility() == View.VISIBLE) {
                Log.d("OptionsButton::onClick", "VISIBLE");
                myView.findViewById(R.id.options_layout).setVisibility(View.GONE);
                ImageView button = myView.findViewById(R.id.options_button);
                button.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                return;
            }

            if (myView.findViewById(R.id.options_layout).getVisibility() == View.GONE) {
                Log.d("OptionsButton::onClick", "GONE");
                myView.findViewById(R.id.options_layout).setVisibility(View.VISIBLE);
                ImageView button = myView.findViewById(R.id.options_button);
                button.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
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
        extra.putString("type", "Cards");
        extra.putFloat("score", item.getYourScore());
        dialog.setArguments(extra);
        dialog.show(fragmentManager, "dialog");
    }
}
