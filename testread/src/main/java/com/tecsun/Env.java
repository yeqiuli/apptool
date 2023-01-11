package com.tecsun;

/**
 * Created by hgy
 * on 2/20/21
 */
public class Env {

    //正式授权
    public static final String SERIAL_LOC = "TravelApp";

    public static final String HOST = "http://47.105.107.174:7877";

    public static final String printContent = "{\"printTitle\":\"茅山二维码入园凭证\",\"printBody\":[{\"sort\":1,\"key\":\"票名：\",\"type\":\"text\",\"value\":\"茅山套票\"},{\"sort\":2,\"key\":\"单价：\",\"type\":\"text\",\"value\":\"12345678\"},{\"sort\":3,\"key\":\"游玩日期：\",\"type\":\"text\",\"value\":\"2022年11月23日\"},{\"sort\":4,\"key\":\"有效期：\",\"type\":\"text\",\"value\":\"当天有效\"},{\"sort\":5,\"key\":\"支付方式：\",\"type\":\"text\",\"value\":\"支付宝扫码支付\"},{\"sort\":6,\"key\":\"一维码\",\"type\":\"barCode\",\"value\":\"99999999\"},{\"sort\":7,\"key\":\"购买时间：\",\"type\":\"text\",\"value\":\"2022-11-2311:00:51\"},{\"sort\":8,\"key\":\"票号：\",\"type\":\"text\",\"value\":\"12345678\"},{\"sort\":9,\"key\":\"二维码\",\"type\":\"qrCode\",\"value\":\"12345678\"}]}";

    public static final String goodsContent = "{\"printTitle\":\"菜单\",\"printBody\":[{\"key\":\"商店ID：\",\"sort\":1,\"type\":\"text\",\"value\":\"1\",\"valueList\":[]},{\"key\":\"商店名称：\",\"sort\":2,\"type\":\"text\",\"value\":\"广州酒家\",\"valueList\":[]},{\"key\":\"地址：\",\"sort\":3,\"type\":\"text\",\"value\":\"天河\",\"valueList\":[]},{\"key\":\"消费：\",\"sort\":4,\"type\":\"text\",\"value\":\"0.03\",\"valueList\":[]},{\"key\":\"电话：\",\"sort\":5,\"type\":\"text\",\"value\":\"13420117978\",\"valueList\":[]},{\"key\":\"商品清单\",\"sort\":6,\"type\":\"goodsList\",\"value\":\"\",\"valueList\":[{\"goodsName\":\"精选美味可口纯天然阿克苏绿豆饼特产\",\"goodsNum\":10,\"goodsPrice\":\"30.01\"},{\"goodsName\":\"阿克苏牛奶\",\"goodsNum\":30,\"goodsPrice\":\"80.01\"}]}]}";

    public static final String KEY_INIT_RESP_NAME = "zim.init.resp";

    // 值为"1000"调用成功
    // 值为"1003"用户选择退出
    // 值为"1004"超时
    // 值为"1005"用户选用其他支付方式
    public static final int CODE_SUCCESS = 1000;
    public  static final int CODE_EXIT = 1003;
    public static final int CODE_TIMEOUT = 1004;
    public static final int CODE_OTHER_PAY = 1005;

    public  static final String TXT_EXIT = "已退出刷脸支付";
    public  static final String TXT_TIMEOUT = "操作超时";
    public  static final String TXT_OTHER_PAY = "已退出刷脸支付";
    public  static final String TXT_OTHER = "抱歉未支付成功，请重新支付";

    //刷脸支付相关
    public  static final String SMILEPAY_CODE_SUCCESS = "10000";
    public static final String SMILEPAY_SUBCODE_LIMIT = "ACQ.PRODUCT_AMOUNT_LIMIT_ERROR";
    public  static final String SMILEPAY_SUBCODE_BALANCE_NOT_ENOUGH = "ACQ.BUYER_BALANCE_NOT_ENOUGH";
    public  static final String SMILEPAY_SUBCODE_BANKCARD_BALANCE_NOT_ENOUGH = "ACQ.BUYER_BANKCARD_BALANCE_NOT_ENOUGH";

