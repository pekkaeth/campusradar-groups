package com.campusradar.groups;

import com.campusradar.common.User;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/groups/post")
public class MessagePostServlet extends HttpServlet {
    private final GroupBoardDAO dao = new GroupBoardDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/groups/devlogin");
            return;
        }
        try {
            int groupId = Integer.parseInt(req.getParameter("groupId"));
            String text = req.getParameter("message");
            if (text != null) text = text.trim();

            if (!dao.isMember(groupId, user.getId())) {
                res.sendRedirect(req.getContextPath() + "/groups/list?msg=Only+members+can+post");
                return;
            }
            if (text != null && !text.isEmpty() && text.length() <= 500) {
                dao.addMessage(groupId, user.getId(), text);
            }
            res.sendRedirect(req.getContextPath() + "/groups/view?id=" + groupId);
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/groups/list?msg=Could+not+post+message");
        }
    }
}