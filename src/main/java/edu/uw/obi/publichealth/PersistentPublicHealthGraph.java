/**
 * 
 */
package edu.uw.obi.publichealth;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;


public class PersistentPublicHealthGraph extends OrientGraph
{
	public PersistentPublicHealthGraph(String graphPath)
	{
		super(graphPath);
                init();
	}
	
	private void init()
	{
                // to do;
	}
	 
}
