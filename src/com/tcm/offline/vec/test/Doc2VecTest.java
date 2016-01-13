package com.tcm.offline.vec.test;

import com.tcm.offline.vec.Learn;
import com.tcm.offline.vec.LearnDocVec;
import com.tcm.offline.vec.Word2VEC;
import com.tcm.offline.vec.domain.Neuron;
import com.tcm.offline.vec.util.ReadWriteFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by azurexsyl on 2016/1/4.
 */
public class Doc2VecTest {
    public static void main(String[] args) throws IOException {

        File result = new File("./resources/dataset/tcm_qa/classifier2/data/seg_corpus.txt");

        Learn learn = new Learn();

        // 训练词向量

        learn.learnFile(result);

        learn.saveModel(new File("./resources/dataset/tcm_qa/classifier2/model/d2v.mod"));

        Word2VEC w2v = new Word2VEC();

        w2v.loadJavaModel("./resources/dataset/tcm_qa/classifier2/model/d2v.mod");

        // 得到训练完的词向量，训练文本向量

        Map<String, Neuron> word2vec_model = learn.getWord2VecModel();

        LearnDocVec learn_doc = new LearnDocVec(word2vec_model);

        learn_doc.learnFile(result);

        // 文本向量写文件

        Map<Integer, float[]> doc_vec = learn_doc.getDocVector();

        StringBuilder sb = new StringBuilder();

        for (int doc_no : doc_vec.keySet()) {

            StringBuilder doc = new StringBuilder("sent_" + (doc_no + 1) + " *** ");

            float[] vector = doc_vec.get(doc_no);

            for (float e : vector) {

                doc.append(e + " ");
            }
            sb.append(doc.toString().trim() + "\n");

        }
        ReadWriteFile.writeFile("./resources/dataset/tcm_qa/classifier2/vec/d2v.txt",
                sb.toString());

    }
}
