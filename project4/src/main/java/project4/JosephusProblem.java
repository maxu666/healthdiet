package project4;

public class JosephusProblem {
    // 计算约瑟夫问题的最后一个出局者的序号
    public static int josephus(int n, int k) {
        int result = 0; // 初始情况下，只有一个人时，位置是0（即第一个人）

        // 从 2 人开始，一直到 n 人
        for (int i = 2; i <= n; i++) {
            result = (result + k) % i;  // 递推公式
        }

        return result + 1;  // 返回的结果是从1开始的，所以加1
    }

    public static void main(String[] args) {
        int n = 12;  // 总人数
        int k = 5;   // 每次数到5的人出局

        // 计算最后一个出局的人
        int lastPerson = josephus(n, k);

        System.out.println("最后一个出局的人是: " + lastPerson);
    }
}

