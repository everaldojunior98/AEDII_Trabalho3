import com.everaldojunior.utils.list.CountByChar;
import com.everaldojunior.utils.list.LinkedList;
import com.everaldojunior.utils.list.Symbol;
import com.everaldojunior.utils.tree.TreeNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.BitSet;

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
    }

    public void Compress()
    {
        //Funcionamento
        //1 - Contar a frequência dos símbolos
        //2 - Montar a arvore binaria, agrupando os simbolos por sua frequencia
        //3 - Percorrer a arvore para montar o dicionario com o novo codigo de cada simbolo
        //4 - Recodificar os dados usando esse dicionario

        this.currentSymbol = "";
        this.symbolsTable = new LinkedList<>();

        //Quantidade de ocorrência por char
        var countByChar = new LinkedList<TreeNode<CountByChar>>();
        StringBuilder fileContent = new StringBuilder();

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
                fileContent.append((char) content);

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
            System.out.println("ERRO: Não foi possível ler o arquivo no caminho: " + inputPath);
            return;
        }

        //Adiciona EOF na lista
        var data = new TreeNode<>(new CountByChar(Character.UNASSIGNED));
        countByChar.Add(data);

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

        //Percorre a arvore e monta a tabela de simbolos
        GenerateSymbolTable(countByChar.GetFirstNode().GetData());

        //Recodificando usando a tabela de simbolos
        var fileContentCharArray = fileContent.toString().toCharArray();
        var fileCompactedBits = new LinkedList<Integer>();

        //Percorre cada char do arquivo e adiciona na lista de simbolos
        for (var i = 0; i < fileContentCharArray.length; i++)
        {
            var currentNode = this.symbolsTable.GetFirstNode();
            while (currentNode != null)
            {
                //Encontra a letra e seu codigo na lista tabela de simbolos
                if(currentNode.GetData().GetCharCode() == fileContentCharArray[i])
                {
                    var code = currentNode.GetData().GetCode();
                    for (var j = 0; j < code.length; j++)
                        fileCompactedBits.Add(code[j]);
                    break;
                }
                currentNode = currentNode.GetNextNode();
            }
        }

        //Adiciona o EOF no final do arquivo
        var tempNode = this.symbolsTable.GetFirstNode();
        while (tempNode != null)
        {
            //Encontra a letra e seu codigo na lista tabela de simbolos
            if(tempNode.GetData().GetCharCode() == Character.UNASSIGNED)
            {
                var code = tempNode.GetData().GetCode();
                for (var j = 0; j < code.length; j++)
                    fileCompactedBits.Add(code[j]);
                break;
            }
            tempNode = tempNode.GetNextNode();
        }

        //Converte os bits de linked list pra array
        var fileCompacted = new int[fileCompactedBits.GetLength()];
        var currentNode = fileCompactedBits.GetFirstNode();
        var i = 0;
        while (currentNode != null)
        {
            fileCompacted[i] = currentNode.GetData();
            currentNode = currentNode.GetNextNode();
            i++;
        }

        var node = this.symbolsTable.GetFirstNode();
        var bitCount = 0;
        while (node != null)
        {
            bitCount += node.GetData().GetCode().length;
            node = node.GetNextNode();
        }

        //Salva os dados comprimidos
        try (var stream = new FileOutputStream(outputPath))
        {
            //Montando o header
            //Quantidade de caracteres da tabela de simbolos
            var symbolsBits = new int[bitCount];
            var charCount = (byte)this.symbolsTable.GetLength();
            stream.write(charCount);

            //Escreve cada caracter e o tamanho do codigo
            node = this.symbolsTable.GetFirstNode();
            var currentBitPosition = 0;
            while (node != null)
            {
                var symbol = (byte)node.GetData().GetCharCode();
                stream.write(symbol);
                var code = node.GetData().GetCode();
                var codeSize = (byte)code.length;
                stream.write(codeSize);

                for (i = 0; i < code.length; i++)
                {
                    symbolsBits[currentBitPosition] = code[i];
                    currentBitPosition++;
                }

                node = node.GetNextNode();
            }

            //Salva os códigos de cada caracter
            var codeBytes = EncodeToByteArray(symbolsBits);
            stream.write(codeBytes);

            //Converte o arquivo compactado para byte[] e salva
            var fileCompactedBytes = EncodeToByteArray(fileCompacted);
            stream.write(fileCompactedBytes);
        }
        catch (IOException e)
        {
            System.out.println("ERRO: Não foi possível comprimir o arquivo.");
        }
    }

    public void Decompress()
    {
        byte[] fileBytes;
        //Le o arquivo para descomprimir
        try
        {
            fileBytes = Files.readAllBytes(Paths.get(inputPath));
        }
        catch (IOException e)
        {
            System.out.println("ERRO: Não foi possível ler o arquivo no caminho: " + inputPath);
            return;
        }

        //Pega o tamanho do header
        var headerSize = (int)fileBytes[0];

        //Transforma os bytes do arquivo em bits
        var bits = BitSet.valueOf(fileBytes);

        //Posição para começar a ler os bits
        var currentBitPosition = ((headerSize * 2) + 1) * 8;

        //Lista com os simbolos
        var symbols = new LinkedList<Symbol>();

        //Lê o header e monta a tabela de simbolos
        for (var i = 1; i <= headerSize * 2; i+= 2)
        {
            var symbolCode = (int)fileBytes[i];
            var codeSize = (int)fileBytes[i + 1];
            var code = new int[codeSize];

            for (var j = 0; j < codeSize; j++)
            {
                code[j] = bits.get(currentBitPosition) ? 1 : 0;
                currentBitPosition++;
            }

            var symbol = new Symbol(symbolCode, code);
            symbols.Add(symbol);
        }

        //Calcula a posição inicial dos dados
        var bodyByteIndex = currentBitPosition % 8 > 0 ? (currentBitPosition / 8) + 1 : currentBitPosition / 8;
        var bodyBits = BitSet.valueOf(Arrays.copyOfRange(fileBytes, bodyByteIndex, fileBytes.length));

        //Faz o parser da string
        var decodedString = new StringBuilder();
        var buffer = new StringBuilder();
        for (var i = 0; i < bodyBits.length(); i++)
        {
            buffer.append(bodyBits.get(i) ? "1" : "0");

            var tempNode = symbols.GetFirstNode();
            while (tempNode != null)
            {
                var internalBuffer = new StringBuilder();
                for(var j = 0; j < tempNode.GetData().GetCode().length; j++)
                    internalBuffer.append(tempNode.GetData().GetCode()[j]);

                if(internalBuffer.toString().equals(buffer.toString()))
                {
                    buffer.setLength(0);
                    decodedString.append((char) tempNode.GetData().GetCharCode());
                    break;
                }

                tempNode = tempNode.GetNextNode();
            }
        }

        //Salvar o arquivo descomprimido
        try
        {
            var outputFile = new File(outputPath);
            outputFile.createNewFile();

            var stream = new FileOutputStream(outputPath);
            stream.write(decodedString.toString().getBytes());
            stream.close();
        }
        catch (IOException e)
        {
            System.out.println("ERRO: Não foi possível descomprimir o arquivo.");
        }
    }

    //Converte array de bits para arrayde bytes
    private byte[] EncodeToByteArray(int[] bits)
    {
        var bitSet = new BitSet(bits.length);
        for (var i = 0; i < bits.length; i++)
            if (bits[i] == 1)
                bitSet.set(i);

        return bitSet.toByteArray();
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
                var symbol = new Symbol(node.GetData().GetCharCode(), bitArray);
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