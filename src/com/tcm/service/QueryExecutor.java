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

import java.io.InputStream;
import java.util.List;

/**
 * Created by azurexsyl on 2015/12/20.
 */
public class QueryExecutor {

    private static Model model = null;
    private static InputStream in = null;
    private static String dir = "E:\\IDEA\\jfinal\\resources\\final_rdfs\\new_3180259.rdfs";

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
                answer.addOris(str);
            }
        }
        System.out.println("FINISHED!");
    }
}
