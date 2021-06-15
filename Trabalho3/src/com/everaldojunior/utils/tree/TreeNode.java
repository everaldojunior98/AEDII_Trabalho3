package com.everaldojunior.utils.tree;

import com.everaldojunior.utils.list.CountByChar;

//Um nó genérico para uma arvore binaria
public class TreeNode<T extends Comparable<T>>  implements Comparable<TreeNode<T>>
{
    //Valor salvo no nó
    private T data;
    //Folha esquerda
    private TreeNode<T> left;
    //Folha direita
    private TreeNode<T> right;

    public TreeNode(T data)
    {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    //Atualiza a folha esquerda
    public void SetLeftNode(TreeNode left)
    {
        this.left = left;
    }

    //Pega a folha esquerda
    public TreeNode<T> GetLeftNode()
    {
        return this.left;
    }

    //Atualiza a folha direita
    public void SetRightNode(TreeNode right)
    {
        this.right = right;
    }

    //Pega a folha esquerda
    public TreeNode<T> GetRightNode()
    {
        return this.right;
    }

    //Retorna o valor do nó
    public T GetData()
    {
        return data;
    }

    //Atualiza o valor do nó
    public void SetData(T data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return this.data.toString();
    }

    @Override
    public int compareTo(TreeNode<T> o)
    {
        return this.data.compareTo(o.GetData());
    }
}