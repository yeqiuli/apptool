/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.hikvision.artemis.sdk.util;

import com.hikvision.artemis.sdk.Response;
import com.hikvision.artemis.sdk.constant.*;
import com.tecsun.network.utils.LogUntil;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

import httpclient.org.apache.http.ClientConnectionManager;
import httpclient.org.apache.http.client.ClientProtocolException;
import httpclient.org.apache.http.client.HttpClient;
import httpclient.org.apache.http.client.entity.UrlEncodedFormEntity;
import httpclient.org.apache.http.client.methods.HttpDelete;
import httpclient.org.apache.http.client.methods.HttpGet;
import httpclient.org.apache.http.client.methods.HttpPost;
import httpclient.org.apache.http.client.methods.HttpPut;
import httpclient.org.apache.http.conn.scheme.Scheme;
import httpclient.org.apache.http.conn.scheme.SchemeRegistry;
import httpclient.org.apache.http.impl.client.DefaultHttpClient;
import httpcode.org.apache.commons.codec.binary.StringUtils;
import httpcore.org.apache.http.Header;
import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.NameValuePair;
import httpcore.org.apache.http.entity.ByteArrayEntity;
import httpcore.org.apache.http.entity.StringEntity;
import httpcore.org.apache.http.message.BasicNameValuePair;
import httpcore.org.apache.http.params.CoreConnectionPNames;
import httpcore.org.apache.http.ssl.SSLSocketFactory;


/**
 * Http工具类
 */
