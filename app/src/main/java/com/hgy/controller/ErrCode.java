package com.hgy.controller;

public class ErrCode {
    public static final int SUCCESS = 2000;//成功
    public static final int URL_ERR = -2000;//接口地址不合法
    public static final int OTHER_ERR = -2006;//其他错误
    public static final int PWD_NOT_NULL = -2002;//密码为空
    public static final int PWD_ERR = -2003;//密码错误
    public static final int PWD_NO_INIT = -2004;//请初始化密码
    public static final int KEY_ERR = -2005;//输入参数有问题
    public static final int DATA_NULL = -2001;//未查询到相关数据
    public static final int NO_LOG = -2007;//不存在日志
    public static final int NO_OP = -2008;//不支持操作
}
