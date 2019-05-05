import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;


public class LinkedStackTest {

    private LinkedStack linkedStack;


    @Before
    public void createLinkedStack() {
        linkedStack = new LinkedStack();
    }

    @Test(expected = NoSuchElementException.class)
    public void testPeekTrowExceptionWhenIsEmpty(){
        doThrow(new NoSuchElementException()).when(linkedStack.peek());
        linkedStack.peek();
    }

    @Test(expected = NoSuchElementException.class)
    public void testPopTrowExceptionWhenIsEmpty(){
        doThrow(new NoSuchElementException()).when(linkedStack.pop());
        linkedStack.pop();
    }

    @Test
    public void testIsEmpty() {
        assertThat(this.linkedStack.isEmpty(), is(true));
        linkedStack.push(3);
        assertThat(this.linkedStack.isEmpty(),  is(false));
    }

    @Test
    public void testToString() {
        linkedStack.push(1);
        linkedStack.push(2);
        linkedStack.push(3);
        assertThat(this.linkedStack.toString(), is("[ 321 ]"));

    }
}