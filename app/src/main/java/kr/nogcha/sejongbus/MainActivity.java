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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private static MainActivity sInstance;
    private ViewPager pager;
    public ImageButton[] tabarray = new ImageButton[5];
    public int prevBtnNum;

    public static void hideSoftInput() {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MainSlideAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(5);

        tabarray[0] = (ImageButton) findViewById(R.id.tab_btn_1);
        tabarray[1] = (ImageButton) findViewById(R.id.tab_btn_2);
        tabarray[2] = (ImageButton) findViewById(R.id.tab_btn_3);
        tabarray[3] = (ImageButton) findViewById(R.id.tab_btn_4);
        tabarray[4] = (ImageButton) findViewById(R.id.tab_btn_5);
        for (int i = 0; i <= 4; i++) {
            tabarray[i].setOnClickListener(this);
        }
        tabarray[0].setImageResource(R.drawable.main_tab_btn_drawable_blue_1);
        prevBtnNum = 0;
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setTabBtnClick(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    public void setTabBtnClick(int initBtnNum){
        if(prevBtnNum!=initBtnNum){
            tabarray[prevBtnNum].setImageResource(R.drawable.main_tab_btn_drawable_gray_1+(prevBtnNum));
            tabarray[initBtnNum].setImageResource(R.drawable.main_tab_btn_drawable_blue_1+(initBtnNum));
            prevBtnNum=initBtnNum;
        }
        else{
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_btn_1:
                pager.setCurrentItem(0);
                setTabBtnClick(0);
                break;
            case R.id.tab_btn_2:
                pager.setCurrentItem(1);
                setTabBtnClick(1);
                break;
            case R.id.tab_btn_3:
                pager.setCurrentItem(2);
                setTabBtnClick(2);
                break;
            case R.id.tab_btn_4:
                pager.setCurrentItem(3);
                setTabBtnClick(3);
                break;
            case R.id.tab_btn_5:
                pager.setCurrentItem(4);
                setTabBtnClick(4);
                break;
        }
    }
    private static class MainSlideAdapter extends FragmentPagerAdapter{
        public MainSlideAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public int getCount() {
            return 5;
        }
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return Main_FavoriteFragment.newInstance(position);
                case 1:
                    return Main_BusRouteSearchFragment.newInstance(position);
                case 2:
                    return Main_BusStopSearchFragment.newInstance(position);
                case 3:
                    return Main_ExploreFragment.newInstance(position);
                case 4:
                    return Main_FavoriteFragment.newInstance(position);
                default:
                    return null;
            }
        }
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
}
