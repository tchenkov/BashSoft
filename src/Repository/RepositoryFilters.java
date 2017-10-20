package Repository;

import IO.OutputWriter;
import StaticData.ExceptionMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class RepositoryFilters {
    public static void printFilteredStudents(
            HashMap<String, ArrayList<Integer>> courseData, String filterType, Integer numberOfStudents)
    {
        Predicate<Double> filter = createFilter(filterType);
        
        if (filter == null){
            OutputWriter.displayException(ExceptionMessages.INVALID_FILTER);
            return;
        }
        
        int studentsCount = 0;
        boolean isResultEmpty = true;
        for (String student : courseData.keySet()) {
            if (studentsCount >= numberOfStudents){
                break;
            }
            
            ArrayList<Integer> studentMarks = courseData.get(student);
            Double averageMark = studentMarks.stream()
                    .mapToInt(Integer::valueOf)
                    .average()
                    .getAsDouble();
            Double percentageOfFulfillment = averageMark / 100;
    
            Double mark = percentageOfFulfillment * 4 + 2;
            
            if (filter.test(mark)){
                OutputWriter.printStudent(student, studentMarks);
                isResultEmpty = false;
                studentsCount++;
            }
        }
        
        if (isResultEmpty){
            OutputWriter.writeMessageOnNewLine(
                    String.format("There are no students with %s score in this course", filterType));
        }
    }
    
    private static Predicate<Double> createFilter(String filterType) {
        switch (filterType) {
            case "excellent":
                return mark -> 5.0 <= mark;
            case "average":
                return mark -> 3.5 <= mark && mark < 5.0;
            case "poor":
                return mark -> mark < 3.5;
            default:
                return null;
        }
    }
}
