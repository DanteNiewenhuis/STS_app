package e.dante.sts.Detail;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import e.dante.sts.Cards.Card;
import e.dante.sts.Cards.CardHelper;
import e.dante.sts.Global.GlobalFunctions;
import e.dante.sts.Global.RatingFragment;
import e.dante.sts.R;
import e.dante.sts.Relics.Relic;
import e.dante.sts.Relics.RelicHelper;

/*
    This class makes the infofragment that is placed on in the detailTapped page as the first page
 */
public class DetailInfoFragment extends Fragment implements CardHelper.SingleCallback, RelicHelper.SingleCallback {
    private String name;
    private View myView;
    private GlobalFunctions gFunctions;
    private String type;
    private FirebaseUser mUser;

    public DetailInfoFragment() {
        // Required empty public constructor
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
        name = args.getString("name");
        type = args.getString("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_info, container, false);

        gFunctions = new GlobalFunctions(getActivity().getSupportFragmentManager());

        Log.d("InfoFragment", "type: " + type);
        if (type.equals("Cards")) {
            new CardHelper().getSingleCard(this, name);
        }

        if (type.equals("Relics")) {
            Log.d("detailInfo", "init relic");
            new RelicHelper().getSingleRelic(this, name);
        }

        return myView;
    }

    @Override
    // create the layout for the chosen card
    public void gotSingleCard(final Card card) {
        // get all views
        TextView desView = myView.findViewById(R.id.info_description_view);
        TextView upgradedDesView = myView.findViewById(R.id.info_upgraded_description_view);
        TextView nameView = myView.findViewById(R.id.info_name_view);
        TextView upgradedNameView = myView.findViewById(R.id.info_upgraded_name_view);
        TextView costView = myView.findViewById(R.id.info_cost_view);
        TextView upgradedCostView = myView.findViewById(R.id.info_upgraded_cost_view);
        TextView yourScoreView = myView.findViewById(R.id.info_your_score_view);
        TextView averageScoreView = myView.findViewById(R.id.info_average_score_view);
        TextView voteCountView = myView.findViewById(R.id.info_vote_count);
        ImageView cardImgView = myView.findViewById(R.id.info_image_view);

        // add all the information of the card to the views
        desView.setText(gFunctions.makeSpans(card.getDescription()));
        upgradedDesView.setText(gFunctions.makeSpans(card.getUpgradeDescription()));
        nameView.setText(card.getName());
        upgradedNameView.setText(card.getName() + "+");
        costView.setText(card.getCost());
        upgradedCostView.setText(card.getUpgradeCost());
        averageScoreView.setText(card.getAverageScore() + "/5");
        Picasso.get().load(card.getImgUrl()).into(cardImgView);

        // make the text clickable
        desView.setMovementMethod(LinkMovementMethod.getInstance());
        upgradedDesView.setMovementMethod(LinkMovementMethod.getInstance());

        if (mUser != null) {
            yourScoreView.setText(card.getYourScore() + "/5");

            ConstraintLayout score_layout = myView.findViewById(R.id.info_score_layout);
            score_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment dialog = new RatingFragment();
                    Bundle extra = new Bundle();
                    extra.putString("name", card.getName());
                    extra.putString("type", "Cards");
                    extra.putFloat("score", card.getYourScore());
                    dialog.setArguments(extra);
                    dialog.show(getActivity().getSupportFragmentManager(), "dialog");
                }
            });
        }
        else {
            yourScoreView.setText("Log In");
        }

        voteCountView.setText(card.getVoteCount() + " votes");
    }

    @Override
    public void gotSingleCardError(String message) {

    }

    @Override
    // create the layout for the chosen relic
    public void gotSingleRelic(final Relic relic) {
        // get all views
        TextView desView = myView.findViewById(R.id.info_description_view);
        TextView upgradedDesView = myView.findViewById(R.id.info_upgraded_description_view);
        TextView nameView = myView.findViewById(R.id.info_name_view);
        TextView upgradedNameView = myView.findViewById(R.id.info_upgraded_name_view);
        TextView costView = myView.findViewById(R.id.info_cost_view);
        TextView upgradedCostView = myView.findViewById(R.id.info_upgraded_cost_view);
        TextView yourScoreView = myView.findViewById(R.id.info_your_score_view);
        TextView averageScoreView = myView.findViewById(R.id.info_average_score_view);
        TextView voteCountView = myView.findViewById(R.id.info_vote_count);
        ImageView relicImgView = myView.findViewById(R.id.info_image_view);

        // set the information of the relic in all the views
        desView.setText(gFunctions.makeSpans(relic.getDescription()));
        upgradedDesView.setText(gFunctions.makeSpans(relic.getInfo()));
        upgradedNameView.setText("Information");
        nameView.setText(relic.getName());
        costView.setText("");
        upgradedCostView.setText("");
        averageScoreView.setText(relic.getAverageScore() + "/5");
        Picasso.get().load(relic.getImgUrl()).into(relicImgView);

        // make the text clickable
        desView.setMovementMethod(LinkMovementMethod.getInstance());
        upgradedDesView.setMovementMethod(LinkMovementMethod.getInstance());

        if (mUser != null) {
            yourScoreView.setText(relic.getYourScore() + "/5");

            ConstraintLayout score_layout = myView.findViewById(R.id.info_score_layout);
            score_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment dialog = new RatingFragment();
                    Bundle extra = new Bundle();
                    extra.putString("name", relic.getName());
                    extra.putString("type", "Relics");
                    extra.putFloat("score", relic.getYourScore());
                    dialog.setArguments(extra);
                    dialog.show(getActivity().getSupportFragmentManager(), "dialog");
                }
            });
        }
        else {
            yourScoreView.setText("Log In");
        }
        voteCountView.setText(relic.getVoteCount() + " votes");
    }

    @Override
    public void gotSingleRelicError(String message) {
        Toast.makeText(this.getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }
}
