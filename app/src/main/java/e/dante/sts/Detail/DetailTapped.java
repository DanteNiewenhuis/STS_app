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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import e.dante.sts.R;

/*
    this is the fragment which handles the bottomnavigation interaction. It does this by changing the
    fragment when a navigation item is clicked.
 */
public class DetailTapped extends Fragment {
    private View myView;
    private String name;
    private String type;
    private FirebaseUser mUser;

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
        myView = inflater.inflate(R.layout.fragment_detail_tapped, container, false);

        getActivity().setTitle(name);
        
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (type.equals("Cards")) {
            cardCreate();
        }

        if (type.equals("Relics")) {
            relicCreate();
        }

        return myView;
    }

    // create the bottom navigation for cards
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
                                // make the button unclickable and indicate that the user has to
                                // log in to use it
                                if (mUser != null) {
                                    selectedFragment = new DetailCardOpinionFragment();
                                }
                                else {
                                    item.setCheckable(false);
                                    item.setIcon(R.drawable.ic_lock_black_24dp);
                                    item.setTitle("Log in to use");
                                }
                                break;
                            case R.id.action_item3:
                                selectedFragment = new DetailComboFragment();
                                break;
                        }

                        // return false if no fragment is chosen
                        if (selectedFragment == null) {
                            return false;
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

    // create the bottom navigation for relics
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
                                // make the button unclickable and indicate that the user has to
                                // log in to use it
                                if (mUser != null) {
                                    selectedFragment = new DetailRelicOpinionFragment();
                                }
                                else {
                                    item.setCheckable(false);
                                    item.setIcon(R.drawable.ic_lock_black_24dp);
                                    item.setTitle("Log in to use");
                                }
                                break;
                            case R.id.action_item3:
                                selectedFragment = new DetailComboFragment();
                                break;
                        }

                        // return false if no fragment is chosen
                        if (selectedFragment == null) {
                            return false;
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
