package com.peanutwolf;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.CookieStore;
        import org.apache.http.client.config.RequestConfig;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.config.Registry;
        import org.apache.http.config.RegistryBuilder;
        import org.apache.http.cookie.*;
        import org.apache.http.impl.client.BasicCookieStore;
        import org.apache.http.impl.client.CloseableHttpClient;
        import org.apache.http.impl.client.HttpClients;
        import org.apache.http.impl.cookie.*;
        import org.apache.http.message.BasicNameValuePair;
        import org.apache.http.protocol.HttpContext;
        import org.apache.http.util.EntityUtils;
        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.Element;
        import org.jsoup.select.Elements;

        import java.io.IOException;
        import java.io.UnsupportedEncodingException;
        import java.util.ArrayList;
        import java.util.Iterator;
        import java.util.List;
        import java.util.regex.Pattern;

public class VKProtoPort implements ProtoPort{
    HttpResponse response;
    HttpEntity entity;
    HttpPost post;
    HttpGet get;
    CookieStore cookieStore;
    Registry<CookieSpecProvider> r;
    RequestConfig requestConfig;
    List<BasicNameValuePair> postParameters;
    String access_token;
    CloseableHttpClient httpClient;

    private String email;
    private String pass;
    private String client_id = "5007256";
    private String scope = "messages";
    private String redirect_uri = "http://oauth.vk.com/blank.html";
    private String display = "popup";
    private String response_type = "token";

    VKProtoPort(String email, String pass){

        this.cookieStore = new BasicCookieStore();

        this.r = RegistryBuilder.<CookieSpecProvider>create()
                .register("myCookieSpec", new MyLenientCookieSpec())
                .build();

        this.requestConfig = RequestConfig.custom()
                .setCookieSpec("myCookieSpec")
                .build();

        this.httpClient =  HttpClients.custom().
                setDefaultCookieSpecRegistry(r).
                setDefaultRequestConfig(requestConfig).
                setDefaultCookieStore(cookieStore).build();

        this.email = email;
        this.pass  = pass;
    }

    private HttpPost createVKLoginPost(String html_page, String email, String pass) throws UnsupportedEncodingException {
        Iterator<Element> it;
        Element el;
        Elements elGroup;
        HttpPost post;

        Document doc = Jsoup.parse(html_page);
        Element elFiFat = doc.select("div.fi_fat").first();
        if(elFiFat == null)
            return null;
        el = elFiFat.select("[action^=https://login.vk.com/]").first();
        if(el == null)
            return null;
        if(el.attr("method").equals("post") && el.attr("action") != null)
            post = new HttpPost(el.attr("action"));
        else
            return null;

        elGroup = el.select("input[type=hidden]");

        it = elGroup.iterator();
        postParameters = new ArrayList<>();
        while(it.hasNext()){
            el = it.next();
            postParameters.add(new BasicNameValuePair(el.attr("name"), el.attr("value")));
        }
        postParameters.add(new BasicNameValuePair("email", email));
        postParameters.add(new BasicNameValuePair("pass", pass));

        post.setEntity(new UrlEncodedFormEntity(postParameters));

        return  post;
    }

    private int parseUnreadMessageCount(String xmlResponse){

        Iterator<Element> it;
        int unreadMsgs = 0;
        String read_state;
        System.out.println("Parsing vk messages.get response");

        Document doc = Jsoup.parse(xmlResponse);
        Elements messages = doc.select("message");
        it = messages.iterator();
        while(it.hasNext()){
            Element msg = it.next();
            msg = msg.select("read_state").first();
            if(msg != null) {
                read_state = msg.text();
                if (read_state.equals("0")) {
                    unreadMsgs++;
                }
            }
        }
        return unreadMsgs;
    }

    public static List<BasicNameValuePair> getQueryMap(String query)
    {
        List<BasicNameValuePair> queryMap = new ArrayList<BasicNameValuePair>();
        String[] params = query.split(Pattern.quote("&"));
        for (String param : params)
        {
            String[] chunks = param.split(Pattern.quote("="));
            String name = chunks[0], value = null;
            if(chunks.length > 1) {
                value = chunks[1];
            }
            queryMap.add(new BasicNameValuePair(name, value));
        }
        return queryMap;
    }

