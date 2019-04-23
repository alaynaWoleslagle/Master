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

import java.util.HashMap;
import java.util.Map;

public class SuggestAccuseScreen {

	private String[] characters = {"Miss Scarlet", "Col. Mustard", "Mrs. White", "Mr. Green", "Mrs. Peacock", "Prof. Plum"};
	private String[] weapons = {"Rope", "Lead Pipe", "Knife", "Wrench", "Candlestick", "Revolver"};
	private String[] rooms = {"Study", "Library", "Conservatory", "Hall", "Billiard Room", "Ballroom", "Lounge",
			"Dining Room", "Kitchen"};

	private String selectedCharacter;
	private String selectedWeapon;
	private String selectedRoom;

	private Map<String, Rectangle> characterRectMap = new HashMap<>();
	private Map<String, Rectangle> weaponRectMap = new HashMap<>();

	public SuggestAccuseScreen(String currentRoom)
	{
		selectedRoom = currentRoom;
	}
	
	public Scene createScene() {
		//suggestion/accusation selection area
		HBox charactersBox = new HBox();
		for (String character : characters) {
			Rectangle rect = new Rectangle(100, 175);
			rect.setFill(Color.WHITE);
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
			rect.setFill(Color.WHITE);
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
			if (room.equals(selectedRoom)) {
				rect.setFill(Color.GOLD);
			}
			else {
				rect.setFill(Color.GRAY);
			}
			rect.setStroke(Color.BLACK);
			Text text = new Text(room);
			StackPane roomSelect = new StackPane(rect, text);
			roomsBox.getChildren().add(roomSelect);
		}
		VBox rows = new VBox(20, charactersBox, weaponsBox, roomsBox);
		Group selections = new Group(rows);

		//submit and close buttons
		Button submitBtn = new Button("Submit");
		submitBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//TODO
			}
		});
		Button closeBtn = new Button("Close");
		closeBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//TODO
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
			characterRectMap.get(this.selectedCharacter).setFill(Color.WHITE);
		}
		characterRectMap.get(selectedCharacter).setFill(Color.GOLD);
		this.selectedCharacter = selectedCharacter;

	}
	public String getSelectedCharacter() {
		return selectedCharacter;
	}

	public void setSelectedWeapon(String selectedWeapon) {
		if (this.selectedWeapon != null) {
			weaponRectMap.get(this.selectedWeapon).setFill(Color.WHITE);
		}
		weaponRectMap.get(selectedWeapon).setFill(Color.GOLD);
		this.selectedWeapon = selectedWeapon;
	}
	public String getSelectedWeapon() {
		return selectedWeapon;
	}

	public String getSelectedRoom() {
		return selectedRoom;
	}
}
