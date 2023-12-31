package com.thesis.ecommerceweb.controller;


import com.thesis.ecommerceweb.dto.UserDTO;
import com.thesis.ecommerceweb.global.GlobalData;
import com.thesis.ecommerceweb.model.Category;
import com.thesis.ecommerceweb.model.Product;
import com.thesis.ecommerceweb.model.User;
import com.thesis.ecommerceweb.service.CategoryService;
import com.thesis.ecommerceweb.service.ProductService;
import com.thesis.ecommerceweb.service.StockService;
import com.thesis.ecommerceweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/homePage")
    public String homePage(Model model, Principal principal){
        if(principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }
        model.addAttribute("cart", GlobalData.cart);
        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPrice).sum());
        return "web/HomePage";
    }

    @GetMapping("/homePageAfter")
    public String homePageAfter(Model model){
        GlobalData.cart.clear();
        return "web/HomePage";
    }

    //ShopPage section:
    @GetMapping("/shopPage/{id}")
    public String shoesPage(Model model, @PathVariable int id, Principal principal){
        if(principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByCategoryId(id));
        return "web/ShopPage";
    }

    @GetMapping("/shopPage/{gender}/{id}")
    public String shoesPageGender(Model model, @PathVariable String gender, @PathVariable int id, Principal principal){
        if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByGender(gender, id));
        return "web/ShopPage";
    }

    @GetMapping("/shopPage/{gender}/{brand}/{id}")
    public String shoesPageBrand(Model model, @PathVariable String gender, @PathVariable String brand, @PathVariable int id, Principal principal){
        if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByGenderAndBrand(gender, brand, id));
        return "web/ShopPage";
    }

    @GetMapping("/shopPageBrand/{brand}")
    public String shopPageBrand(Model model, @PathVariable String brand, Principal principal){
        if (principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }
        model.addAttribute("products", productService.getAllProductsByBrand(brand));
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

    //Login Section:
    @GetMapping("/product/{id}")
    public String showProduct(@PathVariable int id, Principal principal, Model model){
        if(principal != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
            model.addAttribute("user", userDetails);
        }
        model.addAttribute("product", productService.getProductById(id).get());
        model.addAttribute("sizes", stockService.getStockByPid(id));
        return "web/Product";
    }
}
