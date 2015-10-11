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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private static MainActivity sInstance;
    private ImageButton tab_btn_1,tab_btn_2,tab_btn_3,tab_btn_4,tab_btn_5,prevView;
    private Animation fadeinAnim, fadeoutAnim;
    private ViewPager pager;
    private Toolbar toolbar;

    private int prevViewNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        pager = (ViewPager)findViewById(R.id.main_viewpager);
        pager.setAdapter(new MainSlideAdapter(getSupportFragmentManager()));

        tab_btn_1=(ImageButton)findViewById(R.id.main_tab_btn1);
        tab_btn_2=(ImageButton)findViewById(R.id.main_tab_btn2);
        tab_btn_3=(ImageButton)findViewById(R.id.main_tab_btn3);
        tab_btn_4=(ImageButton)findViewById(R.id.main_tab_btn4);
        tab_btn_5=(ImageButton)findViewById(R.id.main_tab_btn5);
        tab_btn_1.setOnClickListener(this);
        tab_btn_2.setOnClickListener(this);
        tab_btn_3.setOnClickListener(this);
        tab_btn_4.setOnClickListener(this);
        tab_btn_5.setOnClickListener(this);
        prevView = tab_btn_1;
        tab_btn_1.setImageResource(R.drawable.main_tab_btn_drawable_brightblue_1);
        prevViewNum = 1;

        fadeinAnim = AnimationUtils.loadAnimation(this,R.anim.fade_in_anim);
        fadeoutAnim = AnimationUtils.loadAnimation(this,R.anim.fade_out_anim);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    TabChange(tab_btn_1,1);
                }else if(position==1){
                    TabChange(tab_btn_2,2);
                }else if(position==2){
                    TabChange(tab_btn_3,3);
                }else if(position==3){
                    TabChange(tab_btn_4,4);
                }else if(position==4){
                    TabChange(tab_btn_5,5);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void TabChange(ImageButton initView, int initViewNum){
        if(prevViewNum!=initViewNum){
            pager.setCurrentItem(initViewNum-1);
            prevView.setImageResource(R.drawable.main_tab_btn_drawable_gray_1 + (prevViewNum - 1));
            initView.setImageResource(R.drawable.main_tab_btn_drawable_brightblue_1+(initViewNum-1));
            prevView=initView;
            prevViewNum=initViewNum;
        }
    }
    public static class MainSlideAdapter extends FragmentPagerAdapter {

        public MainSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Parcelable saveState() {
            return super.saveState();
        }

        @Override
        public int getCount() {
            return 5;
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FavoriteFramgent.newInstance(position);
                case 1:
                    return BusRouteFragment.newInstance(position);
                case 2:
                    return BusStopFragment.newInstance(position);
                case 3:
                    return ExploreFragment.newInstance(position);
                case 4:
                    return SurroundStopFragment.newInstance(position);
                default:
                    return null;
            }
        }
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.main_tab_btn1:
                TabChange(tab_btn_1, 1);
                break;
            case R.id.main_tab_btn2:
                TabChange(tab_btn_2, 2);
                break;
            case R.id.main_tab_btn3:
                TabChange(tab_btn_3, 3);
                break;
            case R.id.main_tab_btn4:
                TabChange(tab_btn_4, 4);
                break;
            case R.id.main_tab_btn5:
                TabChange(tab_btn_5, 5);
                break;

        }
    }
    public static void hideSoftInput() {
        final InputMethodManager inputMethodManager =
                (InputMethodManager) sInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
