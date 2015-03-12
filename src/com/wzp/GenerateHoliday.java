package com.wzp;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.joda.time.LocalDate;

import com.wzp.dbcp.DbPoolUtils;

public class GenerateHoliday {
    private static DbPoolUtils dbPool ;
    private static Connection connection;
    private static String sql = "insert into tb_holiday(holiday_tp,holiday) values ";

    public static void main(String[] args) {
        try{
            GenerateHoliday.class.getClassLoader().loadClass("com.mysql.jdbc.Driver");
        }catch(Exception e){
            e.printStackTrace();
        }
        dbPool = DbPoolUtils.getInstance("dev", "111111","jdbc:mysql://115.29.191.5:3306/niurr_dev");
        connection = dbPool.getConnection();
        
        LocalDate ld  = LocalDate.now();
        LocalDate start = new LocalDate(ld.getYear(), 1, 1);
        LocalDate end = new LocalDate(ld.getYear(),12,31);
        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);
        while(!start.equals(end)){
            printHoliday(start,buffer);
            start = start.plusDays(1);
        }
        printHoliday(start,buffer);
        buffer.setLength(buffer.length() -1);
        System.out.println(buffer);
        Statement stmt = null;
        try{
            stmt = connection.createStatement();
            stmt.execute(buffer.toString());
            stmt.close();
        }catch(Exception e){
            
        }finally{
            try {
                if(stmt != null){
                    stmt.close();
                    connection.close();
                }
            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    public static void printHoliday(LocalDate date,StringBuffer buffer){
        int dayOfWeek = date.getDayOfWeek();
        if(dayOfWeek == 6 || dayOfWeek == 7){
//            System.out.println(date);
            buffer.append("(0,'");
            buffer.append(date+"')");
            buffer.append(",(1,'");
            buffer.append(date+"'),");
        }
    }
    
    public static void t(){
        
    }

}
