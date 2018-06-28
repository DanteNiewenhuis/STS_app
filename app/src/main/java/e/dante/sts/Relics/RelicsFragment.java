package e.dante.sts.Relics;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
/*
    class where the gridview of all the relics a showed. also implements the filtering and sorting
    of the arraylist of relics.
 */
public class RelicsFragment extends Fragment implements RelicHelper.Callback, RelicsAdapter.ItemClickListener {
    private View myView;
    private ArrayList<Relic> relics;
    private ArrayList<Relic> filteredRelics;
    private FragmentManager fragmentManager;
    private RelicsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<String> colorList;

    private String searchFilter = "";
    private String sortMethod = "Name";
    private boolean reverseCheck = false;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        myView = inflater.inflate(R.layout.fragment_relics, container, false);

        getActivity().setTitle("Relics");

        ArrayList<Relic> items = new ArrayList<>();
        recyclerView = myView.findViewById(R.id.relic_recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));

        adapter = new RelicsAdapter(getContext(), items);
        adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);

        new RelicHelper().getRelics(this);

        // set all onclicklisteners
        myView.findViewById(R.id.checkbox_ironclad_relics).setOnClickListener(new CheckBoxlistener("Color"));
        myView.findViewById(R.id.checkbox_silent_relics).setOnClickListener(new CheckBoxlistener("Color"));
        myView.findViewById(R.id.checkbox_defect_relics).setOnClickListener(new CheckBoxlistener("Color"));
        myView.findViewById(R.id.checkbox_neutral_relics).setOnClickListener(new CheckBoxlistener("Color"));

        // add all colors to the colorList
        colorList = new ArrayList<>();
        colorList.add("Ironclad");
        colorList.add("Silent");
        colorList.add("Defect");
        colorList.add("Any");
        colorList.add("Curse");
        colorList.add("Status");

        fragmentManager = getFragmentManager();
        return myView;
    }

    @Override
    // handle an item from the menu that has been clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.menu_sort_group) {
            item.setChecked(!item.isChecked());
            sortMethod = item.getTitle().toString();
        }

        updateLayout();
        return false;
    }

    @Override
    // create the searchmenu with the possibility to search, filter and sort
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        searchView = (SearchView) item.getActionView();
        MenuItem filterButton = menu.findItem(R.id.menu_filter);

        // change the reverseCheck based on if the item is checked
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

        // make the filterbutton show and hide the filterlayout
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

        // handle the searchinput when it is changed
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

    public boolean onBackPressed() {
        if (searchView != null & !searchView.isIconified()) {
            searchView.setIconified(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void gotRelics(ArrayList<Relic> relics) {
        Log.d("gotRelics", "length of relics: " + relics.size());
        this.relics = relics;

        updateLayout();
    }

    // filter the card data and update the adapter
    private void updateLayout() {
        filteredRelics = filterArrayList();
        adapter.updateList(filteredRelics);
    }

    @Override
    public void gotRelicsError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private ArrayList<Relic> filterArrayList() {
        ArrayList<Relic> result = new ArrayList<>();

        // add items to the resulting list if match the filter
        for (Relic item : this.relics) {
            if (filterMatch(item)) {
                result.add(item);
            }
        }


        return sortList(result);
    }

    private ArrayList<Relic> sortList(ArrayList<Relic> relics) {
        Collections.sort(relics, new Comparator<Relic>() {
            @Override
            public int compare(Relic relic, Relic t1) {
                switch (sortMethod) {
                    case "Color":
                        return relic.getHero().compareTo(t1.getHero());
                    case "Name":
                        return relic.getName().compareTo(t1.getName());
                    case "Rarity":
                        return relic.getRarity().compareTo(t1.getRarity());
                }
                return 0;
            }
        });

        if (reverseCheck) {
            Collections.reverse(relics);
        }

        return relics;
    }

    @NonNull
    private Boolean filterMatch(Relic item) {
        if (!matchesText(item)) {
            return false;
        }

        if (!colorList.contains(item.getHero())) {
            return false;
        }

        return true;
    }

    private boolean matchesText(Relic item) {
        String[] splitText = searchFilter.split("\\s+");

        for (String word : splitText) {
            word = word.toLowerCase();
            if (!((item.getName().toLowerCase().contains(word)) ||
                    (item.getDescription().toLowerCase().contains(word)))) {
                return false;
            }
        }

        return true;
    }

    public void onItemClick(View view, int position) {
        Log.d("RelicsFragment", "onItemClick");
        String name = adapter.getItem(position).getName();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("type", "Relics");
        DetailTapped fragment = new DetailTapped();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("tag").commit();
    }

    public void onRatingClick(View view, int position) {
        Relic item = adapter.getItem(position);
        DialogFragment dialog = new RatingFragment();
        Bundle extra = new Bundle();
        extra.putString("name", item.getName());
        extra.putString("type", "Relics");
        extra.putFloat("score", item.getYourScore());
        dialog.setArguments(extra);
        dialog.show(fragmentManager, "dialog");
    }

    private class CheckBoxlistener implements View.OnClickListener {
        private String type;

        CheckBoxlistener(String type) {
            this.type = type;
        }

        @Override
        public void onClick(View view) {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                if (type.equals("Color")) {
                    colorList.add(c.getText().toString());
                }
            } else {
                if (type.equals("Color")) {
                    colorList.remove(c.getText().toString());
                }
            }

            updateLayout();
        }
    }
}
