package com.ytr.injector.demo.di;

import com.ytr.injector.annotation.Injector;
import com.ytr.injector.demo.MainActivity;
import com.ytr.injector.demo.di.MainDataSource;

/**
 * Created by YTR on 2017/8/30.
 */
@Injector(dataSource = {MainDataSource.class,LanguageDataSource.class})
public interface MainInjector {
    void inject(MainActivity mainActivity);
}
