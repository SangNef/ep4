package com.example.eproject4.service;

import com.example.eproject4.model.Voucher;
import com.example.eproject4.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> findById(int id) {
        return voucherRepository.findById(id);
    }

    public Voucher save(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    public void deleteById(int id) {
        voucherRepository.deleteById(id);
    }
}
