package com.ytr.injector;

import com.ytr.injector.annotation.DataSource;
import com.ytr.injector.annotation.Inject;
import com.ytr.injector.annotation.Injector;
import com.ytr.injector.handler.AnnotationHandler;
import com.ytr.injector.handler.DataSourceHandler;
import com.ytr.injector.handler.InjectHandler;
import com.ytr.injector.handler.InjectorHandler;
import com.ytr.injector.writer.InjectorWriter;
import com.ytr.injector.mode.FieldBean;
import com.ytr.injector.mode.InjectorBean;
import com.ytr.injector.mode.MethodBean;
import com.ytr.injector.utils.PrintUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;


/**
 * Created by YTR on 2017/8/31.
 */
@SupportedAnnotationTypes( {"com.ytr.injector.annotation.Inject",
                            "com.ytr.injector.annotation.Provides",
                            "com.ytr.injector.annotation.DataSource",
                            "com.ytr.injector.annotation.Injector"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class DIProcessor extends AbstractProcessor{

    public static final String OUTPUT_FILE_NAME = "/Users/ytr_mac/Desktop/Code/SimpleDI/lib_injector/info/log.txt";

    private Map<Class,AnnotationHandler> handlerMap = new HashMap<>();

    private InjectorWriter defualtWriter;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        registAnnnotationHandler(Inject.class , new InjectHandler(processingEnvironment));
        registAnnnotationHandler(Injector.class , new InjectorHandler(processingEnvironment));
        registAnnnotationHandler(DataSource.class , new DataSourceHandler(processingEnvironment));
        defualtWriter = new InjectorWriter(processingEnvironment);

    }

    private void registAnnnotationHandler(Class clazz, AnnotationHandler annotationHandler){
        handlerMap.put(clazz,annotationHandler);
    }

    private <T extends AnnotationHandler>T getHandler(Class clazz){
        return (T)handlerMap.get(clazz);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        InjectHandler handler = getHandler(Inject.class);
        Map<String,List<FieldBean>> injectMap = handler.handleAnnotations(roundEnvironment);
        PrintUtils.printInfo(OUTPUT_FILE_NAME,injectMap.toString());

        InjectorHandler injectorHandler = getHandler(Injector.class);
        List<InjectorBean> injectorList = injectorHandler.handleAnnotations(roundEnvironment);
        PrintUtils.printInfo(OUTPUT_FILE_NAME,injectorList.toString());

        DataSourceHandler dataSourceHandler = getHandler(DataSource.class);
        Map<String,List<MethodBean>> providesMap = dataSourceHandler.handleAnnotations(roundEnvironment);
        PrintUtils.printInfo(OUTPUT_FILE_NAME,providesMap.toString());

        defualtWriter.generateClass(injectMap,injectorList,providesMap);
        return true;
    }

}
