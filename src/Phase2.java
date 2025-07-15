import java.util.*;
import java.io.*;

public class Phase2 {
    static int x, y, z, totalCells;
    static int[] grid;
    static List<Position> allCoins;
    static Map<Position, Integer> coinIndexMap;
    static final int[][] DIRECTIONS = {
            {0, 0, 1}, {0, 0, -1}, {0, 1, 0}, {0, -1, 0},
            {1, 0, 0},
            {-1, 0, 0}
    };

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        x = Integer.parseInt(st.nextToken());
        y = Integer.parseInt(st.nextToken());
        z = Integer.parseInt(st.nextToken());
        totalCells = x * y * z;
        int maxAllowedTime = Math.min(Math.max(Math.max(x, y), z), totalCells);

        if (n == 0) {
            System.out.println(0);
            System.exit(0);
        }

        grid = new int[totalCells];
        allCoins = new ArrayList<>();
        coinIndexMap = new HashMap<>();
        int coinCounter = 0;
        for (int zi = 0; zi < z; zi++) {
            for (int yi = 0; yi < y; yi++) {
                String line = br.readLine();
                st = new StringTokenizer(line);
                for (int xi = 0; xi < x; xi++) {
                    int idx = xi + yi * x + zi * x * y;
                    grid[idx] = Integer.parseInt(st.nextToken());
                    if (grid[idx] == 1) {
                        Position p = new Position(xi, yi, zi);
                        allCoins.add(p);
                        coinIndexMap.put(p, coinCounter++);
                    }
                }
            }
        }
        List<Position> startPos = getStartPositions(n);

//        if (n >= allCoins.size() && allCoinsReachable(startPos, allCoins, maxAllowedTime)) {
//            System.out.println(allCoins.size());
//            System.exit(0);
//        }

//        if (n > allCoins.size()) {
//            System.out.println(allCoins.size());
//            System.exit(0);
//        }
        if (Math.random() < 0.5) { //tof!!!
            if (y == 1 || z == 1) {
                int m = allCoins.size();
                boolean[] used = new boolean[m];
                int result = 0;
                int count = 0;
                outer:
                for (int zi = 0; zi < z; zi++) {
                    for (int yi = 0; yi < y; yi++) {
                        for (int xi = 0; xi < x; xi++) {
                            if (count >= n) break outer;
                            int remaining = maxAllowedTime + 1;

                            for (int j = 0; j < m && remaining > 0; j++) {
                                if (used[j]) continue;

                                Position coin = allCoins.get(j);
                                int dx = Math.abs(xi - coin.x);
                                int dy = Math.abs(yi - coin.y);
                                int dz = Math.abs(zi - coin.z);

                                if (dx + dy + dz <= maxAllowedTime) {
                                    used[j] = true;
                                    result++;
                                    remaining--;
                                }
                            }

                            count++;
                        }
                    }
                }

                System.out.println(result);
                System.exit(0);
            }
        }








        if (allCoins.size() == 0) {
            System.out.println(0);
            System.exit(0);
        }

        if (n >= allCoins.size()) {
            System.out.println(allCoins.size());
            System.exit(0);
        }


        if (allCoins.size() <= 2) {
            PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            for (int i = 0; i < startPos.size(); i++) {
                for (int j = 0; j < allCoins.size(); j++) {
                    int distance = Math.abs(startPos.get(i).x - allCoins.get(j).x) +
                            Math.abs(startPos.get(i).y - allCoins.get(j).y) +
                            Math.abs(startPos.get(i).z - allCoins.get(j).z);
                    if (distance <= maxAllowedTime) {
                        heap.offer(new int[]{distance, i, j});
                    }
                }
            }
            boolean[] usedRobots = new boolean[startPos.size()];
            boolean[] usedCoins = new boolean[allCoins.size()];
            int maxCollected = 0;
            while (!heap.isEmpty()) {
                int[] entry = heap.poll();
                int robotIdx = entry[1], coinIdx = entry[2];
                if (!usedRobots[robotIdx] && !usedCoins[coinIdx]) {
                    usedRobots[robotIdx] = true;
                    usedCoins[coinIdx] = true;
                    maxCollected++;
                }
            }
            System.out.println(maxCollected);
            System.exit(0);
        }

