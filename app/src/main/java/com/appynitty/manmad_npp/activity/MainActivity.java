package com.appynitty.manmad_npp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.appynitty.ghantagaditracker.activity.SplashScreenActivity;
import com.appynitty.ghantagaditracker.utils.AUtils;
import com.appynitty.ghantagaditracker.utils.LocaleHelper;
import com.appynitty.manmad_npp.BuildConfig;
import com.appynitty.manmad_npp.R;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context base) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            super.attachBaseContext(LocaleHelper.onAttach(base, AUtils.LanguageConstants.MARATHI));
        } else {
            super.attachBaseContext(base);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Prefs.putString(AUtils.APP_ID, "1022");
        Prefs.putString(AUtils.APP_ID_GG, "1031");

        Prefs.putString(AUtils.LOCATION, "20.255615,74.440824");
        Prefs.putInt(AUtils.VERSION_CODE, BuildConfig.VERSION_CODE);

        Prefs.putString(AUtils.LANGUAGE_NAME, Prefs.getString(AUtils.LANGUAGE_NAME, AUtils.LanguageConstants.MARATHI));

        List<String> languageList = new ArrayList<String>();
        languageList.add(AUtils.LanguageNameConstants.ENGLISH);
        languageList.add(AUtils.LanguageNameConstants.MARATHI);
        AUtils.setLanguageList(languageList);

        startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
