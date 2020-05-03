package com.example.demo.avltree;

import lombok.Getter;
import lombok.Setter;


/**
 *  平衡二叉树
 */
@Setter
@Getter
public class AvlTree {

    /**
     * 根节点
     */
    private TreeNode root;


    /**
     * 添加节点
     * @param value 新节点值
     * @return 当前节点
     */
    public void insert(int value) {
        insert(value, value);
    }

    /**
     * 添加节点
     * @param value 新节点值
     * @param data 新节点数据
     * @return 当前节点
     */
    public void insert(int value,Object data){
        TreeNode newNode = new TreeNode(value, data);
        // 根节点为空 或 新节点等于根节点 则 新节点为根节点，直接返回
        if (root == null) {
            root = newNode;
            return;
        }
        // 等于根节点，则 数据赋根节点，直接返回
        if( root.value == newNode.value){
            root.data = newNode.data;
            return;
        }

        // 临时节点
        TreeNode tempNode = null;
        int newValue = newNode.value;
        // 当前节点 ，新节点的父节点
        TreeNode parent = root;
        /**
         * 从根节点一直往下找，每个节点命名为：当前节点
         * 1.新节点 = 当前节点，赋值给当前节点直接返回
         * 2.新节点 < 当前节点，如当前节点左子节点为空直接插入，否则继续往下找
         * 3.新节点 > 当前节点，如当前节点右子节点为空直接插入，否则接续往下找
         * 4.平衡节点
         */
        while (true){

            //新节点小于当前节点，新节点放至在当前节点左边
            if (newValue < parent.value) {
                tempNode = parent.left;
                // 左子节点为空，插入否则，继续往下找
                if(tempNode == null) {
                    parent.left = newNode;
                    newNode.parent = parent;
                    break;
                }

            //新节点大于当前节点，新节点放至当前节点右边
            }else if(newValue > parent.value){
                tempNode = parent.right;
                // 右子节点为空，插入否则，继续往下找
                if(tempNode == null) {
                    parent.right = newNode;
                    newNode.parent = parent;
                    break;
                }

            }else{
                // 新节点等于当前节点，数据赋值在当前节点直接返回
                tempNode.data =newNode.data;
                newNode = tempNode;
                break;
            }

            parent = tempNode;
        }

        // 平衡节点
        avlNode(newNode);
    }

    /**
     * 根据值查找病删除对应节点
     * @param value 删除节点的值
     */
    public void delete(int value) {

        TreeNode node = root;
        while (node != null) {

            if (value < node.value) {
                node = node.left;
                continue;

            }
            if (value > node.value) {
                node = node.right;
                continue;
            }
            /**
             * 匹配到删除节点
             */

            TreeNode parent = node.parent;
            // 为父节点，且只有一个节点，直接删除父几点
            if (parent == null && node.left == null && node.right == null) {
                root = null;
                break;
            }

            // 子节点为空，直接删除
            if (node.left == null && node.right == null) {
                deleteNode(node);
                node = parent;
                break;
            }
            /**
             * 只有一个子节点，直接把子节点替换到当前位置
             */
            if (node.left == null) {
                replaceSubNode(node.right, parent);
                break;
            }
            if (node.right == null) {
                replaceSubNode(node.left, parent);
                break;
            }
            /**
             * 有双子节点
             * 用左子节点的的最右节点代替当前节点（及左边最大数 ）
             */

            // 替换节点
            TreeNode replaceNode = searchMax(node.left);

            // 暂存替换节点的父节点，为平衡节点，
            TreeNode tempParent = replaceNode.parent;

            // 替换节点就是删除节点的右节点，则无需替换
            if(node.left.value != replaceNode.value) {

                // 替换节点的左子节点 替换到替换节点
                replaceParentRight(replaceNode.left, replaceNode.parent);

                // 删除节点的左子节点 设置为 替换节点的左节点
                replaceParentLeft(node.left, replaceNode);
            }

            // 删除节点的父节点 设置 替换节点的父节点
            replaceSubNode(replaceNode, parent);

            // 删除节点的右子节点 设置为 替换节点的右节点
            replaceParentRight(node.right, replaceNode);

            // 从原替换节点的父节点开始向上平衡
            node = tempParent;

            break;
        }
        // 平衡节点
        avlNode(node);
    }

