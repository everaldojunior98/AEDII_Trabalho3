package com.everaldojunior.utils.tree;

public class BinaryTree<T>
{
    //Nó raiz da arvore
    private TreeNode<T> root;

    public BinaryTree(TreeNode<T> root)
    {
        this.root = root;
    }

    //Checa se a arvore esta vazia
    public boolean isEmpty()
    {
        return root == null;
    }
}
