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
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
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

public class TrafficFragment2 extends Fragment {
    private JSONArray mJSONArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_traffic_2, container, false);

        SejongBisClient bisClient = new SejongBisClient(getActivity());
        if (bisClient.isNetworkConnected()) {
            ArrayList<Spanned> list = new ArrayList<>();
            try {
                mJSONArray = bisClient.searchBusStopRoute(getArguments().getInt("arg1"), true)
                        .getJSONArray("busStopRouteList");

                TextView textView1 = (TextView) rootView.findViewById(R.id.textView1);
                TextView textView2 = (TextView) rootView.findViewById(R.id.textView2);
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

            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            listView.setAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, list));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment fragment = new TrafficFragment1();
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putInt("arg1",
                                mJSONArray.getJSONObject(position).getInt("route_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }

        return rootView;
    }
}
