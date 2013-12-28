package com.example.hanoitour;

import com.example.hanoivisit.R;

import android.os.Bundle;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.Menu;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity{
	
	public static final String KEY_PREF_VIBRATE = "pref_vibrate";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference);
		PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preference, false);
		
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		if(v.hasVibrator()){
			getPreferenceScreen().findPreference(KEY_PREF_VIBRATE).setEnabled(false);
		}else{
			getPreferenceScreen().findPreference(KEY_PREF_VIBRATE).setEnabled(false);
			getPreferenceScreen().findPreference(KEY_PREF_VIBRATE).setSummary("Your device cannot vibrate");
		}
	}
}
