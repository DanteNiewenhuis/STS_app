package e.dante.sts.Cards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private ArrayList<String> colorList;
    private ArrayList<String> costList;

    private String searchFilter = "";
    private String sortMethod = "Name";
    private boolean reverseCheck = false;
    private SearchView searchView;

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

        myView.findViewById(R.id.checkbox_ironclad_cards).setOnClickListener(new CheckBoxlistener("Color"));
        myView.findViewById(R.id.checkbox_silent_cards).setOnClickListener(new CheckBoxlistener("Color"));
        myView.findViewById(R.id.checkbox_defect_cards).setOnClickListener(new CheckBoxlistener("Color"));
        myView.findViewById(R.id.checkbox_neutral_cards).setOnClickListener(new CheckBoxlistener("Color"));

        myView.findViewById(R.id.checkbox_cost_0).setOnClickListener(new CheckBoxlistener("Cost"));
        myView.findViewById(R.id.checkbox_cost_1).setOnClickListener(new CheckBoxlistener("Cost"));
        myView.findViewById(R.id.checkbox_cost_2).setOnClickListener(new CheckBoxlistener("Cost"));
        myView.findViewById(R.id.checkbox_cost_3).setOnClickListener(new CheckBoxlistener("Cost"));
        myView.findViewById(R.id.checkbox_cost_X).setOnClickListener(new CheckBoxlistener("Cost"));

        colorList = new ArrayList<>();
        colorList.add("Ironclad");
        colorList.add("silent");
        colorList.add("Defect");
        colorList.add("Colorless");

        costList = new ArrayList<>();
        costList.add("0");
        costList.add("1");
        costList.add("2");
        costList.add("3");
        costList.add("X");

        fragmentManager = getFragmentManager();
        return myView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.menu_sort_group) {
            item.setChecked(!item.isChecked());
            sortMethod = item.getTitle().toString();
        }

        makeList();
        return false;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        searchView = (SearchView) item.getActionView();
        MenuItem filterButton = menu.findItem(R.id.menu_filter);

        MenuItem reverseItem = menu.findItem(R.id.menu_sort_reverse);
        reverseItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                menuItem.setChecked(!menuItem.isChecked());
                reverseCheck = menuItem.isChecked();

                makeList();
                return false;
            }
        });

        filterButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                LinearLayout filterLayout = myView.findViewById(R.id.filter_layout);

                if (filterLayout.getVisibility() == View.GONE) {
                    filterLayout.setVisibility(View.VISIBLE);
                    return true;
                }
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    filterLayout.setVisibility(View.GONE);
                    return true;
                }

                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilter = newText;
                makeList();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onBackPressed() {
        if (searchView != null & !searchView.isIconified()) {
            searchView.setIconified(true);
            return true;
        }
        else {
            return false;
        }
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


        return sortList(result);
    }

    private ArrayList<Card> sortList(ArrayList<Card> cards) {
        Collections.sort(cards, new Comparator<Card>() {
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

        if (reverseCheck) {
            Collections.reverse(cards);
        }

        return cards;
    }

    private Boolean filterMatch(Card item) {
        if (!matchesText(item)) {
            return false;
        }

        if (!colorList.contains(item.getHero())) {
            return false;
        }

        if (!costList.contains(item.getCost())) {
            return false;
        }

        return true;
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

    public void onItemClick(View view, int position) {
        String name = adapter.getItem(position).getName();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        CardDetailTapped fragment = new CardDetailTapped();
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

    private class CheckBoxlistener implements View.OnClickListener {
        private String type;

        public CheckBoxlistener(String type) {
            this.type = type;
        }

        @Override
        public void onClick(View view) {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                if (type.equals("Color")) {
                    colorList.add(c.getText().toString());
                }
                if (type.equals("Cost")) {
                    costList.add(c.getText().toString());
                }
            }

            else {
                if (type.equals("Color")) {
                    colorList.remove(c.getText().toString());
                }
                if (type.equals("Cost")) {
                    costList.remove(c.getText().toString());
                }
            }

            makeList();
        }
    }
}
