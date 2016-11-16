package com.architjn.myapp.api;

import android.util.Log;

import com.architjn.myapp.BuildConfig;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.net.URI;

public class ServerInteractor {
    public static final int TIMEOUTCONNECTION = 30000;


    public static String httpServerPost(String jsonString, String URL) {
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), TIMEOUTCONNECTION); // Timeout
        // Limit
        HttpResponse httpresponse;
        String response = "";
        try {
            HttpPost post = new HttpPost(URL);
            Log.e("jsonRequest:", jsonString);
            Log.e("URL:", URL);
            StringEntity se = new StringEntity(jsonString);
            post.setHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8");
            post.setHeader("Accept", "application/json");

            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8"));
            post.setEntity(se);
            // response = client.execute(post);
            httpresponse = client.execute(post);
            response = EntityUtils.toString(httpresponse.getEntity()).trim();

            // Log.e("result", response);
            /* Checking response */
            // if(httpresponse!=null)
            // {
            // InputStream in = httpresponse.getEntity().getContent(); //Get the
            // data in the entity
            // }
        } catch (Exception e) {
            e.printStackTrace();
            response = e.getMessage();
            return null;

        }
        if (BuildConfig.DEBUG)
            Log.e("result:", "-" + String.valueOf(response));
        return response;
    }

    public static String httpGetRequestToServer(String URLs) {

        //http://125.454.54.4/ksm/login?username=abc&password=12324
        HttpClient client = new DefaultHttpClient();

        HttpGet request = new HttpGet();

        Log.w("url", URLs);
        try {
            URI url = new URI(URLs.replace(" ", "%20"));
            request.setURI(url);
            HttpResponse response = client.execute(request);
            String responseEntity = EntityUtils.toString(response.getEntity());
            Log.w("server resp:", responseEntity);
            return responseEntity;
        } catch (Exception e) {
            return "false";
        }
    }
}
