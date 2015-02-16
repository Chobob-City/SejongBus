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
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import kr.nogcha.sejongbus.R;
import kr.nogcha.sejongbus.RouteExploreActivity;
import kr.nogcha.sejongbus.SejongBisClient;

public class ExploreFragment extends Fragment {
    public static int stBusStop = 0;
    public static int edBusStop = 0;
    private EditText mEditText1;
    private EditText mEditText2;
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
        View rootView = inflater.inflate(R.layout.f_explore, container, false);

        ImageButton imageButton1 = (ImageButton) rootView.findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new ExploreFragment1()).commit();
            }
        });

        ImageButton imageButton2 = (ImageButton) rootView.findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new ExploreFragment2()).commit();
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
                    Intent intent = new Intent(getActivity(), RouteExploreActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("stBusStop", stBusStop);
                    bundle.putInt("edBusStop", edBusStop);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }
}
