package com.thesis.ecommerceweb.controller;

import com.thesis.ecommerceweb.configuration.VNPayService;
import com.thesis.ecommerceweb.global.GlobalData;
import com.thesis.ecommerceweb.model.Order;
import com.thesis.ecommerceweb.model.Product;
import com.thesis.ecommerceweb.service.OrderService;
import com.thesis.ecommerceweb.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class CartController {
    private int cid;
    private int total = 0;
    private int totalQuantity = 0;
    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Autowired
    private VNPayService vnPayService;

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id) {
        int index = -1;
        for (int i = 0; i < GlobalData.cart.size(); i++) {
            if (id == GlobalData.cart.get(i).getPid()) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            // Product found in cart
            Product product = GlobalData.cart.get(index);
            int newQuantity = product.getQuantity() + 1;
            product.setQuantity(newQuantity);
        } else {
            Product currentProduct = productService.getProductById(id).get();
            Product product = new Product();
            product = productService.getProductById(id).get();
            cid = product.getCategory().getCid();
            product.setQuantity(1);
            GlobalData.cart.add(product);
        }
        return "redirect:/shopPage/" + cid;
    }

    @GetMapping("/cart")
    public String cartGet(Model model) {
        for (Product product : GlobalData.cart) {
            total += product.getPrice() * product.getQuantity();
            totalQuantity += product.getQuantity();
        }
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", total);
        model.addAttribute("cart", GlobalData.cart);
        return "/web/cart";
    }

    @GetMapping("/cart/removeItem/{index}")
    public String cartItemRemove(@PathVariable int index) {
        GlobalData.cart.remove(index);
        return "redirect:/cart";
    }

    @GetMapping("/shopPage/removeItem/{index}")
    public String removeItem(@PathVariable int index) {
        GlobalData.cart.remove(index);
        return "redirect:/shopPage/" + cid;
    }

    @GetMapping("/checkoutCod")
    public String checkoutCod(Model model, Order order) {
        order.setUsername(GlobalData.RememberUser);
        order.setQuantity(totalQuantity);
        order.setTotal_price(total);
        order.setPay_type("COD");
        order.setStatus("Ordered");
        order.setIs_pay(0);
        orderService.addOrder(order);
        totalQuantity = 0;
        return "/web/CodNotice";
    }

    //Checkout online
    @GetMapping("/checkoutOnline")
    public String submitOrder(HttpServletRequest request){
        String orderInfo = "Running store";
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(total + 15000, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Order order){
        int paymentStatus = vnPayService.orderReturn(request);
//        String orderInfo = request.getParameter("vnp_OrderInfo");
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//
//        model.addAttribute("orderId", orderInfo);
//        model.addAttribute("totalPrice", totalPrice);
//        model.addAttribute("paymentTime", paymentTime);
//        model.addAttribute("transactionId", transactionId);
        order.setUsername(GlobalData.RememberUser);
        order.setQuantity(totalQuantity);
        order.setTotal_price(total);
        order.setPay_type("Online");
        order.setStatus("Delivering");
        if (paymentStatus == 1) {
            order.setIs_pay(1);
            orderService.addOrder(order);
            totalQuantity = 0;
            return "/web/ordersuccess";
        }
        order.setIs_pay(0);
        orderService.addOrder(order);
        return "/web/orderfail";
    }
}