    public int getUnreadMessageCount(String access_token, int count){
        String xmlResponse;
        int unreadMsgsCount = -1;

        if(access_token == null)
            access_token = this.access_token;

        postParameters = new ArrayList<>();

        postParameters.add(new BasicNameValuePair("out", "0"));
        postParameters.add(new BasicNameValuePair("count", count+""));
        postParameters.add(new BasicNameValuePair("access_token", access_token));

        post = new HttpPost("https://api.vk.com/method/messages.get.xml");
        try {
            post.setEntity(new UrlEncodedFormEntity(postParameters));
            response = httpClient.execute(post);
            xmlResponse = EntityUtils.toString(response.getEntity());
            unreadMsgsCount = parseUnreadMessageCount(xmlResponse);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return unreadMsgsCount;
    }

    public int getUnreadMessageCount(){
        String xmlResponse;
        int unreadMsgsCount = 0;
        String read_state;

        postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("filter", "messages"));
        postParameters.add(new BasicNameValuePair("access_token", access_token));

        post = new HttpPost("https://api.vk.com/method/account.getCounters.xml");
        try {
            post.setEntity(new UrlEncodedFormEntity(postParameters));
            response = httpClient.execute(post);
            if(response == null || response.getStatusLine().getStatusCode() != 200)
                return -1;
            xmlResponse = EntityUtils.toString(response.getEntity());
            Document doc = Jsoup.parse(xmlResponse);
            //TODO : process errors from server;
            Element msg = doc.select("messages").first();
            if(msg != null) {
                read_state = msg.text();
                unreadMsgsCount = Integer.parseInt(read_state);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return unreadMsgsCount;
    }

    @Override
    public void connect() throws Error{

        System.out.println("Logging in vk.com...");

        get = new HttpGet("http://oauth.vk.com/authorize" +
                "?client_id=" + client_id +
                "&scope=" + scope +
                "&redirect_uri=" + redirect_uri +
                "&display=" + display +
                "&response_type=" + response_type);

        try {

            response = httpClient.execute(get);
            if (response == null || response.getStatusLine().getStatusCode() != 200) {
                throw new Error("Oauth server unavailable");
            }

            entity = response.getEntity();
            String htmlLoginPage = EntityUtils.toString(entity);
            get.abort();

            post = createVKLoginPost(htmlLoginPage, email, pass);
            response = httpClient.execute(post);
            post.abort();
            if (response == null || response.getStatusLine().getStatusCode() != 302) {
                throw new Error("VK server not found");
            }

            post = new HttpPost(response.getFirstHeader("location").getValue());
            response = httpClient.execute(post);
            post.abort();
            if (response == null || response.getStatusLine().getStatusCode() != 302) {
                throw new Error("Unknown error");
            }

            post = new HttpPost(response.getFirstHeader("location").getValue());
            response = httpClient.execute(post);
            post.abort();
            if (response == null || response.getStatusLine().getStatusCode() != 302) {
                throw new Error("Unknown error");
            }

            get = new HttpGet(response.getFirstHeader("location").getValue());
            postParameters = getQueryMap(get.getURI().getFragment());
            response = httpClient.execute(get);
            get.abort();
            if (response == null || response.getStatusLine().getStatusCode() != 200) {
                throw new Error("Unknown error");
            }

            Iterator<BasicNameValuePair> it = postParameters.iterator();

            while (it.hasNext()){
                BasicNameValuePair bnvp= it.next();
                if(bnvp.getName().equals("access_token")){
                    System.out.println("Successfully logged in vk.com...");
                    access_token = bnvp.getValue();
                    return;
                }
            }

        }catch (IOException ex){
            ex.printStackTrace();
        }

        throw new Error("Access token not received");

    }

    class MyLenientCookieSpec implements CookieSpecProvider {
        String[] customExpiresPattern = {"EEE, dd MMM yyyy HH:mm:ss z"};

        @Override
        public CookieSpec create(HttpContext httpContext) {
            return new DefaultCookieSpec(customExpiresPattern, false);
        }
    }

}



