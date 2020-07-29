package leetcode.date;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 2020/7/25 7:59
 */
public class Function {

    // 874 2020-07-29 09:23:27
    // 题意很好理解，只是处理转向以及判断障碍点是个问题
    public int robotSim(int[] commands, int[][] obstacles) {
        int[] dx = new int[]{0, 1, 0, -1}; // 转向相关
        int[] dy = new int[]{1, 0, -1, 0};
        int x = 0, y = 0, di = 0;
        Set<Long> obstacleSet = new HashSet<>();
        for (int[] obstacle : obstacles) { // 存储障碍点
            long ox = (long) obstacle[0] + 30000;
            long oy = (long) obstacle[1] + 30000;
            obstacleSet.add((ox << 16) + oy);
        }
        int ans = 0;
        for (int cmd : commands) {
            if (cmd == -2) // left 转向
                di = (di + 3) % 4;
            else if (cmd == -1) // right 转向
                di = (di + 1) % 4;
            else {
                for (int k = 0; k < cmd; k++) {
                    int nx = x + dx[di]; // 前进 or 后退 或者 止步不前
                    int ny = y + dy[di]; // 前进 or 后退 或者 止步不前
                    long code = (((long) nx + 30000) << 16) + ((long) ny + 30000);
                    if (!obstacleSet.contains(code)) { // 判断是否遇到障碍点
                        x = nx;
                        y = ny;
                        ans = Math.max(ans, x * x + y * y);
                    }
                }
            }
        }
        return ans;
    }

    // 724 2020-07-29 09:54:09
    // leftSum == sum - leftSum - nums[i]
    public int pivotIndex(int[] nums) {
        int sum = 0, leftSum = 0;
        for (int n : nums) sum += n;
        for (int i = 0; i < nums.length; i++) {
            if (leftSum == sum - leftSum - nums[i]) return i;
            leftSum += nums[i];
        }
        return -1;
    }

    // 1033 2020-07-29 10:15:10
    public int[] numMovesStones(int a, int b, int c) {
        int[] arr = new int[]{a, b, c};
        Arrays.sort(arr);
        a = arr[0];
        b = arr[1];
        c = arr[2];
        int[] result = new int[2];

        if (b - a == 1 && c - b == 1) {
            result[0] = 0;
        } else if (b - a == 1 || c - b == 1 || c - b == 2 || b - a == 2) { // 相隔为 2 也可移动一次达到目标
            result[0] = 1;
        } else {
            result[0] = 2;
        }
        result[1] = c - a - 2;
        return result;
    }

    // 168 2020-07-29 11:05:20
    public String convertToTitle(int n) {
        if (n < 0) return "";
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--;
            sb.append((char) (n % 26 + 'A'));
            n /= 26;
        }
        return sb.reverse().toString();
    }

    // 507 2020-07-29 11:08:25
    public boolean checkPerfectNumber(int num) {
        if (num <= 0) return false;
        int sum = 0;
        for (int i = 1; i * i <= num; i++) {
            if (num % i == 0) {
                sum += i;
                if (i * i != num) {
                    sum += num / i;
                }
            }
        }
        return sum - num == num;
    }

    // 欧几里得-欧拉定理
    // 每个偶完全数都可以写成 2^{p-1}(2^p-1) 的形式，其中 p 为素数。
    public int pn(int p) {
        return (1 << (p - 1)) * ((1 << p) - 1);
    }

    public boolean checkPerfectNumber2(int num) {
        int[] primes = new int[]{2, 3, 5, 7, 13, 17, 19, 31};
        for (int prime : primes) {
            if (pn(prime) == num)
                return true;
        }
        return false;
    }

    // 1201 2020-07-29 11:33:42
    class Solution {
        private long lcmAB;
        private long lcmAC;
        private long lcmBC;
        private long lcmABC;

        public int nthUglyNumber(int n, int a, int b, int c) {
            if (n < 1 || a < 1 || b < 1 || c < 1) return -1;
            long lcmAB = lcm(a, b);
            long lcmAC = lcm(a, c);
            long lcmBC = lcm(b, c);
            long lcmABC = lcm(lcmAB, c);
            long low = Math.min(Math.min(a, b), c);
            long high = low * n;
            long res = binarySearch(low, high, a, b, c, n);
            long leftA = res % a;
            long leftB = res % b;
            long leftC = res % c;
            return (int) (res - Math.min(Math.min(leftA, leftB), leftC));
        }

        private long binarySearch(long low, long high, int a, int b, int c, int n) {
            if (low >= high) return low;
            long mid = (low + high) >> 1;
            long count = mid / a + mid / b + mid / c - mid / lcmAB - mid / lcmBC - mid / lcmAC + mid / lcmABC;
            if (count == n) return mid;
            if (count < n) return binarySearch(mid + 1, high, a, b, c, n);
            return binarySearch(low, mid - 1, a, b, c, n);
        }

        // 最小公倍数
        private long lcm(long a, long b) {
            long multi = a * b;
            while (b > 0) {
                long tmp = a % b;
                a = b;
                b = tmp;
            }
            return multi / a;
        }
    }

    // 523 2020-07-29 14:09:55
    // 暴力
    public boolean checkSubarraySum(int[] nums, int k) {
        for (int start = 0; start < nums.length - 1; start++) {
            for (int end = start + 1; end < nums.length; end++) {
                int sum = 0;
                for (int i = start; i <= end; i++)
                    sum += nums[i];
                if (sum == k || (k != 0 && sum % k == 0))
                    return true;
            }
        }
        return false;
    }

    // 两个不同的前缀和的余数相等，意味着这两个前缀和之差就是 k 的倍数
    // sum=sum%k对求解前缀和的余数没有影响，只是让sum和余数之间的差去掉
    public boolean checkSubarraySum2(int[] nums, int k) {
        int sum = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (k != 0)
                sum = sum % k;
            if (map.containsKey(sum)) {
                if (i - map.get(sum) > 1)
                    return true;
            } else map.put(sum, i);
        }
        return false;
    }
}

