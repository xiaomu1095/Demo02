package com.example.ll.demo02.code.strategy;




public class Strategy1 implements Strategy {

    @Override
    public String getSQL(String[] usernames) {
        StringBuilder sql = new StringBuilder("select * from user_info where ");
        for (String user : usernames) {
            sql.append("username = '");
            sql.append(user);
            sql.append("' or ");
        }
        sql.delete(sql.length() - " or ".length(), sql.length());
        return sql.toString();
    }

}