package kr.nogcha.sejongbus;

import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

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

    /*
     * stop_id
     *
     * returns busRealLocList
     */
    public static JSONObject searchBusRealLocationDetail(int busRouteId) {
        String content = "busRouteId=" + busRouteId;
        return execute("web/traffic/searchBusRealLocationDetail", content);
    }

    /*
     * (route_type)route_name번
     * st_stop_name ↔ ed_stop_name
     *
     * ... route_id
     *
     * returns busRouteList
     */
    public static JSONObject searchBusRoute(String busRoute) {
        String content = "busRoute=" + busRoute;
        return execute("mobile/traffic/searchBusRoute", content);
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
     *
     * returns busRouteDetailList
     */
    public static JSONObject searchBusRouteDetail(int busRouteId) {
        String content = "busRouteId=" + busRouteId;
        return execute("mobile/traffic/searchBusRouteDetail", content);
    }

    /*
     * returns busRouteExpMapList
     */
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

    /*
     * stop_name
     * [service_id]
     *
     * ... stop_id
     *
     * returns busStopList
     */
    public static JSONObject searchBusStop(String busStop) {
        String content = "busStop=" + busStop;
        return execute("mobile/traffic/searchBusStop", content);
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
     *
     * returns busStopRouteList
     */
    public static JSONObject searchBusStopRoute(int busStopId) {
        String content = "busStopId=" + busStopId;
        return execute("mobile/traffic/searchBusStopRoute", content);
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
     *
     * returns routeExplore
     */
    public static JSONObject searchRouteExplore(int stBusStop, int edBusStop) {
        String content = "stBusStop=" + stBusStop + "&edBusStop=" + edBusStop;
        return execute("mobile/traffic/searchRouteExplore", content);
    }

    /*
     * returns busStopList
     */
    public static JSONObject searchSurroundStopList(double lat, double lng) {
        String content = "lat=" + lat + "&lng=" + lng;
        return execute("mobile/traffic/searchSurroundStopList", content);
    }

    public static JSONObject selectBusStop(int busStopId) {
        String content = "busStopId=" + busStopId;
        return execute("mobile/traffic/selectBusStop", content);
    }

    public static Spannable getRouteType(int route_type) {
        Spannable routeType;
        switch (route_type) {
            case 43:
                routeType = new SpannableString("세종광역");
                routeType.setSpan(new BackgroundColorSpan(Color.RED), 0, 4,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case 50:
                routeType = new SpannableString("대전광역");
                routeType.setSpan(new BackgroundColorSpan(Color.RED), 0, 4,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case 51:
                routeType = new SpannableString("청주광역");
                routeType.setSpan(new BackgroundColorSpan(Color.RED), 0, 4,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case 30:
                routeType = new SpannableString("마을");
                routeType.setSpan(new BackgroundColorSpan(Color.GREEN), 0, 2,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            default:
                routeType = new SpannableString("일반");
                routeType.setSpan(new BackgroundColorSpan(Color.BLUE), 0, 2,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        routeType.setSpan(new ForegroundColorSpan(Color.WHITE), 0, routeType.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return routeType;
    }

    private static JSONObject execute(String relativeUri, String content) {
        JSONObject object = null;
        try {
            object = new JSONObject(new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    HttpURLConnection connection = null;
                    String response = null;
                    try {
                        connection = (HttpURLConnection) new URL(ABSOLUTE_URI + params[0])
                                .openConnection();
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
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                    return response;
                }
            }.execute(relativeUri, content).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }
}
