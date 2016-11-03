package com.zhj.pixivision_getter.util;

import org.xutils.DbManager;

import java.io.File;

/**
 * Created by ZHJ on 2016/11/3 0003.
 * Purpose:
 */

public class ConfigManeger {
    public static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("pixiv.db")
            .setDbDir(new File("/sdcard/HelloPixivABCDEF"))
            .setDbVersion(2)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    db.getDatabase().enableWriteAheadLogging();
                }
            });

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    public void setDaoConfig(DbManager.DaoConfig daoConfig) {
        this.daoConfig = daoConfig;
    }
}
