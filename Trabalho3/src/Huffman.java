import com.everaldojunior.utils.list.CountByChar;
import com.everaldojunior.utils.list.LinkedList;
import com.everaldojunior.utils.list.ListNode;
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

    public Huffman(String input, String output)
    {
        this.inputPath = input;
        this.outputPath = output;
    }

    //Ordena a lista baseado no número de ocorrências
    private void OrderList(LinkedList<TreeNode<CountByChar>> list)
    {
        boolean swapped;
        ListNode<TreeNode<CountByChar>> node;
        ListNode<TreeNode<CountByChar>> lastNode = null;

        //Ordena os valores usando o algoritmo Bubble Sort
        do
        {
            swapped = false;
            node = list.GetFirstNode();

            while (node.GetNextNode() != lastNode)
            {
                if (node.GetData().GetData().GetCount() > node.GetNextNode().GetData().GetData().GetCount())
                {
                    //Atualiza os valores do nó
                    CountByChar temp = node.GetData().GetData();
                    node.GetData().SetData(node.GetNextNode().GetData().GetData());
                    node.GetNextNode().GetData().SetData(temp);

                    swapped = true;
                }
                node = node.GetNextNode();
            }
            lastNode = node;
        }
        while (swapped);
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
        OrderList(countByChar);

        //Montando a estrutura da arvore binaria
        var currentNode = countByChar.GetFirstNode();
        var nextNode = currentNode.GetNextNode();
        //Percorre todos os elementos da lista
        while (currentNode != null)
        {
            //Remove os 2 nós de moenor frequencia
            countByChar.Remove(currentNode.GetData());
            if(nextNode != null)
                countByChar.Remove(nextNode.GetData());

            //Agrupa esses 2 nós em um nó pai
            var newTreeNode = new TreeNode<>(new CountByChar(-1));
            var count = currentNode.GetData().GetData().GetCount() + (nextNode == null ? 0 : nextNode.GetData().GetData().GetCount());
            newTreeNode.GetData().SetCount(count);

            //Verifica a posição para adicionar os nós
            newTreeNode.SetLeftNode(currentNode.GetData());
            if(nextNode != null)
                newTreeNode.SetRightNode(nextNode.GetData());

            //Adiciona o nó pai na lista e reordena a lista
            countByChar.Add(newTreeNode);
            OrderList(countByChar);

            currentNode = nextNode == null ? null : nextNode.GetNextNode();
            nextNode = currentNode == null ? null : currentNode.GetNextNode();
        }

        //Cria a arvore
        var binaryTree = new BinaryTree<>(countByChar.GetFirstNode().GetData());
    }
}