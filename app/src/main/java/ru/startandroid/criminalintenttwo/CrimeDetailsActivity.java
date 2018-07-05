package ru.startandroid.criminalintenttwo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class CrimeDetailsActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private UUID crimeId;

    private static final String EXTRA_CRIME_ID = "ru.startandroid.criminalintenttwo.crime_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_details);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {

            crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
            pagerSettings();
            for (int i = 0; i < mCrimes.size(); i++) {
                if (mCrimes.get(i).getId().equals(crimeId)) {
                    mViewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimeDetailsActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    private void pagerSettings() {
        mViewPager = (ViewPager) findViewById(R.id.crime_details_container);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeDetailsFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
    }
}
