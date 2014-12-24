package com.zynap.talentstudio.web.workflow.performance;

import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: amark
 * Date: 06-Dec-2005
 * Time: 14:03:29
 * Class that represents a set of answers to a given question - used when a manager views evaluator answers for an appraisal.
 */
public final class PerformanceReviewQuestion implements Serializable {

    private final List answers;

    private final String questionLabel;

    public PerformanceReviewQuestion(String questionLabel) {
        this.questionLabel = questionLabel;
        this.answers = new ArrayList();
    }

    public Collection getAnswers() {
        return answers;
    }

    public String getQuestionLabel() {
        return questionLabel;
    }

    public void addAnswer(PerformanceReviewAnswer answer) {
        this.answers.add(answer);

        Collections.sort(answers);
    }

    public boolean isHasAnswers() {
        return answers != null && !answers.isEmpty();
    }
}
