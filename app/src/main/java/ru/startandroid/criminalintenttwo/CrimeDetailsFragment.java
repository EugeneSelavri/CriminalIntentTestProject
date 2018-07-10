package ru.startandroid.criminalintenttwo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

public class CrimeDetailsFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mTimeButton;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    public static CrimeDetailsFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, id);

        CrimeDetailsFragment fragment = new CrimeDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.crime_details_fragment, container, false);
        if (container != null) {
            connectingWidgets(v);
            showCrime(mCrime);
        }
        return v;
    }

    private void connectingWidgets(View view) {
        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mCrime != null) {
                    mCrime.setTitle(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //nothing
            }
        });

        mDateButton = view.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeDetailsFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mCrime != null) {
                    mCrime.setSolved(isChecked);
                }
            }
        });

        mTimeButton = view.findViewById(R.id.crime_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment picker = TimePickerFragment.newInstance(mCrime.getDate());
                picker.setTargetFragment(CrimeDetailsFragment.this, REQUEST_TIME);
                picker.show(manager, DIALOG_TIME);
            }
        });
    }

    public void showCrime(Crime crime) {
        mCrime = crime;

        if (crime == null) {
            return;
        }

        mTitleField.setText(mCrime.getTitle());
        updateDateUI();

        mSolvedCheckBox.setChecked(mCrime.isSolved());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            int year = data.getIntExtra(DatePickerFragment.EXTRA_YEAR, 1970);
            int month = data.getIntExtra(DatePickerFragment.EXTRA_MONTH, 1);
            int day = data.getIntExtra(DatePickerFragment.EXTRA_DAY, 1);
            mCrime.setDate(MyDateUtils.updateDate(mCrime.getDate(), year, month, day));
        } else if (requestCode == REQUEST_TIME) {
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
            int minute = data.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 0);
            mCrime.setDate(MyDateUtils.updateDate(mCrime.getDate(), hour, minute));
        }
        updateDateUI();
    }

    private void updateDateUI() {
        String formattedDate = MyDateUtils.formatDate(mCrime.getDate(), "EEE, MMM d, ''yy");
        mDateButton.setText(formattedDate);

        String formattedTime = MyDateUtils.formatDate(mCrime.getDate(), "HH:mm");
        mTimeButton.setText(formattedTime);
    }
}
