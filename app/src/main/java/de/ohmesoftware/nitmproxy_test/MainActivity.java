/*
 *
 *  * Copyright 2021 Karsten Ohme
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package de.ohmesoftware.nitmproxy_test;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.chhsiao90.nitmproxy.NitmProxy;
import com.github.chhsiao90.nitmproxy.NitmProxyConfig;
import com.github.chhsiao90.nitmproxy.enums.ProxyMode;

import org.conscrypt.Conscrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MainActivity extends AppCompatActivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    private NitmProxy server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SLF4JBridgeHandler.install();
    }

    private void showError(Exception e) {
        runOnUiThread(() -> {
            String errorMsg = e.getMessage();
            LOGGER.error("Error", e);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            final AlertDialog alertDialog = alertDialogBuilder
                    .setTitle(R.string.exception)
                    .setMessage(errorMsg)
                    .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                        dialog.dismiss();
                    }).
                            setCancelable(false).create();

            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        });
    }

    private void showOk(String message) {
        runOnUiThread(() -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            final AlertDialog alertDialog = alertDialogBuilder
                    .setTitle(R.string.success)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                        dialog.dismiss();
                    }).
                            setCancelable(false).create();

            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        });
    }


    public void runProxy(View view) {
        new Thread(() -> {
            try {
                NitmProxyConfig config = new NitmProxyConfig();
                config.setSslProvider(Conscrypt.newProvider());
                config.setProxyMode(ProxyMode.SOCKS);
                config.setHost("127.0.0.1");
                config.setPort(9090);
                Files.copy(getResources().openRawResource(R.raw.server), new File(getFilesDir(), "server.pem").toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(getResources().openRawResource(R.raw.key), new File(getFilesDir(), "key.pem").toPath(), StandardCopyOption.REPLACE_EXISTING);
                config.setCertFile(new File(getFilesDir(), "server.pem").getAbsolutePath());
                config.setKeyFile(new File(getFilesDir(), "key.pem").getAbsolutePath());
                config.setInsecure(true);
                server = new NitmProxy(config);
                server.start();
            } catch (Exception e) {
                runOnUiThread(() -> showError(e));
            }
        }).start();
    }

    public void stopProxy(View view) {
        new Thread(() -> {
            try {
                stopServer();
                runOnUiThread(() -> showOk(getString(R.string.success_stopped_msg)));
            } catch (Exception e) {
                runOnUiThread(() -> showError(e));
            }
        }).start();
    }

    private void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopServer();
    }
}