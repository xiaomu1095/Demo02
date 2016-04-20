package com.example.ll.demo02.okhttp;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.TlsVersion;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

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



    public static class synchronousGet{
        private final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception{
            Request request = new Request.Builder()
                    .get()
                    .url("http://www.baidu.com")
                    .tag(1)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//            if (response.body().contentLength())
            System.out.println(response.request().toString());
            BufferedSource buff = response.body().source();
//            response.body().toString();
            System.out.println(buff.readUtf8());
        }
        public static void main(String[] args) {
            try {
                new synchronousGet().run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    //Asynchronous Get(下载亦可)//http://aiwoapp.blog.51cto.com/8677066/1622635[Okio的使用]
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
//                    .url("http://publicobject.com/helloworld.txt")
                    .url("http://202.31.108.37:8080/hankookpda/apkserver/version/apk/1.apk")
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

//                    long contentLength = response.body().contentLength();
//                    float sum = 0;
//                    int len = 0;
                    BufferedSource bs = response.body().source();
                    File apk = new File("d:/test/1.txt");
                    if (!apk.exists()) {
                        apk.createNewFile();
                    }
                    BufferedSink sink = Okio.buffer(Okio.sink(apk));
                    while (sink.writeAll(bs) > 0) {
                        sink.writeAll(bs);
//                        int progress = (int) ((sum / contentLength) * 100);
//                        System.out.println(contentLength);
                    }
                    sink.close();

//                    System.out.println(response.body().string());
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
    (避免上传超过1Mb的参数)
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

//            CacheControl cacheControl = new CacheControl.Builder()
//                    .noCache()
//                    .maxAge(1000, TimeUnit.SECONDS)
//                    .build();
            client = new OkHttpClient.Builder()
                    .cache(cache)
                    .build();
        }

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("http://publicobject.com/helloworld.txt")
//                    .cacheControl(CacheControl.FORCE_CACHE)   //强制缓存
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


    //Canceling a Call
    /*
     Both synchronous and asynchronous calls can be canceled.
     */
    public static class cancelReq{
        private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        private final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
                    .build();

            final long startNanos = System.nanoTime();
            final Call call = client.newCall(request);

            // Schedule a job to cancel the call in 1 second.
            executor.schedule(new Runnable() {
                @Override public void run() {
                    System.out.printf("%.2f Canceling call.%n", (System.nanoTime() - startNanos) / 1e9f);
                    call.cancel();
                    System.out.printf("%.2f Canceled call.%n", (System.nanoTime() - startNanos) / 1e9f);
                }
            }, 1, TimeUnit.SECONDS);

            try {
                System.out.printf("%.2f Executing call.%n", (System.nanoTime() - startNanos) / 1e9f);
                Response response = call.execute();
                System.out.printf("%.2f Call was expected to fail, but completed: %s%n",
                        (System.nanoTime() - startNanos) / 1e9f, response);
            } catch (IOException e) {
                System.out.printf("%.2f Call failed as expected: %s%n",
                        (System.nanoTime() - startNanos) / 1e9f, e);
            }
        }
    }



    //Timeouts
    public static class ConfigureTimeouts{
        private final OkHttpClient client;

        public ConfigureTimeouts() throws Exception {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        }

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("http://httpbin.org/delay/2") // This URL is served with a 2 second delay.
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("Response completed: " + response);
        }
    }



    //Per-call Configuration
    public static class percalConfig{
        private final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("http://httpbin.org/delay/1") // This URL is served with a 1 second delay.
                    .build();

            try {
                // Copy to customize OkHttp for this request.
                OkHttpClient copy = client.newBuilder()
                        .readTimeout(500, TimeUnit.MILLISECONDS)
                        .build();

                Response response = copy.newCall(request).execute();
                System.out.println("Response 1 succeeded: " + response);
            } catch (IOException e) {
                System.out.println("Response 1 failed: " + e);
            }

            try {
                // Copy to customize OkHttp for this request.
                OkHttpClient copy = client.newBuilder()
                        .readTimeout(3000, TimeUnit.MILLISECONDS)
                        .build();

                Response response = copy.newCall(request).execute();
                System.out.println("Response 2 succeeded: " + response);
            } catch (IOException e) {
                System.out.println("Response 2 failed: " + e);
            }
        }
    }



    //Authenticate
    public static class Authenticate {
        private final OkHttpClient client;

        public Authenticate() {
            client = new OkHttpClient.Builder()
                    .authenticator(new Authenticator() {
                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            System.out.println("Authenticating for response: " + response);
                            System.out.println("Challenges: " + response.challenges());
                            String credential = Credentials.basic("jesse", "password1");


                            if (credential.equals(response.request().header("Authorization"))) {
                                return null; // If we already failed with these credentials, don't retry.
                            }
                            if (responseCount(response) >= 3) {
                                return null; // If we've failed 3 times, give up.
                            }


                            return response.request().newBuilder()
                                    .header("Authorization", credential)
                                    .build();
                        }

                        private int responseCount(Response response) {
                            int result = 1;
                            while ((response = response.priorResponse()) != null) {
                                result++;
                            }
                            return result;
                        }

                    })
                    .build();
        }

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("http://publicobject.com/secrets/hellosecret.txt")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());
        }
    }



    //Interceptor
    public static class LogInter implements Interceptor {

        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
//            logger.info(String.format("Sending request %s on %s%n%s",
//                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
//            logger.info(String.format("Received response for %s in %.1fms%n%s",
//                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }


    public static class https{
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .build();
    }



    public static class CertificatePinning{
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .build();
        public CertificatePinning() {
            client = new OkHttpClient.Builder()
                    .certificatePinner(new CertificatePinner.Builder()
                            .add("publicobject.com", "sha1/DmxUShsZuNiqPQsX2Oi9uv2sCnw=")
                            .add("publicobject.com", "sha1/SXxoaOSEzPC6BgGmxAt/EAcsajw=")
                            .add("publicobject.com", "sha1/blhOM3W9V/bVQhsWAcLYwPU6n24=")
                            .add("publicobject.com", "sha1/T5x9IXmcrQ7YuQxXnxoCmeeQ84c=")
                            .build())
                    .build();
        }

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("https://publicobject.com/robots.txt")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            for (Certificate certificate : response.handshake().peerCertificates()) {
                System.out.println(CertificatePinner.pin(certificate));
            }
        }
    }




    //自定义Https证书验证
    public static class CustomTrust{
        private final OkHttpClient client;

        public CustomTrust() {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            try {
                trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[] { trustManager }, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

            client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory) //, trustManager
                    .build();
        }

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("https://publicobject.com/helloworld.txt")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            System.out.println(response.body().string());
        }

        /**
         * Returns an input stream containing one or more certificate PEM files. This implementation just
         * embeds the PEM files in Java strings; most applications will instead read this from a resource
         * file that gets bundled with the application.
         */
        private InputStream trustedCertificatesInputStream() {
            // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
            // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
            // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
            // Typically developers will need to get a PEM file from their organization's TLS administrator.
            String comodoRsaCertificationAuthority = ""
                    + "-----BEGIN CERTIFICATE-----\n"
                    + "MIIF2DCCA8CgAwIBAgIQTKr5yttjb+Af907YWwOGnTANBgkqhkiG9w0BAQwFADCB\n"
                    + "hTELMAkGA1UEBhMCR0IxGzAZBgNVBAgTEkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4G\n"
                    + "A1UEBxMHU2FsZm9yZDEaMBgGA1UEChMRQ09NT0RPIENBIExpbWl0ZWQxKzApBgNV\n"
                    + "BAMTIkNPTU9ETyBSU0EgQ2VydGlmaWNhdGlvbiBBdXRob3JpdHkwHhcNMTAwMTE5\n"
                    + "MDAwMDAwWhcNMzgwMTE4MjM1OTU5WjCBhTELMAkGA1UEBhMCR0IxGzAZBgNVBAgT\n"
                    + "EkdyZWF0ZXIgTWFuY2hlc3RlcjEQMA4GA1UEBxMHU2FsZm9yZDEaMBgGA1UEChMR\n"
                    + "Q09NT0RPIENBIExpbWl0ZWQxKzApBgNVBAMTIkNPTU9ETyBSU0EgQ2VydGlmaWNh\n"
                    + "dGlvbiBBdXRob3JpdHkwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCR\n"
                    + "6FSS0gpWsawNJN3Fz0RndJkrN6N9I3AAcbxT38T6KhKPS38QVr2fcHK3YX/JSw8X\n"
                    + "pz3jsARh7v8Rl8f0hj4K+j5c+ZPmNHrZFGvnnLOFoIJ6dq9xkNfs/Q36nGz637CC\n"
                    + "9BR++b7Epi9Pf5l/tfxnQ3K9DADWietrLNPtj5gcFKt+5eNu/Nio5JIk2kNrYrhV\n"
                    + "/erBvGy2i/MOjZrkm2xpmfh4SDBF1a3hDTxFYPwyllEnvGfDyi62a+pGx8cgoLEf\n"
                    + "Zd5ICLqkTqnyg0Y3hOvozIFIQ2dOciqbXL1MGyiKXCJ7tKuY2e7gUYPDCUZObT6Z\n"
                    + "+pUX2nwzV0E8jVHtC7ZcryxjGt9XyD+86V3Em69FmeKjWiS0uqlWPc9vqv9JWL7w\n"
                    + "qP/0uK3pN/u6uPQLOvnoQ0IeidiEyxPx2bvhiWC4jChWrBQdnArncevPDt09qZah\n"
                    + "SL0896+1DSJMwBGB7FY79tOi4lu3sgQiUpWAk2nojkxl8ZEDLXB0AuqLZxUpaVIC\n"
                    + "u9ffUGpVRr+goyhhf3DQw6KqLCGqR84onAZFdr+CGCe01a60y1Dma/RMhnEw6abf\n"
                    + "Fobg2P9A3fvQQoh/ozM6LlweQRGBY84YcWsr7KaKtzFcOmpH4MN5WdYgGq/yapiq\n"
                    + "crxXStJLnbsQ/LBMQeXtHT1eKJ2czL+zUdqnR+WEUwIDAQABo0IwQDAdBgNVHQ4E\n"
                    + "FgQUu69+Aj36pvE8hI6t7jiY7NkyMtQwDgYDVR0PAQH/BAQDAgEGMA8GA1UdEwEB\n"
                    + "/wQFMAMBAf8wDQYJKoZIhvcNAQEMBQADggIBAArx1UaEt65Ru2yyTUEUAJNMnMvl\n"
                    + "wFTPoCWOAvn9sKIN9SCYPBMtrFaisNZ+EZLpLrqeLppysb0ZRGxhNaKatBYSaVqM\n"
                    + "4dc+pBroLwP0rmEdEBsqpIt6xf4FpuHA1sj+nq6PK7o9mfjYcwlYRm6mnPTXJ9OV\n"
                    + "2jeDchzTc+CiR5kDOF3VSXkAKRzH7JsgHAckaVd4sjn8OoSgtZx8jb8uk2Intzna\n"
                    + "FxiuvTwJaP+EmzzV1gsD41eeFPfR60/IvYcjt7ZJQ3mFXLrrkguhxuhoqEwWsRqZ\n"
                    + "CuhTLJK7oQkYdQxlqHvLI7cawiiFwxv/0Cti76R7CZGYZ4wUAc1oBmpjIXUDgIiK\n"
                    + "boHGhfKppC3n9KUkEEeDys30jXlYsQab5xoq2Z0B15R97QNKyvDb6KkBPvVWmcke\n"
                    + "jkk9u+UJueBPSZI9FoJAzMxZxuY67RIuaTxslbH9qh17f4a+Hg4yRvv7E491f0yL\n"
                    + "S0Zj/gA0QHDBw7mh3aZw4gSzQbzpgJHqZJx64SIDqZxubw5lT2yHh17zbqD5daWb\n"
                    + "QOhTsiedSrnAdyGN/4fy3ryM7xfft0kL0fJuMAsaDk527RH89elWsn2/x20Kk4yl\n"
                    + "0MC2Hb46TpSi125sC8KKfPog88Tk5c0NqMuRkrF8hey1FGlmDoLnzc7ILaZRfyHB\n"
                    + "NVOFBkpdn627G190\n"
                    + "-----END CERTIFICATE-----\n";
            String entrustRootCertificateAuthority = ""
                    + "-----BEGIN CERTIFICATE-----\n"
                    + "MIIEkTCCA3mgAwIBAgIERWtQVDANBgkqhkiG9w0BAQUFADCBsDELMAkGA1UEBhMC\n"
                    + "VVMxFjAUBgNVBAoTDUVudHJ1c3QsIEluYy4xOTA3BgNVBAsTMHd3dy5lbnRydXN0\n"
                    + "Lm5ldC9DUFMgaXMgaW5jb3Jwb3JhdGVkIGJ5IHJlZmVyZW5jZTEfMB0GA1UECxMW\n"
                    + "KGMpIDIwMDYgRW50cnVzdCwgSW5jLjEtMCsGA1UEAxMkRW50cnVzdCBSb290IENl\n"
                    + "cnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA2MTEyNzIwMjM0MloXDTI2MTEyNzIw\n"
                    + "NTM0MlowgbAxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1FbnRydXN0LCBJbmMuMTkw\n"
                    + "NwYDVQQLEzB3d3cuZW50cnVzdC5uZXQvQ1BTIGlzIGluY29ycG9yYXRlZCBieSBy\n"
                    + "ZWZlcmVuY2UxHzAdBgNVBAsTFihjKSAyMDA2IEVudHJ1c3QsIEluYy4xLTArBgNV\n"
                    + "BAMTJEVudHJ1c3QgUm9vdCBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTCCASIwDQYJ\n"
                    + "KoZIhvcNAQEBBQADggEPADCCAQoCggEBALaVtkNC+sZtKm9I35RMOVcF7sN5EUFo\n"
                    + "Nu3s/poBj6E4KPz3EEZmLk0eGrEaTsbRwJWIsMn/MYszA9u3g3s+IIRe7bJWKKf4\n"
                    + "4LlAcTfFy0cOlypowCKVYhXbR9n10Cv/gkvJrT7eTNuQgFA/CYqEAOwwCj0Yzfv9\n"
                    + "KlmaI5UXLEWeH25DeW0MXJj+SKfFI0dcXv1u5x609mhF0YaDW6KKjbHjKYD+JXGI\n"
                    + "rb68j6xSlkuqUY3kEzEZ6E5Nn9uss2rVvDlUccp6en+Q3X0dgNmBu1kmwhH+5pPi\n"
                    + "94DkZfs0Nw4pgHBNrziGLp5/V6+eF67rHMsoIV+2HNjnogQi+dPa2MsCAwEAAaOB\n"
                    + "sDCBrTAOBgNVHQ8BAf8EBAMCAQYwDwYDVR0TAQH/BAUwAwEB/zArBgNVHRAEJDAi\n"
                    + "gA8yMDA2MTEyNzIwMjM0MlqBDzIwMjYxMTI3MjA1MzQyWjAfBgNVHSMEGDAWgBRo\n"
                    + "kORnpKZTgMeGZqTx90tD+4S9bTAdBgNVHQ4EFgQUaJDkZ6SmU4DHhmak8fdLQ/uE\n"
                    + "vW0wHQYJKoZIhvZ9B0EABBAwDhsIVjcuMTo0LjADAgSQMA0GCSqGSIb3DQEBBQUA\n"
                    + "A4IBAQCT1DCw1wMgKtD5Y+iRDAUgqV8ZyntyTtSx29CW+1RaGSwMCPeyvIWonX9t\n"
                    + "O1KzKtvn1ISMY/YPyyYBkVBs9F8U4pN0wBOeMDpQ47RgxRzwIkSNcUesyBrJ6Zua\n"
                    + "AGAT/3B+XxFNSRuzFVJ7yVTav52Vr2ua2J7p8eRDjeIRRDq/r72DQnNSi6q7pynP\n"
                    + "9WQcCk3RvKqsnyrQ/39/2n3qse0wJcGE2jTSW3iDVuycNsMm4hH2Z0kdkquM++v/\n"
                    + "eu6FSqdQgPCnXEqULl8FmTxSQeDNtGPPAUO6nIPcj2A781q0tHuu2guQOHXvgR1m\n"
                    + "0vdXcDazv/wor3ElhVsT/h5/WrQ8\n"
                    + "-----END CERTIFICATE-----\n";
            return new Buffer()
                    .writeUtf8(comodoRsaCertificationAuthority)
                    .writeUtf8(entrustRootCertificateAuthority)
                    .inputStream();
        }

        /**
         * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
         * certificates have not been signed by these certificates will fail with a {@code
         * SSLHandshakeException}.
         *
         * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
         * set. This is useful in development where certificate authority-trusted certificates aren't
         * available. Or in production, to avoid reliance on third-party certificate authorities.
         *
         * <p>See also {@link CertificatePinner}, which can limit trusted certificates while still using
         * the host platform's built-in trust store.
         *
         * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
         *
         * <p>Relying on your own trusted certificates limits your server team's ability to update their
         * TLS certificates. By installing a specific set of trusted certificates, you take on additional
         * operational complexity and limit your ability to migrate between certificate authorities. Do
         * not use custom trusted certificates in production without the blessing of your server's TLS
         * administrator.
         */
        private X509TrustManager trustManagerForCertificates(InputStream in)
                throws GeneralSecurityException {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
            if (certificates.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            }

            // Put the certificates a key store.
            char[] password = "password".toCharArray(); // Any password will work.
            KeyStore keyStore = newEmptyKeyStore(password);
            int index = 0;
            for (Certificate certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate);
            }

            // Use it to build an X509 trust manager.
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        }

        private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
            try {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                InputStream in = null; // By convention, 'null' creates an empty key store.
                keyStore.load(in, password);
                return keyStore;
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public static void main(String... args) throws Exception {
            new CustomTrust().run();
        }
    }





}
