package kr.nogcha.sejongbus;

import android.app.FragmentManager;
import android.content.Context;
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
        setContentView(R.layout.a_bis_host);

        baseContext = getBaseContext();
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
            super.onBackPressed();
        }
    }
}
