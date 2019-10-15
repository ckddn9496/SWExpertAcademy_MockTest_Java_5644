import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

class Solution {
	static class BC {
		int x, y;
		int coverage;
		int performance;

		public BC(int x, int y, int coverage, int performance) {
			this.x = x;
			this.y = y;
			this.coverage = coverage;
			this.performance = performance;
		}

//		public String toString() {
//			return "(" + x + ", " + y + "): c=" + coverage + " p=" + performance + " ";
//		}
	}

	static BC[] bcs;
	static int[][] dir = { { 0, 0 }, { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } }; // 상 우 하 좌
	static int[][] map;

	public static void main(String args[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());

		for (int test_case = 1; test_case <= T; test_case++) {

			StringTokenizer st = new StringTokenizer(br.readLine());
			int M = Integer.parseInt(st.nextToken());
			int A = Integer.parseInt(st.nextToken());

			st = new StringTokenizer(br.readLine());
			int[] p1 = new int[M];
			int idx = 0;
			while (st.hasMoreTokens()) {
				p1[idx++] = Integer.parseInt(st.nextToken());
			}
			st = new StringTokenizer(br.readLine());
			int[] p2 = new int[M];
			idx = 0;
			while (st.hasMoreTokens()) {
				p2[idx++] = Integer.parseInt(st.nextToken());
			}

			map = new int[11][11];

			bcs = new BC[A];
			for (int i = 0; i < A; i++) {
				st = new StringTokenizer(br.readLine());
				int y = Integer.parseInt(st.nextToken());
				int x = Integer.parseInt(st.nextToken());
				bcs[i] = new BC(x, y, Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
			}
			Arrays.sort(bcs, new Comparator<BC>() {
				public int compare(BC bc1, BC bc2) {
					return bc2.performance - bc1.performance;
				}
			});
//			System.out.println(Arrays.toString(bcs));
			for (int i = 0; i < A; i++) {
				BC bc = bcs[i];
				int x = bc.x;
				int y = bc.y;
				for (int dx = 0; dx < 11; dx++) {
					for (int dy = 0; dy < 11; dy++) {
						if (Math.abs(dx - x) + Math.abs(dy - y) <= bc.coverage) {
							map[dx][dy] = map[dx][dy] | 1 << i;
						}
					}
				}
			}

//			printMap(map);

			int p1x = 1;
			int p1y = 1;
			int p2x = 10;
			int p2y = 10;
			int total = 0;

			for (int i = 0; i <= M; i++) {
//				System.out.println("Try " + i + "  p1 : " + p1x + ", " + p1y + "  p2 : " + p2x + ", " + p2y);
				int p1Power = map[p1x][p1y];
				int p2Power = map[p2x][p2y];

				int p1First = -1;
				int p1Second = -1;
				int p2First = -1;
				int p2Second = -1;

				for (int bIdx = 0; bIdx < A; bIdx++) {
					if ((1 & p1Power >> bIdx) == 1) {
						if (p1First == -1)
							p1First = bIdx;
						else if (p1Second == -1) {
							p1Second = bIdx;
						}
					}

					if ((1 & p2Power >> bIdx) == 1) {
						if (p2First == -1)
							p2First = bIdx;
						else if (p2Second == -1) {
							p2Second = bIdx;
						}
					}
				}
				
				if (p1First != -1 && p2First != -1) {
					
					if (p1First == p2First) { // 둘다 존재, 하지만 같은 idx Second고려
						
						if (p1Second == -1 && p2Second == -1) { // 둘다 second 없다. 반반써야함
							total += bcs[p1First].performance;
						} else if (p1Second != -1 && p2Second != -1) { // 둘다 second 있다. 둘중 최소 idx 찾아서 performance 더해줌
							total += bcs[p1First].performance + bcs[Math.min(p1Second, p2Second)].performance;
						} else if (p1Second == -1){
							total += bcs[p1First].performance + bcs[p2Second].performance;
						} else {
							total += bcs[p2First].performance + bcs[p1Second].performance;							
						}
						
					} else { // 둘다 존재, 다른 idx이면 각자 더한다
						total += bcs[p1First].performance + bcs[p2First].performance;
					}
					
				} else if (p1First == -1 && p2First != -1) {
					total += bcs[p2First].performance;
				} else if (p1First != -1 && p2First == -1) {
					total += bcs[p1First].performance;
				}
					
				if (i == M) {
					break;
				}
				p1x += dir[p1[i]][0];
				p1y += dir[p1[i]][1];
				p2x += dir[p2[i]][0];
				p2y += dir[p2[i]][1];
			}

			System.out.println("#" + test_case + " "+total);
		}
	}

//	private static void printMap(int[][] map2) {
//		for (int i = 1; i < 11; i++) {
//			for (int j = 1; j < 11; j++) {
//				System.out.print(String.format("%8s", Integer.toBinaryString(map2[i][j])));
//			}
//			System.out.println();
//		}
//	}
}