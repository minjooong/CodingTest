import java.util.*;

class UserSolution {

    // 차량 정보를 담을 클래스
    private class Car {
        String fullNo;      // 차량 번호 전체
        int prefix;         // 앞 2자리 숫자 (우선순위용)
        char mid;           // 중간 문자 (우선순위용)
        int suffix;         // 뒤 4자리 숫자 (검색 인덱싱용)
        
        int entryTime;      // 입차 시간
        int zone;           // 주차 구역 (0=A, 1=B ...)
        int slot;           // 슬롯 번호
        
        boolean isTowed;    // 견인 여부
        boolean isValid;    // 현재 시스템에 존재하는지 여부 (삭제된 차량 체크)

        public Car(String no, int time, int z, int s) {
            this.fullNo = no;
            this.entryTime = time;
            this.zone = z;
            this.slot = s;
            this.isTowed = false;
            this.isValid = true;

            // XXYZZZZ 파싱
            this.prefix = Integer.parseInt(no.substring(0, 2));
            this.mid = no.charAt(2);
            this.suffix = Integer.parseInt(no.substring(3));
        }
    }

    // 전역 변수 및 자료구조
    private int N, M, L;
    private int[] emptyCnt;                 // 각 구역별 빈 슬롯 개수
    private PriorityQueue<Integer>[] slots; // 각 구역별 사용 가능한 슬롯 번호 (Min Heap)
    private HashMap<String, Car> carMap;    // 차량 번호 -> 차량 객체 매핑
    private PriorityQueue<Car> towingPQ;    // 견인 시간 순으로 정렬된 큐
    private ArrayList<Car>[] suffixTable;   // 뒷 4자리별 차량 리스트 (검색용)

    // 견인 시간 비교를 위한 Comparator
    private Comparator<Car> towingComp = new Comparator<Car>() {
        @Override
        public int compare(Car o1, Car o2) {
            return Integer.compare(o1.entryTime, o2.entryTime);
        }
    };

    public void init(int N, int M, int L) {
        this.N = N;
        this.M = M;
        this.L = L;

        emptyCnt = new int[N];
        slots = new PriorityQueue[N];
        for (int i = 0; i < N; i++) {
            emptyCnt[i] = M;
            slots[i] = new PriorityQueue<>();
            for (int j = 0; j < M; j++) {
                slots[i].add(j);
            }
        }

        carMap = new HashMap<>();
        towingPQ = new PriorityQueue<>(towingComp);
        
        // 뒷자리 0000~9999
        suffixTable = new ArrayList[10000];
    }

    // 현재 시간(mTime) 기준으로 견인되어야 할 차량들을 처리
    private void updateTowing(int mTime) {
        while (!towingPQ.isEmpty()) {
            Car c = towingPQ.peek();
            
            // 이미 출차되어 삭제된 차량이면 큐에서 제거
            if (!c.isValid) {
                towingPQ.poll();
                continue;
            }

            // 견인 시간이 아직 안 됐으면 중단
            if (c.entryTime + L > mTime) {
                break;
            }

            // 견인 처리
            towingPQ.poll();
            if (!c.isTowed) {
                c.isTowed = true;
                // 슬롯 반납
                slots[c.zone].add(c.slot);
                emptyCnt[c.zone]++;
            }
        }
    }

    // 검색 시 우선순위 비교
    private int compareSearch(Car c1, Car c2) {
        // 1. 주차된 차량(!Towed) > 견인된 차량(Towed)
        if (c1.isTowed != c2.isTowed) {
            return c1.isTowed ? 1 : -1; // false(0) < true(1) 이므로 parked가 더 작게(우선)
        }
        // 2. 앞 숫자(XX) 작은 순
        if (c1.prefix != c2.prefix) {
            return Integer.compare(c1.prefix, c2.prefix);
        }
        // 3. 문자(Y) 빠른 순
        return Character.compare(c1.mid, c2.mid);
    }