    /**
     * 删除当前节点
     */
    private void deleteNode(TreeNode node) {
        if (node.parent == null) {
            return;
        }
        if (node.value < node.parent.value) {
            node.parent.left = null;
        }else{
            node.parent.right = null;
        }
    }

    /**
     * 子节点父节点改成此父节点
     * 节点不能为空
     * @param subNode 子节点 不能为空
     * @param parentNode 父节点
     */
    private void replaceSubNode(TreeNode subNode, TreeNode parentNode) {
        if(subNode == null){
            return;
        }

        subNode.parent = parentNode;

        if(parentNode!=null) {
            if (subNode.value < parentNode.value) {
                parentNode.left = subNode;
            } else {
                parentNode.right = subNode;
            }
        }
    }

    /**
     * 此父节点的左子节点更换成此子节点
     * 已知是替换父节点的左子节点
     * @param subNode 子节点 可为空
     * @param parentNode 父节点 可为空
     */
    private void replaceParentLeft(TreeNode subNode, TreeNode parentNode) {
        if(parentNode!=null) {
            parentNode.left = subNode;
        }
        if (subNode != null) {
            subNode.parent = parentNode;
        }

    }
    /**
     * 此父节点的右子节点更换成此子节点
     * 已知是替换父节点的右子节点
     * @param subNode 子节点 可为空
     * @param parentNode 父节点 可为空
     */
    private void replaceParentRight(TreeNode subNode, TreeNode parentNode) {
        if(parentNode!=null) {
            parentNode.right = subNode;
        }
        if (subNode != null) {
            subNode.parent = parentNode;
        }

    }

    /**
     * 查找当前节点里最大的子节点
     * @param node
     * @return
     */
    public TreeNode searchMax(TreeNode node) {
        TreeNode maxNode = node.right;
        if (maxNode != null) {
            return searchMax(maxNode);
        }
        return node;

    }

    /**
     * 平衡节点
     * @param node
     */
    private void avlNode(TreeNode node) {
        while (node != null) {

            // 左树高 = 左子节点的高节点+1
            int leftHeight = (node.left == null ? 0 : Math.max(node.left.leftHeight, node.left.rightHeight)+ 1) ;
            // 右树高 = 右子节点的高节点+1
            int rightHeight = (node.right == null ? 0 : Math.max(node.right.leftHeight, node.right.rightHeight) + 1);
            int factor = leftHeight - rightHeight;

            if (factor == 2) {// 左树比有高
                // L子节点的L子节点高
                int leftLeftHeight = node.left == null ? 0 : node.left.leftHeight;
                // L子节点的R子节点高
                int leftRightHeight = node.left == null ? 0 : node.left.rightHeight;

                if (leftLeftHeight > leftRightHeight) {
                    // RR ： L子节点的L子节点 > L子节点的R子节点  --> 右旋
                    rightRotate(node);
                }else {
                    // LR 否则左右旋
                    leftRightRotate(node);

                }

            }else if(factor==-2) {// 右树比左树高
                // R子节点的R子节点高
                int rightRightHeight = node.right == null ? 0 : node.right.rightHeight;
                // R子节点的L子节点高
                int rightLeftHeight = node.right == null ? 0 : node.right.leftHeight;
                if (rightRightHeight > rightLeftHeight){
                    // LL ：R子节点的R子节点 大于 R子节点的L子节点 --> 左旋
                    leftRotate(node);
                }else {
                    // RL 否则右左旋
                    rightLeftRotate(node);
                }
            }
            // 重新节点左右树高
            calcHeight(node);

            if (node.parent == null) {
                this.root = node;
            }
            node = node.parent;
        }
    }

