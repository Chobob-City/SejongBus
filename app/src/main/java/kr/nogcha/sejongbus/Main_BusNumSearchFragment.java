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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Main_BusNumSearchFragment extends Fragment {
    private List<CommonListItem> mList = new ArrayList<>();
    private SejongBisClient mBisClient;
    private CommonAdapter mAdapter;
    private EditText mEditText;
    private ListView mListView;
    private JSONArray mJSONArray;

    public static Main_BusNumSearchFragment newInstance(int num) {
        Main_BusNumSearchFragment f = new Main_BusNumSearchFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        mBisClient = new SejongBisClient(activity);
        mAdapter = new CommonAdapter(activity, R.layout.common_list_item, mList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_search, container, false);

        Bundle busString = getArguments();
        if(busString.getBoolean("edittextBoolean")==true) {
            search(busString.getString("busNum"));
        }
        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mListView.setEmptyView(rootView.findViewById(R.id.text_view));
        mListView.setAdapter(mAdapter);

        return rootView;
    }

    private void search() {
        String query = mEditText.getText().toString();
        if (!query.equals("")) {
            MainActivity.hideSoftInput();
            if (mBisClient.isNetworkConnected()) {
                searchBusRoute(query);
            }
        }
    }

    private void searchBusRoute(String busRoute) {
        try {
            mList.clear();
            mJSONArray = mBisClient.searchBusRoute(busRoute, true).getJSONArray("busRouteList");
            for (int i = 0; i < mJSONArray.length(); i++) {
                CommonListItem item = new CommonListItem();
                JSONObject json = mJSONArray.getJSONObject(i);

                switch (json.getInt("route_type")) {
                    case 30:
                        item.resId = R.drawable.bus_town;
                        break;
                    case 43:
                        item.resId = R.drawable.bus_sejong;
                        break;
                    case 50:
                        item.resId = R.drawable.bus_daejeon;
                        break;
                    case 51:
                        item.resId = R.drawable.bus_cheongju;
                        break;
                    default:
                        item.resId = R.drawable.bus_general;
                }

                item.text1 = json.getString("route_name");
                item.text2 = json.getString("st_stop_name") + "~" + json.getString("ed_stop_name");
                mList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BusRouteDetailActivity.class);
                Bundle extras = new Bundle();
                try {
                    extras.putInt("route_id",
                            mJSONArray.getJSONObject(position).getInt("route_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
}
