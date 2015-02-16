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
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.nogcha.sejongbus.MainActivity;
import kr.nogcha.sejongbus.R;
import kr.nogcha.sejongbus.RouteExploreActivity;
import kr.nogcha.sejongbus.SejongBisClient;

public class ExploreFragment2 extends Fragment {
    private EditText mEditText;
    private SejongBisClient mBisClient;
    private JSONArray mJSONArray;
    private ArrayList<Spanned> mList = new ArrayList<>();
    private ArrayAdapter<Spanned> mAdapter;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBisClient = new SejongBisClient(getActivity());
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_explore_2, container, false);

        mEditText = (EditText) rootView.findViewById(R.id.editText);
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();

                    mEditText.setHint("");
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
                    onSearch2();
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
                onSearch2();
            }
        });

        return rootView;
    }

    private void onSearch2() {
        String query = mEditText.getText().toString();
        if (!query.equals("")) {
            MainActivity.hideSoftInput();

            if (mBisClient.isNetworkConnected()) {
                try {
                    mJSONArray = mBisClient.searchBusStop(query, true).getJSONArray("busStopList");
                    mList.clear();
                    for (int i = 0; i < mJSONArray.length(); i++) {
                        JSONObject json = mJSONArray.getJSONObject(i);
                        mList.add(new SpannableString(json.getString("stop_name") + "\n("
                                + json.getString("service_id") + ")"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            JSONObject json = mJSONArray.getJSONObject(position);
                            ExploreFragment.edBusStop = json.getInt("stop_id");
                            getFragmentManager().popBackStack();
                            mEditText.setHint(json.getString("stop_name") + "("
                                    + json.getString("service_id") + ")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mEditText.setText("");
                    }
                });
            }
        }
    }
}
