package com.xsy.documentduplicatechecking.examination;

import lombok.Data;

import java.util.List;

/**
 * @author xushiyue
 * @date 2019年7月30日10:38:32
 */
@Data
public class Text {

    private Long id;

    private String text;

    private List<MyWord> words;

    private String hash;

}
