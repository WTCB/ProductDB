package be.pauwel.pi.product.services.impl;

import be.pauwel.pi.product.services.CompanyService;
import be.pauwel.pi.product.viewmodel.data.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Override
    public List<Company> getAllCompanies() {
        return Company.companyList;
    }
}




