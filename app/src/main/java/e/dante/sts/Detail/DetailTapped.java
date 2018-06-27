package e.dante.sts.Detail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import e.dante.sts.Combos.DetailComboFragment;
import e.dante.sts.R;
import e.dante.sts.Relics.DetailRelicOpinionFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTapped extends Fragment {
    private View myView;
    private String name;
    private String type;

    public DetailTapped() {
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
        myView =  inflater.inflate(R.layout.fragment_detail_tapped, container, false);

        if (type.equals("Cards")) {
            cardCreate();
        }

        if (type.equals("Relics")) {
            relicCreate();
        }

        return myView;
    }

    private void cardCreate() {
        BottomNavigationView bottomNavigationView = myView.findViewById(R.id.bottem_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = new DetailInfoFragment();
                                break;
                            case R.id.action_item2:
                                selectedFragment = new DetailCardOpinionFragment();
                                break;
                            case R.id.action_item3:
                                selectedFragment = new DetailComboFragment();
                                break;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "Cards");
                        bundle.putString("name", name);
                        selectedFragment.setArguments(bundle);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.bottom_nav_frame, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        bottomNavigationView.setSelectedItemId(R.id.action_item1);
    }

    private void relicCreate() {
        BottomNavigationView bottomNavigationView = myView.findViewById(R.id.bottem_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = new DetailInfoFragment();
                                break;
                            case R.id.action_item2:
                                selectedFragment = new DetailRelicOpinionFragment();
                                break;
                            case R.id.action_item3:
                                selectedFragment = new DetailComboFragment();
                                break;
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "Relics");
                        bundle.putString("name", name);
                        selectedFragment.setArguments(bundle);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.bottom_nav_frame, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        bottomNavigationView.setSelectedItemId(R.id.action_item1);
    }

}
