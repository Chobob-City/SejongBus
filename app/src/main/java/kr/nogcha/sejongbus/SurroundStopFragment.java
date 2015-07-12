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
import android.content.Intent;
import android.location.Location;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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
    private Activity mActivity;
    private SejongBisClient mBisClient;
    private GoogleApiClient mApiClient;
    private MapView mMapView;
    private ListView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mBisClient = new SejongBisClient(mActivity);
        if (!mBisClient.isNetworkConnected()) return;

        mApiClient = new GoogleApiClient.Builder(mActivity).addConnectionCallbacks(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_surround_stop, container, false);

        Toast.makeText(mActivity, "위치를 켜면 자동으로 검색됩니다.\n"
                + "더미 데이터가 표시될 수 있습니다.", Toast.LENGTH_SHORT).show();

        mMapView = (MapView) rootView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);
                if (MapsInitializer.initialize(mActivity) != ConnectionResult.SUCCESS) {
                    Toast.makeText(mActivity, "지도 초기화 실패", Toast.LENGTH_SHORT).show();
                }
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
        mMapView.onResume();

        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (errorCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(errorCode)) {
                GooglePlayServicesUtil.getErrorDialog(errorCode, mActivity, 0).show();
            } else {
                Toast.makeText(mActivity, "지원하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mApiClient.isConnected()) mApiClient.disconnect();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
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

        mJSONList.clear();
        List<CommonListItem> list = new ArrayList<>();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
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

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(latitude, longitude), (float) 14.7));
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
