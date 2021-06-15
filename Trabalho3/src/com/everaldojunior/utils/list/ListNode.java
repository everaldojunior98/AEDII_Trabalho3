package com.everaldojunior.utils.list;

//Um nó genérico
public class ListNode<T extends Comparable<T>>  implements Comparable<ListNode<T>>
{
    //Valor salvo no nó
    private T data;
    //Próximo nó
    private ListNode<T> nextNode;

    public ListNode(T data, ListNode next)
    {
        this.data = data;
        this.nextNode = next;
    }

    //Atualiza o próximo nó
    public void SetNextNode(ListNode next)
    {
        this.nextNode = next;
    }

    //Pega o próximo nó
    public ListNode<T> GetNextNode()
    {
        return this.nextNode;
    }

    //Retorna o valor salvo
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
    public int compareTo(ListNode<T> o)
    {
        return this.data.compareTo(o.GetData());
    }
}