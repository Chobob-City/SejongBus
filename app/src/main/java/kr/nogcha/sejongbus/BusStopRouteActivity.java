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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusStopRouteActivity extends ActionBarActivity {
    private JSONArray mJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_bus_route_detail);

        SejongBisClient bisClient = new SejongBisClient(this);
        if (bisClient.isNetworkConnected()) {
            ArrayList<Spanned> list = new ArrayList<>();
            try {
                mJSONArray = bisClient.searchBusStopRoute(getIntent().getExtras().getInt("stop_id"), true)
                        .getJSONArray("busStopRouteList");

                TextView textView1 = (TextView) findViewById(R.id.text_view_1);
                TextView textView2 = (TextView) findViewById(R.id.text_view_2);
                JSONObject json = mJSONArray.getJSONObject(0);
                textView1.setText(json.getString("stop_name"));
                textView2.setText("[" + json.getString("service_id") + "]");

                for (int i = 0; i < mJSONArray.length(); i++) {
                    json = mJSONArray.getJSONObject(i);

                    Spanned route = (Spanned) TextUtils.concat(
                            bisClient.getRouteType(json.getInt("route_type")),
                            new SpannableString(" " + json.getString("route_name") + "\n"));

                    int provide_code = json.getInt("provide_code");
                    switch (provide_code) {
                        case 1:
                            route = (Spanned) TextUtils.concat(route, new SpannableString("도착: "
                                    + json.getString("provide_type") + "\n현위치: 기점"));
                            break;
                        case 2:
                            route = (Spanned) TextUtils.concat(route,
                                    new SpannableString("회차지 대기 중"));
                            break;
                        default:
                            route = (Spanned) TextUtils.concat(route, new SpannableString("도착: "
                                    + json.getString("provide_type") + "\n현위치: "
                                    + json.getString("rstop")));
                    }

                    list.add(route);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, list));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(BusStopRouteActivity.this, BusRouteDetailActivity.class);
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putInt("route_id",
                                mJSONArray.getJSONObject(position).getInt("route_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}
