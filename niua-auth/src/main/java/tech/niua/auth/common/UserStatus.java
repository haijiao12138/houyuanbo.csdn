package tech.niua.auth.common;

/**
 * @author wangzhen
 * @title: UserStatus
 * @projectName niua_easy_parent
 * @description: 用户状态
 * @date 2020/5/29 10:56 下午
 */
public enum UserStatus {

    OK(0, "正常"), DISABLE(1, "停用"), DELETED(2, "删除");

    private final int code;
    private final String info;

    UserStatus(int code, String info){
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
