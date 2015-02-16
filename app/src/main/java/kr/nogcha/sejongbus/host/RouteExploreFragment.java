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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.nogcha.sejongbus.R;
import kr.nogcha.sejongbus.SejongBisClient;

public class RouteExploreFragment extends Fragment {
    private JSONArray mJSONArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SejongBisClient bisClient = new SejongBisClient(getActivity());
        Bundle arguments = getArguments();
        try {
            mJSONArray = bisClient
                    .searchRouteExplore(arguments.getInt("arg1"), arguments.getInt("arg2"), true)
                    .getJSONArray("routeExplore");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.a_route_explore, container, false);

        ArrayList<String> list = new ArrayList<>();
        try {
            TextView textView1 = (TextView) rootView.findViewById(R.id.text_view_1);
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

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, list));

        return rootView;
    }
}
