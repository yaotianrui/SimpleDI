package com.ytr.injector.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by YTR on 2017/9/1.
 */
public class MethodBean {
    //所在类的信息
    public TypeBean attachType;
    //方法名称
    public String methodName;
    //方法修饰符
    public String modifier;
    //返回值的类信息
    public TypeBean returnType;
    //参数列表信息
    public List<ParameterBean> parameterList;

    @Override
    public String toString() {
        return "MethodBean{" +
                "attachType=" + attachType +
                ", methodName='" + methodName + '\'' +
                ", modifier='" + modifier + '\'' +
                ", returnType=" + returnType +
                ", parameterList=" + parameterList +
                '}';
    }

    public MethodBean(){
    }

    public MethodBean(ExecutableElement methodElement, Elements mElementUtils, Types mTypeUtils){
        TypeElement typeElement = (TypeElement) methodElement.getEnclosingElement();
        String className = typeElement.getSimpleName().toString();
        String packageName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        //获取被注解的方法名称
        String methodName = methodElement.getSimpleName().toString();
        //获取方法的返回值类型
        TypeMirror returnTypeMirror = methodElement.getReturnType();
        TypeElement returnTypeElement = (TypeElement) mTypeUtils.asElement(returnTypeMirror);
        if(returnTypeElement != null) {
            String retrunClass = returnTypeElement.getSimpleName().toString();
            //获取方法返回值类型的包名
            String returnPackage = mElementUtils.getPackageOf(returnTypeElement).getQualifiedName().toString();
            this.returnType = new TypeBean();
            this.returnType.simpleName = retrunClass;
            this.returnType.packageName = returnPackage;
        }else{
            TypeKind kind = returnTypeMirror.getKind();
            this.returnType = new TypeBean();
            if (kind == TypeKind.BYTE) {
                this.returnType.simpleName = "byte";
            } else if (kind == TypeKind.INT) {
                this.returnType.simpleName = "int";
            } else if (kind == TypeKind.BOOLEAN) {
                this.returnType.simpleName = "boolean";
            } else if (kind == TypeKind.LONG) {
                this.returnType.simpleName = "long";
            } else if (kind == TypeKind.SHORT) {
                this.returnType.simpleName = "short";
            } else if (kind == TypeKind.CHAR) {
                this.returnType.simpleName = "char";
            } else if (kind == TypeKind.FLOAT) {
                this.returnType.simpleName = "float";
            } else if (kind == TypeKind.DOUBLE) {
                this.returnType.simpleName = "double";
            } else if (kind == TypeKind.VOID) {
                this.returnType.simpleName = "void";
            }
        }

        this.attachType = new TypeBean();
        this.attachType.simpleName = className;
        this.attachType.packageName = packageName;

        this.methodName = methodName;



        Set<Modifier> modifiers = methodElement.getModifiers();
        this.modifier = "";
        if(modifiers != null) {
            if (modifiers.contains(Modifier.PUBLIC)) {
                this.modifier += "public ";
            } else if (modifiers.contains(Modifier.PROTECTED)) {
                this.modifier += "protected ";
            } else if (modifiers.contains(Modifier.PRIVATE)) {
                this.modifier += "private ";
            } else {
                this.modifier += "";
            }
            if (modifiers.contains(Modifier.STATIC)) {
                this.modifier += "static ";
            }
            if (modifiers.contains(Modifier.FINAL)) {
                this.modifier += "final ";
            }
        }

        List<? extends VariableElement> parameters = methodElement.getParameters();
        if(parameters != null) {
            this.parameterList = new ArrayList<>();
            for (int i = 0; i < parameters.size(); i++) {
                VariableElement element = parameters.get(i);
                ParameterBean parameterBean = new ParameterBean();
                parameterBean.parameterName = element.getSimpleName().toString();
                TypeMirror typeMirror = element.asType();
                TypeElement pTypeElement = (TypeElement) mTypeUtils.asElement(typeMirror);
                String pTypeClassName = pTypeElement.getSimpleName().toString();
                String pTypePackageName = mElementUtils.getPackageOf(pTypeElement).getQualifiedName().toString();
                parameterBean.typeBean = new TypeBean();
                parameterBean.typeBean.simpleName = pTypeClassName;
                parameterBean.typeBean.packageName = pTypePackageName;
                this.parameterList.add(parameterBean);
            }
        }
    }
}
