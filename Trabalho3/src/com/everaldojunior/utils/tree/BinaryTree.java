package com.everaldojunior.utils.tree;

public class BinaryTree<T extends Comparable<T>>
{
    //Nó raiz da arvore
    public TreeNode<T> root;

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