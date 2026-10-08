package com.campusradar.groups;

import com.campusradar.common.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/groups/view")
public class GroupViewServlet extends HttpServlet {
    private final GroupBoardDAO dao = new GroupBoardDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/groups/devlogin");
            return;
        }
        try {
            int groupId = Integer.parseInt(req.getParameter("id"));
            if (!dao.isMember(groupId, user.getId())) {
                res.sendRedirect(req.getContextPath() + "/groups/list?msg=Join+the+group+to+open+it");
                return;
            }
            Group group = dao.findGroup(groupId);
            if (group == null) {
                res.sendRedirect(req.getContextPath() + "/groups/list?msg=Group+not+found");
                return;
            }
            req.setAttribute("group", group);
            req.setAttribute("messages", dao.listMessages(groupId));
            req.setAttribute("members", dao.listMemberNames(groupId));
            req.getRequestDispatcher("/WEB-INF/views/groups/view.jsp").forward(req, res);
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/groups/list?msg=Could+not+open+group");
        }
    }
}