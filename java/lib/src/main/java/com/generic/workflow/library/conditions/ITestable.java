package com.generic.workflow.library.conditions;

public interface ITestable<I> {
    /**
     * Test if something meets the criteria.
     * @return true if condition is fulfilled, false otherwise
     */
    boolean testAfter(I inputToTest);
}
