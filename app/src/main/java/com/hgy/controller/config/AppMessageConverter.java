package com.hgy.controller.config;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.yanzhenjie.andserver.annotation.Converter;
import com.yanzhenjie.andserver.framework.MessageConverter;
import com.yanzhenjie.andserver.framework.body.JsonBody;
import com.yanzhenjie.andserver.framework.body.StringBody;
import com.yanzhenjie.andserver.http.ResponseBody;
import com.yanzhenjie.andserver.util.IOUtils;
import com.yanzhenjie.andserver.util.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * 转换请求的数据
 */
@Converter
public class AppMessageConverter implements MessageConverter {
    @Override
    public ResponseBody convert(@NonNull Object output, @Nullable MediaType mediaType) {
        if (mediaType != null) {
            if (mediaType.getSubtype().equals("json")) {
                return new JsonBody((String) output);
            }
        }
        return new StringBody((String) output);
    }

    @Nullable
    @Override
    public <T> T convert(@NonNull InputStream stream, @Nullable MediaType mediaType, Type type) throws IOException {
        Charset charset = mediaType == null ? null : mediaType.getCharset();
        if (charset == null) {
            return new Gson().fromJson(IOUtils.toString(stream), type);
        }
        return new Gson().fromJson(IOUtils.toString(stream, charset), type);
    }
}
