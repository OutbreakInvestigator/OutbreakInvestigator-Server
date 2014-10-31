/***** Copyright 2014 University of Washington (Neil Abernethy, Wilson Lau, Todd Detwiler)***/
/***** http://faculty.washington.edu/neila/ ****/
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
