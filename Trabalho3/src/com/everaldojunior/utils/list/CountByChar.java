package com.everaldojunior.utils.list;

public class CountByChar
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
}