package com.practice.ecommerce.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.practice.ecommerce.model.Enums.EmailMessages;
import com.practice.ecommerce.model.Enums.ProductCategory;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.Order;
import com.practice.ecommerce.model.Stock;
import com.practice.ecommerce.model.Product;
import com.practice.ecommerce.model.User;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private static final String pathToReceipt = "src/main/resources/static/Receipt.pdf";

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public static Map<EmailMessages, String> messages = new HashMap<>();
    public static Map<EmailMessages, String> messagesSubject = new HashMap<>();

    public EmailService() {
        messagesSubject.put(EmailMessages.welcomeMessage, "Welcome to E-Commerce!! \uD83C\uDF89 \uD83C\uDF89");
        messagesSubject.put(EmailMessages.orderPlaced, "We have received your order!!");
        messagesSubject.put(EmailMessages.productStockOver, "Regarding stock for you product with ProductID:  ");

        messages.put(EmailMessages.welcomeMessage, readFile("src/main/resources/templates/welcomeMessage.txt"));
        messages.put(EmailMessages.orderPlaced,
                "<p>Thank You Dear Customer!!</p>" +
                "<span>Your order has been received by us.</span>" +
                "<p>To check the status of your order, you can visit:</p>" +
                "<a href=\"http://localhost:3000/orders\" target=\"_blank\">http://localhost:3000/orders</a>");
        messages.put(EmailMessages.productStockOver,
                "<p>Dear Admin, </p>" +
                "<span>Your stock for product with Product ID: " + " has been finished.</span>" +
                "<p>Please replenish the stock as soon as possible</p>");
    }

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException ex) {
            logger.info("Failed to send email: " + ex.getMessage());
        }
    }

    public void sendWelcomeMail(String to, EmailMessages type) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper htmlMail = new MimeMessageHelper(message, true);
            htmlMail.setTo(to);
            htmlMail.setSubject(messagesSubject.get(type));
            htmlMail.setText(messages.get(type), true);

            javaMailSender.send(message);
        } catch (Exception ex) {
            logger.info("Failed to send MimeMessage: " + ex.getMessage());
        }
    }

    public void sendHtmlMail(String to, EmailMessages type, Product product) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper htmlMail = new MimeMessageHelper(message, true);
            htmlMail.setTo(to);

            String str = messagesSubject.get(type);
            String subject = new StringBuilder(str).insert(str.length()-1, product.getProductId()).toString();
            htmlMail.setSubject(subject);

            String text = messages.get(type);
            String content = new StringBuilder(text).insert(text.indexOf("ID:")+4, product.getProductId()).toString();
            htmlMail.setText(content);

            htmlMail.setText(content, true);

            javaMailSender.send(message);
        } catch (Exception ex) {
            logger.info("Failed to send MimeMessage: " + ex.getMessage());
        }
    }

    public void sendMailWithAttachment(String to, EmailMessages type, List<Order> orders) {
        MimeMessage message = javaMailSender.createMimeMessage();
        generateReceipt(pathToReceipt, orders);

        try {
            MimeMessageHelper attachmentMail = new MimeMessageHelper(message, true);

            attachmentMail.setTo(to);
            attachmentMail.setSubject(messagesSubject.get(type));
            attachmentMail.setText(messages.get(type));

            FileSystemResource file = new FileSystemResource(new File(pathToReceipt));
            attachmentMail.addAttachment("Receipt", file, Files.probeContentType(file.getFile().toPath()));

            javaMailSender.send(message);
        } catch (Exception ex) {
            logger.info("Failed to send MimeMessage with Attachment: " + ex.getMessage());
        }
    }

    public String readFile(String path) {
        if (path == null) return null;
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(path));) {
            String line;
            while ((line=br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ex) {
            System.out.println("Failed to load file: " + ex.getMessage());
        }

        return sb.toString();
    }

    public void generateReceipt(String path, List<Order> orders) {
        User user = new User("tempUser", UserType.customer);
        Product product = new Product("tempProduct", 500, 300, "xyxyxyxxyy", 10, ProductCategory.aesthtic);
        product.setProductId(20);
        product.setVirtualStock(new Stock(product, 5));
        Integer total = 0;

        try (PdfDocument pdfDoc = new PdfDocument(new PdfWriter(path));
             Document document = new Document(pdfDoc);) {

            Paragraph branding = new Paragraph("E-Commerce").setFontSize(16).setTextAlignment(TextAlignment.CENTER);
            document.add(branding);
            Paragraph title = new Paragraph("Money Receipt").setFontSize(14).setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            Table topTable = new Table(new float[] {1, 1})
                    .setBorder(Border.NO_BORDER).setFontColor(Color.GRAY).setFontSize(12)
                    .setWidth(UnitValue.createPercentValue(100));
            topTable.addCell(new Cell().add("Ref. Id. :").setBorder(Border.NO_BORDER));
            topTable.addCell(new Cell().add(UUID.randomUUID().toString()).setBorder(Border.NO_BORDER));
            topTable.addCell(new Cell().add("Date : ").setBorder(Border.NO_BORDER));
            topTable.addCell(new Cell().add(String.valueOf(new Date())).setBorder(Border.NO_BORDER));

            document.add(topTable);

            Table mainTable = new Table(new float[] {1, 2, 4, 1})
                    .setWidth(UnitValue.createPercentValue(100))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            mainTable.addHeaderCell("S. No.");
            mainTable.addHeaderCell("Order Id");
            mainTable.addHeaderCell("Product");
            mainTable.addHeaderCell("Amount");

            for (int i=0; i<orders.size(); i++) {
                mainTable.addCell(new Cell().add(String.valueOf(i+1)));
                mainTable.addCell(new Cell().add(orders.get(i).getOrderId().toString()));
                mainTable.addCell(new Cell().add(orders.get(i).getProduct().getName()));
                Integer currPrice = orders.get(i).getProduct().getVirtualStock().getVirtualStock();
                mainTable.addCell(new Cell().add(currPrice.toString()));
                total += currPrice;
            }

            document.add(mainTable);

            Table totalAmt = new Table(new float[] {1, 1})
                    .setWidth(UnitValue.createPercentValue(100))
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);
            totalAmt.addCell(new Cell().add("Total Amount "));
            totalAmt.addCell(new Cell().add(total.toString()));
            mainTable.addCell(new Cell().add(totalAmt));

            document.add(totalAmt);

            Paragraph lastLine = new Paragraph("This receipt has been generated by a computer")
                    .setFontSize(9)
                    .setFontColor(Color.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setUnderline()
                    .setFixedPosition(0f, 15f, UnitValue.createPercentValue(100));

            document.add(lastLine);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
