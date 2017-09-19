package com.ytr.injector.handler;

import com.ytr.injector.annotation.DataSource;
import com.ytr.injector.annotation.Provides;
import com.ytr.injector.mode.MethodBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Created by YTR on 2017/8/31.
 */

public class DataSourceHandler extends AbstractHandler<Map<String,List<MethodBean>>> {

    public DataSourceHandler(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public Map<String,List<MethodBean>> handleAnnotations(RoundEnvironment roundEnvironment) {
        Map<String,List<MethodBean>> map = new HashMap<>();
        Set<? extends Element> dataSourceElements = roundEnvironment.getElementsAnnotatedWith(DataSource.class);
        for (Element dataSourceElement: dataSourceElements) {
            TypeElement typeElement = (TypeElement) dataSourceElement;
            String className = typeElement.getSimpleName().toString();
            String packageName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            String key = packageName + "." + className;
            List<MethodBean> provideMethodList = new ArrayList<>();
            List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
            for (Element element:enclosedElements) {
                if(element instanceof ExecutableElement){
                    Provides annotation = element.getAnnotation(Provides.class);
                    if(annotation != null){//说明就是带有@Provides注解的方法
                        provideMethodList.add(new MethodBean((ExecutableElement) element,mElementUtils,mTypeUtils));
                    }
                }
            }
            map.put(key,provideMethodList);
        }
        return map;
    }
}
