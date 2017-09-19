package com.ytr.injector.demo.di;

import com.ytr.injector.annotation.DataSource;
import com.ytr.injector.annotation.Provides;
import com.ytr.injector.demo.mode.Language;

/**
 * Created by YTR on 2017/9/8.
 */
@DataSource
public class LanguageDataSource {

    @Provides
    public Language provideLanguage(){
        Language language = new Language();
        language.name = "Java";
        language.description = "Java是世界上最好的编程语言";
        return language;
    }
}
