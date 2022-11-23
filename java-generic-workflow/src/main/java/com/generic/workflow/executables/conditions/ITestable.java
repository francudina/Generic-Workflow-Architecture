package com.generic.workflow.executables.conditions;

public interface ITestable<I> {
    /**
     * Test if something meets the criteria.
     * @return true if condition is fulfilled, false otherwise
     */
    boolean test(I inputToTest);
}
