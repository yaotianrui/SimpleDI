package com.ytr.injector.handler;


import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;

/**
 * 注解处理器接口
 * Created by YTR on 2017/8/31.
 */

public interface AnnotationHandler<T> {
    /**
     * 初始化处理环境
     * @param processingEnvironment
     */
    void init(ProcessingEnvironment processingEnvironment);

    /**
     * 处理注解
     * @param roundEnvironment
     * @return
     */
    T handleAnnotations(RoundEnvironment roundEnvironment);

}
