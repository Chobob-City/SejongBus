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
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Button btn1, btn2, btn3, btn4;
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

        toolbar = (Toolbar) findViewById(R.id.main_toolBar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        LinearLayout ll_drawer = (LinearLayout) findViewById(R.id.main_drawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(toggle);

        sInstance = this;
        mFragmentManager = getFragmentManager();
        if (findViewById(R.id.frameLayout) != null) {
            if (savedInstanceState != null) {return;}
            mMainFragment1 = new MainFragment1();
            mMainFragment1.setArguments(getIntent().getExtras());
            mFragmentManager.beginTransaction().replace(R.id.frameLayout, mMainFragment1).commit();
        }
        btn1 = (Button) ll_drawer.findViewById(R.id.btn1);
        btn2 = (Button) ll_drawer.findViewById(R.id.btn2);
        btn3 = (Button) ll_drawer.findViewById(R.id.btn3);
        btn4 = (Button) ll_drawer.findViewById(R.id.btn4);
        for(int i = 0; i < 4; i++){
            findViewById(R.id.btn1 + i).setOnClickListener(this);
        }
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn1:
                mFragmentManager.beginTransaction().replace(R.id.frameLayout, mMainFragment1)
                        .commit();
                break;
            case R.id.btn2:
                if (mMainFragment2 == null) mMainFragment2 = new MainFragment2();
                mFragmentManager.beginTransaction().replace(R.id.frameLayout, mMainFragment2)
                        .commit();
                break;
            case R.id.btn3:
                if (mMainFragment3 == null) mMainFragment3 = new MainFragment3();
                mFragmentManager.beginTransaction().replace(R.id.frameLayout, mMainFragment3)
                        .commit();
                break;
            case R.id.btn4:
                if (mSettingsFragment == null) mSettingsFragment = new SettingsFragment();
                mFragmentManager.beginTransaction().replace(R.id.frameLayout, mSettingsFragment)
                        .commit();
                break;
        }
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
    
}
