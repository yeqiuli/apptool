package com.hgy.treelist;

import com.hgy.bean.NodeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyehai on 2018/7/11 0011.
 */

public class TreeHelper {
    /**
     * 传入node  返回排序后的Node
     * 拿到用户传入的数据，转化为List<NodeBean>以及设置Node间关系，然后根节点，从根往下遍历进行排序；
     *
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<NodeBean> getSortedNodes(List<NodeBean> datas, int defaultExpandLevel) {
        List<NodeBean> result = new ArrayList<NodeBean>();
        // 设置Node间父子关系
        List<NodeBean> nodes = convetData2Node(datas);
        // 拿到根节点
        List<NodeBean> rootNodes = getRootNodes(nodes);
        // 排序以及设置Node间关系
        for (NodeBean node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    /**
     * 过滤出所有可见的Node
     * 过滤Node的代码很简单，遍历所有的Node，只要是根节点或者父节点是展开状态就添加返回
     *
     * @param nodes
     * @return
     */
    public static List<NodeBean> filterVisibleNode(List<NodeBean> nodes) {
        List<NodeBean> result = new ArrayList<NodeBean>();

        for (NodeBean node : nodes) {
            // 如果为跟节点，或者上层目录为展开状态
            if (node.isRootNode() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 将我们的数据转化为树的节点
     * 设置Node间，父子关系;让每两个节点都比较一次，即可设置其中的关系
     */
    private static List<NodeBean> convetData2Node(List<NodeBean> nodes) {

        for (int i = 0; i < nodes.size(); i++) {
            NodeBean n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                NodeBean m = nodes.get(j);
                if (m.getPid() instanceof String) {
                    if (m.getPid().equals(n.getId())) {
                        n.getChildren().add(m);
                        m.setParent(n);
                    } else if (m.getId().equals(n.getPid())) {
                        m.getChildren().add(n);
                        n.setParent(m);
                    }
                } else {
                    if (m.getPid() == n.getId()) {
                        n.getChildren().add(m);
                        m.setParent(n);
                    } else if (m.getId() == n.getPid()) {
                        m.getChildren().add(n);
                        n.setParent(m);
                    }
                }
            }
        }
        return nodes;
    }

    /**
     * 获得根节点
     *
     * @param nodes
     * @return
     */
    private static List<NodeBean> getRootNodes(List<NodeBean> nodes) {
        List<NodeBean> root = new ArrayList<NodeBean>();
        for (NodeBean node : nodes) {
            if (node.isRootNode())
                root.add(node);
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     * 通过递归的方式，把一个节点上的所有的子节点等都按顺序放入
     */
    private static <T> void addNode(List<NodeBean> nodes, NodeBean<T> node,
                                    int defaultExpandLeval, int currentLevel) {
        nodes.add(node);

        if (defaultExpandLeval >= currentLevel) {
            node.setExpand(true);
        }

        if (node.isLeaf())
            return;
        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(nodes, node.getChildren().get(i), defaultExpandLeval,
                    currentLevel + 1);
        }
    }

    /**
     * 设置节点的图标
     *
     * @param node
     */
    private static void setNodeIcon(NodeBean node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(node.iconExpand);
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(node.iconNoExpand);
        } else {
            node.setIcon(-1);
        }
    }
}
