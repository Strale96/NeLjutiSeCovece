package Handlers;

import Game.Covece;
import Objects.DiceField;
import Objects.FullBoard;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class GameHandler {

	private Covece Game;
	private Rectangle 	PauseRec, PauseTextRec, Selection;
	private Text[] 		MainOption;
	private Text[] 		PauseOption;
	private Text[]		PlSelectOption;
	private Rectangle[] TypeSelect = new Rectangle[4];
	private Text 		TitleText, LevelOver, LevelText, Autor;
	public boolean 	IsPaused = false, IsTypeSelection = false,
					IsMainMenu = false, GamePlaying = false, 
					IsPlSelection = false, DiceTurn = true;
	
	private boolean DiceIsRolled = true;
	public int DiceValue = 6;
	public int PlayerMove = 0;
	private int MaxPlayers = 4;
	private int[] FigTypes = new int[4];
	private Group[] GTypes = new Group[4];
	private Timeline[] RotateTimelines = new Timeline[4];
	private PointLight Svetlo;
	private int[] Occupied = new int[4];
	private int k = 0;
	private int[] Places = new int[4];
	private Timeline T;
	
	private int MenuCounter = 1,	MaxMenuCounter = 2, 
				PlSelection = 1, MaxPlSelection = 3,
				PauseCounter = 1, 	MaxPauseCounter = 3;
	
	public GameHandler(Covece Game){
		for (int i = 0; i < 4; i++) {
			FigTypes[i] = -1;
			Occupied[i] = -1;
		}
		MainOption = new Text[MaxMenuCounter];
		PauseOption = new Text[MaxPauseCounter];
		PlSelectOption = new Text[MaxPlSelection];
		this.Game = Game;
	}
	
	public void Click(){
		if (GamePlaying)
		if (DiceIsRolled){
			PrekiniBlinkanje();
			DiceTurn = false;
			
			boolean M = false;
			for (int i = 0; i < 4; i++){
				if (Game.FB.MoveExists(PlayerMove, i, DiceValue)){
					M = true;
					int PoljeBlink = Game.FB.SveFigure[PlayerMove][i].At() + DiceValue;
					if ((PlayerMove == 0) && (PoljeBlink > 39)) {
//						PoljeBlink -= 40;
					} else if ((PlayerMove == 1) && (PoljeBlink > 39)) {
						PoljeBlink -= 40;
					} else if ((PlayerMove == 2) && (PoljeBlink > 39)) {
						PoljeBlink -= 40;
					} else if ((PlayerMove == 3) && (PoljeBlink > 39)) {
						PoljeBlink -= 40;
					}
					if ((PlayerMove == 1) && (PoljeBlink > 10) && (Game.FB.SveFigure[PlayerMove][i].At() <= 10)) {
						int k = 0;
						k = Game.FB.SveFigure[PlayerMove][i].At() - 10 + DiceValue;
						PoljeBlink = 43 + k;
						System.out.println("POLJE: " + PoljeBlink + " k: " + k + " DICE: " + DiceValue + " AT: " + Game.FB.SveFigure[PlayerMove][i].At());
					} else if ((PlayerMove == 2) && (PoljeBlink > 20) && (Game.FB.SveFigure[PlayerMove][i].At() <= 20)) {
						int k = 0;
						k = Game.FB.SveFigure[PlayerMove][i].At() - 20 + DiceValue;
						PoljeBlink = 47 + k;
						System.out.println("POLJE: " + PoljeBlink + " k: " + k + " DICE: " + DiceValue + " AT: " + Game.FB.SveFigure[PlayerMove][i].At());
					} else if ((PlayerMove == 3) && (PoljeBlink > 30) && (Game.FB.SveFigure[PlayerMove][i].At() <= 30)) {
						int k = 0;
						k = Game.FB.SveFigure[PlayerMove][i].At() - 30 + DiceValue;
						PoljeBlink = 51 + k;
						System.out.println("POLJE: " + PoljeBlink + " k: " + k + " DICE: " + DiceValue + " AT: " + Game.FB.SveFigure[PlayerMove][i].At());
					} else if ((PlayerMove == 0) && (PoljeBlink > 40) && (Game.FB.SveFigure[PlayerMove][i].At() <= 40) && !Game.FB.FirstMove(PlayerMove, i, DiceValue)) {
						int k = 0;
						k = Game.FB.SveFigure[PlayerMove][i].At() - 40 + DiceValue;
						System.out.println("POLJE: " + PoljeBlink + " k: " + k + " DICE: " + DiceValue + " AT: " + Game.FB.SveFigure[PlayerMove][i].At());
					}
					if (Game.FB.FirstMove(PlayerMove, i, DiceValue)){
						Game.FB.BlinkajFiguru(PlayerMove, i);
						Game.FB.BlinkajPolje(PlayerMove*10+1);
						Game.FB.SveFigure[PlayerMove][i].NextStepExists = true;
						Game.FB.SveFigure[PlayerMove][i].Move = DiceValue;	
					} else {
						Game.FB.BlinkajFiguru(PlayerMove, i);
						Game.FB.BlinkajPolje(PoljeBlink);
						Game.FB.SveFigure[PlayerMove][i].NextStepExists = true;
						Game.FB.SveFigure[PlayerMove][i].Move = DiceValue;	
					}
					
				}
			}
			
			System.out.println(M);
			if (M == false) {
				DiceTurn = true;
				BlinkajKockicu();
			}
			PlayerMove++;
			if (PlayerMove == MaxPlayers) PlayerMove = 0;
		}
	}
	
	public void BlinkajKockicu(){
		Color c1 = Color.IVORY;
		Color c2 = new Color(0.72, 0.53, 0.04, 1.0);
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(c1);
		
		Game.DF.Kockica.setMaterial(mat);
		
		KeyValue Poc = new KeyValue(mat.diffuseColorProperty(), c1);
		KeyValue Kraj = new KeyValue(mat.diffuseColorProperty(), c2);
		KeyFrame prvi = new KeyFrame(Duration.millis(0), Poc);
		KeyFrame drugi = new KeyFrame(Duration.millis(500), Kraj);
		
		T = new Timeline(prvi, drugi);
		T.setCycleCount(Timeline.INDEFINITE);
		T.setAutoReverse(true);
		T.play();
	}
	
	public void PrekiniBlinkanje(){
		T.stop();
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(Color.IVORY);
		Game.DF.Kockica.setMaterial(mat);
	}
	
	public void LoadGame(){
		GamePlaying = true;
		PointLight svetlo = Game.napraviSvetlo(Game.root);
		PointLight svetlo1 = Game.napraviSvetlo(Game.root);
		svetlo1.setTranslateX(-1000);
		svetlo1.setTranslateZ(500);
		Game.DF = new DiceField();
		Game.kamera.FixKamera();
		Game.root.getChildren().addAll(Game.FB, Game.DF);
		Game.scena.setCamera(Game.kamera.kamera);
		BlinkajKockicu();
	}
	
	public void UP(){
		if (IsMainMenu){
			if (MenuCounter != 1){
				MenuCounter--;
				MainMenuSelection();
			}
		} else if (IsPlSelection){
			if (PlSelection != 1){
				PlSelection--;
				PlayerSelection();
			}
		}
	}
	public void DOWN(){
		if (IsMainMenu){
			if (MenuCounter != MaxMenuCounter){
				MenuCounter++;
				MainMenuSelection();
			}
		} else if (IsPlSelection){
			if (PlSelection != MaxPlSelection){
				PlSelection++;
				PlayerSelection();
			}
		}
	}
	public void ENTER(){
		if (IsMainMenu){
    		if (MenuCounter == 1){
    			IsMainMenu = false;
    			IsPlSelection = true;
    			LoadPlayerSelection();
    		}else if (MenuCounter == 2) {
    			Platform.exit();
    		}
    	} else if (IsPlSelection){
    		IsPlSelection = false;
    		if (PlSelection == 1){
    			MaxPlayers = 2;
    			Game.root.getChildren().removeAll(PauseRec, Selection, PauseRec, PauseTextRec, TitleText, Autor);
    			Game.root.getChildren().removeAll(PlSelectOption);
    			PlayerMove = 0;
    			Svetlo = napraviSvetlo();
    			LoadTypeSelection(0);
    		} else if (PlSelection == 2){
    			MaxPlayers = 3;
    			Game.root.getChildren().removeAll(PauseRec, Selection, PauseRec, PauseTextRec, TitleText, Autor);
    			Game.root.getChildren().removeAll(PlSelectOption);
    			PlayerMove = 0;
    			Svetlo = napraviSvetlo();
    			LoadTypeSelection(0);
    		} else if (PlSelection == 3){
    			MaxPlayers = 4;
    			Game.root.getChildren().removeAll(PauseRec, Selection, PauseRec, PauseTextRec, TitleText, Autor);
    			Game.root.getChildren().removeAll(PlSelectOption);
    			PlayerMove = 0;
    			Svetlo = napraviSvetlo();
    			LoadTypeSelection(0);
    		}
    		Click();
    	}
	}
	
	private void LoadTypeSelection(int Player){
		IsPlSelection = false;
		IsTypeSelection = true;
		PauseRec = new Rectangle(0, 0, 1250, 1150);
		PauseRec.setFill(Color.BLACK);
		PauseRec.setTranslateX(-150);
		PauseRec.setTranslateY(-150);
		PauseRec.setTranslateZ(350);
		PauseTextRec = new Rectangle(80, 20, 740, 110);
		PauseTextRec.setFill(null);
		PauseTextRec.setStrokeWidth(5);
		PauseTextRec.setStroke(Color.LIME);
		TitleText = new Text();
		TitleText.setText("Igrac " + (Player+1) + " Bira figuru");
		TitleText.setTranslateX(200);
		TitleText.setTranslateY(100);
		TitleText.setFont(new Font("Century Gothic", 65));
		TitleText.setFill(Color.LIME);
		Text Autor = new Text();
		Autor.setText("Strahinja Milovanovic");
		Autor.setFont(new Font("Century Gothic", 30));
		Autor.setTranslateY(770);
		Autor.setTranslateX(280);
		Autor.setFill(Color.WHITE);
		Game.root.getChildren().addAll(PauseRec, PauseTextRec, TitleText, Autor);
		GenerateTypeSelections(Player);
	}
	
	private PointLight napraviSvetlo(){
		PointLight svetlo = new PointLight();
		svetlo.setTranslateY(-150);
		svetlo.setTranslateZ(-350);
		svetlo.setTranslateX(150);
		svetlo.setColor(Color.WHITE);
		return svetlo;
	}
	
	private Group GenerateTypeOne(){
		Group g = new Group();
		
		PhongMaterial mat = new PhongMaterial();
		Color c = Color.WHITE;
		mat.setDiffuseColor(c);
		Cylinder postolje = new Cylinder(30, 15, 100);
		postolje.setMaterial(mat);
		postolje.setTranslateY(15);
		
		Cylinder telo = new Cylinder(21, 45, 100);
		telo.setMaterial(mat);
		telo.setTranslateY(-15);
		
		Cylinder vrat = new Cylinder(24, 15, 100);
		vrat.setMaterial(mat);
		vrat.setTranslateY(-45);
		
		Sphere glava = new Sphere(30, 100);
		glava.setMaterial(mat);
		glava.setTranslateY(-72);
		
		g.getChildren().addAll(postolje, telo, vrat, glava);
		
		return g;
	}
	
	private Group GenerateTypeTwo(){
		PhongMaterial mat = new PhongMaterial();
		Color c = Color.WHITE;
		mat.setDiffuseColor(c);
		Group g = new Group();
		
		Cylinder postolje = new Cylinder(30, 15, 100);
		postolje.setMaterial(mat);
		postolje.setTranslateY(15);
		
		Cylinder telo = new Cylinder(15, 75, 100);
		telo.setMaterial(mat);
		telo.setTranslateY(-15);
		
		Box G1 = new Box(24, 24, 60);
		G1.setMaterial(mat);
		G1.setTranslateY(-60);
		G1.getTransforms().add(new Rotate(45, Rotate.Y_AXIS));
		
		Box G2 = new Box(24, 24, 60);
		G2.setMaterial(mat);
		G2.setTranslateY(-60);
		G2.getTransforms().add(new Rotate(-45, Rotate.Y_AXIS));
		
		g.getChildren().addAll(postolje, telo, G1, G2);
		return g;
	}	
	private Group GenerateTypeThree(){
		PhongMaterial mat = new PhongMaterial();
		Color c = Color.WHITE;
		mat.setDiffuseColor(c);
		Group g = new Group();
		
		
		Cylinder postolje = new Cylinder(30, 15, 100);
		postolje.setMaterial(mat);
		postolje.setTranslateY(15);
		
		Cylinder telo = new Cylinder(27, 15, 100);
		telo.setMaterial(mat);
		telo.setTranslateY(-15);
		
		Cylinder vrat = new Cylinder(24, 15, 100);
		vrat.setMaterial(mat);
		vrat.setTranslateY(0);
		
		Cylinder C1 = new Cylinder(21, 15, 100);
		C1.setMaterial(mat);
		C1.setTranslateY(-30);
		
		Cylinder C2 = new Cylinder(18, 15, 100);
		C2.setMaterial(mat);
		C2.setTranslateY(-45);
		
		Cylinder C3 = new Cylinder(15, 15, 100);
		C3.setMaterial(mat);
		C3.setTranslateY(-60);
		
		Cylinder C4 = new Cylinder(39, 15, 100);
		C4.setMaterial(mat);
		C4.setTranslateY(-75);
		
		g.getChildren().addAll(postolje, telo, vrat, C1, C2, C3, C4);
		return g;
	}
	private Group GenerateTypeFour(){
		PhongMaterial mat = new PhongMaterial();
		Color c = Color.WHITE;
		mat.setDiffuseColor(c);
		Group g = new Group();
		
		Cylinder postolje = new Cylinder(30, 15, 100);
		postolje.setMaterial(mat);
		postolje.setTranslateY(15);
		
		Cylinder telo = new Cylinder(24, 18, 6);
		telo.setMaterial(mat);
		telo.setTranslateY(-3);
		
		Cylinder vrat = new Cylinder(15, 75, 100);
		vrat.setMaterial(mat);
		vrat.setTranslateY(-15);
		
		Cylinder C1 = new Cylinder(24, 18, 6);
		C1.setMaterial(mat);
		C1.setTranslateY(-63);
		
		g.getChildren().addAll(postolje, telo, vrat, C1);
		return g;
	}
	
	private void GenerateNextSelection(){
		PlayerMove++;
		Svetlo.setDisable(true);
		Game.root.getChildren().removeAll(PauseRec, PauseTextRec, TitleText, Autor);
		Game.root.getChildren().removeAll(TypeSelect);
		Game.root.getChildren().removeAll(GTypes);
		if (PlayerMove < MaxPlayers) LoadTypeSelection(PlayerMove);
		else {
			for (int i = 0; i < 4; i ++) System.out.print(FigTypes[i] + " " );
			PlayerMove = 0;
			Game.FB = new FullBoard(MaxPlayers,new int[] {1, 2, 3, 4} ,FigTypes, Game);
			LoadGame();
		}
	}
	
	private void GenerateTypeSelections(int Player){
		for (int i = 0; i < 4-Player; i++){
			TypeSelect[i] = new Rectangle(-50 + i*260, 280, 200, 350);
			TypeSelect[i].setFill(Color.BLACK);
			TypeSelect[i].setStrokeWidth(5);
			TypeSelect[i].setStroke(Color.LIME);
			TypeSelect[i].setTranslateZ(300);
			Group type = new Group();
			k = 0;
			for (int j = 0; j < 4; j++){
				if (Occupied[j] == -1){
					k = j;
					Occupied[j] = 0;
					break;
				}
			}
			Places[i] = k;
			
			switch(k){
			case 0: GTypes[i] = type = GenerateTypeOne(); break;
			case 1: GTypes[i] = type = GenerateTypeTwo(); break;
			case 2: GTypes[i] = type = GenerateTypeThree(); break;
			case 3: GTypes[i] = type = GenerateTypeFour(); break;
			}
			
			type.setTranslateX(110 + i*220);
			type.setTranslateY(480);
			type.getTransforms().add(new Rotate(25, Rotate.Z_AXIS));
			GTypes[i].setRotationAxis(Rotate.Y_AXIS);
//			Game.root.getChildren().addAll(Svetlo);
			KeyValue PocR = new KeyValue(GTypes[i].rotateProperty(), 0);
			KeyValue KrajR = new KeyValue(GTypes[i].rotateProperty(), 360);
			KeyFrame PrR = new KeyFrame(Duration.millis(0), PocR);
			KeyFrame KrR = new KeyFrame(Duration.millis(2000), KrajR);
			RotateTimelines[i] = new Timeline(PrR, KrR);
			RotateTimelines[i].setCycleCount(Timeline.INDEFINITE);
			if (i == 0) {
				TypeSelect[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
				    	RotateTimelines[0].play();
				    }
				});
				TypeSelect[i].setOnMouseExited(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						RotateTimelines[0].pause();
					}
				});
				GTypes[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
				    	RotateTimelines[0].play();
				    }
				});
				GTypes[i].setOnMouseExited(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						RotateTimelines[0].pause();
					}
				});
				TypeSelect[i].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						FigTypes[Player] = Places[0];
						Occupied[Places[0]] = 1;
						GenerateNextSelection();
					}
				});
				GTypes[i].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						FigTypes[Player] = Places[0];
						Occupied[Places[0]] = 1;
						GenerateNextSelection();
					}
				});
			}
			if (i == 1) {
				TypeSelect[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
				    	RotateTimelines[1].play();
				    }
				});
				TypeSelect[i].setOnMouseExited(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						RotateTimelines[1].pause();
					}
				});
				GTypes[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
				    	RotateTimelines[1].play();
				    }
				});
				GTypes[i].setOnMouseExited(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						RotateTimelines[1].pause();
					}
				});
				TypeSelect[i].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						FigTypes[Player] = Places[1];
						Occupied[Places[1]] = 1;
						GenerateNextSelection();
					}
				});
				GTypes[i].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						FigTypes[Player] = Places[1];
						Occupied[Places[1]] = 1;;
						GenerateNextSelection();
					}
				});
			}
			if (i == 2) {
				TypeSelect[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
				    	RotateTimelines[2].play();
				    }
				});
				TypeSelect[i].setOnMouseExited(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						RotateTimelines[2].pause();
					}
				});
				GTypes[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
				    	RotateTimelines[2].play();
				    }
				});
				GTypes[i].setOnMouseExited(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						RotateTimelines[2].pause();
					}
				});
				TypeSelect[i].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						FigTypes[Player] = Places[2];
						Occupied[Places[2]] = 1;
						GenerateNextSelection();
					}
				});
				GTypes[i].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						FigTypes[Player] = Places[2];
						Occupied[Places[2]] = 1;
						GenerateNextSelection();
					}
				});
			}
			if (i == 3) {
				TypeSelect[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
				    	RotateTimelines[3].play();
				    }
				});
				TypeSelect[i].setOnMouseExited(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						RotateTimelines[3].pause();
					}
				});
				GTypes[i].setOnMouseEntered(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent me) {
				    	RotateTimelines[3].play();
				    }
				});
				GTypes[i].setOnMouseExited(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						RotateTimelines[3].pause();
					}
				});
				TypeSelect[i].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						FigTypes[Player] = Places[3];
						Occupied[Places[3]] = 1;
						GenerateNextSelection();
					}
				});
				GTypes[i].setOnMouseClicked(new EventHandler<MouseEvent>(){
					public void handle(MouseEvent me){
						FigTypes[Player] = k;
						Occupied[k] = 1;
						GenerateNextSelection();
					}
				});
			}
			    
			
			Game.root.getChildren().addAll(TypeSelect[i], GTypes[i]);
		}
		for (int j = 0; j < 4; j++) if (Occupied[j] == 0) Occupied[j] = -1;
	}
	
	private void LoadPlayerSelection(){
		IsPlSelection = true;
		IsMainMenu = false;
		Game.root.getChildren().removeAll(PauseRec, PauseTextRec, TitleText, Autor);
		Game.root.getChildren().removeAll(MainOption);
		Game.root.getChildren().removeAll(PlSelectOption);
		PauseRec = new Rectangle(0, 0, 900, 800);
		PauseRec.setFill(Color.BLACK);
		PauseTextRec = new Rectangle(80, 20, 740, 110);
		PauseTextRec.setFill(null);
		PauseTextRec.setStrokeWidth(5);
		PauseTextRec.setStroke(Color.LIME);
		TitleText = new Text();
		TitleText.setText("Odaberite broj igraca");
		TitleText.setTranslateX(100);
		TitleText.setTranslateY(100);
		TitleText.setFont(new Font("Century Gothic", 65));
		TitleText.setFill(Color.LIME);
		Text Autor = new Text();
		Autor.setText("Strahinja Milovanovic");
		Autor.setFont(new Font("Century Gothic", 30));
		Autor.setTranslateY(770);
		Autor.setTranslateX(280);
		Autor.setFill(Color.WHITE);
		Game.root.getChildren().addAll(PauseRec, PauseTextRec, TitleText, Autor);
		PlayerSelection();
	}
	
	private void GeneratePlayerSelectionOptions(){
		for (int i = 0; i < MaxPlSelection; i++){
			PlSelectOption[i] = new Text();
			PlSelectOption[i].setTranslateX(300);
			PlSelectOption[i].setTranslateY(300 + i*150);
			PlSelectOption[i].setFont(new Font("Century Gothic", 50));
			PlSelectOption[i].setFill(Color.LIME);
		}
		PlSelectOption[0].setText("Dva igraca");
		PlSelectOption[1].setText("Tri igraca");
		PlSelectOption[2].setText("Cetiri igraca");
	}
	
	private void PlayerSelection(){
		Game.root.getChildren().remove(Selection);
		Game.root.getChildren().removeAll(PlSelectOption);
		GeneratePlayerSelectionOptions();
		Selection = new Rectangle(290, 100 + PlSelection*150, 330, 65);
		Selection.setFill(null);
		Selection.setStrokeWidth(3);
		Selection.setStroke(Color.LIME);
		Game.root.getChildren().addAll(PlSelectOption);
		Game.root.getChildren().add(Selection);
	}
	
	public void GenerateMainMenuOptions(){
		for (int i = 0; i < MaxMenuCounter; i++){
			MainOption[i] = new Text();
			MainOption[i].setTranslateX(300);
			MainOption[i].setTranslateY(300 + i*250);
			MainOption[i].setFont(new Font("Century Gothic", 50));
			MainOption[i].setFill(Color.LIME);
		}
		MainOption[0].setText("Start Game");
		MainOption[1].setText("Exit");
	}
	
	public void LoadMainMenu(){
		IsMainMenu = true;
		Game.root.getChildren().removeAll();
		PauseRec = new Rectangle(0, 0, 900, 800);
		PauseRec.setFill(Color.BLACK);
		PauseTextRec = new Rectangle(80, 20, 740, 110);
		PauseTextRec.setFill(null);
		PauseTextRec.setStrokeWidth(5);
		PauseTextRec.setStroke(Color.LIME);
		TitleText = new Text();
		TitleText.setText("Covece ne ljuti se");
		TitleText.setTranslateX(100);
		TitleText.setTranslateY(100);
		TitleText.setFont(new Font("Century Gothic", 80));
		TitleText.setFill(Color.LIME);
		Autor = new Text();
		Autor.setText("Strahinja Milovanovic");
		Autor.setFont(new Font("Century Gothic", 30));
		Autor.setTranslateY(770);
		Autor.setTranslateX(280);
		Autor.setFill(Color.WHITE);
		Game.root.getChildren().addAll(PauseRec, PauseTextRec, TitleText, Autor);
		MainMenuSelection();
	}
	public void MainMenuSelection(){
		Game.root.getChildren().remove(Selection);
		Game.root.getChildren().removeAll(MainOption);
		GenerateMainMenuOptions();
		Selection = new Rectangle(290, MenuCounter*250, 300, 65);
		Selection.setFill(null);
		Selection.setStrokeWidth(3);
		Selection.setStroke(Color.LIME);
		Game.root.getChildren().addAll(MainOption);
		Game.root.getChildren().add(Selection);
	}
}
