package com.cafe.cafeshopmanagement.dao;

import java.util.Map;

public interface ReportDAO {
    Map<String, Integer> getRevenueLast7Days();
    Map<String, Integer> getTopSellingProducts();
    int getTodayRevenue();
}