public class HttpUtil {
    /**
     * HTTP GET
     *
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpGet(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.GET, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        Response r = null;

        try {
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

            HttpGet get = new HttpGet(initUrl(host, path, querys));

            for (Map.Entry<String, String> e : headers.entrySet()) {
                get.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
            }
            HttpResponse rp = httpClient.execute(get);
            r = convert(rp);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }

        return r;
    }


    public static Response httpImgGet(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.GET, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        Response r = null;

        try {
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

            HttpGet get = new HttpGet(initUrl(host, path, querys));

            for (Map.Entry<String, String> e : headers.entrySet()) {
                get.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
            }
            HttpResponse rp = httpClient.execute(get);
            r = convertImg(rp);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }

        return r;
    }

    /**
     * HTTP POST表单
     *
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param bodys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPost(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, Map<String, String> bodys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_FORM);

        headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, bodys, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPost post = new HttpPost(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        UrlEncodedFormEntity formEntity = buildFormEntity(bodys);
        if (formEntity != null) {
            post.setEntity(formEntity);
        }

        return convert(httpClient.execute(post));
    }

    /**
     * HTTP POST表单
     *
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param bodys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpImgPost(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, Map<String, String> bodys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        headers.put(HttpHeader.HTTP_HEADER_CONTENT_TYPE, ContentType.CONTENT_TYPE_FORM);

        headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, bodys, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPost post = new HttpPost(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        UrlEncodedFormEntity formEntity = buildFormEntity(bodys);
        if (formEntity != null) {
            post.setEntity(formEntity);
        }

        return convertImg(httpClient.execute(post));
    }


    /**
     * Http POST 字符
     *
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param body
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPost(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, String body, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {

        String contentType = headers.get(HttpHeader.HTTP_HEADER_CONTENT_TYPE);
        if (ContentType.CONTENT_TYPE_FORM.equals(contentType)) {//postString发鿁content-type为表单的请求，请求的body字符串必须为key-value组成的串，类似a=1&b=2这种形式
            Map<String, String> paramMap = strToMap(body);

            String modelDatas = paramMap.get("modelDatas"); //这个base64的字符串经过url编码，签名时先解砿(这个是针对大数据某个请求包含url编码的参敿)，对某个请求包含的参数的特殊处理
            if (StringUtils.isNotBlank(modelDatas)) {
                paramMap.put("modelDatas", URLDecoder.decode(modelDatas));
            }

            headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, paramMap, signHeaderPrefixList, appKey, appSecret);
        } else {
            headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);
        }

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPost post = new HttpPost(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, Constants.ENCODING));

        }

        return convert(httpClient.execute(post));
    }


    /**
     * Http POST 字符丿
     *
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param body
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpImgPost(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, String body, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {

        String contentType = headers.get(HttpHeader.HTTP_HEADER_CONTENT_TYPE);
        if (ContentType.CONTENT_TYPE_FORM.equals(contentType)) {//postString发鿁content-type为表单的请求，请求的body字符串必须为key-value组成的串，类似a=1&b=2这种形式
            Map<String, String> paramMap = strToMap(body);

            String modelDatas = paramMap.get("modelDatas"); //这个base64的字符串经过url编码，签名时先解砿(这个是针对大数据某个请求包含url编码的参敿)，对某个请求包含的参数的特殊处理
            if (StringUtils.isNotBlank(modelDatas)) {
                paramMap.put("modelDatas", URLDecoder.decode(modelDatas));
            }

            headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, paramMap, signHeaderPrefixList, appKey, appSecret);
        } else {
            headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);
        }

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPost post = new HttpPost(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (StringUtils.isNotBlank(body)) {
            post.setEntity(new StringEntity(body, Constants.ENCODING));

        }

        return convertImg(httpClient.execute(post));
    }

    /**
     * HTTP POST 字节数组
     *
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param bodys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPost(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, byte[] bodys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.POST, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPost post = new HttpPost(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            post.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (bodys != null) {
            post.setEntity(new ByteArrayEntity(bodys));
        }

        return convert(httpClient.execute(post));
    }

    /**
     * HTTP PUT 字符丿
     *
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param body
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPut(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, String body, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.PUT, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPut put = new HttpPut(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (StringUtils.isNotBlank(body)) {
            put.setEntity(new StringEntity(body, Constants.ENCODING));

        }

        return convert(httpClient.execute(put));
    }

    /**
     * HTTP PUT字节数组
     *
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param bodys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpPut(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, byte[] bodys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.PUT, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpPut put = new HttpPut(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            put.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        if (bodys != null) {
            put.setEntity(new ByteArrayEntity(bodys));
        }

        return convert(httpClient.execute(put));
    }

    /**
     * HTTP DELETE
     *
     * @param host
     * @param path
     * @param connectTimeout
     * @param headers
     * @param querys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static Response httpDelete(String host, String path, int connectTimeout, Map<String, String> headers, Map<String, String> querys, List<String> signHeaderPrefixList, String appKey, String appSecret)
            throws Exception {
        headers = initialBasicHeader(HttpMethod.DELETE, path, headers, querys, null, signHeaderPrefixList, appKey, appSecret);

        HttpClient httpClient = wrapClient(host);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout(connectTimeout));

        HttpDelete delete = new HttpDelete(initUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            delete.addHeader(e.getKey(), MessageDigestUtil.utf8ToIso88591(e.getValue()));
        }

        return convert(httpClient.execute(delete));
    }

    /**
     * 构建FormEntity
     *
     * @param formParam
     * @return
     * @throws UnsupportedEncodingException
     */
    private static UrlEncodedFormEntity buildFormEntity(Map<String, String> formParam)
            throws UnsupportedEncodingException {
        if (formParam != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : formParam.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, formParam.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, Constants.ENCODING);
            formEntity.setContentType(ContentType.CONTENT_TYPE_FORM);
            return formEntity;
        }

