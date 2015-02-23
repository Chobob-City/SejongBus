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
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SurroundStopFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        LocationListener {
    private GoogleMap mMap = null;
    private List<JSONObject> mJSONList = new ArrayList<>();
    private SejongBisClient mBisClient;
    private GoogleApiClient mApiClient;
    private ListView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        mBisClient = new SejongBisClient(activity);
        if (!mBisClient.isNetworkConnected()) return;

        mApiClient = new GoogleApiClient.Builder(activity).addConnectionCallbacks(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_surround_stop, container, false);

        FragmentManager fragmentManager;
        if (Build.VERSION.SDK_INT >= 21) {
            fragmentManager = getChildFragmentManager();
        } else {
            fragmentManager = getFragmentManager();
        }
        MapFragment map = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // 조치원역
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(36.601031, 127.295882), (float) 14.7));
                mMap.setMyLocationEnabled(true);
            }
        });

        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mListView.setEmptyView(rootView.findViewById(R.id.text_view));

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (errorCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
                GooglePlayServicesUtil.getErrorDialog(errorCode, activity, 0).show();
            } else {
                Toast.makeText(activity, "해당 기기는 지원되지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mApiClient.isConnected()) mApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(1);
        LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMap == null) return;

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        List<CommonListItem> list = new ArrayList<>();
        try {
            JSONArray jsonArray = mBisClient.searchSurroundStopList(latitude, longitude)
                    .getJSONArray("busStopList");

            for (int i = 0; i < jsonArray.length(); i++) mJSONList.add(jsonArray.getJSONObject(i));
            Collections.sort(mJSONList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject lhs, JSONObject rhs) {
                    int result = 0;
                    try {
                        result = lhs.getInt("distance") - rhs.getInt("distance");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return result;
                }
            });

            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            for (int i = 0; i < mJSONList.size(); i++) {
                CommonListItem item = new CommonListItem();
                JSONObject json = mJSONList.get(i);
                item.resId = R.drawable.busstopicon;
                item.text1 = json.getString("stop_name");
                item.text2 = json.getString("service_id");
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(json.getDouble("lat"), json.getDouble("lng")))
                        .title(item.text1 + " [" + item.text2 + "]"));
                item.text2 += "\n" + json.getString("distance") + "m";
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mListView.setAdapter(new CommonAdapter(getActivity(), R.layout.common_list_item, list));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BusStopRouteActivity.class);
                Bundle extras = new Bundle();
                try {
                    extras.putInt("stop_id", mJSONList.get(position).getInt("stop_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }
}
