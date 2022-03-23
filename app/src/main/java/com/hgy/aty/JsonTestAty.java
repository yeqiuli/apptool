
package com.hgy.aty;

import static com.tecsun.network.network.BaseHelper.ThreadTransformer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hgy.Env;
import com.hgy.adapter.RecyclerViewAdapter;
import com.hgy.base.BaseActivity;
import com.hgy.bean.ChildDeptBean;
import com.hgy.bean.ChildListBean;
import com.hgy.bean.DeptBean;
import com.hgy.bean.NodeBean;
import com.hgy.bean.OrgBean;
import com.hgy.bean.sqlite.OrganizationBean;
import com.hgy.sqlite.RoomDataUtils;
import com.hgy.treelist.OnTreeNodeCheckedChangeListener;
import com.hgy.treelist.OnTreeNodeClickListener;
import com.hgy.view.BToast;
import com.tecsun.network.utils.AppUtils;
import com.tecsun.network.utils.LogUntil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class JsonTestAty extends BaseActivity {

    private RecyclerView rlView;
    private List<OrganizationBean> organizationBeanArrayList = new ArrayList<>();

    private List<NodeBean> dataList = new ArrayList<>();

    private RecyclerViewAdapter mAdapter;


    @Override
    public int getLayout() {
        return R.layout.aty_test_json;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        rlView = findViewById(R.id.rlView);
        rlView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter(rlView, this, dataList,
                0, R.drawable.bt_arrow_up_gray, R.drawable.bt_arrow_down_gray);
        findViewById(R.id.tvTest).setOnClickListener(v -> {
            toJson();
        });
        rlView.setAdapter(mAdapter);

        mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener() {
            @Override
            public void onClick(NodeBean node, int position) {
                Log.e("xyh", "setOnTreeNodeClickListener: " + node.getName() + node.getLevel());
            }
        });

        //选中状态监听
        mAdapter.setCheckedChangeListener(new OnTreeNodeCheckedChangeListener() {
            @Override
            public void onCheckChange(NodeBean node, int position, boolean isChecked) {
                //获取所有选中节点
                List<NodeBean> selectedNode = mAdapter.getSelectedNode();
                for (NodeBean n : selectedNode) {
                    Log.e("xyh", "onCheckChange: " + n.getName() + n.getLevel());
                }
            }
        });
    }

    @Override
    public void backAty() {

    }

    @Override
    public void gotoCheckIn() {

    }

    private void toJson() {
//        OrgBean bean = AppUtils.getInstance().fromJson(Env.dept, OrgBean.class);
        OrgBean bean = AppUtils.getInstance().fromJson(Env.deptJson, OrgBean.class);
        String dept = bean.getData().toString();
        toJsonDeptFirst(dept);
    }

    private void toJsonDeptFirst(String dept) {
        LogUntil.e(dept);
        DeptBean deptBean = AppUtils.getInstance().fromJson(dept, DeptBean.class);
        String rid = deptBean.getRid();
        LogUntil.e(rid);
        OrganizationBean bean = new OrganizationBean();
        bean.setOrgID("公司名称");
        bean.setDeptID(rid);
        bean.setDeptName(deptBean.getName());
        organizationBeanArrayList.add(bean);
        JsonArray jsonArray = deptBean.getChildren();
        if (jsonArray.size() <= 0) {
            addOrg(organizationBeanArrayList);
            return;
        }
        List<ChildDeptBean> data = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            LogUntil.e(jsonObject.toString());
            ChildDeptBean childDeptBean = new ChildDeptBean();
            childDeptBean.setOrgID(rid);
            childDeptBean.setData(jsonObject.toString());
            data.add(childDeptBean);
        }
        if (data.size() > 0) {
            toJsonDept(data);
        } else {
            addOrg(organizationBeanArrayList);
        }
    }


    private void toJsonDept(List<ChildDeptBean> data) {
        // 遍历键值对对象的集合，得到每一个键值对对象
        List<ChildDeptBean> map = new ArrayList<>();
        for (ChildDeptBean childDeptBean : data) {
            // 根据键值对对象获取键和值
            String key = childDeptBean.getOrgID();
            String value = childDeptBean.getData();
            DeptBean deptBean = AppUtils.getInstance().fromJson(value, DeptBean.class);
            OrganizationBean bean = new OrganizationBean();
            String rid = deptBean.getRid();
            bean.setOrgID(key);
            bean.setDeptID(rid);
            bean.setDeptName(deptBean.getName());
            organizationBeanArrayList.add(bean);
            JsonArray jsonArray = deptBean.getChildren();
            if (jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                    LogUntil.e(jsonObject.toString());
                    ChildDeptBean childDeptBean1 = new ChildDeptBean();
                    childDeptBean1.setOrgID(rid);
                    childDeptBean1.setData(jsonObject.toString());
                    map.add(childDeptBean1);
                }
            }
        }
        if (data.size() > 0) {
            toJsonDept(map);
        } else {
            addOrg(organizationBeanArrayList);
        }
    }


    private void addOrg(List<OrganizationBean> orgList) {
        Observable.just(orgList)
                .map(r -> {
                    RoomDataUtils.getInstance().getOrgDao().deleteAllDept();
                    int count = 0;
                    for (OrganizationBean bean : r) {
                        RoomDataUtils.getInstance().getOrgDao().addDept(bean);
                        count++;
                    }

                    return count;
                })
                .compose(ThreadTransformer())
                .subscribe(r -> {
                    hideDialog();
                    organizationBeanArrayList.clear();
                    showToast("添加部门成功" + r, BToast.ICONTYPE_SUCCEED);
                    queryDept();
                }, e -> {
                    hideDialog();
                    e.printStackTrace();
                });
    }

    private void queryDept() {
        Observable.just(0)
                .map(r -> {
                    dataList.clear();
                    List<OrganizationBean> list = RoomDataUtils.getInstance().getOrgDao().queryCompany("公司名称");
                    List<ChildListBean> data = new ArrayList<>();
                    data.add(new ChildListBean(-1, list));
                    initData(data);
                    return 0;

                })
                .compose(ThreadTransformer())
                .subscribe(r -> {
                    mAdapter.addData(dataList);
                    id = 0;
                }, e -> {
                    hideDialog();
                    e.printStackTrace();
                });
    }

    private int id = 0;

    private void initData(List<ChildListBean> data) {
        List<ChildListBean> tem = new ArrayList<>();
        for (ChildListBean bean : data) {
            List<OrganizationBean> list = bean.getList();
            int pid = bean.getPid();
            for (OrganizationBean organizationBean : list) {
                String rid = organizationBean.getDeptID();
                dataList.add(new NodeBean(id + "", pid + "", organizationBean.getDeptName(), rid));
                List<OrganizationBean> organizationBeans = RoomDataUtils.getInstance().getOrgDao().queryCompany(rid);
                if (organizationBeans.size() > 0) {
                    ChildListBean listBean = new ChildListBean(id, organizationBeans);
                    tem.add(listBean);
                }
                id++;
            }
        }
        if (tem.size() > 0) {
            initData(tem);
        }
    }

}