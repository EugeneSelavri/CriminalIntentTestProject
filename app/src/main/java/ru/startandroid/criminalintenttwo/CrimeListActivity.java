package ru.startandroid.criminalintenttwo;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.UUID;

public class CrimeListActivity extends AppCompatActivity implements CrimeListFragment.onSomeEventListener {
    private CrimeListFragment fragmentList;
    private CrimeDetailsFragment fragmentDetail;
    private UUID id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_list);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentList = fm.findFragmentById(R.id.crime_list_container);
        FragmentTransaction ft = fm.beginTransaction();

        if (fragmentList == null) {
            fragmentList = new CrimeListFragment();
            ft.add(R.id.crime_list_container, fragmentList);
        }

        this.fragmentList = (CrimeListFragment) fragmentList;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Fragment fragmentDetail = fm.findFragmentById(R.id.crime_detail);
            if (fragmentDetail == null) {
                fragmentDetail = CrimeDetailsFragment.newInstance(id);
                ft.add(R.id.crime_detail, fragmentDetail);
            }
            this.fragmentDetail = (CrimeDetailsFragment) fragmentDetail;
        }
        ft.commit();
    }

    @Override
    public void someEvent(boolean startActivity, Crime crime) {
        id = crime.getId();
        if (startActivity) {
            Intent intent = CrimeDetailsActivity.newIntent(this, id);
            startActivity(intent);
        } else if (fragmentDetail != null) {
            fragmentDetail.showCrime(crime);
        }
    }
}
