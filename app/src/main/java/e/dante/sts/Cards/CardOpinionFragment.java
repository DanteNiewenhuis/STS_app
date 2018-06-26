package e.dante.sts.Cards;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import e.dante.sts.Combos.ComboAdapter;
import e.dante.sts.GlobalFunctions;
import e.dante.sts.Globals;
import e.dante.sts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardOpinionFragment extends Fragment implements CardHelper.SingleCallback{
    private View myView;
    private String name;
    private GlobalFunctions gFunctions;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    public CardOpinionFragment() {
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
        myView = inflater.inflate(R.layout.fragment_card_detail_opinion, container, false);
        gFunctions = new GlobalFunctions(getActivity().getSupportFragmentManager());

        new CardHelper().getSingleCard(this, name);

        return myView;
    }

    @Override
    public void gotSingleCard(final Card card) {
        //TODO add a button and make that the listener
        CardView notesView = myView.findViewById(R.id.card_detail_card_notes_card_view);
        notesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gFunctions.getNotes("Cards", card.getName(), card.getYourNote());
            }
        });

//        myView.findViewById(R.id.card_detail_your_score_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onRatingClick(card.getName(), card.getYourScore());
//            }
//        });

        final List<String> comboCards = Globals.getInstance().getCards(card.getHero());
        for (String s: card.getYourComboCards()) {
            comboCards.remove(s);
        }
        comboCards.remove(card.getName());

        final List<String> comboRelics = Globals.getInstance().getRelics(card.getHero());
        for (String s: card.getYourComboRelics()) {
            comboRelics.remove(s);
        }

        final List<String> antiComboCards = Globals.getInstance().getCards(card.getHero());
        for (String s: card.getYourAntiComboCards()) {
            antiComboCards.remove(s);
        }
        antiComboCards.remove(card.getName());

        final List<String> antiComboRelics = Globals.getInstance().getRelics(card.getHero());
        for (String s: card.getYourAntiComboRelics()) {
            antiComboRelics.remove(s);
        }

        makeAddListeners(card, comboCards, comboRelics, antiComboCards, antiComboRelics);
        makeAutoCompleteText(comboCards, comboRelics, antiComboCards, antiComboRelics);
        makeLists(card);
    }

    @Override
    public void gotSingleCardError(String message) {

    }
    public void makeLists(final Card card) {
        ListView comboCardsView = myView.findViewById(R.id.card_detail_combo_cards_list);
        comboCardsView.setAdapter(new ComboAdapter(getContext(), R.layout.item_combo,
                card.getYourComboCards(), name,"Cards", "Relics","Combos"));

        ListView comboRelicsView = myView.findViewById(R.id.card_detail_combo_relics_list);
        comboRelicsView.setAdapter(new ComboAdapter(getContext(), R.layout.item_combo,
                card.getYourComboRelics(), name,"Relics", "Relics","Combos"));

        ListView antiComboCardsView = myView.findViewById(R.id.card_detail_anti_combo_cards_list);
        antiComboCardsView.setAdapter(new ComboAdapter(getContext(), R.layout.item_combo,
                card.getYourAntiComboCards(), name,"Cards", "Relics","Anti_Combos"));

        ListView antiComboRelicsView = myView.findViewById(R.id.card_detail_anti_combo_relics_list);
        antiComboRelicsView.setAdapter(new ComboAdapter(getContext(), R.layout.item_combo,
                card.getYourAntiComboRelics(), name,"Cards", "Relics","Anti_Combos"));
    }

    public void makeAutoCompleteText(final List<String> comboCards,
                                 final List<String> comboRelics, final List<String> antiComboCards,
                                 final List<String> antiComboRelics) {

        ArrayAdapter<String> comboCardAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, comboCards);

        AutoCompleteTextView cardsAutoView = myView.findViewById(R.id.card_detail_combo_cards_auto_complete);
        cardsAutoView.setAdapter(comboCardAdapter);

        ArrayAdapter<String> comboRelicAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, comboRelics);

        AutoCompleteTextView relicsAutoView = myView.findViewById(R.id.card_detail_combo_relics_auto_complete);
        relicsAutoView.setAdapter(comboRelicAdapter);

        ArrayAdapter<String> antiComboCardAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, antiComboCards);

        AutoCompleteTextView cardsAntiAutoView = myView.findViewById(R.id.card_detail_anti_combo_cards_auto_complete);
        cardsAntiAutoView.setAdapter(antiComboCardAdapter);

        ArrayAdapter<String> antiComboRelicAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, antiComboRelics);

        AutoCompleteTextView relicsAntiAutoView = myView.findViewById(R.id.card_detail_anti_combo_relics_auto_complete);
        relicsAntiAutoView.setAdapter(antiComboRelicAdapter);
    }

    public void makeAddListeners(final Card card, final List<String> comboCards,
                                 final List<String> comboRelics, final List<String> antiComboCards,
                                 final List<String> antiComboRelics) {

        myView.findViewById(R.id.card_detail_combo_cards_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.card_detail_combo_cards_auto_complete);
                String selected = textView.getText().toString();

                if (!card.getYourComboCards().contains(selected) &&
                        comboCards.contains(selected)) {
                    mDatabase.child("Opinions").child("Cards").child(name).child(mUser.getUid())
                            .child("Combos").child("Cards").child(selected).setValue(1);
                    mDatabase.child("Opinions").child("Cards").child(selected).child(mUser.getUid())
                            .child("Combos").child("Cards").child(name).setValue(1);
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                    textView.setText("");
                }
            }
        });

        myView.findViewById(R.id.card_detail_combo_relics_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.card_detail_combo_relics_auto_complete);
                String selected = textView.getText().toString();

                if (!card.getYourComboCards().contains(selected) &&
                        comboRelics.contains(selected)) {
                    mDatabase.child("Opinions").child("Cards").child(name).child(mUser.getUid())
                            .child("Combos").child("Relics").child(selected).setValue(1);
                    mDatabase.child("Opinions").child("Relics").child(selected).child(mUser.getUid())
                            .child("Combos").child("Cards").child(name).setValue(1);
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                    textView.setText("");
                }
            }
        });

        myView.findViewById(R.id.card_detail_anti_combo_cards_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.card_detail_anti_combo_cards_auto_complete);
                String selected = textView.getText().toString();

                if (!card.getYourComboCards().contains(selected) &&
                        antiComboCards.contains(selected)) {
                    mDatabase.child("Opinions").child("Cards").child(name).child(mUser.getUid())
                            .child("Anti_Combos").child("Cards").child(selected).setValue(1);
                    mDatabase.child("Opinions").child("Cards").child(selected).child(mUser.getUid())
                            .child("Anti_Combos").child("Cards").child(name).setValue(1);
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                    textView.setText("");
                }
            }
        });

        myView.findViewById(R.id.card_detail_anti_combo_relics_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.card_detail_anti_combo_relics_auto_complete);
                String selected = textView.getText().toString();

                if (!card.getYourComboCards().contains(selected) &&
                        antiComboRelics.contains(selected)) {
                    mDatabase.child("Opinions").child("Cards").child(name).child(mUser.getUid())
                            .child("Anti_Combos").child("Cards").child(selected).setValue(1);
                    mDatabase.child("Opinions").child("Cards").child(selected).child(mUser.getUid())
                            .child("Anti_Combos").child("Relics").child(name).setValue(1);
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                    textView.setText("");
                }
            }
        });
    }
}
