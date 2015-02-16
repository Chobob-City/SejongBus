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

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import kr.nogcha.sejongbus.main.ExploreFragment;
import kr.nogcha.sejongbus.main.SearchFragment;
import kr.nogcha.sejongbus.main.SurroundStopFragment;

public class MainActivity extends ActionBarActivity {
    private static MainActivity sInstance;
    private FragmentManager mFragmentManager;
    private int mSelectedItemId = R.id.action_search;

    public static void hideSoftInput() {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
        //TODO
        inputMethodManager.toggleSoftInput(0, 0);
    }

    public static void startHostActivity(int... args) {
        Intent intent = new Intent(sInstance, HostActivity.class);
        for (int i = 0; i < args.length; i++) intent.putExtra("arg" + i, args[i]);
        sInstance.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        sInstance = this;
        mFragmentManager = getFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.frameLayout, new SearchFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (mSelectedItemId != R.id.action_search) {
                    mSelectedItemId = R.id.action_search;
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, new SearchFragment()).commit();
                }
                return true;
            case R.id.action_explore:
                if (mSelectedItemId != R.id.action_explore) {
                    mSelectedItemId = R.id.action_explore;
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, new ExploreFragment()).commit();
                }
                return true;
            case R.id.action_surround_stop:
                if (mSelectedItemId != R.id.action_surround_stop) {
                    mSelectedItemId = R.id.action_surround_stop;
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, new SurroundStopFragment()).commit();
                }
                return true;
            case R.id.action_settings:
                if (mSelectedItemId != R.id.action_settings) {
                    mSelectedItemId = R.id.action_settings;
                    startActivity(new Intent(this, SettingsActivity.class));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
