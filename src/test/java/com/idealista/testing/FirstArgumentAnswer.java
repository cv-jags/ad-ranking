package com.idealista.testing;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class FirstArgumentAnswer<T> implements Answer<T> {
    @Override
    @SuppressWarnings("unchecked")
    public T answer(InvocationOnMock invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        return (T) args[0];
    }
}