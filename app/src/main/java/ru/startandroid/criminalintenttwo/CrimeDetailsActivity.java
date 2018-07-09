package ru.startandroid.criminalintenttwo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class CrimeDetailsActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private UUID crimeId;
    private Button mStartButton;
    private Button mEndButton;
    private int buttonPos;

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
            buttonSettings();
            buttonListener();

            for (int i = 0; i < mCrimes.size(); i++) {
                if (mCrimes.get(i).getId().equals(crimeId)) {
                    setCurrentIndex(i);
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
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeDetailsFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return super.getItemPosition(object);
            }
        });

    }

    private void buttonListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateButtonStateAtPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void buttonSettings() {
        mStartButton = (Button) findViewById(R.id.to_the_start);
        mEndButton = (Button) findViewById(R.id.to_the_end);


        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentIndex(0);
            }
        });


        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              setCurrentIndex(mCrimes.size()-1);
            }
        });

    }

    private void setCurrentIndex(int index) {
        mViewPager.setCurrentItem(index);
        updateButtonStateAtPosition(index);
    }

    private void updateButtonStateAtPosition(int index) {
        mStartButton.setEnabled(index > 0);
        mEndButton.setEnabled(index < mCrimes.size()-1);
    }
}
