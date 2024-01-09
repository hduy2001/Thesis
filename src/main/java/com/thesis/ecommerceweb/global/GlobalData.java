package com.thesis.ecommerceweb.global;

import com.thesis.ecommerceweb.model.Product;

import java.util.ArrayList;
import java.util.List;


public class GlobalData {
    public static String RememberUser;
    public static String orderName;
    public static List<Product> cart;
    static {
        cart = new ArrayList<>();
    }


}
