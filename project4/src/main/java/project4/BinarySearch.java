package project4;

import java.util.Arrays;

public class BinarySearch {
    public static void main(String[] args) {
        // 给定一个字典（已按字母顺序排序）
        String[] dictionary = {
                "apple", "banana", "cherry", "date", "fig", "grape", "kiwi", "lemon", "mango", "orange"
        };

        // 需要查找的单词
        String target = "banana";

        // 调用二分查找方法
        int result = binarySearch(dictionary, target);

        // 输出查找结果
        if (result == -1) {
            System.out.println("单词 '" + target + "' 不在字典中");
        } else {
            result=result+1;
            System.out.println("单词 '" + target + "' 在字典中的索引位置为: " + result);
        }
    }

    // 二分查找法实现
    public static int binarySearch(String[] dictionary, String target) {
        int left = 0; // 查找范围的左边界
        int right = dictionary.length - 1; // 查找范围的右边界

        while (left <= right) {
            int mid = left + (right - left) / 2; // 计算中间位置

            // 如果目标单词等于中间元素，返回中间位置
            if (dictionary[mid].equals(target)) {
                return mid;
            }
            // 如果目标单词小于中间元素，继续在左半部分查找
            else if (target.compareTo(dictionary[mid]) < 0) {
                right = mid - 1;
            }
            // 如果目标单词大于中间元素，继续在右半部分查找
            else {
                left = mid + 1;
            }
        }

        // 如果查找结束仍然没有找到目标单词，返回 -1
        return -1;
    }
}

