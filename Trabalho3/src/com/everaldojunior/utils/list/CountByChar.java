package com.everaldojunior.utils.list;

public class CountByChar implements Comparable<CountByChar>
{
    //Codigo do char em analise
    private int code;
    //Quantidade de ocorrência
    private int count;

    public CountByChar(int code)
    {
        this.code = code;
        this.count = 1;
    }

    //Atualiza a quantidade de ocorrência
    public void SetCount(int count)
    {
        this.count = count;
    }

    //Pega a quantidade de ocorrências
    public int GetCount()
    {
        return this.count;
    }

    //Pega o char em analise
    public int GetCharCode()
    {
        return this.code;
    }

    @Override
    public String toString()
    {
        return (code == -1 ? "+" : ("\"" + ((char)code) + "\"")) + " : " + count;
    }

    @Override
    public int compareTo(CountByChar obj)
    {
        if (this.count > obj.GetCount())
        {
            //Se for maior retorna 1
            return 1;
        }
        else if (this.count < obj.GetCount())
        {
            //Se for menor retorna -1
            return -1;
        }
        else
        {
            //Se for igual retorna 0
            return 0;
        }
    }
}