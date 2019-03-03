package Objects;

import Game.Covece;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class FullBoard extends Group {
	
	private Group[] Kucice = new Group[4];
	public Figure[][] SveFigure = new Figure[4][4];
	private int Zauzeto[][] = new int[56][4];
	private Cylinder[] Polja = new Cylinder[56];
	private Timeline[] Blinkeri = new Timeline[160];
	private int BlinkeriID[][] = new int[160][3];
	private int[] FigureType = new int[4];
	private int BlinkInd = 0;
	private int NumOfPlayers = 0;
	private int PlayerEat = 0, FigureEat = 0;
	private Covece Game;
	public Text KockicaT;
	
	public void SetFigureType(int Player, int Type){
		FigureType[Player] = Type;
	}
	
	private void napraviTablu(){
		PhongMaterial mat = new PhongMaterial();
//		mat.setDiffuseColor(Color.CYAN);
		mat.setDiffuseMap(new Image("Tekstura.jpg"));
		
		Box podijum = new Box(600, 30, 600);		
		podijum.setMaterial(mat);
		
		getChildren().addAll(podijum);
	}
	
	private void napraviPolja(int[] boje){
		
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(Color.IVORY);
		
		double u = 0, k = 0;
		
		for (int i = 0; i < 4; i ++){
			u += Math.PI/20;
			
			Color c = Color.WHITE;
			switch(boje[i]){
			case 1: c = Color.CRIMSON; break;
			case 2: c = Color.GREEN; break;
			case 3: c = Color.CORNFLOWERBLUE; break;
			case 4: c = Color.YELLOW; break;
			}
			
			PhongMaterial newMat = new PhongMaterial();
			newMat.setDiffuseColor(c);
			
			for (int j = 0; j < 9; j++){
				
				int index = 10*i + j + 1;
				
				Polja[index] = new Cylinder(15, 5, 100);
				Polja[index].setMaterial(mat);
				if (j == 0) Polja[index].setMaterial(newMat);
				
				int sgnSn = 1, sgnCs = 1;
				if (Math.sin(u) < 0) sgnSn = -1;
				if (Math.cos(u) < 0) sgnCs = -1;
				
				Polja[index].setTranslateY(-15);
				Polja[index].setTranslateX( Math.sin(u)*Math.sin(u)*Math.sin(u)*Math.sin(u)*Math.sin(u)*230 + sgnSn*50);
				Polja[index].setTranslateZ( Math.cos(u)*Math.cos(u)*Math.cos(u)*Math.cos(u)*Math.cos(u)*230 + sgnCs*50);

				getChildren().addAll(Polja[index]);
				u += Math.PI/20;
			}
			
			int mov = 280;
			for (int j = 0; j < 5; j++){
				int index = i*10;
				int indexj = 39 + i*4 + j;
				if (j == 0) {					
					Polja[index] = new Cylinder(15, 5, 100);
					Polja[index].setMaterial(mat);
					Polja[index].setTranslateY(-15);
					Polja[index].setTranslateX(Math.sin(k)*mov);
					Polja[index].setTranslateZ(Math.cos(k)*mov);
					getChildren().addAll(Polja[index]);
				} else {
					Polja[indexj] = new Cylinder(15, 5, 100);
					Polja[indexj].setMaterial(newMat);
					Polja[indexj].setTranslateY(-15);
					Polja[indexj].setTranslateX(Math.sin(k)*mov);
					Polja[indexj].setTranslateZ(Math.cos(k)*mov);
					getChildren().addAll(Polja[indexj]);
				}
				
				
				mov -= 40;
			}
			k += Math.PI/2;
		}
		setTranslateZ(0);
	}
	
	private void napraviSveFigure(int k, int v){
		double u = Math.PI/4; 
		for (int i = 0; i <= 3; i++){
			Figure figura = new Figure(k, v, i, this, FigureType[v]+1, v);
			figura.setTranslateY(-5);
			figura.setTranslateX(Math.sin(u)*40);
			figura.setTranslateZ(Math.cos(u)*40);
			
			SveFigure[v][i] = figura;
			
			getChildren().addAll(figura);
			
			u += Math.PI/2;
		}
	}
	
	private Group napraviPoljaKucica(){
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(Color.IVORY);
		Group g = new Group();
		
		double u = Math.PI/4;
		for (int i = 0; i < 4; i++){
			Cylinder polje = new Cylinder(15, 5, 100);
			polje.setMaterial(mat);
			
			polje.setTranslateY(2.5);
			polje.setTranslateX(Math.sin(u)*40);
			polje.setTranslateZ(Math.cos(u)*40);
	
			getChildren().addAll(polje);
			u += Math.PI/2;
			
			g.getChildren().addAll(polje);
		}	
		return g;
	}
	
	private Group napraviKucicu(int k, int v, int Max){
		Color c = Color.WHITE;
		switch(k){
		case 1: c = Color.ORANGERED; break;
		case 2: c = Color.PALEGREEN; break;
		case 3: c = Color.LIGHTBLUE; break;
		case 4: c = Color.LIGHTYELLOW; break;
		}
		
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(c);
		
		Cylinder postolje = new Cylinder(60, 5, 100);
		postolje.setMaterial(mat);
		postolje.setTranslateY(5);
		
		if (k <= Max) napraviSveFigure(k, v);
		Group polja = napraviPoljaKucica();
		
		Group Kucica = new Group();
		Kucica.getChildren().addAll(postolje, polja);
		if (k <= Max) Kucica.getChildren().addAll(SveFigure[v][0], SveFigure[v][1], SveFigure[v][2], SveFigure[v][3]);
		
		return Kucica;
	}
	
	private void napraviGrupuKucica(int k, int[] boje){
		double u = Math.PI/4; 
		for (int i = 0; i < 4; i++){
			Group Kucica = napraviKucicu(boje[i], i, k);
			Kucica.setTranslateY(-22.5);
			Kucica.setTranslateX(Math.sin(u)*325);
			Kucica.setTranslateZ(Math.cos(u)*325);
			
			Kucice[i] = Kucica;
			
			getChildren().addAll(Kucica);
			
			u += Math.PI/2;
		}
	}
	
	public void BlinkajFiguru(int Player, int Figure){
		Color c1 = Color.CRIMSON;
		Color c2 = new Color(0.0, 0.6, 0.6, 1.0);
		
		switch(Player){
		case 1: 
			c1 = Color.GREEN;
			c2 = Color.PURPLE;
			break;
		case 2: 
			c1 = Color.CORNFLOWERBLUE;
			c2 = new Color(0.93, 0.74, 0.39, 1.0);
			break;
		case 3: 
			c1 = Color.YELLOW;
			c2 = Color.BLUE;
			break;
		}
		
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(c1);
		
		SveFigure[Player][Figure].setMat(mat);
		
		KeyValue Poc = new KeyValue(mat.diffuseColorProperty(), c1);
		KeyValue Kraj = new KeyValue(mat.diffuseColorProperty(), c2);
		KeyFrame prvi = new KeyFrame(Duration.millis(0), Poc);
		KeyFrame drugi = new KeyFrame(Duration.millis(500), Kraj);
		
		Blinkeri[BlinkInd] = new Timeline(prvi, drugi);
		Blinkeri[BlinkInd].setCycleCount(Timeline.INDEFINITE);
		Blinkeri[BlinkInd].setAutoReverse(true);
		Blinkeri[BlinkInd].play();
		BlinkeriID[BlinkInd][0] = 1;
		BlinkeriID[BlinkInd][1] = Player;
		BlinkeriID[BlinkInd][2] = Figure;
		BlinkInd++;
	}
	
	public void BlinkajPolje(int polje){
		Color c1 = Color.IVORY;
		Color c2 = new Color(0.72, 0.53, 0.04, 1.0);
		
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(c1);
		
		Polja[polje].setMaterial(mat);
		
		KeyValue Poc = new KeyValue(mat.diffuseColorProperty(), c1);
		KeyValue Kraj = new KeyValue(mat.diffuseColorProperty(), c2);
		KeyFrame prvi = new KeyFrame(Duration.millis(0), Poc);
		KeyFrame drugi = new KeyFrame(Duration.millis(500), Kraj);
		
		Blinkeri[BlinkInd] = new Timeline(prvi, drugi);
		Blinkeri[BlinkInd].setCycleCount(Timeline.INDEFINITE);
		Blinkeri[BlinkInd].setAutoReverse(true);
		Blinkeri[BlinkInd].play();
		BlinkeriID[BlinkInd][0] = 0;
		BlinkeriID[BlinkInd][1] = polje;
		BlinkInd++;
	}
	
	public void PrekiniBlinkanja(){
		for (int i = 0; i < BlinkInd; i++){
			Blinkeri[i].stop();
			if (BlinkeriID[i][0] == 0){
				Color c = Color.IVORY;				
				int index = BlinkeriID[i][1];
				if ((index == 1) || (index == 11) || (index == 21) || (index == 31)){
					index = index/10;
					switch(index){
					case 0: c = Color.CRIMSON; break;
					case 1: c = Color.GREEN; break;
					case 2: c = Color.CORNFLOWERBLUE; break;
					case 3: c = Color.YELLOW; break;
					}
				}
				if (index > 39){
					if (index < 44) c = Color.CRIMSON;
					else if (index < 48) c = Color.GREEN;
					else if (index < 52) c = Color.CORNFLOWERBLUE;
					else c = Color.YELLOW;
				}
				PhongMaterial mat = new PhongMaterial();
				mat.setDiffuseColor(c);
				Polja[BlinkeriID[i][1]].setMaterial(mat);
			} else {
				PhongMaterial mat = new PhongMaterial();
				Color c = Color.WHITE;
				switch(BlinkeriID[i][1]){
				case 0: c = Color.CRIMSON; break;
				case 1: c = Color.GREEN; break;
				case 2: c = Color.CORNFLOWERBLUE; break;
				case 3: c = Color.YELLOW; break;
				}
				mat.setDiffuseColor(c);
				
				SveFigure[BlinkeriID[i][1]][BlinkeriID[i][2]].setMat(mat);
			}
		}
			
		BlinkInd = 0;
	}
	
	private void dodajKockicu(){
		KockicaT = new Text();
		KockicaT.setText("");
//		KockicaT.setTranslateZ(-45);
		KockicaT.setTranslateY(15);
		KockicaT.setTranslateX(-27);
		KockicaT.setFont(new Font("Century Gothic", 100));
		KockicaT.setRotationAxis(Rotate.X_AXIS);
		KockicaT.setRotate(-90);
		KockicaT.setStroke(Color.BLACK);
		KockicaT.setStrokeWidth(3.0);
		KockicaT.setFill(Color.IVORY);
		getChildren().addAll(KockicaT);
	}
	
	public FullBoard(int k, int[] boje, int[] Types, Covece Game){
		this.Game = Game;
		NumOfPlayers = k;
		for (int i = 0; i < NumOfPlayers; i++){
			FigureType[i] = Types[i];
		}
		napraviTablu();
		napraviGrupuKucica(k, boje);
		napraviPolja(boje);
		dodajKockicu();
		
		for (int i = 0; i < 56; i++){
			for (int j = 0; j < 4; j++){
				Zauzeto[i][j] = -1;
			}
		}
	}
	
	private boolean MoveCheckP(int Player, int Figure, int mov){
		boolean T = true;
		int index = Player*10 + 1;
		if (SveFigure[Player][Figure].At() != -1) {
			index = SveFigure[Player][Figure].At() + mov;
			
			if ((SveFigure[Player][Figure].At() <= 10*Player) && (SveFigure[1][Figure].At() + mov > 10*Player)){
				if (SveFigure[Player][Figure].At() + mov > 10*Player+3) T = false;
				else if (Zauzeto[index][Player] != -1) T = false;
			} else {
					if (index < 40)
						for (int i = 0; i < 4; i++){
							if (Zauzeto[index][i] != -1){
								if (i == Player) T = false;
							} 
						}
					else {
						if ((index > 40 + Player*3 + 1) || (Zauzeto[index][Player] != -1)) T = false;
					}
			}
			for (int i = SveFigure[Player][Figure].At()+1; i<= index; i++)
				if (Zauzeto[i][Player] != -1) T = false;
		}
		if (SveFigure[Player][Figure].At() == -1) {
			if (mov != 6) T = false;
			else if (Zauzeto[10*Player+1][Player] != -1) T = false;
		}
		return T;
	}
	
	public boolean MoveExists(int Player, int Figure, int mov){
		boolean T = true;
//		switch(Player){
//		case 0: T = MoveCheckP1(Figure, mov); break;
//		case 1: T = MoveCheckP2(Figure, mov); break;
//		case 2: T = MoveCheckP3(Figure, mov); break;
//		case 3: T = MoveCheckP4(Figure, mov); break;
//		}
//		if (SveFigure[Player][Figure].InHouse) T = false;
		T = MoveCheckP(Player, Figure, mov);
		return T;
	}
	
	public boolean FirstMove(int Player, int Figure, int mov){
		boolean T = false;
		if (SveFigure[Player][Figure].At() == -1) {
			if (mov != 6) T = false;
			else T = true;
		}
		return T;
	}
	
	public void PlaceFigure(int Player, int Figure){	
		Game.GH.DiceTurn = true;
		Game.GH.BlinkajKockicu();
		
		for (int i = 0; i < 4; i++){
			if (Zauzeto[Player*10+1][i] != -1){
				System.out.println("ZAUZETO!!!");
				if (i != Player) {
					System.out.println("POJEDI!!!");
					PojediFiguru(i, Zauzeto[Player*10+1][i]);
					Zauzeto[Player*10+1][i] = -1;
				}
			}
		}
		
		Zauzeto[Player*10+1][Player] = Figure;
		SveFigure[Player][Figure].SetMov(Player*10+1);
		System.out.println(SveFigure[Player][Figure].At());
		
		for (int i = 0; i < 4; i++){
			SveFigure[Player][i].NextStepExists = false;
		}
		
		double u = Math.PI/20 + Player*10*Math.PI/20;
		double f = Math.PI/4 + Player*Math.PI/2;
		
		int sgnSn = 1, sgnCs = 1;
		if (Math.sin(u) < 0) sgnSn = -1;
		if (Math.cos(u) < 0) sgnCs = -1;
		
		SveFigure[Player][Figure].setTranslateY(0);
		SveFigure[Player][Figure].setTranslateX(-Math.sin(f)*325 + Math.sin(u)*Math.sin(u)*Math.sin(u)*Math.sin(u)*Math.sin(u)*230 + sgnSn*50);
		SveFigure[Player][Figure].setTranslateZ(-Math.cos(f)*325 + Math.cos(u)*Math.cos(u)*Math.cos(u)*Math.cos(u)*Math.cos(u)*230 + sgnCs*50);
	}
	
	private void MoveIt(int Polje, int Pom, int Player, int Figure, boolean L, int HousePom){
		for (int i = 0; i < 4; i++){
			SveFigure[Player][i].NextStepExists = false;
		}
		
		int PrPolje = Polje - Pom - HousePom - 1;
		if (PrPolje < 0) PrPolje += 40;
		double u = Math.PI/20 + PrPolje*Math.PI/20;
		double f = Math.PI/20 + PrPolje*Math.PI/20;
		double t = Math.PI/4 + Player*Math.PI/2;
		SveFigure[Player][Figure].setTranslateY(-2.5);
		Timeline T = new Timeline();
		
		KeyValue PrvoY = new KeyValue(SveFigure[Player][Figure].translateYProperty(), -2.5);
		KeyValue PoslY = new KeyValue(SveFigure[Player][Figure].translateYProperty(), -50);
		
		KeyFrame PrvoYKF = new KeyFrame(Duration.millis(0), PrvoY);
		KeyFrame SrednjeYKF = new KeyFrame(Duration.millis(500), PoslY);
		KeyFrame PoslYKF = new KeyFrame(Duration.millis(1000), PrvoY);
		Timeline Y = new Timeline(PrvoYKF, SrednjeYKF, PoslYKF);
		Y.setCycleCount(Pom + HousePom);
		
		for (int i = 0; i < Pom; i++){
			
			int index = Polje - Pom - HousePom + i;
			
			if (index < 0) index += 40;
			
			u += Math.PI/20;
			
			int sgnSn = 1, sgnCs = 1;
			if (Math.sin(u) < 0) sgnSn = -1;
			if (Math.cos(u) < 0) sgnCs = -1;
			
			int sgnSnP = 1, sgnCsP = 1;
			if (Math.sin(f) < 0) sgnSnP = -1;
			if (Math.cos(f) < 0) sgnCsP = -1;
			
			double PrevXMov = 0;
			double PrevZMov = 0;
			double PrevX = 0;
			double PrevZ = 0;
			double NextX = 0;
			double NextZ = 0;
			if ((index != 10) && (index != 20) && (index != 30) && (index != 0)){
				PrevXMov = Math.sin(f)*Math.sin(f)*Math.sin(f)*Math.sin(f)*Math.sin(f)*230 + sgnSnP*50;
				PrevZMov = Math.cos(f)*Math.cos(f)*Math.cos(f)*Math.cos(f)*Math.cos(f)*230 + sgnCsP*50;
			} else {
				PrevXMov = Math.sin(f)*280;
				PrevZMov = Math.cos(f)*280;
			}
			
			PrevX = -Math.sin(t)*325 + PrevXMov;
			PrevZ = -Math.cos(t)*325 + PrevZMov;
			if ((index != 9) && (index != 19) && (index != 29) && (index != 39)){
				NextX = (SveFigure[Player][Figure].getTranslateX() -PrevXMov + Math.sin(u)*Math.sin(u)*Math.sin(u)*Math.sin(u)*Math.sin(u)*230 + sgnSn*50);
				NextZ = (SveFigure[Player][Figure].getTranslateZ() -PrevZMov + Math.cos(u)*Math.cos(u)*Math.cos(u)*Math.cos(u)*Math.cos(u)*230 + sgnCs*50);	
			} else {
				NextX = SveFigure[Player][Figure].getTranslateX() - PrevXMov + Math.sin(u)*280;
				NextZ = SveFigure[Player][Figure].getTranslateZ() - PrevZMov + Math.cos(u)*280;
			}
			
			
			KeyValue PrvoX = new KeyValue(SveFigure[Player][Figure].translateXProperty(), PrevX);
			KeyValue PrvoZ = new KeyValue(SveFigure[Player][Figure].translateZProperty(), PrevZ);
			KeyValue PoslX = new KeyValue(SveFigure[Player][Figure].translateXProperty(), NextX);
			KeyValue PoslZ = new KeyValue(SveFigure[Player][Figure].translateZProperty(), NextZ);
			KeyFrame Prvi = new KeyFrame(Duration.millis(1000*i), PrvoX, PrvoZ);
			KeyFrame Posl = new KeyFrame(Duration.millis(1000*(i+1)), PoslX, PoslZ);
			T.getKeyFrames().addAll(Prvi, Posl);
			
			SveFigure[Player][Figure].setTranslateX(NextX);
			SveFigure[Player][Figure].setTranslateZ(NextZ);
		
			f += Math.PI/20;
		}
		
		if (L){
			for (int i = 0; i < HousePom; i++){	
				double k = Player*Math.PI/2 + Math.PI/4;
				SveFigure[Player][Figure].setTranslateX(0);
				SveFigure[Player][Figure].setTranslateX(-Math.sin(k)*325);
				SveFigure[Player][Figure].setTranslateZ(0);
				SveFigure[Player][Figure].setTranslateZ(-Math.cos(k)*325);
				k -= Math.PI/4;
				
				double OrigX = SveFigure[Player][Figure].getTranslateX() + Math.sin(k)*280;
				double OrigZ = SveFigure[Player][Figure].getTranslateZ() + Math.cos(k)*280;
				
				double PocX = OrigX - Math.sin(k) * (40 * i);
				double PocZ = OrigZ - Math.cos(k) * (40 * i);
				
				double KrajX = OrigX - Math.sin(k) * (40 * (i+1));
				double KrajZ = OrigZ - Math.cos(k) * (40 * (i+1));
				
				KeyValue PocKVX = new KeyValue(SveFigure[Player][Figure].translateXProperty(), PocX);
				KeyValue KrajKVX = new KeyValue(SveFigure[Player][Figure].translateXProperty(), KrajX);
				KeyValue PocKVZ = new KeyValue(SveFigure[Player][Figure].translateZProperty(), PocZ);
				KeyValue KrajKVZ = new KeyValue(SveFigure[Player][Figure].translateZProperty(), KrajZ);
				
				KeyFrame Frame1 = new KeyFrame(Duration.millis(1000*i + 1000*Pom), PocKVX, PocKVZ);
				KeyFrame Frame2 = new KeyFrame(Duration.millis(1000*(i+1) + 1000*Pom), KrajKVX, KrajKVZ);
			
				T.getKeyFrames().addAll(Frame1, Frame2);
			}
		}
		
		T.play();
		Y.play();
		
		EventHandler<ActionEvent> ev1 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ev){
				Game.GH.DiceTurn = true;
				Game.GH.BlinkajKockicu();
				PojediFiguru(PlayerEat, FigureEat);
			}
		};
		
		EventHandler<ActionEvent> ev2 = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ev){
				Game.GH.DiceTurn = true;
				Game.GH.BlinkajKockicu();
			}
		};
		boolean M = false;
		for (int i = 0; i < 4; i++){
			if (Polje < 0) Polje += 40;
			if (Zauzeto[Polje][i] != -1){
				PlayerEat = i;
				for (int j = 0; j < 4; j++){
					if ((SveFigure[i][j].At() == Polje) && (i != Player)){
						FigureEat = j;
						T.setOnFinished(ev1);
						M = true;
					}	
				} 
			}
		}
		if (!M) T.setOnFinished(ev2);
	}
	
	public void MoveFigure(int Player, int Figure, int NumOfMoves){
		if (NumOfMoves == 0){
			SveFigure[Player][Figure].SetMov(Player*10+1);
			for (int i = 0; i < 4; i++){
				if (Zauzeto[Player*10+1][i] != -1){
					System.out.println("ZAUZETO!!!");
					if (i != Player) {
						System.out.println("POJEDI!!!");
						PojediFiguru(i, Zauzeto[Player*10+1][i]);
						Zauzeto[Player*10+1][i] = -1;
					}
				}
			}
			Zauzeto[Player*10+1][Player] = Figure;
			PlaceFigure(Player, Figure);
		} else {
			if ((Player == 0) && (SveFigure[Player][Figure].At() + NumOfMoves > 40)){
				int R = 0;
				R = SveFigure[Player][Figure].At() - 40;
				SveFigure[Player][Figure].InHouse = true;
				MoveIt(SveFigure[Player][Figure].At()+NumOfMoves, NumOfMoves - R, Player, Figure, true, R);
			} else {
				SveFigure[Player][Figure].MoveFig(NumOfMoves);
				int k = 0;
				if (SveFigure[Player][Figure].At() - NumOfMoves < 0) k = 40;
				Zauzeto[SveFigure[Player][Figure].At()][Player] = Figure;
				
				if ((SveFigure[Player][Figure].At() - NumOfMoves + k <= Player*10) && (SveFigure[Player][Figure].At() > Player*10)){
					int R = 0;
					R = SveFigure[Player][Figure].At() - Player*10;
					SveFigure[Player][Figure].InHouse = true;
					MoveIt(SveFigure[Player][Figure].At(), NumOfMoves - R, Player, Figure, true, R);
				} else {
					Zauzeto[SveFigure[Player][Figure].At() - NumOfMoves + k][Player] = -1;
					MoveIt(SveFigure[Player][Figure].At(), NumOfMoves, Player, Figure, false, 0);
				}	
			}	
		}
	}
	
	public void PojediFiguru(int Player, int Figure){
		SveFigure[Player][Figure].SetMov(-1);
		KeyValue PocX = new KeyValue(SveFigure[Player][Figure].translateXProperty(), SveFigure[Player][Figure].getTranslateX());
		KeyValue PocZ = new KeyValue(SveFigure[Player][Figure].translateZProperty(), SveFigure[Player][Figure].getTranslateZ());
		KeyValue PocY = new KeyValue(SveFigure[Player][Figure].translateYProperty(), SveFigure[Player][Figure].getTranslateY());
		SveFigure[Player][Figure].setRotationAxis(Rotate.Z_AXIS);
		KeyValue PocZR = new KeyValue(SveFigure[Player][Figure].rotateProperty(), 0);
		
		double u = Math.PI/4 + Player*Math.PI/2;
		double p = Math.PI/4 + Figure*Math.PI/2;
		double NextX = Math.sin(p)*40;
		double NextZ = Math.cos(p)*40;
		
		KeyValue KrajX = new KeyValue(SveFigure[Player][Figure].translateXProperty(), NextX);
		KeyValue SledY = new KeyValue(SveFigure[Player][Figure].translateZProperty(), -100);
		KeyValue KrajZ = new KeyValue(SveFigure[Player][Figure].translateZProperty(), NextZ);
		KeyValue KrajZR = new KeyValue(SveFigure[Player][Figure].rotateProperty(), 720);
		KeyValue KrajY = new KeyValue(SveFigure[Player][Figure].translateYProperty(), -5);
		
		KeyFrame Prvi = new KeyFrame(Duration.millis(0), PocX, PocZ, PocY, PocZR);
		KeyFrame Drugi = new KeyFrame(Duration.millis(1000), SledY);
		KeyFrame Posl = new KeyFrame(Duration.millis(2000), KrajX, KrajZ, KrajY, KrajZR);
		
		Timeline T = new Timeline(Prvi, Drugi, Posl);
		T.play();
	}
	
}
