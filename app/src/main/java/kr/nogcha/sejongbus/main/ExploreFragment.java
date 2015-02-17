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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import kr.nogcha.sejongbus.R;
import kr.nogcha.sejongbus.RouteExploreActivity;

public class ExploreFragment extends Fragment {
    public static int stBusStop = 0;
    public static int edBusStop = 0;
    private FragmentManager mFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_explore, container, false);

        mFragmentManager = getFragmentManager();

        final EditText editText1 = (EditText) rootView.findViewById(R.id.edit_text_1);
        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onSearch(false);
                    return true;
                }
                return false;
            }
        });

        ImageButton imageButton1 = (ImageButton) rootView.findViewById(R.id.image_button_1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch(false);
            }
        });

        final EditText editText2 = (EditText) rootView.findViewById(R.id.edit_text_2);
        editText2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    onSearch(true);
                    return true;
                }
                return false;
            }
        });

        ImageButton imageButton2 = (ImageButton) rootView.findViewById(R.id.image_button_2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch(true);
            }
        });

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stBusStop == 0) {
                    Toast.makeText(getActivity(), "출발지를 선택하세요.", Toast.LENGTH_SHORT)
                            .show();
                } else if (edBusStop == 0) {
                    Toast.makeText(getActivity(), "도착지를 선택하세요.", Toast.LENGTH_SHORT)
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

    private void onSearch(boolean isSecond) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout,
                !isSecond ? new ExploreFragment1() : new ExploreFragment2());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
