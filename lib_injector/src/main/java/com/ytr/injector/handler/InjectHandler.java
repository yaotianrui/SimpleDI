package com.ytr.injector.handler;

import com.ytr.injector.annotation.Inject;
import com.ytr.injector.mode.FieldBean;
import com.ytr.injector.mode.TypeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by YTR on 2017/8/31.
 */

public class InjectHandler extends AbstractHandler<Map<String,List<FieldBean>>> {

    public InjectHandler(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public Map<String,List<FieldBean>> handleAnnotations(RoundEnvironment roundEnvironment) {
        Map<String,List<FieldBean>> map = new HashMap<>();
        Set<? extends Element> injectElements = roundEnvironment.getElementsAnnotatedWith(Inject.class);
        for (Element injectElement : injectElements) {
            //获取被注解的变量元素 因为@Inject注解的Target是Field ,所以使用该注解的元素一定是变量元素(VariableElement)
            VariableElement fieldElement = (VariableElement) injectElement;
            //获取被注解的变量名称
            String fieldName = fieldElement.getSimpleName().toString();
            //获取变量的类型元素
            TypeMirror typeMirror = fieldElement.asType();
            TypeElement fieldTypeElement = (TypeElement) mTypeUtils.asElement(typeMirror);
            String fieldTypePackageName = mElementUtils.getPackageOf(fieldTypeElement).getQualifiedName().toString();
            //获取变量类型元素的名称
            String fieldTypeName = fieldTypeElement.getSimpleName().toString();
            //获取变量的修饰符 public protected private defualt static final
            Set<Modifier> modifiers = fieldElement.getModifiers();
            //获取变量所属的类元素
            TypeElement classTypeElement = (TypeElement) injectElement.getEnclosingElement();
            //获取变量元素所属的类名
            String className = classTypeElement.getSimpleName().toString();
            //获取类所属的包元素并获取包名
            String packageName = mElementUtils.getPackageOf(classTypeElement).getQualifiedName().toString();

            String key = packageName + "."+ className;
            List<FieldBean> list = map.get(key);

            FieldBean fieldBean = new FieldBean();
            fieldBean.attachType = new TypeBean();
            fieldBean.attachType.simpleName = className;
            fieldBean.attachType.packageName = packageName;
            fieldBean.fieldName = fieldName;
            fieldBean.fieldType = new TypeBean();
            fieldBean.fieldType.simpleName = fieldTypeName;
            fieldBean.fieldType.packageName = fieldTypePackageName;
            fieldBean.modifier = "";
            if(modifiers.contains(Modifier.PUBLIC)){
                fieldBean.modifier += "public ";
            } else if (modifiers.contains(Modifier.PROTECTED)){
                fieldBean.modifier += "protected ";
            } else if (modifiers.contains(Modifier.PRIVATE)){
                fieldBean.modifier += "private ";
            } else if (modifiers.contains(Modifier.DEFAULT)){
                fieldBean.modifier += "";
            }else {
                fieldBean.modifier += "public ";
            }
            if(modifiers.contains(Modifier.STATIC)){
                fieldBean.modifier += "static ";
            }
            if(modifiers.contains(Modifier.FINAL)){
                fieldBean.modifier += "final ";
            }

            if(list != null){
                list.add(fieldBean);
            }else{
                list = new ArrayList<>();
                list.add(fieldBean);
                map.put(key,list);
            }
        }
        return map;
    }


}
