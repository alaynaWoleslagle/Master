package gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;
import java.util.Map;

public class GameScreen {

	private Color[] colors = {Color.RED, Color.PURPLE, Color.GRAY, Color.YELLOW, Color.GREEN, Color.BLUE};
	private static final Color ACTIVE_PLAYER_COLOR = Color.GOLD;
	private static final Color DISPROVING_PLAYER_COLOR = Color.CYAN;

	private static final int SCENE_WIDTH = 900;
	private static final int SCENE_HEIGHT = 1000;
	//to be used if showing dev buttons
	private static final int SCENE_DEV_HEIGHT = 1200;

	private String[] players;
	private int numPlayers;
	private String[] cards;
	private int assignedTurnIndex;
	private int currentTurnIndex = 0;

	private Group root;
	private Rectangle[] turnRects;
	private ScrollPane gameLogScroll;
	private Text gameLogText;
	private Button suggestBtn;
	private Button accuseBtn;
	private Text disproveText;
	private Map<String, Room> roomMap = new HashMap<>();
	private Map<String, Hallway> hallwayMap = new HashMap<>();

	private int tempDevIndex = 0;
	private UserInterface ui;

	//expects players in turn order
	public GameScreen(String[] players, String[] cards, int assignedTurnIndex) {
		this.players = players;
		numPlayers = players.length;
		this.cards = cards;
		this.assignedTurnIndex = assignedTurnIndex;
	}
	
	public Scene createScene() {
		root = new Group();

		//Game Board Display
		Group gameBoard = new Group();

		// Build rooms
		String[] roomNames = {"Study", "Library", "Conservatory", "Hall", "Billiard Room", "Ballroom", "Lounge",
				"Dining Room", "Kitchen"};
		int roomIndex = 0;
		for (int i = 0 ; i < 3 ; i++) {
			for (int j = 0 ; j < 3 ; j++) {
				int x = i * 200;
				int y = j * 200;
				Room room = new Room(x, y, roomNames[roomIndex]);
				room.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						//TODO
					}
				});
				roomMap.put(room.getName(), room);
				gameBoard.getChildren().add(room);

