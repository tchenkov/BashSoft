package IO;

import StaticData.ExceptionMessages;
import StaticData.SessionData;

import java.io.File;
import java.util.*;

public class IOManager {
    public static void traverseFolder(int depth) {
        LinkedList<File> subFolders = new LinkedList<>();
        
        String path = SessionData.currentPath;
        int initialIndentation = path.split("\\\\").length;
        
        File root = new File(path);
        
        subFolders.add(root);
        
        while (subFolders.size() > 0) {
            File currentFolder = subFolders.removeFirst();
            int currentIndentation = currentFolder.toString().split("\\\\").length - initialIndentation;
            
            if (currentIndentation > depth + 1) {
                break;
            }
            Queue<String> filesList = new ArrayDeque<>();
            
            if (currentFolder.listFiles() != null) {
                try {
                    for (File file : currentFolder.listFiles()) {
                        if (file.isDirectory()) {
                            subFolders.add(file);
                        }
                        else if (currentIndentation <= depth){
                            int indexOfLastSlash = file.toString().lastIndexOf("\\");
                            
                            String fileName = Collections.nCopies(indexOfLastSlash, "-").toString().replaceAll("[\\[\\], ]", "");
                            fileName += file.getName();
                            filesList.offer(fileName);
                        }
                    }
                }
                catch (Exception e) {
                    OutputWriter.writeMessageOnNewLine("Access Denied");
                }
            }
            
            OutputWriter.writeMessageOnNewLine(currentFolder.toString());
            //
            while (!filesList.isEmpty()) {
                OutputWriter.writeMessageOnNewLine(filesList.poll());
            }
        }
        
    }
    
    public static void createDirectoryInCurrentFolder(String directoryName) {
        String path = getCurrentDirectoryPath() + "\\" + directoryName;
        File file = new File(path);
        file.mkdir();
    }
    
    public static void changeCurrentDirRelativePath (String relativePath){
        if (relativePath.equals("..")){
            String currentPath = SessionData.currentPath;
            int indexOfLastSlash = currentPath.lastIndexOf("\\");
            String newPath = currentPath.substring(0, indexOfLastSlash);
            
            SessionData.currentPath = newPath;
        }
        else {
            String currentPath = SessionData.currentPath;
            currentPath += "\\" + relativePath;
            
            changeCurrentDirAbsolute(currentPath);
        }
    }
    
    public static void changeCurrentDirAbsolute(String absolutePath) {
        // todo make case insensitive input return case sensitive output
        // todo work for folder with space in the name
        File file = new File(absolutePath);
        if (!file.exists()){
            OutputWriter.displayException(ExceptionMessages.INVALID_PATH);
            return;
        }
        
        SessionData.currentPath = absolutePath;
    }
    
    private static String getCurrentDirectoryPath() {
        String currentPath = SessionData.currentPath;
        return currentPath;
    }
}
