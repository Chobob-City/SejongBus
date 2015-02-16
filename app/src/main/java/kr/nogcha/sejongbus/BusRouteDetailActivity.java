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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusRouteDetailActivity extends ActionBarActivity {
    private JSONArray mJSONArray;
    private CommonArrayAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_bus_route_detail);

        SejongBisClient bisClient = new SejongBisClient(this);
        if (bisClient.isNetworkConnected()) {
            ArrayList<CommonListItem> list = new ArrayList<>();
            try {
                mJSONArray = bisClient.searchBusRouteDetail(getIntent().getExtras().getInt("route_id"), true)
                        .getJSONArray("busRouteDetailList");
                for (int i = 0; i < mJSONArray.length() - 1; i++) {
                    JSONObject json = mJSONArray.getJSONObject(i);
                    list.add(new CommonListItem(
                            new SpannableString(json.getString("stop_name")),
                            "[" + json.getString("service_id") + "]"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter = new CommonArrayAdapter(this, R.layout.common_list_item, list);
        }

        if (mAdapter != null) {
            TextView textView1 = (TextView) findViewById(R.id.text_view_1);
            TextView textView2 = (TextView) findViewById(R.id.text_view_2);
            TextView textView3 = (TextView) findViewById(R.id.text_view_3);
            try {
                JSONObject json = mJSONArray.getJSONObject(mJSONArray.length() - 1);
                textView1.setText(json.getString("route_name"));
                textView2.setText(json.getString("st_stop_name") + "~"
                        + json.getString("ed_stop_name"));
                textView3.setText(json.getString("alloc_time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(BusRouteDetailActivity.this, BusStopRouteActivity.class);
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putInt("stop_id", mJSONArray.getJSONObject(position).getInt("stop_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_traffic, menu);
        return true;
    }
}
