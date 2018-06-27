package e.dante.sts.Detail;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import e.dante.sts.Combos.ComboAdapter;
import e.dante.sts.Global.GlobalFunctions;
import e.dante.sts.Global.Globals;
import e.dante.sts.R;
import e.dante.sts.Relics.Relic;
import e.dante.sts.Relics.RelicHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailRelicOpinionFragment extends Fragment implements RelicHelper.SingleCallback {
    private View myView;
    private String name;
    private GlobalFunctions gFunctions;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private Context context;

    public DetailRelicOpinionFragment() {
        // Required empty public constructor
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
        name = args.getString("name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_detail_opinion, container, false);
        gFunctions = new GlobalFunctions(getActivity().getSupportFragmentManager());

        new RelicHelper().getSingleRelic(this, name);

        return myView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void gotSingleRelic(final Relic relic) {
        TextView notesView = myView.findViewById(R.id.opinions_notes_text_view);
        Button notesButton = myView.findViewById(R.id.opinions_notes_button);

        if (!(relic.getYourNote().equals(""))) {
            notesView.setText(gFunctions.makeSpans(relic.getYourNote()));
        }
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gFunctions.getNotes("Relics", relic.getName(), relic.getYourNote());
            }
        });

        final List<String> comboCards = Globals.getInstance().getCards(relic.getHero());
        final List<String> antiComboCards = Globals.getInstance().getCards(relic.getHero());

        for (String s : relic.getYourComboCards()) {
            comboCards.remove(s);
            antiComboCards.remove(s);
        }
        for (String s : relic.getYourAntiComboCards()) {
            comboCards.remove(s);
            antiComboCards.remove(s);
        }

        final List<String> comboRelics = Globals.getInstance().getRelics(relic.getHero());
        final List<String> antiComboRelics = Globals.getInstance().getRelics(relic.getHero());
        for (String s : relic.getYourComboRelics()) {
            comboRelics.remove(s);
            antiComboRelics.remove(s);
        }
        for (String s : relic.getYourAntiComboRelics()) {
            comboRelics.remove(s);
            antiComboRelics.remove(s);
        }

        comboRelics.remove(relic.getName());
        antiComboRelics.remove(relic.getName());

        makeAutoCompleteText(comboCards, comboRelics, antiComboCards, antiComboRelics);
        makeLists(relic);
        makeAddListeners(relic, comboCards, comboRelics, antiComboCards, antiComboRelics);
    }

    @Override
    public void gotSingleRelicError(String message) {

    }

    public void makeLists(final Relic relic) {
        ListView comboCardsView = myView.findViewById(R.id.opinions_combo_cards_list);
        comboCardsView.setAdapter(new ComboAdapter(context, R.layout.item_combo,
                relic.getYourComboCards(), name, "Relics", "Cards", "Combos"));

        ListView comboRelicsView = myView.findViewById(R.id.opinions_combo_relics_list);
        comboRelicsView.setAdapter(new ComboAdapter(context, R.layout.item_combo,
                relic.getYourComboRelics(), name, "Relics", "Relics", "Combos"));

        ListView antiComboCardsView = myView.findViewById(R.id.opinions_anti_combo_cards_list);
        antiComboCardsView.setAdapter(new ComboAdapter(context, R.layout.item_combo,
                relic.getYourAntiComboCards(), name, "Relics", "Cards", "Anti_Combos"));

        ListView antiComboRelicsView = myView.findViewById(R.id.opinions_anti_combo_relics_list);
        antiComboRelicsView.setAdapter(new ComboAdapter(context, R.layout.item_combo,
                relic.getYourAntiComboRelics(), name, "Relics", "Relics", "Anti_Combos"));
    }

    public void makeAutoCompleteText(final List<String> comboCards,
                                     final List<String> comboRelics, final List<String> antiComboCards,
                                     final List<String> antiComboRelics) {

        ArrayAdapter<String> comboCardAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, comboCards);

        AutoCompleteTextView cardsAutoView = myView.findViewById(R.id.opinions_combo_cards_auto_complete);
        cardsAutoView.setAdapter(comboCardAdapter);

        ArrayAdapter<String> comboRelicAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, comboRelics);

        AutoCompleteTextView relicsAutoView = myView.findViewById(R.id.opinions_combo_relics_auto_complete);
        relicsAutoView.setAdapter(comboRelicAdapter);

        ArrayAdapter<String> antiComboCardAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, antiComboCards);

        AutoCompleteTextView cardsAntiAutoView = myView.findViewById(R.id.opinions_anti_combo_cards_auto_complete);
        cardsAntiAutoView.setAdapter(antiComboCardAdapter);

        ArrayAdapter<String> antiComboRelicAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, antiComboRelics);

        AutoCompleteTextView relicsAntiAutoView = myView.findViewById(R.id.opinions_anti_combo_relics_auto_complete);
        relicsAntiAutoView.setAdapter(antiComboRelicAdapter);
    }

    public void makeAddListeners(final Relic relic, final List<String> comboCards,
                                 final List<String> comboRelics, final List<String> antiComboCards,
                                 final List<String> antiComboRelics) {

        myView.findViewById(R.id.opinions_combo_cards_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.opinions_combo_cards_auto_complete);
                String selected = textView.getText().toString();

                if (comboCards.contains(selected)) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                    textView.setText("");
                    mDatabase.child("Opinions").child("Relics").child(name).child(mUser.getUid())
                            .child("Combos").child("Cards").child(selected).setValue(1);
                    mDatabase.child("Opinions").child("Cards").child(selected).child(mUser.getUid())
                            .child("Combos").child("Relics").child(name).setValue(1);
                }
            }
        });

        myView.findViewById(R.id.opinions_combo_relics_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.opinions_combo_relics_auto_complete);
                String selected = textView.getText().toString();

                if (comboRelics.contains(selected)) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                    textView.setText("");
                    mDatabase.child("Opinions").child("Relics").child(name).child(mUser.getUid())
                            .child("Combos").child("Relics").child(selected).setValue(1);
                    mDatabase.child("Opinions").child("Relics").child(selected).child(mUser.getUid())
                            .child("Combos").child("Relics").child(name).setValue(1);
                }
            }
        });

        myView.findViewById(R.id.opinions_anti_combo_cards_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.opinions_anti_combo_cards_auto_complete);
                String selected = textView.getText().toString();

                if (antiComboCards.contains(selected)) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                    textView.setText("");
                    mDatabase.child("Opinions").child("Relics").child(name).child(mUser.getUid())
                            .child("Anti_Combos").child("Cards").child(selected).setValue(1);
                    mDatabase.child("Opinions").child("Cards").child(selected).child(mUser.getUid())
                            .child("Anti_Combos").child("Relics").child(name).setValue(1);
                }
            }
        });

        myView.findViewById(R.id.opinions_anti_combo_relics_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.opinions_anti_combo_relics_auto_complete);
                String selected = textView.getText().toString();

                if (antiComboRelics.contains(selected)) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                    textView.setText("");
                    mDatabase.child("Opinions").child("Relics").child(name).child(mUser.getUid())
                            .child("Anti_Combos").child("Relics").child(selected).setValue(1);
                    mDatabase.child("Opinions").child("Relics").child(selected).child(mUser.getUid())
                            .child("Anti_Combos").child("Relics").child(name).setValue(1);
                }
            }
        });
    }
}
