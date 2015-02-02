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

public class MainFragment2 extends Fragment {
    private EditText mEditText1;
    private EditText mEditText2;

    private SejongBisClient mBisClient;
    private JSONArray mJSONArray;
    private ArrayList<Spanned> mList = new ArrayList<>();
    private ArrayAdapter<Spanned> mAdapter;
    private ListView mListView;
    // TODO
    private int stBusStop = 293018070;
    private int edBusStop = 293018069;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBisClient = new SejongBisClient(getActivity());
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_main_2, container, false);

        mEditText1 = (EditText) rootView.findViewById(R.id.editText1);
        mEditText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();

                    stBusStop = 0;
                    mEditText1.setHint("");
                    mEditText1.setText("");
                    return true;
                }
                return false;
            }
        });
        mEditText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearch1();
                    return true;
                }
                return false;
            }
        });

        mEditText2 = (EditText) rootView.findViewById(R.id.editText2);
        mEditText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mList.clear();
                    mAdapter.notifyDataSetChanged();

                    edBusStop = 0;
                    mEditText2.setHint("");
                    mEditText2.setText("");
                    return true;
                }
                return false;
            }
        });
        mEditText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        ImageButton imageButton1 = (ImageButton) rootView.findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch1();
            }
        });

        ImageButton imageButton2 = (ImageButton) rootView.findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch2();
            }
        });

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stBusStop == 0) {
                    Toast.makeText(getActivity(), "출발할 정류소를 검색하세요.", Toast.LENGTH_SHORT)
                            .show();
                } else if (edBusStop == 0) {
                    Toast.makeText(getActivity(), "도착할 정류소를 검색하세요.", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    MainActivity.startHostActivity(TrafficActivity.ROUTE_EXPLORE, stBusStop,
                            edBusStop);
                }
            }
        });

        return rootView;
    }

    private void onSearch1() {
        String query = mEditText1.getText().toString();
        if (!query.equals("")) {
            MainActivity.hideSoftInput();

            if (mBisClient.isNetworkConnected()) {
                searchBusStop(query);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            JSONObject json = mJSONArray.getJSONObject(position);
                            stBusStop = json.getInt("stop_id");
                            mEditText1.setHint(json.getString("stop_name") + "("
                                    + json.getString("service_id") + ")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mEditText1.setText("");
                    }
                });
            }
        }
    }

    private void onSearch2() {
        String query = mEditText2.getText().toString();
        if (!query.equals("")) {
            MainActivity.hideSoftInput();

            if (mBisClient.isNetworkConnected()) {
                searchBusStop(query);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            JSONObject json = mJSONArray.getJSONObject(position);
                            edBusStop = json.getInt("stop_id");
                            mEditText2.setHint(json.getString("stop_name") + "("
                                    + json.getString("service_id") + ")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mEditText2.setText("");
                    }
                });
            }
        }
    }

    private void searchBusStop(String busStop) {
        try {
            mJSONArray = mBisClient.searchBusStop(busStop, true).getJSONArray("busStopList");
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
    }
}
