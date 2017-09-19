package com.ytr.injector.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.ytr.injector.annotation.Inject;
import com.ytr.injector.demo.di.DIMainInjector;
import com.ytr.injector.demo.di.LanguageDataSource;
import com.ytr.injector.demo.di.MainDataSource;
import com.ytr.injector.demo.mode.Hero;
import com.ytr.injector.demo.mode.Language;
import com.ytr.injector.demo.mode.User;

public class MainActivity extends AppCompatActivity {
    @Inject
    public User user;
    @Inject
    public Hero hero;
    @Inject
    public Language language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //使用注入
        DIMainInjector.builder()
                .setMainDataSource(new MainDataSource())
                .setLanguageDataSource(new LanguageDataSource())
                .build()
                .inject(this);

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText(user.name + "\n" + hero.nickName+"\n" + language.description);

    }
}
