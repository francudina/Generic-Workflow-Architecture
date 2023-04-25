package com.generic.workflow.library.conditions;

public abstract class Condition<I> implements ITestable<I> {

    protected boolean negateCondition;

    public Condition(boolean negateCondition) {
        this.negateCondition = negateCondition;
    }

    public Condition() {
        this(false);
    }

    /**
     * Negate original {@link Condition} {@link #test(I)} result.
     *
     * @return negated {@link #test(I)}
     */
    public abstract <C extends Condition<I>> C negate();
}
