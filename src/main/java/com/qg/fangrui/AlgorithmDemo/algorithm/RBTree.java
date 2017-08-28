package com.qg.fangrui.AlgorithmDemo.algorithm;

/**
 * Created by funrily on 17-8-27
 * 红黑树的 Java 实现
 * 参考资料：http://www.cnblogs.com/skywang12345/p/3624343.html
 * @version 1.0.0
 */
public class RBTree<T extends Comparable<T>> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private RBTNode<T> mRoot;    // 根节点

    public RBTree() {
        mRoot = null;
    }

    private RBTNode<T> parentOf(RBTNode<T> node) {
        return node != null ? node.parent : null;
    }

    private boolean colorOf(RBTNode<T> node) {
        return node != null ? node.color : BLACK;   //若为null，表明是叶子
    }

    private boolean isRed(RBTNode<T> node) {
        return ((node != null) && (node.color == RED));
    }

    private boolean isBlack(RBTNode<T> node) {
        return !isRed(node);
    }

    private void setBlack(RBTNode<T> node) {
        if (node != null) node.color = BLACK;
    }

    private void setRed(RBTNode<T> node) {
        if (node != null) node.color = RED;
    }

    private void setColor(RBTNode<T> node, boolean color) {
        if (node != null) node.color = color;
    }

    private void setParent(RBTNode<T> node, RBTNode<T> parent) {
        if (node != null) node.parent = parent;
    }

    // 先序遍历（递归实现）
    private void preOrder(RBTNode<T> tree) {
        if (tree != null) {
            System.out.println(tree.key + "");
            preOrder(tree.left);
            preOrder(tree.right);
        }
    }

    public void preOrder() {
        preOrder(mRoot);
    }

    // 查找红黑树 x 中键值为 key 的节点(递归实现)
    private RBTNode<T> search(RBTNode<T> x, T key) {
        if (x == null) return x;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return search(x.left, key);
        else if (cmp > 0) return search(x.right, key);
        else return x;
    }

    public RBTNode<T> search(T key) {
        return search(mRoot, key);
    }

    // 查找红黑树 x 中键值为 key 的节点(非递归实现)
    private RBTNode<T> iterativeSearch(RBTNode<T> x, T key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x;
        }
        return x;
    }

    public RBTNode<T> iterativeSearch(T key) {
        return iterativeSearch(mRoot, key);
    }

    // 查找最小结点：返回tree为根结点的红黑树的最小结点。
    private RBTNode<T> minimum(RBTNode<T> tree) {
        if (tree == null) return null;
        while (tree.left != null)
            tree = tree.left;
        return tree;
    }

    public T minimum() {
        RBTNode<T> p = minimum(mRoot);
        if (p != null) return p.key;
        return null;
    }

    //查找最大结点：返回tree为根结点的红黑树的最大结点。
    private RBTNode<T> maxmum(RBTNode<T> tree) {
        if (tree == null) return null;
        while (tree.right != null)
            tree = tree.right;
        return tree;
    }

    public T maximum() {
        RBTNode<T> p = maxmum(mRoot);
        if (p != null) return p.key;
        return null;
    }

    // 找结点(x)的后继结点。即，查找"红黑树中数据值大于该结点"的"最小结点"。
    public RBTNode<T> successor(RBTNode<T> x) {
        // 如果x存在右孩子，则查找以其右孩子为根的子树的最小结点
        if (x.right != null)
            return minimum(x.right);
        // 如果没有右孩子
        // 1、x是一个左孩子，则查找其父节点；2、x是一个右孩子，查找最低的父节点且必须是有左孩子
        RBTNode<T> y = x.parent;    // 获取父节点
        while ((y != null) && (x == y.right)) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    // 找结点(x)的前驱结点。即，查找"红黑树中数据值小于该结点"的"最大结点"。
    public RBTNode<T> predecessor(RBTNode<T> x) {   // 参考上一个方法
        if (x.left != null) return maxmum(x.left);
        RBTNode<T> y = x.parent;
        while ((y != null) && (x == y.left)) {
            x = y;
            y = y.parent;
        }
        return y;
    }

    /**
     * 对红黑树的节点(x)进行左旋转
     *
     * 左旋示意图(对节点x进行左旋)：
     *        px                            px
     *       /                             /
     *      x                             y
     *     /  \      --(左旋)--           / \
     *   lx   y                         x  ry
     *      /   \                      / \
     *     ly   ry                    lx  ly
     */
    private void leftRotate(RBTNode<T> x) {
        RBTNode<T> y = x.right;
        x.right = y.left;
        if (y.left != null)
            y.left.parent = x;  // 建立相互关系
        y.parent = x.parent;
        if (x.parent == null) {
            this.mRoot = y;         // 如果 x 的父节点为空，则将 y 设置为根节点
        } else {
            if (x.parent.left == x){

                x.parent.left = y;  // 如果 x 是左孩子
            } else{
                x.parent.right = y; // 如果 x 是右孩子
            }
        }
        y.left =x;
        x.parent =y;
    }
    /**
     * 对红黑树的节点(y)进行右旋转
     *
     * 右旋示意图(对节点y进行左旋)：
     *            py                               py
     *           /                                /
     *          y                                x
     *         /  \      --(右旋)-.             /  \
     *        x   ry                          lx   y
     *       / \                                  / \
     *      lx  rx                               rx  ry
     *
     */
    private void rightRotate(RBTNode<T> y) {
        RBTNode<T> x = y.left;
        y.left = x.right;
        if (x.right != null)
            x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null){
            this.mRoot = x;
        } else {
            if (y == y.parent.right){
                y.parent.right = x;
            } else {
                y.parent.left = x;
            }
        }
        x.right = y;
        y.parent = x;
    }

    /**
     * 红黑树插入修正函数
     *
     * 在向红黑树中插入节点之后(失去平衡)，再调用该函数
     * 目的是将它重新塑造成一颗红黑树。
     * @param node 插入的结点
     */
    private void insertFixUp(RBTNode<T> node) {
        RBTNode<T> parent, gparent;
        // 若父节点存在，并且父节点的颜色是红色
        while (((parent = parentOf(node))!=null) && isRed(parent)) {
            gparent = parentOf(parent); //获得祖父节点
            // 若父节点是祖父节点的左孩子
            if (parent == gparent.left){
                RBTNode<T> uncle = gparent.right;
                // case 1 叔叔节点是红色(非null)
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                // case 2 叔叔节点是黑色且当前节点是右孩子
                if (parent.right == node){
                    RBTNode<T> tmp;
                    leftRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // case 3 叔叔节点是黑色且当前节点是左孩子
                setBlack(parent);
                setRed(gparent);
                rightRotate(gparent);
            } else {    // 若父节点是祖父节点的右孩子
                RBTNode<T> uncle = gparent.left;
                // case 1 叔叔节点是红色(非null)
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }
                // case 2 叔叔节点是黑色且当前节点是左孩子
                if (parent.left == node) {
                    RBTNode<T> tmp;
                    rightRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = parent;
                }
                // case 3 叔叔节点是黑色且当前节点是右孩子
                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }
        }
        setBlack(this.mRoot);   // 将根节点设为黑色
    }

    /**
     * 将节点插入到红黑树中
     * @param node 插入的节点
     */
    private void insert(RBTNode<T> node) {
        int cmp;
        RBTNode<T> y = null;
        RBTNode<T> x = this.mRoot;
        // 1 将红黑树当作一颗二叉查找树，将节点添加到二叉查找树中
        while (x != null){
            y=x;
            cmp = node.key.compareTo(x.key);    // 比较后找到合适的插入位置
            if (cmp < 0)
                x = x.left;
            else
                x = x.right;
        }
        node.parent = y;
        if (y != null){
            cmp = node.key.compareTo(y.key);   // 判断node应该是y左右哪边的孩子
            if (cmp < 0){
                y.left = node;
            } else {
                y.right = node;
            }
        } else {    // node 是第一个节点
            this.mRoot = node;
        }
        // 2 设置节点的颜色为红色
        node.color = RED;
        // 3 将它重新修正为一颗二叉查找树
        insertFixUp(node);
    }

    /**
     * 新建结点(key)，并将其插入到红黑树中
     * @param key
     */
    public void insert(T key) {
        RBTNode<T> node = new RBTNode<T>(key, BLACK, null, null, null); // 孤立的节点
        if (node != null) insert(node);
    }

    /**
     * 红黑树删除修正函数
     *
     * 在从红黑树中删除插入节点之后(红黑树失去平衡)，再调用该函数；
     * 目的是将它重新塑造成一颗红黑树。
     * @param node 待修正的节点
     * @param parent
     */
    private void removeFixUp(RBTNode<T> node, RBTNode<T> parent) {
        RBTNode<T> other;
        while ((node==null || isBlack(node)) && (node != this.mRoot)) {
            // 当前节点是父节点的左孩子
            if (parent.left == node){
                other = parent.right;   // 被删除节点的兄弟
                // case 1 兄弟是红色的(非null)
                if (isRed(other)) {
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.right;
                }
                if (other == null){
//                    System.out.println("other is null !");
                    return;
                }
                // case 2 兄弟是黑色的，并且兄弟的两个孩子也是黑色的
                if ((other.left==null || isBlack(other.left)) && (other.right==null || isBlack(other.right))) {
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {
                    // case 3 兄弟是黑色，并且兄弟的左孩子是红色的，右孩子是黑色的
                    if (other.right==null || isBlack(other.right)){
                        setBlack(other.left);
                        setRed(other);
                        rightRotate(other);
                        other = parent.right;
                    }
                    // case 4 兄弟是黑色，并且兄弟的右孩子是红色的
                    setColor(other, colorOf(parent));
                    setBlack(other.right);
                    leftRotate(parent);
                    node = this.mRoot;
                    break;
                }
            } else {
                other = parent.left;
                // case 1 兄弟是红色
                if (isRed(other)){
                    setBlack(other);
                    setRed(parent);
                    rightRotate(parent);
                    other = parent.left;
                }
                if (other == null){
//                    System.out.println("other is null !");
                    return;
                }
                // case 2 兄弟是黑色，并且兄弟的两个孩子也是黑色的
                if ((other.left==null
                        || isBlack(other.left))
                        && (other.right==null
                        || isBlack(other.right))) {
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {
                    // case 3 兄弟是黑色，并且兄弟的左孩子是红色的，右孩子是黑色的
                    if (other.right==null || isBlack(other.right)){
                        setBlack(other.right);
                        setRed(other);
                        leftRotate(other);
                        other = parent.left;
                    }
                    // case 4 兄弟是黑色，并且兄弟的右孩子是红色的
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.left);
                    rightRotate(parent);
                    node = this.mRoot;
                    break;
                }
            }
        }
        if (node != null)
            setBlack(node);
    }

    /**
     * 删除结点(node)，并返回被删除的结点
     * @param node 删除的结点
     */
    private void remove(RBTNode<T> node) {
        RBTNode<T> child, parent;
        boolean color;
        // case 1 被删除节点左右孩子不为空
        if ((node.left != null) && (node.right != null)){
            RBTNode<T> replace = node;  // 取代被删除节点
            replace = replace.right;    // 获取后继节点
            while (replace.left != null) {
                replace = replace.left;     // 找到大于 node 的最小节点(二叉树的特点)
            }
            if (parentOf(node) != null){
                if (parentOf(node).left == node){
                    parentOf(node).left = replace;  // 如果 node 是左孩子
                } else {
                    parentOf(node).right = replace; // 如果 node 是右孩子
                }
            } else {
                this.mRoot = replace;   // 如果 node 是根节点
            }

            // child 是取代节点 replace 的右孩子，也需要调整(注意：replace 肯定木有左孩子，因为前面找的是最小节点)
            child = replace.right;
            parent = parentOf(replace);
            color = colorOf(replace);   // 保留取代节点的颜色

            // 如果被删除节点 node 是取代节点的父节点
            if (parent == node){
                parent = replace;   // 就是说取代节点是被删除节点的右孩子
            } else {
                if (child != null)  setParent(child, parent);
                parent.left = child;
                replace.right = node.right;
                setParent(node.right, replace); // 接管被删除节点的右孩子
            }
            replace.parent = node.parent;
            replace.color = node.color;
            replace.left = node.left;   // 接管被删除节点的左孩子
            node.left.parent = replace;

            if (color == BLACK){
                removeFixUp(child, parent); // 删除修正函数
            }
            node= null;
            return;
        }   // case 1 end

        if (node.left != null) {    // 保存被删除节点的孩子
            child = node.left;      // 被删除节点左孩子不为空
        } else {
            child = node.right;     // 被删除节点右孩子不为空
        }
        parent = node.parent;
        color = node.color;
        if (child != null)
            child.parent = parent;

        if (parent != null){
            if (parent.left!=null && parent.left == node) {
                parent.left = child;
            } else {
                parent.right = child;
            }
        } else {    // 被删除节点是根节点
            this.mRoot = child;
        }

        if (color == BLACK)
            removeFixUp(child, parent);
        node = null;
    }
    /**
     * 删除节点
     * @param key 删除节点的属性值
     */
    public void remove(T key){
        RBTNode<T> node;
        if ((node = search(mRoot, key)) != null)
            remove(node);
    }

    /**
     * 销毁红黑树（递归实现）
     * @param tree
     */
    private void destroy(RBTNode<T> tree){
        if (tree == null)   return;
        if (tree.left !=  null)
            destroy(tree.left);
        if (tree.right != null)
            destroy(tree.right);
        tree = null;
    }
    public void clear(){
        destroy(mRoot);
        mRoot = null;
    }

    /**
     * 打印"红黑树"
     *
     *
     * @param tree
     * @param key
     * @param direction
     */
    private void print(RBTNode<T> tree, T key, int direction) {
        if (tree != null){
            if (direction == 0) {   // tree 是根节点
                System.out.printf("%2d(B) is root\n", tree.key);
            } else {                // tree 是分支节点
                System.out.printf("%2d(%s) is %2d's %6s child\n", tree.key, isRed(tree)?"R":"B", key, direction==1?"right" : "left");
            }
            print(tree.left, tree.key, -1);
            print(tree.right, tree.key, 1);
        }
    }
    public void print(){
        if (mRoot != null)
            print(mRoot, mRoot.key, 0);
    }
    /**
     * 树节点
     * @param <T>
     */
    public class RBTNode<T extends Comparable<T>> {
        boolean color;      // 颜色标记
        T key;              // 关键字(键值)
        RBTNode<T> left;    // 左孩子
        RBTNode<T> right;   // 右孩子
        RBTNode<T> parent;  // 父节点

        public RBTNode(T key, boolean color, RBTNode<T> parent, RBTNode<T> left, RBTNode<T> right) {
            this.key = key;
            this.color = color;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
        public T getKey() {
            return key;
        }
        public String toString() {
            return ""+key+(this.color==RED?"(R)":"B");
        }
    }
}
