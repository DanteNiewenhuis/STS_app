package e.dante.sts.Relics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import e.dante.sts.R;
import e.dante.sts.RatingFragment;

public class RelicsFragment extends Fragment implements RelicHelper.Callback, RelicsAdapter.ItemClickListener {
    private View myView;
    private ArrayList<Relic> relics;
    private ArrayList<Relic> filteredRelics;
    private FragmentManager fragmentManager;
    private RelicsAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_relics, container, false);

        ArrayList<Relic> items = new ArrayList<>();
        recyclerView = myView.findViewById(R.id.relic_recycle_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),3));

        adapter = new RelicsAdapter(getContext(), items);
        adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);

        new RelicHelper().getRelics(this);

        // init all clicklisteners so the layout is remade when clicked
        //TODO init filter and sort options
        myView.findViewById(R.id.checkbox_silent_relics).setOnClickListener(new e.dante.sts.Relics.RelicsFragment.OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_ironclad_relics).setOnClickListener(new e.dante.sts.Relics.RelicsFragment.OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_defect_relics).setOnClickListener(new e.dante.sts.Relics.RelicsFragment.OnCheckBoxClickListener());
        myView.findViewById(R.id.checkbox_neutral_relics).setOnClickListener(new e.dante.sts.Relics.RelicsFragment.OnCheckBoxClickListener());
        myView.findViewById(R.id.reverse_check).setOnClickListener(new e.dante.sts.Relics.RelicsFragment.OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_name).setOnClickListener(new e.dante.sts.Relics.RelicsFragment.OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_class).setOnClickListener(new e.dante.sts.Relics.RelicsFragment.OnCheckBoxClickListener());
        myView.findViewById(R.id.radio_rarity).setOnClickListener(new e.dante.sts.Relics.RelicsFragment.OnCheckBoxClickListener());

        // create the search onclicklistener
        //TODO add searchlistener
        myView.findViewById(R.id.options_button).setOnClickListener(new e.dante.sts.Relics.RelicsFragment.OptionsButtonClickListener());
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
    public void gotRelics(ArrayList<Relic> relics) {
        Log.d("gotRelics", "length of relics: " + relics.size());
        this.relics = relics;

        makeList();
    }

    private void makeList() {
        Log.d("makeList", "init");
        filteredRelics = filterArrayList();
        Log.d("makeList", "filtered data: " + filteredRelics.size());
        adapter.updateList(filteredRelics);
        Log.d("makeList", "done");
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

        // get the chosen sort method and sort accordingly
        RadioGroup sortGroup = myView.findViewById(R.id.sort_group);
        RadioButton checked = myView.findViewById(sortGroup.getCheckedRadioButtonId());
        final String sortMethod = checked.getText().toString();

        Collections.sort(result, new Comparator<Relic>() {
            @Override
            public int compare(Relic relic, Relic t1) {
                switch (sortMethod) {
                    case "Class":
                        return relic.getHero().compareTo(t1.getHero());
                    case "Name":
                        return relic.getName().compareTo(t1.getName());
                    case "Rarity":
                        return relic.getRarity().compareTo(t1.getRarity());
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

    private Boolean filterMatch(Relic item) {
        CheckBox silentCheck = myView.findViewById(R.id.checkbox_silent_relics);
        CheckBox ironcladCheck = myView.findViewById(R.id.checkbox_ironclad_relics);
        CheckBox defectCheck = myView.findViewById(R.id.checkbox_defect_relics);
        CheckBox anyCheck = myView.findViewById(R.id.checkbox_neutral_relics);

        if (!matchesText(item)) {
            return false;
        }

        ArrayList<String> colorList = new ArrayList<>();
        if (silentCheck.isChecked()) colorList.add("Silent");
        if (ironcladCheck.isChecked()) colorList.add("Ironclad");
        if (defectCheck.isChecked()) colorList.add("Defect");
        if (anyCheck.isChecked()) colorList.add("Any");

        return (colorList.contains(item.getHero()));
    }

    private boolean matchesText(Relic item) {
        EditText searchView = myView.findViewById(R.id.search_input);

        String searchText = searchView.getText().toString();

        String[] splitText = searchText.split("\\s+");

        for (String word: splitText) {
            word = word.toLowerCase();
            if (!((item.getName().toLowerCase().contains(word)) || (item.getDescription().toLowerCase().contains(word)))){
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
                myView.findViewById(R.id.search_layout).setVisibility(View.GONE);
                return;
            }

            if (myView.findViewById(R.id.options_layout).getVisibility() == View.GONE) {
                Log.d("OptionsButton::onClick", "GONE");
                myView.findViewById(R.id.options_layout).setVisibility(View.VISIBLE);
                myView.findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
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
        RelicDetailFragment fragment = new RelicDetailFragment();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("tag").commit();
    }

    public void onRatingClick(View view, int position) {
        Relic item = adapter.getItem(position);
        DialogFragment dialog = new RatingFragment();
        Bundle extra = new Bundle();
        extra.putString("name", item.getName());
        extra.putFloat("score", item.getYourScore());
        dialog.setArguments(extra);
        dialog.show(fragmentManager, "dialog");
    }
}
