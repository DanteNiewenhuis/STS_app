package e.dante.sts;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import e.dante.sts.Cards.CardsFragment;
import e.dante.sts.Detail.DetailTapped;
import e.dante.sts.Event.EventsFragment;
import e.dante.sts.Global.InfoHelper;
import e.dante.sts.Keyword.KeywordsFragment;
import e.dante.sts.Potion.PotionsFragment;
import e.dante.sts.Relics.RelicsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FragmentManager fragmentManager;
    private View hView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // init the navigationbar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext()
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(hView.getWindowToken(), 0);
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // start the first fragment
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment())
                .addToBackStack(null).commit();

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                    if (mUser.getUid().equals("Fl284IEleCaXsoxwBtNDoBnKtXc2")) {
                        navigationView.getMenu().findItem(R.id.nav_database).setVisible(true);
                    }

                    viewAccountInfo();
                } else {
                    navigationView.getMenu().findItem(R.id.nav_database).setVisible(false);
                    // User is signed out
                    viewLogIn();
                }

                // reload current fragment
                resetFragment();
            }
        });

        new InfoHelper().getLists();
    }

    // deletes the current fragment and creates a new instance of the same fragment
    private void resetFragment() {
        Fragment f = fragmentManager.findFragmentById(R.id.content_frame);
        if (!(f instanceof FirstFragment)) {
            fragmentManager.popBackStack();
        }
        if (f instanceof CardsFragment) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CardsFragment())
                    .addToBackStack(null).commit();
        }
        if (f instanceof DetailTapped) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new DetailTapped())
                    .addToBackStack(null).commit();
        }
    }

    private void viewLogIn() {
        //remove the current header view
        navigationView.removeHeaderView(navigationView.getHeaderView(0));

        //inflate the header view with the login fragment
        navigationView.inflateHeaderView(R.layout.header_log_in);
        hView = navigationView.getHeaderView(0);

        // set onclickListeners
        hView.findViewById(R.id.btn_log_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogIn();
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
        navigationView.inflateHeaderView(R.layout.header_sign_up);
        hView = navigationView.getHeaderView(0);

        // set onclickListener
        hView.findViewById(R.id.btn_submit_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignUp();
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
        navigationView.inflateHeaderView(R.layout.header_reset_pass);
        hView = navigationView.getHeaderView(0);

        // set onclickListener
        hView.findViewById(R.id.btn_submit_reset_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleResetPass();
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
        navigationView.inflateHeaderView(R.layout.header_account_info);
        hView = navigationView.getHeaderView(0);

        TextView nameView = hView.findViewById(R.id.nav_header_name);
        TextView mailView = hView.findViewById(R.id.nav_header_mail);

        mUser = mAuth.getCurrentUser();

        nameView.setText(mUser.getDisplayName());
        mailView.setText(mUser.getEmail());

        // set onclickListener
        hView.findViewById(R.id.btn_log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                viewLogIn();
            }
        });
    }

    private void handleSignUp() {
        EditText mail_input = hView.findViewById(R.id.mail_sign_up);
        EditText pass_input = hView.findViewById(R.id.pass_sign_up);
        EditText display_input = hView.findViewById(R.id.display_sign_up);

        String mail = mail_input.getText().toString().trim();
        final String display = display_input.getText().toString().trim();
        String password = pass_input.getText().toString().trim();

        if (TextUtils.isEmpty(display)) {
            Toast.makeText(getApplicationContext(), "Enter display name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //create user
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(MainActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            InputMethodManager imm = (InputMethodManager) getApplicationContext()
                                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(hView.getWindowToken(), 0);
                            userProfile(display);
                        }
                    }
                });
    }

    private void userProfile(final String display) {
        mUser = mAuth.getCurrentUser();

        if (mUser != null) {
            UserProfileChangeRequest profileUpdater = new UserProfileChangeRequest.Builder()
                    .setDisplayName(display).build();

            mUser.updateProfile(profileUpdater).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {
                        Toast.makeText(MainActivity.this, "Profile made: " + display,
                                Toast.LENGTH_SHORT).show();
                        viewAccountInfo();
                    }
                }
            });

        }
    }

    private void handleLogIn() {
        EditText mail_input = hView.findViewById(R.id.mail_log_in);
        final EditText pass_input = hView.findViewById(R.id.pass_log_in);

        String mail = mail_input.getText().toString().trim();
        final String password = pass_input.getText().toString().trim();

        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //authenticate user
        mAuth.signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                pass_input.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            InputMethodManager imm = (InputMethodManager) getApplicationContext()
                                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(hView.getWindowToken(), 0);
                            viewAccountInfo();
                        }
                    }
                });
    }

    //TODO handle pass reset
    private void handleResetPass() {
        viewLogIn();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment f = fragmentManager.findFragmentById(R.id.content_frame);
            if (f instanceof CardsFragment) {
                if (((CardsFragment) f).onBackPressed()) {
                    return;
                }
            }

            super.onBackPressed();
        }
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
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CardsFragment())
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_relics) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RelicsFragment())
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_keywords) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new KeywordsFragment())
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_potions) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new PotionsFragment())
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_events) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new EventsFragment())
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_database) {
            new DataScraper().execute();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
