package com.ytr.injector.handler;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by YTR on 2017/8/31.
 */

public abstract class AbstractHandler<T> implements AnnotationHandler {
    protected ProcessingEnvironment mProcessingEnvironment;
    protected Filer mFiler;
    protected Types mTypeUtils;
    protected Elements mElementUtils;
    protected Messager mMessager;

    public AbstractHandler(ProcessingEnvironment processingEnvironment){
        init(processingEnvironment);
    }

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        mProcessingEnvironment = processingEnvironment;
        mFiler = mProcessingEnvironment.getFiler();
        mTypeUtils = mProcessingEnvironment.getTypeUtils();
        mElementUtils = mProcessingEnvironment.getElementUtils();
        mMessager = mProcessingEnvironment.getMessager();
    }

    @Override
    public abstract T handleAnnotations(RoundEnvironment roundEnvironment);
}
