package com.everaldojunior.utils.list;

public class Symbol implements Comparable<Symbol>
{
    //Codigo do char em analise
    private int symbol;
    //Bit referente ao char
    private int[] code;

    public Symbol(int symbol, int[] code)
    {
        this.symbol = symbol;
        this.code = code;
    }

    //Pega a quantidade de ocorrências
    public int[] GetCode()
    {
        return this.code;
    }

    //Pega o char em analise
    public int GetCharCode()
    {
        return this.symbol;
    }

    @Override
    public int compareTo(Symbol obj)
    {
        return 0;
    }
}