package com.ytr.injector.mode;


import java.util.Arrays;
import java.util.List;

/**
 * Created by YTR on 2017/8/30.
 */
public class InjectorBean {
    //@Injector注解所在类
    public TypeBean attachTypeBean;
    //@Injector 注解中的 dataSource值。
    public TypeBean[] dataSource;
    //@Injector注解所在类里面的方法信息
    public List<MethodBean> methodBeanList;

    @Override
    public String toString() {
        return "InjectorBean{" +
                ", attachTypeBean='" + attachTypeBean + '\'' +
                ", dataSource=" + Arrays.toString(dataSource) +
                ", methodBeanList=" + methodBeanList +
                '}';
    }
}
