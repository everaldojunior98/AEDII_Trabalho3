package com.everaldojunior.utils.list;

//Lista encadeada genérica
public class LinkedList<T>
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
}