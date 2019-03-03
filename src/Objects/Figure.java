package Objects;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.event.EventHandler;

public class Figure extends Group {

	private int FieldNum = -1;
	private Cylinder postolje, telo, vrat, C1, C2, C3, C4;
	private Sphere glava;
	private Box G1, G2;
	public int Player = 0, Figure = 0;
	private FullBoard FB;
	public int Move = 0;
	public boolean NextStepExists = false;
	private int type = 4;
	public int Col = 0;
	public boolean InHouse = false;
	
	public void SetMov(int Value){
		FieldNum = Value;
	}
	public void MoveFig(int Value){
		FieldNum += Value;
		if (FieldNum > 40 && !InHouse) FieldNum = FieldNum - 40;
	}
	
	public int At(){
		return FieldNum;
	}
	
	public void setMat(PhongMaterial mat){
		postolje.setMaterial(mat);
		telo.setMaterial(mat);
		if (type == 1){
			vrat.setMaterial(mat);
			glava.setMaterial(mat);
		} else if (type == 2){
			G1.setMaterial(mat);
			G2.setMaterial(mat);
		} else if (type == 3){
			vrat.setMaterial(mat);
			C1.setMaterial(mat);
			C2.setMaterial(mat);
			C3.setMaterial(mat);
			C4.setMaterial(mat);
		} else {
			vrat.setMaterial(mat);
			C1.setMaterial(mat);
		}
		
	}
	
	private void GenerateTypeOne(){
		PhongMaterial mat = new PhongMaterial();
		Color c = Color.WHITE;
		mat.setDiffuseColor(c);
		postolje = new Cylinder(10, 5, 100);
		postolje.setMaterial(mat);
		postolje.setTranslateY(5);
		
		telo = new Cylinder(7, 15, 100);
		telo.setMaterial(mat);
		telo.setTranslateY(-5);
		
		vrat = new Cylinder(8, 5, 100);
		vrat.setMaterial(mat);
		vrat.setTranslateY(-15);
		
		glava = new Sphere(10, 100);
		glava.setMaterial(mat);
		glava.setTranslateY(-24);
		
		getChildren().addAll(postolje, telo, vrat, glava);
	}
	private void GenerateTypeTwo(){
		PhongMaterial mat = new PhongMaterial();
		Color c = Color.WHITE;
		mat.setDiffuseColor(c);
		postolje = new Cylinder(10, 5, 100);
		postolje.setMaterial(mat);
		postolje.setTranslateY(5);
		
		telo = new Cylinder(5, 25, 100);
		telo.setMaterial(mat);
		telo.setTranslateY(-5);
		
		G1 = new Box(8, 8, 20);
		G1.setMaterial(mat);
		G1.setTranslateY(-20);
		G1.getTransforms().add(new Rotate(45, Rotate.Y_AXIS));
		
		G2 = new Box(8, 8, 20);
		G2.setMaterial(mat);
		G2.setTranslateY(-20);
		G2.getTransforms().add(new Rotate(-45, Rotate.Y_AXIS));
		
		getChildren().addAll(postolje, telo, G1, G2);
	}	
	private void GenerateTypeThree(){
		PhongMaterial mat = new PhongMaterial();
		Color c = Color.WHITE;
		mat.setDiffuseColor(c);
		
		postolje = new Cylinder(10, 5, 100);
		postolje.setMaterial(mat);
		postolje.setTranslateY(5);
		
		telo = new Cylinder(9, 5, 100);
		telo.setMaterial(mat);
		telo.setTranslateY(-5);
		
		vrat = new Cylinder(8, 5, 100);
		vrat.setMaterial(mat);
		vrat.setTranslateY(0);
		
		C1 = new Cylinder(7, 5, 100);
		C1.setMaterial(mat);
		C1.setTranslateY(-10);
		
		C2 = new Cylinder(6, 5, 100);
		C2.setMaterial(mat);
		C2.setTranslateY(-15);
		
		C3 = new Cylinder(5, 5, 100);
		C3.setMaterial(mat);
		C3.setTranslateY(-20);
		
		C4 = new Cylinder(13, 5, 100);
		C4.setMaterial(mat);
		C4.setTranslateY(-25);
		
		getChildren().addAll(postolje, telo, vrat, C1, C2, C3, C4);
	}
	private void GenerateTypeFour(){
		PhongMaterial mat = new PhongMaterial();
		Color c = Color.WHITE;
		mat.setDiffuseColor(c);
		postolje = new Cylinder(10, 5, 100);
		postolje.setMaterial(mat);
		postolje.setTranslateY(5);
		
		telo = new Cylinder(8, 6, 6);
		telo.setMaterial(mat);
		telo.setTranslateY(-1);
		
		vrat = new Cylinder(5, 25, 100);
		vrat.setMaterial(mat);
		vrat.setTranslateY(-5);
		
		C1 = new Cylinder(8, 6, 6);
		C1.setMaterial(mat);
		C1.setTranslateY(-21);
		
		getChildren().addAll(postolje, telo, vrat, C1);
	}
	
	public Figure(int k, int Player, int Figure, FullBoard FB, int type, int C){
		this.Player = Player;
		this.Figure = Figure;
		this.FB = FB;
		this.type = type;

		switch(type){
		case 1: GenerateTypeOne(); break;
		case 2: GenerateTypeTwo(); break;
		case 3: GenerateTypeThree(); break;
		case 4: GenerateTypeFour(); break;
		}
		Color c1 = Color.WHITE;
		
		switch(C){
		case 0: c1 = Color.CRIMSON; break;
		case 1: c1 = Color.GREEN; break;
		case 2:	c1 = Color.CORNFLOWERBLUE;	break;
		case 3: c1 = Color.YELLOW;break;
		}
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(c1);
		
		setMat(mat);
		
		setOnMouseClicked(new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent me) {
		    	if (NextStepExists){
		    		if (FieldNum == -1){
		    			FB.PlaceFigure(Player, Figure);
		    			NextStepExists = false;
		    			FieldNum = Player*10+1;
		    			System.out.println("FN= " + FieldNum);
		    		} else {
		    			FB.MoveFigure(Player, Figure, Move);
		    			NextStepExists = false;
		    			Move = 0;
		    		}
		    	}
		        System.out.println(FieldNum);
		        FB.PrekiniBlinkanja();
		    }
		});
		
//		getChildren().addAll(postolje, telo, vrat, glava);
	}
}
