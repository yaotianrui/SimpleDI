package com.ytr.injector.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ytr.injector.annotation.Inject;
import com.ytr.injector.demo.di.DISplashInjector;
import com.ytr.injector.demo.di.LanguageDataSource;
import com.ytr.injector.demo.mode.Language;

public class SecondActivity extends AppCompatActivity {

    @Inject
    public Language language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        DISplashInjector.builder().setLanguageDataSource(new LanguageDataSource()).build().inject(this);
    }
}
