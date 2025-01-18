package com.practice.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.practice.ecommerce.model.Cart;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    public Cart getCart(String identifier) {
        Optional<Cart> cart = cartRepository.findById(identifier);
        return cart.orElse(null);
    }

    public Product addToCart(String identifier, Integer productId) {
        Product product = productService.getProduct(productId);
        if (product == null) return null;
        Cart cart = getCart(identifier);

        if (cart != null) {
            cart.getProducts().add(product);
            cartRepository.save(cart);
        } else {
            Cart newCustomer = new Cart(identifier);
            newCustomer.getProducts().add(product);
            cartRepository.save(newCustomer);
        }
        return product;
    }

    public boolean deleteItemFromCart(String identifier, Integer productId) {
        List<Product> products = getCart(identifier).getProducts();
        if (products == null || products.isEmpty()) {
            return false;
        }
        Cart cart = new Cart(identifier);
        for (Product pr : products) {
            if (pr.getProductId() != productId) {
                cart.getProducts().add(pr);
            }
        }
        cartRepository.save(cart);
        return true;
    }
}
