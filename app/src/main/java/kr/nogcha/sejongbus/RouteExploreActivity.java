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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.nogcha.sejongbus.R;
import kr.nogcha.sejongbus.SejongBisClient;

public class RouteExploreActivity extends ActionBarActivity {
    private JSONArray mJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_route_explore);

        SejongBisClient bisClient = new SejongBisClient(this);
        Bundle extras = getIntent().getExtras();
        try {
            mJSONArray = bisClient
                    .searchRouteExplore(extras.getInt("stBusStop"), extras.getInt("edBusStop"), true)
                    .getJSONArray("routeExplore");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> list = new ArrayList<>();
        try {
            TextView textView1 = (TextView) findViewById(R.id.text_view_1);
            JSONObject json = mJSONArray.getJSONObject(0);
            textView1.setText("출발: " + json.getString("sstationname") + "("
                    + json.getString("sService_id") + ")\n도착: "
                    + json.getString("estationname") + "(" + json.getString("eService_id")
                    + ")");

            for (int i = 0; i < mJSONArray.length(); i++) {
                json = mJSONArray.getJSONObject(i);
                String route;

                int xtype = json.getInt("xtype");
                if (xtype == 1) {
                    route = "무";
                } else {
                    route = "";
                }
                route += "환승 경로\n" + json.getString("sstationname") + "(" +
                        json.getString("sService_id") + ")에서 " + json.getString("srouteno")
                        + "번 버스에 승차\n";
                if (xtype != 1) {
                    route += json.getString("tstationname") + "("
                            + json.getString("tService_id") + ")에서 "
                            + json.getString("erouteno") + "번 버스로 환승\n";
                }
                route += json.getString("estationname") + "(" + json.getString("eService_id")
                        + ")에서 하차\n" + json.getString("seq") + "개 정류소, "
                        + json.getInt("distance") / 1000. + "km";

                list.add(route);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
    }
}
