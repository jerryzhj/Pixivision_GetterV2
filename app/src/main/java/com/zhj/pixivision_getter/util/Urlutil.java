package com.zhj.pixivision_getter.util;

/**
 * Created by ZHJ on 2016/10/26 0026.
 * Purpose:pixivsion的地址常量
 */

public class Urlutil {
    //pixivsion的基础地址，后面添加数字就可以到达指定的地址
    public static final String BASEURL = "http://www.pixivision.net/zh/a/";
    //图片下载或者显示的地址,两个都可用
    public static final String IMAGEURL1 = "http://i1.pixiv.net/c/480x960/img-master/img/";
    public static final String IMAGEURL2 = "http://i2.pixiv.net/c/480x960/img-master/img/";
    //经过包装的二段目录，包含所有的插画，按照时间排序。目前共52页
    public static final String PIXIVIONURL = "http://www.pixivision.net/zh/c/illustration/?p=";
    //获取的流为中文流的
    public static final String CHINESE = "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3";
    //解析作者名,图片名,图片地址的字符串
    public static final String PIXIVER_ICON = "aiwsp__uesr-icon";
    public static final String PIXIVER_INERLINK = "gtm__act-ClickUserIcon inner-link";
    public static final String PIXIVER = "aiwsp__user-name";//三行后
    public static final String PICNAME = "aiwsp__title";//两行后
    public static final String PICURL ="aiwsp__illust";
    public static final String DATE = "_date large midlight-gray";//一行后
    public static final String TYPE = "_category-label margin-bottom-small large spotlight";
    //原画，更加清晰，更大。
    public static final String IMAGE_ORE_URL = "http://i2.pixiv.net/img-original/img/";
}
