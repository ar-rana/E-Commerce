package com.practice.ecommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.ecommerce.model.Enums.DeliveryStatus;
import com.practice.ecommerce.model.Enums.EmailMessages;
import com.practice.ecommerce.model.Enums.Keys;
import com.practice.ecommerce.model.Enums.ListType;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.PaymentDetails;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.repository.OrderRepository;
import com.practice.ecommerce.repository.PaymentsRepository;
import com.practice.ecommerce.service.redis.Publisher;
import com.practice.ecommerce.service.redis.RedisCacheService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private SavedProductsService savedProductsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private RedisCacheService cache;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private Publisher publisher;

    @Value("${app.payments.Id}")
    private String paymentsId;

    @Value("${app.payments.secret}")
    private String paymentsSecret;

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    public Map<String, String> newOrder(String identifier, String address, String contact, String name) {
        User user = userService.getUserObject(identifier);
        if (user == null) return null;
        List<Product> cart = savedProductsService.getListItems(ListType.CART, identifier);
        if (cart == null || cart.isEmpty()) return null;
        Integer totalPrice = productService.getTotalPrice(cart.stream().map(item -> item.getProductId()).toList());

        logger.info("Total: {} + for User: {}", totalPrice, identifier);
        String referenceId = UUID.randomUUID().toString();

        List<Integer> orderIds = new ArrayList<>();
        for (Product product: cart) {
            Order order = new Order(identifier, DeliveryStatus.pending, product, address, contact, name, referenceId);
            orderIds.add(orderRepository.save(order).getOrderId());
        }
        String rzpOrderId;
        try {
            RazorpayClient razorpay = new RazorpayClient(paymentsId, paymentsSecret);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", totalPrice*100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", referenceId);

            com.razorpay.Order rzpOrder = razorpay.orders.create(orderRequest);
            rzpOrderId = rzpOrder.get("id");
        } catch (RazorpayException e) {
            logger.error("PAYMENT FAILED FOR Customer: {}", user.getIdentifier());
            orderRepository.deleteAllById(orderIds);
            return null;
        }
        cache.deleteCache(Keys.key(Keys.ORDER, identifier));

        Map<String, String> map = new HashMap<>();
        map.put("id", rzpOrderId);
        map.put("total", String.valueOf(totalPrice*100));
        map.put("ref", referenceId);

        return map;
    }

    public String successHandler(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature, String referenceId, String identifier) {
        ObjectMapper mapper = new ObjectMapper();
        String key = Keys.key(Keys.ORDER, identifier);
        List<Order> items = cache.getCache(key, new TypeReference<List<Order>>() {});
        List<Product> cart = savedProductsService.getListItems(ListType.CART, identifier);
        List<Integer> cartIds = cart.stream().map(p -> p.getProductId()).toList();
        List<Order> orders = orderRepository.findOrderForUser(identifier, cartIds);

        if (items != null) {
            items.addAll(orders);
            cache.setCache(key, items, 15);
        }

        PaymentDetails paymentDetails = new PaymentDetails(razorpayPaymentId, razorpayOrderId, razorpaySignature, referenceId, orders);
        paymentsRepository.save(paymentDetails);

        Map<String, String> map = new HashMap<>();
        String orderJson;
        try {
            orderJson = mapper.writeValueAsString(orders);
            map.put("referenceId", referenceId);
            map.put("orders", orderJson);
            map.put("paymentId", razorpayPaymentId);
            map.put("to", identifier);
            map.put("type", EmailMessages.orderPlaced.name());
            publisher.publishToStream(map);
        } catch (JsonProcessingException e) {
            logger.error("FAILED TO PARSE OBJECT List<Order> at: {}", OrderService.class);
        }

        for (int prodId: cartIds) {
            productService.adjustStock(-1, prodId);
        }
        savedProductsService.clearCart(identifier);
        return "Order Placed Successfully!!";
    }

    public List<Order> getOrders(String identifier) { // cached
        String key = Keys.key(Keys.ORDER, identifier);
        List<Order> item = cache.getCache(key, new TypeReference<List<Order>>() {});
        if (item != null) {
            logger.info("Item from cache ORDERS - key: {}", key);
            return item;
        }
        List<Order> order = orderRepository.findByUserIdentifier(identifier);
        if (!order.isEmpty()) {
            cache.setCache(key, order, 15);
            return order;
        }
        return null;
    }

    public Integer getTotal(List<Integer> productIds) {
        return productService.getTotalPrice(productIds);
    }

    public Order getOrderById(Integer orderId) { // cached
        String key = Keys.key(Keys.ORDER, orderId);
        Order item = cache.getCache(key, Order.class);
        if (item != null) {
            logger.info("Item from cache ORDER - key: {}", key);
            return item;
        }
        com.practice.ecommerce.model.Order order = orderRepository.findById(orderId).orElse(null);
        cache.setCache(key, order, 10);
        return null;
    }

    public boolean addReview(Integer productId, String identifier, String content, Integer rating) {
        return reviewService.addReview(productId, identifier, content, rating);
    }

    public PaymentDetails getPaymentByRef(String refNo) {
        return paymentsRepository.findByReferenceNumber(refNo).orElse(null);
    }

    public PaymentDetails getPaymentById(String paymentId) {
        return paymentsRepository.findById(paymentId).orElse(null);
    }
}
