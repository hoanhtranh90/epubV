/*
 * Copyright (C) 2016 Pedro Paulo de Amorim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.folioreader.android.sample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.folioreader.Config;
import com.folioreader.FolioReader;
import com.folioreader.model.HighLight;
import com.folioreader.model.locators.ReadLocator;
import com.folioreader.ui.base.OnSaveHighlight;
import com.folioreader.util.AppUtil;
import com.folioreader.util.OnHighlightListener;
import com.folioreader.util.ReadLocatorListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements OnHighlightListener, ReadLocatorListener, FolioReader.OnClosedListener {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private FolioReader folioReader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        folioReader = FolioReader.get()
                .setOnHighlightListener(this)
                .setReadLocatorListener(this)
                .setOnClosedListener(this);

        getHighlightsAndSave();

//        findViewById(R.id.btn_raw).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Config config = AppUtil.getSavedConfig(getApplicationContext());
//                if (config == null)
//                    config = new Config();
//                config.setAllowedDirection(Config.AllowedDirection.VERTICAL_AND_HORIZONTAL);
//
//                folioReader.setConfig(config, true)
//                        .openBook(R.raw.accessible_epub_3);
//            }
//        });

        findViewById(R.id.btn_assest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JSON", "saveReadLocator112345: " + v);
                ReadLocator readLocator = getLastReadLocator();

                Config config = AppUtil.getSavedConfig(getApplicationContext());
                if (config == null)
                    config = new Config();
                config.setAllowedDirection(Config.AllowedDirection.VERTICAL_AND_HORIZONTAL);
                folioReader.setReadLocator(readLocator);
                Log.d("JSON", "saveReadLocatasor1123sa45: " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/finish(4).epub");
                folioReader.setConfig(config, true)
//                        .openBook("file:///android_asset/TheSilverChair.epub");
                        .openBook(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/sangdz.epub");

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                openFile();
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/finish (4).epub");
//                if(file.exists()) {
//                    Log.d("JSON", "file ton tai: ");
//                }
//                else {
//                    Log.d("JSON", "file khong ton tai: ");
//                }
            }
        });
    }
    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, Home.class);
        startActivity(switchActivityIntent);
    }
    private static final int PICK_PDF_FILE = 2;

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("application/epub+zip");

        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri currentUri = null;
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
//                if (data != null) {
//                    currentUri = data.getData();
//                    Log.d("JSON", "currentUri: " + currentUri);
//                    Log.d("JSON", "getScheme: " + currentUri.getScheme());
//                    Log.d("JSON", "getHost: " + currentUri.getHost());
//                    Log.d("JSON", "getPath: " + currentUri.getPath());
//                }

                if (data != null) {

                    currentUri = data.getData();
                    String path = currentUri.getPath();
                    Log.d("JSON", "datatata: " + currentUri);
                    Config config = AppUtil.getSavedConfig(getApplicationContext());
                    if (config == null)
                        config = new Config();
                    config.setAllowedDirection(Config.AllowedDirection.VERTICAL_AND_HORIZONTAL);
                    if(currentUri.getPath().contains("document/raw:")){
                        path = path.replace("/document/raw:","");
                    }
                    folioReader.setConfig(config, true)
                            .openBook(path);
                }
            }
        }
    }
    private ReadLocator getLastReadLocator() {

        try {
            String loc = getApplicationContext().getFilesDir().getAbsolutePath() + "/read_locator.json";
            File file = new File(loc);
            String json;
            InputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            return ReadLocator.fromJson(json);
        } catch (Exception ignored) {
            String jsonString = loadAssetTextAsString("Locators/LastReadLocators/last_read_locator_1.json");
            return ReadLocator.fromJson(jsonString);
        }
    }

    @Override
    public void saveReadLocator(ReadLocator readLocator) {
        Log.i(LOG_TAG, "-> saveReadLocator -> " + readLocator.toJson());
        try {
            String json = readLocator.toJson();
            Log.d("JSON", "saveReadLocator112345: " + json);

            String loc = getApplicationContext().getFilesDir().getAbsolutePath();
            File writeLocator = new File(loc, readLocator.getBookId()+".json");
            FileWriter writer = new FileWriter(writeLocator, false);
            writer.write(json);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Book", "saveReadLocator: " + e.getMessage(), e);
        }
        Log.i(LOG_TAG, "->  -> ");
    }

    /*
     * For testing purpose, we are getting dummy highlights from asset. But you can get highlights from your server
     * On success, you can save highlights to FolioReader DB.
     */
    private void getHighlightsAndSave() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<HighLight> highlightList = null;
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    highlightList = objectMapper.readValue(
                            loadAssetTextAsString("highlights/highlights_data.json"),
                            new TypeReference<List<HighlightData>>() {
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (highlightList == null) {
                    folioReader.saveReceivedHighLights(highlightList, new OnSaveHighlight() {
                        @Override
                        public void onFinished() {
                            //You can do anything on successful saving highlight list
                        }
                    });
                }
            }
        }).start();
    }

    private String loadAssetTextAsString(String name) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ((str = in.readLine()) != null) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e("HomeActivity", "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e("HomeActivity", "Error closing asset " + name);
                }
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FolioReader.clear();
    }

    @Override
    public void onHighlight(HighLight highlight, HighLight.HighLightAction type) {
        Toast.makeText(this,
                "highlight id = " + highlight.getUUID() + " type = " + type,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFolioReaderClosed() {
        Log.v(LOG_TAG, "-> onFolioReaderClosed");
    }
}