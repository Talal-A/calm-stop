package com.policestrategies.calm_stop.citizen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.policestrategies.calm_stop.R;
import com.policestrategies.calm_stop.SharedUtil;
import com.policestrategies.calm_stop.citizen.beacon_detection.BeaconDetectionActivity;

import org.w3c.dom.Text;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by mariavizcaino on 4/14/17.
 */

public class ProfileDisplayActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;

    private TextView Name;
    private TextView Email;
    private TextView Phone;
    private TextView DOB;
    private TextView DriverLis;
    private TextView Gender;
    private TextView Lang;
    private TextView Ethn;
    private TextView Zipcode;

    private TextView Title;
    private TextView Nametxt;
    private TextView Emailtxt;
    private TextView Phonetxt;
    private TextView DOBtxt;
    private TextView DriverListxt;
    private TextView Gendertxt;
    private TextView Langtxt;
    private TextView Ethntxt;
    private TextView Zipcodetxt;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mProfileReference;

    private static final String TAG = "ProfileActivity";

    private ProgressDialog mProgressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profiledisplay);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/avenir-next.ttf");

        Name = (TextView) findViewById(R.id.Nametxt);
        Email = (TextView) findViewById(R.id.Emailtxt);
        Phone = (TextView) findViewById(R.id.Phonenumtxt);
        DOB = (TextView) findViewById(R.id.DOBtxt);
        DriverLis = (TextView) findViewById(R.id.DriversListxt);
        Zipcode = (TextView) findViewById(R.id.Zipcodetxt);
        Lang = (TextView) findViewById(R.id.Langtxt);
        Gender = (TextView) findViewById(R.id.Gendertxt);
        Ethn = (TextView) findViewById(R.id.Ethnicitytxt);

        Title = (TextView) findViewById(R.id.title);
        Nametxt = (TextView) findViewById(R.id.Name);
        Emailtxt = (TextView) findViewById(R.id.Email);
        Phonetxt = (TextView) findViewById(R.id.Phonenumber);
        DOBtxt = (TextView) findViewById(R.id.DOB);
        DriverListxt = (TextView) findViewById(R.id.DriversLis);
        Zipcodetxt = (TextView) findViewById(R.id.Zipcode);
        Langtxt = (TextView) findViewById(R.id.langPref);
        Gendertxt = (TextView) findViewById(R.id.Gender);
        Ethntxt = (TextView) findViewById(R.id.ethnicity);

        findViewById(R.id.viewDocs).setOnClickListener(this);
        findViewById(R.id.EditButton).setOnClickListener(this);
        findViewById(R.id.menu_main).setOnClickListener(this);

        mProgressDialog = ProgressDialog.show(this, "", "Loading", true, false);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mCurrentUser == null) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            mProfileReference = FirebaseDatabase.getInstance().getReference("citizen")
                    .child(mCurrentUser.getUid()).child("profile");

        }

        customizeFont(custom_font);

        mProfileReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String zip = snapshot.child("zip_code").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String firstName = snapshot.child("first_name").getValue().toString();
                String lastName = snapshot.child("last_name").getValue().toString();
                String license = snapshot.child("license_number").getValue().toString();
                String phoneNumber = snapshot.child("phone_number").getValue().toString();
                String dateOfBirth = snapshot.child("dob").getValue().toString();
                String gender = snapshot.child("gender").getValue().toString();
                String language = snapshot.child("language").getValue().toString();
                String ethnicity = snapshot.child("ethnicity").getValue().toString();


                String name = firstName + " " + lastName;
                Zipcode.setText(zip);
                Email.setText(email);
                Name.setText(name);
                DriverLis.setText(license);
                Phone.setText(phoneNumber);
                DOB.setText(dateOfBirth);
                Ethn.setText(ethnicity);
                Gender.setText(gender);
                Lang.setText(language);

                SharedUtil.dismissProgressDialog(mProgressDialog);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }

        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id) {
                    case R.id.profile:
                        profile();
                        break;
                    case R.id.previous_stops:
                        previousStops();
                        break;
                    case R.id.help:
                        help();
                        break;
                    case R.id.about_us:
                        aboutUs();
                        break;
                    case R.id.settings:
                        settings();
                        break;
                    case R.id.logout:
                        logout();
                        break;
                    case R.id.documents:
                        documents();
                        break;

                    case R.id.detect_beacon_debug:
                        detectBecon();
                        break;
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /*
    private String getEth(int id){
        switch (id){
            case 0:
                return "Prefer Not to Answer";
            case 1:
                return "African American";
            case 2:
                return "American Indian";
            case 3:
                return "Asian";
            case 4:
                return "Hispanic";
            case 5:
                return "Pacific Islander";
            case 6:
                return "White";
        }
        return "Prefer Not to Answer";
    }

    private String getGen(int id){
        switch (id){
            case 0:
                return "Female";
            case 1:
                return "Male";
            case 2:
                return "Prefer Not to Answer";
        }
        return "Prefer Not to Answer";
    }

    private String getLang(int id){
        switch (id){
            case 0:
                return "Arabic";
            case 1:
                return "Chinese (Mandarin)";
            case 2:
                return "English";
            case 3:
                return "French";
            case 4:
                return "German";
            case 5:
                return "Italian";
            case 6:
                return "Portuguese";
            case 7:
                return "Russian";
            case 8:
                return "Spanish";
            case 9:
                return "Swedish";
            case 10:
                return "Vietnamese";
        }
        return "English";
    }
*/

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewDocs:
                toDocuments();
                break;
            case R.id.EditButton:
                toProfileEdit();
                break;
            case R.id.menu_main:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
    }

    private void customizeFont(Typeface custom_font){

        Title.setTypeface(custom_font);
        Nametxt.setTypeface(custom_font);
        Emailtxt.setTypeface(custom_font);
        Phonetxt.setTypeface(custom_font);
        DOBtxt.setTypeface(custom_font);
        DriverListxt.setTypeface(custom_font);
        Zipcodetxt.setTypeface(custom_font);
        Langtxt.setTypeface(custom_font);
        Gendertxt.setTypeface(custom_font);
        Ethntxt.setTypeface(custom_font);

        Name.setTextColor(getResources().getColor(R.color.black));
        Name.setTypeface(custom_font);
        Name.setTextSize(14);

        Email.setTextColor(getResources().getColor(R.color.black));
        Email.setTypeface(custom_font);
        Email.setTextSize(14);

        Phone.setTextColor(getResources().getColor(R.color.black));
        Phone.setTypeface(custom_font);
        Phone.setTextSize(14);

        DOB.setTextColor(getResources().getColor(R.color.black));
        DOB.setTypeface(custom_font);
        DOB.setTextSize(14);

        DriverLis.setTextColor(getResources().getColor(R.color.black));
        DriverLis.setTypeface(custom_font);
        DriverLis.setTextSize(14);

        Zipcode.setTextColor(getResources().getColor(R.color.black));
        Zipcode.setTypeface(custom_font);
        Zipcode.setTextSize(14);

        Lang.setTextColor(getResources().getColor(R.color.black));
        Lang.setTypeface(custom_font);
        Lang.setTextSize(14);

        Gender.setTextColor(getResources().getColor(R.color.black));
        Gender.setTypeface(custom_font);
        Gender.setTextSize(14);

        Ethn.setTextColor(getResources().getColor(R.color.black));
        Ethn.setTypeface(custom_font);
        Ethn.setTextSize(14);

    }

    private void toHomepage() {
        Intent i = new Intent(getBaseContext(), HomepageActivity.class);
        startActivity(i);
    }

    private void toProfileEdit() {
        Intent i = new Intent(getBaseContext(), ProfileActivity.class);
        startActivity(i);
    }

    private void toDocuments() {
        Intent i = new Intent(getBaseContext(), DocumentsActivity.class);
        startActivity(i);
    }

    private void profile() {
        Intent i = new Intent(getBaseContext(), ProfileDisplayActivity.class);
        startActivity(i);
        finish();
    }

    private void previousStops() {
        Intent i = new Intent(getBaseContext(), PreviousStopsActivity.class);
        startActivity(i);
        finish();
    }

    private void help() {
        Intent i = new Intent(getBaseContext(), HelpActivity.class);
        startActivity(i);
        finish();
    }

    private void aboutUs() {
        Intent i = new Intent(getBaseContext(), AboutUsActivity.class);
        startActivity(i);
        finish();
    }

    private void settings() {
        Intent i = new Intent(getBaseContext(), SettingsActivity.class);
        startActivity(i);
        finish();
    }

    private void documents() {
        Intent i = new Intent(getBaseContext(), DocumentsActivity.class);
        startActivity(i);
        finish();
    }

    private void logout() {
        //You want to logout -> login page
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void detectBecon(){
        Intent i = new Intent(this, BeaconDetectionActivity.class);
        startActivity(i);
        finish();
    }


}
