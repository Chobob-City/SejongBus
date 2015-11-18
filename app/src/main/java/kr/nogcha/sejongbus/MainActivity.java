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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static MainActivity sInstance;
    private TabLayout tablayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    public FloatingActionButton floatingBtn;
    public EditText editText;
    public String getbusString;
    Main_BusNumSearchFragment busNumSearchFragment;
    Main_BusStopSearchFragment busStopSearchFragment;
    Main_FavoriteFragment favoriteFragment;
    Main_ExploreFragment exploreFragment;
    Bundle busStringBundle;

    public static void hideSoftInput() {
        final InputMethodManager imm = (InputMethodManager) sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public static void showSoftInput() {
        final InputMethodManager imm = (InputMethodManager) sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        busStringBundle = new Bundle();

        sInstance = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(
                new MainActivityTabAdapter(getSupportFragmentManager(), this));
        viewPager.setOffscreenPageLimit(1);

        tablayout = (TabLayout) findViewById(R.id.tablayout);
        tablayout.setupWithViewPager(viewPager);

        floatingBtn = (FloatingActionButton) findViewById(R.id.floatingbtn);
        floatingBtn.setOnClickListener(this);
        floatingBtn.hide();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == 3) {
                    floatingBtn.hide();
                    if(editText.getVisibility()==View.VISIBLE){
                        editText.setVisibility(View.GONE);
                        hideSoftInput();
                    }
                } else {
                    floatingBtn.show();
                    if(editText.getVisibility()==View.VISIBLE){
                        editText.setVisibility(View.GONE);
                    }
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        editText = (EditText) findViewById(R.id.edit_text);
        if(editText.getVisibility()==View.VISIBLE){
            busStringBundle.putBoolean("edittextBoolean", true);//true 일때 visible
        }
        else{
            busStringBundle.putBoolean("edittextBoolean", false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.floatingbtn:
                if(editText.getVisibility()==View.GONE) {
                    editText.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                    showSoftInput();
                }
                else{
                    editText.setVisibility(View.GONE);
                }
                if (viewPager.getCurrentItem() == 1) {
                    editText.setHint("버스노선을 입력하세요.");
                    busStringBundle.putString("busNum", editText.getText().toString());
                    busNumSearchFragment.setArguments(busStringBundle);
                } else if (viewPager.getCurrentItem() == 2) {
                    editText.setHint("정류장이름 또는 번호을 입력하세요.");
                    busStringBundle.putString("busStop", editText.getText().toString());
                    busStopSearchFragment.setArguments(busStringBundle);
                }
                break;
        }
    }
    public class MainActivityTabAdapter extends FragmentPagerAdapter{
        final int PAGE_COUNT = 4;
        private String[] tabTitles = {"즐겨찾기","노선","정류장","경로탐색"};
        private Context context;

        public MainActivityTabAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return favoriteFragment.newInstance(position);
            }
            else if(position==1){
                return busNumSearchFragment.newInstance(position);
            }
            else if(position==2){
                return busStopSearchFragment.newInstance(position);
            }
            else if(position==3){
                return exploreFragment.newInstance(position);
            }
            else{
                return null;
            }
        }
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
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
