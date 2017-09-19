package com.ytr.injector.mode;


/**
 * 函数的参数信息
 * Created by YTR on 2017/9/1.
 */
public class ParameterBean {
    // 参数类型信息
    public TypeBean typeBean;
    //参数名
    public String parameterName;

    @Override
    public String toString() {
        return "ParameterBean{" +
                "typeBean=" + typeBean +
                ", parameterName='" + parameterName + '\'' +
                '}';
    }
}
