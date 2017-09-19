package com.ytr.injector.handler;

import com.ytr.injector.DIProcessor;
import com.ytr.injector.annotation.Injector;
import com.ytr.injector.mode.InjectorBean;
import com.ytr.injector.mode.MethodBean;
import com.ytr.injector.mode.TypeBean;
import com.ytr.injector.utils.PrintUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 * Created by YTR on 2017/8/31.
 */

public class InjectorHandler extends AbstractHandler<List<InjectorBean>> {
    public InjectorHandler(ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
    }

    @Override
    public List<InjectorBean> handleAnnotations(RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Injector.class);
        List<InjectorBean> list = new ArrayList<>();
        for (Element elment : elements) {
            InjectorBean injectorBean = new InjectorBean();
            TypeElement typeElement = (TypeElement) elment;
            String className = typeElement.getSimpleName().toString();
            String packageName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            injectorBean.attachTypeBean = new TypeBean();
            injectorBean.attachTypeBean.packageName = packageName;
            injectorBean.attachTypeBean.simpleName = className;
            Injector injector = typeElement.getAnnotation(Injector.class);
            try {
                //会报错误 Attempt to access Class objects for TypeMirrors , 抛出TypeMirrorException,我们可以利用这个异常获取到类的包名和类名。
                Class[] ds = injector.dataSource();
            }catch (MirroredTypesException e){
                PrintUtils.printInfo(DIProcessor.OUTPUT_FILE_NAME,"MirroredTypesException:-----"+e.getMessage());
                List<? extends TypeMirror> typeMirrors = e.getTypeMirrors();
                injectorBean.dataSource = new TypeBean[typeMirrors.size()];
                for(int i = 0 ;i < typeMirrors.size();i++){
                    TypeMirror classTypeMirror = typeMirrors.get(i);
                    TypeElement classTypeElement = (TypeElement)mTypeUtils.asElement(classTypeMirror);
                    TypeBean dataSource = new TypeBean();
                    //获取canonicalName
                    String canonicalName = classTypeElement.getQualifiedName().toString();
                    dataSource.packageName = canonicalName.substring(0,canonicalName.lastIndexOf("."));
                    //获取simple name
                    dataSource.simpleName = classTypeElement.getSimpleName().toString();
                    injectorBean.dataSource[i] = dataSource;
                }
            }
            List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
            injectorBean.methodBeanList = new ArrayList<>();
            for (int i = 0 ; i < enclosedElements.size(); i++){
                ExecutableElement executableElement = (ExecutableElement) enclosedElements.get(i);
                injectorBean.methodBeanList.add(new MethodBean(executableElement,mElementUtils,mTypeUtils));
            }
            list.add(injectorBean);
        }
        return list;
    }
}
