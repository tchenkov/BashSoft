package Network;

import StaticData.ExceptionMessages;
import IO.OutputWriter;
import StaticData.SessionData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadManager {
    public static void download(String fileUrl){
        URL url;
        ReadableByteChannel rbc = null;
        FileOutputStream fos = null;
        try {
            if (Thread.currentThread().getName().equals("main")){
                OutputWriter.writeMessageOnNewLine("Started downloading...");
            }
    
            url = new URL(fileUrl);
            rbc = Channels.newChannel(url.openStream());
    
    
            String fileName = extractNameOfFile(fileUrl);
            File file = new File(SessionData.currentPath + "/" + fileName);
    
            fos = new FileOutputStream(file);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    
            if (Thread.currentThread().getName().equals("main")){
                OutputWriter.writeMessageOnNewLine("Download complete.");
            }
    
            if (!"main".equalsIgnoreCase(Thread.currentThread().getName())) {
                OutputWriter.writeEmptyLine();
                OutputWriter.writeMessageOnNewLine(String.format("Download on thread %d is complete.", Thread.currentThread().getId()));
                OutputWriter.writeMessage(String.format("%s >", SessionData.currentPath));
            }
        }
        catch (MalformedURLException mfue){
            OutputWriter.displayException(mfue.getMessage());
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            try {
                if (fos != null){
                    fos.close();
                }
                
                if (rbc != null){
                    rbc.close();
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    // test link
    // downloadAsync https://upload.wikimedia.org/wikipedia/commons/3/3d/LARGE_elevation.jpg
    public static void downloadOnNewThread(String fileUrl){
        Thread thread = new Thread(() -> download(fileUrl));
        OutputWriter.writeMessageOnNewLine(String.format("Worker thread %d started downloading", thread.getId()));
        thread.setDaemon(false);
        thread.start();
    }
    
    private static String extractNameOfFile(String fileUrl) throws MalformedURLException {
        int indexOfLastSlash = fileUrl.lastIndexOf('/');
        if (indexOfLastSlash < 0){
            throw new MalformedURLException(ExceptionMessages.INVALID_PATH);
        }
        
        return fileUrl.substring(indexOfLastSlash + 1);
    }
}
