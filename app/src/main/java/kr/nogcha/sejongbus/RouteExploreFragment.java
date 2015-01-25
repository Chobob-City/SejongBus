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
        View rootView = inflater.inflate(R.layout.fragment_route_explore, container, false);

        if (MainActivity.isNetworkConnected()) {
            ArrayList<String> list = new ArrayList<>();
            try {
                routeExplore = SejongBis
                        .searchRouteExplore(getArguments().getInt("stBusStop"),
                                getArguments().getInt("edBusStop"))
                        .getJSONArray("routeExplore");

                TextView textView = (TextView) rootView.findViewById(R.id.textView);
                JSONObject object = routeExplore.getJSONObject(0);
                textView.setText("출발: " + object.getString("sstationname") + " [" +
                        object.getString("sService_id") + "]\n도착: " +
                        object.getString("estationname") + " [" +
                        object.getString("eService_id") + "]");

                for (int i = 0; i < routeExplore.length(); i++) {
                    object = routeExplore.getJSONObject(i);
                    String route;

                    int xtype = object.getInt("xtype");
                    if (xtype == 1) {
                        route = "무";
                    } else {
                        route = "";
                    }
                    route += "환승 경로\n" + object.getString("sstationname") + "에서 " +
                            object.getString("srouteno") + "번에 승차\n";
                    if (xtype != 1) {
                        route += object.getString("tstationname") + "에서 " +
                                object.getString("erouteno") + "번으로 환승\n";
                    }
                    route += object.getString("estationname") + "에서 하차\n" +
                            object.getString("seq") + "개 정류장, " +
                            object.getInt("distance") / 1000. + "km";

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
