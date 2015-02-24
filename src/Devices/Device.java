package Devices;

public class Device {

	String name;
	int id;
	String altid;
	int category;
	int subcategory;
	int room;
	int parent;
	String image = "";

    @Override
    public String toString(){
    	return "Name: " + name + " Id: " + id + " AltID: " + altid + " Category: " + category + " Subcategory: " + subcategory + " Room: " + room + " Parent: " + parent;
    }
    
    public String getDetails(){
    	return "Name: " + name + 
    			"\nId: " + id + 
    			"\nAltID: " + altid + 
    			"\nCategory: " + category + 
    			"\nSubcategory: " + subcategory + 
    			"\nRoom: " + room + 
    			"\nParent: " + parent;
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
	public void setImage(String image){
		this.image = image;
	}


}
