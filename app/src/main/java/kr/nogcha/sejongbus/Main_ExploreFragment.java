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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Main_ExploreFragment extends Fragment {
    private static int sStBusStop = 0;
    private static int sEdBusStop = 0;
    private static EditText sEditText1;
    private static EditText sEditText2;

    public static Main_ExploreFragment newInstance(int num) {
        Main_ExploreFragment f = new Main_ExploreFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    public static void setStBusStop(int stBusStop, String text) {
        sStBusStop = stBusStop;
        sEditText1.setText(text);
    }

    public static void setEdBusStop(int edBusStop, String text) {
        sEdBusStop = edBusStop;
        sEditText2.setText(text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_explore, container, false);

        sEditText1 = (EditText) rootView.findViewById(R.id.edit_text_1);
        sEditText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(false);
            }
        });

        ImageButton imageButton1 = (ImageButton) rootView.findViewById(R.id.image_button_1);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(false);
            }
        });

        sEditText2 = (EditText) rootView.findViewById(R.id.edit_text_2);
        sEditText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(true);
            }
        });

        ImageButton imageButton2 = (ImageButton) rootView.findViewById(R.id.image_button_2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(true);
            }
        });

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sStBusStop == 0) {
                    Toast.makeText(getActivity(), "출발지를 선택하세요.", Toast.LENGTH_SHORT).show();
                } else if (sEdBusStop == 0) {
                    Toast.makeText(getActivity(), "도착지를 선택하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), RouteExploreActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt("stBusStop", sStBusStop);
                    extras.putInt("edBusStop", sEdBusStop);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    private void search(boolean isSecond) {
        startActivity(new Intent(getActivity(),
                !isSecond ? ExploreActivity1.class : ExploreActivity2.class));
    }
}
