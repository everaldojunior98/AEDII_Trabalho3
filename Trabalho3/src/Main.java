public class Main
{
    public static void main(String[] args)
    {
        var inputPath = "src/files/input.txt";
        var outputPath = "src/files/output.txt";

        var huffman = new Huffman(inputPath, outputPath);
        huffman.Compress();
    }
}
