package com.campusradar.groups;

import com.campusradar.common.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/groups/create")
public class GroupCreateServlet extends HttpServlet {
    private final GroupDAO dao = new GroupDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/groups/create.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/groups/devlogin");
            return;
        }
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String category = req.getParameter("category");

        if (name == null || name.trim().length() < 3) {
            req.setAttribute("error", "Group name must be at least 3 characters.");
            req.getRequestDispatcher("/WEB-INF/views/groups/create.jsp").forward(req, res);
            return;
        }
        try {
            dao.create(name.trim(), description == null ? "" : description.trim(), category, user.getId());
            res.sendRedirect(req.getContextPath() + "/groups/list?msg=Group+created");
        } catch (Exception e) {
            req.setAttribute("error", "Could not create group: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/groups/create.jsp").forward(req, res);
        }
    }
}