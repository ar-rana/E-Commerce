package com.practice.ecommerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Stock;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.repository.StockRepository;
import com.practice.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private SearchService searchService;

    public Product getProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public List<Product> getProducts(List<Integer> ids) {
        return productRepository.findAllById(ids);
    }

    @Deprecated
    public Stock getVirtualStock(Integer id) {
        Optional<Stock> stock = stockRepository.findById(id);
        return  stock.orElse(null);
    }

    public Integer getPriceOnly(Integer id) {
        Optional<Integer> price = productRepository.findCurrentPriceById(id);
        return  price.orElse(null);
    }

    public Integer getTotalPrice(List<Integer> ids) {
        List<Integer> prices = productRepository.findAllByIdAndGetCurrentPrice(ids);
        Integer total = 0;
        for (Integer price: prices) {
            total += price;
        }
        return total;
    }

    public List<Product> getProductByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getSearchProducts(String search) {
        List<Integer> productIds = searchService.searchProduct(Stream.of(search.trim().split(" "))
                                                                     .map(val -> val.trim().toLowerCase())
                                                                     .collect(Collectors.toList())
        );
        if (productIds.isEmpty()) return null;
        return getProducts(productIds);
    }

    public Product updateProductStock(Product product) {
        return productRepository.save(product);
    }
}
