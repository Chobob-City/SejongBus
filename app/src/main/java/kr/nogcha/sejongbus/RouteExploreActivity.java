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
import android.text.SpannableString;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RouteExploreActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_route_explore);

        SejongBisClient bisClient = new SejongBisClient(this);
        Bundle extras = getIntent().getExtras();
        List<CommonListItem> list = new ArrayList<>();
        try {
            JSONArray jsonArray = bisClient
                    .searchRouteExplore(extras.getInt("stBusStop"), extras.getInt("edBusStop"),
                            true)
                    .getJSONArray("routeExplore");

            TextView textView = (TextView) findViewById(R.id.text_view);
            JSONObject json = jsonArray.getJSONObject(0);
            textView.setText("출발: " + json.getString("sstationname") + " ["
                    + json.getString("sService_id") + "]\n도착: " + json.getString("estationname")
                    + " [" + json.getString("eService_id") + "]");

            for (int i = 0; i < jsonArray.length(); i++) {
                CommonListItem item = new CommonListItem();
                json = jsonArray.getJSONObject(i);
                item.text1 = new SpannableString("");

                int xtype = json.getInt("xtype");
                if (xtype == 1) {
                    item.text2 = "무";
                } else {
                    item.text2 = "";
                }
                item.text2 += "환승 경로";
                item.text3 = json.getString("sstationname") + " [" + json.getString("sService_id")
                        + "]에서 " + json.getString("srouteno") + "번 버스에 승차\n";
                if (xtype != 1) {
                    item.text3 += json.getString("tstationname") + " ["
                            + json.getString("tService_id") + "]에서 " + json.getString("erouteno")
                            + "번 버스로 환승\n";
                }
                item.text3 += json.getString("estationname") + " [" + json.getString("eService_id")
                        + "]에서 하차\n" + json.getString("seq") + "개 정류소, "
                        + json.getInt("distance") / 1000.0 + "km";

                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new CommonAdapter(this, R.layout.common_list_item, list));
    }
}
