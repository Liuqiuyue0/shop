package demo2.com.example.liuqiuyue.shop;


/**
 * Created by liuqiuyue on 2017/4/9.
 * URL
 */

public class Contants {
    public static final String CAMPAIGN_ID = "campaign_id";
    public static final String WARE = "ware";


    public static final String DES_KEY = "Cniao5_123456";  //密码加密

    public static final String USER_JSON = "user_json";
    public static final String TOKEN = "token";


    public static final int REQUEST_CODE = 0;

    public static class API {
        public static final String BASE_URL = "http://112.124.22.238:8081/course_api/";

        public static final String BANNER_HOME = BASE_URL + "banner/query?type=1";   //主页轮播
        public static final String CAMPAINGN_Home = BASE_URL + "campaign/recommend";   //主页活动
        public static final String WARES_CAMPAIGN_LIST = BASE_URL + "wares/campaign/list";  //主页活动子列表
        public static final String WARES_DETAIL = BASE_URL + "wares/detail.html";   //商品详情

        public static final String WARES_HOT = BASE_URL + "wares/hot";    //热门页面列表


        public static final String WARES_LIST = BASE_URL + "wares/list";     //分类页面热门商品列表
        public static final String BANNER = BASE_URL + "banner/query";          //分类页面轮播
        public static final String CATEGORY_LIST = BASE_URL + "category/list";   //分类页面一级列表

        public static final String LOGIN = BASE_URL + "auth/login";     //用户登录 id mobi email usename logo_url
        public static final String USER_DETAIL = BASE_URL + "user/get?id=1";

        public static final String REG = BASE_URL + "auth/reg";  //注册账号
        public static final String ADDRESS_LIST = BASE_URL + "addr/list";
        public static final String ADDRESS_DEL = BASE_URL + "addr/del";  //删除地址
        public static final String ADDRESS_CREATE = BASE_URL + "addr/create";   //添加地址
        public static final String ADDRESS_UPDATE = BASE_URL + "addr/update";  //编辑地址


        public static final String ORDER_LIST = BASE_URL + "order/list";  //订单列表 权限问题 无法使用

        public static final String FAVORITE_LIST = BASE_URL + "favorite/list";
        public static final String FAVORITE_CREATE = BASE_URL + "favorite/create";  //添加到收藏

    }
}
