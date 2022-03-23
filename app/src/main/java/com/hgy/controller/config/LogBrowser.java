package com.hgy.controller.config;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tecsun.network.utils.LogUntil;
import com.yanzhenjie.andserver.annotation.Config;
import com.yanzhenjie.andserver.error.NotFoundException;
import com.yanzhenjie.andserver.framework.body.FileBody;
import com.yanzhenjie.andserver.framework.website.BasicWebsite;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.ResponseBody;
import com.yanzhenjie.andserver.util.Assert;
import com.yanzhenjie.andserver.util.DigestUtils;
import com.yanzhenjie.andserver.util.MediaType;
import com.yanzhenjie.andserver.util.ObjectUtils;
import com.yanzhenjie.andserver.util.Patterns;
import com.yanzhenjie.andserver.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Config
public class LogBrowser extends BasicWebsite implements Patterns {
    private final String mRootPath;

    public LogBrowser(String rootPath) {
        Assert.isTrue(!StringUtils.isEmpty(rootPath), "The rootPath cannot be empty.");
        Assert.isTrue(rootPath.matches(Patterns.PATH), "The format of [%s] is wrong, it should be like [/root/project].");
        this.mRootPath = rootPath;
    }


    @Override
    public boolean intercept(@NonNull HttpRequest request) {
        String httpPath = request.getPath();//  /faceImg/1.jpg
        File source = findHttpPathResource(httpPath);
        LogUntil.e("httpPath:" + httpPath);
        return source != null;
    }

    /**
     * 判断是否是正确的照片路径
     *
     * @param httpPath
     * @return
     */
    private File findHttpPathResource(@NonNull String httpPath) {//修改后的路径检查
        LogUntil.e("请求路径：" + httpPath);
        if (!httpPath.contains("log")) {
            return getDefault();
        }

        String path = mRootPath + httpPath;
        File root = new File(path);
        return root.exists() ? root : getDefault();
    }


    private File getDefault() {
        return null;
    }

    @Override
    public String getETag(@NonNull HttpRequest request) {
        String httpPath = request.getPath();
        File resource = findHttpPathResource(httpPath);
        if (resource != null) {
            String tag = resource.getAbsolutePath() + resource.lastModified();
            return DigestUtils.md5DigestAsHex(tag);
        }
        return null;
    }

    @Override
    public long getLastModified(@NonNull HttpRequest request) {
        String httpPath = request.getPath();
        File resource = findHttpPathResource(httpPath);
        if (resource != null) {
            return resource.lastModified();
        }
        return -1;
    }


    @NonNull
    @Override
    public ResponseBody getBody(@NonNull HttpRequest request, @NonNull HttpResponse response) throws IOException {
        String httpPath = request.getPath();
        LogUntil.e("ResponseBody_httpPath:" + httpPath);
        File resource = findHttpPathResource(httpPath);
        if (resource == null) {
            throw new NotFoundException(httpPath);
        }
        if (resource.isDirectory()) {
            File tempFile = File.createTempFile("file_browser", ".html");
            OutputStream outputStream = new FileOutputStream(tempFile);

            String folderName = resource.getName();
            String prefix = String.format(FOLDER_HTML_PREFIX, folderName, folderName);
            outputStream.write(prefix.getBytes("utf-8"));

            File[] children = resource.listFiles();
            if (!ObjectUtils.isEmpty(children)) {
                for (File file : children) {
                    String filePath = file.getAbsolutePath();
                    int rootIndex = filePath.indexOf(mRootPath);
                    String subHttpPath = filePath.substring(rootIndex + mRootPath.length());
                    subHttpPath = addStartSlash(subHttpPath);
                    String fileItem = String.format(FOLDER_ITEM, subHttpPath, file.getName());
                    outputStream.write(fileItem.getBytes("utf-8"));
                }
            }

            outputStream.write(FOLDER_HTML_SUFFIX.getBytes("utf-8"));
            return new FileBody(tempFile) {
                @Nullable
                @Override
                public MediaType contentType() {
                    MediaType mimeType = super.contentType();
                    if (mimeType != null) {
                        mimeType = new MediaType(mimeType.getType(), mimeType.getSubtype(), StandardCharsets.UTF_8);
                    }
                    return mimeType;
                }
            };
        } else {
            return new FileBody(resource);
        }
    }

    private static final String FOLDER_HTML_PREFIX = "<!DOCTYPE html><html><head><meta http-equiv=\"content-type\" " +
            "content=\"text/html; charset=utf-8\"/> <meta name=\"viewport\" content=\"width=device-width, " +
            "initial-scale=1, user-scalable=no\"><metaname=\"format-detection\" content=\"telephone=no\"/> " +
            "<title>%1$s</title><style>.center_horizontal{margin:0 auto;text-align:center;} *,*::after,*::before " +
            "{box-sizing: border-box;margin: 0;padding: 0;}a:-webkit-any-link {color: -webkit-link;cursor: auto;" +
            "text-decoration: underline;}ul {list-style: none;display: block;list-style-type: none;-webkit-margin-before:" +
            " 1em;-webkit-margin-after: 1em;-webkit-margin-start: 0px;-webkit-margin-end: 0px;-webkit-padding-start: " +
            "40px;}li {display: list-item;text-align: -webkit-match-parent;margin-bottom: 5px;}</style></head><body><h1 " +
            "class=\"center_horizontal\">%2$s</h1><ul>";
    private static final String FOLDER_ITEM = "<li><a href=\"%1$s\">%2$s</a></li>";
    private static final String FOLDER_HTML_SUFFIX = "</ul></body></html>";
}
