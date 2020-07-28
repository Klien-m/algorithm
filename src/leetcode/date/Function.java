package leetcode.date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 2020/7/25 7:59
 */
public class Function {

    // 面试题 08.01 2020-07-28 09:45:54
//    从 n - 1 阶楼梯，上 1 阶可以到达；
//    从 n - 2 阶楼梯，上 2 阶可以到达；
//    从 n - 3 阶楼梯，上 3 阶可以到达；
//    假设上到 n 阶楼梯的方法数为 f(n), 由于上面三种情况都可以到达，所以
//    f(n) = f(n - 1) + f(n - 2) + f(n - 3) (n >= 3)
    public int waysToStep(int n) {
        if (n <= 2) return n;
        if (n == 3) return 4;
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        dp[3] = 4;
        for (int i = 4; i <= n; i++) {
            dp[i] = (dp[i - 1] + dp[i - 2]) % 1000000007 + dp[i - 3];
            dp[i] %= 1000000007;
        }
        return dp[n];
    }

    // 414 2020-07-28 10:02:42
    // 自己思路没问题，但是没有考虑 MIN_VALUE, 同时对于无第三大的情况没有办法
    public int thirdMax(int[] nums) {
        long MIN_VALUE = Long.MIN_VALUE;
        long first = MIN_VALUE;
        long second = MIN_VALUE;
        long third = MIN_VALUE;
        for (int num : nums) {
            if (num > first) {
                third = second;
                second = first;
                first = num;
            } else if (num > second && num < first) {
                third = second;
                second = num;
            } else if (num > third && num < second) {
                third = num;
            }
        }
        return third == MIN_VALUE ? (int) first : (int) third;
    }

    // 949 2020-07-28 10:44:19
    public String largestTimeFromDigits(int[] A) {
        int ans = -1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j != i) {
                    for (int k = 0; k < 4; k++) {
                        if (k != i && k != j) {
                            int l = 6 - i - j - k;
                            int hours = 10 * A[i] + A[j];
                            int mins = 10 * A[k] + A[l];
                            if (hours < 24 && mins < 60) {
                                ans = Math.max(ans, hours * 60 + mins);
                            }
                        }
                    }
                }
            }
        }
        return ans >= 0 ? String.format("%02d:%02d", ans / 60, ans % 60) : "";
    }

    // 434 2020-07-28 10:55:21
    public int countSegments(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            // 前一个为空格后一个不为空格则为一个单词
            if ((i == 0 || s.charAt(i - 1) == ' ') && s.charAt(i) != ' ')
                count++;
        }
        return count;
    }

    // 941 2020-07-28 11:04:40
    public boolean validMountainArray(int[] A) {
        int N = A.length;
        int i = 0;
        while (i + 1 < N && A[i] < A[i + 1])
            i++;
        if (i == 0 || i == N - 1)
            return false;
        while (i + 1 < N && A[i] > A[i + 1])
            i++;
        return i == N - 1;
    }

    // 866 2020-07-28 11:15:25
    public int primePalindrome(int N) {
        int[] check = new int[]{2, 2, 2, 3, 5, 5, 7, 7, 11, 11, 11, 11};
        if (N < check.length) return check[N];
        while (true) {
            int mod = N % 6;
            String cs = String.valueOf(N);
            if ((cs.length() & 1) == 0) { // 偶数长度的回文数中只有11是素数，其他的都可以被11整除。
                N = (int) Math.pow(10, cs.length()) + 1;
                continue;
            }
            if (mod == 1 || mod == 5) {
                boolean isPrime = true, isPalindrome = true;
                int L1 = (int) Math.sqrt(N), strLen = cs.length(), L2 = strLen >> 1; // L2 = strLen / 2;
                for (int i = 5, j = 0; i <= L1 || j < L2; i += 6, ++j) {
                    if (i <= L1 && (N % i == 0 || N % (i + 2) == 0)) { // 是否是素数，6*x-1 6*x+1
                        isPrime = false;
                        break;
                    }
                    if (j < L2 && cs.charAt(j) != cs.charAt(strLen - j - 1)) {
                        isPalindrome = false;
                        break;
                    }
                }
                if (isPalindrome && isPrime)
                    return N;
                N = mod == 1 ? N + 4 : N + 2;
            } else {
                N = mod == 0 ? N + 1 : N + (5 - mod);
            }
        }
    }

    // 1488 2020-07-28 11:21:05
    private int lowerBound(ArrayList<Integer> A, int target) {
        int left = 0, right = A.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (A.get(mid) >= target) right = mid;
            else left = mid + 1;
        }
        return left;
    }

    public int[] avoidFlood(int[] rains) {
        int[] ans = new int[rains.length];
        Map<Integer, Integer> water = new HashMap<>(); // 每个湖泊上一次下雨的日期
        ArrayList<Integer> zeros = new ArrayList<>();
        for (int i = 0; i < rains.length; i++) {
            int pool = rains[i];
            if (pool == 0) {
                zeros.add(i);
            } else {
                ans[i] = -1;
                if (water.containsKey(pool)) {
                    int lastFull = water.get(pool);
                    int id = lowerBound(zeros, lastFull);  // 前一个满了的pool右边的第一个0的index
                    int zeroIdx = id >= zeros.size() ? -1 : zeros.get(id);
                    if (zeroIdx > lastFull) {
                        ans[zeroIdx] = pool;
                        zeros.remove(Integer.valueOf(zeroIdx));
                        water.put(pool, i);
                    } else {
                        return new int[0];
                    }
                } else {
                    water.put(pool, i);
                }
            }
        }
        for (int z : zeros) ans[z] = 1;
        return ans;
    }
}

