package com.ytr.injector.demo.di;

import com.ytr.injector.annotation.DataSource;
import com.ytr.injector.annotation.Provides;
import com.ytr.injector.demo.mode.Hero;
import com.ytr.injector.demo.mode.User;

/**
 * Created by YTR on 2017/8/30.
 */
@DataSource
public class MainDataSource {
    @Provides
    public User getUser() {
        User user = new User();
        user.id = 0;
        user.name = "admin";
        user.age = 26;
        user.email = "admin@qq.com";
        return user;
    }

    @Provides
    public Hero getHero(){
        Hero hero = new Hero();
        hero.nickName = "盖伦";
        hero.q = "致命打击";
        hero.w = "勇气";
        hero.e = "审判";
        hero.r = "德玛西亚正义";
        return hero;
    }
}
