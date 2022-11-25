package com.generic.workflow.library;

import com.generic.workflow.library.activity.*;
import com.generic.workflow.library.workflow.CustomWorkflow;
import org.junit.jupiter.api.Test;

public class ActivityTest {

    @Test
    void creatingCustomActivity() {

        // left branch
        var left_activityTreeBranch = new CustomWorkflow()
                .startWith(new CustomActivity_1())
                .setOrResetDefaultCondition(new CustomCondition_0())
                .next(new CustomActivity_2())
                .setOrResetDefaultCondition(new CustomCondition_0())
                .next(new CustomActivity_1())
                .next(new CustomActivity_2())
                .next(new CustomCondition_2(), new CustomActivity_2())
                .next(new CustomCondition_1(), new CustomActivity_2())
                .when(new CustomCondition_2(), new CustomActivity_1(), new CustomActivity_2())
                .root();

        // right branch
        var right_activityTreeBranch = new CustomWorkflow()
                .startWith(new CustomActivity_2())
                .next(new CustomCondition_1(), new CustomActivity_2())
                .when(new CustomCondition_2(), new CustomActivity_1())
                .when(new CustomCondition_1(), new CustomActivity_2())
                .root();

        // root node
        var main_root = new CustomWorkflow()
                .startWith(new CustomActivity_0())
                .setOrResetDefaultCondition(new CustomCondition_1())
                .next(new CustomCondition_2(), new CustomActivity_2())
                .when(new CustomCondition_0(), left_activityTreeBranch, right_activityTreeBranch)
                .root();

        // test branches:
        // - left
        boolean left_couldPass_1 = left_activityTreeBranch.test();
        boolean left_couldPass_2 = left_activityTreeBranch.test(new CustomCondition_1());
        boolean left_couldPass_3 = left_activityTreeBranch.test(new CustomCondition_2());
        boolean left_passed = left_activityTreeBranch.execute();
        var left_status = left_activityTreeBranch.status();
        boolean left_suspended = left_activityTreeBranch.suspend();
        // - right
        boolean right_couldPass_1 = right_activityTreeBranch.test();
        boolean right_couldPass_2 = right_activityTreeBranch.test(new CustomCondition_1());
        boolean right_couldPass_3 = right_activityTreeBranch.test(new CustomCondition_2());
        boolean right_passed = right_activityTreeBranch.execute();
        var right_status = right_activityTreeBranch.status();
        boolean right_suspended = right_activityTreeBranch.suspend();
        // - main
        boolean main_couldPass_1 = main_root.test();
        boolean main_couldPass_2 = main_root.test(new CustomCondition_1());
        boolean main_couldPass_3 = main_root.test(new CustomCondition_2());
        boolean passed = main_root.execute();
        var status = main_root.status();
        boolean suspended = main_root.suspend();

        // build workflow
        var workflow = main_root.build();

        // test, execute, monitor & suspend workflow
        boolean workflow_couldPass_1 = workflow.test();
        boolean workflow_couldPass_2 = workflow.test(new CustomCondition_2());
        boolean workflow_passed = workflow.execute();
        var workflow_status = workflow.status();
        boolean workflow_suspended = workflow.suspend();

    }
}
