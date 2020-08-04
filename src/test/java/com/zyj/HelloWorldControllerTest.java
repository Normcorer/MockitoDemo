package com.zyj;

import com.zyj.controller.HelloWorldController;
import com.zyj.domain.User;
import com.zyj.service.IHelloWorldService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HelloWorldControllerTest {

    @InjectMocks
    private HelloWorldController helloWorldController;

    @Mock
    private IHelloWorldService helloWorldService;

    @Test
    public void testHello() {
        helloWorldController.hello(new NullPointerException());

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(helloWorldService, times(1)).save(argumentCaptor.capture());

        assertEquals("zyj", argumentCaptor.getValue().getName());
    }

    @Test
    public void testHello1() {
        helloWorldController.hello(new NullPointerException());
        helloWorldController.hello(new Exception());

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(helloWorldService, times(2)).save(argumentCaptor.capture());

        assertEquals("zyj1", argumentCaptor.getValue().getName());
    }

    @Test
    public void testHello2() {
        helloWorldController.hello(new NullPointerException());
        helloWorldController.hello(new Exception());

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(helloWorldService, times(2)).save(argumentCaptor.capture());

        List<User> allValues = argumentCaptor.getAllValues();

        assertEquals(2, allValues.size());
        assertTrue(allValues.stream().allMatch(user ->
                Arrays.asList("zyj", "zyj1").contains(user.getName()))
        );
    }
}
