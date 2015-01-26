package kr.nogcha.sejongbus;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends Activity {
    private static Context baseContext;

    public static void toggleSoftInput() {
        ((InputMethodManager) baseContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(0, 0);
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                baseContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        if (savedInstanceState == null) {
            Fragment routeExploreFragment = new RouteExploreFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("stBusStop", 186004622);
            bundle.putInt("edBusStop", 186004640);
            routeExploreFragment.setArguments(bundle);

            //getFragmentManager().beginTransaction().add(R.id.container, routeExploreFragment)
            //        .commit();

            getFragmentManager().beginTransaction().add(R.id.container, new MainFragment())
                   .commit();

        }

        baseContext = getBaseContext();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
