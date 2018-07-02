package ru.startandroid.criminalintenttwo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class CrimeDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_CRIME_ID = "ru.startandroid.criminalintenttwo.crime_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_details);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.crime_details_container);

            if (fragment == null) {
                fragment = new CrimeDetailsFragment();
                fm.beginTransaction().add(R.id.crime_details_container, fragment).commit();
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeDetailsActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
}
