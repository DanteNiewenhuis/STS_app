package e.dante.sts.Cards;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
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

import java.util.ArrayList;
import java.util.List;

import e.dante.sts.GlobalFunctions;
import e.dante.sts.Globals;
import e.dante.sts.InfoFragment;
import e.dante.sts.InfoHelper;
import e.dante.sts.InputFragment;
import e.dante.sts.R;
import e.dante.sts.RatingFragment;

public class CardDetailFragment extends Fragment implements CardHelper.SingleCallback,
        InfoHelper.Callback {
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

        gFunctions = new GlobalFunctions(this);
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

        new CardHelper().getSingleCard(this, name);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, Globals.getInstance().getCards());

        ArrayAdapter<String> relicAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, Globals.getInstance().getRelics());

        AutoCompleteTextView cardAutoView = myView.findViewById(R.id.auto_text_cards);
        cardAutoView.setAdapter(adapter);

        AutoCompleteTextView relicAutoView = myView.findViewById(R.id.auto_text_relics);
        relicAutoView.setAdapter(relicAdapter);

        return myView;
    }

    @Override
    public void gotSingleCard(final Card card) {
        TextView desView = myView.findViewById(R.id.card_description_view);
        TextView upgradedDesView = myView.findViewById(R.id.card_upgrade_description_view);
        TextView nameView = myView.findViewById(R.id.card_name_view);
        TextView nameUpgradeView = myView.findViewById(R.id.card_upgrade_name_view);
        TextView notesView = myView.findViewById(R.id.card_notes_view);
        TextView yourScore = myView.findViewById(R.id.your_card_detail_score);
        TextView averageScore = myView.findViewById(R.id.average_card_detail_score);

        ImageView cardImgView = myView.findViewById(R.id.card_image_view);

        //TODO implement not being able to vote when not logged in
        desView.setText(gFunctions.makeSpans(card.getDescription()));
        upgradedDesView.setText(gFunctions.makeSpans(card.getUpgradeDescription()));
        desView.setMovementMethod(LinkMovementMethod.getInstance());
        upgradedDesView.setMovementMethod(LinkMovementMethod.getInstance());

        nameView.setText(card.getName());
        nameUpgradeView.setText(card.getName() + "+");
        notesView.setText(card.getYourNote());
        yourScore.setText(card.getYourScore() + "/10");
        averageScore.setText(card.getAverageScore() + "/10");

        Picasso.get().load(card.getImgUrl()).into(cardImgView);

        myView.findViewById(R.id.your_card_score_detail_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRatingClick(card.getName(), card.getYourScore());
            }
        });
        notesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new InputFragment();
                Bundle extra = new Bundle();
                extra.putString("name", card.getName());
                extra.putString("oldNote", card.getYourNote());
                dialog.setArguments(extra);
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

        myView.findViewById(R.id.cards_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.auto_text_cards);
                String selected = textView.getText().toString();

                if (!card.getYourComboCards().contains(selected)) {
                    mDatabase.child("Combos").child("Cards").child(name).child("Cards").child(mUser.getUid()).child(selected).setValue(1);
                    mDatabase.child("Combos").child("Cards").child(selected).child("Cards").child(mUser.getUid()).child(name).setValue(1);
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                }
            }
        });

        myView.findViewById(R.id.relics_add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AutoCompleteTextView textView = myView.findViewById(R.id.auto_text_relics);
                String selected = textView.getText().toString();

                if (!card.getYourComboCards().contains(selected)) {
                    mDatabase.child("Combos").child("Cards").child(name).child("Relics").child(mUser.getUid()).child(selected).setValue(1);
                    mDatabase.child("Combos").child("Relics").child(selected).child("Cards").child(mUser.getUid()).child(name).setValue(1);
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myView.getWindowToken(), 0);
                }
            }
        });

        ListView comboCardsView = myView.findViewById(R.id.combo_cards_list);
        comboCardsView.setAdapter(new ComboAdapter(getContext(), R.layout.item_combo, card.getYourComboCards(), "Cards"));

        ListView comboRelicsView = myView.findViewById(R.id.combo_relics_list);
        comboRelicsView.setAdapter(new ComboAdapter(getContext(), R.layout.item_combo, card.getYourComboRelics(), "Relics"));
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

            convertView.findViewById(R.id.combo_delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.child("Combos").child("Cards").child(name).child(type).child(mUser.getUid()).child(comboName).setValue(null);
                    mDatabase.child("Combos").child(type).child(comboName).child("Cards").child(mUser.getUid()).child(name).setValue(null);
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
        extra.putFloat("score", score);
        dialog.setArguments(extra);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }

    public void gotSpan(int viewId, SpannableString ss) {
        TextView textView = myView.findViewById(viewId);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void gotInfo(String name, String type, String des) {
        DialogFragment dialog = new InfoFragment();
        Bundle extra = new Bundle();
        extra.putSerializable("name", name);
        extra.putSerializable("type", type);
        extra.putSerializable("des", des);
        dialog.setArguments(extra);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");

    }

    @Override
    public void gotInfoError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
