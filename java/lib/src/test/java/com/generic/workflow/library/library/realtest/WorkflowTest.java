package com.generic.workflow.library.library.realtest;

import com.generic.workflow.library.activities.Activity;
import com.generic.workflow.library.library.realtest.activity.Breakfast;
import com.generic.workflow.library.library.realtest.activity.PrepareInformation;
import com.generic.workflow.library.library.realtest.activity.Training;
import com.generic.workflow.library.library.realtest.confitions.DefaultFinishedCondition;
import com.generic.workflow.library.library.realtest.workflow.MorningRoutine;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

public class WorkflowTest {

    @Test
    void givenFewRealActivities_whenBreakfastIsValid_thenSucceed() throws Exception {

        var info = new PrepareInformation(
                ZonedDateTime.now()
                        .withHour(9)
                        .toInstant(),
                "L",
                "L"
        );

        var workflow = buildFlow_1(info)
                .build();
        boolean started = workflow.execute(null);
        boolean passed = workflow.testAfter(new DefaultFinishedCondition());

        assert started;
        assert passed;
    }

    @Test
    void givenFewRealActivities_whenBreakfastIsNotValid_thenFail() throws Exception {

        var info = new PrepareInformation(
                ZonedDateTime.now()
                        .withHour(13)
                        .toInstant(),
                "L",
                "L"
        );

        var activity = buildFlow_1(info);
        var workflow = activity.build();

        boolean started = workflow.execute(null);
        boolean workflowPassed = workflow.testAfter(new DefaultFinishedCondition());

        var breakfast = workflow.getActivityFromHistoryOrder(1);
        boolean activityPassed = breakfast.testAfter(new DefaultFinishedCondition());

        assert started;
        assert workflowPassed;
        assert !activityPassed;
    }

    @Test
    void givenFewRealActivities_whenBreakfastIsNotValid_thenExecuteTraining() throws Exception {

        var info = new PrepareInformation(
                ZonedDateTime.now()
                        .withHour(13)
                        .toInstant(),
                "L",
                "L"
        );

        var workflow = buildFlow_2(info)
                .build();
        boolean started = workflow.execute(null);
        boolean passed = workflow.testAfter(new DefaultFinishedCondition());

        assert started;
        assert passed;
    }

    private Activity buildFlow_1(PrepareInformation information) throws Exception {

        var defCondition = new DefaultFinishedCondition();

        var breakfast = new Breakfast(6, 10);
        var training = new Training("L", "L");

        var morningRoutine = new MorningRoutine();

        return morningRoutine
                .startWith(information)
                .when(defCondition, breakfast)
                .when(defCondition, training)
                .root();
    }

    private Activity buildFlow_2(PrepareInformation information) throws Exception {

        var defCondition = new DefaultFinishedCondition();

        var breakfast = new Breakfast(6, 10);
        var training = new Training("L", "L");

        var morningRoutine = new MorningRoutine();

        return morningRoutine
                .startWith(information)
                .next(defCondition, breakfast)
                .when(defCondition.negate(), training)
                .root();
    }
}
