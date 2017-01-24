package uk.ac.kent.dover.fastGraph;

import java.util.HashSet;
import java.util.Random;

/**
 * Class to perform the inexact subgraph isomorphism
 * 
 * @author Rob Baker
 *
 */
public class InexactSubgraphIsomorphism {

	private FastGraph target, pattern;

	/**
	 * Trivial constructor
	 * 
	 * @param target The target graph
	 * @param pattern The pattern graph
	 */
	public InexactSubgraphIsomorphism(FastGraph target, FastGraph pattern) {
		this.target = target;
		this.pattern = pattern;
	}
	
	/**
	 * Performs the inexact subgraph isomorphism
	 * 
	 */
	public void subgraphIsomorphismFinder() {
		Random r = new Random(target.getNodeBuf().getLong(0));
		for (int i = 0; i < target.getNumberOfNodes(); i++) {
			
			//generate set of subgraphs
			EnumerateSubgraphNeighbourhood esn = new EnumerateSubgraphNeighbourhood(target);
			HashSet<FastGraph> subs = new HashSet<FastGraph>();
			esn.enumerateSubgraphsFromNode(pattern.getNumberOfNodes(), 1, 100, i, r, subs);
			
			//for each generated subgraph
			for(FastGraph sub : subs) {
				
				ExactSubgraphIsomorphism esi = new ExactSubgraphIsomorphism(pattern,sub,null,null);
				boolean result = esi.subgraphIsomorphismFinder();
				
				//now what?
				
			}
		}

	}
	
	
}
