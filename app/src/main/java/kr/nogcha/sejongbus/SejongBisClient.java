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

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

class SejongBisClient {
    private Context mContext;

    SejongBisClient(Context context) {
        mContext = context;
    }

    JSONObject searchBusRealLocationDetail(int busRouteId) {
        String params = "busRouteId=" + busRouteId;
        return sendRequest("searchBusRealLocationDetail", params);
    }

    JSONObject searchBusRoute(String busRoute, boolean isMobile) {
        String params = "busRoute=" + busRoute;
        return sendRequest("searchBusRoute", params, isMobile);
    }

    JSONObject searchBusRouteDetail(int busRouteId, boolean isMobile) {
        String params = "busRouteId=" + busRouteId;
        return sendRequest("searchBusRouteDetail", params, isMobile);
    }

    JSONObject searchBusRouteExpMap1(int stRouteId, int sstOrd, int eedOrd, int stStopId,
                                     int edStopId) {
        String params = "stRouteId=" + stRouteId + "&sstOrd=" + sstOrd + "&eedOrd=" + eedOrd
                + "&stStopId=" + stStopId + "&edStopId=" + edStopId;
        return sendRequest("web/traffic/searchBusRouteExpMap1", params);
    }

    JSONObject searchBusRouteMap(int busRouteId, boolean isMobile) {
        String params = "busRouteId=" + busRouteId;
        return sendRequest("searchBusRouteMap", params, isMobile);
    }

    JSONObject searchBusStop(String busStop, boolean isMobile) {
        String params = "busStop=" + busStop;
        return sendRequest("searchBusStop", params, isMobile);
    }

    JSONObject searchBusStopRoute(int busStopId, boolean isMobile) {
        String params = "busStopId=" + busStopId;
        return sendRequest("searchBusStopRoute", params, isMobile);
    }

    JSONObject searchRouteExplore(int stBusStop, int edBusStop, boolean isMobile) {
        String params = "stBusStop=" + stBusStop + "&edBusStop=" + edBusStop;
        return sendRequest("searchRouteExplore", params, isMobile);
    }

    JSONObject searchSurroundStopList(double lat, double lng, boolean isMobile) {
        String params = "lat=" + lat + "&lng=" + lng;
        return sendRequest("searchSurroundStopList", params, isMobile);
    }

    JSONObject selectBusStop(int busStopId, boolean isMobile) {
        String params = "busStopId=" + busStopId;
        return sendRequest("selectBusStop", params, isMobile);
    }

    Spannable getRouteType(int route_type) {
        Spannable routeType;
        int backgroundColor;
        switch (route_type) {
            case 30:
                routeType = new SpannableString("마을");
                backgroundColor = Color.GREEN;
                break;
            case 43:
                routeType = new SpannableString("세종광역");
                backgroundColor = Color.RED;
                break;
            case 50:
                routeType = new SpannableString("대전광역");
                backgroundColor = Color.RED;
                break;
            case 51:
                routeType = new SpannableString("청주광역");
                backgroundColor = Color.RED;
                break;
            default:
                routeType = new SpannableString("일반");
                backgroundColor = Color.BLUE;
        }
        routeType.setSpan(new ForegroundColorSpan(Color.WHITE), 0, routeType.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        routeType.setSpan(new BackgroundColorSpan(backgroundColor), 0, routeType.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return routeType;
    }

    private JSONObject sendRequest(String url, String params) {
        return sendRequest(url, params, false);
    }

    private JSONObject sendRequest(String url, String params, boolean isMobile) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && activeNetworkInfo.isConnected()) {
            try {
                return new JSONObject(new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        HttpURLConnection connection = null;
                        String response = null;
                        try {
                            Log.v("SejongBisClient", "Network is working");
                            connection = (HttpURLConnection) new URL(params[0]).openConnection();
                            connection.setDoOutput(true);
                            connection.setChunkedStreamingMode(0);

                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                                    connection.getOutputStream(), "UTF-8"));
                            writer.write(params[1]);
                            writer.close();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(
                                    connection.getInputStream(), "UTF-8"));
                            response = reader.readLine();
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (connection != null) connection.disconnect();
                        }
                        return response;
                    }
                }.execute("http://bis.sejong.go.kr/" + (!isMobile ? "web" : "mobile") + "/traffic/"
                        + url, params).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, "네트워크에 연결해 주세요.", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
