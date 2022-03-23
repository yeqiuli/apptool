package com.hgy.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hgy.bean.sqlite.OrganizationBean;

import java.util.List;

@Dao
public interface OrgDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addDept(OrganizationBean visitRecordBean);

    @Query("SELECT * FROM deptJson")
    OrganizationBean getVoiceBean();

    @Query("delete from deptJson ")
    void deleteAllDept();//ok

    @Query("SELECT * FROM deptJson where orgID =:orgID")
    List<OrganizationBean> queryCompany(String orgID);
}
