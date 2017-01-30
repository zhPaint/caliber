package com.revature.caliber.gateway.impl;

import com.revature.caliber.gateway.ApiGateway;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.HashMap;

public class ApiGatewayImplTest {

    private static ApplicationContext context;
    private static ApiGateway apiGateway;

    @Before
    public void setUp() throws Exception {
        context = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/beans.xml");
        apiGateway = context.getBean(ApiGateway.class);
    }
    
    @Test
    @Ignore
    public void getAggregatedGradesForTrainee() throws Exception {
        HashMap<String, Double[]> grades = apiGateway.getTechGradeDataForTrainee(1);
        for (String grade : grades.keySet()) {
            System.out.print(grade + " -> [");
            for (Double d : grades.get(grade)) {
                System.out.print(d + ", ");
            }
            System.out.println("]");
        }

    }
    
    @Ignore
    @Test
    public void testo(){
    	HashMap<String, Double []> hey = apiGateway.getTechGradeDataForBatch(1);
    }

    @Ignore
    @Test
    public void getWeekAggregatedGradesForTrainee() throws Exception {
        HashMap<String, Double[]> grades = apiGateway.getWeekGradeDataForTrainee(1);

//        for (String grade : grades.keySet()) {
//            System.out.print(grade + " -> [");
//            for (Double d : grades.get(grade)) {
//                System.out.print(d + ", ");
//            }
//            System.out.println("]");
//        }

    }

    @Test
    @Ignore
    public void getGradesForBatchWeekly() throws Exception {
        HashMap<String, Double[]> grades = apiGateway.getGradesForBatchWeekly(1);

        for (String grade : grades.keySet()) {
            System.out.print(grade + " -> [");
            for (Double d : grades.get(grade)) {
                System.out.print(d + ", ");
            }
            System.out.println("]");
        }

    }
}