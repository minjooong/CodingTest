class TargetNumber {
    static int dfs(int[] numbers, int target, int index, int sum) {
        int result = 0;
        int now = numbers[index];
        
        if (index == numbers.length - 1) {
            if (sum + now == target) return 1;
            if (sum - now == target) return 1;
            else return 0;
        }
        
        result += dfs(numbers, target, index + 1, sum + now);
        result += dfs(numbers, target, index + 1, sum - now);
        
        return result;
    }
    
    public int solution(int[] numbers, int target) {
        int answer = dfs(numbers, target, 0, 0);
        return answer;
    }
}