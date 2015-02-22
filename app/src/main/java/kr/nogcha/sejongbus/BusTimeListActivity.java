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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusTimeListActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_bus_time_list);

        SejongBisClient bisClient = new SejongBisClient(this);
        if (!bisClient.isNetworkConnected()) return;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<String> list = new ArrayList<>();
        try {
            JSONArray jsonArray = bisClient
                    .searchBusTimeList(getIntent().getExtras().getInt("route_id"))
                    .getJSONArray("busTimeList");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                list.add(formatTime(json.getString("departure_time").trim()));
                list.add(formatTime(json.getString("arrival_time").trim()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setAdapter(new ArrayAdapter<>(this, R.layout.simple_list_item, list));
    }

    private String formatTime(String time) {
        if (!time.equals("-")) {
            if (time.length() == 3) time = "0" + time;
            return new StringBuilder(time).insert(time.length() - 2, ":").toString();
        } else {
            return time;
        }
    }
}
