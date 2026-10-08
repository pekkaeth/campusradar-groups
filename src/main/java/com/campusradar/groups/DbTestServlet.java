package com.campusradar.groups;

import com.campusradar.common.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/groups/dbtest")
public class DbTestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html;charset=UTF-8");
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
            rs.next();
            res.getWriter().println("<h1>Database connected! Users in table: " + rs.getInt(1) + "</h1>");
        } catch (Exception e) {
            res.getWriter().println("<h1>Connection failed</h1><p>" + e.getMessage() + "</p>");
        }
    }
}
