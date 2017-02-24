package uk.ac.kent.displayGraph;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * Exports the current graph in SVG
 * Useful for saving exmaples for publications etc
 * 
 * @author rb440
 *
 */
public class ExportSVG {
	private Graph graph;
	
	public ExportSVG(Graph graph){
		this.graph = graph;
	}
	
	
	public boolean saveGraph(File file) {

		try {
			BufferedWriter b = new BufferedWriter(new FileWriter(file));

			//System.out.println("Exporting SVG...");
			
			int[] border = graph.findBorder();
			int padding = 40;
			border[0] -= (padding);
			border[1] += (padding)-border[0];
			border[2] -= (padding);
			border[3] += (padding)-border[2];
			int left = border[0];
			int top = border[2];
			
			//int widthAdjust = border[0];
			//int heightAdjust = border[3];
			
		//	System.out.println(Arrays.toString(border));
			
			b.write("<svg xmlns=\"http://www.w3.org/2000/svg\"");
			b.newLine();
			b.write("width=\""+border[1]+"\"");
			b.newLine();
			b.write("height=\""+border[3]+"\"");
			b.write(" >");
			b.newLine();
			
			
			//write the edges
			for(Edge e : graph.getEdges()){
				
				Node n1 = e.getFrom();
				Node n2 = e.getTo();
				
				if (n1 == null || n2 == null) {
					continue;
				}
				
				b.write(getStartElementString("line"));
				
				b.write(" "+getAttributeString("x1",(n1.getX()-left)+""));
				b.write(" "+getAttributeString("y1",(n1.getY()-top)+""));
				
				b.write(" "+getAttributeString("x2",(n2.getX()-left)+""));
				b.write(" "+getAttributeString("y2",(n2.getY()-top)+""));
				
				//This is to improve the colours for paper output
				Color stroke = e.getType().getLineColor();
				if (stroke.getRGB() == Color.yellow.getRGB()){
					stroke = Color.green;
				}
				b.write(" "+getAttributeString("stroke","rgb(" + stroke.getRed() + "," + 
							stroke.getGreen() + "," +
							stroke.getBlue() + ")"
						));
				b.write(" "+getAttributeString("stroke-width","2"));
				
				//b.write(" "+getAttributeString("from",n1.getMatch().toString()));
				//b.write(" "+getAttributeString("to",n2.getMatch().toString()));
				//b.write(" "+getAttributeString("label",e.getLabel()));
				b.write(getEndEmptyElementString());
				b.newLine();
				
				b.write(getStartElementString("text"));
				b.write(" "+getAttributeString("x",Integer.toString((n1.getX()-left+n2.getX()-left)/2)));
				b.write(" "+getAttributeString("y",Integer.toString((n1.getY()-top+n2.getY()-top)/2)));
				b.write(getEndAttributesString());
				b.write(e.getLabel());
				b.write(getEndElementString("text"));
				b.newLine();
				
			}
			
			//write the nodes
			int index = 0;
			for(Node n : graph.getNodes()) {
				n.setMatch(new Integer(index));
	
				b.write(getStartElementString("circle"));

				b.write(" "+getAttributeString("id","n"+n.getMatch().toString()));
				//b.write(" "+getAttributeString("text",n.getLabel()));
				b.write(" "+getAttributeString("cx",Integer.toString(n.getX()-left)));
				b.write(" "+getAttributeString("cy",Integer.toString(n.getY()-top)));
				
				//if (n.type.height)
				int nodeHeight = n.type.height/2;
				if (nodeHeight < 1){
					nodeHeight = 1;
				}
				b.write(" "+getAttributeString("r",""+nodeHeight));
				
				//This is to improve the colours for paper output
				Color stroke = n.getType().getBorderColor();
				if (stroke.getRGB() == Color.yellow.getRGB()){
					stroke = Color.green;
				}
				
				b.write(" "+getAttributeString("stroke","rgb(" + stroke.getRed() + "," + 
							stroke.getGreen() + "," +
							stroke.getBlue() + ")"
						));

				Color fill = n.getType().getFillColor();
				b.write(" "+getAttributeString("fill","rgb(" + fill.getRed() + "," + 
							fill.getGreen() + "," +
							fill.getBlue() + ")"
						));
				b.write(" "+getAttributeString("stroke-width","2"));
				
				b.write(getEndEmptyElementString());
				b.newLine();
				
				b.write(getStartElementString("text"));
				b.write(" "+getAttributeString("x",Integer.toString(n.getX()-left-10)));
				b.write(" "+getAttributeString("y",Integer.toString(n.getY()-top+5)));
				b.write(getEndAttributesString());
				b.write(n.getLabel());
				b.write(getEndElementString("text"));
				b.newLine();
				
				index++;
			}
			
			
			
			b.write("</svg>");

			b.newLine();
			b.close();
		}
		catch(IOException e){
			System.out.println("An IO exception occured when saving in SVG "+file.getName()+". Error: "+e+"\n");
			return false;
		}
		//System.out.println("Done");
		return true;

	}
	
	public static String getAttributeString(String attName,String attValue) {
		return(attName+"='"+attValue+"'");
	}
	
	public static String getStartElementString(String name) {
		return("<"+name);
	}

	public static String getEndElementString(String name) {
		return("</"+name+">");
	}

	public static String getEndEmptyElementString() {
		return("/>");
	}
	
	public static String getEndAttributesString() {
		return(">");
	}

}
