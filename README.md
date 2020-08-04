## 前言
在实际项目中写单元测试的过程中，我们会发现需要测试的类有很多依赖，这些依赖又会有依赖，导致想要完成单元测试是一件很难的事情，为了解决这些问题，我们引入了Mock的概念，简单的说就是模拟这些资源或者依赖。
![Mockito](https://molzhao-pic.oss-cn-beijing.aliyuncs.com/2020-08-04/1148190-20171012164125840-513174531.png)

## Mock捕获参数的情景
我们在被mock的方法调用参数明确的情况下，可以无需捕获参数，但是有些情况下，比如方法没有返回值，根据不同的情景，方法需要传入不同的参数，比如说监控的错误日志，针对不同的错误，我们传入的错误日志的参数也是不同，诸多这些例子有很多。

## 捕获一次mock方法的调用参数
下面是一个简单的例子，只捕获一次调用参数
```java
@Test
    public void testHello() {
        helloWorldController.hello(new NullPointerException());

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(helloWorldService, times(1)).save(argumentCaptor.capture());

        assertEquals("zyj", argumentCaptor.getValue().getName());
    }
```

```java
@Controller
public class HelloWorldController {
    @Resource
    IHelloWorldService helloWorldService;

    public void hello(Throwable cause) {
        if ((cause instanceof NullPointerException)) {
            helloWorldService.save(new User("zyj", 18));
        } else {
            helloWorldService.save(new User("zyj1", 19));
        }

    }
}
```
我们根据传入的不同异常，可以获取当前传入`save()`方法的参数，从而实现校验过程。
比如我传入NPE异常`argumentCaptor.getValue().getName()`就应该是zyj，传入`Exception`,那`argumentCaptor.getValue().getName()`的结果就是zyj1

## 捕获多次mock方法的调用参数
如果mock方法被调用多次，该如何知道每次调用时的参数呢？`argumentCaptor.getValue()` 只会返回最后一次调用的参数值

```java
@Test
    public void testHello1() {
        helloWorldController.hello(new NullPointerException());
        helloWorldController.hello(new Exception());

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(helloWorldService, times(2)).save(argumentCaptor.capture());

        assertEquals("zyj1", argumentCaptor.getValue().getName());
    }
```
我们手动模拟调用两次`hellowoldController.hello()`的方法，得到的结果却是最后一次的调用的返回结果，说明了`argumentCaptor.getValue()` 方法只保存最后一次调用结果。

如果我们需要获得所有的调用参数值，那该怎么做呢，Mockito给我们提供了`argumentCaptor.getAllValues()`方法，它返回的是一个`List<T>`

```java
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
```
通过`argumentCaptor.getAllValues()`得到的集合，如果mock方法是异步无序调用的，则可以使用Java8的新特性Stream的allMatch(),或者anyMatch()去匹配断言。如果是按顺序则直接断言即可。
