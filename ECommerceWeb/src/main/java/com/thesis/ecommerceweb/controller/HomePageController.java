package com.thesis.ecommerceweb.controller;


import com.thesis.ecommerceweb.global.GlobalData;
import com.thesis.ecommerceweb.model.Category;
import com.thesis.ecommerceweb.model.Product;
import com.thesis.ecommerceweb.model.User;
import com.thesis.ecommerceweb.service.CategoryService;
import com.thesis.ecommerceweb.service.ProductService;
import com.thesis.ecommerceweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class HomePageController {
    private String rememberUser;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @GetMapping("/homePage")
    public String homePage(Model model){
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
    public String shoesPage(Model model, @PathVariable int id, HttpSession session){
        if (session.getAttribute("USERNAME") != null) {
            int total = 0;
            for (Product product : GlobalData.cart) {
                total += product.getPrice() * product.getQuantity();
            }
            session.setAttribute("USERNAME", rememberUser);
            model.addAttribute("cart", GlobalData.cart);
            model.addAttribute("cartCount", GlobalData.cart.size());
            model.addAttribute("total", total);
            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("products", productService.getAllProductsByCategoryId(id));
            return "web/ShopPage";
        }
        return "redirect:/login";
    }

    @GetMapping("/shopPage/{gender}/{id}")
    public String shoesPageGender(Model model, @PathVariable String gender, @PathVariable int id, HttpSession session){
        if (session.getAttribute("USERNAME") != null) {
            session.setAttribute("USERNAME", rememberUser);
            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("products", productService.getAllProductsByGender(gender, id));
            return "web/ShopPage";
        }
        return "redirect:/login";
    }

    @GetMapping("/shopPage/{gender}/{brand}/{id}")
    public String shoesPageBrand(Model model, @PathVariable String gender, @PathVariable String brand, @PathVariable int id, HttpSession session){
        if (session.getAttribute("USERNAME") != null) {
            session.setAttribute("USERNAME", rememberUser);
            model.addAttribute("categories", categoryService.getAllCategory());
            model.addAttribute("products", productService.getAllProductsByGenderAndBrand(gender, brand, id));
            return "web/ShopPage";
        }
        return "redirect:/login";
    }

    //Login Section:
    @RequestMapping("/login")
    public String showLogin(){
        return "web/Login";
    }

    @PostMapping("/checkLogin")
    public String checkLogin(Model model, @RequestParam("username")String username, @RequestParam("password")String password, HttpSession session){
        if (username.equals("admin") && password.equals("admin")) {
            return "redirect:/admin";
        }

        if (userService.checkLogin(username, password)) {
            rememberUser = username;
            GlobalData.RememberUser = username;
            session.setAttribute("USERNAME", username);
            model.addAttribute("USERS", userService.findAll());
            return "redirect:/homePage";
        }
        model.addAttribute("ERROR", "Username or password not exist");
        return "web/Login";
    }

    //Logout Section
    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute("USERNAME");
        GlobalData.cart.clear();
        return "redirect:/login";
    }

    //Register Section:
    @GetMapping("/register")
    public String getUser() {
        return "web/Register";
    }

    @PostMapping("/register")
    public String postUserAdd(@ModelAttribute("USER") User user, Model model, @RequestParam("username")String username) {
        if (userService.checkRegister(username)) {
            user.setIsConfirm(0);
            userService.save(user);
            return "redirect:/login";
        }
        model.addAttribute("ERROR", "Username already existed!");
        return "web/Register";
    }

    @GetMapping("/user/{username}")
    public String getUserDetails(@PathVariable String username, Model model) {
        Optional<User> user = userService.findById(username);
        if (user.isPresent()) {
            model.addAttribute("USER", user.get());
        }else {
            model.addAttribute("USER", new User());
        }
        return "web/User";
    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute("USER") User user, Model model) {
        userService.save(user);
        return "redirect:/homePage";
    }
}