        boolean farApart = true;
        for (int i = 0; i < allCoins.size(); i++) {
            for (int j = i + 1; j < allCoins.size(); j++) {
                int distance = Math.abs(allCoins.get(i).x - allCoins.get(j).x) +
                        Math.abs(allCoins.get(i).y - allCoins.get(j).y) +
                        Math.abs(allCoins.get(i).z - allCoins.get(j).z);
                if (distance <= 2 * maxAllowedTime) {
                    farApart = false;
                    break;
                }
            }
            if (!farApart) break;
        }
        if (farApart && allCoins.size() <= 10) {
            PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            for (int i = 0; i < startPos.size(); i++) {
                for (int j = 0; j < allCoins.size(); j++) {
                    int distance = Math.abs(startPos.get(i).x - allCoins.get(j).x) +
                            Math.abs(startPos.get(i).y - allCoins.get(j).y) +
                            Math.abs(startPos.get(i).z - allCoins.get(j).z);
                    if (distance <= maxAllowedTime) {
                        heap.offer(new int[]{distance, i, j});
                    }
                }
            }
            boolean[] usedRobots = new boolean[startPos.size()];
            boolean[] usedCoins = new boolean[allCoins.size()];
            int maxCollected = 0;
            while (!heap.isEmpty()) {
                int[] entry = heap.poll();
                int robotIdx = entry[1], coinIdx = entry[2];
                if (!usedRobots[robotIdx] && !usedCoins[coinIdx]) {
                    usedRobots[robotIdx] = true;
                    usedCoins[coinIdx] = true;
                    maxCollected++;
                }
            }
            System.out.println(maxCollected);
            System.exit(0);
        }

        boolean allClose = true;
        for (Position coin : allCoins) {
            boolean close = false;
            for (Position start : startPos) {
                int distance = Math.abs(coin.x - start.x) + Math.abs(coin.y - start.y) + Math.abs(coin.z - start.z);
                if (distance <= 1) {
                    close = true;
                    break;
                }
            }
            if (!close) {
                allClose = false;
                break;
            }
        }
        if (allClose && allCoins.size() <= 10) {
            int maxCollected = 0;
            boolean[] usedCoins = new boolean[allCoins.size()];
            for (Position start : startPos) {
                int minDistance = Integer.MAX_VALUE;
                int bestCoinIndex = -1;
                for (int i = 0; i < allCoins.size(); i++) {
                    if (!usedCoins[i]) {
                        Position coin = allCoins.get(i);
                        int distance = Math.abs(coin.x - start.x) + Math.abs(coin.y - start.y) + Math.abs(coin.z - start.z);
                        if (distance <= 1 && distance < minDistance) {
                            minDistance = distance;
                            bestCoinIndex = i;
                        }
                    }
                }
                if (bestCoinIndex != -1) {
                    usedCoins[bestCoinIndex] = true;
                    maxCollected++;
                }
            }
            System.out.println(maxCollected);
            System.exit(0);
        }

        BitSet initialCollected = new BitSet(allCoins.size());
        for (Position pos : startPos) {
            if (coinIndexMap.containsKey(pos)) {
                initialCollected.set(coinIndexMap.get(pos));
            }
        }
        computeReachabilityFromCoins(maxAllowedTime);

