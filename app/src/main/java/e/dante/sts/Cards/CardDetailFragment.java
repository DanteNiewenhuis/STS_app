package e.dante.sts.Cards;

import com.google.gson.Gson;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import e.dante.sts.ComboFragment;
import e.dante.sts.ComboHelper;
import e.dante.sts.GlobalFunctions;
import e.dante.sts.InfoFragment;
import e.dante.sts.InfoHelper;
import e.dante.sts.InputFragment;
import e.dante.sts.R;
import e.dante.sts.RatingFragment;

public class CardDetailFragment extends Fragment implements CardHelper.SingleCallback,
        InfoHelper.Callback, ComboHelper.Callback {
    private DatabaseReference mDatabase;
    private String name;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private View myView;
    private GlobalFunctions gFunctions;
    private InfoHelper inforHelper;

    public CardDetailFragment() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        gFunctions = new GlobalFunctions(this);
        inforHelper = new InfoHelper();
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

        return myView;
    }

    @Override
    public void gotSingleCard(final Card card) {
        inforHelper.getSpan(this, R.id.card_description_view, card.getDescription());
        inforHelper.getSpan(this, R.id.card_upgrade_description_view, card.getUpgradeDescription());

        TextView nameView = myView.findViewById(R.id.card_name_view);
        TextView nameUpgradeView = myView.findViewById(R.id.card_upgrade_name_view);
        TextView notesView = myView.findViewById(R.id.card_notes_view);
        TextView yourScore = myView.findViewById(R.id.your_card_detail_score);
        TextView averageScore = myView.findViewById(R.id.average_card_detail_score);

        ImageView cardImgView = myView.findViewById(R.id.card_image_view);

        //TODO implement the combogrids
        LinearLayout comboCardsLayout = myView.findViewById(R.id.combo_cards_layout);
//        GridView comboRelicsView = myView.findViewById(R.id.combo_relics_list);

        comboCardsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ComboHelper().getCombos(CardDetailFragment.this, card.getName(), "Cards", "Cards");
            }
        });

        //TODO implement not being able to vote when not logged in
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
        Log.d("gotInfo", "init");
        DialogFragment dialog = new InfoFragment();
        Bundle extra = new Bundle();
        extra.putSerializable("name", name);
        extra.putSerializable("type", type);
        extra.putSerializable("des", des);
        dialog.setArguments(extra);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");
        Log.d("gotInfo", "done");

    }

    @Override
    public void gotInfoError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void gotCombos(String name, String type1, String type2, List<String> combos) {
        DialogFragment dialog = new ComboFragment();
        Bundle extra = new Bundle();
        extra.putString("name", name);
        extra.putString("type1", type1);
        extra.putString("type2", type2);
        extra.putSerializable("combos", new Gson().toJson(combos));
        dialog.setArguments(extra);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }
}
