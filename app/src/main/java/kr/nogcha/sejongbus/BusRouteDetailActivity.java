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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusRouteDetailActivity extends ActionBarActivity {
    private JSONArray mJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_bus_route_detail);

        SejongBisClient bisClient = new SejongBisClient(this);
        if (!bisClient.isNetworkConnected()) return;

        List<CommonListItem> list = new ArrayList<>();
        try {
            mJSONArray = bisClient
                    .searchBusRouteDetail(getIntent().getExtras().getInt("route_id"), true)
                    .getJSONArray("busRouteDetailList");

            TextView textView1 = (TextView) findViewById(R.id.text_view_1);
            TextView textView2 = (TextView) findViewById(R.id.text_view_2);
            TextView textView3 = (TextView) findViewById(R.id.text_view_3);
            JSONObject json = mJSONArray.getJSONObject(mJSONArray.length() - 1);
            textView1.setText(json.getString("route_name"));
            textView2.setText(json.getString("st_stop_name") + "~"
                    + json.getString("ed_stop_name"));
            textView3.setText(json.getString("alloc_time"));

            for (int i = 0; i < mJSONArray.length() - 1; i++) {
                CommonListItem item = new CommonListItem();
                json = mJSONArray.getJSONObject(i);
                item.text1 = new SpannableString("");
                item.text2 = json.getString("stop_name");
                item.text3 = json.getString("service_id");
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new CommonAdapter(this, R.layout.common_list_item, list));
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
