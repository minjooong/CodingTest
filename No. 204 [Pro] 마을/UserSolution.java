import java.util.*;

class UserSolution {

    int L, R;
    int count;
    HashMap<Integer, House> houses = new HashMap<>();
    HashMap<Integer, City> cities = new HashMap<>();

    @SuppressWarnings("unchecked")
    List<Integer>[][] map = new ArrayList[100][100];

    class House {
        int houseId;
        int x, y;
        int cityId = -1;
        boolean isRemoved = false;

        House(int houseId, int x, int y) {
            this.houseId = houseId;
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            House h = (House) o;
            if (h.houseId == this.houseId) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return houseId;
        }
    }
    class City {
        int cityId;
        PriorityQueue<Integer> houses;
        int houseCount = 0;

        City(int cityId) {
            this.cityId = cityId;
            houses = new PriorityQueue<>();
        }

        @Override
        public boolean equals(Object o) {
            City c = (City) o;
            if (c.cityId == this.cityId) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return cityId;
        }
    }

	public void init(int L, int R) {
        this.L = L;
        this.R = R;
        count = 0;
        houses.clear();
        cities.clear();
        
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                map[i][j] = new ArrayList<>();
            }
        }

		return;
	}

    int[] dx = new int[] {-1, 0, 1, -1, 0, 1, -1, 0, 1};
    int[] dy = new int[] {1, 1, 1, 0, 0, 0, -1, -1, -1};
	public int add(int mId, int mX, int mY) {
        House newHouse = new House(mId, mX, mY);
        houses.put(newHouse.houseId, newHouse);
        int x = mX / L;
        int y = mY / L;
        map[x][y].add(mId);
        int res = 0;

        for (int i = 0; i < 9; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (nx < 0 || nx > 100 || ny < 0 || ny > 100) continue;
            
            for (int houseId : map[nx][ny]) {
                House house = houses.get(houseId);
            
                if (house.houseId == newHouse.houseId) continue;
                if (house.cityId == newHouse.cityId) continue;
                if (house.isRemoved) continue;

                int distance = Math.abs(house.x - newHouse.x) + Math.abs(house.y - newHouse.y);
                if (distance <= L) {
                    if (newHouse.cityId == -1) {
                        newHouse.cityId = house.cityId;
                        City city = cities.get(newHouse.cityId);
                        city.houses.offer(newHouse.houseId);
                        res = ++city.houseCount;
                    }
                    else {
                        City city1 = cities.get(newHouse.cityId);
                        City city2 = cities.get(house.cityId);

                        int size = city1.houses.size();
                        for (int j = 0; j < size; j++) {
                            House dupHouse = houses.get(city1.houses.poll());
                            dupHouse.cityId = city2.cityId;
                            city2.houses.offer(dupHouse.houseId);
                        }
                        cities.remove(city1.cityId);

                        res = city2.houseCount += city1.houseCount;
                    }
                }
            }

        }
        if (newHouse.cityId == -1) {
            City newCity = new City(count++);
            newCity.houses.offer(newHouse.houseId);
            cities.put(newCity.cityId, newCity);
            newHouse.cityId = newCity.cityId;
            res = ++newCity.houseCount;
        }

		return res;
	}

	public int remove(int mId) {
        House remHouse = houses.get(mId);
        if (remHouse == null || remHouse.isRemoved) return -1;
        remHouse.isRemoved = true;

        City city = cities.get(remHouse.cityId);
        int res = --city.houseCount;
        if (res == 0) {
            cities.remove(city.cityId);
        }
		return res;
	}

	public int check(int mId) {
        House chkHouse = houses.get(mId);
        if (chkHouse == null || chkHouse.isRemoved) return -1;

        int houseId = cities.get(chkHouse.cityId).houses.poll();
        while (houses.get(houseId).isRemoved) {
            houseId = cities.get(houses.get(mId).cityId).houses.poll();
        }
        cities.get(houses.get(mId).cityId).houses.offer(houseId);
		return houseId;
	}

	public int count() {
		return cities.size();
	}
}