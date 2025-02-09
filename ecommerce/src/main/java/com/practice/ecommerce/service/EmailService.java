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
        messagesSubject.put(EmailMessages.otpRequest, "Your One Time Password(OTP) for E-commerce Login");

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
        messages.put(EmailMessages.otpRequest,
                "Dear User, \r\n" +
                "Your One Time Password for the login into our website is: \r\n" +
                "This OTP is only valid for 10 minutes only, please do not share this with anyone. \r\n" +
                "It will be invalidated once you login. \r\n" +
                "Thank You \r\n");
    }

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException ex) {
            logger.error("Failed to send email: " + ex.getMessage());
        }
    }

    public void sendOTPMail(String to, EmailMessages type, String otp) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(messagesSubject.get(type));
        String content = new StringBuilder(messages.get(type))
                .insert(messages.get(type).indexOf(":") + 2, otp).toString();
        simpleMailMessage.setText(content);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException ex) {
            logger.error("Failed to send OTP email: " + ex.getMessage());
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
            logger.error("Failed to send WELCOME MimeMessage: " + ex.getMessage());
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
            logger.error("Failed to send MimeMessage: " + ex.getMessage());
        }
    }

    public void sendMailWithAttachment(String to, EmailMessages type, List<Order> orders, String refNo, String paymentId) {
        MimeMessage message = javaMailSender.createMimeMessage();
        generateReceipt(pathToReceipt, orders, to, refNo, paymentId);

        try {
            MimeMessageHelper attachmentMail = new MimeMessageHelper(message, true);

            attachmentMail.setTo(to);
            attachmentMail.setSubject(messagesSubject.get(type));
            attachmentMail.setText(messages.get(type), true);

            FileSystemResource file = new FileSystemResource(new File(pathToReceipt));
            attachmentMail.addAttachment("Receipt", file, Files.probeContentType(file.getFile().toPath()));

            javaMailSender.send(message);
        } catch (Exception ex) {
            logger.error("Failed to send MimeMessage with Attachment: " + ex.getMessage());
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
            logger.error("Failed to load file: " + ex.getMessage());
        }

        return sb.toString();
    }

    public void generateReceipt(String path, List<Order> orders, String user, String refNo, String paymentId) {
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
            topTable.addCell(new Cell().add(refNo).setBorder(Border.NO_BORDER));
            topTable.addCell(new Cell().add("Payment Id. :").setBorder(Border.NO_BORDER));
            topTable.addCell(new Cell().add(paymentId).setBorder(Border.NO_BORDER));
            topTable.addCell(new Cell().add("Date : ").setBorder(Border.NO_BORDER));
            topTable.addCell(new Cell().add(String.valueOf(new Date())).setBorder(Border.NO_BORDER));
            topTable.addCell(new Cell().add("Customer Identifier : ").setBorder(Border.NO_BORDER));
            topTable.addCell(new Cell().add(user).setBorder(Border.NO_BORDER));

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
                Integer currPrice = orders.get(i).getProduct().getCurrentPrice();
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
        } catch (Exception ex) {
            logger.error("Failed to load file: " + ex.getMessage());
        }
    }
}
