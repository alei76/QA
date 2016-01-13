package com.tcm.offline.vec.test;

import com.tcm.offline.vec.Learn;
import com.tcm.offline.vec.Word2VEC;

import java.io.File;
import java.io.IOException;

/**
 * Created by azurexsyl on 2016/1/4.
 */
public class Word2VecTest {
    public static void main(String[] args) throws IOException {

        File result = new File("file//amazon_docs.txt");

        Learn lean = new Learn();

        lean.learnFile(result);

        lean.saveModel(new File("model//amazon_vector.mod"));

        Word2VEC w2v = new Word2VEC();

        w2v.loadJavaModel("model//amazon_vector.mod");

        float[] vector = w2v.getWordVector("windows");

        for (float d : vector) {

            System.out.println(d);
        }

        System.out.println(w2v.distance("windows"));

        System.out.println(w2v.analogy("microsoft", "windows", "apple"));

    }
}