    /**
     * 右旋
     * @param node 被选转的的节点，及当前节点
     * @return 当前节点
     */
    private TreeNode rightRotate(TreeNode node) {

        // 当前节点的右子节点
        TreeNode left = node.left;
        // 原左子节点的右子节点 替换到 当前节点的左子节点
        replaceParentLeft(left.right,node);
        // 当前节点的左子节点替换到当前节点位置

        /**
         * 当前节点和原左子节点，更换父子关系。
         * 当前节点成为原左儿子的右儿子，原左儿子成当前节点的父亲
         */

        // 当前节点的左子节点父节点 更换成 当前节点父节点
        replaceSubNode(left,node.parent);

        // 当前节点的原左子节点 替换成 当前节点的父节点，原左子节点的右子节点 更换成 当前节点
        replaceParentRight(node, left);

        return node;
    }

    /**
     * 左旋
     * @param node 被选转的的节点，及当前节点
     * @return 当前节点
     */
    private TreeNode leftRotate(TreeNode node) {

        // 当前节点的右子节点
        TreeNode right = node.right;
        // 原右子节点的左子节点 替换到 当前节点的右子节点
        replaceParentRight(right.left,node);

        /**
         * 当前节点和原右子节点，更换父子关系。
         * 当前节点成为原右儿子的左儿子，原右儿子成当前节点的父亲
         */

        // 当前节点的右子节点父节点 更换成 当前节点父节点，
        replaceSubNode(right,node.parent);
        // 当前节点的原右子节点 更换成 当前节点的父节点，原右子节点的左子节点 更换成 当前节点
        replaceParentLeft(node, right);

        return node;
    }

    /**
     * 左右旋
     * @param node
     * @return
     */
    private TreeNode leftRightRotate(TreeNode node) {
        TreeNode currentNode = leftRotate(node.left);
        calcHeight(currentNode);

        rightRotate(node);
        return node;
    }

    /**
     * 右左旋
     * @param node
     * @return
     */
    private TreeNode rightLeftRotate(TreeNode node) {
        TreeNode currentNode = rightRotate(node.right);
        calcHeight(currentNode);
        leftRotate(node);
        return node;
    }

    // 计算节点都的左右树高
    private void calcHeight(TreeNode node) {
        // 左树高 = 左子节点的高节点+1
        node.leftHeight = (node.left == null ? 0 : Math.max(node.left.leftHeight, node.left.rightHeight)+ 1) ;
        // 右树高 = 右子节点的高节点+1
        node.rightHeight = (node.right == null ? 0 : Math.max(node.right.leftHeight, node.right.rightHeight) + 1);
    }




    public void print() {
        if (root.right != null) {
            printTree(root.right, true, "");
        }
        printNodeValue(root);
        if (root.left != null) {
            printTree(root.left, false, "");
        }
        System.out.println();
        System.out.println();
    }

    private void printNodeValue(TreeNode node) {
        System.out.print(node.value);
        System.out.println();
    }

    private void printTree(TreeNode node, boolean isRight, String indent) {
        if (node.right != null) {
            printTree(node.right, true, indent + (isRight ? "        " : " |      "));
        }
        System.out.print(indent);
        if (isRight) {
            System.out.print(" /");
        } else {
            System.out.print(" \\");
        }
        System.out.print("----- ");
        printNodeValue(node);
        if (node.left != null) {
            printTree(node.left, false, indent + (isRight ? " |      " : "        "));
        }
    }

    /**
     * 二叉树节点
     */
    static class TreeNode {

        /**
         * 值
         */
        private int value;

        private Object data;
        /**
         * 左树高度
         */
        private int leftHeight;
        /**
         * 右树高度
         */
        private int rightHeight;
        /**
         * 父节点
         */
        private transient TreeNode parent;
        /**
         * 左子节点
         */
        private TreeNode left;
        /**
         * 右子节点
         */
        private TreeNode right;

        public TreeNode() {}

        public TreeNode(int value, Object data) {
            this.value = value;
            this.data = data;
        }
    }
}
