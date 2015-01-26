package kr.nogcha.sejongbus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

public class BisHostActivity extends ActionBarActivity {
    private static Context baseContext;

    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) baseContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bis_host);

        baseContext = getBaseContext();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Fragment fragment = null;
        Intent intent = getIntent();
        Bundle bundle;
        switch (intent.getIntExtra("fragment", 0)) {
            case 0:
                fragment = new BusRouteFragment();
                bundle = new Bundle();
                bundle.putInt("busRouteId", intent.getIntExtra("arg1", 0));
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new BusStopFragment();
                bundle = new Bundle();
                bundle.putInt("busStopId", intent.getIntExtra("arg1", 0));
                fragment.setArguments(bundle);
                break;
            case 2:
                fragment = new RouteExploreFragment();
                bundle = new Bundle();
                bundle.putInt("stBusStop", intent.getIntExtra("arg1", 0));
                bundle.putInt("edBusStop", intent.getIntExtra("arg2", 0));
                fragment.setArguments(bundle);
        }
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bis_host, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
