package com.practice.ecommerce.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.ProductDTO;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.service.AdminService;
import com.practice.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
//Demo Product format
// {
//    "name": "someName",
//    "basicPrice": 500,
//    "thumbnail": MultipartFile,
//    "category": "homedecore",
//    "stock": 100,
//    "currentPrice": 450,
//    "tags": "some search tags separated by comma"
//  }
    @PostMapping("/add/product") // checked
    public ResponseEntity<String> addProduct(@ModelAttribute ProductDTO productDTO) {
        int stock = productDTO.getStock();
        String base64 = encodeThumbnail(productDTO.getThumbnail());
        String extension = getMediaType(productDTO.getThumbnail());
        if (base64 == null) return ResponseEntity.badRequest().build();
        Product product = new Product(
                productDTO.getName(),
                productDTO.getBasicPrice(),
                productDTO.getCurrentPrice(),
                base64,
                stock,
                productDTO.getCategory(),
                stock - 10,
                extension
        );
        Product newProduct = adminService.addProduct(product, productDTO.getTags());
        if (newProduct != null) return new ResponseEntity<>("Product saved successfully!! " + "id: " + newProduct.getProductId() + "stock: " + newProduct.getName(), HttpStatus.OK);
        return new ResponseEntity<>("Product save unsuccessfully!!" + product, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/add/admin") // checked
    public ResponseEntity<User> addAdmin(@RequestBody Map<String, String> user) {
        User admin = adminService.addAdmin(user.get("user"), UserType.admin);
        if (admin !=  null) {
            logger.info("NEW ADMIN: " + admin.toString());
            return new ResponseEntity<>(admin, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/alter/price") // checked
    public String changePrice(@RequestBody Map<String, Integer> newPrice) {
        Integer setPrice = newPrice.get("currentPrice");
        if (adminService.changePrice(setPrice, newPrice.get("productId"))) return "Changed price to " + setPrice;
        return "Failed to alter price for product: " + newPrice.get("productId");
    }

    @PostMapping("/upload")
    public String addImageOfProduct(@RequestParam Integer id, @RequestBody MultipartFile file) {
        if (file.isEmpty()) return "NOT FILE SELECTED";
        logger.info("FILE RECEIVED for STORAGE = {}", file.getName());
        return adminService.addToFirebaseStorage(id, file);
    }

    private String encodeThumbnail(MultipartFile file) {
        String base64 = null;
        try {
            base64 = Base64.getEncoder().encodeToString(file.getBytes());
        } catch (IOException ex) {
            logger.error("ERROR encoding file: {}", ex.getMessage());
        }
        return base64;
    }

    private String getMediaType(MultipartFile file) {
        String name = file.getResource().getFilename();
        int i = name.indexOf('.');
        System.out.println("name: " + name);
        StringBuilder sb = new StringBuilder();
        for (int j=i+1;j<name.length();j++) {
            sb.append(name.charAt(j));
        }
        return sb.toString().toUpperCase();
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Integer id) {
        Product product = productService.getProduct(id);

        if (product == null || product.getThumbnail() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Base64.getDecoder().decode(product.getThumbnail());
        return ResponseEntity.ok()
                             .contentType(MediaType.IMAGE_JPEG) // Adjust based on image type
                             .body(imageBytes);
    }
}
