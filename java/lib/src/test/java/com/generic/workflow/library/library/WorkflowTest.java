package com.generic.workflow.library.library;

import com.generic.workflow.library.library.activity.*;
import com.generic.workflow.library.library.payload.CustomPayload_1;
import com.generic.workflow.library.library.payload.CustomPayload_2;
import com.generic.workflow.library.library.payload.CustomPayload_3;
import com.generic.workflow.library.library.workflow.CustomWorkflow;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;

public class WorkflowTest {

    @Test
    void happy_path() throws OperationNotSupportedException {

        var payload_1 = new CustomPayload_1(null);
        var payload_2 = new CustomPayload_2(null);
        var payload_3 = new CustomPayload_3(null);

        // left branch
        var wf_left = new CustomWorkflow();
        var left_activityTreeBranch = wf_left
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
        var wf_right = new CustomWorkflow();
        var right_activityTreeBranch = wf_right
                .startWith(new CustomActivity_2())
                .next(new CustomCondition_1(), new CustomActivity_2())
                .when(new CustomCondition_2(), new CustomActivity_1())
                .when(new CustomCondition_1(), new CustomActivity_2())
                .root();

        // root node
        var wf_root = new CustomWorkflow();
        var main_root = wf_root
                .startWith(new CustomActivity_0())
                .setOrResetDefaultCondition(new CustomCondition_1())
                .next(new CustomCondition_2(), new CustomActivity_2())
                .when(new CustomCondition_0(), left_activityTreeBranch, right_activityTreeBranch)
                .root();

        // test activities:
        // - left
        boolean act_left_couldPass_1 = left_activityTreeBranch.testBefore(payload_1);
        boolean act_left_passed = left_activityTreeBranch.execute(payload_1);
        boolean act_left_couldPass_2 = left_activityTreeBranch.testAfter(new CustomCondition_1());
        boolean act_left_couldPass_3 = left_activityTreeBranch.testAfter(new CustomCondition_2());
        var act_left_status = left_activityTreeBranch.status();
        boolean act_left_suspended = left_activityTreeBranch.suspend();
        // - right
        boolean act_right_couldPass_1 = right_activityTreeBranch.testBefore(payload_2);
        boolean act_right_passed = right_activityTreeBranch.execute(payload_2);
        boolean act_right_couldPass_2 = right_activityTreeBranch.testAfter(new CustomCondition_1());
        boolean act_right_couldPass_3 = right_activityTreeBranch.testAfter(new CustomCondition_2());
        var act_right_status = right_activityTreeBranch.status();
        boolean act_right_suspended = right_activityTreeBranch.suspend();
        // - main
        boolean act_main_couldPass_1 = main_root.testBefore(payload_3);
        boolean act_passed = main_root.execute(payload_3);
        boolean act_main_couldPass_2 = main_root.testAfter(new CustomCondition_1());
        boolean act_main_couldPass_3 = main_root.testAfter(new CustomCondition_2());
        var act_status = main_root.status();
        boolean act_suspended = main_root.suspend();

        // test branches:
        // - left
        boolean left_couldPass_1 = wf_left.testBefore(payload_1);
        boolean left_passed = wf_left.execute(payload_1);
        boolean left_couldPass_2 = wf_left.testAfter(new CustomCondition_1());
        boolean left_couldPass_3 = wf_left.testAfter(new CustomCondition_2());
        var left_status = wf_left.status();
        boolean left_suspended = wf_left.suspend();
        // - right
        boolean right_couldPass_1 = wf_right.testBefore(payload_2);
        boolean right_passed = wf_right.execute(payload_2);
        boolean right_couldPass_2 = wf_right.testAfter(new CustomCondition_1());
        boolean right_couldPass_3 = wf_right.testAfter(new CustomCondition_2());
        var right_status = wf_right.status();
        boolean right_suspended = wf_right.suspend();
        // - main
        boolean main_couldPass_1 = wf_root.testBefore(payload_3);
        boolean passed = wf_root.execute(payload_3);
        boolean main_couldPass_2 = wf_root.testAfter(new CustomCondition_1());
        boolean main_couldPass_3 = wf_root.testAfter(new CustomCondition_2());
        var status = wf_root.status();
        boolean suspended = wf_root.suspend();

        // build workflow
        var workflow = main_root.build();

        // test, execute, monitor & suspend workflow
        boolean workflow_couldPass_1 = workflow.testBefore(payload_3);
        boolean workflow_passed = workflow.execute(payload_3);
        boolean workflow_couldPass_2 = workflow.testAfter(new CustomCondition_2());
        var workflow_status = workflow.status();
        boolean workflow_suspended = workflow.suspend();

    }
}
