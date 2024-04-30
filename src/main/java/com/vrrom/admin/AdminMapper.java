package com.vrrom.admin;

import com.vrrom.application.calculator.Calculator;
import com.vrrom.application.dto.ApplicationRequest;
import com.vrrom.application.model.Application;
import com.vrrom.application.model.ApplicationStatus;
import com.vrrom.customer.Customer;
import com.vrrom.financialInfo.model.FinancialInfo;
import com.vrrom.vehicle.model.VehicleDetails;

import java.time.LocalDate;
import java.util.List;

public class AdminMapper {

    public static Admin toEntity( AdminRequest adminRequest) {
        Admin admin = new Admin();
        admin.setName(adminRequest.getName());
        admin.setSurname(adminRequest.getSurname());
        return  admin;
    }
}
