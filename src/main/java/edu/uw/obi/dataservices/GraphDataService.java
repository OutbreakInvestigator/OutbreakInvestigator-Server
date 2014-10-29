/**
 *
 */
package edu.uw.obi.dataservices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Sets;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;

import edu.uw.obi.publichealth.PersistentPublicHealthGraph;

 
//Sets the path to base URL + /gds
@Path("/gds")
public class GraphDataService {

    private static OrientGraph graph;

    static {
//		graph = new PersistentPublicHealthGraph("plocal:F:/obi-server/OBIDataServices/graph_db/synth");
        graph = new PersistentPublicHealthGraph("remote:localhost/graph_db/synth");

    }

    // This method is called if application/json is request
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getGraph() {
        String results = "";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            GraphSONWriter.outputGraph(graph, os);
            results = new String(os.toByteArray(), "utf8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return results;
        }

        return results;
    }

    @GET
    @Path("/vquery")
    @Produces(MediaType.APPLICATION_JSON)
    public String graphFromVQuery(@QueryParam("query") String query) {
        Graph resultGraph = new TinkerGraph();
        //Graph resultGraph = new OrientGraph("memory:result");
        if (query != null) {
            Iterable<Vertex> vertexIt = (Iterable<Vertex>) graph.command(new OCommandSQL(query)).execute();
            Set<Vertex> vertices = Sets.newHashSet(vertexIt);
            resultGraph = graphFromVertices(resultGraph, vertices);
        }

        String results = "";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            GraphSONWriter.outputGraph(resultGraph, os);
            results = new String(os.toByteArray(), "utf8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return results;
        }

        return results;
    }

    @GET
    @Path("/equery")
    @Produces(MediaType.APPLICATION_JSON)
    public String graphFromEQuery(@QueryParam("query") String query) {
        TinkerGraph resultGraph = new TinkerGraph();
        if (query != null) {
            for (Edge e : (Iterable<Edge>) 
                    graph.command(new OCommandSQL(query)).execute()) {
                System.out.println(e);
            }
        }

        String results = "";

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            GraphSONWriter.outputGraph(resultGraph, os);
            results = new String(os.toByteArray(), "utf8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return results;
        }

        return results;
    }
 
    private Graph graphFromVertices(Graph resultGraph, Set<Vertex> vertices) {
        Set<Edge> resultEdges = new HashSet<Edge>();

        for (Vertex v : vertices) {
            for (Edge e : v.getEdges(Direction.OUT)) {
                Vertex otherV = e.getVertex(Direction.IN);
                if (!vertices.contains(otherV)) {
                    continue;
                }

                // if we made it here we will keep this edge
                resultEdges.add(e);
            }

            // add vertex to result graph
            Vertex newV = resultGraph.addVertex(v.getId());
            for (String key : v.getPropertyKeys()) {
                newV.setProperty(key, v.getProperty(key));
            }
        }

        // create new graph
        for (Edge edge : resultEdges) {
            Vertex headV = edge.getVertex(Direction.IN);
            Vertex newHeadV = resultGraph.getVertex(headV.getId());
            Vertex tailV = edge.getVertex(Direction.OUT);
            Vertex newTailV = resultGraph.getVertex(tailV.getId());
            resultGraph.addEdge(edge.getId(), newTailV, newHeadV, edge.getLabel());
        }

        return resultGraph;
    }

    private Graph graphFromEdges(Graph resultGraph, Iterable<Edge> edges) {

        return resultGraph;
    }

    /*
     public static String fileToString(String file) {
     String result = null;
     DataInputStream in = null;

     try {
     File f = new File(file);
     byte[] buffer = new byte[(int) f.length()];
     in = new DataInputStream(new FileInputStream(f));
     in.readFully(buffer);
     result = new String(buffer);
     } catch (IOException e) {
     throw new RuntimeException("IO problem in fileToString", e);
     } finally {
     try {
     in.close();
     } catch (IOException e) { 
     }
     }
     return result;
     }
     */
    public static void main(String[] args) {
        GraphDataService service = new GraphDataService();
        //String vquery = "select from Case where REPORT_DT >= '2009-09-01' AND REPORT_DT <= '2009-12-31'";
        String vquery = "select from Case where REPORT_DT.format('yyyy-MM-dd') >= '2009-09-01' AND REPORT_DT.format('yyyy-MM-dd') <= '2009-12-31'";
        /*
         String graph_query = "select from E let $c = (SELECT from Case WHERE REPORT_DT >= " +
         "'2005-01-01' AND REPORT_DT <= '2009-12-31') " +
         " WHERE E.in IN $c AND E.out IN $c";*/

        /*
         String graph_query = "select from Case AS case1, Case AS case2, E where Case1.REPORT_DT >= " +
         "'2009-01-01' AND Case1.REPORT_DT <= '2009-12-31'" +
         " AND Case2.REPORT_DT >= '2009-01-01' AND Case2.REPORT_DT <= " +
         "'2009-12-31' and E.source=case1.DBID and E.target=case2.DBID";
         */
		//select expand( $c ) let $a = ( select from E ), $b = ( select from V ), $c = union( $a, $b )
		/*
         String graph_query = "select $results " +
         "let $cases = (select from Case where REPORT_DT >= '2009-01-01' AND REPORT_DT <= '2009-12-31'), " +
         "$results = (select from E where in IN $cases and out IN $cases)";
         */
        /*
         String edge_query = "SELECT outE() from Case where REPORT_DT >= " +
         "'2009-01-01' AND REPORT_DT <= '2009-12-31'";*/
        String result = service.graphFromVQuery(vquery);
        //String result = service.graphFromEQuery("select from E");
        System.err.println(result);
    }
}
