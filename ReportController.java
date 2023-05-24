package com.demo.report;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
public class ReportController {
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws JRException, IOException {

        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=jsonTemplate.pdf");

        InputStream template = getClass().getResourceAsStream("/".concat("a.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(template);
        InputStream json = getClass().getResourceAsStream("/".concat("a.json"));
        //JsonDataSource datasource = new JsonDataSource(json, "employees");
       // JsonDataSource datasource = new JsonDataSource(json, _defaultJSONSelectExpression);
        Map parameters = new HashMap();
        // new JREmptyDataSource()
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
        template.close();
        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }


//    @GetMapping("/export2")
//    public void exportRebuild(HttpServletResponse response) throws JRException, IOException {
//
//        response.setContentType("application/x-pdf");
//        response.setHeader("Content-disposition", "inline; filename=helloWorldReport.pdf");
//
//        InputStream template = getClass().getResourceAsStream("/".concat("employeeEmailReport.jrxml"));
//
//        JasperReport jasperReport = JasperCompileManager.compileReport(template);
//
//        Map<String, Object> params = new HashMap<>();
//        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
//        template.close();
//
//        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
//    }

    @GetMapping("/export3")
    public void exportRebuild3(HttpServletResponse response) throws JRException, IOException {

        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=jsonTemplate.pdf");

        InputStream template = getClass().getResourceAsStream("/".concat("JsonCustomersReport.jrxml"));

        JasperReport jasperReport = JasperCompileManager.compileReport(template);

        InputStream json = getClass().getResourceAsStream("/".concat("northwind.json"));

        JsonDataSource datasource = new JsonDataSource(json);

        Map parameters = new HashMap();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);
        template.close();

        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }
    @GetMapping("/export2")
    public void exportRebuild2(HttpServletResponse response) throws JRException, IOException {

        response.setContentType("application/x-pdf");
        response.setHeader("Content-disposition", "inline; filename=helloWorldReport.pdf");

        InputStream template = getClass().getResourceAsStream("/".concat("employeeEmailReport.jrxml"));

        JasperReport jasperReport = JasperCompileManager.compileReport(template);


        Map parameters = new HashMap();
        parameters.put("ReportTitle", "Address Report");
        parameters.put("DataFile", "CsvDataSource.txt - CSV data source");
        Set states = new HashSet();
        states.add("Active");
        states.add("Trial");
        parameters.put("IncludedStates", states);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, this.getDataSource());
        template.close();

        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    private JRCsvDataSource getDataSource() throws JRException
    {
        String[] columnNames = new String[]{"name"};

        InputStream template = getClass().getResourceAsStream("/".concat("CsvDataSource.txt"));

        JRCsvDataSource ds = new JRCsvDataSource(template);
        ds.setRecordDelimiter("\r\n");
        ds.setColumnNames(columnNames);
        return ds;
    }



    public void fill() throws JRException
    {
        long start = System.currentTimeMillis();
        //Preparing parameters
        Map parameters = new HashMap();
        parameters.put("ReportTitle", "Address Report");
        parameters.put("DataFile", "CsvDataSource.txt - CSV data source");
        Set states = new HashSet();
        states.add("Active");
        states.add("Trial");
        parameters.put("IncludedStates", states);

        JasperFillManager.fillReportToFile("build/reports/CsvDataSourceReport.jasper", parameters, getDataSource());
        System.err.println("Filling time : " + (System.currentTimeMillis() - start));
    }


}
