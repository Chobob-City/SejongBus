package kr.nogcha.sejongbus;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SejongBis {
    private static final String ABSOLUTE_URI = "http://mbis.sejong.go.kr/";

    public static JSONObject searchBusRealLocationDetail(int busRouteId) {
        String content = "busRouteId=" + busRouteId;
        return execute("web/traffic/searchBusRealLocationDetail", content);
    }

    public static JSONObject searchBusRoute(String busRoute) {
        String content = "busRoute=" + busRoute;
        return execute("mobile/traffic/searchBusRoute", content);
    }

    public static JSONObject searchBusRouteDetail(int busRouteId) {
        String content = "busRouteId=" + busRouteId;
        return execute("mobile/traffic/searchBusRouteDetail", content);
    }

    public static JSONObject searchBusRouteExpMap1(int stRouteId,
                                                   int sstOrd, int eedOrd,
                                                   int stStopId, int edStopId) {
        String content = "stRouteId=" + stRouteId +
                "&sstOrd=" + sstOrd + "&eedOrd=" + eedOrd +
                "&stStopId=" + stStopId + "&edStopId=" + edStopId;
        return execute("web/traffic/searchBusRouteExpMap1", content);
    }

    public static JSONObject searchBusRouteMap(int busRouteId) {
        String content = "busRouteId=" + busRouteId;
        return execute("mobile/traffic/searchBusRouteMap", content);
    }

    public static JSONObject searchBusStop(String busStop) {
        String content = "busStop=" + busStop;
        return execute("mobile/traffic/searchBusStop", content);
    }

    public static JSONObject searchBusStopRoute(int busStopId) {
        String content = "busStopId=" + busStopId;
        return execute("mobile/traffic/searchBusStopRoute", content);
    }

    public static JSONObject searchRouteExplore(int stBusStop, int edBusStop) {
        String content = "stBusStop=" + stBusStop + "&edBusStop=" + edBusStop;
        return execute("mobile/traffic/searchRouteExplore", content);
    }

    public static JSONObject searchSurroundStopList(double lat, double lng) {
        String content = "lat=" + lat + "&lng=" + lng;
        return execute("mobile/traffic/searchSurroundStopList", content);
    }

    public static JSONObject selectBusStop(int busStopId) {
        String content = "busStopId=" + busStopId;
        return execute("mobile/traffic/selectBusStop", content);
    }

    private static JSONObject execute(String relativeUri, String content) {
        JSONObject json = null;
        if (MainActivity.checkNetworkConnected()) {
            try {
                json = new JSONObject(new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        HttpURLConnection urlConnection = null;
                        String response = null;
                        try {
                            urlConnection = (HttpURLConnection) new URL(ABSOLUTE_URI + params[0])
                                    .openConnection();
                            urlConnection.setDoOutput(true);
                            urlConnection.setChunkedStreamingMode(0);

                            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                                    urlConnection.getOutputStream(), "UTF-8"));
                            writer.write(params[1]);
                            writer.close();

                            BufferedReader reader = new BufferedReader(new InputStreamReader(
                                    urlConnection.getInputStream(), "UTF-8"));
                            response = reader.readLine();
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                        }
                        return response;
                    }
                }.execute(relativeUri, content).get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return json;
    }
}
