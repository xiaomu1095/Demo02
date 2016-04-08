package com.example.ll.demo02.code.strategy;




public class Strategy2 implements Strategy {

    @Override
    public String getSQL(String[] usernames) {
        StringBuilder sql = new StringBuilder("select * from user_info where ");
        boolean needOr = false;
        for (String user : usernames) {
            if (needOr) {
                sql.append(" or ");
            }
            sql.append("username = '");
            sql.append(user);
            sql.append("'");
            needOr = true;
        }
        return sql.toString();
    }

}