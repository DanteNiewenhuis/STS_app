package e.dante.sts;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class NavFragment extends Fragment implements OnFragmentInteractionListener {
    private View myView;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, final Bundle savedInstanceState) {
        myView = new LoginFragment().onCreateView(inflater, container, savedInstanceState);

        fragmentManager = getFragmentManager();
        myView.findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myView = new SignUpFragment().onCreateView(inflater, container, savedInstanceState);
            }
        });
        myView.findViewById(R.id.btn_reset_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().replace(R.id.nav_fragment, new ResetPassFragment()).commit();
            }
        });

        return myView;
    }

    public void changeFragment(String id) {
        if (id.equals("signup")) {
            fragmentManager.beginTransaction().replace(R.id.nav_fragment, new SignUpFragment()).commit();
        }
        else if (id.equals("login")) {
            fragmentManager.beginTransaction().replace(R.id.nav_fragment, new LoginFragment()).commit();
        }
        else if (id.equals("reset")) {
            fragmentManager.beginTransaction().replace(R.id.nav_fragment, new ResetPassFragment()).commit();
        }
    }
}
