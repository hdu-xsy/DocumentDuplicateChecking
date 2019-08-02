package com.xsy.documentduplicatechecking.examination;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.huaban.analysis.jieba.JiebaSegmenter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xushiyue
 * @date 2019年7月30日10:38:15
 */
public class Simhash {

    private static final HashFunction HASH_FUNCTION = Hashing.murmur3_32();

    private static final JiebaSegmenter SEGMENTER = new JiebaSegmenter();

    private static final MultiValueMap<String, Long> MULTI_VALUE_MAP = new LinkedMultiValueMap<>();

    public static void main(String[] args) {
        Simhash simhash = new Simhash();
        String[] sentences = new String[]{
                "我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果",
                "我今天买了3个苹果,我今天买了3个苹果,我今天买了2个香蕉,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果,我今天买了2个苹果",
                "从前有座山,山里有座庙",
                "山里有座庙,从前有座山",
                "我有一只小毛驴我从来也不骑"
        };
        final List<Text> init = simhash.init(sentences);
        simhash.calTfIdf(init);
        System.out.println(init);
        System.out.println(init.get(2));
        System.out.println(init.get(3));
        init.forEach(text -> {
            Set<Long> sameIds = simhash.putIntoMap(text);
            sameIds.forEach(
                    id -> System.out.println(id)
            );
        });
        System.out.println(simhash.getDiff(init.get(2).getHash(), init.get(3).getHash()));
        simhash.putIntoMap(init.get(0));
        simhash.putIntoMap(init.get(1));
        simhash.putIntoMap(init.get(2));
        simhash.putIntoMap(init.get(3));
    }

    public List<Text> init(String[] texts) {
        List<Text> text = new ArrayList<>();
        for (Long i = 0L; i < texts.length; i++) {
            Text t = new Text();
            t.setId(i);
            t.setText(texts[i.intValue()]);
            text.add(t);
        }
        return text;
    }

    public Text segmenter(Text text) {
        List<MyWord> result = new ArrayList<>();
        List<String> process = SEGMENTER.sentenceProcess(text.getText());
        process.forEach(
                p -> result.add(new MyWord(p))
        );
        text.setWords(result);
        return text;
    }

    public String toHash(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toBinaryString(HASH_FUNCTION.hashString(str, StandardCharsets.UTF_8).asInt()));
        while (sb.length() < 32) {
            sb.insert(0, "0");
        }
        return new String(sb);
    }

    public Text calTf(Text text) {
        long len = text.getWords().size();
        text.getWords().forEach(
                word -> {
                    StringBuilder s = new StringBuilder(text.getText());
                    int position = s.indexOf(word.getWord());
                    int length = word.getWord().length();
                    double count = 0;
                    while (position != -1) {
                        s.replace(position, position + length, "");
                        position = s.indexOf(word.getWord());
                        count++;
                    }
                    word.setCount(count);

                    word.setTf(count / len);
                }
        );
        return text;
    }

    public boolean hasWord(MyWord word, Text text) {
        return text.getWords().contains(word);
    }

    public MyWord calIdf(MyWord word, List<Text> texts) {
        double count = 0.0;
        double len = texts.size();
        for (Text text : texts) {
            count += hasWord(word, text) ? 1 : 0;
        }
        word.setIdf(Math.log(len / count));
        return word;
    }

    public List<Text> calTfIdf(List<Text> texts) {
        //texts = calHash(texts);
        for (Text text : texts) {
            text = calTf(segmenter(text));
        }
        for (Text text : texts) {
            StringBuilder sb = new StringBuilder();
            List<MyWord> words = text.getWords();
            double[] sum = new double[32];
            for (MyWord word : words) {
                word.setHash(toHash(word.getWord()));
                word = calIdf(word, texts);
                double tfIdf = word.getTf() * word.getIdf();
                word.setTfIdf(tfIdf);
                word = calScore(word);
                sum = add(sum, word.getScore());
            }
            for (int i = 0; i < sum.length; i++) {
                sb.append(sum[i] > 0 ? '1' : '0');
            }
            text.setHash(new String(sb));
        }
        return texts;
    }

    public MyWord calScore(MyWord myWord) {
        String hash = myWord.getHash();
        double tfIdf = myWord.getTfIdf();
        for (int i = 0; i < hash.length(); i++) {
            myWord.getScore()[i] = hash.charAt(i) == '0' ? -tfIdf : tfIdf;
        }
        return myWord;
    }

    public double[] add(double[] a, double[] b) {
        int len = 31;
        double[] result = new double[32];
        for (int i = 0; i < len; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }

    public int getDiff(String a, String b) {
        int count = 0;
        for (int i = 0; i < a.length(); i++) {
            count += a.charAt(i) == b.charAt(i) ? 0 : 1;
        }
        return count;
    }

    public Set<Long> putIntoMap(Text text) {
        String key;
        Set<Long> set = new HashSet<>();
        for (int low = 0, high = 8; high < 32; low += 8, high += 8) {
            key = text.getHash().substring(low, high);
            List<Long> ids = MULTI_VALUE_MAP.get(key);
            if (ids != null) {
                set.addAll(ids);
            }
            MULTI_VALUE_MAP.add(key, text.getId());
        }
        return set;
    }


}