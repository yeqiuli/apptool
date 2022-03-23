package com.hgy.treelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.hgy.bean.NodeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangke on 2017-1-14.
 */
public abstract class TreeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;

    /**
     * 默认不展开
     */
    private int defaultExpandLevel = 0;

    /**
     * 展开与关闭的图片
     */
    private int iconExpand = -1, iconNoExpand = -1;

    /**
     * 存储所有的Node
     */
    protected List<NodeBean> mAllNodes = new ArrayList<>();

    /**
     * 存储所有可见的Node
     */
    protected List<NodeBean> mNodes = new ArrayList<>();

    protected LayoutInflater mInflater;

    /**
     * 点击的回调接口
     */
    private OnTreeNodeClickListener onTreeNodeClickListener;

    public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
        this.onTreeNodeClickListener = onTreeNodeClickListener;
    }

    public TreeRecyclerAdapter(RecyclerView recyclerView, Context context, List<NodeBean> datas,
                               int defaultExpandLevel, int iconExpand, int iconNoExpand) {

        mContext = context;
        this.defaultExpandLevel = defaultExpandLevel;
        this.iconExpand = iconExpand;
        this.iconNoExpand = iconNoExpand;

        for (NodeBean node : datas) {
            node.getChildren().clear();
            node.iconExpand = iconExpand;
            node.iconNoExpand = iconNoExpand;
        }

        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);

        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);

        mInflater = LayoutInflater.from(context);
    }

    /**
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     */
    public TreeRecyclerAdapter(RecyclerView mTree, Context context, List<NodeBean> datas, int defaultExpandLevel) {
        this(mTree, context, datas, defaultExpandLevel, -1, -1);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NodeBean node = mNodes.get(position);
        //        convertView = getConvertView(node, position, convertView, parent);
        // 设置内边距
        holder.itemView.setPadding(node.getLevel() * 50, 10, 10, 10);
        /**
         * 设置节点点击时，可以展开以及关闭,将事件继续往外公布
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandOrCollapse(position);
                if (onTreeNodeClickListener != null) {
                    onTreeNodeClickListener.onClick(mNodes.get(position), position);
                }
            }
        });
        onBindViewHolder(node, holder, position);
    }

    @Override
    public int getItemCount() {
        return mNodes.size();
    }


    /**
     * 获取排序后所有节点
     *
     * @return
     */
    public List<NodeBean> getAllNodes() {
        if (mAllNodes == null)
            mAllNodes = new ArrayList<NodeBean>();
        return mAllNodes;
    }

    /**
     * 获取所有选中节点
     *
     * @return
     */
    public List<NodeBean> getSelectedNode() {
        List<NodeBean> checks = new ArrayList<NodeBean>();
        for (int i = 0; i < mAllNodes.size(); i++) {
            NodeBean n = mAllNodes.get(i);
            if (n.isChecked()) {
                checks.add(n);
            }
        }
        return checks;
    }


    /**
     * 相应ListView的点击事件 展开或关闭某节点
     *
     * @param position
     */
    public void expandOrCollapse(int position) {
        NodeBean n = mNodes.get(position);

        if (n != null) {// 排除传入参数错误异常
            if (!n.isLeaf()) {
                n.setExpand(!n.isExpand());
                mNodes = TreeHelper.filterVisibleNode(mAllNodes);
                notifyDataSetChanged();// 刷新视图
            }
        }
    }

    /**
     * 设置多选
     *
     * @param node
     * @param checked
     */
    protected void setChecked(final NodeBean node, boolean checked) {
        node.setChecked(checked);
        setChildChecked(node, checked);
        if (node.getParent() != null) {
            setNodeParentChecked(node.getParent(), checked);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置多选
     *
     * @param node
     * @param checked
     */
    protected void setNoChecked(final NodeBean node, boolean checked) {
        node.setChecked(checked);
        for (NodeBean bean : mAllNodes) {
            if (bean.getId().equals(node.getId())) {
                continue;
            }
            bean.setChecked(false);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置是否选中
     *
     * @param node
     * @param checked
     */
    public <T> void setChildChecked(NodeBean<T> node, boolean checked) {
        if (!node.isLeaf()) {
            node.setChecked(checked);
            for (NodeBean childrenNode : node.getChildren()) {
                setChildChecked(childrenNode, checked);
            }
        } else {
            node.setChecked(checked);
        }
    }


    private void setNodeParentChecked(NodeBean node, boolean checked) {
        if (checked) {
            node.setChecked(true);
        } else {
            List<NodeBean> childrens = node.getChildren();
            boolean isChecked = false;
            for (NodeBean children : childrens) {
                if (children.isChecked()) {
                    isChecked = true;
                    break;
                }
            }
            //如果所有自节点都没有被选中 父节点也不选中
            if (!isChecked) {
                node.setChecked(false);
            }
        }
        if (node.getParent() != null)
            setNodeParentChecked(node.getParent(), checked);
    }

    /**
     * 清除掉之前数据并刷新  重新添加
     *
     * @param mlists
     * @param defaultExpandLevel 默认展开几级列表
     */
    public void addDataAll(List<NodeBean> mlists, int defaultExpandLevel) {
        mAllNodes.clear();
        addData(-1, mlists, defaultExpandLevel);
    }

    /**
     * 在指定位置添加数据并刷新 可指定刷新后显示层级
     *
     * @param index
     * @param mlists
     * @param defaultExpandLevel 默认展开几级列表
     */
    public void addData(int index, List<NodeBean> mlists, int defaultExpandLevel) {
        this.defaultExpandLevel = defaultExpandLevel;
        notifyData(index, mlists);
    }

    /**
     * 在指定位置添加数据并刷新
     *
     * @param index
     * @param mlists
     */
    public void addData(int index, List<NodeBean> mlists) {
        notifyData(index, mlists);
    }

    /**
     * 添加数据并刷新
     *
     * @param mlists
     */
    public void addData(List<NodeBean> mlists) {
        addData(mlists, defaultExpandLevel);
    }

    /**
     * 添加数据并刷新 可指定刷新后显示层级
     *
     * @param mlists
     * @param defaultExpandLevel
     */
    public void addData(List<NodeBean> mlists, int defaultExpandLevel) {
        this.defaultExpandLevel = defaultExpandLevel;
        notifyData(-1, mlists);
    }

    /**
     * 添加数据并刷新
     *
     * @param node
     */
    public void addData(NodeBean node) {
        addData(node, defaultExpandLevel);
    }

    /**
     * 添加数据并刷新 可指定刷新后显示层级
     *
     * @param node
     * @param defaultExpandLevel
     */
    public void addData(NodeBean node, int defaultExpandLevel) {
        List<NodeBean> nodes = new ArrayList<>();
        nodes.add(node);
        this.defaultExpandLevel = defaultExpandLevel;
        notifyData(-1, nodes);
    }

    /**
     * 刷新数据
     *
     * @param index
     * @param mListNodes
     */
    private void notifyData(int index, List<NodeBean> mListNodes) {
        for (int i = 0; i < mListNodes.size(); i++) {
            NodeBean node = mListNodes.get(i);
            node.getChildren().clear();
            node.iconExpand = iconExpand;
            node.iconNoExpand = iconNoExpand;
        }
        for (int i = 0; i < mAllNodes.size(); i++) {
            NodeBean node = mAllNodes.get(i);
            node.getChildren().clear();
            // node.isNewAdd = false;
        }
        mAllNodes.clear();
        if (index != -1) {
            mAllNodes.addAll(index, mListNodes);
        } else {
            mAllNodes.addAll(mListNodes);
        }
        /**
         * 对所有的Node进行排序
         */
        mAllNodes = TreeHelper.getSortedNodes(mAllNodes, defaultExpandLevel);
        /**
         * 过滤出可见的Node
         */
        mNodes = TreeHelper.filterVisibleNode(mAllNodes);
        //刷新数据
        notifyDataSetChanged();
    }


    public abstract void onBindViewHolder(NodeBean node, RecyclerView.ViewHolder holder, final int position);
}
