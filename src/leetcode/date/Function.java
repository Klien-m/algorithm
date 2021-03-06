package leetcode.date;

import java.util.Arrays;

/**
 * @since 2020/7/25 7:59
 */
public class Function {

    // 1201 2020-07-29 11:33:42
    class TODO1 {
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

    // 1477 2020-07-31 14:52:46
    // 此方法超时
    public int maxEvents(int[][] events) {
        Arrays.sort(events, (o1, o2) -> o1[1] == o2[1] ? o1[0] - o2[0] : o1[1] - o2[1]);
        boolean[] flag = new boolean[100001];
        int res = 0;
        for (int[] event : events) {
            for (int day = event[0]; day <= event[1]; day++) {
                if (!flag[day]) {
                    flag[day] = true;
                    res++;
                    break;
                }
            }
        }
        return res;
    }

    // 198 2020-08-05 10:05:15
    public int rob1(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        int length = nums.length;
        if (length == 1)
            return nums[0];
        int[] dp = new int[length];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < length; i++)
            dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
        return dp[length - 1];
    }

    // 只存储前两间房屋的最高总金额
    public int rob2(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        int length = nums.length;
        if (length == 1)
            return nums[0];
        int first = nums[0], second = Math.max(nums[0], nums[1]);
        for (int i = 2; i < length; i++) {
            int tmp = second;
            second = Math.max(first + nums[i], second);
            first = tmp;
        }
        return second;
    }

    // 213 2020-08-05 10:35:15
    private int myRob(int[] nums) {
        int pre = 0, cur = 0, tmp;
        for (int num : nums) {
            tmp = cur;
            cur = Math.max(pre + num, cur);
            pre = tmp;
        }
        return cur;
    }

    public int rob(int[] nums) {
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        return Math.max(myRob(Arrays.copyOfRange(nums, 0, nums.length - 1)),
                myRob(Arrays.copyOfRange(nums, 1, nums.length)));
    }

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    // 337 2020-08-05 10:42:01
    // https://leetcode-cn.com/problems/house-robber-iii/solution/san-chong-fang-fa-jie-jue-shu-xing-dong-tai-gui-hu/
    private int[] robInternal(TreeNode root) {
        if (root == null) return new int[2];
        int[] result = new int[2];
        int[] left = robInternal(root.left);
        int[] right = robInternal(root.right);
        result[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        result[1] = left[0] + right[0] + root.val;
        return result;
    }

    public int rob(TreeNode root) {
        int[] result = robInternal(root);
        return Math.max(result[0], result[1]);
    }

    // 849 2020-08-05 11:02:46
    public int maxDistToClosest(int[] seats) {
        int N = seats.length;
        int[] left = new int[N], right = new int[N];
        Arrays.fill(left, N);
        Arrays.fill(right, N);
        for (int i = 0; i < N; i++) {
            if (seats[i] == 1) left[i] = 0;
            else if (i > 0) left[i] = left[i - 1] + 1;
        }
        for (int i = N - 1; i >= 0; i--) {
            if (seats[i] == 1) right[i] = 0;
            else if (i < N - 1) right[i] = right[i + 1] + 1;
        }
        int ans = 0;
        for (int i = 0; i < N; i++)
            if (seats[i] == 0)
                ans = Math.max(ans, Math.min(left[i], right[i]));
        return ans;
    }

    public int maxDistToClosest2(int[] seats) {
        int N = seats.length;
        int prev = -1, future = 0;
        int ans = 0;
        for (int i = 0; i < N; i++) {
            if (seats[i] == 1)
                prev = i;
            else {
                while (future < N && seats[future] == 0 || future < i) future++;
                int left = prev == -1 ? N : i - prev;
                int right = future == N ? N : future - i;
                ans = Math.max(ans, Math.min(left, right));
            }
        }
        return ans;
    }

    public int maxDistToClosest3(int[] seats) {
        int prev = -1;
        int max = 1;
        for (int i = 0; i < seats.length; i++) {
            if (seats[i] == 1) {
                if (prev >= 0) max = Math.max((i - prev) / 2, max);
                else max = i;
                prev = i;
            }
        }
        max = Math.max((seats.length - 1) - prev, max);
        return max;
    }

    // 剑指 Offer 04 2020-08-05 11:43:17
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return false;
        int rows = matrix.length, columns = matrix[0].length;
        int row = 0, column = columns - 1;
        while (row < rows && column >= 0) {
            int num = matrix[row][column];
            if (num == target)
                return true;
            else if (num > target)
                column--;
            else row++;
        }
        return false;
    }

    // 925 2020-08-05 12:00:29
    public boolean isLongPressedName(String name, String typed) {
        int i = 0, j = 0;
        char[] namec = name.trim().toCharArray();
        char[] typedc = typed.trim().toCharArray();
        if (namec.length > typedc.length) return false;
        while (i < namec.length && j < typedc.length) {
            if (namec[i] == typedc[j]) {
                i++;
                j++;
            } else if (j > 0 && typedc[j] == typedc[j - 1]) {
                j++;
            } else {
                return false;
            }
        }
        while (j < typedc.length) {
            if (typedc[j] != typedc[j - 1])
                return false;
            j++;
        }
        return i == namec.length;
    }

    // 441 2020-08-05 12:15:22
    public int arrangeCoins(int n) {
        return (int) (Math.sqrt(1 + 8 * (long) n) - 1) / 2;
    }

    public int arrangeCoins2(int n) {
        int i = 1;
        while (n >= i) {
            n -= i;
            i++;
        }
        return i - 1;
    }
}

