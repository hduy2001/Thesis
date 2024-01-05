package com.thesis.ecommerceweb.controller;

import com.thesis.ecommerceweb.configuration.VNPayService;
import com.thesis.ecommerceweb.global.GlobalData;
import com.thesis.ecommerceweb.model.Cart;
import com.thesis.ecommerceweb.model.Order;
import com.thesis.ecommerceweb.model.Product;
import com.thesis.ecommerceweb.model.Stock;
import com.thesis.ecommerceweb.service.CartService;
import com.thesis.ecommerceweb.service.OrderService;
import com.thesis.ecommerceweb.service.ProductService;
import com.thesis.ecommerceweb.service.StockService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class CartController {
    private int cid;
    private int total = 0;
    private int totalQuantity = 0;
    @Autowired
    ProductService productService;

    @Autowired
    StockService stockService;

    @Autowired
    OrderService orderService;

    @Autowired
    CartService cartService;

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

    @GetMapping("/getSize")
    @ResponseBody
    public List<String> getSize(@RequestParam int pid) {
        List<String> listSize = new ArrayList<>();

        for (Stock stock : stockService.getStockByPid(pid)) {
            listSize.add(stock.getSize());
        }

        return listSize;
    }

    @GetMapping("/getStock")
    @ResponseBody
    public int getStock(@RequestParam int pid, @RequestParam String size) {
        int stock = 0;
        Product product = productService.getProductById(pid).get();
        if (product.getCategory().getCid() == 2 || product.getCategory().getCid() == 3 || product.getCategory().getCid() == 7) {
            stock = stockService.getStockOneProduct(pid).get().getInStock();
        }
        else {
            stock = stockService.getStock(pid, size).get().getInStock();
        }

        return stock;
    }

    @PostMapping("/addToCart")
    @ResponseBody
    public String addCart(@RequestParam int pid, @RequestParam String size, @RequestParam int quantity, Principal principal) {
        int stock = stockService.getStock(pid, size).get().getInStock();

        if (principal != null) {
            Cart existingCart = cartService.findExactlyCart(pid, principal.getName(), size);

            if (existingCart != null) {
                // Nếu sản phẩm đã tồn tại trong cart, cập nhật quantity nếu không vượt quá tồn kho
                int newQuantity = Math.min(stock, existingCart.getQuantity() + quantity);
                existingCart.setQuantity(newQuantity);
                cartService.saveCart(existingCart);
            } else {
                // Nếu sản phẩm chưa có trong cart, thêm mới
                Cart cart = new Cart();
                cart.setUsername(principal.getName());
                cart.setPid(pid);
                cart.setSize(size);
                cart.setQuantity(Math.min(stock, quantity));
                cart.setComplete(false);
                cartService.saveCart(cart);
            }
        }

        return "Changes saved successfully";
    }

    @GetMapping("/updateCartDropdown")
    public ResponseEntity<Map<String, Object>> updateCartDropdown(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        List<Cart> userCarts = cartService.findAllCartByUsername(principal.getName());
        List<Product> productList = new ArrayList<>();
        totalQuantity = 0;
        total = 0;
        for (int i = 0; i < userCarts.size(); i++) {
            Product product = productService.getProductById(userCarts.get(i).getPid()).get();
            Product cartProduct = new Product();
            cartProduct.setPid(product.getPid());
            cartProduct.setName(product.getName());
            cartProduct.setPrice(product.getPrice());
            cartProduct.setImage(product.getImage());
            cartProduct.setColor(product.getColor());
            cartProduct.setSize(userCarts.get(i).getSize());
            cartProduct.setQuantity(userCarts.get(i).getQuantity());
            total += (product.getPrice() * userCarts.get(i).getQuantity());
            productList.add(cartProduct);
        }
        response.put("cartCount", userCarts.size());
        response.put("total", total);
        response.put("cart", productList);

        return ResponseEntity.ok(response);
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

    @GetMapping("/removeItem")
    public ResponseEntity<Map<String, Object>> cartItemRemove(@RequestParam int pid, @RequestParam String size, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        Cart existingCart = cartService.findExactlyCart(pid, principal.getName(), size);
        cartService.removeItem(existingCart.getCartId());
        return ResponseEntity.ok(response);
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
