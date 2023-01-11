package com.tecsun.sixse.security;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        boolean flag = IDCardCheck.idIsOk("440102200204242316");
        boolean flag1 = IDCardCheck.idIsOk("532522199201152417");
        System.out.println("身份证状态：" + flag);
        System.out.println("身份证状态1：" + flag1);
    }
}