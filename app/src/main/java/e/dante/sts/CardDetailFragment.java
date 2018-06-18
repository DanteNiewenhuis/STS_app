package e.dante.sts;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CardDetailFragment extends Fragment implements CardHelper.SingleCallback, InfoHelper.Callback{
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
        TextInputEditText notesView = myView.findViewById(R.id.card_notes_input);
        TextView yourScore = myView.findViewById(R.id.your_card_detail_score);
        TextView averageScore = myView.findViewById(R.id.average_card_detail_score);

        ImageView cardImgView = myView.findViewById(R.id.card_image_view);

        //TODO implement the grids
        GridView comboCardsView = myView.findViewById(R.id.combo_cards_grid);
        GridView comboRelicsView = myView.findViewById(R.id.combo_relics_grid);

        nameView.setText(card.getName());
        nameUpgradeView.setText(card.getName() + "+");
        notesView.setText(card.getNotes());
        yourScore.setText(card.getYourScore() + "/10");
        averageScore.setText(card.getAverageScore() + "/10");

        Picasso.get().load(card.getImgUrl()).into(cardImgView);

        myView.findViewById(R.id.your_card_score_detail_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRatingClick(card.getName(), card.getYourScore());
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
        dialog.show(getFragmentManager(), "dialog");
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
        dialog.show(getFragmentManager(), "dialog");
        Log.d("gotInfo", "done");

    }

    @Override
    public void gotInfoError(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }
}
