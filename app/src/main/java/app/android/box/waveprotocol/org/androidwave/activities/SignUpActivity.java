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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import app.android.box.waveprotocol.org.androidwave.R;
import app.android.box.waveprotocol.org.androidwave.service.WaveService;
import app.android.box.waveprotocol.org.androidwave.util.Util;

public class SignUpActivity extends Activity {

    EditText username;
    EditText password;
    EditText reEnterPassword;
    Button signUp;
    TextView signIn;

    AsyncTask<String, Void, Boolean> waveSignUpTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        username = (EditText) findViewById(R.id.input_username);
        password = (EditText) findViewById(R.id.input_password);
        reEnterPassword = (EditText) findViewById(R.id.input_reEnterPassword);
        signUp = (Button) findViewById(R.id.btn_signIn);
        signIn = (TextView) findViewById(R.id.link_signup);

        waveSignUpTask = new AsyncTask<String, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(String... params) {
                WaveService waveService = new WaveService();
                return waveService.waveSignUpTask(params[0], params[1], params[2]);
            }


            @Override
            protected void onPostExecute(Boolean signUpResult) {
                if (signUpResult) {
                    Intent openLoginActivity = new Intent("app.android.box.waveprotocol.org.androidwave.LOGINACTIVITY");
                    startActivity(openLoginActivity);
                    Toast.makeText(SignUpActivity.this, "User sign up successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "User sign up fail", Toast.LENGTH_LONG).show();
                    Toast.makeText(SignUpActivity.this, "Please try again later...", Toast.LENGTH_LONG).show();
                }
            }
        };

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveSignUpTask.execute(Util.getHostAndUserNames(username.getText().toString())[1], Util.getHostAndUserNames(username.getText().toString())[0], password.getText().toString());
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLoginActivity = new Intent("app.android.box.waveprotocol.org.androidwave.LOGINACTIVITY");
                startActivity(openLoginActivity);
            }
        });
    }

}
