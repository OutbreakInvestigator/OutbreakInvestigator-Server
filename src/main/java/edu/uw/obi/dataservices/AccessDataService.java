/**
 * *** Copyright 2014 University of Washington (Neil Abernethy, Wilson Lau,
 * Todd Detwiler)**
 */
/**
 * *** http://faculty.washington.edu/neila/ ***
 */
/**
 *
 */
package edu.uw.obi.dataservices;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import edu.uw.obi.dataModel.Case;
import edu.uw.obi.dataModel.CaseAttribute;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.naming.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Sets the path to base URL + /gds
@Path("/ads")
public class AccessDataService {

    private DataSource ds;
    private HashMap<String, Case> cases;
    private HashMap<String, List> clusters;
//    private static OrientGraph graph;
    private Connection conn = null;
    private String graphJson;
    final Logger logger = LoggerFactory.getLogger(AccessDataService.class);

    public AccessDataService() {
        this.cases = new HashMap();
        this.clusters = new HashMap();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getGraph() {

        return graphJson;
    }

    private JsonArrayBuilder getEdges(String query) {
        JsonArrayBuilder jAbuilder = Json.createArrayBuilder();

        // dbid:dbid as _id, _inV, outV, 'edge' as _type, 'cluster_link' as _label  
        for (List<String> contacts : clusters.values()) {
            for (String inC : contacts) {
                for (String outC : contacts) {
                    if (!inC.equals(outC)) {

                        jAbuilder.add(Json.createObjectBuilder()
                                .add("_id", inC + ":" + outC)
                                .add("_inV", inC)
                                .add("_outV", outC)
                                .add("_type", "edge")
                                .add("_label", "cluster_link"));
                    }
                }
            }
        }
//            try {
//
//                Statement st = conn.createStatement();
//                ResultSet rs = st.executeQuery(query);
////            ResultSet rs = st.executeQuery("SELECT c1.dbid&\":\"&c2.dbid as _id, "
////                    + " c1.dbid  as _inV, c2.dbid as _outV , 'edge' as _type, 'cluster_link' as _label  "
////                    + "from casedata1 as c1  inner join casedata1 as c2 "
////                    + "on c1.clusterID = c2.clusterID and c1.dbid <> c2.dbid"); // ("SELECT * FROM  CaseData1");
//                ResultSetMetaData meta = rs.getMetaData();
//                JsonObjectBuilder jbuilder;
//
//                while (rs.next()) {
//
//                    jbuilder = Json.createObjectBuilder();
//                    for (int idx = 1; idx <= meta.getColumnCount(); idx++) {
//                        if (rs.getString(idx) == null) {
//                            jbuilder.add(meta.getColumnLabel(idx).replaceAll("Z_", "_"), "");
//                        } else if (meta.getColumnType(idx) == java.sql.Types.DOUBLE) {
//                            jbuilder.add(meta.getColumnLabel(idx).replaceAll("Z_", "_"), rs.getDouble(idx));
//                        } else {
//                            jbuilder.add(meta.getColumnLabel(idx).replaceAll("Z_", "_"), rs.getString(idx));
//                        }
//                    }
//                    jAbuilder.add(jbuilder);
//                }
//            } catch (Exception ex) {
//                logger.getLogger(AccessDataService.class.getName()).log(Level.SEVERE, null, ex);
//            }
        return jAbuilder;
    }

    private JsonArrayBuilder getVertices(String query) {
        JsonArrayBuilder jAbuilder = Json.createArrayBuilder();

        try {
            Statement st = conn.createStatement();
            //rs = st.executeQuery("SELECT Case.*,Address.Lat,Address.StreetAddress,Address.Lng FROM Case,Address where Case.DbID=Address.Case_DbID");
//            ResultSet rs = st.executeQuery("SELECT REPORT_DT, "
//                    + " CLUSTERID,  LNG,  DBID,  AGEMONTHS,  SEX, "
//                    + " STREETADDRESS,  AGEYEARS,  HEALTHCARE_FACILITY,"
//                    + " LAT, dbid as _id,  \"vertex\" as _type from CaseData1 where REPORT_DT >= #2009-09-01# AND REPORT_DT <= #2015-12-31# "); // ("SELECT * FROM  CaseData1");
            logger.debug(query);
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                Case c = new Case();
//                ArrayList<CaseAttribute> attrs = new ArrayList();
                for (int idx = 1; idx <= meta.getColumnCount(); idx++) {
                    String columnLabel = meta.getColumnLabel(idx);
                    c.add(columnLabel, new CaseAttribute(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_") : columnLabel,
                            rs.getString(idx) == null ? "" : rs.getString(idx), meta.getColumnType(idx)));
//                    attrs.add(new CaseAttribute(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_").toLowerCase() : columnLabel,
//                            rs.getString(idx) == null ? "" : rs.getString(idx), meta.getColumnType(idx)));
                }

                cases.put(rs.getString("dbid"), c);

                // populate clusters of contacts
                String clusterNm = ((CaseAttribute) c.get("CLUSTER_NM")).getValue();
                String dbid = ((CaseAttribute) c.get("DBID")).getValue();
                if (!clusterNm.equals("")) {
                    if (clusters.containsKey(clusterNm)) {
                        ((List) (clusters.get(clusterNm))).add(dbid);
                    } else {
                        List l = new ArrayList();
                        l.add(dbid);
                        clusters.put(clusterNm, l);
                    }
                }

//                JsonObjectBuilder jbuilder = Json.createObjectBuilder();
//                for (int idx = 1; idx <= meta.getColumnCount(); idx++) {
//                    String columnLabel = meta.getColumnLabel(idx);
//                    if (rs.getString(idx) == null) {
//                        jbuilder.add(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_").toLowerCase() : columnLabel, "");
//                    } else if (meta.getColumnType(idx) == java.sql.Types.DOUBLE) {
//                        jbuilder.add(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_").toLowerCase() : columnLabel, rs.getDouble(idx));
//                    } else {
//                        jbuilder.add(columnLabel.startsWith("Z_") ? columnLabel.replaceAll("Z_", "_").toLowerCase() : columnLabel, rs.getString(idx));
//                    }
//                }
//                jAbuilder.add(jbuilder);
            }
            for (Case c : cases.values()) {
                jAbuilder.add(c.build());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return jAbuilder;
    }
// This method is called if application/json is request
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)

//    public String getGraph() {
//        String results = "";
//
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        try {
//            GraphSONWriter.outputGraph(graph, os);
//            results = new String(os.toByteArray(), "utf8");
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return results;
//        }
//
//        return results;
//    }
    @GET
    @Path("/vquery")
    @Produces(MediaType.APPLICATION_JSON)
    public String graphFromVQuery(@QueryParam("query") String query) {
        try {

            Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/myaccess");
            conn = ds.getConnection();

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        JsonObjectBuilder jbuilder = Json.createObjectBuilder().add("mode", "NORMAL")
                .add("vertices", this.getVertices(query)).add("edges", this.getEdges(query));
        graphJson = jbuilder.build().toString();
        return graphJson;
    }

    @GET
    @Path("/equery")
    @Produces(MediaType.APPLICATION_JSON)
    public String graphFromEQuery(@QueryParam("query") String query) {

        return getGraph();
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
    public static void main(String[] args) throws SQLException {
        AccessDataService service = new AccessDataService();
        //String vquery = "select from Case where REPORT_DT >= '2009-09-01' AND REPORT_DT <= '2009-12-31'";
//        String vquery = "select from Case where REPORT_DT.format('yyyy-MM-dd') >= '2009-09-01' AND REPORT_DT.format('yyyy-MM-dd') <= '2015-12-31'";
        String vquery = "SELECT Case.*,Address.Lat,Address.StreetAddress,Address.Lng FROM Case,Address where Case.DbID=Address.Case_DbID and REPORT_DT >= #2009-09-01# AND REPORT_DT <= #2015-12-31#";
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

        System.err.println(service.getGraph());
    }
}
