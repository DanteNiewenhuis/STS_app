package e.dante.sts;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getFragmentManager();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            viewAccountInfo();
        }

        else {
            viewLogIn();
        }
    }

    private void viewLogIn() {
        //remove the current header view
        navigationView.removeHeaderView(navigationView.getHeaderView(0));

        //inflate the header view with the login fragment
        navigationView.inflateHeaderView(R.layout.fragment_header_log_in);
        View hView = navigationView.getHeaderView(0);

        // set onclickListeners
        hView.findViewById(R.id.btn_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO handle login
                viewAccountInfo();
            }
        });
        hView.findViewById(R.id.btn_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSignUp();
            }
        });
        hView.findViewById(R.id.btn_reset_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewResetPass();
            }
        });
    }

    private void viewSignUp() {
        //remove the current header view
        navigationView.removeHeaderView(navigationView.getHeaderView(0));

        //inflate the header view with the login fragment
        navigationView.inflateHeaderView(R.layout.fragment_header_sign_up);
        View hView = navigationView.getHeaderView(0);

        // set onclickListener
        hView.findViewById(R.id.btn_submit_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO handle sign in
                viewAccountInfo();
            }
        });
        hView.findViewById(R.id.btn_back_to_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLogIn();
            }
        });
    }

    private void viewResetPass() {
        //remove the current header view
        navigationView.removeHeaderView(navigationView.getHeaderView(0));

        //inflate the header view with the login fragment
        navigationView.inflateHeaderView(R.layout.fragment_header_reset_pass);
        View hView = navigationView.getHeaderView(0);

        // set onclickListener
        hView.findViewById(R.id.btn_submit_reset_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO handle pass reset
                viewLogIn();
            }
        });
        hView.findViewById(R.id.btn_back_to_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLogIn();
            }
        });
    }

    private void viewAccountInfo() {
        //remove the current header view
        navigationView.removeHeaderView(navigationView.getHeaderView(0));

        //inflate the header view with the login fragment
        navigationView.inflateHeaderView(R.layout.fragment_header_account_info);
        View hView = navigationView.getHeaderView(0);

        // set onclickListener
        hView.findViewById(R.id.btn_log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO handle log out
                viewLogIn();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cards) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CardsFragment()).commit();
        } else if (id == R.id.nav_relics) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RelicsFragment()).commit();
        } else if (id == R.id.nav_keywords) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new KeywordsFragment()).commit();
        } else if (id == R.id.nav_potions) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new PotionsFragment()).commit();
        } else if (id == R.id.nav_events) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new EventsFragment()).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
