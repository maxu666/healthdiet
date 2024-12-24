package project4;

public class PerfectNumber {
    public static void main(String[] args) {
        // 遍历 1 到 1000 之间的所有数
        for (int num = 2; num <= 1000; num++) {
            int sum = 0;  // 存储因子之和

            // 找出 num 的所有因子（不包括 num 本身）
            for (int i = 1; i <= num / 2; i++) {
                if (num % i == 0) {
                    sum += i;  // 如果 i 是 num 的因子，则加到 sum
                }
            }

            // 如果因子之和等于 num，则 num 是完数
            if (sum == num) {
                System.out.println(num);  // 输出完数
            }
        }
    }
}
