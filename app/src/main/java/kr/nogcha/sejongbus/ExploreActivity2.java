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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
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

public class ExploreActivity2 extends ActionBarActivity {
    private List<CommonListItem> mList = new ArrayList<>();
    private SejongBisClient mBisClient;
    private CommonAdapter mAdapter;
    private EditText mEditText;
    private ListView mListView;
    private JSONArray mJSONArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_explore_2);

        mBisClient = new SejongBisClient(this);
        mAdapter = new CommonAdapter(this, R.layout.common_list_item, mList);

        mEditText = (EditText) findViewById(R.id.edit_text);
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
                    onSearch();
                    return true;
                }
                return false;
            }
        });
        mEditText.requestFocus();

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setEmptyView(findViewById(R.id.text_view));
        mListView.setAdapter(mAdapter);

        ImageButton imageButton = (ImageButton) findViewById(R.id.image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch();
            }
        });
    }

    private void onSearch() {
        String query = mEditText.getText().toString();
        if (!query.equals("")) {
            MainActivity.hideSoftInput();
            if (!mBisClient.isNetworkConnected()) return;

            try {
                mJSONArray = mBisClient.searchBusStop(query, true).getJSONArray("busStopList");
                mList.clear();
                for (int i = 0; i < mJSONArray.length(); i++) {
                    CommonListItem item = new CommonListItem();
                    JSONObject json = mJSONArray.getJSONObject(i);
                    item.image = getResources().getDrawable(R.drawable.busstopicon);
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
                    try {
                        ExploreFragment.edBusStop = mJSONArray.getJSONObject(position)
                                .getInt("stop_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            });
        }
    }
}
