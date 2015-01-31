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

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

public class TrafficActivity extends ActionBarActivity {
    public static final int BUS_ROUTE = 0;
    public static final int BUS_STOP = 1;
    public static final int ROUTE_EXPLORE = 2;

    public static Spannable getRouteType(int route_type) {
        Spannable routeType;
        int backgroundColor;
        switch (route_type) {
            case 43:
                routeType = new SpannableString("세종광역");
                backgroundColor = Color.RED;
                break;
            case 50:
                routeType = new SpannableString("대전광역");
                backgroundColor = Color.RED;
                break;
            case 51:
                routeType = new SpannableString("청주광역");
                backgroundColor = Color.RED;
                break;
            case 30:
                routeType = new SpannableString("마을");
                backgroundColor = Color.GREEN;
                break;
            default:
                routeType = new SpannableString("일반");
                backgroundColor = Color.BLUE;
        }
        routeType.setSpan(new ForegroundColorSpan(Color.WHITE), 0, routeType.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        routeType.setSpan(new BackgroundColorSpan(backgroundColor), 0, routeType.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return routeType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_host);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Fragment fragment;
        Bundle extras = getIntent().getExtras();
        Bundle bundle;
        switch (extras.getInt("arg0")) {
            case BUS_ROUTE:
                fragment = new TrafficFragment1();
                bundle = new Bundle();
                bundle.putInt("busRouteId", extras.getInt("arg1"));
                break;
            case BUS_STOP:
                fragment = new TrafficFragment2();
                bundle = new Bundle();
                bundle.putInt("busStopId", extras.getInt("arg1"));
                break;
            case ROUTE_EXPLORE:
                fragment = new TrafficFragment3();
                bundle = new Bundle();
                bundle.putInt("stBusStop", extras.getInt("arg1"));
                bundle.putInt("edBusStop", extras.getInt("arg2"));
                break;
            default:
                return;
        }
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_host, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
