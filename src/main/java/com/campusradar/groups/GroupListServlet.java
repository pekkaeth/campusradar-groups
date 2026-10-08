package com.campusradar.groups;

import com.campusradar.common.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/groups/list")
public class GroupListServlet extends HttpServlet {
    private final GroupDAO dao = new GroupDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/groups/devlogin");
            return;
        }
        try {
            req.setAttribute("groups", dao.listAll(user.getId()));
        } catch (Exception e) {
            req.setAttribute("error", "Could not load groups: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/groups/list.jsp").forward(req, res);
    }
}