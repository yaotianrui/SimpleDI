package com.ytr.injector.demo.di;

import com.ytr.injector.annotation.Injector;
import com.ytr.injector.demo.SecondActivity;

/**
 * Created by YTR on 2017/9/8.
 */
@Injector(dataSource = LanguageDataSource.class)
public interface SplashInjector {
    void inject(SecondActivity splashActivity);
}
