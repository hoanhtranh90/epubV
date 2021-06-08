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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;

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

        findViewById(R.id.btn_raw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Config config = AppUtil.getSavedConfig(getApplicationContext());
//                if (config == null)
//                    config = new Config();
//                config.setAllowedDirection(Config.AllowedDirection.VERTICAL_AND_HORIZONTAL);
//
//                folioReader.setConfig(config, true)
//                        .openBook(R.raw.accessible_epub_3);
//                loadData();
                String loc = getApplicationContext().getFilesDir().getAbsolutePath();
                Config config = AppUtil.getSavedConfig(getApplicationContext());
                if (config == null)
                    config = new Config();
                folioReader.setConfig(config, true)
//                        .openBook("file:///android_asset/TheSilverChair.epub");
                        .openBook("/data/data/com.folioreader.android.sample/filesfinish1.epub");

            }
        });

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

    private void loadData(){
        new AsyncTask<Void, Void, ArrayList>() {
            @Override
            protected ArrayList doInBackground(Void... voids) {
                ArrayList<HashMap<String, String>> last = new ArrayList<>();

                try {
                    String url = "https://truyenfull.vn/yeu-nham-nu-bang-chu-sieu-quay/";
                    System.out.println("abccascasd");
                    Document doc = Jsoup.connect(url).get();
                    Element content = doc.getElementById("total-page");
                    System.out.println("try");
                    String titleBook = doc.getElementsByTag("title").text();
                    ArrayList<String> listChuong = new ArrayList<>();
                    String urlCheck = doc.getElementsByClass("list-chapter").get(0).getElementsByAttribute("href").get(0).attr("href");
                    if(urlCheck.contains("quyen")){
                        String totalPage = content.select("input").attr("value");
                        for (int i = 1; i <= Integer.parseInt(totalPage); i++) {
                            String url1 = url+"/trang-"+i+"/";
                            Document doc1 = Jsoup.connect(url1).get();
                            Elements getListChuong = doc1.getElementsByClass("list-chapter");
                            for (int k = 0; k < getListChuong.size(); k++) {
                                for (int j = 0; j < getListChuong.get(k).getElementsByAttribute("href").size(); j++) {
                                    String url2 = getListChuong.get(k).getElementsByAttribute("href").get(j).attr("href");
                                    listChuong.add(url2);
                                }
                            }
                        }
                    }
                    else {
                        String totalPage = content.select("input").attr("value");

                        String url1 = url+"/trang-"+Integer.parseInt(totalPage)+"/";
                        Document doc1 = Jsoup.connect(url1).get();
                        Elements getListChuong = doc1.getElementsByClass("list-chapter");
                        String urlLast = getListChuong.get(getListChuong.size()-1)
                                .getElementsByAttribute("href")
                                .get(getListChuong.get(getListChuong.size()-1)
                                        .getElementsByAttribute("href").size()-1).attr("href");
                        int count = Integer.parseInt(urlLast.replaceAll("[^0-9]", ""));
                        System.out.println("abccascasd");
                        System.out.println(count);
                        for (int i = 1; i <= count; i++) {
                            String urlv = url+"/chuong-"+i+"/";
                            listChuong.add(urlv);
                        }
                    }
                    System.out.println(listChuong);

                    HashMap<String, String> fullData = new HashMap<>();
//            for (int i = 0; i < listChuong.size(); i++) {
                    for (int i = 0; i < 10; i++) {
                        String url_n = listChuong.get(i);
                        Document doc_n = Jsoup.connect(url_n).get();
                        Element datas = doc_n.select("div.chapter-c").first();
                        datas.select("div").remove();
                        Element title = doc_n.getElementsByClass("truyen-title").first();
                        Element chap = doc_n.getElementsByClass("chapter-title").first();
//            if(datas == null) {
//                return ResponseEntity.ok("truyen bi loi");
//            }
                        HashMap<String, String> fullDatax = new HashMap<>();
                        fullDatax.put("title", title.text());

                        fullDatax.put("datas", datas.html());
                        String h = datas.html();
                        fullDatax.put("chap", chap.text());


                        last.add(fullDatax);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String loc = getApplicationContext().getFilesDir().getAbsolutePath();
                Book book = new Book();
                book.getMetadata().addTitle("hahaha");
                book.getMetadata().addAuthor(new Author("hahahah"));
                int k = 0;
                for (int i = 0; i < last.size(); i++) {
                    HashMap<String, String> element = last.get(i);

                    File file = new File(loc + "test" + i + ".html");
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    file.getParentFile().mkdirs();
                    try {

                        PrintWriter prw = new PrintWriter(file);
                        prw.write(
                                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\n" +
                                        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                                        "<head>\n" +
                                        "<title>Cover</title>\n" +
                                        "<style type=\"text/css\"> img { max-width: 100%; } </style>\n" +
                                        "</head>\n" +
                                        "<body>\n" +
                                        "<div id=\"cover-image\">\n" +
                                        "<img src=\"/images/cover.jpg\" alt=\"Title\"/>\n" +
                                        "</div>\n" +
                                        "</body>\n" +
                                        "</html>"
                        );
                        prw.close();
                        System.out.println("Successfully wrote to the file.");
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    try {
                        book.addSection(element.get("chap"), new Resource(new FileInputStream(
                                new File(loc + "test" + i + ".html")), "test" + i + ".html"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    EpubWriter epubWriter = new EpubWriter();
                    File file = new File(loc + "finish1.epub");
                    if (!file.exists()) {

                        file.createNewFile();

                    }
                    epubWriter.write(book, new FileOutputStream(file));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return last;
            }
        }.execute();
    }
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