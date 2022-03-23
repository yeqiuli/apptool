package com.hgy.sqlite;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hgy.MyApp;
import com.hgy.bean.sqlite.OrganizationBean;
import com.hgy.sqlite.dao.OrgDao;
import com.hgy.tool.MyFileUtil;

import io.reactivex.FlowableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;


@Database(entities = {
        OrganizationBean.class}, version = 1, exportSchema = false)
public abstract class RoomDataUtils extends RoomDatabase {

    private static final String DB_NAME = "data.db";
    private static volatile RoomDataUtils instance;

    public static RoomDataUtils getInstance() {
        if (instance == null) {
            synchronized (RoomDataUtils.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            MyApp.getInstances(),
                            RoomDataUtils.class,
                            MyFileUtil.databasePath + DB_NAME)
//                            .addMigrations(migration_1_2)
                            .build();
                }
            }
        }
        return instance;
    }

    private final static Migration migration_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //此处对于数据库中的所有更新都需要写下面的代码
        }
    };


    public abstract OrgDao getOrgDao();


    public static <T> FlowableTransformer<T, T> FlowThreadTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static <T> Observable.Transformer<T, T> ThreadTransformer() {
        return upstream -> upstream.subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread());

    }

    public static <T> SingleTransformer<T, T> SingleThreadTransformer() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
