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

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

public class AboutActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText(Html.fromHtml("<p>세종버스 v2.0<br>Copyright (C) 2015 Chobob City</p>"
                + "<p>Licensed under the Apache License, Version 2.0 (the \"License\"); "
                + "you may not use this file except in compliance with the License. "
                + "You may obtain a copy of the License at</p>"
                + "<p>http://www.apache.org/licenses/LICENSE-2.0</p>"
                + "<p>Unless required by applicable law or agreed to in writing, software "
                + "distributed under the License is distributed on an \"AS IS\" BASIS, "
                + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. "
                + "See the License for the specific language governing permissions and "
                + "limitations under the License.</p>"
                + "<p><b>버스정보시스템 DB는 세종시에서 제공하며, "
                + "저희 개발자들이 관리하는 사항이 아님을 알립니다.</b></p>"
                + "<p>https://github.com/kanglib/SejongBus</p>"));
    }
}
