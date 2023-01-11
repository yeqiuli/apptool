package com.tecsun.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tecsun.bean.SqlDevInfo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface DevListDao {
    /**
     * 获取所有白名单数据
     */
    @Query("SELECT * FROM devList ")
    List<SqlDevInfo> getAll();

    /**
     * 添加记录
     *
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(SqlDevInfo sqlDevInfo);

    /**
     * 通过身份证号查询信息
     *
     * @param devName 身份证号
     */
    @Query("SELECT * FROM devList where devName=:devName")
    SqlDevInfo findByDevName(String devName);

    /**
     * 根据条件查询数据
     *
     * @return
     */
    @Query("SELECT * FROM devList WHERE productId=:productId and devName=:devName")
    SqlDevInfo getDevInfoByKey(String productId, String devName);


    /**
     * 根据条件查询数据
     *
     * @return
     */
    @Query("SELECT * FROM devList WHERE productId=:productId")
    List<SqlDevInfo> getDevByProductId(String productId);

    /**
     * 删除全部数据
     *
     */
    @Query("delete from devList ")
    void deleteAll();

    /**
     * 删除单条数据
     */
    @Query("delete from devList where devName= :devName")
    void deleteByDevName(String devName);

}
