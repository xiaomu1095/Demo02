package com.example.ll.demo02.okhttp;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by Administrator on 2016/4/19.
 */
public class MyDemo {


    //Synchronous Get(不能超过1Mb)
    public static class get {
        final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("http://publicobject.com/helloworld.txt")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            System.out.println(response.body().string());//(使用string（）方法时候，返回的内容不能超过1Mb)
        }

    }


    //Asynchronous Get
    public static class synchronous {
        private final OkHttpClient client = new OkHttpClient();


        public static void main(String[] args) {
            try {
                new synchronous().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("http://publicobject.com/helloworld.txt")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(response.body().string());
                }
            });
        }
    }


    //Accessing Headers
    /*
    When writing request headers, use header(name, value) to set the only occurrence of name to value. If there are existing values,
    they will be removed before the new value is added. Use addHeader(name, value) to add a header without removing the headers already present.

    When reading response a header, use header(name) to return the last occurrence of the named value. Usually this is also the only occurrence!
    If no value is present, header(name) will return null. To read all of a field's values as a list, use headers(name).
     */
    public static class header {
        private final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("https://api.github.com/repos/square/okhttp/issues")
                    .header("User-Agent", "OkHttp Headers.java")
                    .addHeader("Accept", "application/json; q=0.5")
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            for (String h : response.headers().names()) {
                System.out.println(h + "：" + response.header(h));
            }

            System.out.println("----------------------");
            for (String h : request.headers().names()) {
                System.out.println(h + "：" + request.header(h));
            }
//            System.out.println("Server: " + response.header("Server"));
//            System.out.println("Date: " + response.header("Date"));
//            System.out.println("Vary: " + response.headers("Vary"));
        }

        public static void main(String[] args) {
            try {
                new header().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //Posting a String
    /*
    Because the entire request body is in memory simultaneously, avoid posting large (greater than 1 MiB) documents using this API.
     */
    public static class postString{
        public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

        private final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception {
            String postBody = ""
                    + "Releases\n"
                    + "--------\n"
                    + "\n"
                    + " * _1.0_ May 6, 2013\n"
                    + " * _1.1_ June 15, 2013\n"
                    + " * _1.2_ August 11, 2013\n";

            Request request = new Request.Builder()
                    .url("https://api.github.com/markdown/raw")
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }



    //Post Streaming
    public static class postStream{
        public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

        private final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception {
            RequestBody requestBody = new RequestBody() {
                @Override public MediaType contentType() {
                    return MEDIA_TYPE_MARKDOWN;
                }

                @Override public void writeTo(BufferedSink sink) throws IOException {
                    sink.writeUtf8("Numbers\n");
                    sink.writeUtf8("-------\n");
                    for (int i = 2; i <= 997; i++) {
                        sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                    }
                }

                private String factor(int n) {
                    for (int i = 2; i < n; i++) {
                        int x = n / i;
                        if (x * i == n) return factor(x) + " × " + i;
                    }
                    return Integer.toString(n);
                }
            };

            Request request = new Request.Builder()
                    .url("https://api.github.com/markdown/raw")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }



    //Posting a File
    public static class postFile{
        public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

        private final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception {
            File file = new File("README.md");

            Request request = new Request.Builder()
                    .url("https://api.github.com/markdown/raw")
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }



    //Posting form parameters
    public static class postForm{
        private final OkHttpClient client = new OkHttpClient();

        public static void main(String[] args){
            try {
                new postForm().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() throws Exception {
            RequestBody formBody = new FormBody.Builder()
                    .add("search", "Jurassic Park")
                    .build();
            Request request = new Request.Builder()
                    .url("https://en.wikipedia.org/w/index.php")
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }



    //Posting a multipart request
    public static class multipartReq{
        private static final String IMGUR_CLIENT_ID = "...";
        private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        private final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception {
            // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("title", "Square Logo")
                    .addFormDataPart("image", "logo-square.png", RequestBody.create(MEDIA_TYPE_PNG, new File("d://Desert.jpg")))
                    .addFormDataPart("image", "logo-square1.png", RequestBody.create(MEDIA_TYPE_PNG, new File("d://Desert.jpg")))
                    .addFormDataPart("image", "logo-square2.png", RequestBody.create(MEDIA_TYPE_PNG, new File("d://Desert.jpg")))
//                    .addFormDataPart("image", "logo-square.png",
//                            RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                    .url("https://api.imgur.com/3/image")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
        public static void main(String[] args){
            try {
                new multipartReq().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    //Parse a JSON Response With Gson
    public static class parseJSON{
        private final OkHttpClient client = new OkHttpClient();
        private final Gson gson = new Gson();

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("https://api.github.com/gists/c2a7c39532239ff261be")
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Gist gist = gson.fromJson(response.body().charStream(), Gist.class);
            for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue().content);
            }
        }

        public static void main(String[] args){
            try {
                new parseJSON().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        static class Gist {
            Map<String, GistFile> files;
        }

        static class GistFile {
            String content;
        }
    }



    //Response Caching

    /*To cache responses, you'll need a cache directory that you can read and write to, and a limit on the cache's size.
    The cache directory should be private, and untrusted applications should not be able to read its contents!

    It is an error to have multiple caches accessing the same cache directory simultaneously. Most applications should call new OkHttpClient() exactly once,
     configure it with their cache, and use that same instance everywhere. Otherwise the two cache instances will stomp on each other,
     corrupt the response cache, and possibly crash your program.(OkHttp应该使用单例模式！！！！！)

    Response caching uses HTTP headers for all configuration. You can add request headers like Cache-Control: max-stale=3600 and OkHttp's cache will honor them.
    Your webserver configures how long responses are cached with its own response headers, like Cache-Control: max-age=9600.
     There are cache headers to force a cached response, force a network response, or force the network response to be validated with a conditional GET.
    */
    public static class CacheResponse{
        private final OkHttpClient client;

        public CacheResponse(File cacheDirectory) throws Exception {
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(cacheDirectory, cacheSize);

            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .build();
        }

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("http://publicobject.com/helloworld.txt")
                    .build();

            Response response1 = client.newCall(request).execute();
            if (!response1.isSuccessful()) throw new IOException("Unexpected code " + response1);

            String response1Body = response1.body().string();
            System.out.println("Response 1 response:          " + response1);
            System.out.println("Response 1 cache response:    " + response1.cacheResponse());
            System.out.println("Response 1 network response:  " + response1.networkResponse());

            Response response2 = client.newCall(request).execute();
            if (!response2.isSuccessful()) throw new IOException("Unexpected code " + response2);

            String response2Body = response2.body().string();
            System.out.println("Response 2 response:          " + response2);
            System.out.println("Response 2 cache response:    " + response2.cacheResponse());
            System.out.println("Response 2 network response:  " + response2.networkResponse());

            System.out.println("Response 2 equals Response 1? " + response1Body.equals(response2Body));
        }

        public static void main(String[] args){
            try {
                new CacheResponse(new File("D://test//")).run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