    public Solution.RESULT_E enter(int mTime, String mCarNo) {
        Solution.RESULT_E res = new Solution.RESULT_E();
        updateTowing(mTime);

        // 이미 존재하는 차량(견인된 상태)이 재입차 하는 경우
        if (carMap.containsKey(mCarNo)) {
            Car oldCar = carMap.get(mCarNo);
            // 기존 기록 삭제 (문제 조건: 견인된 차량 번호 전달 시 기록 삭제)
            oldCar.isValid = false; 
            carMap.remove(mCarNo);
            // suffixTable에서는 lazy removal (검색 시 필터링)
        }

        // 1. 빈 슬롯이 가장 많은 구역 찾기 (동점일 경우 알파벳 순)
        int bestZone = -1;
        int maxEmpty = 0;
        
        for (int i = 0; i < N; i++) {
            if (emptyCnt[i] > maxEmpty) {
                maxEmpty = emptyCnt[i];
                bestZone = i;
            }
        }

        // 빈 슬롯이 없는 경우
        if (bestZone == -1) {
            res.success = 0;
            return res;
        }

        // 2. 해당 구역에서 가장 번호가 빠른 슬롯 배정
        int bestSlot = slots[bestZone].poll();
        emptyCnt[bestZone]--;

        // 차량 객체 생성 및 등록
        Car newCar = new Car(mCarNo, mTime, bestZone, bestSlot);
        carMap.put(mCarNo, newCar);
        towingPQ.add(newCar);
        
        if (suffixTable[newCar.suffix] == null) {
            suffixTable[newCar.suffix] = new ArrayList<>();
        }
        suffixTable[newCar.suffix].add(newCar);

        // 결과 반환 문자열 생성
        res.success = 1;
        // locname: Zone(char) + Slot(3자리 숫자)
        char zoneChar = (char)('A' + bestZone);
        // String.format은 느릴 수 있으므로 수동 조합
        StringBuilder sb = new StringBuilder();
        sb.append(zoneChar);
        if (bestSlot < 10) sb.append("00");
        else if (bestSlot < 100) sb.append("0");
        sb.append(bestSlot);
        res.locname = sb.toString();

        return res;
    }

    public int pullout(int mTime, String mCarNo) {
        updateTowing(mTime);

        if (!carMap.containsKey(mCarNo)) {
            return -1;
        }

        Car c = carMap.get(mCarNo);
        if (!c.isValid) return -1; // 방어 코드

        int fee = 0;
        if (!c.isTowed) {
            // 주차 중인 경우: 주차 기간 반환
            fee = mTime - c.entryTime;
            // 슬롯 반납
            slots[c.zone].add(c.slot);
            emptyCnt[c.zone]++;
        } else {
            // 견인된 경우: 계산식에 따라 반환
            // 주차기간(L) + 견인기간(mTime - (entryTime+L)) * 5
            int parkedTime = L;
            int towedTime = mTime - (c.entryTime + L);
            fee = -1 * (parkedTime + towedTime * 5);
            // 슬롯은 이미 updateTowing에서 반납되었음
        }

        // 기록 삭제
        c.isValid = false;
        carMap.remove(mCarNo);

        return fee;
    }

    public Solution.RESULT_S search(int mTime, String mStr) {
        Solution.RESULT_S res = new Solution.RESULT_S();
        updateTowing(mTime);

        int searchSuffix = Integer.parseInt(mStr);
        ArrayList<Car> bucket = suffixTable[searchSuffix];

        if (bucket == null || bucket.isEmpty()) {
            res.cnt = 0;
            return res;
        }

        // 후보군 추리기 및 유효하지 않은 차량 제거 (Lazy cleaning)
        ArrayList<Car> candidates = new ArrayList<>();
        // Iterator를 사용하여 안전하게 삭제
        Iterator<Car> it = bucket.iterator();
        while (it.hasNext()) {
            Car c = it.next();
            if (!c.isValid) {
                // 완전히 삭제된 차량은 리스트에서도 제거하여 최적화
                it.remove(); 
            } else {
                candidates.add(c);
            }
        }

        // 우선순위 정렬
        Collections.sort(candidates, (o1, o2) -> compareSearch(o1, o2));

        // 상위 5개 추출
        res.cnt = Math.min(candidates.size(), 5);
        for (int i = 0; i < res.cnt; i++) {
            res.carlist[i] = candidates.get(i).fullNo;
        }

        return res;
    }
}
