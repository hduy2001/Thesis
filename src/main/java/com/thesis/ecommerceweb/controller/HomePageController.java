package com.thesis.ecommerceweb.controller;


import com.thesis.ecommerceweb.dto.UserDTO;
import com.thesis.ecommerceweb.global.ExcelWriter;
import com.thesis.ecommerceweb.global.GlobalData;
import com.thesis.ecommerceweb.model.*;
import com.thesis.ecommerceweb.recommenderSystem.DataFromPython;
import com.thesis.ecommerceweb.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class HomePageController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    StockService stockService;
    @Autowired
    OrderService orderService;
    @Autowired
    FeedbackService feedbackService;

    @GetMapping("/homePage")
    public String homePage(Model model, Principal principal, @Param("keyword") String keyword){
        if(principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }

        List<Product> womenList = productService.getWomenList();
        List<Product> MenList = productService.getMenList();
        model.addAttribute("womenList", womenList);
        model.addAttribute("menList", MenList);
        model.addAttribute("womenShoesList", productService.getShoesList("Women"));
        model.addAttribute("menShoesList", productService.getShoesList("Men"));
        model.addAttribute("accessoriesList", productService.getAccessoriesList());
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("keyword", keyword);
        return "web/HomePage";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Product> searchProducts(@RequestParam String keyword) {
        List<Product> searchResults = productService.searchProducts(keyword);
        return searchResults;
    }

    //ShopPage section:
    @GetMapping("/shopPage/{id}")
    public String shoesPage(Model model, @PathVariable int id, Principal principal, @Param("keyword") String keyword,
                            @RequestParam(name = "selectedBrands", required = false) String selectedBrands,
                            @RequestParam(name = "color", required = false) String color){
        if(principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }

        List<String> brandList = new ArrayList<>();

        if (selectedBrands != null) {
            String[] selectedArray = selectedBrands.split(",");
            brandList = Arrays.asList(selectedArray);
            model.addAttribute("selectedBrands", brandList);
        }

        if (color != null && !color.isEmpty()) {
            model.addAttribute("selectedColor", color);
        }

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByCategoryId(id, keyword, brandList, color));
        model.addAttribute("keyword", keyword);
        model.addAttribute("allColors", productService.getAllColors());
        model.addAttribute("brands", productService.getAllBrands());

        return "web/ShopPage";
    }

    @GetMapping("/shopPage/{gender}/{id}")
    public String shoesPageGender(Model model, @PathVariable String gender, @PathVariable int id, Principal principal, @Param("keyword") String keyword,
                                  @RequestParam(name = "selectedBrands", required = false) String selectedBrands,
                                  @RequestParam(name = "color", required = false) String color){
        if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }

        List<String> brandList = new ArrayList<>();

        if (selectedBrands != null) {
            String[] selectedArray = selectedBrands.split(",");
            brandList = Arrays.asList(selectedArray);
            model.addAttribute("selectedBrands", brandList);
        }

        if (color != null && !color.isEmpty()) {
            model.addAttribute("selectedColor", color);
        }
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByGender(gender, id, keyword, brandList, color));
        model.addAttribute("keyword", keyword);
        model.addAttribute("allColors", productService.getAllColors());
        model.addAttribute("brands", productService.getAllBrands());
        return "web/ShopPage";
    }

    @GetMapping("/shopPage/{gender}/{brand}/{id}")
    public String shoesPageBrand(Model model, @PathVariable String gender, @PathVariable String brand, @PathVariable int id, Principal principal,
                                 @Param("keyword") String keyword,
                                 @RequestParam(name = "selectedBrands", required = false) String selectedBrands,
                                 @RequestParam(name = "color", required = false) String color){
        if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }

        List<String> brandList = new ArrayList<>();

        if (selectedBrands != null) {
            String[] selectedArray = selectedBrands.split(",");
            brandList = Arrays.asList(selectedArray);
            model.addAttribute("selectedBrands", brandList);
        }

        if (color != null && !color.isEmpty()) {
            model.addAttribute("selectedColor", color);
        }

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByGenderAndBrand(gender, brand, id, keyword, brandList, color));
        model.addAttribute("keyword", keyword);
        model.addAttribute("allColors", productService.getAllColors());
        model.addAttribute("brands", productService.getAllBrands());
        return "web/ShopPage";
    }

    @GetMapping("/shopPageBrand/{brand}")
    public String shopPageBrand(Model model, @PathVariable String brand, Principal principal, @Param("keyword") String keyword,
                                @RequestParam(name = "selectedBrands", required = false) String selectedBrands,
                                @RequestParam(name = "color", required = false) String color){
        if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }

        List<String> brandList = new ArrayList<>();

        if (selectedBrands != null) {
            String[] selectedArray = selectedBrands.split(",");
            brandList = Arrays.asList(selectedArray);
            model.addAttribute("selectedBrands", brandList);
        }

        if (color != null && !color.isEmpty()) {
            model.addAttribute("selectedColor", color);
        }

        model.addAttribute("products", productService.getAllProductsByBrand(brand, keyword, brandList, color));
        model.addAttribute("keyword", keyword);
        model.addAttribute("allColors", productService.getAllColors());
        model.addAttribute("brands", productService.getAllBrands());
        return "web/ShopPage";
    }

    //Login Section:
    @GetMapping("/login")
    public String showLogin(){
        return "web/Login";
    }

    //Register Section:
    @GetMapping("/register")
    public String getUser() {
        return "web/Register";
    }

    @PostMapping("/register")
    public String postUserAdd(@ModelAttribute("USER") UserDTO userDTO, Model model, @RequestParam("username")String username, HttpServletRequest request) {
        if (username.equals(userService.findUserByUsername(username))) {
            model.addAttribute("ERROR", "Username already existed!");
            return "web/Register";
        }
        userDTO.setVerificationCode(UUID.randomUUID().toString());
        String path = request.getRequestURL().toString();
        path = path.replace(request.getServletPath(), "");
        userService.save(userDTO, path);
        return "web/Login";
    }

    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code) {
        userService.verifyAccount(code);
        return "web/Verification";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam("email") String email, HttpServletRequest request) {
        String path = request.getRequestURL().toString();
        path = path.replace(request.getServletPath(), "");
        userService.sendEmailGetPassword(email, path);
        return "redirect:/login";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@Param("username") String username, Model model) {
        model.addAttribute("username", username);
        return "web/NewPassword";
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam("username") String username, @RequestParam("confirmPassword") String confirmPassword) {
        userService.updatePassword(username, confirmPassword);
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String getUserDetails(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        model.addAttribute("USER", userService.findUserByUsername(principal.getName()));
        return "web/User";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("USER") UserDTO userDTO) {
        userService.updateUser(userDTO);
        return "redirect:/homePage";
    }

    @GetMapping("/trackingOrder")
    public String getTrackingOrder(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());

        List<Order> ordered = orderService.getAllOrderByUsername(principal.getName(), "Ordered");
        List<Order> delivering = orderService.getAllOrderByUsername(principal.getName(), "Delivering");
        List<Order> success = orderService.getAllOrderByUsername(principal.getName(), "Success");
        List<Order> canceled = orderService.getAllOrderByUsername(principal.getName(), "Canceled");

        model.addAttribute("user", userDetails);
        model.addAttribute("ordered", ordered);
        model.addAttribute("delivering", delivering);
        model.addAttribute("success", success);
        model.addAttribute("canceled", canceled);
        return "web/TrackingOrder";
    }

    @GetMapping("/getOrderedProduct")
    @ResponseBody
    public List<Product> getOrderedProduct(@RequestParam int oid) {
        List<Product> productList = new ArrayList<>();
        Order order = orderService.getOrderById(oid).get();
        List<Integer> idsList = GlobalData.extractUniqueIds(order.getDetail());
        productList.addAll(productService.getListProduct(idsList));
        return productList;
    }

    @PostMapping("/submitRating")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> submitRating(@RequestParam int pid, @RequestParam Double rating, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        feedbackService.addFeedback(pid, principal.getName(), rating, System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    //Product Section:
    @GetMapping("/product/{id}")
    public String showProduct(@PathVariable int id, Principal principal, Model model){
        DataFromPython dataFromPython = new DataFromPython();
        List<Integer> pidList = dataFromPython.getDataFromPython(principal.getName());
        List<Product> recommendProducts = new ArrayList<>();
        List<Feedback> allRatings = feedbackService.getAllFeedBack(id);

        if(principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }

        for (int i = 0; i < pidList.size(); i++) {
            recommendProducts.add(productService.getProductById(pidList.get(i)).get());
        }

        int ratingsPerPage = 5;

        List<List<Feedback>> groupedRatings = ListUtils.partition(allRatings, ratingsPerPage);

        model.addAttribute("product", productService.getProductById(id).get());
        model.addAttribute("recommendProducts", recommendProducts);
        model.addAttribute("ratings", allRatings);
        model.addAttribute("groupedRatings", groupedRatings);
        model.addAttribute("countRatings", feedbackService.countRating(id));
        model.addAttribute("sizes", stockService.getStockByPid(id));
        return "web/Product";
    }
}
