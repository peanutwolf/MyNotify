package com.peanutwolf;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import sun.org.mozilla.javascript.internal.Token;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class Auth {

    private static HttpClient httpclient = new DefaultHttpClient();

    private static HttpPost post;
    private static HttpResponse response;

    private static String makeRequest(String nextRequest){
        post = new HttpPost(nextRequest);
        try {
            response = httpclient.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        post.abort();
        return response.getFirstHeader("location").getValue();
    }

    /**
     *
     * @param idapp  Your application's identifier
     * @param settings  Access rights requested by your application
     * @param login Your e-mail
     * @param pass  Your password
     * @return P object, which defines access to api
     * @throws IOException
     */
    public static void logIn(String idapp, String settings, String login, String pass) throws IOException {
        try{

            //First request

            String nextRequest = makeRequest("http://api.vkontakte.ru/oauth/authorize?" +
                    "client_id=" + idapp +
                    "&scope=" + settings +
                    "&redirect_uri=http://api.vkontakte.ru/blank.html&display=popup&response_type=token");

            //Second request

            nextRequest = makeRequest(nextRequest);


            String[] tmp = nextRequest.split("&");
            String ip_h = null;
            String to = null;

            for (String i : tmp) {
                if(i.contains("ip_h"))
                    ip_h = i.split("=")[1];
                else if(i.contains("to"))
                    to = i.split("=")[1];

            }


            //Post login and password

            post = new HttpPost("https://login.vk.com/?act=login&soft=1&utf8=1");
            List<NameValuePair> postform = new ArrayList<NameValuePair>();
            postform.add(new BasicNameValuePair("q", "1"));
            postform.add(new BasicNameValuePair("from_host", "oauth.vkontakte.ru"));
            postform.add(new BasicNameValuePair("from_protocol", "http"));
            postform.add(new BasicNameValuePair("ip_h", ip_h));
            postform.add(new BasicNameValuePair("to", to));
            postform.add(new BasicNameValuePair("email", login));
            postform.add(new BasicNameValuePair("pass", pass));

            post.setEntity(new UrlEncodedFormEntity(postform, HTTP.UTF_8));
            response = httpclient.execute(post);
            post.abort();

            nextRequest = response.getFirstHeader("location").getValue();

            //Get page with allow_perm function or page with access_token

            post = new HttpPost(nextRequest);
            response = httpclient.execute(post);
            post.abort();

            String body = EntityUtils.toString(response.getEntity());

            if(body.length() == 0){
                nextRequest = response.getFirstHeader("location").getValue();
                nextRequest = makeRequest(nextRequest);

            } else {
                int start = body.indexOf("https://oauth.vkontakte.ru/grant_access?");
                int end = body.indexOf("token_type=") + "token_type=".length() + 1;

                nextRequest = body.substring(start, end);
                nextRequest = makeRequest(nextRequest);
            }



            String accessToken = nextRequest.split("&")[0].split("#")[1].split("=")[1];
            String id = nextRequest.split("=")[nextRequest.split("=").length - 1];


        } catch (Exception e) {
            //e.printStackTrace();
            //return null;
            throw new IOException();
        }
    }

}