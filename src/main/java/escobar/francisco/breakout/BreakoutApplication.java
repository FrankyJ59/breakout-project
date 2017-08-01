package escobar.francisco.breakout;

import java.util.Random;

import acm.graphics.GRect;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BreakoutApplication extends Application {

	// with of game display
	private static final int WIDTH = 400;

	// height of game display
	private static final int HEIGHT = 600;

	// width of paddle
	private static final int PADDLE_WIDTH = 60;

	// height of paddle
	private static final int PADDLE_HEIGHT = 10;

	// offset of paddle up from the bottom
	private static final int PADDLE_Y_OFFSET = 30;

	// number of bricks per row
	private static final int NBRICKS_PER_ROW = 10;

	// number of rows of bricks
	private static final int NBRICK_ROWS = 10;

	// separation between bricks
	private static final int BRICK_SEP = 4;

	// width of each brick (based on the dimensions of the game display)
	private static final int BRICK_WIDTH = WIDTH / NBRICKS_PER_ROW - BRICK_SEP;

	// height of brick
	private static final int BRICK_HEIGHT = 8;

	// radius of ball in pixels
	private static final int BALL_RADIUS = 6;

	// offset of the top brick row from top
	private static final int BRICK_Y_OFFSET = 70;

	// number of turns
	private static final int NTURNS = 3;

	// the paddle
	private Rectangle paddle;

	// the ball
	private Circle ball;

	// ball velocity in both direction (x-direction and y-direction)
	private double vx, vy;

	// records the last x position of the mouse
	private double lastX;

	// used for mouse events
	private int toggle;

	// random generators for ball start
	private Random rgen;

	// lives
	private int countLives = 3;

	// score
	private int countScore = 0;

	// bricks
	private int countBricks = 300;

	// paddle hits
	private int paddleHits = 0;

	// number of bricks hit
	private int bricksHit = 0;

	private Label lives, score, label, loser, winner;

	Group root;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {

		// setup
		primaryStage.setTitle("Breakout");

		root = new Group();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		createBricks();
		createPaddle();
		createBall();

		primaryStage.show();
	}
	
	public void createPaddle() {
		paddle = new Rectangle((WIDTH - PADDLE_WIDTH) / 2, (HEIGHT - PADDLE_HEIGHT) - PADDLE_Y_OFFSET, PADDLE_WIDTH,
				PADDLE_HEIGHT);
		paddle.setFill(Color.GRAY.brighter());
		root.getChildren().add(paddle);
	}
	
	public void createBall() {
		ball = new Circle((WIDTH - (BALL_RADIUS * 2)) / 2, (HEIGHT - (BALL_RADIUS * 2)) / 2, BALL_RADIUS);
		ball.setFill(Color.GRAY.brighter());
		root.getChildren().add(ball);
	}
	
	private Object getCollidingObject() {
		if (getElementAt(ball.getCenterX(), ball.getCenterY()) != null && getElementAt(ball.getCenterX(), ball.getCenterY()) instanceof Rectangle)
			return getElementAt(ball.getCenterX(), ball.getCenterY());
		else if (getElementAt(ball.getCenterX() + BALL_RADIUS * 2, ball.getCenterY()) != null
				&& getElementAt(ball.getCenterX() + BALL_RADIUS * 2, ball.getCenterY()) instanceof Rectangle)
			return getElementAt(ball.getCenterX() + BALL_RADIUS * 2, ball.getCenterY());
		else if (getElementAt(ball.getCenterX() + BALL_RADIUS * 2, ball.getCenterY() + BALL_RADIUS * 2) != null
				&& getElementAt(ball.getCenterX() + BALL_RADIUS * 2, ball.getCenterY() + BALL_RADIUS * 2) instanceof Rectangle)
			return getElementAt(ball.getCenterX() + BALL_RADIUS * 2, ball.getCenterY() + BALL_RADIUS * 2);
		else if (getElementAt(ball.getCenterX(), ball.getCenterY() + BALL_RADIUS * 2) != null
				&& getElementAt(ball.getCenterX(), ball.getCenterY() + BALL_RADIUS * 2) instanceof Rectangle)
			return getElementAt(ball.getCenterX(), ball.getCenterY() + BALL_RADIUS * 2);
		else
			return null;
	}

	public void createBricks() {
		// make the bricks
		for (int x = 0; x < NBRICK_ROWS; x++) {
			for (int y = 0; y < NBRICKS_PER_ROW; y++) {
				Rectangle brick = new Rectangle((y * BRICK_WIDTH) + BRICK_SEP * y + BRICK_SEP / 2,
						BRICK_Y_OFFSET + (BRICK_HEIGHT * x) + BRICK_SEP * x, BRICK_WIDTH, BRICK_HEIGHT);
				if (x <= 9) {
					brick.setFill(Color.CYAN.darker().darker());
				}
				root.getChildren().addAll(brick);
			}
		}
		for (int a = 0; a < 8; a++) {
			for (int b = 0; b < NBRICKS_PER_ROW; b++) {
				Rectangle brick = new Rectangle((b * BRICK_WIDTH) + BRICK_SEP * b + BRICK_SEP / 2,
						BRICK_Y_OFFSET + (BRICK_HEIGHT * a) + BRICK_SEP * a, BRICK_WIDTH, BRICK_HEIGHT);
				if (a <= 7) {
					brick.setFill(Color.BLUE.darker().darker());
				}
				root.getChildren().addAll(brick);
			}
		}
		for (int c = 0; c < 6; c++) {
			for (int d = 0; d < NBRICKS_PER_ROW; d++) {
				Rectangle brick = new Rectangle((d * BRICK_WIDTH) + BRICK_SEP * d + BRICK_SEP / 2,
						BRICK_Y_OFFSET + (BRICK_HEIGHT * c) + BRICK_SEP * c, BRICK_WIDTH, BRICK_HEIGHT);
				if (c <= 5) {
					brick.setFill(Color.GREEN.darker().darker());
				}
				root.getChildren().addAll(brick);
			}
		}
		for (int e = 0; e < 4; e++) {
			for (int f = 0; f < NBRICKS_PER_ROW; f++) {
				Rectangle brick = new Rectangle((f * BRICK_WIDTH) + BRICK_SEP * f + BRICK_SEP / 2,
						BRICK_Y_OFFSET + (BRICK_HEIGHT * e) + BRICK_SEP * e, BRICK_WIDTH, BRICK_HEIGHT);
				if (e <= 3) {
					brick.setFill(Color.ORANGE.darker().darker());
				}
				root.getChildren().addAll(brick);
			}
		}
		for (int g = 0; g < 2; g++) {
			for (int h = 0; h < NBRICKS_PER_ROW; h++) {
				Rectangle brick = new Rectangle((h * BRICK_WIDTH) + BRICK_SEP * h + BRICK_SEP / 2,
						BRICK_Y_OFFSET + (BRICK_HEIGHT * g) + BRICK_SEP * g, BRICK_WIDTH, BRICK_HEIGHT);
				if (g <= 1) {
					brick.setFill(Color.RED.darker().darker());
				}
				root.getChildren().addAll(brick);
			}
		}
	}
}