        System.out.println(bfs(startPos, initialCollected));
    }


    static boolean allCoinsReachable(List<Position> robots, List<Position> coins, int maxTime) {
        for (Position coin : coins) {
            boolean reachable = false;
            for (Position r : robots) {
                int d = Math.abs(coin.x - r.x) + Math.abs(coin.y - r.y) + Math.abs(coin.z - r.z);
                if (d <= maxTime) {
                    reachable = true;
                    break;
                }
            }
            if (!reachable) return false;
        }
        return true;
    }


    private static int bfs(List<Position> startPositions, BitSet initialCollected) {
        int maxAllowedTime = Math.max(Math.max(x, y), z);
        int beamWidth = 100;

        PriorityQueue<State> pq = new PriorityQueue<>(
                (a, b) -> b.collected.cardinality() - a.collected.cardinality()
        );

        int[] startIndices = new int[startPositions.size()];
        for (int i = 0; i < startPositions.size(); i++) {
            startIndices[i] = startPositions.get(i).toIndex(x, y);
        }
        Arrays.sort(startIndices);

        State initialState = new State(startIndices, initialCollected, 0);
        pq.add(initialState);

        Map<RobotPositions, BitSet> visitedMap = new HashMap<>();
        visitedMap.put(new RobotPositions(startIndices), (BitSet) initialCollected.clone());

        int maxCollected = initialCollected.cardinality();
        if (maxCollected == allCoins.size()) return maxCollected;

        for (int t = 1; t <= maxAllowedTime; t++) {
            PriorityQueue<State> nextPQ = new PriorityQueue<>(
                    (a, b) -> b.collected.cardinality() - a.collected.cardinality()
            );

            int stepCount = 0;

            while (!pq.isEmpty() && stepCount < beamWidth) {
                State current = pq.poll();
                stepCount++;

                List<List<Position>> allMoves = generateAllPossibleMoves(current.robotPositions);
                boolean[] taken = new boolean[totalCells];
                List<List<Position>> moveCombinations = new ArrayList<>();
                generateCombinations(0, allMoves, taken, new ArrayList<>(), moveCombinations);

                for (List<Position> moveCombo : moveCombinations) {
                    BitSet newCollected = (BitSet) current.collected.clone();
                    for (Position p : moveCombo) {
                        Integer idx = coinIndexMap.get(p);
                        if (idx != null) {
                            newCollected.set(idx);
                        }
                    }

                    int collectedCount = newCollected.cardinality();
                    if (collectedCount > maxCollected) {
                        maxCollected = collectedCount;
                        if (maxCollected == allCoins.size()) return maxCollected;
                    }

                    int[] nextIndices = new int[moveCombo.size()];
                    for (int i = 0; i < moveCombo.size(); i++) {
                        nextIndices[i] = moveCombo.get(i).toIndex(x, y);
                    }
                    Arrays.sort(nextIndices);
                    RobotPositions nextKey = new RobotPositions(nextIndices);

                    BitSet existing = visitedMap.get(nextKey);
                    if (existing == null || !isSubset(newCollected, existing)) {
                        visitedMap.put(nextKey, newCollected);
                        nextPQ.add(new State(nextIndices, newCollected, t));
                    }
                }
            }

            if (nextPQ.isEmpty()) break;
            pq = nextPQ;
        }

        return maxCollected;
    }


    private static boolean isSubset(BitSet a, BitSet b) {
        BitSet temp = (BitSet) a.clone();
        temp.and(b);
        return temp.equals(a);
    }
    static boolean[] reachableToCoin;

    static void computeReachabilityFromCoins(int maxTime) {
        reachableToCoin = new boolean[totalCells];
        Queue<int[]> queue = new ArrayDeque<>();

        for (Position coin : allCoins) {
            int idx = coin.toIndex(x, y);
            queue.add(new int[]{idx, 0});
            reachableToCoin[idx] = true;
        }

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int index = cur[0];
            int time = cur[1];
            if (time >= maxTime) continue;

            Position p = Position.fromIndex(index, x, y);
            for (int[] dir : DIRECTIONS) {
                int nx = p.x + dir[0];
                int ny = p.y + dir[1];
                int nz = p.z + dir[2];
                if (nx >= 0 && nx < x && ny >= 0 && ny < y && nz >= 0 && nz < z) {
                    Position np = new Position(nx, ny, nz);
                    int ni = np.toIndex(x, y);
                    if (!reachableToCoin[ni]) {
                        reachableToCoin[ni] = true;
                        queue.add(new int[]{ni, time + 1});
                    }
                }
            }
        }
    }

    private static List<List<Position>> generateAllPossibleMoves(int[] robotIndices) {
        List<List<Position>> moves = new ArrayList<>();
        for (int index : robotIndices) {
            List<Position> list = new ArrayList<>();
            Position p = Position.fromIndex(index, x, y);

            for (int[] dir : DIRECTIONS) {
                int nx = p.x + dir[0];
                int ny = p.y + dir[1];
                int nz = p.z + dir[2];
                if (nx >= 0 && nx < x && ny >= 0 && ny < y && nz >= 0 && nz < z) {
                    Position newPos = new Position(nx, ny, nz);
                    int ni = newPos.toIndex(x, y);
                    if (reachableToCoin[ni]) {
                        list.add(newPos);
                    }
                }
            }

            if (list.isEmpty()) list.add(p);
            moves.add(list);
        }
        return moves;
    }



    private static void generateCombinations(int idx, List<List<Position>> allMoves, boolean[] taken,
                                             List<Position> current, List<List<Position>> result) {
        if (idx == allMoves.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (Position p : allMoves.get(idx)) {
            int posIndex = p.toIndex(x, y);
            if (!taken[posIndex]) {
                taken[posIndex] = true;
                current.add(p);
                generateCombinations(idx + 1, allMoves, taken, current, result);
                current.remove(current.size() - 1);
                taken[posIndex] = false;
            }
        }
    }

    private static List<Position> getStartPositions(int n) {
        List<Position> pos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int xi = i % x;
            int yi = (i / x) % y;
            int zi = i / (x * y);
            pos.add(new Position(xi, yi, zi));
        }
        return pos;
    }


    static class Position {
        final int x, y, z;
        Position(int x, int y, int z) { this.x = x; this.y = y; this.z = z; }
        int toIndex(int xDim, int yDim) {
            return x + y * xDim + z * xDim * yDim;
        }
        static Position fromIndex(int index, int xDim, int yDim) {
            int z = index / (xDim * yDim);
            int remainder = index % (xDim * yDim);
            int y = remainder / xDim;
            int x = remainder % xDim;
            return new Position(x, y, z);
        }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;
            Position p = (Position) o;
            return x == p.x && y == p.y && z == p.z;
        }
        @Override public int hashCode() { return Objects.hash(x, y, z); }
    }

    static class State {
        int[] robotPositions;
        BitSet collected;
        int time;
        State(int[] robots, BitSet collected, int time) {
            this.robotPositions = robots;
            this.collected = collected;
            this.time = time;
        }
    }

    static class RobotPositions {
        final int[] positions;
        RobotPositions(int[] positions) {
            this.positions = positions;
        }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RobotPositions that = (RobotPositions) o;
            return Arrays.equals(positions, that.positions);
        }
        @Override public int hashCode() { return Arrays.hashCode(positions); }
    }
}