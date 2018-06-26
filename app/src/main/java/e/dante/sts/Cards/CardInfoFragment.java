package e.dante.sts.Cards;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import e.dante.sts.GlobalFunctions;
import e.dante.sts.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardInfoFragment extends Fragment implements CardHelper.SingleCallback{
    private DatabaseReference mDatabase;
    private String name;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private View myView;
    private GlobalFunctions gFunctions;
    private String type;

    public CardInfoFragment() {
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
        myView =  inflater.inflate(R.layout.fragment_card_info, container, false);

        gFunctions = new GlobalFunctions(getActivity().getSupportFragmentManager());
        Log.d("CardInfo", "name: " + name);
        new CardHelper().getSingleCard(this, name);

        return myView;
    }

    @Override
    public void gotSingleCard(Card card) {
        TextView desView = myView.findViewById(R.id.card_detail_description_view);
        TextView upgradedDesView = myView.findViewById(R.id.card_detail_upgraded_description_view);
        TextView nameView = myView.findViewById(R.id.card_detail_name_view);
        TextView upgradedNameView = myView.findViewById(R.id.card_detail_upgraded_name_view);
        TextView costView = myView.findViewById(R.id.card_detail_cost_view);
        TextView upgradedCostView = myView.findViewById(R.id.card_detail_upgraded_cost_view);
        TextView yourScoreView = myView.findViewById(R.id.card_detail_your_score_view);
        TextView averageScoreView = myView.findViewById(R.id.card_detail_average_score_view);
        ImageView cardImgView = myView.findViewById(R.id.card_detail_card_image);

        desView.setText(card.getDescription());
        upgradedDesView.setText(card.getUpgradeDescription());
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
}
