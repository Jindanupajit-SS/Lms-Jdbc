package com.smoothstack.jan2020.LmsJDBC.mvc;

import com.smoothstack.jan2020.LmsJDBC.Debug;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class MVCEngine implements Runnable{
    private static HashMap<Class<? extends Controller>, Controller> controllerSingleton = new HashMap<>();
    private static HashMap<Class<? extends View>, View> viewSingleton = new HashMap<>();
    private static HashMap<String, Method> controllerMap = new HashMap<>();
    private static HashMap<String, Method> viewMap = new HashMap<>();

    protected MVCEngine() {
    }

    public static void start() {
        Thread mvc = new Thread(new MVCEngine());

        mvc.setName("LMS Library Management System");
        mvc.start();
    }

    synchronized public static void registerController(Class<? extends Controller> controller) throws IllegalAccessException, InstantiationException {
        for (Method method : controller.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && method.isAnnotationPresent(Mapping.class)) {
                controllerMap.put(method.getAnnotation(Mapping.class).value().toLowerCase(), method);
                controllerSingleton.put(controller, controller.newInstance());
            }
        }
    }

    synchronized public static void registerView(Class<? extends View> view) throws IllegalAccessException, InstantiationException {
        for (Method method : view.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && method.isAnnotationPresent(Template.class)) {
                viewMap.put(method.getAnnotation(Template.class).value().toLowerCase(), method);
                viewSingleton.put(view, view.newInstance());
            }
        }
    }


    @Override
    public void run() {

        String endPoint = "home";
        String template = "";
        Model model = new Model();
        RequestParam requestParam = new RequestParam();

        while(!endPoint.toLowerCase().matches("exit")) {

            if (! controllerMap.containsKey(endPoint.toLowerCase()))
                throw new RuntimeException(String.format("No controller mapping for '%s'", endPoint));
            Debug.printf("Calling Controller '%s'\n", endPoint);
            try {

                Method method = controllerMap.get(endPoint.toLowerCase());

                Controller instant = controllerSingleton.get(method.getDeclaringClass());

                template = (String) method.invoke(instant, model, requestParam);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            if (template.toLowerCase().matches("^redirect:.+")) {
                endPoint = template.toLowerCase().replaceAll("^redirect:", "");
                Debug.printf("Redirect -> '%s'\n", endPoint);
                template = "";
                model.clear();
                continue;
            }
            else {
                endPoint = "";
                requestParam.clear();
            }

            if (! viewMap.containsKey(template.toLowerCase()))
                throw new RuntimeException(String.format("No template mapping for '%s'", template));

            Debug.printf("Calling Template '%s'\n", template);
            try {

                Method method = viewMap.get(template.toLowerCase());

                View instant = viewSingleton.get(method.getDeclaringClass());

                endPoint = (String) method.invoke(instant, model, requestParam);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            if (endPoint.toLowerCase().matches("exit")) {
                break;
            }

            template = "";
            model.clear();
        }
    }
}