    public  static final String SMILEPAY_TXT_LIMIT = "刷脸支付超出限额，请选用其他支付方式";
    public  static final String SMILEPAY_TXT_EBALANCE_NOT_ENOUGH = "账户余额不足，支付失败";
    public  static final String SMILEPAY_TXT_BANKCARD_BALANCE_NOT_ENOUGH = "账户余额不足，支付失败";
    public  static final String SMILEPAY_TXT_FAIL = "抱歉未支付成功，请重新支付";
    public  static final String SMILEPAY_TXT_SUCCESS = "刷脸支付成功";

    //这里三个值请填写自己真实的值
    //应用的签名私钥
    public final static String appKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDIn2+jD1AT1QSuMSEjlMPNqcSEN+zVDR/0l1Ln7T0zouXWhpFt7Pq48knzBMXWZN3i3O/SmfY+arXr4XveQzGXWLWaf9DZeP9vM5AKEZN6sOr7D+6GDQ1kqW78a3HalOv2lEi4YaGiTCMwKOFNhTJWc7jWiPcL7HVPPeMEYLFsTJYuP71gFLCPsKBDA/WPlAjLzrKZgOW90/b2VB7sqzosBNnvNEunuD2a0SgtfTESr5cXJKk2bCcI41XgnHOYR6LQsgcv3hJFG0ru391P/4NEqD0kWl1iwrsp29ENjZAGS9KgaqgoWcAsq1tAm1qIMNOqN6wL0upm6E9WRtDf4+ZrAgMBAAECggEAc7X5ZnvDfNzEyU8OxP3dfPYooPW8hBkA0EqjB59+SBTxKS0OHe+roBYmbqLXFFOy4lD5hWdFbjSP3o97qPLqXFdOzuBfbb7q3JFJx3ThscbukIVc1jYa/m7202GJFkAEZICb/LRazEfLl9BMOjDTmqc9+YafrmHgOyb/k5vNWR9A9EuPSU3P/D0von+m3Uy31Co/SBrooidjQ+9oHGW2LZyOyFMilUTxus7KUXZQXVWc9z64oLsp823lLkKXIaq+AhsxMdH2ll740twt2pamrtDtz5cUEdcgvYpHrptkd3sI0Dc1nnpnB/w/XVDv4E7V6vWU9Goeer28uBlA5E+zkQKBgQDz1ByBepg2X9Pj/4hZ34YgBn+ZOBTS0aZ/0W7YaQH0J8es2opErOY6qGM5JIm3DJVTRnMg/SnIW0U6bFOwIjLGisJVgyWMYhkJfS5u+Ypon/wC/E2OVqT/KywTVbKiyvRGDJqshx4bb9y5a3xiYomKyanMM+UtVrULUXpSjzgdgwKBgQDSozKanq8ihyri1E0Cf0KWEFDUdaMk7IQea4XvsmMVn6ju7cM1as2FcAgiCAvI8PqS1TaFY77fuHYxGT9jMxnF19hjI4acyiY7EL4LLIZB5Pta0UeF+Ic8Kb1887KtHadsiMJgtQr6e87YtSUqPipqXpAjFSA/8izxlT1Yrq9m+QKBgB/+/Z6gD8lS2eNZbPMeOeOJ9sSMb28aw9aSDRGo9fCsJ6+vHeXVKoEYB60/9jJnHFFJNWJm1kzMbVE1VeYPFsqm+slt5Ed/t/ley/EUHe2M0O0zXpoHFK/HYolG/aIPL+7hSIrLUFAZZcWPLUtb0uC7heSeCrzgW/GYR4z97kcxAoGAG5bUofv25IbfzyYPkCwrBIzOiYV/AO8ZvwJGYMb0w3hOID10PCLpGHk8F3kGUiN+Lo1Ovtn5vkPi9ztfWhbvYUKMTS/yCcdQipv+yGfkjez4H0UaISoUTIULFLq2xIYFz+l5zEDqVzSwOyI+4/GQjK4mIvI5bybBTENH7b2zq8ECgYBt8Wl+wfSwAHoXJSB5up5e31ga9E/dM4tlJSuC5D2stKCzUo0sEGIsk/iuMqcsRJBkuWFuEdoJjPo1jF2lFDI8v+sQjXXd1Y3DrvK19W9pPsSNE61xZw0YxPpptp5hCf2ETt1IQC35yRYmz2N4gU2bRjO4sq66KtBtpQej3zs+VA==";
    //商户id
    public final static String partnerId = "2088441511496940";
    //    public final static String partnerId = "2088441511496941";
    //应用的appId
    public final static String appId = "2021003167606600";
//    public final static String appId = "2021003167606601";

}
