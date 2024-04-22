package com.example.citycare.util;

import com.example.citycare.model.ReportModel;

import java.util.List;

public interface AllReportsCallback {
    void onSuccess(List<ReportModel> reports);
    void onError(String errorMessage);
}

