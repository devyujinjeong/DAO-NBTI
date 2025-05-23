package com.dao.nbti.problem.domain.aggregate;

public enum AnswerTypeEnum {
    MULTIPLE_CHOICE(1, "선다형"),
    SHORT_ANSWER(2, "단답형"),
    MULTIPLE_RESPONSE(3, "다답형");

    private final int id;
    private final String description;

    AnswerTypeEnum(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    // id로 enum 찾는 유틸
    public static String of(int id) {
        for (AnswerTypeEnum at : values()) {
            if (at.id == id) {
                return at.description;
            }
        }
        return "알 수 없음";
    }
}
