package com.generic.workflow.library.conditions;

public abstract class Condition<I> implements ITestable<I> {

    protected boolean isNegatedCondition;

    public Condition(boolean isNegatedCondition) {
        this.isNegatedCondition = isNegatedCondition;
    }

    public Condition() {
        this(false);
    }

    /**
     * Negate original {@link Condition} {@link #testAfter(I)} result.
     *
     * @return negated {@link #testAfter(I)}
     */
    public abstract Condition<I> negate();
}
