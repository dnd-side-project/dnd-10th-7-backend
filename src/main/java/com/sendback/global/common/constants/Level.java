package com.sendback.global.common.constants;

import lombok.Getter;

@Getter
public enum Level {
    ONE("ONE"), TWO("TWO"), THREE("THREE"), FOUR("FOUR"), FIVE("FIVE");
    private final String level;

    Level(String level){
        this.level = level;
    }
}
