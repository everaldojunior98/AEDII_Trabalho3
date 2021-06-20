import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class Main
{
    private static String path;

    public static void main(String[] args)
    {
        path = null;
        //Criando a UI
        var frame = new JFrame("Trabalho3 - AED2 - Everaldo Junior");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(365,220);
        frame.setResizable(false);

        var progressBar = new JProgressBar();

        var label1 = new JLabel("ALGORITMOS E ESTRUTURAS DE DADOS II");
        label1.setBounds(55,5,240, 10);
        frame.add(label1);

        var label2 = new JLabel("TRABALHO 3 - ALGORITMO DE HUFFMAN");
        label2.setBounds(60,20,240, 10);
        frame.add(label2);

        var label3 = new JLabel("ARQUIVO DE ENTRADA .TXT:");
        label3.setBounds(5,50,180, 10);
        frame.add(label3);

        var fileButton = new JButton("SELECIONE");
        fileButton.setBounds(5,65,100, 40);
        fileButton.addActionListener(fileActionListener ->
        {
            //Cria o file picker
            var filePicker = new JFileChooser();
            //Colocar para apenas arquivos
            filePicker.setFileSelectionMode(JFileChooser.FILES_ONLY);

            //Cria um filtro pra arquivo txt
            filePicker.addChoosableFileFilter(new FileFilter()
            {
                @Override
                public boolean accept(File file)
                {
                    var extension = "";
                    var filename = file.getName();
                    int i = filename.lastIndexOf('.');

                    if (i > 0 &&  i < filename.length() - 1)
                        extension = filename.substring(i + 1).toLowerCase();

                    return extension.equals("txt");
                }

                @Override
                public String getDescription()
                {
                    return null;
                }
            });
            filePicker.addActionListener(fileActionEvent -> path = filePicker.getSelectedFile() == null ? null : filePicker.getSelectedFile().getPath());

            //Habilita o filtro
            filePicker.setAcceptAllFileFilterUsed(false);
            //Abre o file picker
            filePicker.showOpenDialog(frame);
        });
        frame.add(fileButton);

        var compressButton = new JButton("COMPRIMIR");
        compressButton.setBounds(110,65,105, 40);
        compressButton.addActionListener(compressActionListener ->
        {
            if(path == null)
            {
                JOptionPane.showMessageDialog(frame, "Você deve selecionar um arquivo primeiro");
                return;
            }

            //Cria uma task para rodar a compressão
            var task = new Runnable()
            {
                public void run()
                {
                    //Instancia o compressor
                    var huffmanCompress = new Huffman(path, progress ->
                    {
                        progressBar.setValue(progress);
                        return null;
                    }, errorMessage ->
                    {
                        //Reseta o file e o path
                        progressBar.setValue(0);
                        path = null;

                        JOptionPane.showMessageDialog(frame, errorMessage);
                        return null;
                    }, successMessage ->
                    {
                        //Reseta o file e o path
                        progressBar.setValue(0);
                        path = null;

                        JOptionPane.showMessageDialog(frame, successMessage);
                        return null;
                    });
                    huffmanCompress.Compress();
                }
            };
            new Thread(task).start();
        });
        frame.add(compressButton);

        var decompressButton = new JButton("DESCOMPRIMIR");
        decompressButton.setBounds(220,65,125, 40);
        decompressButton.addActionListener(compressActionListener ->
        {
            if(path == null)
            {
                JOptionPane.showMessageDialog(frame, "Você deve selecionar um arquivo primeiro");
                return;
            }

            //Cria uma task para rodar a descompressão
            var task = new Runnable()
            {
                public void run()
                {
                    //Instancia o compressor
                    var huffmanCompress = new Huffman(path, progress ->
                    {
                        progressBar.setValue(progress);
                        return null;
                    }, errorMessage ->
                    {
                        //Reseta o file e o path
                        progressBar.setValue(0);
                        path = null;

                        JOptionPane.showMessageDialog(frame, errorMessage);
                        return null;
                    }, successMessage ->
                    {
                        //Reseta o file e o path
                        progressBar.setValue(0);
                        path = null;

                        JOptionPane.showMessageDialog(frame, successMessage);
                        return null;
                    });
                    huffmanCompress.Decompress();
                }
            };
            new Thread(task).start();
        });
        frame.add(decompressButton);

        var label4 = new JLabel("PROGRESSO:");
        label4.setBounds(5,110,105, 10);
        frame.add(label4);

        progressBar.setStringPainted(true);
        progressBar.setBounds(5,125,340, 25);
        progressBar.setValue(0);
        frame.add(progressBar);

        var label5 = new JLabel("DESENVOLVIDO POR EVERALDO JUNIOR");
        label5.setBounds(60,160,230, 10);
        frame.add(label5);

        frame.setLayout(null);
        frame.setVisible(true);
    }
}