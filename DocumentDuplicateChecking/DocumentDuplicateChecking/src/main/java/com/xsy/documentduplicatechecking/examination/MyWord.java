package com.xsy.documentduplicatechecking.examination;

import lombok.Data;

import java.util.Objects;

/**
 * @author xushiyue
 * @date 2019年7月30日10:38:00
 */
@Data
public class MyWord {

    private String word;

    private double tf;

    private double count;

    private double idf;

    private double tfIdf;

    private String hash;

    private double[] score = new double[32];

    public MyWord() {
    }

    public MyWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof MyWord) {
            MyWord otherObj = (MyWord) obj;
            if (otherObj.word.equals(word)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }
}
