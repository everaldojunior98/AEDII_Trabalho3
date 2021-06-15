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
        Percorrer(binaryTree.root);
    }

    public String currentString = "";
    public void Percorrer(TreeNode<CountByChar> node)
    {
        if (node != null)
        {
            if(node.GetData().GetCharCode() != -1)
                System.out.println(currentString + ">> \"" + ((char)node.GetData().GetCharCode()) + "\"");
            //currentString += "0";
            Percorrer(node.GetLeftNode());
            //currentString = currentString.substring(0, currentString.length() - 1);

            //currentString += "1";
            //Percorrer(node.GetRightNode());
            //currentString = currentString.substring(0, currentString.length() - 1);
        }
    }
}