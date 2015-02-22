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
import android.widget.RelativeLayout;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private static MainActivity sInstance;
    private FragmentManager mFragmentManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    public static void hideSoftInput() {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        sInstance = this;
        mFragmentManager = getFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        RelativeLayout drawer = (RelativeLayout) findViewById(R.id.drawer);
        RelativeLayout button1 = (RelativeLayout) drawer.findViewById(R.id.button_1);
        button1.setOnClickListener(this);
        RelativeLayout button2 = (RelativeLayout) drawer.findViewById(R.id.button_2);
        button2.setOnClickListener(this);
        RelativeLayout button3 = (RelativeLayout) drawer.findViewById(R.id.button_3);
        button3.setOnClickListener(this);

        mFragmentManager.beginTransaction().add(R.id.frame_layout, new SearchFragment()).commit();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_1:
                mFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new SearchFragment()).commit();
                break;
            case R.id.button_2:
                mFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new ExploreFragment()).commit();
                break;
            case R.id.button_3:
                mFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, new SurroundStopFragment()).commit();
                break;
        }
        mDrawerLayout.closeDrawers();
    }
}
