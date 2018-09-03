package com.nd.auxo.recommend.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/7/1.
 */
public class ListRequestHelper {

    public static final int IDS_LIMITS = 50;

    /**
     * 拆分List参数
     *
     * @param list
     * @return
     */
    public static <T> List<List<T>> subList(List<T> list) {
        return subList(list, IDS_LIMITS);
    }

    /**
     * 拆分List参数
     *
     * @param list
     * @return
     */
    public static <T> List<List<T>> subList(List<T> list, Integer maxsize) {
        List<List<T>> lists = new ArrayList<List<T>>();
        int size = list.size();
        int round = (size / maxsize) + (size % maxsize != 0 ? 1 : 0);
        for (int i = 0; i < round; i++) {
            int from = i * maxsize;
            int to = (from + maxsize) >= size ? size : from + maxsize;
            lists.add(list.subList(from, to));
        }
        return lists;
    }

    /**
     * 拆分Array参数
     *
     * @param array
     * @return
     */
    public static <T> List<List<T>> subArray(T[] array, Integer maxsize) {
        List<List<T>> lists = new ArrayList<List<T>>();
        List<T> arrayList = Arrays.asList(array);
        int size = array.length;
        int round = (size / maxsize) + (size % maxsize != 0 ? 1 : 0);
        for (int i = 0; i < round; i++) {
            int from = i * maxsize;
            int to = (from + maxsize) >= size ? size : from + maxsize;
            lists.add(arrayList.subList(from, to));
        }
        return lists;
    }

}
