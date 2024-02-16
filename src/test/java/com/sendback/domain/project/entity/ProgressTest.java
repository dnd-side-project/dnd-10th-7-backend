package com.sendback.domain.project.entity;

import com.sendback.global.exception.type.NotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.sendback.domain.project.entity.Progress.*;
import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProgressTest {

    @Nested
    @Disabled
    @DisplayName("진행 여부 enum으로 변경 시")
    class convertEnumProgress {

        @Test
        @DisplayName("planning -> 정상적인 값 반환")
        public void success_planning() throws Exception {
            //given
            String planningMessage = "planning";

            //when
            Progress planning = Progress.toEnum(planningMessage);

            //then
            assertThat(planning).isEqualTo(PLANNING);
        }

        @Test
        @DisplayName("developing -> 정상적인 값 반환")
        public void success_developing() throws Exception {
            //given
            String developingMessage = "developing";

            //when
            Progress planning = Progress.toEnum(developingMessage);

            //then
            assertThat(planning).isEqualTo(DEVELOPING);
        }

        @Test
        @DisplayName("refactoring -> 정상적인 값 반환")
        public void success_refactoring() throws Exception {
            //given
            String refactoringMessage = "refactoring";

            //when
            Progress planning = Progress.toEnum(refactoringMessage);

            //then
            assertThat(planning).isEqualTo(REFACTORING);
        }

        @Test
        @DisplayName("존재하지 않는 진행 여부는 예외 처리")
        public void fail_notExist() throws Exception {
            //given
            String randomMessage = "무작위";

            //when - then
            assertThatThrownBy(() -> Progress.toEnum(randomMessage))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(NOT_FOUND_PROGRESS.getMessage());
        }
    }

}
