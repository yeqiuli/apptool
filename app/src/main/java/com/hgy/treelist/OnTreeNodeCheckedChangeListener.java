package com.hgy.treelist;

import com.hgy.bean.NodeBean;

/**
 * Created by xiaoyehai on 2018/7/12 0012.
 */

public interface OnTreeNodeCheckedChangeListener {

    void onCheckChange(NodeBean node, int position, boolean isChecked);
}
