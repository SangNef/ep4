package com.example.eproject4.controller;

import com.example.eproject4.model.Voucher;
import com.example.eproject4.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public String index(Model model) {
        List<Voucher> vouchers = voucherService.findAll();
        model.addAttribute("vouchers", vouchers);
        return "admin/voucher/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("voucher", new Voucher());
        return "admin/voucher/create";
    }

    @PostMapping
    public String create(@ModelAttribute Voucher voucher) {
        voucherService.save(voucher);
        return "redirect:/admin/voucher";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        Voucher voucher = voucherService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid voucher ID: " + id));
        model.addAttribute("voucher", voucher);
        return "admin/voucher/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable int id, @ModelAttribute Voucher voucher) {
        voucher.setId(id);
        voucherService.save(voucher);
        return "redirect:/admin/voucher";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        voucherService.deleteById(id);
        return "redirect:/admin/voucher";
    }
}
