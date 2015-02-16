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

package kr.nogcha.sejongbus.main;

import android.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import java.util.regex.Pattern;

import kr.nogcha.sejongbus.CommonArrayAdapter;
import kr.nogcha.sejongbus.CommonListItem;
import kr.nogcha.sejongbus.MainActivity;
import kr.nogcha.sejongbus.R;
import kr.nogcha.sejongbus.SejongBisClient;
import kr.nogcha.sejongbus.HostActivity;

public class SearchFragment extends Fragment {
    private EditText mEditText;
    private ListView mListView;
    private SejongBisClient mBisClient;
    private JSONArray mJSONArray;
    private ArrayList<CommonListItem> mList;
    private CommonArrayAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBisClient = new SejongBisClient(getActivity());
        mList = new ArrayList<>();
        mAdapter = new CommonArrayAdapter(getActivity(), R.layout.common_list_item, mList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_search, container, false);

        mEditText = (EditText) rootView.findViewById(R.id.editText);
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mEditText.setText("");
                    return true;
                }
                return false;
            }
        });
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearch();
                    return true;
                }
                return false;
            }
        });

        mListView = (ListView) rootView.findViewById(R.id.listView);
        mListView.setEmptyView(rootView.findViewById(R.id.textView));
        mListView.setAdapter(mAdapter);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch();
            }
        });

        return rootView;
    }

    private void onSearch() {
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
                JSONObject json = mJSONArray.getJSONObject(i);
                mList.add(new CommonListItem((Spanned) TextUtils.concat(mBisClient.getRouteType(json.getInt("route_type")), new SpannableString(" " + json.getString("route_name"))), json.getString("st_stop_name") + "~" + json.getString("ed_stop_name")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    MainActivity.startHostActivity(HostActivity.BUS_ROUTE_DETAIL,
                            mJSONArray.getJSONObject(position).getInt("route_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void searchBusStop(String busStop) {
        try {
            mJSONArray = mBisClient.searchBusStop(busStop, true).getJSONArray("busStopList");
            mList.clear();
            for (int i = 0; i < mJSONArray.length(); i++) {
                JSONObject json = mJSONArray.getJSONObject(i);
                mList.add(new CommonListItem(new SpannableString(json.getString("stop_name")), "[" + json.getString("service_id") + "]"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    MainActivity.startHostActivity(HostActivity.BUS_STOP_ROUTE,
                            mJSONArray.getJSONObject(position).getInt("stop_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
