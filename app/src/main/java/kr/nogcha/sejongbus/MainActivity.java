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
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import kr.nogcha.sejongbus.main.ExploreFragment;
import kr.nogcha.sejongbus.main.SearchFragment;
import kr.nogcha.sejongbus.main.SurroundStopFragment;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    private static MainActivity sInstance;
    private FragmentManager mFragmentManager;
    private LinearLayout tbtn1,tbtn2,tbtn3;
    private ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    public static void hideSoftInput() {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        RelativeLayout rl_drawer = (RelativeLayout) findViewById(R.id.drawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.main_drawerLayout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(toggle);
        tbtn1 = (LinearLayout)rl_drawer.findViewById(R.id.btn1);
        tbtn1.setOnClickListener(this);
        tbtn2 = (LinearLayout)rl_drawer.findViewById(R.id.btn2);
        tbtn2.setOnClickListener(this);
        tbtn3 = (LinearLayout)rl_drawer.findViewById(R.id.btn3);
        tbtn3.setOnClickListener(this);

        mFragmentManager = getFragmentManager();
        sInstance = this;
        mFragmentManager.beginTransaction().add(R.id.frameLayout, new SearchFragment()).commit();

    }

    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.btn1:
                mFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new SearchFragment()).commit();
                break;
            case R.id.btn2:
                mFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new ExploreFragment()).commit();
                break;
            case R.id.btn3:
                mFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new SurroundStopFragment()).commit();
                break;
        }
        drawerLayout.closeDrawers();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
