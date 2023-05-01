package com.generic.workflow.library.conditions;

public abstract class Condition<I> implements ITestable<I> {

    protected boolean isNegatedCondition;

    public Condition(boolean negateCondition) {
        this.isNegatedCondition = negateCondition;
    }

    public Condition() {
        this(false);
    }

    /**
     * Negate original {@link Condition} {@link #testAfter(I)} result.
     *
     * @return negated {@link #testAfter(I)}
     */
    public abstract <C extends Condition<I>> C negate();
}
