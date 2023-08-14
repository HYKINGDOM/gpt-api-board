package com.java.flex.javaflexdemo;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.github.rholder.retry.StopStrategies;
import com.java.flex.javaflexdemo.constant.Piece;
import com.java.flex.javaflexdemo.constant.StoneColor;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;

import java.util.concurrent.ThreadPoolExecutor;

import static com.java.flex.javaflexdemo.constant.CommonConstant.HEIGHT;
import static com.java.flex.javaflexdemo.constant.CommonConstant.TILE_SIZE;
import static com.java.flex.javaflexdemo.constant.CommonConstant.WIDTH;
import static com.java.flex.javaflexdemo.gpt.OpenAiApi.executeRequest;
import static com.java.flex.javaflexdemo.util.BoardCheckUtil.checkWin;
import static com.java.flex.javaflexdemo.util.ThreadExecutorUtil.threadExecutor;

/**
 * @author kscs
 */
public class HelloController {

    @FXML
    private StackPane canvasPane;

    @FXML
    private Button startGameButton;

    /**
     * 初始化为黑子先手
     */
    private StoneColor currentPlayer = StoneColor.BLACK;

    /**
     * 棋盘记录 0: 无子, 1: 白子, 2: 黑子
     */
    private int[][] board;

    private int row;

    private int col;

    private GraphicsContext graphicsContext2D;

    private final ThreadPoolExecutor THREAD_POOL_EXECUTOR = threadExecutor();

    @FXML
    protected void onHelloButtonClick() {
        Canvas canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        canvasPane.getChildren().add(canvas);
        graphicsContext2D = canvas.getGraphicsContext2D();

        //隐藏button
        startGameButton.setVisible(false);
        drawBoard(graphicsContext2D);

        board = new int[(int)HEIGHT][(int)WIDTH];

        Retryer<Integer> retryer = RetryerBuilder.<Integer>newBuilder()
            // 非正数进行重试
            .retryIfRuntimeException()
            // 偶数则进行重试
            .retryIfResult(result -> result % 2 == 0)
            // 设置最大执行次数3次
            .withStopStrategy(StopStrategies.stopAfterAttempt(3)).build();



        canvasPane.setOnMouseClicked(mouseEvent -> {

            if (currentPlayer == StoneColor.BLACK) {

                double mouseX = mouseEvent.getX();
                double mouseY = mouseEvent.getY();

                row = (int)(mouseY / TILE_SIZE);
                col = (int)(mouseX / TILE_SIZE);

                System.out.println("Mouse clicked at: (" + mouseX + ", " + mouseY + ")");
                System.out.println("Mouse clicked at: (" + row + ", " + col + ")");

                // 落子操作
                goPlay(row, col);

                THREAD_POOL_EXECUTOR.submit(playWhiteTask);
            }
        });
    }

    private final Runnable playWhiteTask = () -> {
        if (currentPlayer == StoneColor.WHITE) {
            System.out.println(StoneColor.WHITE + " play");
            Pair<Integer, Integer> gpt = gpt(row, col);
            goPlay(gpt.getKey(), gpt.getValue());
            currentPlayer = StoneColor.BLACK;
        }
    };

    private void goPlay(int row, int col) {
        if (placeStone(row, col, graphicsContext2D, currentPlayer)) {

            printBoard();

            // 落子成功切换下一位玩家
            if (checkWin(row, col, currentPlayer, board)) {
                showWinDialog(currentPlayer);
            } else {
                currentPlayer = (currentPlayer == StoneColor.BLACK) ? StoneColor.WHITE : StoneColor.BLACK;
            }
        }
    }

    private Pair<Integer, Integer> gpt(int row, int col) {

        String str = "[%s,%s]";

        String coordinateUser = String.format(str, row, col);

        String coordinateGpt = executeRequest(coordinateUser);

        Integer subAfter = Integer.valueOf(StrUtil.subAfter(coordinateGpt, ",", true));

        Integer subBefore = Integer.valueOf(StrUtil.subBefore(coordinateGpt, ",", true));

        return new Pair<>(subBefore, subAfter);
    }

    private void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(board[i][j]);
                if (j == 18) {
                    System.out.println();
                }
            }
        }
    }

    private void drawBoard(GraphicsContext gc) {
        gc.setFill(Color.valueOf("#EECFA1"));
        gc.fillRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        for (int i = 0; i < WIDTH; i++) {
            gc.strokeLine(i * TILE_SIZE + TILE_SIZE / 2, TILE_SIZE / 2, i * TILE_SIZE + TILE_SIZE / 2,
                HEIGHT * TILE_SIZE - TILE_SIZE / 2);
        }

        for (int i = 0; i < HEIGHT; i++) {
            gc.strokeLine(TILE_SIZE / 2, i * TILE_SIZE + TILE_SIZE / 2, WIDTH * TILE_SIZE - TILE_SIZE / 2,
                i * TILE_SIZE + TILE_SIZE / 2);
        }
    }

    private boolean placeStone(int row, int col, GraphicsContext graphicsContext, StoneColor color) {
        if (row >= 0 && row < HEIGHT && col >= 0 && col < WIDTH) {
            // 检查该位置是否已经有棋子
            if (isPositionOccupied(row, col)) {
                // 获取该位置的坐标
                double x = col * TILE_SIZE;
                double y = row * TILE_SIZE;

                // 绘制棋子
                graphicsContext.setFill(color == StoneColor.BLACK ? Color.BLACK : Color.WHITE);
                graphicsContext.fillOval(x + TILE_SIZE / 4, y + TILE_SIZE / 4, TILE_SIZE / 2, TILE_SIZE / 2);
                //标记该位置已经落子
                board[row][col] = color == StoneColor.BLACK ? 2 : 1;
                return true;
            }
        }

        return false;
    }

    /**
     * 校验该位置是否已经落子
     *
     * @param row
     * @param col
     * @return
     */
    private boolean isPositionOccupied(int row, int col) {
        return board[row][col] == 0;
    }

    private void showWinDialog(StoneColor color) {
        String name = color == StoneColor.BLACK ? Piece.BLACK.getName() : Piece.WHITE.getName();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("游戏结束");
        alert.setHeaderText(null);
        alert.setContentText(name + "方获胜！");
        alert.setOnHidden(evt -> restartGame());
        alert.showAndWait();
    }

    private void restartGame() {
        // 清空棋盘
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = 0;
            }
        }

        // 重绘棋盘
        drawBoard(graphicsContext2D);

        // 重置当前玩家
        currentPlayer = StoneColor.BLACK;
    }
}