        return null;
    }

    public static String initUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append(Constants.SPE3);
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append(Constants.SPE4);
                        sbQuery.append(URLEncoder.encode(query.getValue(), Constants.ENCODING));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append(Constants.SPE5).append(sbQuery);
            }
        }

        return sbUrl.toString();
    }


    /**
     * 初始化基硿Header
     *
     * @param method
     * @param path
     * @param headers
     * @param querys
     * @param bodys
     * @param signHeaderPrefixList
     * @param appKey
     * @param appSecret
     * @return
     * @throws MalformedURLException
     */
    private static Map<String, String> initialBasicHeader(String method, String path,
                                                          Map<String, String> headers,
                                                          Map<String, String> querys,
                                                          Map<String, String> bodys,
                                                          List<String> signHeaderPrefixList,
                                                          String appKey, String appSecret)
            throws MalformedURLException {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }

        headers.put(SystemHeader.X_CA_TIMESTAMP, String.valueOf(new Date().getTime()));
        headers.put(SystemHeader.X_CA_NONCE, UUID.randomUUID().toString());
        headers.put(SystemHeader.X_CA_KEY, appKey);
        String sign = SignUtil.sign(appSecret, method, path, headers, querys, bodys, signHeaderPrefixList);
        LogUntil.e("sign=" + sign);
        headers.put(SystemHeader.X_CA_SIGNATURE, sign);

        return headers;
    }

    /**
     * 读取超时时间
     *
     * @param timeout
     * @return
     */
    private static int getTimeout(int timeout) {
        if (timeout == 0) {
            return Constants.DEFAULT_TIMEOUT;
        }

        return timeout;
    }

    private static Response convert(HttpResponse response) throws IOException {
        Response res = new Response();

        if (null != response) {
            res.setStatusCode(response.getStatusLine().getStatusCode());
            for (Header header : response.getAllHeaders()) {
                res.setHeader(header.getName(), MessageDigestUtil.iso88591ToUtf8(header.getValue()));
            }

            res.setContentType(res.getHeader("Content-Type"));
            res.setRequestId(res.getHeader("X-Ca-Request-Id"));
            res.setErrorMessage(res.getHeader("X-Ca-Error-Message"));
            if (response.getEntity() == null) {
                res.setBody(null);
            } else {
                res.setBody(readStreamAsStr(response.getEntity().getContent()));
            }
        } else {
            //服务器无回应
            res.setStatusCode(500);
            res.setErrorMessage("No Response");
        }

        return res;
    }


    private static Response convertImg(HttpResponse response) throws IOException {
        Response res = new Response();
        String newUrl;

        if (null != response) {
            if (302 == response.getStatusLine().getStatusCode()) {
                Header header = response.getFirstHeader("location");
                newUrl = header.getValue();
                HttpGet httpget = new HttpGet(newUrl);
                HttpClient httpClient = wrapClient(httpget.getURI().getScheme() + "://" + httpget.getURI().getHost());
                HttpResponse response1 = httpClient.execute(httpget);
                response = response1;
                int code = response1.getStatusLine().getStatusCode();
            }

            res.setStatusCode(response.getStatusLine().getStatusCode());
            for (Header header : response.getAllHeaders()) {
                res.setHeader(header.getName(), MessageDigestUtil.iso88591ToUtf8(header.getValue()));
            }

            res.setContentType(res.getHeader("Content-Type"));
            res.setRequestId(res.getHeader("X-Ca-Request-Id"));
            res.setErrorMessage(res.getHeader("X-Ca-Error-Message"));
            res.setResponse(response);
        } else {
            res.setStatusCode(500);
            res.setErrorMessage("No Response");
        }

        return res;
    }


    /**
     * 将流转换为字符串
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String readStreamAsStr(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WritableByteChannel dest = Channels.newChannel(bos);
        ReadableByteChannel src = Channels.newChannel(is);
        ByteBuffer bb = ByteBuffer.allocate(4096);

        while (src.read(bb) != -1) {
            bb.flip();
            dest.write(bb);
            bb.clear();
        }
        src.close();
        dest.close();

        return new String(bos.toByteArray(), Constants.ENCODING);
    }

    /**
     * 将流转换为字符串
     *
     * @param src
     * @return
     * @throws IOException
     */
    public static String readImageAsStr(byte[] src) throws IOException {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String inStream2String(InputStream src) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = src.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        return new String(baos.toByteArray());
    }


    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }

    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = null;

            /** JDK1.6支持TLS的版本为最高为TLSv1.1
             ** 如果不指定具体版本，使用默认版本建立连接，JDK1.8默认版本TLSv1.2；JDK1.7默认版本TLSv1.1；JDK1.6默认版本TLSv1
             ** TLSv1、TLSv1.1 陆续禁用,导致JDK6无法支持https调用
             */
            String jdkVersion = System.getProperty("java.specification.version");
            if (Double.parseDouble(jdkVersion) >= Constants.JDK_VERSION) {
                ctx = SSLContext.getInstance("TLSv1.2");
            } else {
                ctx = SSLContext.getInstance("TLS");
            }
            //   SSLContext ctx = SSLContext.getInstance("SSLv3");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }

                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Map<String, String> strToMap(String str) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            String[] params = str.split("&");
            for (String param : params) {
                String[] a = param.split("=");
                map.put(a[0], a[1]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }


}
