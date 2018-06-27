package e.dante.sts.Detail;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import e.dante.sts.Cards.Card;
import e.dante.sts.Cards.CardHelper;
import e.dante.sts.Global.GlobalFunctions;
import e.dante.sts.R;
import e.dante.sts.Relics.Relic;
import e.dante.sts.Relics.RelicHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailInfoFragment extends Fragment implements CardHelper.SingleCallback, RelicHelper.SingleCallback {
    private String name;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private View myView;
    private GlobalFunctions gFunctions;
    private String type;
    private Context context;

    public DetailInfoFragment() {
        // Required empty public constructor
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
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void gotSingleCard(Card card) {
        TextView desView = myView.findViewById(R.id.info_description_view);
        TextView upgradedDesView = myView.findViewById(R.id.info_upgraded_description_view);
        TextView nameView = myView.findViewById(R.id.info_name_view);
        TextView upgradedNameView = myView.findViewById(R.id.info_upgraded_name_view);
        TextView costView = myView.findViewById(R.id.info_cost_view);
        TextView upgradedCostView = myView.findViewById(R.id.info_upgraded_cost_view);
        TextView yourScoreView = myView.findViewById(R.id.info_your_score_view);
        TextView averageScoreView = myView.findViewById(R.id.info_average_score_view);
        ImageView cardImgView = myView.findViewById(R.id.info_image_view);

        desView.setText(gFunctions.makeSpans(card.getDescription()));
        upgradedDesView.setText(gFunctions.makeSpans(card.getUpgradeDescription()));
        desView.setMovementMethod(LinkMovementMethod.getInstance());
        upgradedDesView.setMovementMethod(LinkMovementMethod.getInstance());

        nameView.setText(card.getName());
        upgradedNameView.setText(card.getName() + "+");
        costView.setText(card.getCost());
        upgradedCostView.setText(card.getUpgradeCost());
        yourScoreView.setText(card.getYourScore() + "/5");
        averageScoreView.setText(card.getAverageScore() + "/5");
        Picasso.get().load(card.getImgUrl()).into(cardImgView);
    }

    @Override
    public void gotSingleCardError(String message) {

    }

    @Override
    public void gotSingleRelic(Relic relic) {
        Log.d("gotSingleRelic", "init");

        TextView desView = myView.findViewById(R.id.info_description_view);
        TextView upgradedDesView = myView.findViewById(R.id.info_upgraded_description_view);
        TextView nameView = myView.findViewById(R.id.info_name_view);
        TextView upgradedNameView = myView.findViewById(R.id.info_upgraded_name_view);
        TextView costView = myView.findViewById(R.id.info_cost_view);
        TextView upgradedCostView = myView.findViewById(R.id.info_upgraded_cost_view);
        TextView yourScoreView = myView.findViewById(R.id.info_your_score_view);
        TextView averageScoreView = myView.findViewById(R.id.info_average_score_view);
        ImageView relicImgView = myView.findViewById(R.id.info_image_view);

        desView.setText(gFunctions.makeSpans(relic.getDescription()));
        upgradedDesView.setText(gFunctions.makeSpans(relic.getInfo()));
        desView.setMovementMethod(LinkMovementMethod.getInstance());
        upgradedDesView.setMovementMethod(LinkMovementMethod.getInstance());

        nameView.setText(relic.getName());
        upgradedNameView.setText(relic.getName() + "+");
        costView.setText("");
        upgradedCostView.setText("");
        yourScoreView.setText(relic.getYourScore() + "/5");
        averageScoreView.setText(relic.getAverageScore() + "/5");
        Picasso.get().load(relic.getImgUrl()).into(relicImgView);
    }

    @Override
    public void gotSingleRelicError(String message) {

    }
}
