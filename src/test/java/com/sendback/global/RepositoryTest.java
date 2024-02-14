package com.sendback.global;

import com.sendback.domain.feedback.persister.FeedbackSubmitTestPersister;
import com.sendback.domain.feedback.persister.FeedbackTestPersister;
import com.sendback.domain.field.persister.FieldTestPersister;
import com.sendback.domain.like.persister.LikeTestPersister;
import com.sendback.domain.project.persister.ProjectImageTestPersister;
import com.sendback.domain.project.persister.ProjectTestPersister;
import com.sendback.domain.scrap.persister.ScrapTestPersister;
import com.sendback.domain.user.persister.UserTestPersister;
import com.sendback.global.config.TestJpaConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Persister.class))
@Import(TestJpaConfig.class)
@ActiveProfiles({"test"})
@TestMethodOrder(MethodOrderer.DisplayName.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(PER_CLASS)
public abstract class RepositoryTest {

    @Autowired
    protected FieldTestPersister fieldTestPersister;

    @Autowired
    protected ProjectTestPersister projectTestPersister;

    @Autowired
    protected UserTestPersister userTestPersister;

    @Autowired
    protected ProjectImageTestPersister projectImageTestPersister;

    @Autowired
    protected LikeTestPersister likeTestPersister;

    @Autowired
    protected ScrapTestPersister scrapTestPersister;

    @Autowired
    protected FeedbackTestPersister feedbackTestPersister;

    @Autowired
    protected FeedbackSubmitTestPersister feedbackSubmitTestPersister;

}
