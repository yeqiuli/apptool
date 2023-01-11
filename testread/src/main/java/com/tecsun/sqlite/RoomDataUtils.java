package com.tecsun.sqlite;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tecsun.MyApp;
import com.tecsun.bean.SqlDevInfo;
import com.tecsun.network.utils.LogUntil;
import com.tecsun.sqlite.dao.DevListDao;


@Database(entities = {SqlDevInfo.class}, version = 1, exportSchema = false)
public abstract class RoomDataUtils extends RoomDatabase {

    private static final String DB_NAME = "devInfoData";
    private static volatile RoomDataUtils instance;

    public static RoomDataUtils getInstance() {
        if (instance == null) {
            synchronized (RoomDataUtils.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    MyApp.getInstances(),
                                    RoomDataUtils.class,
                                    DB_NAME)
                            .build();
                }
            }
        }
        return instance;
    }

    public void init() {
        LogUntil.i("生成数据库");
    }

    public abstract DevListDao getDev();



}
