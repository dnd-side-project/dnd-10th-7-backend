package com.sendback.domain.user.entity;

import lombok.Getter;

@Getter
public enum Level {
    ONE("주먹밥", 0L),
    TWO("솜주먹", 5L),
    THREE("물주먹", 10L),
    FOUR("돌주먹", 15L),
    FIVE("불주먹", 20L);
    private final String name;
    private final Long feedbackSubmitCount;

    Level(String name, Long feedbackSubmitCount){
        this.name = name;
        this.feedbackSubmitCount = feedbackSubmitCount;
    }

    public static int toNumber(Level level) {
        return switch (level) {
            case ONE -> 1;
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
        };
    }

    public static Level getLevelByFeedbackSubmitCount(Long feedbackSubmitCount) {
        if (TWO.feedbackSubmitCount > feedbackSubmitCount) {
            return ONE;
        }
        else if (THREE.feedbackSubmitCount > feedbackSubmitCount) {
            return TWO;
        }
        else if (FOUR.feedbackSubmitCount > feedbackSubmitCount) {
            return THREE;
        }
        else if (FIVE.feedbackSubmitCount > feedbackSubmitCount) {
            return FOUR;
        }
        else {
            return FIVE;
        }
    }

    public static Long getRemainCountUntilNextLevel(Long feedbackSubmitCount) {
        if (TWO.feedbackSubmitCount > feedbackSubmitCount) {
            return TWO.feedbackSubmitCount - feedbackSubmitCount;
        }
        else if (THREE.feedbackSubmitCount > feedbackSubmitCount) {
            return THREE.feedbackSubmitCount - feedbackSubmitCount;
        }
        else if (FOUR.feedbackSubmitCount > feedbackSubmitCount) {
            return FOUR.feedbackSubmitCount - feedbackSubmitCount;
        }
        else {
            return FIVE.feedbackSubmitCount - feedbackSubmitCount;
        }
    }
}