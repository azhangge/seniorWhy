package com.nd.auxo.recommend;

import com.nd.gaea.core.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/5.
 */
@Slf4j
public abstract class BasicLombokObjTest {
    public static final UUID EMPTY_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    protected boolean testBasicGetterAndSetter = true;
    protected boolean testLombokSupportFunctions = true;
    public Object t1;
    public Object t2;
    public Class cls;
    public List<Method> getMethods = new ArrayList<>();
    public List<Method> setMethods = new ArrayList<>();

    /**
     * init t1 and t2 here
     * change testing params here
     */
    public void init(Object t) throws IllegalAccessException, InstantiationException {
        cls = t.getClass();
        initData();
    }

    @Test
    public void test() {
        testBasicGetterAndSetter();
    }

    public void testBasicGetterAndSetter() {
        if (!testBasicGetterAndSetter)
            return;
        initMethods();
        testLombokSupportFunctions();
        try {
            //构造倒序setter队列，以走到更多比对逻辑
            List<Method> descSetters = new ArrayList<>();
            descSetters.addAll(setMethods);
            Collections.sort(descSetters, new Comparator<Method>() {
                @Override
                public int compare(Method o1, Method o2) {
                    return -1;
                }
            });
            //第一轮，先设置t1单个属性，比t2null
            for (Method setter : descSetters) {
                initData();
                Object[] args = getRandomValue(setter, 0);
                setter.invoke(t1, args);
                t1.equals(t2);
                testLombokSupportFunctions();
            }
            //第二轮，重新初始化，给t1\t2设置相同属性
            initData();
            for (Method setter : descSetters) {
                Object[] args = getRandomValue(setter, 0);
                setter.invoke(t1, args);
                setter.invoke(t2, args);
                t1.equals(t2);
                testLombokSupportFunctions();
            }
            //第三轮，不重新初始化，设置t2不同属性
            for (Method setter : descSetters) {
                Object[] args = getRandomValue(setter, 1);
                setter.invoke(t2, args);

                t1.equals(t2);
                testLombokSupportFunctions();
            }
            for (Method getter : getMethods) {
                getter.invoke(t1);
                getter.invoke(t2);
            }
        } catch (IllegalAccessException e) {
            log.debug("", e);
        } catch (InstantiationException e) {
            log.debug("", e);
        } catch (InvocationTargetException e) {
            log.debug("", e);
        }
    }


    public void testLombokSupportFunctions() {
        if (!testLombokSupportFunctions)
            return;
        Assert.assertTrue(t1.equals(t1));
        Assert.assertFalse(t1.equals("t1"));
        if (t1.equals(t2)) {
            Assert.assertTrue(t1.equals(t2));
        } else {
            Assert.assertFalse(t1.equals(t2));
        }
        try {
            Assert.assertTrue(t1.equals(BeanUtils.cloneBean(t1)));
        } catch (Exception e) {
            log.debug("", e);
        }
        Assert.assertNotNull(t1.hashCode());
        Assert.assertNotNull(t1.toString());
        Assert.assertNotNull(t2.hashCode());
        Assert.assertNotNull(t2.toString());
    }

    private void initData() throws IllegalAccessException, InstantiationException {
        t1 = cls.newInstance();
        t2 = cls.newInstance();
    }

    private void initMethods() {
        Method[] methods = cls.getMethods();
        Field[] fields = cls.getDeclaredFields();
        Set<String> fNames = new HashSet<>();
        for (Field f : fields) {
            fNames.add("get" + f.getName().toLowerCase());
            fNames.add("set" + f.getName().toLowerCase());
        }
        String gs = "get(\\w+)";
        String ss = "set(\\w+)";
        for (int i = 0; i < methods.length; ++i) {
            Method m = methods[i];
            String methodName = m.getName();
            if (methodName.equals("getClass")) continue;
            if (Pattern.matches(gs, methodName) && fNames.contains(methodName.toLowerCase())) {
                if (m.getParameterTypes().length > 0) continue;//只支持lombok生产的基础的getter
                getMethods.add(m);
            } else if (Pattern.matches(ss, methodName) && fNames.contains(methodName.toLowerCase())) {
                if (m.getParameterTypes().length > 1) continue;//只支持lombok生产的基础的setter
                setMethods.add(m);
            }
        }
    }

    private Object[] getRandomValue(Method setter, int seed) {
        Class valueClass = setter.getParameterTypes()[0];
        Object[] args = new Object[1];
        //先判断一些基本类型的封装类
        if (valueClass.equals(String.class)) {
            args[0] = String.valueOf(new SecureRandom().nextInt());
        } else if (valueClass.equals(Integer.class) || valueClass.toString().equals("int")) {
            args[0] = new SecureRandom().nextInt(2147483640);
        } else if (valueClass.equals(Double.class) || valueClass.toString().equals("double")) {
            args[0] = new SecureRandom().nextDouble();
        } else if (valueClass.equals(Float.class) || valueClass.toString().equals("float")) {
            args[0] = new SecureRandom().nextFloat();
        } else if (valueClass.equals(Long.class) || valueClass.toString().equals("long")) {
            args[0] = new SecureRandom().nextLong();
        } else if (valueClass.equals(Short.class) || valueClass.toString().equals("short")) {
            args[0] = Short.valueOf(String.valueOf(new SecureRandom().nextInt(32760)));
        } else if (valueClass.equals(Boolean.class) || valueClass.toString().equals("boolean")) {
            args[0] = seed == 0;
        } else if (valueClass.equals(Date.class)) {
            args[0] = new Date();
        } else if (valueClass.equals(UUID.class)) {
            args[0] = seed == 0 ? EMPTY_UUID : UUID.randomUUID();
        } else {
            if (valueClass.isEnum()) {
                //if 枚举类
                Object[] ts = valueClass.getEnumConstants();
                //如果多个值，取不同的值
                args[0] = seed == 0 ? ts[0] : (ts.length > 1 ? ts[1] : ts[0]);
            } else if (valueClass.isArray()) {
                Class pt = (Class) setter.getGenericParameterTypes()[0];
                args[0] = Array.newInstance(pt.getComponentType(), seed);
            } else {
                if (valueClass.isInterface()) {
                    //如果是个接口类
                    try {
                        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                        Class classOfClassLoder = classLoader.getClass();
                        while (classOfClassLoder != ClassLoader.class) {
                            classOfClassLoder = classOfClassLoder.getSuperclass();
                        }
                        Field clf = classOfClassLoder.getDeclaredField("classes");
                        clf.setAccessible(true);
                        List<Class> v = new ArrayList<>((Vector)clf.get(classLoader));
                        for (Class c : v) {
                            if (valueClass.isAssignableFrom(c) && !valueClass.equals(c) && !c.isInterface() && Modifier.toString(c.getModifiers()).contains("public")) {
                                try {
                                    args[0] = c.newInstance();
                                    break;
                                } catch (InstantiationException e) {
                                    log.debug("", e);
                                } catch (IllegalAccessException e) {
                                    log.debug("", e);
                                }
                            }
                        }
                        clf.setAccessible(false);
                    } catch (NoSuchFieldException e) {
                        log.debug("", e);
                    } catch (IllegalAccessException e) {
                        log.debug("", e);
                    } catch (ConcurrentModificationException e) {
                        log.error("", e);
                    }
                } else {
                    //复杂对象
                    try {
                        args[0] = valueClass.newInstance();
                    } catch (InstantiationException e) {
                        log.debug("", e);
                    } catch (IllegalAccessException e) {
                        log.debug("", e);
                    }
                }
            }
        }
        return args;
    }
}
