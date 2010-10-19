package test.support.com.pyxis.petstore.web;

import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WindowTracker {

    private final WebDriver proxy;
    private final WebDriver webdriver;
    private final String windowHandle;

    public static WindowTracker tracking(WebDriver webdriver) {
        return new WindowTracker(webdriver);
    }

    public WindowTracker(WebDriver webdriver) {
        Class<? extends WebDriver> type = webdriver.getClass();
        List<Class<?>> interfaces = new ArrayList<Class<?>>(Arrays.asList(type.getInterfaces()));
        if (!interfaces.contains(WebDriver.class)) interfaces.add(WebDriver.class);
        proxy = WebDriver.class.cast(Proxy.newProxyInstance(
                type.getClassLoader(),
                interfaces.toArray(new Class[interfaces.size()]),
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return forward(method, args);
                    }
                }));
        this.webdriver = webdriver;
        this.windowHandle = webdriver.getWindowHandle();
    }

    public WebDriver inWindow() {
        return proxy;
    }

    public Object forward(Method method, Object[] args) throws Throwable {
        if (closeCalled(method) || quitClosed(method)) {
            closeWindow();
            return null;
        } else {
            return method.invoke(webdriver, args);
        }
    }

    private boolean quitClosed(Method method) {
        return method.getName().equals("quit");
    }

    private boolean closeCalled(Method method) {
        return method.getName().equals("close");
    }

    private void closeWindow() {
        webdriver.switchTo().window(windowHandle);
        webdriver.close();
    }
}