package kr.nogcha.sejongbus;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RouteExploreFragment extends Fragment {
    JSONArray routeExplore;

    public RouteExploreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f_main_2, container, false);

        if (BisHostActivity.isNetworkConnected()) {
            ArrayList<String> list = new ArrayList<>();
            try {
                routeExplore = SejongBis
                        .searchRouteExplore(getArguments().getInt("stBusStop"),
                                getArguments().getInt("edBusStop"))
                        .getJSONArray("routeExplore");

                TextView textView = (TextView) rootView.findViewById(R.id.textView);
                JSONObject json = routeExplore.getJSONObject(0);
                textView.setText("출발: " + json.getString("sstationname") + " [" +
                        json.getString("sService_id") + "]\n도착: " +
                        json.getString("estationname") + " [" +
                        json.getString("eService_id") + "]");

                for (int i = 0; i < routeExplore.length(); i++) {
                    json = routeExplore.getJSONObject(i);
                    String route;

                    int xtype = json.getInt("xtype");
                    if (xtype == 1) {
                        route = "무";
                    } else {
                        route = "";
                    }
                    route += "환승 경로\n" + json.getString("sstationname") + "에서 " +
                            json.getString("srouteno") + "번에 승차\n";
                    if (xtype != 1) {
                        route += json.getString("tstationname") + "에서 " +
                                json.getString("erouteno") + "번으로 환승\n";
                    }
                    route += json.getString("estationname") + "에서 하차\n" +
                            json.getString("seq") + "개 정류장, " +
                            json.getInt("distance") / 1000. + "km";

                    list.add(route);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            listView.setAdapter(new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, list));
        }

        return rootView;
    }
}
