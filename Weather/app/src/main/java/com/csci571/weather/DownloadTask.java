package com.csci571.weather;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by Zhuo on 2015/11/24.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {

    private String datastr="";


    protected String doInBackground(String... urlStr) {

        int len;
        try {
            URL url = new URL(urlStr[0]);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();

            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            byte[] bs = new byte[1024];
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(bs)) != -1) {
                String str = new String(bs, 0, len);
                sb.append(str);
            }
            datastr = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datastr;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
        Log.i("asynctask",result);
    }

}


