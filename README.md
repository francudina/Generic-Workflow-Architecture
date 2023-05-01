# Generic Workflow Architecture
[![Tests](https://github.com/francudina/Generic-Workflow-Architecture/actions/workflows/tests.yaml/badge.svg)](https://github.com/francudina/Generic-Workflow-Architecture/actions/workflows/tests.yaml)


### Description
Project used to make life easier every time when there is a business logic that requires some decision trees! 
With execution status tracking, it is possible to direct execution flow.

### Examples
```
// left branch
var wf_left = new CustomWorkflow();
var left_activityTreeBranch = wf_left
        .startWith(new CustomActivity_1())
        .setOrResetDefaultCondition(new CustomCondition_0())
        .next(new CustomActivity_1())
        .next(new CustomActivity_2())
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
    
// build workflow
var workflow = main_root.build();

// test, execute, monitor & suspend workflow
boolean workflow_couldPass_1 = workflow.testBefore(payload_3);
boolean workflow_passed = workflow.execute(payload_3);
boolean workflow_couldPass_2 = workflow.testAfter(new CustomCondition_2());
var workflow_status = workflow.status();
boolean workflow_suspended = workflow.suspend();
```
