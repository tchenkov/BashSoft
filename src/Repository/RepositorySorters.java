package Repository;

import IO.OutputWriter;
import StaticData.ExceptionMessages;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class RepositorySorters {
    public static void printSortedStudents(
            HashMap<String, ArrayList<Integer>> courseData, String comparisonType, Integer numberOfStudents)
    {
        Comparator<Map.Entry<String, ArrayList<Integer>>> comparator = (st1, st2) -> Double.compare(
                st1.getValue().stream().mapToInt(Integer::valueOf).average().getAsDouble(),
                st2.getValue().stream().mapToInt(Integer::valueOf).average().getAsDouble()
        );
        if (!comparisonType.equals("ascending") && !comparisonType.equals("descending")){
            OutputWriter.displayException(ExceptionMessages.INVALID_COMPARATOR);
            return;
        }
        
        List<String> sortedStudents = courseData.entrySet()
                .stream()
                .sorted(comparator)
                .limit(numberOfStudents)
                .map(st -> st.getKey())
                .collect(Collectors.toList());
        
        if (comparator.equals("descending")){
            Collections.reverse(sortedStudents);
        }
        
        for (String student : sortedStudents) {
            OutputWriter.printStudent(student, courseData.get(student));
        }
    }
    
//    private static Comparator<Map.Entry<String, ArrayList<Integer>>> createComparator(String comparisonType){
//        switch (comparisonType){
//            case "ascending":
//                return new Comparator<Map.Entry<String, ArrayList<Integer>>>() {
//                    @Override
//                    public int compare(Map.Entry<String, ArrayList<Integer>> o1, Map.Entry<String, ArrayList<Integer>> o2) {
//                        Map.Entry<String, ArrayList<Integer>> firstStudent = o1;
//                        Map.Entry<String, ArrayList<Integer>> secondStudent = o2;
//
//                        Double firstStudentTotal = getTotalScore(firstStudent.getValue());
//                        Double secondStudentTotal = getTotalScore(secondStudent.getValue());
//
//                        return firstStudentTotal.compareTo(secondStudentTotal);
//                    }
//                };
//            case "descending":
//                return new Comparator<Map.Entry<String, ArrayList<Integer>>>() {
//                    @Override
//                    public int compare(Map.Entry<String, ArrayList<Integer>> o1, Map.Entry<String, ArrayList<Integer>> o2) {
//                        Map.Entry<String, ArrayList<Integer>> firstStudent = o1;
//                        Map.Entry<String, ArrayList<Integer>> secondStudent = o2;
//
//                        Double firstStudentTotal = getTotalScore(firstStudent.getValue());
//                        Double secondStudentTotal = getTotalScore(secondStudent.getValue());
//
//                        return secondStudentTotal.compareTo(firstStudentTotal);
//                    }
//                };
//            default:
//                return null;
//        }
//    }
}
