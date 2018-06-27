package e.dante.sts.Cards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import e.dante.sts.Combos.DetailComboFragment;
import e.dante.sts.GlobalFunctions;
import e.dante.sts.Globals;
import e.dante.sts.R;
import e.dante.sts.RatingFragment;

public class CardDetailFragment extends Fragment implements CardHelper.SingleCallback {
    private DatabaseReference mDatabase;
    private String name;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private View myView;
    private GlobalFunctions gFunctions;

    public CardDetailFragment() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        name = args.getString("name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_card_detail, container, false);

        gFunctions = new GlobalFunctions(getActivity().getSupportFragmentManager());
        new CardHelper().getSingleCard(this, name);

        return myView;
    }

    @Override
    public void gotSingleCard(final Card card) {
        TextView desView = myView.findViewById(R.id.card_detail_normal_description);
        TextView upgradedDesView = myView.findViewById(R.id.card_detail_upgrade_description);
        TextView nameView = myView.findViewById(R.id.card_detail_name_view);
        TextView nameUpgradeView = myView.findViewById(R.id.card_detail_upgrade_name_view);
        TextView notesView = myView.findViewById(R.id.card_detail_notes);
        TextView yourScore = myView.findViewById(R.id.card_detail_your_score_view);
        TextView averageScore = myView.findViewById(R.id.card_detail_average_score_view);

        ImageView cardImgView = myView.findViewById(R.id.card_detail_image_view);


        Log.d("gotSingleCard", "init");
        desView.setText(gFunctions.makeSpans(card.getDescription()));
        upgradedDesView.setText(gFunctions.makeSpans(card.getUpgradeDescription()));
        desView.setMovementMethod(LinkMovementMethod.getInstance());
        upgradedDesView.setMovementMethod(LinkMovementMethod.getInstance());

        getActivity().setTitle(card.getName());

        nameView.setText(card.getName());
        nameUpgradeView.setText(card.getName() + "+");
        notesView.setText(card.getYourNote());
        yourScore.setText(card.getYourScore() + "/5");
        averageScore.setText(card.getAverageScore() + "/5");

        Picasso.get().load(card.getImgUrl()).into(cardImgView);

        makeOnClickListeners(card);
    }

    private void makeOnClickListeners(final Card card) {
        TextView notesView = myView.findViewById(R.id.card_detail_notes);
        myView.findViewById(R.id.card_detail_your_score_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRatingClick(card.getName(), card.getYourScore());
            }
        });
        notesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gFunctions.getNotes("Cards", card.getName(), card.getYourNote());
            }
        });

        final List<String> cardList = Globals.getInstance().getCards(card.getHero());
        for (String s: card.getYourComboCards()) {
            cardList.remove(s);
        }

        final List<String> relicList = Globals.getInstance().getRelics(card.getHero());
        for (String s: card.getYourComboRelics()) {
            relicList.remove(s);
        }
        cardList.remove(name);
        myView.findViewById(R.id.card_detail_card_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.card_detail_cards_auto_text);
                String selected = textView.getText().toString();

                if (!card.getYourComboCards().contains(selected) &&
                        cardList.contains(selected)) {
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

        myView.findViewById(R.id.card_detail_relic_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.card_detail_relics_auto_text);
                String selected = textView.getText().toString();

                if (!card.getYourComboRelics().contains(selected) &&
                        relicList.contains(selected)) {
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

        ListView comboCardsView = myView.findViewById(R.id.card_detail_combo_cards_list);
        comboCardsView.setAdapter(new ComboAdapter(getContext(), R.layout.item_combo, card.getYourComboCards(), "Cards"));

        ListView comboRelicsView = myView.findViewById(R.id.card_detail_combo_relics_list);
        comboRelicsView.setAdapter(new ComboAdapter(getContext(), R.layout.item_combo, card.getYourComboRelics(), "Relics"));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, cardList);

        ArrayAdapter<String> relicAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, relicList);

        AutoCompleteTextView cardsAutoView = myView.findViewById(R.id.card_detail_cards_auto_text);
        cardsAutoView.setAdapter(adapter);

        AutoCompleteTextView relicsAutoView = myView.findViewById(R.id.card_detail_relics_auto_text);
        relicsAutoView.setAdapter(relicAdapter);

        myView.findViewById(R.id.card_detail_other_opinions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailComboFragment fragment = new DetailComboFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", card.getName());
                bundle.putString("type", "Cards");
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });
    }

    private class ComboAdapter extends ArrayAdapter<String> {
        private int resource;
        private List<String> combos;
        private String type;

        public ComboAdapter(@NonNull Context context, int resource, @NonNull List<String> objects, String type) {
            super(context, resource, objects);
            this.resource = resource;
            this.combos = objects;
            this.type = type;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            }

            final String comboName = combos.get(position);

            //TODO make this clickable
            TextView nameView = convertView.findViewById(R.id.combo_name_view);
            nameView.setText(comboName);

            nameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gFunctions.getInfo(comboName, type);
                }
            });
            convertView.findViewById(R.id.combo_delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.child("Opinions").child("Cards").child(name).child(mUser.getUid())
                            .child("Combos").child(type).child(comboName).setValue(null);
                    mDatabase.child("Opinions").child(type).child(comboName).child(mUser.getUid())
                            .child("Combos").child("Cards").child(name).setValue(null);
                }
            });
            return convertView;
        }
    }

    @Override
    public void gotSingleCardError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void onRatingClick(String name, float score) {
        DialogFragment dialog = new RatingFragment();
        Bundle extra = new Bundle();
        extra.putString("name", name);
        extra.putString("type", "Cards");
        extra.putFloat("score", score);
        dialog.setArguments(extra);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }
}
