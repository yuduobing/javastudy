package com.hzw.api.yreturn;

import com.hzw.common.constant.HttpStatus;
import com.hzw.common.utils.StringUtils;

import java.util.HashMap;

/**
 * @Auther yuduobin
 * @Email 1510557673@qq.com
 * @Create $(YEAR)-$(MONTH)-$(DAY)
 */
//java 枚举类里，每个bai枚举值都表示一个du特定的对象，你zhi定义成整形变量肯dao定不行，只能是引用zhuan类型的而且shu有指向具体的对象才可以
public class ResultCode extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public ResultCode() {
    }

    public ResultCode(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public ResultCode(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    public static ResultCode success(String msg, Object data) {
        return new ResultCode(HttpStatus.SUCCESS, msg, data);
    }

    public static ResultCode success(Object data) {

        return new ResultCode(0, "success", data);
    }

    public static ResultCode success(int code, String msg) {

        return new ResultCode(code, msg);
    }

    public static ResultCode error(int code, String msg) {

        return new ResultCode(code, msg);
    }
}
