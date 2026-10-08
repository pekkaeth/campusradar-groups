package com.campusradar.groups;

import com.campusradar.common.User;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/groups/join", "/groups/leave"})
public class GroupMemberServlet extends HttpServlet {
    private final GroupDAO dao = new GroupDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/groups/devlogin");
            return;
        }
        String msg;
        try {
            int groupId = Integer.parseInt(req.getParameter("groupId"));
            if (req.getServletPath().equals("/groups/join")) {
                String result = dao.join(groupId, user.getId());
                msg = (result == null) ? "You joined the group!" : result;
            } else {
                dao.leave(groupId, user.getId());
                msg = "You left the group.";
            }
        } catch (Exception e) {
            msg = "Error: " + e.getMessage();
        }
        res.sendRedirect(req.getContextPath() + "/groups/list?msg=" + URLEncoder.encode(msg, "UTF-8"));
    }
}
