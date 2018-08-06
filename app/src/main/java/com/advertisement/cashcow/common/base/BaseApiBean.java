package com.advertisement.cashcow.common.base;

import com.google.gson.Gson;

/**
 * 作者：吴国洪 on 2018/6/25
 * 描述：网络实体基类
 */
public class BaseApiBean {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
