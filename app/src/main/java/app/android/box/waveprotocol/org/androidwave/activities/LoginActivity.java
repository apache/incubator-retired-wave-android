/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package app.android.box.waveprotocol.org.androidwave.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.android.box.waveprotocol.org.androidwave.R;
import app.android.box.waveprotocol.org.androidwave.service.WaveService;

public class LoginActivity extends Activity {

    Button login;
    TextView sighup;
    EditText username;
    EditText password;

//    AsyncTask<String, Void, String> waveSignInTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        login = (Button) findViewById(R.id.btn_login);
        sighup = (TextView) findViewById(R.id.link_signup);
        username = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startLoginSession(Util.getHostAndUserNames(username.getText().toString())[1], Util.getHostAndUserNames(username.getText().toString())[0], password.getText().toString());
                Intent openLoginActivity = new Intent("app.android.box.waveprotocol.org.androidwave.INBOXACTIVITY");
                startActivity(openLoginActivity);
//                Intent openTest= new Intent("app.android.box.waveprotocol.org.androidwave.TEST");
//                startActivity(openTest);
            }
        });

        sighup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSignupActivity = new Intent("app.android.box.waveprotocol.org.androidwave.SINGUPACTIVITY");
                startActivity(openSignupActivity);
            }
        });

    }

    private void startLoginSession(String hostname, String username, String password) {
        WaveService waveService = new WaveService();
        waveService.waveLoginTask(hostname, username, password);
    }

}
