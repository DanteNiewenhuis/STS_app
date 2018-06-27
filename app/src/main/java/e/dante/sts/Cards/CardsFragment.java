package e.dante.sts.Cards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import e.dante.sts.Detail.DetailTapped;
import e.dante.sts.R;
import e.dante.sts.Global.RatingFragment;

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

        // create an empty recycleview before the data has been gained.
        ArrayList<Card> items = new ArrayList<>();
        recyclerView = myView.findViewById(R.id.card_recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        adapter = new CardsAdapter(getContext(), items);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // get all cards from the database
        new CardHelper().getCards(this);

        // set clickListeners for the checkboxes
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
        colorList.add("Silent");
        colorList.add("Defect");
        colorList.add("Neutral");
        colorList.add("Curse");
        colorList.add("Status");

        costList = new ArrayList<>();
        costList.add("0");
        costList.add("1");
        costList.add("2");
        costList.add("3");
        costList.add("X");
        costList.add("Unplayable");

        fragmentManager = getFragmentManager();
        return myView;
    }

    // handle an item from the menu that has been clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.menu_sort_group) {
            item.setChecked(!item.isChecked());
            sortMethod = item.getTitle().toString();
        }

        updateLayout();
        return false;
    }

    // create the toolbarmenu.
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        searchView = (SearchView) item.getActionView();
        MenuItem filterButton = menu.findItem(R.id.menu_filter);

        // reverse the list if clicked
        MenuItem reverseItem = menu.findItem(R.id.menu_sort_reverse);
        reverseItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                menuItem.setChecked(!menuItem.isChecked());
                reverseCheck = menuItem.isChecked();

                updateLayout();
                return false;
            }
        });

        // open the filtermenu if the filterbutton is clicked.
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

        // run updateLayout() with the new string whenever the the text changes
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilter = newText;
                updateLayout();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    // close the searchView if open, execute the normal backpress otherwise
    public boolean onBackPressed() {
        if (searchView != null & !searchView.isIconified()) {
            searchView.setIconified(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void gotCards(ArrayList<Card> cards) {
        this.cards = cards;
        updateLayout();
    }

    // updateLayour filters the cards and updates the adapter with the new list
    private void updateLayout() {
        filteredCards = filterArrayList();
        adapter.updateList(filteredCards);
    }

    @Override
    public void gotCardsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    // go through all items in the cards list and see if they match all the filters
    private ArrayList<Card> filterArrayList() {
        ArrayList<Card> result = new ArrayList<>();

        // add items to the resulting list if match the filter
        for (Card item : this.cards) {
            if (!matchesText(item)) {
                continue;
            }

            if (!colorList.contains(item.getHero())) {
                continue;
            }

            if (!costList.contains(item.getCost())) {
                continue;
            }
            result.add(item);
        }

        // sort the list before it is returned
        return sortList(result);
    }

    // check if a card matched the search text
    private boolean matchesText(Card item) {
        String[] splitText = searchFilter.split("\\s+");

        for (String word : splitText) {
            word = word.toLowerCase();
            if (!((item.getName().toLowerCase().contains(word)) || (item.getDescription().toLowerCase().contains(word)) ||
                    (item.getUpgradeDescription().toLowerCase().contains(word)))) {
                return false;
            }
        }

        return true;
    }

    // sort the cards based on the chosen sort method
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
                    case "Rarity":
                        Log.d("compare", "winner is: " + card.getRarity().compareTo(t1.getRarity()));
                        return card.getRarity().compareTo(t1.getRarity());
                    case "Cost":
                        ArrayList<String> costSortList = new ArrayList<>();
                        costSortList.add("0");
                        costSortList.add("1");
                        costSortList.add("2");
                        costSortList.add("3");
                        costSortList.add("X");
                        costSortList.add("Unplayable");

                        if (costSortList.indexOf(card.getCost()) == costSortList.indexOf(t1.getCost())) {
                            return 0;
                        }
                        if (costSortList.indexOf(card.getCost()) < costSortList.indexOf(t1.getCost())) {
                            return -1;
                        } else return 1;
                }
                return 0;
            }
        });

        // reverse the list if needed
        if (reverseCheck) {
            Collections.reverse(cards);
        }

        return cards;
    }

    // start the detailpage of a card when it is clicked
    public void onItemClick(View view, int position) {
        String name = adapter.getItem(position).getName();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("type", "Cards");
        DetailTapped fragment = new DetailTapped();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("tag").commit();
    }

    // start the rating dialogfragment when a star is clicked
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

    // remove or add the option from the appropriate list when clicked
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
            } else {
                if (type.equals("Color")) {
                    colorList.remove(c.getText().toString());
                }
                if (type.equals("Cost")) {
                    costList.remove(c.getText().toString());
                }
            }

            updateLayout();
        }
    }
}
