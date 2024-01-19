package com.thesis.ecommerceweb.global;

import com.thesis.ecommerceweb.model.Product;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GlobalData {
    public static String RememberUser;
    public static String orderName;
    public static List<Product> cart;
    static {
        cart = new ArrayList<>();
    }

    public static List<Integer> extractQuantities(String input) {
        List<Integer> quantities = new ArrayList<>();
        Matcher matcher = Pattern.compile("(\\d+) x").matcher(input);
        while (matcher.find()) {
            quantities.add(Integer.parseInt(matcher.group(1)));
        }
        return quantities;
    }

    public static List<Integer> extractIds(String input) {
        List<Integer> ids = new ArrayList<>();
        Matcher matcher = Pattern.compile("id=(\\d+)").matcher(input);
        while (matcher.find()) {
            ids.add(Integer.parseInt(matcher.group(1)));
        }
        return ids;
    }

    public static List<String> extractSizes(String input) {
        List<String> sizes = new ArrayList<>();
        Matcher matcher = Pattern.compile("size=([\\d.]+)").matcher(input);
        while (matcher.find()) {
            sizes.add(matcher.group(1));
        }
        return sizes;
    }

    public static List<Integer> extractUniqueIds(String input) {
        Set<Integer> uniqueIds = new HashSet<>();
        Matcher matcher = Pattern.compile("id=(\\d+)").matcher(input);

        while (matcher.find()) {
            uniqueIds.add(Integer.parseInt(matcher.group(1)));
        }
        return new ArrayList<>(uniqueIds);
    }
}
