package gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import logic.GameProcessor;

import java.util.HashMap;
import java.util.Map;

public class SuggestAccuseScreen {

	private Stage stage;

	private String[] characters = {"Miss Scarlet", "Col. Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Prof. Plum"};
	private String[] weapons = {"Rope", "Lead Pipe", "Knife", "Wrench", "Candlestick", "Revolver"};
	private String[] rooms = {"Study", "Library", "Conservatory", "Hall", "Billiard Room", "Ballroom", "Lounge",
			"Dining Room", "Kitchen"};

	private String selectedCharacter;
	private String selectedWeapon;
	private String selectedRoom;

	private Map<String, Rectangle> characterRectMap = new HashMap<>();
	private Map<String, Rectangle> weaponRectMap = new HashMap<>();
	private Map<String, Rectangle> roomRectMap = new HashMap<>();

	private boolean isMakingSuggestion;

	private static final Color SELECTED_COLOR = Color.GOLD;
	private static final Color UNSELECTED_COLOR = Color.WHITE;
	private static final Color DISABLED_COLOR = Color.GRAY;

	public SuggestAccuseScreen(Stage stage, String currentRoom)
	{
		this.stage = stage;
		selectedRoom = currentRoom;
		this.isMakingSuggestion = true;
	}
	public SuggestAccuseScreen(Stage stage)
	{
		this.stage = stage;
		this.isMakingSuggestion = false;
	}
	
	public Scene createScene() {
		//suggestion/accusation selection area
		HBox charactersBox = new HBox();
		for (String character : characters) {
			Rectangle rect = new Rectangle(100, 175);
			rect.setFill(UNSELECTED_COLOR);
			rect.setStroke(Color.BLACK);
			Text text = new Text(character);
			StackPane characterSelect = new StackPane(rect, text);
			characterSelect.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					setSelectedCharacter(character);
				}
			});
			characterRectMap.put(character, rect);
			charactersBox.getChildren().add(characterSelect);
		}
		HBox weaponsBox = new HBox();
		for (String weapon : weapons) {
			Rectangle rect = new Rectangle(100, 175);
			rect.setFill(UNSELECTED_COLOR);
			rect.setStroke(Color.BLACK);
			Text text = new Text(weapon);
			StackPane weaponSelect = new StackPane(rect, text);
			weaponSelect.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					setSelectedWeapon(weapon);
				}
			});
			weaponRectMap.put(weapon, rect);
			weaponsBox.getChildren().add(weaponSelect);
		}
		HBox roomsBox = new HBox();
		for (String room : rooms) {
			Rectangle rect = new Rectangle(100, 175);
			rect.setStroke(Color.BLACK);
			Text text = new Text(room);
			StackPane roomSelect = new StackPane(rect, text);
			roomRectMap.put(room, rect);
			if (isMakingSuggestion) {
				if (room.equals(selectedRoom)) {
					rect.setFill(SELECTED_COLOR);
				}
				else {
					rect.setFill(DISABLED_COLOR);
				}
			}
			else {
				rect.setFill(UNSELECTED_COLOR);
				roomSelect.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						setSelectedRoom(room);
					}
				});
			}
			roomsBox.getChildren().add(roomSelect);
		}
		VBox rows = new VBox(20, charactersBox, weaponsBox, roomsBox);
		Group selections = new Group(rows);

		//submit and close buttons
		Button submitBtn = new Button("Submit");
		submitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//TODO: massage to whatever method(s) GameProcessor provides for submitting suggestions and accussations
//				if (isMakingSuggestion)
//					GameProcessor.submitSuggestion(selectedCharacter, selectedWeapon, selectedRoom);
//				else
//					GameProcessor.submitAccusation(selectedCharacter, selectedWeapon, selectedRoom);
//				System.out.println("Submitted: " + selectedCharacter + ", " + selectedWeapon + ", " + selectedRoom);
				stage.close();
			}
		});
		Button closeBtn = new Button("Close");
		closeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				stage.close();
			}
		});
		HBox controlsBox = new HBox(10, submitBtn, closeBtn);
		Group controls = new Group(controlsBox);
		controls.setLayoutX(800);
		controls.setLayoutY(600);

		Group root = new Group(selections, controls);
        
		Scene startScene = new Scene(root, 1000, 700);
		
		return startScene;
	}

	public void setSelectedCharacter(String selectedCharacter) {
		if (this.selectedCharacter != null) {
			characterRectMap.get(this.selectedCharacter).setFill(UNSELECTED_COLOR);
		}
		characterRectMap.get(selectedCharacter).setFill(SELECTED_COLOR);
		this.selectedCharacter = selectedCharacter;

	}
	public String getSelectedCharacter() {
		return selectedCharacter;
	}

	public void setSelectedWeapon(String selectedWeapon) {
		if (this.selectedWeapon != null) {
			weaponRectMap.get(this.selectedWeapon).setFill(UNSELECTED_COLOR);
		}
		weaponRectMap.get(selectedWeapon).setFill(SELECTED_COLOR);
		this.selectedWeapon = selectedWeapon;
	}
	public String getSelectedWeapon() {
		return selectedWeapon;
	}

	public void setSelectedRoom(String selectedRoom) {
		if (this.selectedRoom != null) {
			roomRectMap.get(this.selectedRoom).setFill(UNSELECTED_COLOR);
		}
		roomRectMap.get(selectedRoom).setFill(SELECTED_COLOR);
		this.selectedRoom = selectedRoom;
	}
	public String getSelectedRoom() {
		return selectedRoom;
	}
}
