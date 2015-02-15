/*
 * Copyright (C) 2015 Chobob City
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.nogcha.sejongbus;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends ActionBarActivity {
    private static Activity sInstance;
    private FragmentManager mFragmentManager;
    private Fragment mMainFragment1 = new MainFragment1();
    private Fragment mMainFragment2;
    private Fragment mMainFragment3;
    private Fragment mSettingsFragment;

    public static void hideSoftInput() {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        inputMethodManager.hideSoftInputFromWindow(sInstance.getCurrentFocus().getWindowToken(), 0);
    }

    public static void startHostActivity(int... args) {
        Intent intent = new Intent(sInstance, TrafficActivity.class);
        for (int i = 0; i < args.length; i++) intent.putExtra("arg" + i, args[i]);
        sInstance.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        sInstance = this;
        mFragmentManager = getFragmentManager();
        if (findViewById(R.id.frameLayout) != null) {
            if (savedInstanceState != null) {return;}
            mMainFragment1 = new MainFragment1();
            mMainFragment1.setArguments(getIntent().getExtras());
            mFragmentManager.beginTransaction().replace(R.id.frameLayout, mMainFragment1).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main_1:
                mFragmentManager.beginTransaction().replace(R.id.frameLayout, mMainFragment1)
                        .commit();
                return true;
            case R.id.action_main_2:
                if (mMainFragment2 == null) mMainFragment2 = new MainFragment2();
                mFragmentManager.beginTransaction().replace(R.id.frameLayout, mMainFragment2)
                        .commit();
                return true;
            case R.id.action_main_3:
                if (mMainFragment3 == null) mMainFragment3 = new MainFragment3();
                mFragmentManager.beginTransaction().replace(R.id.frameLayout, mMainFragment3)
                        .commit();
                return true;
            case R.id.action_settings:
                if (mSettingsFragment == null) mSettingsFragment = new SettingsFragment();
                mFragmentManager.beginTransaction().replace(R.id.frameLayout, mSettingsFragment)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
