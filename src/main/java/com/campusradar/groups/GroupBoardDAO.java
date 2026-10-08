package com.campusradar.groups;

import com.campusradar.common.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GroupBoardDAO {

    public boolean isMember(int groupId, int userId) throws Exception {
        String sql = "SELECT 1 FROM group_members WHERE group_id = ? AND user_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Group findGroup(int groupId) throws Exception {
        String sql = "SELECT g.id, g.name, g.description, g.category, g.max_members, u.name AS owner_name, "
                + "(SELECT COUNT(*) FROM group_members m WHERE m.group_id = g.id) AS members "
                + "FROM groups_tbl g JOIN users u ON u.id = g.created_by WHERE g.id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Group g = new Group();
                g.setId(rs.getInt("id"));
                g.setName(rs.getString("name"));
                g.setDescription(rs.getString("description"));
                g.setCategory(rs.getString("category"));
                g.setMaxMembers(rs.getInt("max_members"));
                g.setMemberCount(rs.getInt("members"));
                g.setOwnerName(rs.getString("owner_name"));
                return g;
            }
        }
    }

    public List<Message> listMessages(int groupId) throws Exception {
        String sql = "SELECT m.id, u.name, m.message, m.posted_at FROM group_messages m "
                + "JOIN users u ON u.id = m.user_id WHERE m.group_id = ? "
                + "ORDER BY m.posted_at ASC, m.id ASC LIMIT 200";
        List<Message> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Message m = new Message();
                    m.setId(rs.getInt(1));
                    m.setUserName(rs.getString(2));
                    m.setText(rs.getString(3));
                    m.setPostedAt(rs.getTimestamp(4));
                    list.add(m);
                }
            }
        }
        return list;
    }

    public List<String> listMemberNames(int groupId) throws Exception {
        String sql = "SELECT u.name FROM group_members m JOIN users u ON u.id = m.user_id "
                + "WHERE m.group_id = ? ORDER BY m.joined_at";
        List<String> names = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) names.add(rs.getString(1));
            }
        }
        return names;
    }

    public void addMessage(int groupId, int userId, String text) throws Exception {
        String sql = "INSERT INTO group_messages (group_id, user_id, message) VALUES (?,?,?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ps.setString(3, text);
            ps.executeUpdate();
        }
    }
}