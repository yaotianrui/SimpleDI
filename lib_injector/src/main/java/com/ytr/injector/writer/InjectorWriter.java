package com.ytr.injector.writer;

import com.ytr.injector.mode.FieldBean;
import com.ytr.injector.mode.InjectorBean;
import com.ytr.injector.mode.MethodBean;
import com.ytr.injector.mode.ParameterBean;
import com.ytr.injector.mode.TypeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by YTR on 2017/9/1.
 * Injector实现类的java文件生成器
 */

public class InjectorWriter extends AbstractWriter{

    private static final String PREFIX = "DI";

    public InjectorWriter(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    public void generateClass(Map<String, List<FieldBean>> injectMap, List<InjectorBean> injectorList, Map<String, List<MethodBean>> providesMap) {
        //遍历所有的包含@Injector的类(接口)信息
        for (int i = 0; i < injectorList.size(); i++) {
            InjectorBean mInjectorBean = injectorList.get(i);
            //找到要导入的包信息
            List<TypeBean> packageList = new ArrayList<>();
            //判断类的成员变量是否需要导报
            for (TypeBean typeBean : mInjectorBean.dataSource) {
                if (typeBean.packageName != null && !mInjectorBean.attachTypeBean.packageName.equals(typeBean.packageName)) {
                    packageList.add(typeBean);
                }
            }

            for (MethodBean methodBean : mInjectorBean.methodBeanList) {
                //判断方法的返回类型是否需要导包
                if (methodBean.returnType.packageName != null && !"java.lang".equals(methodBean.returnType.packageName) && !mInjectorBean.attachTypeBean.packageName.equals(methodBean.returnType.packageName)) {
                    packageList.add(methodBean.returnType);
                }
                for (ParameterBean parameterBean : methodBean.parameterList) {
                    //判断方法的形式参数是否需要导包
                    if (parameterBean.typeBean.packageName != null && !"java.lang".equals(parameterBean.typeBean.packageName) && !mInjectorBean.attachTypeBean.packageName.equals(parameterBean.typeBean.packageName)) {
                        packageList.add(parameterBean.typeBean);
                    }
                }
            }
            //创建类构造器
            ClassBuilder builder = builder();
            //构造包名
            builder.buildPackage(mInjectorBean.attachTypeBean.packageName);
            //构造导包部分
            for (TypeBean typeBean : packageList) {
                builder.buildImport(typeBean);
            }
            //构造注释部分
            builder.buildCopyright()
                    //构造类的开始部分（该类的类名就是在Injector接口名的基础上拼前缀DI，并实现改Injector接口）
                    .buildClassStart("public final ", PREFIX + mInjectorBean.attachTypeBean.simpleName, null, new String[]{mInjectorBean.attachTypeBean.simpleName}, false);
            List<FieldBean> fieldList = new ArrayList<>();
            if (mInjectorBean.dataSource != null) {
                //遍历改Injector对应的数据源DataSource,生成对应的成员变量
                for (TypeBean typeBean : mInjectorBean.dataSource) {
                    FieldBean fieldBean = new FieldBean();
                    // 变量的修饰符为private
                    fieldBean.modifier = "private ";
                    //变量的类型
                    fieldBean.fieldType = typeBean;
                    //变量名为m + 变量的类名
                    fieldBean.fieldName = "m" + typeBean.simpleName;
                    //把Injector实现类的变量存起来（因为构造函数和内部类Builder也要用）
                    fieldList.add(fieldBean);
                    //拼变量
                    builder.buildField(fieldBean, false);
                }
            }
            //TODO 实现接口类中的方法
            //1.遍历Injector接口中的方法
            for (MethodBean methodBean : mInjectorBean.methodBeanList) {
                builder.buildMethodStrat(methodBean, true, false);
                if (methodBean.parameterList != null && methodBean.parameterList.size() == 1 && "void".equals(methodBean.returnType.simpleName)) {
                    //2.找到方法中的参数
                    ParameterBean parameterBean = methodBean.parameterList.get(0);
                    //3.找到参数的类型
                    TypeBean parameteType = parameterBean.typeBean;
                    //4.找到类中使用了@Inject注解的变量
                    List<FieldBean> list = injectMap.get(parameteType.packageName + "." + parameteType.simpleName);
                    if (list != null) {
                        for (FieldBean fieldBean : list) {
                            //5.给变量赋值
                            builder.buildMethodContent("\t\t")
                                    .buildMethodContent(parameterBean.parameterName)
                                    .buildMethodContent(".")
                                    .buildMethodContent(fieldBean.fieldName)
                                    .buildMethodContent(" = ");
                            //TODO 怎么赋值？怎么建立关联关系？
                            //1.找到需要被赋值的变量的类型
                            String fieldTypeName = fieldBean.fieldType.packageName + "." + fieldBean.fieldType.simpleName;
                            //2.遍历该Injctor中所有的DataSource
                            for (FieldBean dataSourceBean : fieldList){
                                String key = dataSourceBean.fieldType.packageName + "." +dataSourceBean.fieldType.simpleName;
                                //3.根据DataSource的包名类名找到该DataSource中的所有包含@Provides注解的方法；
                                List<MethodBean> methodList = providesMap.get(key);
                                 //4.遍历所有的方法
                                for (MethodBean providesMethodBean : methodList){
                                    //5如果变量的类型和方法的返回值类型对应则调用此方法给变量赋值。
                                    if(fieldTypeName.equals(providesMethodBean.returnType.packageName+"."+providesMethodBean.returnType.simpleName)){
                                        builder.buildMethodContent(dataSourceBean.fieldName)
                                                .buildMethodContent(".")
                                                .buildMethodContent(providesMethodBean.methodName)
                                                .buildMethodContent("();\n");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                //写方法的结尾
                builder.buildMethodEnd(false);
            }
            // TODO 写Injector实现类的构造函数
            //构造函数中需要传入内部构造器Builder
            List<ParameterBean> parameterList = new ArrayList<>();
            ParameterBean parameterBean = new ParameterBean();
            parameterBean.typeBean = new TypeBean();
            parameterBean.typeBean.simpleName = "Builder";
            parameterBean.parameterName = "pBuilder";
            parameterList.add(parameterBean);
            //拼接构造函数的开头
            builder.buildConstructor(builder.className, parameterList, false);
            //拼接构造函数的方法体 ，非空判断
            builder.buildMethodContent("\t\tassert ")
                    .buildMethodContent(parameterBean.parameterName)
                    .buildMethodContent(" != null;\n");
            //在构造函数类给成员变量（DataSource）赋值
            for (FieldBean fieldBean : fieldList) {
                 //成员变量的值就从Builder中拿
                 builder.buildMethodContent("\t\tthis.")
                        .buildMethodContent(fieldBean.fieldName)
                        .buildMethodContent(" = ")
                        .buildMethodContent(parameterBean.parameterName)
                        .buildMethodContent(".")
                        .buildMethodContent(fieldBean.fieldName)
                        .buildMethodContent(";\n");
            }
            //拼接构造函数的结尾 （Incjiector实现类的构造函数结束了）
            builder.buildMethodEnd(false);
            //拼接内部类（构造器类）的开头
            builder.buildClassStart("public static ", "Builder", true);
            for (TypeBean typeBean : mInjectorBean.dataSource) {
                //拼接构造器中的成员变量
                FieldBean fieldBean = new FieldBean();
                fieldBean.fieldType = typeBean;
                fieldBean.modifier = "private ";
                String fieldName = "m" + typeBean.simpleName;
                fieldBean.fieldName = fieldName;
                builder.buildField(fieldBean, true);
                //拼接构造器中的方法
                MethodBean methodBean = new MethodBean();
                methodBean.modifier = "public ";
                methodBean.returnType = new TypeBean();
                methodBean.methodName = "set" + typeBean.simpleName;
                methodBean.returnType.simpleName = "Builder";
                methodBean.parameterList = new ArrayList<>();
                ParameterBean innerParameterBean = new ParameterBean();
                innerParameterBean.typeBean = typeBean;
                String parameterName = "p" + typeBean.simpleName;
                innerParameterBean.parameterName = parameterName;
                methodBean.parameterList.add(innerParameterBean);

                builder.buildMethodStrat(methodBean, false, true)
                        .buildMethodContent("\t\t\tthis.")
                        .buildMethodContent(fieldName)
                        .buildMethodContent(" = ")
                        .buildMethodContent(parameterName)
                        .buildMethodContent(";")
                        .buildMethodContent("\n")
                        .buildMethodContent("\t\t\treturn this;\n")
                        .buildMethodEnd(true);
            }

            //拼接Injector的构造器类的build()方法,返回一个Injector的实现类
            MethodBean methodBean = new MethodBean();
            methodBean.modifier = "public ";
            methodBean.methodName = "build";
            methodBean.returnType = new TypeBean();
            methodBean.returnType.simpleName = builder.className;
            builder.buildMethodStrat(methodBean, false, true)
                    .buildMethodContent("\t\t\treturn new ")
                    .buildMethodContent(builder.className)
                    .buildMethodContent("(this);\n")
                    .buildMethodEnd(true);

            builder.buildClassEnd(true);
            //对外部替狗一个静态的builder方法,改方法返回构造器类
            MethodBean build_methodBean = new MethodBean();
            build_methodBean.modifier = "public static ";
            build_methodBean.methodName = "builder";
            build_methodBean.returnType = new TypeBean();
            build_methodBean.returnType.simpleName = "Builder";
            builder.buildMethodStrat(build_methodBean, false, false);
            builder.buildMethodContent("\t\treturn new Builder();\n");
            builder.buildMethodEnd(false);
            //类结束了
            builder.buildClassEnd(false)
                    .build();//生成Java 文件
        }
    }

}