				if (i < 2) {
					//draw hallway to right
					Hallway hallway = new Hallway(x + 100, y + 35, 100, 30);
					hallway.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							//TODO
						}
					});
					hallwayMap.put(roomNames[roomIndex] + "-" + roomNames[roomIndex + 3], hallway);
					gameBoard.getChildren().add(hallway);
				}
				if (j < 2) {
					//draw hallway below
					Hallway hallway = new Hallway(x + 35, y + 100, 30, 100);
					hallway.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							//TODO
						}
					});
					hallwayMap.put(roomNames[roomIndex] + "-" + roomNames[roomIndex + 1], hallway);
					gameBoard.getChildren().add(hallway);
				}

				++roomIndex;
			}
		}

		gameBoard.setLayoutX(root.getLayoutX() + 50);
		gameBoard.setLayoutY(root.getLayoutY() + 50);
		root.getChildren().add(gameBoard);


		//Turn Display
		Group turnDisplay = new Group();
		HBox turnBox = new HBox();
		turnRects = new Rectangle[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			Rectangle turnRect = new Rectangle(30, 100);
			turnRect.setFill(colors[i]);
			turnRect.setStroke(Color.BLACK);
			turnRect.setStrokeWidth(2);
			turnBox.getChildren().add(turnRect);
			turnRects[i] = turnRect;
		}
		turnDisplay.getChildren().add(turnBox);
		turnDisplay.setLayoutX(root.getLayoutX() + 610);
		turnDisplay.setLayoutY(root.getLayoutY() + 50);
		root.getChildren().add(turnDisplay);


		//Game Log Display
		Group gameLog = new Group();
		gameLogScroll = new ScrollPane();
		gameLogScroll.setPrefHeight(100);
		gameLogScroll.setMinWidth(500);
		gameLogScroll.setVvalue(1);
		gameLogText = new Text();
		gameLogScroll.setContent(gameLogText);
		gameLog.getChildren().add(gameLogScroll);
		gameLog.setLayoutX(root.getLayoutX() + 50);
		gameLog.setLayoutY(root.getLayoutY() + 600);
		root.getChildren().add(gameLog);


		//Hand Display
		HBox handBox = new HBox(10);
		handBox.setMinWidth(500);
		//TODO: align card center
		for (int i = 0; i < cards.length; i++) {
			Rectangle cardRect = new Rectangle(100, 175);
			cardRect.setFill(Color.WHITE);
			cardRect.setStroke(Color.BLACK);
			Text cardText = new Text(cards[i]);
			cardText.setWrappingWidth(100);
			cardText.setTextAlignment(TextAlignment.CENTER);
			StackPane card = new StackPane(cardRect, cardText);
			handBox.getChildren().add(card);
		}
		Group hand = new Group(handBox);
		hand.setLayoutX(root.getLayoutX() + 50);
		hand.setLayoutY(root.getLayoutY() + 725);
		root.getChildren().add(hand);


		//Controls Display
		suggestBtn = new Button("Make Suggestion");
		suggestBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//TODO
			}
		});
		accuseBtn = new Button("Make Accusation");
		accuseBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//TODO
			}
		});
		VBox controlsBox = new VBox(10, suggestBtn, accuseBtn);
		//TODO: align center
		Group controls = new Group(controlsBox);
		controls.setLayoutX(root.getLayoutX() + 625);
		controls.setLayoutY(root.getLayoutY() + 615);
		root.getChildren().add(controls);


		//Disprove Display
		Rectangle disproveRect = new Rectangle(200, 350);
		disproveRect.setFill(Color.WHITE);
		disproveRect.setStroke(Color.BLACK);
		disproveText = new Text();
		StackPane disprovePane = new StackPane(disproveRect, disproveText);
		Group disproveDisplay = new Group(disprovePane);
		disproveDisplay.setLayoutX(root.getLayoutX() + 600);
		disproveDisplay.setLayoutY(root.getLayoutY() + 200);
		root.getChildren().add(disproveDisplay);

		//start of game setup
		turnRects[currentTurnIndex].setStroke(ACTIVE_PLAYER_COLOR);

		showDevBtns();

		return new Scene(root, SCENE_WIDTH, SCENE_DEV_HEIGHT);
	}

	private void startTurn() {
		enableActions();
		enableValidMoves();
	}
	private void endTurn() {
		disableActions();
		disableMoves();
	}

	public void startNextTurn() {
		turnRects[currentTurnIndex].setStroke(Color.BLACK);
		currentTurnIndex++;
		if (currentTurnIndex >= numPlayers) {
			currentTurnIndex = 0;
		}
		turnRects[currentTurnIndex].setStroke(ACTIVE_PLAYER_COLOR);

		if (currentTurnIndex == assignedTurnIndex) {
			startTurn();
		}
		else {
			endTurn();
		}
	}

	public void indicateDisprovingPlayer(int disprovingPlayerIndex) {
		for (Rectangle turnRect : turnRects) {
			if (turnRect.getStroke().equals(DISPROVING_PLAYER_COLOR)) {
				turnRect.setStroke(Color.BLACK);
			}
		}
		turnRects[disprovingPlayerIndex].setStroke(DISPROVING_PLAYER_COLOR);
	}

	public void addToGameLog(String text) {
		if (gameLogText.getText().equals(""))
			gameLogText.setText(text);
		else {
			gameLogText.setText(gameLogText.getText() + "\n" + text);
		}
		gameLogScroll.setVvalue(1);
	}
	public void clearGameLog() {
		gameLogText.setText("");
	}

	public void displayDisprovingCard(String card) {
		disproveText.setText(card);
	}
	public void removeDisprovingCard() {
		disproveText.setText("");
	}


	private void enableActions() {
		suggestBtn.setDisable(false);
		accuseBtn.setDisable(false);
	}
	private void disableActions() {
		suggestBtn.setDisable(true);
		accuseBtn.setDisable(true);
	}

	private void disableMoves() {
		//TODO: for final submission?
	}
	private void enableValidMoves() {
		//TODO: for final submission?
	}



	/****************************************************************/

	//to be used for testing switch scene button
	public void setUi (UserInterface ui) {
		this.ui = ui;
	}

	//to be used to test functionality (should be deleted later)
	private void showDevBtns() {
		Button btn1 = new Button("next turn");
		btn1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				startNextTurn();
			}
		});
		Button btn2 = new Button("add to text");
		btn2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				addToGameLog("text for log...");
			}
		});
		Button btn3 = new Button("clear text");
		btn3.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				clearGameLog();
			}
		});
		Button btn4 = new Button("set disproving");
		btn4.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				indicateDisprovingPlayer(2);
			}
		});
		Button btn5 = new Button("set d card");
		btn5.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				displayDisprovingCard("pistol");
			}
		});
		Button btn6 = new Button("clear d card");
		btn6.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				removeDisprovingCard();
			}
		});
		Button btn7 = new Button("add to room");
		btn7.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (tempDevIndex < numPlayers) {
					roomMap.get("Kitchen").addOccupant("p" + tempDevIndex, colors[tempDevIndex++]);
					if (tempDevIndex > numPlayers + 1)
						tempDevIndex = numPlayers + 1;
				}
			}
		});
		Button btn8 = new Button("remove from room");
		btn8.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (tempDevIndex > 0) {
					roomMap.get("Kitchen").removeOccupant("p" + --tempDevIndex);
					if (tempDevIndex < 0) {
						tempDevIndex = 0;
					}
				}
			}
		});
		Button btn9 = new Button("add p2 to room");
		btn9.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				roomMap.get("Kitchen").addOccupant("p2", colors[2]);
			}
		});
		Button btn10 = new Button("remove p2 from room");
		btn10.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				roomMap.get("Kitchen").removeOccupant("p2");
			}
		});
		Button btn11 = new Button("add p1 to hallway");
		btn11.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!hallwayMap.get("Study-Library").isOccupied()) {
					hallwayMap.get("Study-Library").addOccupant("p1", colors[1]);
				}
			}
		});
		Button btn12 = new Button("remove p1 from hallway");
		btn12.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				hallwayMap.get("Study-Library").removeOccupant();
			}
		});
		Button btn13 = new Button("change scene");
		btn13.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ui.setScene(ui.createStartScreen());
			}
		});
		VBox tempControlsBox = new VBox(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11, btn12, btn13);
		//TODO: align center
		Group tempControls = new Group(tempControlsBox);
		tempControls.setLayoutX(root.getLayoutX() + 625);
		tempControls.setLayoutY(root.getLayoutY() + 725);
		root.getChildren().add(tempControls);
	}
}
