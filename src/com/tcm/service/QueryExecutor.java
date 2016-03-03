package com.tcm.service;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.tcm.po.Answer;
import com.tcm.po.Question;
import com.tcm.util.Const;

import java.io.InputStream;
import java.util.List;

/**
 * Created by azurexsyl on 2015/12/20.
 */
public class QueryExecutor {

    private static Model model = null;
    private static InputStream in = null;
    private static String dir = Const.RESOURCE_BASE_DIR + "/final_rdfs/new_3180259.rdfs";

    static {
        System.out.println("RDF Initialize");
        model = ModelFactory.createDefaultModel();
        in = FileManager.get().open(dir);
        model.read(in, null);
        System.out.println(model.size());
    }

    public static void execute(Question question) {

        List<Answer> answers = question.getAnswers();
        System.out.println("Answer Count: " + answers.size());
        for(Answer answer : answers) {
            System.out.println(answer.getQuery());
            String query = answer.getQuery();
            QueryExecution qe = QueryExecutionFactory.create(query, model);
            ResultSet results = qe.execSelect();
            while(results.hasNext()) {
                QuerySolution qs = results.next();
                String str = "";
                for (String s : answer.getParam()) {
                    str += qs.get(s) + " XXX ";
                }
                System.out.println(str);
                answer.addOris(str);
            }
        }
        System.out.println("FINISHED!");
    }

    public static void main(String[] args) {
        String query = "SELECT ?x WHERE {  <http://zcy.ckcest.cn/tcm/med#1064>  <http://zcy.ckcest.cn/tcm/relation#treats> ?y. { { ?y <http://zcy.ckcest.cn/tcm/dis/tcm/property#dis_tcm_basic.name_zh> ?x } UNION { ?y <http://zcy.ckcest.cn/tcm/dis/wm/property#dis_wm_basic.name_zh> ?x } } }";
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();
        while(results.hasNext()) {
            QuerySolution qs = results.next();
            System.out.println(qs.get("x"));
        }
    }
}
