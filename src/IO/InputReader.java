package IO;

import StaticData.SessionData;

import java.util.Scanner;

public class InputReader {
    private static final String END_COMMAND = "quit";
    
    public static void readCommands(){
        OutputWriter.writeMessage(String.format("%s >", SessionData.currentPath));
    
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine().trim();
        
        while (!input.equals(END_COMMAND)){
            CommandInterpreter.interpretCommand(input);
            OutputWriter.writeMessage(String.format("%s >", SessionData.currentPath));
            
            input = scan.nextLine().trim();
        }
        
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        for (Thread thread : threads) {
            if (!thread.getName().equals("main") && !thread.isDaemon()){
                try {
                    thread.join();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
