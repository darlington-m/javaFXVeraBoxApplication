import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


public abstract class Device {

	String name;
	int id;
	String altid;
	int category;
	int subcategory;
	int room;
	int parent;
	String image;

    @Override
    public String toString(){
    	return "Name: " + name + " Id: " + id + " AltID: " + altid + " Category: " + category + " Subcategory: " + subcategory + " Room: " + room + " Parent: " + parent;
    }

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public String getAltid() {
		return altid;
	}

	public int getCategory() {
		return category;
	}

	public int getSubcategory() {
		return subcategory;
	}

	public int getRoom() {
		return room;
	}

	public int getParent() {
		return parent;
	}
	public String getImage(){
		return image;
	}
	public abstract int getReading();
	
	public void setImage(String image){
		this.image = image;
	}
	public Pane getPane(){
		Pane pane = new Pane();
		pane.setPrefSize(600,200);
		pane.setStyle("-fx-background-color: #005C99");
		ImageView image = null;
			pane.setStyle("-fx-border-color: red; -fx-background-width: 2px;");
			
			System.out.println ("Resources/" + getImage());
			image = new ImageView(new Image(JsonToJava.class.getResource("Resources/lightbulb1.jpg").toExternalForm()));
			image.setLayoutX(50);
			image.setLayoutY(50);
			
			Text name = new Text(getName());
			name.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
			name.setLayoutX(200);
			name.setLayoutY(50);
		Text reading = new Text("Reading: " + getReading());
		reading.setLayoutX(200);
		reading.setLayoutY(100);
		pane.getChildren().addAll(image,name,reading);
		System.out.println("Pane created");
		return pane;
	}
}
