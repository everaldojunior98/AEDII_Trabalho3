import com.everaldojunior.utils.list.CountByChar;
import com.everaldojunior.utils.list.LinkedList;
import com.everaldojunior.utils.list.Symbol;
import com.everaldojunior.utils.tree.BinaryTree;
import com.everaldojunior.utils.tree.TreeNode;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Huffman
{
    //Caminho dos arquivos
    private String inputPath;
    private String outputPath;
    private String currentSymbol;

    private LinkedList<Symbol> symbolsTable;

    public Huffman(String input, String output)
    {
        this.inputPath = input;
        this.outputPath = output;
        this.currentSymbol = "";
        this.symbolsTable = new LinkedList<>();
    }

    public void Compress()
    {
        //Funcionamento
        //1 - Contar a frequência dos símbolos
        //2 - Montar a arvore binaria, agrupando os simbolos por sua frequencia
        //3 - Percorrer a arvore para montar o dicionario com o novo codigo de cada simbolo
        //4 - Recodificar os dados usando esse dicionario

        //Quantidade de ocorrência por char
        var countByChar = new LinkedList<TreeNode<CountByChar>>();
        var fileContent = "";

        //Carrega o arquivo a ser comprimido
        var file = new File(inputPath);
        try (FileReader reader = new FileReader(file))
        {
            //Lê todos os caracteres do arquivo
            int content;
            while ((content = reader.read()) != -1)
            {
                //Checa se já existe o char na lista
                var found = false;
                var currentNode = countByChar.GetFirstNode();
                fileContent += (char)content;

                //Percorre todos os elementos da lista
                while (currentNode != null)
                {
                    if(currentNode.GetData().GetData().GetCharCode() == content)
                    {
                        found = true;
                        break;
                    }

                    currentNode = currentNode.GetNextNode();
                }

                //Se encontrou ele atualiza a quantidade, se não cria e adiciona na lista
                if(found)
                {
                    currentNode.GetData().GetData().SetCount(currentNode.GetData().GetData().GetCount() + 1);
                }
                else
                {
                    var data = new TreeNode<>(new CountByChar(content));
                    countByChar.Add(data);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("ERRO: Não foi ler o arquivo no caminho: " + inputPath);
            return;
        }

        //Ordena a lista
        countByChar.Sort();

        //Montando a estrutura da arvore binaria
        //Percorre todos os elementos da lista
        while (countByChar.GetLength() > 1)
        {
            var currentNode = countByChar.GetFirstNode();
            var nextNode = currentNode.GetNextNode();

            //Remove os 2 nós de menor frequencia
            countByChar.Remove(currentNode.GetData());
            countByChar.Remove(nextNode.GetData());

            //Agrupa esses 2 nós em um nó pai
            var newTreeNode = new TreeNode<>(new CountByChar(-1));
            newTreeNode.GetData().SetCount(currentNode.GetData().GetData().GetCount() + nextNode.GetData().GetData().GetCount());

            //Verifica a posição para adicionar os nós
            newTreeNode.SetLeftNode(currentNode.GetData());
            newTreeNode.SetRightNode(nextNode.GetData());

            //Adiciona o nó pai na lista e reordena a lista
            countByChar.Add(newTreeNode);
            countByChar.Sort();
        }

        //Cria a arvore
        var binaryTree = new BinaryTree<>(countByChar.GetFirstNode().GetData());

        //Percorre a arvore e monta a tabela de simbolos
        GenerateSymbolTable(binaryTree.root);

        //Recodificando usando a tabela de simbolos
    }

    //Gera a tabela de simbolos
    public void GenerateSymbolTable(TreeNode<CountByChar> node)
    {
        if (node != null)
        {
            //Se não for soma ele adiciona na tabela
            if(node.GetData().GetCharCode() != -1)
            {
                //Convert o simbolo atual para o bit array
                var charArray= this.currentSymbol.toCharArray();
                var bitArray = new int[charArray.length];

                //Converte de char para int
                for(var i = 0; i < charArray.length; i++)
                {
                    if(charArray[i] == '1')
                        bitArray[i] = 1;
                    else
                        bitArray[i] = 0;
                }

                //Adiciona o simbolo na lista
                var symbol = new Symbol(node.GetData().GetCharCode(), null);
                this.symbolsTable.Add(symbol);
            }

            //Adiciona um 0 e percorre o nó da esquerda
            this.currentSymbol += "0";
            GenerateSymbolTable(node.GetLeftNode());
            this.currentSymbol = this.currentSymbol.substring(0, this.currentSymbol.length() - 1);

            //Adiciona um 1 e percorre o nó da direita
            this.currentSymbol += "1";
            GenerateSymbolTable(node.GetRightNode());
            this.currentSymbol = this.currentSymbol.substring(0, this.currentSymbol.length() - 1);
        }
    }
}