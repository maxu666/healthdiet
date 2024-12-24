package com.example.springboottemplate.controller;


import com.example.springboottemplate.POJO.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {
    @RequestMapping("/product/list")
    public String list(Model model) {
        Product p1 = new Product(1,"华为P10", BigDecimal.valueOf(2599),666);
        Product p2 = new Product(2,"Thinkpad T480",BigDecimal.valueOf(8999),1900);
        Product p3 = new Product(3,"耳机",BigDecimal.valueOf(30),388);
        Product p4 = new Product(4,"键盘",BigDecimal.valueOf(65),554);
        Product p5 = new Product(5,"鼠标",BigDecimal.valueOf(48),1268);
        List<Product> data = new ArrayList<>();
        data.add(p1);
        data.add(p2);
        data.add(p3);
        data.add(p4);
        data.add(p5);
        model.addAttribute("data",data);
        return "Product";
    }
}