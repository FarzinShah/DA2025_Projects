
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Phase1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Filter.lQ = sc.nextInt();
        Filter.wQ = sc.nextInt();
        Filter.hQ = sc.nextInt();
        Filter.lF = sc.nextInt();
        Filter.wF = sc.nextInt();
        Filter.hF = sc.nextInt();

        Filter.filter = new Integer[Filter.hF][Filter.wF][Filter.lF];
        Filter.isVisited = new boolean[Filter.hF][Filter.wF][Filter.lF];

        if (Filter.lQ > Filter.lF || Filter.wQ > Filter.wF || Filter.hQ > Filter.hF || Filter.lQ <= 0 || Filter.wQ <= 0 || Filter.hQ <= 0) {
            System.out.println("0");
            System.exit(0);
        }
        for (int z = 0; z < Filter.hF; z++) {
            for (int y = 0; y < Filter.wF; y++) {
                for (int x = 0; x < Filter.lF; x++) {
                    Filter.filter[z][y][x] = sc.nextInt();
                }
            }
        }

//        for (int z = 0; z < hF; z++) {
//            for (int y = 0; y < wF; y++) {
//                for (int x = 0; x < lF; x++) {
//                    System.out.print(filter[z][y][x] + "  " ); //todo: debug test
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }

        LinkedList<Integer[]> path = bfs_PathFinder();

        if (path != null) {
            System.out.println("1");
            for (Integer[] paths : path) {
                if (paths[0] != -99999999)
                    System.out.println((paths[0] + 1) + " " + (paths[1] + 1) + " " + (paths[2] + 1));
            }
        } else {
            System.out.println("0");
        }

// dfs has bug ---------------------------------------
//        boolean found = false;
//
//        for (int x = 0; x <= Filter.lF - Filter.lQ && !found; x++) {
//            for (int y = 0; y <= Filter.wF - Filter.wQ && !found; y++) {
//                int z = 0;
//                if (canPlace(x, y, z)) {
//                    Filter.path.clear();
//                    resetVisited();
//                    if (dfs(x, y, z)) {
//                        found = true;
//                    }
//                }
//            }
//        }
//
//        if (found) {
//            System.out.println("1");
//            for (Integer[] p : Filter.path) {
//                System.out.println((p[0] + 1) + " " + (p[1] + 1) + " " + (p[2] + 1));
//            }
//        } else {
//            System.out.println("0");
//        }
//-----------------------
    }

    public static void resetVisited() {
        for (int z = 0; z < Filter.hF; z++) {
            for (int y = 0; y < Filter.wF; y++) {
                Arrays.fill(Filter.isVisited[z][y], false);
            }
        }
    }

    public static boolean dfs(int x, int y, int z) {
        if (!isValid(x, y, z)) return false;

        if (z == Filter.hF - Filter.hQ) {
            Filter.paths.add(new Integer[]{x, y, z});
            return true;
        }

        Filter.isVisited[z][y][x] = true;
        Integer[] addingPath = new Integer[]{x, y, z};
        Filter.paths.add(addingPath);
        boolean boo = false;
        for (int i = 0; i < 6; i++) {
            int nx = x + Filter.dx[i];
            int ny = y + Filter.dy[i];
            int nz = z + Filter.dz[i];

            if (isValid(nx, ny, nz) && !Filter.isVisited[nz][ny][nx] && validToPlace(nx, ny, nz)) {
                if (!dfs(nx, ny, nz)) {
                    boo = false;
                }
                else {
                    boo = true;
                    return boo;
                }
            }
        }

        Filter.paths.remove(Filter.paths.size() - 1);
        return boo;
    }

    public static boolean isValid(int x, int y, int z) {
        boolean boo = x >= 0 && y >= 0 && z >= 0 && x + Filter.lQ <= Filter.lF && y + Filter.wQ <= Filter.wF && z + Filter.hQ <= Filter.hF;
        return boo;
    }


    public static LinkedList<Integer[]> bfs_PathFinder() {
        Queue<Cell> queue = new LinkedList<>();
        HashMap<String, Cell> map = new HashMap<>();
        AtomicReference<LinkedList<Cell>> checkedNodes = new AtomicReference<>(new LinkedList<>());
        int length = Math.abs(Filter.lF - Filter.lQ);
        int width = Math.abs(Filter.wF - Filter.wQ);
        int x = 0;
        while (x <= length) {
            int y = 0;
            while (y <= width) {
                int z = 0;
                if (validToPlace(x, y, z)) {
                    Filter.isVisited[z][y][x] = true;
                    Cell startNode = new Cell(x, y, z);
                    checkedNodes.get().add(startNode);
                    queue.add(startNode);
                    map.put(startNode.toString(), new Cell(-99999999,-99999999,-99999999));
                }
                y++;
            }
            x++;
        }

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            if (current.z == Filter.hF - Filter.hQ) {
                return makePath(current, map);
            }

            for (int i = 0; i < 6; i++) {
                int nx = current.x + Filter.dx[i];
                int ny = current.y + Filter.dy[i];
                int nz = current.z + Filter.dz[i];

                if (isValid(nx, ny, nz) && !Filter.isVisited[nz][ny][nx] && validToPlace(nx, ny, nz)) {
                    Cell neighbor = new Cell(nx, ny, nz);
                    queue.add(neighbor);
                    map.put(neighbor.toString(), current);
                    Filter.isVisited[nz][ny][nx] = true;
                }
            }
        }

        return null;
    }
    public static boolean validToPlace(int x, int y, int z) {
        if (!isValid(x, y, z)) return false;

        for (int dz = 0; dz < Filter.hQ; dz++) {
            for (int dy = 0; dy < Filter.wQ; dy++) {
                for (int dx = 0; dx < Filter.lQ; dx++) {
                    if (Filter.filter[dz+z][y+dy][x+dx] == 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public static LinkedList<Integer[]> makePath(Cell finish, HashMap<String, Cell> map) {
        LinkedList<Integer[]> paths = new LinkedList<>();
        Cell current = finish;

        while (current != null) {
            paths.addFirst(new Integer[]{current.x, current.y, current.z});
            current = map.get(current.toString());
        }

        return paths;
    }

    public static class Filter {
        static Filter instance;
        static Integer lQ, wQ, hQ;
        static Integer lF, wF, hF;
        static Integer[][][] filter;
        static boolean[][][] isVisited;
        static ArrayList<Integer[]> paths;

        static Integer[] dx ;
        static Integer[] dy;
        static Integer[] dz ;

        public Filter() {
            dx = new Integer[]{-1, 1, 0, 0, 0, 0};
            dy = new Integer[]{0, 0, -1, 1, 0, 0};
            dz = new Integer[]{0, 0, 0, 0, -1, 1};
            paths= new ArrayList<>();
        }
        public static Filter getInstance(){ //singleton is not needed.
            if (instance==null){
                instance = new Filter();
            }
            return instance;
        }
    }
    public static class Cell {
        int x, y, z;

        public Cell(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }



    }
}

