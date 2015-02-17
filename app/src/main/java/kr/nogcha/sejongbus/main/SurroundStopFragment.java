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

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.nogcha.sejongbus.BusStopRouteActivity;
import kr.nogcha.sejongbus.CommonAdapter;
import kr.nogcha.sejongbus.CommonListItem;
import kr.nogcha.sejongbus.MainActivity;
import kr.nogcha.sejongbus.R;
import kr.nogcha.sejongbus.SejongBisClient;

public class SurroundStopFragment extends Fragment implements OnMapReadyCallback {
    private ArrayList<CommonListItem> mList = new ArrayList<>();
    private SejongBisClient mBisClient;
    private CommonAdapter mAdapter;
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
        View rootView = inflater.inflate(R.layout.f_surround_stop, container, false);

        MainActivity.hideSoftInput();

        FragmentManager fragmentManager;
        if (Build.VERSION.SDK_INT >= 17) {
            fragmentManager = getChildFragmentManager();
        } else {
            fragmentManager = getFragmentManager();
        }
        MapFragment map = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        map.getMapAsync(this);

        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setEmptyView(rootView.findViewById(R.id.text_view));
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BusStopRouteActivity.class);
                Bundle bundle = new Bundle();
                try {
                    bundle.putInt("stop_id", mJSONArray.getJSONObject(position).getInt("stop_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng target = cameraPosition.target;
                try {
                    mJSONArray = mBisClient
                            .searchSurroundStopList(target.latitude, target.longitude)
                            .getJSONArray("busStopList");
                    googleMap.clear();
                    mList.clear();
                    for (int i = 0; i < mJSONArray.length(); i++) {
                        CommonListItem item = new CommonListItem();
                        JSONObject json = mJSONArray.getJSONObject(i);

                        item.text2 = json.getString("stop_name");
                        item.text3 = json.getString("service_id");
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(json.getDouble("lat"), json.getDouble("lng")))
                                .title(item.text2 + " [" + item.text3 + "]"));

                        item.text3 += "\n" + json.getString("distance") + "m";
                        mList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
