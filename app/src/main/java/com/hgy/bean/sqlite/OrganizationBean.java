package com.hgy.bean.sqlite;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "deptJson")
public class OrganizationBean {
    @PrimaryKey(autoGenerate = true)//主键是否自动增长，默认为false
    private long id;
    private String deptName;//部门名称
    private String deptID;//部门ID
    private String orgID;//父部门ID

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }
}
