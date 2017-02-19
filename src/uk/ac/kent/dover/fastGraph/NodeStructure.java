package uk.ac.kent.dover.fastGraph;

/**
  * A class for storing information about a node when modifying a fast graph.
  * @author Peter Rodgers
  */

public class NodeStructure {

	protected int id;
	protected String label;
	protected int weight;
	protected byte type;
	protected byte age;

	public NodeStructure(int id, String label, int weight, byte type, byte age) {
		super();
		this.id = id;
		this.label = label;
		this.weight = weight;
		this.type = type;
		this.age = age;
	}
	
	public int getId() {return id;}
	public String getLabel() {return label;}
	public int getWeight() {return weight;}
	public byte getType() {return type;}
	public byte getAge() {return age;}
	
	public void setId(int id) {this.id = id;}
	public void setLabel(String label) {this.label = label;}
	public void setWeight(int weight) {this.weight = weight;}
	public void setType(byte type) {this.type = type;}
	public void setAge(byte age) {this.age = age;}
	
	
	public String toString() {
		String ret = getId()+":"+getLabel()+":"+getWeight()+":"+getType()+":"+getAge();
		return ret;
	}
	
}
