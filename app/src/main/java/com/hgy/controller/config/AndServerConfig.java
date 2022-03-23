package com.hgy.controller.config;

import android.content.Context;

import com.hgy.tool.MyFileUtil;
import com.yanzhenjie.andserver.annotation.Config;
import com.yanzhenjie.andserver.framework.config.Multipart;
import com.yanzhenjie.andserver.framework.config.WebConfig;
import com.yanzhenjie.andserver.framework.website.AssetsWebsite;

import java.io.File;

@Config
public class AndServerConfig implements WebConfig {
    @Override
    public void onConfig(Context context, Delegate delegate) {
        delegate.addWebsite(new LogBrowser(MyFileUtil.rootFile));
        delegate.addWebsite(new LogFileBrowser(MyFileUtil.rootFile));
        delegate.addWebsite(new AssetsWebsite(context, "/web"));
        delegate.setMultipart(Multipart.newBuilder()
                .allFileMaxSize(1024 * 1024 * 50) // 50M
                .fileMaxSize(1024 * 1024 * 50) // 50M
                .maxInMemorySize(1024 * 10) // 1024 * 10 bytes
                .uploadTempDir(new File(context.getCacheDir(), "_server_upload_cache_"))
                .build());
    }
}
