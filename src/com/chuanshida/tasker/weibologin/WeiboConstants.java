package com.chuanshida.tasker.weibologin;

/**
 * 该类定义了新浪微博授权时所需要的参数。
 * 
 * @author huanghua
 * 
 */
public class WeiboConstants {
    public static final String TAG = "weibo_login";

    /** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static final String APP_KEY = "2935574131";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
     * 
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
     * 
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

    /** 注销地址（URL） */
    public static final String REVOKE_OAUTH_URL = "https://api.weibo.com/oauth2/revokeoauth2";
    /** GET 请求方式 */
    public static final String HTTPMETHOD_GET = "GET";
    /** POST 请求方式 */
    public static final String HTTPMETHOD_POST = "POST";
    /** HTTP 参数 */
    public static final String KEY_ACCESS_TOKEN = "access_token";
    /** 访问微博服务接口的地址 */
    public static final String API_SERVER = "https://api.weibo.com/2";
    public static final String API_BASE_URL = API_SERVER + "/users/show.json";
    public static final String USER_NAME_HEAD = "weibo_";
}
