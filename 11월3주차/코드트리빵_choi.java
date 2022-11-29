import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

class Pair {
    int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Boolean isSame(Pair p){
        return this.x == p.x && this.y == p.y;
    }
}

public class Main {

    public static final int INT_MAX = Integer.MAX_VALUE;
    public static final Pair EMPTY = new Pair(-1, -1);

    public static final int DIR_NUM = 4;
    public static final int MAX_M = 30;
    public static final int MAX_N = 15;

    public static int n, m;

    public static int[][] grid = new int[MAX_N][MAX_N];

    public static Pair[] cvsList = new Pair[MAX_M];

    public static Pair[] people = new Pair[MAX_M];

    public static int currT;

    public static int[] dx = new int[]{-1,  0, 0, 1};
    public static int[] dy = new int[]{ 0, -1, 1, 0};
    public static int[][] step = new int[MAX_N][MAX_N];
    public static boolean[][] visited = new boolean[MAX_N][MAX_N];

    static boolean OOB(int x, int y) {
        return x<0 || x >= n || y<0 || y >= n;
    }
    static boolean isValid(int x, int y) {
        return !OOB(x, y) && !visited[x][y] && grid[x][y] != 2;
    }

    static void bfs(Pair startPos) {

        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++) {
                visited[i][j] = false;
                step[i][j] = 0;
            }

        Queue<Pair> q = new LinkedList<>();
        int sx = startPos.x, sy = startPos.y;
        q.add(startPos);
        visited[sx][sy] = true;
        step[sx][sy] = 0;

        while(!q.isEmpty()) {
            Pair currPos = q.poll();

            int x = currPos.x, y = currPos.y;
            for(int i = 0; i < DIR_NUM; i++) {
                int nx = x + dx[i], ny = y + dy[i];
                if(isValid(nx, ny)) {
                    visited[nx][ny] = true;
                    step[nx][ny] = step[x][y] + 1;
                    q.add(new Pair(nx, ny));
                }
            }
        }
    }

    static void simulate() {

        for(int i = 0; i < m; i++) {
            if(people[i] == EMPTY || people[i].isSame(cvsList[i]))
                continue;

            bfs(cvsList[i]);

            int px = people[i].x, py = people[i].y;

            int minDist = INT_MAX;
            int minX = -1, minY = -1;
            for(int j = 0; j < DIR_NUM; j++) {
                int nx = px + dx[j], ny = py + dy[j];
                if(!OOB(nx, ny) && visited[nx][ny] && minDist > step[nx][ny]) {
                    minDist = step[nx][ny];
                    minX = nx; minY = ny;
                }
            }
            people[i] = new Pair(minX, minY);
        }

        for(int i = 0; i < m; i++) {
            if(people[i].isSame(cvsList[i])) {
                int px = people[i].x, py = people[i].y;
                grid[px][py] = 2;
            }
        }

        if(currT > m)
            return;

        bfs(cvsList[currT - 1]);

        int minDist = INT_MAX;
        int minX = -1, minY = -1;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(visited[i][j] && grid[i][j] == 1 && minDist > step[i][j]) {
                    minDist = step[i][j];
                    minX = i; minY = j;
                }
            }
        }
        people[currT - 1] = new Pair(minX, minY);
        grid[minX][minY] = 2;
    }

    static boolean end() {
        for(int i = 0; i < m; i++) {
            if(!people[i].isSame(cvsList[i]))
                return false;
        }
        return true;
    }

    public static void main(String[] args) {

        FastReader scan = new FastReader();

        n = scan.nextInt();
        m = scan.nextInt();
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                grid[i][j] = scan.nextInt();

        for(int i = 0; i < m; i++) {
            int x = scan.nextInt();
            int y = scan.nextInt();
            cvsList[i] = new Pair(x - 1, y - 1);
        }

        for(int i = 0; i < m; i++)
            people[i] = EMPTY;

        while(true) {
            currT++;
            simulate();
            if(end()) break;
        }

        System.out.println(currT);
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}