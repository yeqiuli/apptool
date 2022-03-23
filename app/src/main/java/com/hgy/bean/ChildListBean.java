package com.hgy.bean;

import com.hgy.bean.sqlite.OrganizationBean;

import java.util.List;

public class ChildListBean {
    private int pid;
    private List<OrganizationBean> list;

    public ChildListBean(int pid, List<OrganizationBean> list) {
        this.pid = pid;
        this.list = list;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<OrganizationBean> getList() {
        return list;
    }

    public void setList(List<OrganizationBean> list) {
        this.list = list;
    }
}
