package IO;

import Judge.Tester;
import Repository.StudentsRepository;
import StaticData.ExceptionMessages;
import StaticData.SessionData;
import Network.DownloadManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CommandInterpreter {
    public static void interpretCommand(String input) {
        String[] data = input.split("\\s+");
        String command = data[0];
        
        switch (command) {
            case "open":
                tryOpenFile(input, data);
                break;
            case "mkdir":
                tryCreateDirectory(input, data);
                break;
            case "ls":
                tryTraverseFolder(input, data);
                break;
            case "cmp":
                tryCompareFiles(input, data);
                break;
            case "cd":
            case "changeDirRel":
                tryChangeRelativePath(input, data);
                break;
            case "changeDirAbs":
                tryChangeAbsolutePath(input, data);
                break;
            case "readDb":
                tryReadDatabaseFromFile(input, data);
                break;
            case "show":
                tryShowWantedCourse(input, data);
                break;
            case "filter":
                tryPrintFilteredStudents(input, data);
                break;
            case "order":
                tryPrintOrderStudents(input, data);
                break;
            case "help":
                printHelp();
                break;
            case "download":
                tryDownloadFile(input, data);
                break;
            case "downloadAsynch":
            case "downloadAsync":
                tryDownloadOnNewThread(input, data);
                break;
            default:
                displayInvalidCommandMessage(input);
        }
    }
    
    private static void tryOpenFile(String input, String[] data) {
        // todo open file with space in the name
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        
        String fileName = data[1];
        String filePath = SessionData.currentPath + "\\" + fileName;
        
        try {
            File file = new File(filePath);
            Desktop.getDesktop().open(file);
        }
        catch (IOException e) {
            OutputWriter.writeMessageOnNewLine(e.getMessage());
        }
    }
    
    private static void tryCreateDirectory(String input, String[] data) {
        // todo create folder with space in the name
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        
        String directoryName = data[1];
        IOManager.createDirectoryInCurrentFolder(directoryName);
    }
    
    private static void tryTraverseFolder(String input, String[] data) {
        if (data.length > 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        
        if (data.length == 1) {
            IOManager.traverseFolder(0);
            return;
        }
        // if (data.length == 2)
        try {
            int depth = Integer.parseInt(data[1]);
            IOManager.traverseFolder(depth);
        }
        catch (NumberFormatException e) {
            displayInvalidCommandMessage(input);
        }
    }
    
    private static void tryCompareFiles(String input, String[] data) {
        if (data.length != 3) {
            displayInvalidCommandMessage(input);
            return;
        }
        
        String path1 = data[1];
        String path2 = data[2];
        Tester.compareContent(path1, path2);
    }
    
    private static void tryChangeRelativePath(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        
        String directoryName = data[1];
        IOManager.changeCurrentDirRelativePath(directoryName);
    }
    
    private static void tryChangeAbsolutePath(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        
        String directoryPath = data[1];
        IOManager.changeCurrentDirAbsolute(directoryPath);
    }
    
    private static void tryReadDatabaseFromFile(String input, String[] data) {
        if (data.length != 2) {
            displayInvalidCommandMessage(input);
            return;
        }
        
        String fileName = data[1];
        try {
            StudentsRepository.initializeData(fileName);
        }
        catch (IOException e) {
            OutputWriter.writeMessageOnNewLine(e.getMessage());
        }
    }
    
    private static void tryShowWantedCourse(String input, String[] data) {
        if (data.length != 2 && data.length != 3){
            displayInvalidCommandMessage(input);
            return;
        }
        
        if (data.length == 2){
            String courseName = data[1];
            StudentsRepository.getStudentsByCourse(courseName);
            return;
        }
        // (data.length == 3)
        String courseName = data[1];
        String userName = data[2];
        StudentsRepository.getStudentMarksInCourse(courseName, userName);
    }
    
    private static void tryPrintFilteredStudents(String input, String[] data) {
        if (data.length != 3 && data.length != 4){
            displayInvalidCommandMessage(input);
            return;
        }
        
        String course = data[1];
        String filter = data[2];
        
        if (data.length == 3){
            StudentsRepository.printFilteredStudents(course, filter, null);
            return;
        }
        
        Integer numberOfStudents;
        try {
            numberOfStudents = Integer.valueOf(data[3]);
        } catch (NumberFormatException e){
            displayNotANumber(data[3]);
            return;
        }
    
        if (numberOfStudents <= 0){
            displayInvalidCount();
            return;
        }
    
        if (data.length == 4){
            StudentsRepository.printFilteredStudents(course, filter, numberOfStudents);
        }
    }
    
    private static void tryPrintOrderStudents(String input, String[] data) {
        if (data.length != 3 && data.length != 4){
            displayInvalidCommandMessage(input);
            return;
        }
    
        String course = data[1];
        String compareType = data[2];
    
        if (data.length == 3){
            StudentsRepository.printOrderedStudents(course, compareType, null);
            return;
        }
    
        Integer numberOfStudents;
        try {
            numberOfStudents = Integer.valueOf(data[3]);
        } catch (NumberFormatException e){
            displayNotANumber(data[3]);
            return;
        }
    
        if (numberOfStudents <= 0){
            displayInvalidCount();
            return;
        }
        
        if (data.length == 4){
            StudentsRepository.printOrderedStudents(course, compareType, numberOfStudents);
        }
    }
    
    private static void tryDownloadFile(String input, String[] data) {
        if (data.length != 2){
                displayInvalidCommandMessage(input);
                return;
        }
        
        String fileUrl = data[1];
        DownloadManager.download(fileUrl);
    }
    
    private static void tryDownloadOnNewThread(String input, String[] data) {
        if (data.length != 2){
            displayInvalidCommandMessage(input);
            return;
        }
    
        String fileUrl = data[1];
        DownloadManager.downloadOnNewThread(fileUrl);
    }
    
    private static void printHelp() {
        OutputWriter.writeMessageOnNewLine("mkdir path - make directory");
        OutputWriter.writeMessageOnNewLine("ls depth - traverse directory");
        OutputWriter.writeMessageOnNewLine("cmp path1 path2 - compare two files");
        OutputWriter.writeMessageOnNewLine("changeDirRel relativePath - change directory");
        OutputWriter.writeMessageOnNewLine("cd relativePath - alias of changeDirRel");
        OutputWriter.writeMessageOnNewLine("changeDir absolutePath - change directory");
        OutputWriter.writeMessageOnNewLine("readDb path - read students data base");
        OutputWriter.writeMessageOnNewLine("filter course Excellent - filter excellent students (the output is written on the console)");
        OutputWriter.writeMessageOnNewLine("filter course Excellent count - filter excellent students with count limit");
        OutputWriter.writeMessageOnNewLine("filter course Average - filter average students (the output is written on the console)");
        OutputWriter.writeMessageOnNewLine("filter course Average count - filter average students with count limit");
        OutputWriter.writeMessageOnNewLine("filter course Poor - filter low grade students (the output is on the console)");
        OutputWriter.writeMessageOnNewLine("filter course Poor count - filter low grade students with count limit");
        OutputWriter.writeMessageOnNewLine("order ascending - sort students in increasing order (the output is written on the console)");
        OutputWriter.writeMessageOnNewLine("order ascending count - sort students in increasing order with count limit");
        OutputWriter.writeMessageOnNewLine("order descending - sort students in decreasing order (the output is written on the console)");
        OutputWriter.writeMessageOnNewLine("order descending count - sort students in decreasing order with count limit");
        OutputWriter.writeMessageOnNewLine("download pathOfFile - download file (saved in current directory)");
        OutputWriter.writeMessageOnNewLine("downloadAsynch path - download file asynchronously (save in the current directory)");
        OutputWriter.writeMessageOnNewLine("downloadAsync path - alias of downloadAsynch");
        OutputWriter.writeMessageOnNewLine("show courseName (username) – user name may be omitted");
        OutputWriter.writeMessageOnNewLine("help - get help");
        OutputWriter.writeEmptyLine();
    }
    
    private static void displayInvalidCommandMessage(String input) {
        String output = String.format("The command '%s' is invalid", input);
        OutputWriter.writeMessageOnNewLine(output);
    }
    
    private static void displayNotANumber(String datum) {
        OutputWriter.writeMessage(String.format("\"%s\" ", datum));
        OutputWriter.displayException(ExceptionMessages.NOT_A_NUMBER);
    }
    
    private static void displayInvalidCount() {
        OutputWriter.displayException(ExceptionMessages.INVALID_COUNT);
    }
}
