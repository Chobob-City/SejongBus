package kr.nogcha.sejongbus;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class SejongMbis {
    private static final String ABSOLUTE_URI = "http://mbis.sejong.go.kr/";

    /*
     * stop_id
     */
    public static JSONArray searchBusRealLocationDetail(int busRouteId) throws JSONException {
        String content = "busRouteId=" + String.valueOf(busRouteId);
        return call("web/traffic/searchBusRealLocationDetail", content)
                .getJSONArray("busRealLocList");
    }

    /*
     * (route_type)route_name번
     * st_stop_name ↔ ed_stop_name
     *
     * ... route_id
     */
    public static JSONArray searchBusRoute(String busRoute) throws JSONException {
        String content = "busRoute=" + busRoute;
        return call("mobile/traffic/searchBusRoute", content)
                .getJSONArray("busRouteList");
    }

    /*
     * route_name번 노선정보
     * · 기점 : st_stop_name
     * · 종점 : ed_stop_name
     * · 운행횟수 : alloc_time
     *
     * stop_name
     * [service_id]
     *
     * ... stop_id
     */
    public static JSONArray searchBusRouteDetail(int busRouteId) throws JSONException {
        String content = "busRouteId=" + String.valueOf(busRouteId);
        return call("mobile/traffic/searchBusRouteDetail", content)
                .getJSONArray("busRouteDetailList");
    }

    public static JSONArray searchBusRouteExpMap1(int stRouteId,
                                                  int sstOrd,
                                                  int eedOrd,
                                                  int stStopId,
                                                  int edStopId) throws JSONException {
        String content = "stRouteId=" + String.valueOf(stRouteId) +
                "&sstOrd=" + String.valueOf(sstOrd) +
                "&eedOrd=" + String.valueOf(eedOrd) +
                "&stStopId=" + String.valueOf(stStopId) +
                "&edStopId=" + String.valueOf(edStopId);
        return call("web/traffic/searchBusRouteExpMap1", content)
                .getJSONArray("busRouteExpMapList");
    }

    public static JSONObject searchBusRouteMap(int busRouteId) throws JSONException {
        String content = "busRouteId=" + String.valueOf(busRouteId);
        return call("mobile/traffic/searchBusRouteMap", content);
    }

    /*
     * stop_name
     * [service_id]
     *
     * ... stop_id
     */
    public static JSONArray searchBusStop(String busStop) throws JSONException {
        String content = "busStop=" + busStop;
        return call("mobile/traffic/searchBusStop", content)
                .getJSONArray("busStopList");
    }

    /*
     * stop_name
     * [service_id]
     *
     * (route_type)route_name번
     * provide_code
     * 도착예정: provide_type | 현재위치: rstop
     *
     * ... route_id
     */
    public static JSONArray searchBusStopRoute(int busStopId) throws JSONException {
        String content = "busStopId=" + String.valueOf(busStopId);
        return call("mobile/traffic/searchBusStopRoute", content)
                .getJSONArray("busStopRouteList");
    }

    /*
     * 출발 : sstationname(sService_id)
     * 도착 : estationname(eService_id)
     *
     * xtype
     * 승차 : srouteno 노선(sstationname 정류장)
     * 하차 : tstationname 정류장
     * 환승 : erouteno 노선(tstationname 정류장)
     * 도착 : estationname 정류장
     * (seq개 정류소, (distance / 1000)(km))
     */
    public static JSONArray searchRouteExplore(int stBusStop,
                                               int edBusStop) throws JSONException {
        String content = "stBusStop=" + String.valueOf(stBusStop) +
                "&edBusStop=" + String.valueOf(edBusStop);
        return call("mobile/traffic/searchRouteExplore", content)
                .getJSONArray("routeExplore");
    }

    public static JSONArray searchSurroundStopList(double lat,
                                                   double lng) throws JSONException {
        String content = "lat=" + String.valueOf(lat) +
                "&lng=" + String.valueOf(lng);
        return call("mobile/traffic/searchSurroundStopList", content)
                .getJSONArray("busStopList");
    }

    public static JSONObject selectBusStop(int busStopId) throws JSONException {
        String content = "busStopId=" + String.valueOf(busStopId);
        return call("mobile/traffic/selectBusStop", content);
    }

    private static JSONObject call(String relativeUri, String content) throws JSONException {
        JSONObject object = null;
        try {
            object = new JSONObject(new InternetTask().execute(relativeUri, content).get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return object;
    }

    private static class InternetTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String response = null;
            try {
                URL url = new URL(ABSOLUTE_URI + params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        urlConnection.getOutputStream(), "UTF-8"));
                out.write(params[1]);
                out.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "UTF-8"));
                response = in.readLine();
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return response;
        }
    }
}
