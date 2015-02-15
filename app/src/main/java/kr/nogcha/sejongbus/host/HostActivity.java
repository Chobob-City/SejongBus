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

package kr.nogcha.sejongbus.host;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import kr.nogcha.sejongbus.R;
import kr.nogcha.sejongbus.main.MainActivity;

public class HostActivity extends ActionBarActivity {
    public static final int BUS_ROUTE_DETAIL = 1;
    public static final int BUS_STOP_ROUTE = 2;
    public static final int ROUTE_EXPLORE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_traffic);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Fragment fragment;
        Bundle extras = getIntent().getExtras();
        switch (extras.getInt("arg0")) {
            case BUS_ROUTE_DETAIL:
                fragment = new BusRouteDetailFragment();
                break;
            case BUS_STOP_ROUTE:
                fragment = new BusStopRouteFragment();
                break;
            case ROUTE_EXPLORE:
                fragment = new RouteExploreFragment();
                break;
            default:
                return;
        }
        fragment.setArguments(extras);
        getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_traffic, menu);
        return true;
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
