package com.everaldojunior.utils.list;

import com.everaldojunior.utils.tree.TreeNode;

//Lista encadeada genérica
public class LinkedList<T extends Comparable<T>>
{
    private ListNode<T> firstNode;
    private ListNode<T> lastNode;
    private int length;

    public LinkedList()
    {
        this.firstNode = null;
        this.lastNode = null;
        this.length = 0;
    }

    public int GetLength()
    {
        return this.length;
    }

    public ListNode<T> GetFirstNode()
    {
        return this.firstNode;
    }

    public void Add(T item)
    {
        var newNode = new ListNode<>(item, null);

        //Checa se a lista está vazia, caso esteja preenche o primeiro node
        if (this.firstNode == null)
        {
            this.firstNode = newNode;
        }
        else
        {
            this.lastNode.SetNextNode(newNode);
        }

        //Atualiza o final da lista e incrementa o comprimento
        this.lastNode = newNode;
        this.length++;
    }

    public void Remove(T item)
    {
        if(item == null)
            return;

        var currentNode = this.firstNode;
        ListNode<T> lastNode = null;

        //Percorre todos os elementos da lista e deleta o primeiro que combinar
        while (currentNode != null)
        {
            if(currentNode.GetData() == item)
            {
                var nextNode = currentNode.GetNextNode();
                //Checa se existe um node anterior e se tiver ele já atualiza pro proximo node
                if(lastNode != null)
                    lastNode.SetNextNode(nextNode);

                //Verificacoes do head da lista
                if(currentNode == this.firstNode)
                    this.firstNode = nextNode;
                if(currentNode == this.lastNode)
                    this.lastNode = lastNode;

                length--;
                break;
            }

            lastNode = currentNode;
            currentNode = currentNode.GetNextNode();
        }
    }

    //Ordena a lista de forma generica
    public void Sort()
    {
        //Faz o sort e atualiza o primeiro nó
        this.firstNode = Sort(this.firstNode);

        var currentNode = this.firstNode;
        ListNode<T> lastNode = null;

        //Percorre todos os elementos da lista e preenche o ultimo nó
        while (currentNode != null)
        {
            lastNode = currentNode;
            currentNode = currentNode.GetNextNode();
        }

        this.lastNode = lastNode;
    }

    private ListNode<T> SwapNodes(ListNode<T> node1, ListNode<T> node2)
    {
        node1.SetNextNode(node2.GetNextNode());
        node2.SetNextNode(node1);
        return node2;
    }

    private ListNode<T> Sort(ListNode<T> start)
    {
        if (start == null)
            return null;
        //Manda os valores maiores para baixo
        if (start.GetNextNode() != null && start.GetData().compareTo(start.GetNextNode().GetData()) > 0)
            start = SwapNodes(start, start.GetNextNode());
        start.SetNextNode(Sort(start.GetNextNode()));
        //Sobe com os valores menores
        if (start.GetNextNode() != null && start.GetData().compareTo(start.GetNextNode().GetData()) > 0)
        {
            start = SwapNodes(start, start.GetNextNode());
            start.SetNextNode(Sort(start.GetNextNode()));
        }

        return start;
    }
}