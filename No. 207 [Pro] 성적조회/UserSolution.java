import java.util.*;

class UserSolution {
	
	HashMap<Integer, Integer> scoreById = new HashMap<>();
	HashMap<Integer, Integer> gradeById = new HashMap<>();
	HashMap<Integer, String> genderById = new HashMap<>();

	Grade[] students = new Grade[4];
	
	class Grade {
		TreeSet<Pair> male = new TreeSet<>(Comparator.comparingInt((Pair p) -> p.score).thenComparing(p -> p.id));
		TreeSet<Pair> female = new TreeSet<>(Comparator.comparingInt((Pair p) -> p.score).thenComparing(p -> p.id));
	}
	class Pair {
		int id, score;

		Pair(int id, int score) {
			this.id = id;
			this.score = score;
		}
	}


	public void init() {
		scoreById.clear();
		gradeById.clear();
		genderById.clear();

		students[1] = new Grade();
		students[2] = new Grade();
		students[3] = new Grade();
		return;
	}

	private String toString(char[] list) {
		int length = 0;
		for (int i = 0; i < list.length; i++) {
			if (list[i] == '\0') break;
			length++;
		}
		return new String(list, 0, length);
	}

	public int add(int mId, int mGrade, char mGender[], int mScore) {
		scoreById.put(mId, mScore);
		gradeById.put(mId, mGrade);
		genderById.put(mId, toString(mGender));

		Grade studentsByGrade = students[mGrade];
		TreeSet<Pair> studentsByGender;

		if (toString(mGender).equals("male")) studentsByGender = studentsByGrade.male;
		else studentsByGender = studentsByGrade.female;

		studentsByGender.add(new Pair(mId, mScore));
		return studentsByGender.last().id;
	}

	public int remove(int mId) {
		if (!gradeById.containsKey(mId)) return 0;

		int grade = gradeById.get(mId);
		String gender = genderById.get(mId);

		Grade studentsByGrade = students[grade];
		TreeSet<Pair> studentsByGender;

		if (gender.equals("male")) studentsByGender = studentsByGrade.male;
		else studentsByGender = studentsByGrade.female;

		studentsByGender.remove(new Pair(mId, scoreById.get(mId)));
		gradeById.remove(mId);

		if (studentsByGender.size() == 0) return 0;
		return studentsByGender.first().id;
	}

	public int query(int mGradeCnt, int mGrade[], int mGenderCnt, char mGender[][], int mScore) {
		Pair ret = new Pair(0, Integer.MAX_VALUE);

		for (int i = 0; i < mGradeCnt; i++) {
			int grade = mGrade[i];
			Grade studentsByGrade = students[grade];
			TreeSet<Pair> studentsByGender;

			for (int j = 0; j < mGenderCnt; j++) {
				if (toString(mGender[j]).equals("male")) studentsByGender = studentsByGrade.male;
				else studentsByGender = studentsByGrade.female;
				if (studentsByGender.size() == 0) continue;
				Pair first = studentsByGender.ceiling(new Pair(0, mScore));

				if (first == null) continue;
				if (ret.id == 0 || ret.score > first.score || (ret.score == first.score && ret.id > first.id)) {
					ret = first;
				}
			}
		}

		return ret.id;
	}
}