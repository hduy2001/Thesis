package com.thesis.ecommerceweb.controller;

import com.thesis.ecommerceweb.dto.ProductDTO;
import com.thesis.ecommerceweb.dto.UserDTO;
import com.thesis.ecommerceweb.global.ExcelWriter;
import com.thesis.ecommerceweb.global.GlobalData;
import com.thesis.ecommerceweb.model.Category;
import com.thesis.ecommerceweb.model.Order;
import com.thesis.ecommerceweb.model.Product;
import com.thesis.ecommerceweb.model.User;
import com.thesis.ecommerceweb.service.CategoryService;
import com.thesis.ecommerceweb.service.OrderService;
import com.thesis.ecommerceweb.service.ProductService;
import com.thesis.ecommerceweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/products";
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;

    @GetMapping("/admin")
    public String adminHome(Principal principal, Model model) throws IOException, ParseException {
        ExcelWriter excelWriter = new ExcelWriter();
        User user = userService.findUserByUsername(principal.getName());
        List<Product> productList = productService.getListProduct(excelWriter.getTop10Pid());

        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("dataValues", excelWriter.getOrdersMonthly());
        model.addAttribute("totalOrders", excelWriter.countOrders());
        model.addAttribute("total", excelWriter.getTotalIncomes(excelWriter.getOrdersMonthly()));
        model.addAttribute("totalUser", userService.countAllUser());
        model.addAttribute("productList", productList);
        return "admin/Dashboard";
    }

    //Category Section
    @GetMapping("/admin/categories")
    public String getCat(Principal principal, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("category", new Category());
        return "admin/Categories";
    }

    @PostMapping("/admin/categories/add")
    public String postCatAdd(@ModelAttribute("category") Category category, Principal principal, Model model) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCat(@PathVariable int id) {
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCat(@PathVariable int id, Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("categories", categoryService.getAllCategory());
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            model.addAttribute("updatedCategoryId", id);
            return "admin/CategoryEdit";
        }
        return "404";
    }

    //Product Section
    @GetMapping("/admin/products")
    public String products(Principal principal, Model model){
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/Products";
    }

    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("product") ProductDTO productDTO,
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imgName") String imgName) throws IOException {
        String gender = productDTO.getGender().substring(0, 1).toUpperCase() + productDTO.getGender().substring(1);
        Product product = new Product();
        product.setPid(productDTO.getPid());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCid()).get());
        product.setPrice(productDTO.getPrice());
        product.setBrand(productDTO.getBrand());
        product.setColor(productDTO.getColor());
        product.setGender(gender);
        product.setDescription(productDTO.getDescription());
        String imageUUID;
        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            imageUUID = imgName;
        }
        product.setImage(imageUUID);
        product.setQuantity(productDTO.getQuantity());
        product.setSize(productDTO.getSize());
        product.setRating(productDTO.getRating());
        product.setRatingCount(productDTO.getRatingCount());
        productService.addProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable int id, Principal principal, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        Product product = productService.getProductById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setPid(product.getPid());
        productDTO.setName(product.getName());
        productDTO.setCid(product.getCategory().getCid());
        productDTO.setPrice(product.getPrice());
        productDTO.setBrand(product.getBrand());
        productDTO.setColor(product.getColor());
        productDTO.setGender(product.getGender());
        productDTO.setDescription(product.getDescription());
        productDTO.setImage(product.getImage());

        model.addAttribute("updatedProductId", id);
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("product", productDTO);
        model.addAttribute("products", productService.getAllProduct());
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        return "admin/ProductEdit";
    }

    //Orders Section
    @GetMapping("/admin/orders")
    public String getOrders(Principal principal, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        model.addAttribute("orders", orderService.getAllOrder());
        return "admin/Orders";
    }

    @GetMapping("/admin/orders/delete/{id}")
    public String deleteOrder(@PathVariable int id, Order order) {
        Optional<Order> currentOrder = orderService.getOrderById(id);
        if (currentOrder.get().getStatus().equals("Ordered")) {
            order = currentOrder.get();
            order.setStatus("Canceled");
            orderService.addOrder(order);
            return "redirect:/admin/orders";
        }
        orderService.removeOrderById(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/orders/update/{id}")
    public String updateOrder(@PathVariable int id, Order order) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(currentDate);
        ExcelWriter excelWriter = new ExcelWriter();
        Optional<Order> currentOrder = orderService.getOrderById(id);
        User user = userService.findUserByUsername(currentOrder.get().getUsername());
        List<Integer> idList = GlobalData.extractIds(currentOrder.get().getDetail());
        List<Integer> quantityList = GlobalData.extractQuantities(currentOrder.get().getDetail());
        List<Product> productList = productService.getListProduct(idList);
        if (currentOrder.isPresent()) {
            if (currentOrder.get().getStatus().equals("Delivering")) {
                order = currentOrder.get();
                order.setStatus("Success");
                orderService.addOrder(order);
                for (int i = 0; i < idList.size(); i++) {
                    excelWriter.appendOrders(user.getUsername(), productList.get(i).getPid(), productList.get(i).getName(),quantityList.get(i),formattedDate,productList.get(i).getPrice(), user.getAddress());
                }
                return "redirect:/admin/orders";
            }
            else if (currentOrder.get().getStatus().equals("Canceled")) {
                order = currentOrder.get();
                order.setStatus("Ordered");
                orderService.addOrder(order);
                return "redirect:/admin/orders";
            }
            order = currentOrder.get();
            order.setStatus("Delivering");
            orderService.addOrder(order);
            return "redirect:/admin/orders";
        }
        return "404";
    }

    @GetMapping("/admin/blank")
    public String blankPage(Principal principal, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        return "admin/Blank";
    }

    @GetMapping("/admin/invoice/{id}")
    public String getInvoice(@PathVariable int id, Principal principal, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        int subTotal = 0;
        Optional<Order> currentOrder = orderService.getOrderById(id);
        List<Integer> quantities = GlobalData.extractQuantities(currentOrder.get().getDetail());
        List<Integer> ids = GlobalData.extractIds(currentOrder.get().getDetail());
        List<String> sizes = GlobalData.extractSizes(currentOrder.get().getDetail());
        List<Product> productList = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            Product product = productService.getProductById(ids.get(i)).get();
            product.setQuantity(quantities.get(i));
            product.setSize(sizes.get(i));
            subTotal += product.getQuantity() * product.getPrice();
            productList.add(product);
        }

        model.addAttribute("user", userService.findUserByUsername(currentOrder.get().getUsername()));
        model.addAttribute("USER", user);
        model.addAttribute("currentOrder",currentOrder.get());
        model.addAttribute("currentDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("nextMonth", LocalDateTime.now().plusWeeks(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("productList", productList);
        model.addAttribute("subTotal", subTotal);
        model.addAttribute("shipping", currentOrder.get().getTotalPrice() - subTotal);
        model.addAttribute("total", currentOrder.get().getTotalPrice());
        model.addAttribute("name", principal.getName());
        return "admin/Invoice";
    }

    @GetMapping("/admin/addManager")
    public String getManagerInfo(Principal principal, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("name", principal.getName());
        return "admin/AddManager";
    }

    @PostMapping("/getManager")
    public String postUserAdd(@ModelAttribute("USER") UserDTO userDTO, Model model, @RequestParam("username")String username) {
        if (username.equals(userService.findUserByUsername(username))) {
            model.addAttribute("ERROR", "Username already existed!");
            return "admin/AddManager";
        }
        userService.saveManager(userDTO);
        return "redirect:/admin";
    }

    @GetMapping("/admin/profile")
    public String getProfile(Principal principal, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("USER", user);
        model.addAttribute("name", principal.getName());
        return "admin/Profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute("USER") UserDTO userDTO) {
        userService.updateUser(userDTO);
        return "redirect:/admin";
    }
}
