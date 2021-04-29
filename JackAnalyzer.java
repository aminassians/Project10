package nand2P10;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JackAnalyzer {

    public static void main(String[] args) 
    {
        File jackFile = new File(args[0]);
        File fileOut;
        String fileNameOut = "";
        ArrayList<File> files = new ArrayList<>();
        if (jackFile.isFile() && args[0].endsWith(".jack")) 
        {
            files.add(jackFile);
            fileNameOut = args[0].substring(0, args[0].length() - 5);

        }
        else if (jackFile.isDirectory()) 
        {
            files = getJackFiles(jackFile);
            fileNameOut = args[0];

        }
        fileNameOut = fileNameOut + ".xml";

        fileOut = new File(fileNameOut);
        FileWriter fileWriter = null;
        try 
        {
            fileWriter = new FileWriter(fileOut);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        for (File file : files) 
        {
            String fileOutName = file.toString().substring(0, file.toString().length() - 5) + ".xml";
            File fileOutFile = new File(fileOutName);
            
            CompilationEngine compilationEngine = new CompilationEngine(file, fileOutFile);
            compilationEngine.compileClass();


        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<File> getJackFiles(File jackFile) {
        File[] files = jackFile.listFiles();
        ArrayList<File> results = new ArrayList<>();
        if (files != null) for (File file : files) {
            if (file.getName().endsWith(".jack")) results.add(file);
        }
        return results;
    }

}