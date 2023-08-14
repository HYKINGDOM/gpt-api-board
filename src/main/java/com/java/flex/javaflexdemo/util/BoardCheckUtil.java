package com.java.flex.javaflexdemo.util;

import com.java.flex.javaflexdemo.constant.StoneColor;

import static com.java.flex.javaflexdemo.constant.CommonConstant.HEIGHT;
import static com.java.flex.javaflexdemo.constant.CommonConstant.WIDTH;

public class BoardCheckUtil {

    public static boolean checkWin(int row, int col, StoneColor color, int[][] board) {

        // 检查横向是否连成五子
        if (checkClem(row, col, color, board)) {
            return true;
        }

        // 检查纵向是否连成五子
        if (checkRow(row, col, color, board)) {
            return true;
        }

        // 检查左斜方向是否连成五子
        if (checkLeftOblique(row, col, color, board)) {
            return true;
        }

        // 检查右斜方向是否连成五子
        if (checkRightOblique(row, col, color, board)) {
            return true;
        }

        // 如果没有达成五子连线的条件，则返回false
        return false;
    }

    private static boolean checkClem(int row, int col, StoneColor color, int[][] board) {
        // 用于记录连续相同颜色的棋子数量
        // 因为已经放置了当前位置的棋子，所以从1开始计数
        int count = 1;
        // 向左遍历
        for (int i = col - 1; i >= 0; i--) {
            if (board[row][i] == (color == StoneColor.BLACK ? 2 : 1)) {
                count++;
            } else {
                break;
            }
        }
        // 向右遍历
        for (int i = col + 1; i < WIDTH; i++) {
            if (board[row][i] == (color == StoneColor.BLACK ? 2 : 1)) {
                count++;
            } else {
                break;
            }
        }
        return count >= 5;
    }

    private static boolean checkRow(int row, int col, StoneColor color, int[][] board) {
        // 用于记录连续相同颜色的棋子数量
        // 因为已经放置了当前位置的棋子，所以从1开始计数
        int count = 1;
        // 向上遍历
        for (int i = row - 1; i >= 0; i--) {
            if (board[i][col] == (color == StoneColor.BLACK ? 2 : 1)) {
                count++;
            } else {
                break;
            }
        }
        // 向下遍历
        for (int i = row + 1; i < HEIGHT; i++) {
            if (board[i][col] == (color == StoneColor.BLACK ? 2 : 1)) {
                count++;
            } else {
                break;
            }
        }
        return count >= 5;
    }

    private static boolean checkLeftOblique(int row, int col, StoneColor color, int[][] board) {
        // 用于记录连续相同颜色的棋子数量
        // 因为已经放置了当前位置的棋子，所以从1开始计数
        int count = 1;
        // 向左上遍历
        for (int i = 1; row - i >= 0 && col - i >= 0; i++) {
            if (board[row - i][col - i] == (color == StoneColor.BLACK ? 2 : 1)) {
                count++;
            } else {
                break;
            }
        }
        // 向右下遍历
        for (int i = 1; row + i < HEIGHT && col + i < WIDTH; i++) {
            if (board[row + i][col + i] == (color == StoneColor.BLACK ? 2 : 1)) {
                count++;
            } else {
                break;
            }
        }
        return count >= 5;
    }

    private static boolean checkRightOblique(int row, int col, StoneColor color, int[][] board) {
        // 用于记录连续相同颜色的棋子数量
        // 因为已经放置了当前位置的棋子，所以从1开始计数
        int count = 1;
        // 向右上遍历
        for (int i = 1; row - i >= 0 && col + i < WIDTH; i++) {
            if (board[row - i][col + i] == (color == StoneColor.BLACK ? 2 : 1)) {
                count++;
            } else {
                break;
            }
        }
        // 向左下遍历
        for (int i = 1; row + i < HEIGHT && col - i >= 0; i++) {
            if (board[row + i][col - i] == (color == StoneColor.BLACK ? 2 : 1)) {
                count++;
            } else {
                break;
            }
        }
        return count >= 5;
    }

}
