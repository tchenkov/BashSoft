package Judge;

import IO.OutputWriter;
import StaticData.ExceptionMessages;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tester {
    public static void compareContent(String actualOutputFilePath, String expectedOutputFilePath) {
        OutputWriter.writeMessageOnNewLine("Reading files...");
        String mismatchPath = getMismatchPath(expectedOutputFilePath);
        List<String> actualOutputString = readTextFile(actualOutputFilePath);
        List<String> expectedOutputString = readTextFile(expectedOutputFilePath);
        
        boolean mismatch = compareStrings(actualOutputString, expectedOutputString, mismatchPath);
        
        if (mismatch){
            List<String> mismatchString = readTextFile(mismatchPath);
            mismatchString.forEach(OutputWriter::writeMessageOnNewLine);
        }
        else {
            OutputWriter.writeMessageOnNewLine("Files are identical. There is no mismatch.");
        }
    }
    
    private static String getMismatchPath(String expectedOutput) {
        int index = expectedOutput.lastIndexOf('\\');
        String directoryPath = expectedOutput.substring(0, index);
        return directoryPath + "\\mismatch.txt";
    }
    
    private static List<String> readTextFile(String filePath) {
        List<String> text = new ArrayList<>();
        
        try {   // todo: test for exceptions
            File file = new File(filePath);
            
            try (   // todo: test for exceptions
                    FileReader fileReader = new FileReader(file);
                    BufferedReader br = new BufferedReader(fileReader)
            ) {
                String line = br.readLine();
                
                while (line != null) {
                    text.add(line);
                    line = br.readLine();
                }
                
            }
            catch (Exception e) {
                OutputWriter.writeMessageOnNewLine(e.toString());
            }
        }
        catch (NullPointerException npe) {
            OutputWriter.displayException("NullPointerException.");
            OutputWriter.writeMessageOnNewLine(ExceptionMessages.FILE_NOT_FOUND);
        }
        catch (Exception e) {
            OutputWriter.writeMessageOnNewLine(ExceptionMessages.FILE_NOT_FOUND);
        }
        
        return text;
    }
    
    private static boolean compareStrings(
            List<String> actualOutputString,
            List<String> expectedOutputString,
            String mismatchPath) {
        OutputWriter.writeMessageOnNewLine("Comparing files...");
        boolean isMismatch = false;
        
        try (
                FileWriter fileWriter = new FileWriter(mismatchPath);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
        ) {
            String output;
            for (int i = 0; i < expectedOutputString.size(); i++) {
                String actualLine = actualOutputString.get(i);
                String expectedLine = expectedOutputString.get(i);
                
                if (!actualLine.equals(expectedLine)){
                    output = String.format("mismatch -> expected{%s}, actual{%s}%n", expectedLine, actualLine);
                    isMismatch = true;
                }
                else {
                    output = String.format("line match -> %s%n", actualLine);
                }
                // todo: test if file is flushed and closed
                bufferedWriter.append(output);
            }
        }
        catch (Exception e) {
            OutputWriter.displayException("Error.");
        }
        
        return isMismatch;
    }
}
