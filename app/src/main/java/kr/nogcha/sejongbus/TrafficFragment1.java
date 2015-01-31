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
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrafficFragment1 extends Fragment {
    private SejongBisClient bisClient;
    private JSONArray jsonArray;

    public TrafficFragment1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bisClient = new SejongBisClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_traffic_1, container, false);

        ArrayList<String> list = new ArrayList<>();
        try {
            jsonArray = bisClient.searchBusRouteDetail(getArguments().getInt("arg1"), true)
                    .getJSONArray("busRouteDetailList");

            TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);
            TextView textView2 = (TextView) rootView.findViewById(R.id.textView2);
            TextView textView3 = (TextView) rootView.findViewById(R.id.textView3);
            JSONObject json = jsonArray.getJSONObject(jsonArray.length() - 1);
            textView1.setText(json.getString("route_name"));
            textView2.setText(json.getString("st_stop_name") + "~" +
                    json.getString("ed_stop_name"));
            textView3.setText(json.getString("alloc_time"));

            for (int i = 0; i < jsonArray.length() - 1; i++) {
                json = jsonArray.getJSONObject(i);
                list.add(json.getString("stop_name") + "\n(" +
                        json.getString("service_id") + ")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment busStopFragment = new TrafficFragment2();
                Bundle bundle = new Bundle();
                try {
                    bundle.putInt("arg1", jsonArray.getJSONObject(position)
                            .getInt("stop_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                busStopFragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, busStopFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }
}
