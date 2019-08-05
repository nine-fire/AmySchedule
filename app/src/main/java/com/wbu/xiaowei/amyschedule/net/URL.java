package com.wbu.xiaowei.amyschedule.net;

public class URL {
    /**
     * 登录教务系统的 url
     */
    public static String LOGIN_URL = "http://ids.wbu.edu.cn/authserver/login?service=http://jw.wbu.edu.cn/jsxsd/caslogin.jsp";

    /**
     * 查看成绩的 url
     */
    public static String GET_SCORE_URL = "http://jw.wbu.edu.cn/jsxsd/kscj/cjcx_list";

    /**
     * 查看成绩详情的 url
     */
    public static String GET_SCORE_DETAILS_URL = "http://jw.wbu.edu.cn/jsxsd/kscj/pscj_list.do";

    /**
     * 查看课表的 url
     */
    public static String GET_COURSE_TABLE_URL = "http://jw.wbu.edu.cn/jsxsd/kbcx/kbxx_xzb_ifr";

    /**
     * 获取专业信息的 url
     */
    public static String GET_MAJOR_MAP_URL = "http://jw.wbu.edu.cn/jsxsd/kbcx/getZyByAjax";

    /**
     * 获取学生基本信息的 url
     */
    public static String GET_USER_INFO_URL = "http://jw.wbu.edu.cn/jsxsd/grxx/xsxx";

    /**
     * 站点 url
     */
    public static String BASE_URL = "http://my.wbu.edu.cn/";

    /**
     * 教务系统根 url
     */
    public static String EDU_BASE_URL = "http://jw.wbu.edu.cn/";

    /**
     * 注销的 url
     */
    public static String LOGOUT_URL = "http://ids.wbu.edu.cn/authserver/logout";
}
