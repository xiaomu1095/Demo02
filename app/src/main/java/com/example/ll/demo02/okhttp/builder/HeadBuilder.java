package com.example.ll.demo02.okhttp.builder;

import com.example.ll.demo02.okhttp.OkHttpUtils;
import com.example.ll.demo02.okhttp.request.OtherRequest;
import com.example.ll.demo02.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers).build();
    }
}
