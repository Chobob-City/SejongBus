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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusRouteDetailActivity extends ActionBarActivity {
    private List<CommonListItem> mList = new ArrayList<>();
    private List<Integer> mStopIdList = new ArrayList<>();
    private SejongBisClient mBisClient;
    private int mRouteId;
    private JSONArray mJSONArray;
    private CommonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_busroute_detail);

        mBisClient = new SejongBisClient(this);
        if (!mBisClient.isNetworkConnected()) return;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRouteId = getIntent().getExtras().getInt("route_id");
        try {
            mJSONArray = mBisClient.searchBusRouteDetail(mRouteId, true)
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
                item.resId = R.drawable.l_busstopicon;
                item.text1 = json.getString("stop_name");
                item.text2 = json.getString("service_id");
                mList.add(item);
                mStopIdList.add(json.getInt("stop_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter = new CommonAdapter(this, R.layout.s_common_list_item, mList);
        onRefresh();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusRouteDetailActivity.this, BusTimeListActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("route_id", mRouteId);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BusRouteDetailActivity.this, BusStopRouteActivity.class);
                Bundle extras = new Bundle();
                try {
                    extras.putInt("stop_id", mJSONArray.getJSONObject(position).getInt("stop_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    private void onRefresh() {
        if (!mBisClient.isNetworkConnected()) return;

        for (int i = 0; i < mList.size(); i++) {
            CommonListItem item = mList.get(i);
            item.isBusHere = false;
            mList.set(i, item);
        }

        try {
            JSONArray jsonArray = mBisClient.searchBusRealLocationDetail(mRouteId)
                    .getJSONArray("busRealLocList");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                int location = mStopIdList.indexOf(json.getInt("stop_id"));
                CommonListItem item = mList.get(location);
                item.isBusHere = true;
                mList.set(location, item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }
}
