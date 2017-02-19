package uk.ac.kent.dover.fastGraph;


/** 
 * A class for storing information about an edge when modifying a fast graph.
 * @author Peter Rodgers
 */
public class EdgeStructure {

	protected int id;
	protected String label;
	protected int weight;
	protected byte type;
	protected byte age;
	protected int node1;
	protected int node2;
	
	public EdgeStructure(int id, String label, int weight, byte type, byte age, int node1, int node2) {
		super();
		this.id = id;
		this.label = label;
		this.weight = weight;
		this.type = type;
		this.age = age;
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public int getId() {return id;}
	public String getLabel() {return label;}
	public int getWeight() {return weight;}
	public byte getType() {return type;}
	public byte getAge() {return age;}
	public int getNode1() {return node1;}
	public int getNode2() {return node2;}
	
	public void setId(int id) {this.id = id;}
	public void setLabel(String label) {this.label = label;}
	public void setWeight(int weight) {this.weight = weight;}
	public void setType(byte type) {this.type = type;}
	public void setAge(byte age) {this.age = age;}
	public void setNode1(int node1) {this.node1 = node1;}
	public void setNode2(int node2) {this.node2 = node2;}
	
	public String toString() {
		String ret = getId()+":"+getLabel()+":"+getWeight()+":"+getType()+":"+getAge()+":"+getNode1()+","+getNode2();
		return ret;
	}

}
