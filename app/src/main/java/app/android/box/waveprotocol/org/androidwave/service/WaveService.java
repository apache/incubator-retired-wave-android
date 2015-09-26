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

package app.android.box.waveprotocol.org.androidwave.service;

import android.os.AsyncTask;

import org.waveprotocol.wave.model.document.WaveContext;
import org.waveprotocol.wave.model.id.IdGenerator;
import org.waveprotocol.wave.model.id.IdGeneratorImpl;
import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.wave.ParticipantId;
import org.waveprotocol.wave.model.waveref.WaveRef;
import org.waveprotocol.wave.model.util.Pair;

import java.util.Collections;
import java.util.Timer;
import java.util.Map;

import app.android.box.waveprotocol.org.androidwave.service.concurrencycontrol.Connector.Command;
import app.android.box.waveprotocol.org.androidwave.service.models.Model;
import app.android.box.waveprotocol.org.androidwave.service.models.TypeIdGenerator;

public class WaveService {

    private String waveHost;
    private String waveSessionId;
    private String waveUsername;

    private Timer timer;

    private ParticipantId waveParticipantId;
    private IdGenerator waveIdGenerator;
    private TypeIdGenerator waveTypeIdGenerator;

    private WaveWebSocketClient waveWebSocketClient;
    private RemoteViewServiceMultiplexer waveChannel;

    private Map<WaveRef, Pair<WaveRender, Model>> waveStore;



    public boolean waveSignUpTask(String host, String username, String password){
        WaveSignUp waveSignUpService = new WaveSignUp();
        return waveSignUpService.waveSignUp(host,username,password);
    }

    public void waveLoginTask(String host, String username, String password){
        new WaveSession().execute(host,username,password);
    }

    public String getWaveSessionId() {
        return waveSessionId;
    }

    public void setWaveSessionId(String waveSessionId) {
        this.waveSessionId = waveSessionId;
    }

    public String getWaveHost() {
        return waveHost;
    }

    public void setWaveHost(String waveHost) {
        this.waveHost = waveHost;
    }

    public String getWaveUsername() {
        return waveUsername;
    }

    public void setWaveUsername(String waveUsername) {
        this.waveUsername = waveUsername;
    }

    public boolean isWaveSessionStarted() {
        return waveSessionId != null;
    }

    private void openWebSocketConnection(String hostName, String SessionId){

        String waveWebSocketUrl = "http://"+ hostName +"/atmosphere";

        waveIdGenerator = new IdGeneratorImpl(waveWebSocketUrl, new IdGeneratorImpl.Seed() {
            @Override
            public String get() {
                return waveSessionId.substring(0, 5);
            }
        });

        waveTypeIdGenerator = TypeIdGenerator.get(waveIdGenerator);

        waveWebSocketClient = new WaveWebSocketClient(waveWebSocketUrl, waveSessionId);

        waveWebSocketClient.connect(new WaveWebSocketClient.ConnectionListener() {
            @Override
            public void onConnect() {

            }

            @Override
            public void onReconnect() {

            }

            @Override
            public void onDisconnect() {

            }
        });

        waveChannel = new RemoteViewServiceMultiplexer(waveWebSocketClient, waveParticipantId.getAddress());

    }

    private void closeWebSocket() {

        waveIdGenerator = null;
        waveChannel = null;
        waveWebSocketClient = null;
    }

    public String createModel() {

        WaveId newWaveId = waveTypeIdGenerator.newWaveId();
        final WaveRef waveRef = WaveRef.of(newWaveId);

        final WaveRender waveRender = new WaveRender(true, waveRef, waveChannel, waveParticipantId,
                Collections.<ParticipantId>emptySet(), waveIdGenerator, null, timer);

        waveRender.init(new Command() {

            @Override
            public void execute() {

                WaveContext wave = waveRender.getWaveContext();
                Model model = Model.create(wave, waveHost, waveParticipantId, true, waveIdGenerator);

                waveStore.put(waveRef, new Pair<WaveRender, Model>(waveRender, model));
            }
        });

        return waveRef.getWaveId().serialise();
    }

    public void openModel(String modelId) {

    }

    public Model getModel(String modelId) {

        return null;
    }

    public void closeModel(String modelId) {

    }

    public class WaveSession extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            WaveSignIn waveSignIn = new WaveSignIn();
            waveSessionId = waveSignIn.waveSignIn(params[0], params[1], params[2]);
            return waveSessionId;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                openWebSocketConnection(waveHost, waveSessionId);
            }
        }
    }
}
