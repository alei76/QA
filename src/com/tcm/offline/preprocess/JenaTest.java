package com.tcm.offline.preprocess;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import java.io.InputStream;

/**
 * Created by azurexsyl on 2015/12/16.
 */
public class JenaTest {

    private static Model model = null;
    private static InputStream in = null;
    private static String dir = "E:\\IDEA\\jfinal\\resources\\final_rdfs\\new_3180259.rdfs";


    public static void main(String[] args) throws  Exception{
        model = ModelFactory.createDefaultModel();
        in = FileManager.get().open(dir);
        model.read(in, null);
        System.out.println(model.size());
        String queryString = " SELECT ?x  WHERE {?y <http://zcy.ckcest.cn/tcm/pre/property#pre_basic.name> ?x. ?y <http://zcy.ckcest.cn/tcm/relation#treats> ?z .?z  <http://zcy.ckcest.cn/tcm/dis/wm/property#dis_diagnosis.symptom> ?t. FILTER regex(?t, '肥胖') }";
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        System.out.println(results);
        while(results.hasNext()) {
            QuerySolution qs = results.next();
            System.out.println(qs.get("x"));
        }
    }
}
