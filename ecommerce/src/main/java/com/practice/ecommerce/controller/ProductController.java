package com.practice.ecommerce.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Image;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.Review;
import com.practice.ecommerce.service.ProductService;
import com.practice.ecommerce.service.redis.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisCacheService cache;

    @GetMapping("/getProduct") // checked
    public ResponseEntity<Product> getProduct(@RequestParam Integer id) {
        Product product = productService.getProduct(id);
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/getProduct/{category}") // checked
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable ProductCategory category) {
        List<Product> product = productService.getProductByCategory(category);
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/getPrice") // checked
    public ResponseEntity<Integer> getPrice(@RequestParam Integer id) {
        Integer price = productService.getPriceOnly(id);
        if (price == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(price, HttpStatus.OK);
    }

    @PostMapping("/search") // checked
    public ResponseEntity<List<Product>> getSearchedProducts(@RequestBody Map<String, String> query) {
        List<Product> products = productService.getSearchProducts(query.get("query"));
        if (products == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/random") // TO-DO: implement browser side caching
    public ResponseEntity<List<Product>> getRandomProducts () {
        List<Product> products = productService.getRandomProduct();
        if (products.isEmpty()) return ResponseEntity.notFound().build();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/images/{productId}")
    public ResponseEntity<List<String>> getImageURLsForProduct(@PathVariable Integer productId) {
        List<Integer> imageIds = productService.getImagesForProduct(productId);
        if (imageIds.isEmpty()) return ResponseEntity.notFound().build();
        String api = "public/image/%d";
        List<String> apiStrings = imageIds.stream().map(id ->
                String.format(api, id)).toList();
        return new ResponseEntity<>(apiStrings, HttpStatus.OK);
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<byte[]> getImage(@RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String eTag, @PathVariable Integer imageId) {
        String key = Keys.key(Keys.ETAG, imageId);
        String item = cache.getCache(key, new TypeReference<String>() {});
        if (eTag != null && item != null) {
            String actualETag = eTag.replace("\"", "");
            if (actualETag.equals(item)) return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        Image image = productService.getImagesById(imageId);
        if (image == null) return ResponseEntity.notFound().build();
        String entityTag = image.getImageId() + ":" + image.getProductId() + image.getType();
        cache.setCache(key, entityTag, 1440);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/" + image.getType()))
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)
                        .cachePublic() // can be stored in a public cache
                )
                .contentLength(image.getImage().length)
                .eTag(entityTag)
                .body(image.getImage());
    }

    @GetMapping("review/{productId}")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Integer productId) {
        List<Review> reviews = productService.getReviews(productId);
        if (reviews != null) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("review/new/{productId}")
    public ResponseEntity<List<Review>> getNewReviews(@PathVariable Integer productId, @RequestBody Map<String, List<String>> Ids) {
        List<String> reviewIds = Ids.get("reviewIds");
        List<Review> reviews = productService.getNewReviews(productId, reviewIds);
        if (reviews != null) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
