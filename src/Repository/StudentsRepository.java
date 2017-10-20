package Repository;

import IO.OutputWriter;
import StaticData.ExceptionMessages;
import StaticData.SessionData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentsRepository {
    public static boolean isDataInitialized = false;
    public static HashMap<String, HashMap<String, ArrayList<Integer>>> studentByCourse;
    
    public static void initializeData(String fileName) throws IOException {
        if (isDataInitialized){
            OutputWriter.displayException(ExceptionMessages.DATA_ALREADY_INITIALIZED);
    
            return;
        }
        
        studentByCourse = new HashMap<>();
        
        readData(fileName);
    }
    
    public static void printFilteredStudents(String course, String filter, Integer numberOfStudents){
        if (!isQueryForCoursePossible(course)){
            return;
        }
        
        if (numberOfStudents == null){
            numberOfStudents = studentByCourse.get(course).size();
        }
        
        RepositoryFilters.printFilteredStudents(studentByCourse.get(course), filter, numberOfStudents);
    }
    
    public static void printOrderedStudents(String course, String compareType, Integer numberOfStudents){
        if (!isQueryForCoursePossible(course)){
            return;
        }
        
        if (numberOfStudents == null){
            numberOfStudents = studentByCourse.get(course).size();
        }
        
        RepositorySorters.printSortedStudents(studentByCourse.get(course), compareType, numberOfStudents);
    }
    
    private static void readData(String fileName) throws IOException {
        String expression = "([A-Z][A-Za-z+#]*_[A-Z][a-z]{2}_\\d{4})\\s+([A-z][a-z]{0,3}\\d{2}_\\d{2,4})\\s+(\\d+)";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher;
        
        String path = SessionData.currentPath + "\\" + fileName;
        List<String> lines = Files.readAllLines(Paths.get(path));
        
        //while (!input.isEmpty()){
        for (String line : lines){
            matcher = pattern.matcher(line);
            
            if (!line.isEmpty() && matcher.find()){
                String course = matcher.group(1);
                String student = matcher.group(2);
                Integer mark = Integer.valueOf(matcher.group(3));
                
                // todo check month and year validity form course
                if (0 <= mark && mark <= 100){
                    studentByCourse.putIfAbsent(course, new LinkedHashMap<>());
                    studentByCourse.get(course).putIfAbsent(student, new ArrayList<>());
                    studentByCourse.get(course).get(student).add(mark);
                }
            }
        }
        
        isDataInitialized = true;
        OutputWriter.writeMessageOnNewLine("Data read.");
    }
    
    private static boolean isQueryForCoursePossible(String courseName){
        if (!isDataInitialized){
            OutputWriter.displayException(ExceptionMessages.DATA_NOT_INITIALIZED);
            return false;
        }
        
        if (!studentByCourse.containsKey(courseName)){
            OutputWriter.displayException(ExceptionMessages.NON_EXISTING_COURSE);
            return false;
        }
        
        return true;
    }
    
    private static boolean isQueryForStudentPossible(String courseName, String studentName){
        if (!isQueryForCoursePossible(courseName)){
            return false;
        }
    
        if (!studentByCourse.get(courseName).containsKey(studentName)){
            OutputWriter.displayException(ExceptionMessages.NON_EXISTING_STUDENT);
            return false;
        }
    
        return true;
    }
    
    public static void getStudentMarksInCourse (String courseName, String studentName){
        if (!isQueryForStudentPossible(courseName, studentName)){
            return;
        }
        
        ArrayList<Integer> marks = studentByCourse.get(courseName).get(studentName);
        OutputWriter.printStudent(studentName, marks);
    }
    
    public static void getStudentsByCourse (String courseName){
        if (!isQueryForCoursePossible(courseName)){
            return;
        }
        
        OutputWriter.writeMessageOnNewLine(courseName + ":");
        for (Map.Entry<String, ArrayList<Integer>> student : studentByCourse.get(courseName).entrySet()) {
            OutputWriter.printStudent(student.getKey(), student.getValue());
        }
    }
}
