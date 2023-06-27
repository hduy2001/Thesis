package com.thesis.ecommerceweb.controller;

import com.thesis.ecommerceweb.dto.ProductDTO;
import com.thesis.ecommerceweb.model.Category;
import com.thesis.ecommerceweb.model.Order;
import com.thesis.ecommerceweb.model.Product;
import com.thesis.ecommerceweb.service.CategoryService;
import com.thesis.ecommerceweb.service.OrderService;
import com.thesis.ecommerceweb.service.ProductService;
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
import java.util.Optional;

@Controller
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/products";
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @Autowired
    OrderService orderService;

    @GetMapping("/admin")
    public String adminHome() {
        return "admin/adminHome";
    }

    //Category Section
    @GetMapping("/admin/categories")
    public String getCat(Model model) {
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/categories";
    }

    @GetMapping("/admin/categories/add")
    public String getCatAdd(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String postCatAdd(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCat(@PathVariable int id) {
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCat(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "admin/categoriesAdd";
        }
        return "404";
    }

    //Product Section
    @GetMapping("/admin/products")
    public String products(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "admin/products";
    }

    @GetMapping("/admin/products/add")
    public String productAddGet(Model model){
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String productAddPost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imgName") String imgName) throws IOException {
        Product product = new Product();
        product.setPid(productDTO.getPid());
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCid()).get());
        product.setPrice(productDTO.getPrice());
        product.setBrand(productDTO.getBrand());
        product.setColor(productDTO.getColor());
        product.setGender(productDTO.getGender());
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
        productService.addProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.removeProductById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable int id, Model model) {
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

        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("productDTO", productDTO);
        return "admin/productsAdd";
    }

    //Orders Section
    @GetMapping("/admin/orders")
    public String getOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrder());
        return "admin/orders";
    }

    @GetMapping("/admin/orders/delete/{id}")
    public String deleteOrder(@PathVariable int id) {
        orderService.removeOrderById(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/orders/update/{id}")
    public String updateOrder(@PathVariable int id, Model model, Order order) {
        Optional<Order> currentOrder = orderService.getOrderById(id);
        if (currentOrder.isPresent()) {
            order = currentOrder.get();
            order.setStatus("Delivering");
            orderService.addOrder(order);
            return "redirect:/admin/orders";
        }
        return "404";
    }
}
