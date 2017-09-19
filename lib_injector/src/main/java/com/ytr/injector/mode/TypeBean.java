package com.ytr.injector.mode;

/**
 * 类型信息
 * Created by YTR on 2017/9/6.
 */
public class TypeBean {
    //包名
    public String packageName;
    //类名
    public String simpleName;

    @Override
    public String toString() {
        return "TypeBean{" +
                "packageName='" + packageName + '\'' +
                ", simpleName='" + simpleName + '\'' +
                '}';
    }
}
