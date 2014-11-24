package com.inftt.inner;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动订餐
 * Created by Sam on 2014/9/30.
 */
public class AutoOrder {

    /**
     * @param args arguments
     */
    public static void main(String[] args) throws Exception {

        //Content returnContent =
        //        Request.Get("http://10.11.32.148:81/super/NJSupper.php").execute().returnContent();
        //System.out.println(returnContent.asString());
        //BasicNameValuePair nameValuePair = new BasicNameValuePair()

        List<NameValuePair> reqBody = new ArrayList<NameValuePair>();
        reqBody.add(new BasicNameValuePair("canId", "11512"));
        reqBody.add(new BasicNameValuePair("canName", "王大中"));

        //URLEncodedUtils.
        Content returnContent = Request.Post("http://10.11.32.148:81/super/NJSupper.php")
                .bodyString(URLEncodedUtils.format(reqBody, "UTF-8"), ContentType.APPLICATION_FORM_URLENCODED)
                .execute().returnContent();
        String contentString = returnContent.asString();
        String result = "";
        if (contentString.startsWith(
                "﻿<script>alert('取消订餐失败, 请检查您输入的工号, 姓名.已经超过可以取消订餐的时间!');</script>")) {

        }
    }
}
