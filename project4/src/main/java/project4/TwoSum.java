package project4;

import java.util.HashMap;

public class TwoSum {
    public static void main(String[] args) {
        // 示例 1
        int[] nums1 = {2, 7, 11, 15};
        int target1 = 9;
        int[] result1 = twoSum(nums1, target1);
        System.out.println("结果: [" + result1[0] + ", " + result1[1] + "]");

        // 示例 2
        int[] nums2 = {3, 2, 4};
        int target2 = 6;
        int[] result2 = twoSum(nums2, target2);
        System.out.println("结果: [" + result2[0] + ", " + result2[1] + "]");

        // 示例 3
        int[] nums3 = {3, 3};
        int target3 = 6;
        int[] result3 = twoSum(nums3, target3);
        System.out.println("结果: [" + result3[0] + ", " + result3[1] + "]");
    }

    // 实现 twoSum 方法
    public static int[] twoSum(int[] nums, int target) {
        // 使用哈希表存储元素值和对应的索引
        HashMap<Integer, Integer> map = new HashMap<>();

        // 遍历数组
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i]; // 计算补数

            // 如果补数已经存在于哈希表中，则找到答案
            if (map.containsKey(complement)) {
                return new int[] {map.get(complement), i}; // 返回补数的索引和当前索引
            }

            // 将当前元素的值和索引存入哈希表
            map.put(nums[i], i);
        }

        // 如果没有找到解，返回空数组（根据题目假设每种输入都有唯一解，不会执行到这里）
        return new int[] {};
    }
}

