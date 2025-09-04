import java.util.*;

class ImprovedUserSolution {
    
    static class Student {
        int id, grade, score;
        boolean isMale;
        
        Student(int id, int grade, int score, boolean isMale) {
            this.id = id;
            this.grade = grade;
            this.score = score;
            this.isMale = isMale;
        }
    }
    
    Map<Integer, Student> students = new HashMap<>();
    Map<String, TreeSet<Student>> groups = new HashMap<>();
    
    static final Comparator<Student> COMP = 
        Comparator.comparingInt((Student s) -> s.score).thenComparingInt(s -> s.id);
    
    public void init() {
        students.clear();
        groups.clear();
    }
    
    private String getKey(int grade, boolean isMale) {
        return grade + (isMale ? "M" : "F");
    }
    
    public int add(int mId, int mGrade, char mGender[], int mScore) {
        boolean isMale = mGender[0] == 'm';
        Student student = new Student(mId, mGrade, mScore, isMale);
        
        students.put(mId, student);
        
        String key = getKey(mGrade, isMale);
        groups.computeIfAbsent(key, k -> new TreeSet<>(COMP)).add(student);
        
        return groups.get(key).last().id;
    }
    
    public int remove(int mId) {
        Student student = students.remove(mId);
        if (student == null) return 0;
        
        String key = getKey(student.grade, student.isMale);
        TreeSet<Student> group = groups.get(key);
        group.remove(student);
        
        return group.isEmpty() ? 0 : group.first().id;
    }
    
    public int query(int mGradeCnt, int mGrade[], int mGenderCnt, char mGender[][], int mScore) {
        Student best = null;
        
        for (int i = 0; i < mGradeCnt; i++) {
            for (int j = 0; j < mGenderCnt; j++) {
                String key = getKey(mGrade[i], mGender[j][0] == 'm');
                TreeSet<Student> group = groups.get(key);
                
                if (group == null) continue;
                
                Student candidate = group.ceiling(new Student(0, 0, mScore, false));
                if (candidate != null && (best == null || COMP.compare(candidate, best) < 0)) {
                    best = candidate;
                }
            }
        }
        
        return best == null ? 0 : best.id;
    }
}
