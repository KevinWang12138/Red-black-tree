public class RBTree<K extends Comparable<K>,V> {
    private static final boolean RED = true;
    private static final boolean BLACK = true;
    private RBNode root;

    /**
     * 获取当前节点的父节点
     */
    public RBNode parentOf(RBNode node){
        if(node!=null){
            return node.parent;
        }
        return null;
    }

    public boolean isRed(RBNode node){
        return node.color==RED;
    }

    public boolean isBlACK(RBNode node){
        return node.color==BLACK;
    }

    public void setRed(RBNode node){
        if(node!=null){
            node.color=RED;
        }
    }

    public void setBlack(RBNode node){
        if(node!=null){
            node.color=BLACK;
        }
    }

    /**
     * 中序打印二叉树
     */
    public void inOrderPrint(){
        inOrderPrint(this.root);
    }

    private void inOrderPrint(RBNode root){
        if(root!=null){
            inOrderPrint(root.left);
            System.out.println(root.value);
            inOrderPrint(root.right);
        }
    }

    /**
     * 左旋
     */
    private void leftRotate(RBNode x){
        RBNode p=x.parent;
        RBNode y=x.right;
        if(p!=null){
            if(p.left==x){
                p.left=y;
            }else{
                p.right=y;
            }
            y.parent=p;
        }else{
            this.root=y;
        }
        x.right=y.left;
        if(y.left!=null){
            y.left.parent=x;
        }
        y.left=x;
        x.parent=y;
    }

    private void rightRotate(RBNode y){
        RBNode p=y.parent;
        RBNode x=y.left;
        if(p!=null){
            if(p.left==y){
                p.left=x;
            }else{
                p.right=x;
            }
            x.parent=p;
        }else{
            this.root=x;
        }
        y.left=x.right;
        if(x.right!=null){
            x.right.parent=y;
        }
        x.right=y;
        y.parent=x;
    }

    public void insert(K key,V value){
        RBNode node =new RBNode();
        node.setKey(key);
        node.setValue(value);
        node.setColor(RED);
        insert(node);
    }

    private void insert(RBNode node){
        //查找node的父节点
        RBNode parent=null;
        RBNode x=this.root;
        while(x!=null){
            parent=x;
            int cmp=node.key.compareTo(x.key);
            if(cmp==0){
                x.setValue(node.getValue());
            }else if(cmp>0){
                x=x.right;
            }else{
                x=x.left;
            }
        }
        node.parent=parent;

        //判断node和parent的值，决定node插在左边还是右边
        if(parent!=null){
            int cmp=node.key.compareTo(parent.key);
            if(cmp>0){
                parent.right=node;
            }else{
                parent.left=node;
            }
        }else{
            this.root=node;
        }

        //调用修复红黑树平衡性的方法

    }

    private void insertFixup(RBNode node){
        //1.插入的节点是根节点--将根节点染黑
        if(node==this.root){
            this.root.setColor(BLACK);
            return;
        }
        //2.插入的节点的key已在树中--修改value，上面已经实现，不需要写

        //3.插入的节点的父节点是黑色--不需要修改，不影响红黑树性质

        //4.插入的节点的父节点是红色
        if(isRed(node.parent)){
            //4.1叔叔节点存在，并且为红色--父亲节点和叔叔节点染黑，爷爷节点染红，以爷爷节点为新节点重新修复
            if(node.parent.parent.left!=null&&node.parent.parent.right!=null&&isRed(node.parent.parent.left)&&isRed(node.parent.parent.right)){
                node.parent.parent.left.setColor(BLACK);
                node.parent.parent.right.setColor(BLACK);
                node.parent.setColor(RED);
                insertFixup(node.parent.parent);
                return;
            }
            //4.2父亲节点是爷爷节点的左节点，并且叔叔节点是黑色或者为空
            if(node.parent.parent.left==node.parent){
                if(node.parent.left==node){
                    //4.2.1如果插入的节点是父亲节点的左节点--父亲节点染黑，爷爷节点染红，以爷爷节点为轴进行右旋
                    node.parent.setColor(BLACK);
                    node.parent.parent.setColor(RED);
                    rightRotate(node.parent.parent);
                }else{
                    //4.2.2如果插入的节点是父亲节点的右节点--以父亲节点为轴进行左旋，然后自己染黑，爷爷染红，以爷爷为轴进行右旋
                    RBNode p=node.parent;
                    RBNode pp=p.parent;
                    leftRotate(p);
                    node.setColor(BLACK);
                    pp.setColor(RED);
                    rightRotate(pp);
                }
            }
            //4.3父亲节点是爷爷节点的左节点，并且叔叔节点是黑色或者为空
            if(node.parent.parent.right==node.parent){
                if(node.parent.left==node){
                    //4.3.1如果插入的节点是父亲节点的左节点--以父亲节点为轴进行右旋，然后将自己染黑，爷爷染红，以爷爷节点为轴进行左旋
                    RBNode p=node.parent;
                    RBNode pp=p.parent;
                    rightRotate(p);
                    node.setColor(BLACK);
                    pp.setColor(RED);
                    leftRotate(pp);
                }else{
                    //4.3.2如果插入的节点是父亲节点的右节点--父亲节点染黑，爷爷节点染红，以爷爷节点为轴进行左旋
                    node.parent.setColor(BLACK);
                    node.parent.parent.setColor(RED);
                    leftRotate(node.parent.parent);
                }
            }

        }
    }

    static class RBNode <K extends Comparable<K>,V>{
        private RBNode parent;
        private RBNode left;
        private RBNode right;
        private boolean color;
        private K key;
        private V value;

        public RBNode() {
        }

        public RBNode(RBNode parent, RBNode left, RBNode right, boolean color, K key, V value) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.color = color;
            this.key = key;
            this.value = value;
        }

        public RBNode getParent() {
            return parent;
        }

        public void setParent(RBNode parent) {
            this.parent = parent;
        }

        public RBNode getLeft() {
            return left;
        }

        public void setLeft(RBNode left) {
            this.left = left;
        }

        public RBNode getRight() {
            return right;
        }

        public void setRight(RBNode right) {
            this.right = right;
        }

        public boolean isColor() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
