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
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
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

public class SearchFragment extends Fragment {
    private List<CommonListItem> mList = new ArrayList<>();
    private SejongBisClient mBisClient;
    private CommonAdapter mAdapter;
    private EditText mEditText;
    private ListView mListView;
    private JSONArray mJSONArray;

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

        mEditText = (EditText) rootView.findViewById(R.id.edit_text);
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) mEditText.setText("");
            }
        });
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });

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
                if (Pattern.matches("^[0-9-]+$", query)) {
                    searchBusRoute(query);
                } else {
                    searchBusStop(query);
                }
            }
        }
    }

    private void searchBusRoute(String busRoute) {
        try {
            mJSONArray = mBisClient.searchBusRoute(busRoute, true).getJSONArray("busRouteList");
            mList.clear();
            for (int i = 0; i < mJSONArray.length(); i++) {
                CommonListItem item = new CommonListItem();
                JSONObject json = mJSONArray.getJSONObject(i);
                item.text1 = mBisClient.getRouteType(json.getInt("route_type"));
                item.text2 = json.getString("route_name");
                item.text3 = json.getString("st_stop_name") + "~" + json.getString("ed_stop_name");
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

    private void searchBusStop(String busStop) {
        try {
            mJSONArray = mBisClient.searchBusStop(busStop, true).getJSONArray("busStopList");
            mList.clear();
            for (int i = 0; i < mJSONArray.length(); i++) {
                CommonListItem item = new CommonListItem();
                JSONObject json = mJSONArray.getJSONObject(i);
                item.text1 = new SpannableString("");
                item.text2 = json.getString("stop_name");
                item.text3 = json.getString("service_id");
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
                    extras.putInt("stop_id", mJSONArray.getJSONObject(position).getInt("stop_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
}
