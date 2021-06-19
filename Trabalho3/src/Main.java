public class Main
{
    public static void main(String[] args)
    {
        var inputCompressPath = "src/files/inputCompress.txt";
        var outputCompressPath = "src/files/outputCompress.txt";

        var inputDecompressPath = "src/files/inputDecompress.txt";
        var outputDecompressPath = "src/files/outputDecompress.txt";

        //var huffmanCompress = new Huffman(inputCompressPath, outputCompressPath);
        //huffmanCompress.Compress();

        var huffmanCompress = new Huffman("src/files/teste.txt", "src/files/teste2.txt");
        huffmanCompress.Compress();

        var huffmanDecompress = new Huffman("src/files/teste2.txt", "src/files/teste3.txt");
        huffmanDecompress.Decompress();
    }
}
