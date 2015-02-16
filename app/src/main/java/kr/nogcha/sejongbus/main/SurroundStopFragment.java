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
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
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

import kr.nogcha.sejongbus.R;
import kr.nogcha.sejongbus.SejongBisClient;
import kr.nogcha.sejongbus.host.HostActivity;

public class SurroundStopFragment extends Fragment implements OnMapReadyCallback {
    private SejongBisClient mBisClient;
    private JSONArray mJSONArray;
    private ArrayList<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBisClient = new SejongBisClient(getActivity());
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_surround_stop, container, false);

        MainActivity.hideSoftInput();

        FragmentManager fragmentManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentManager = getChildFragmentManager();
        } else {
            fragmentManager = getFragmentManager();
        }
        MapFragment map = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        map.getMapAsync(this);

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setEmptyView(rootView.findViewById(R.id.textView));
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        final FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        if (mapFragment != null) fragmentManager.beginTransaction().remove(mapFragment).commit();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // 조치원역
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(36.601031, 127.295882)));
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
                        JSONObject json = mJSONArray.getJSONObject(i);
                        String stop = json.getString("stop_name") + " ["
                                + json.getString("service_id") + "]";
                        googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(json.getDouble("lat"), json.getDouble("lng")))
                                .title(stop));
                        mList.add(stop + "\n" + json.getString("distance") + "m");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
