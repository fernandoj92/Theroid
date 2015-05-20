package com.client.thera.theroid.presentation.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.client.thera.theroid.R;

/**
 * Created by Fer on 19/05/2015.
 */
public class SettingsActivity extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
