# SWExpertAcademy_MockTest_Java_5644

## SW Expert Academy  5644. [모의 SW 역량테스트] 무선 충전

### 1. 문제설명

출처: https://swexpertacademy.com/main/code/problem/problemDetail.do?contestProbId=AWXRDL1aeugDFAUo

input으로 사용자의 이동 횟수 `M` 과 `BC`의 개수 `A`가 들어온다. 이어서 두줄로 `M`개의 이동정보가 들어오며 `A`줄의 `BC`정보가 들어온다. `BC`의 `coverage`내부에 있으면 `performance`만큼의 충전을 받을 수 있으며 같은 `BC`의 `coverage`안에 두명의 사용자가 함께 충전한다면 `performance`는 절반의 양만큼만 충전한다. 이동하는 사용자들이 이동이 끝났을 때 충전합의 최대값을 출력하는 문제.

[제약사항]

1. 지도의 가로, 세로 크기는 `10`이다.
2. 사용자는 총 `2`명이며, 사용자`A`는 지도의 `(1, 1)` 지점에서, 사용자`B`는 지도의 `(10, 10)` 지점에서 출발한다.
3. 총 이동 시간 `M`은 `20`이상 `100`이하의 정수이다. `(20 ≤ M ≤ 100)`
4. `BC`의 개수 `A`는 `1`이상 `8`이하의 정수이다. `(1 ≤ A ≤ 8)`
5. `BC`의 충전 범위 `C`는 `1`이상 `4`이하의 정수이다. `(1 ≤ C ≤ 4)`
6. `BC`의 성능 `P`는 `10`이상 `500`이하의 짝수이다. `(10 ≤ P ≤ 500)`
7. 사용자의 초기 위치(`0`초)부터 충전을 할 수 있다.
8. 같은 위치에 `2`개 이상의 `BC`가 설치된 경우는 없다. 그러나 사용자 `A, B`가 동시에 같은 위치로 이동할 수는 있다. 사용자가 지도 밖으로 이동하는 경우는 없다.

[입력]

> 입력의 맨 첫 줄에는 총 테스트 케이스의 개수 `T`가 주어지고, 그 다음 줄부터 `T`개의 테스트 케이스가 주어진다.
> 테스트 케이스의 첫 번째 줄에는 총 이동 시간`(M)`, `BC`의 개수`(A)`가 주어진다.
> 그 다음 `2`개의 줄에는 각각 사용자 `A`와 `B`의 이동 정보가 주어진다.
> 한 사용자의 이동 정보는 `M`개의 숫자로 구성되며, 각각의 숫자는 다음과 같이 매초마다 이동 방향을 의미한다.
 
| 숫자 | 이동방향 |
|---|:---:|
| `0` | 이동하지 않음 |
| `1` | 상 (UP) |
| `2` | 우 (RIGHT) |
| `3` | 하 (DOWN) |
| `4` | 좌 (LEFT) |

> 그 다음 줄에는 `A`개의 줄에 걸쳐 `BC`의 정보가 주어진다.
> 하나의 `BC` 정보는 좌표`(X, Y)`, 충전 범위`(C)`, 처리량`(P)`로 구성된다.

[출력]

> 출력은 `#t`를 찍고 한 칸 띄운 다음 정답을 출력한다. (`t`는 테스트 케이스의 번호를 의미하며 `1`부터 시작한다.)
> 정답은 모든 사용자의 충전량 합의 최대값을 출력한다.


### 2. 풀이

사용자의 시작점이 `1,1`과 `10,10`이며 `BC`의 정보도 `0`을 시작 index으로 하지 않고 들어오기 때문에 `int[][] map = new int[11][11]`로 맵을 지정했다. 사용자의 이동마다 위치에서 `coverage`에 들어간 `BC`를 조사하는 방법은 비효율적이라 생각하여 `map`속에 `BC`의 영향도를 기록하였다. 비트연산자를 이용하여 `i`번째 `BC`의 영향이 미치는 곳이면 `map[x][y] | 1 << i`로 `2^i`를 넣어주었다. 예를들어, `1`, `3`번 `BC`가 영향을 끼치는 곳이면 `101`의 값을 가진다.

사용자의 이동마다 해당 위치의 값을 가져와 값을 `>>`하며 첫번째 비트가 켜져있는지 확인하여 `BC`의 영향이 미치는지 확인하며, 각 사용자 마다 `performance`에 대하여 내림차순으로 정렬한 `BC`의 index를 두개까지만 저장한다. 가장 큰값과 두번째로 큰값을 가지고 가장 큰 충전량을 가질수있도록 하드코딩으로 작성해주었다.

```java
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

```

