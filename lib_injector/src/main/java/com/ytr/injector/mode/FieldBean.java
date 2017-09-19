package com.ytr.injector.mode;

/**
 * 成员变量的信息
 * Created by YTR on 2017/8/31.
 */
public class FieldBean {
    //所属的类信息
    public TypeBean attachType;
    //变量的类型信息
    public TypeBean fieldType;
    //变量名称
    public String fieldName;
    //变量的访问修饰符、状态修饰符等
    public String modifier;

    @Override
    public String toString() {
        return "FieldBean{" +
                "attachType=" + attachType +
                ", fieldType=" + fieldType +
                ", fieldName='" + fieldName + '\'' +
                ", modifier='" + modifier + '\'' +
                '}';
    }
}
