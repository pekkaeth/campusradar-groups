package com.campusradar.groups;

import com.campusradar.common.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {

    public List<Group> listAll(int userId) throws Exception {
        String sql = "SELECT g.id, g.name, g.description, g.category, g.max_members, u.name AS owner_name, "
                + "(SELECT COUNT(*) FROM group_members m WHERE m.group_id = g.id) AS members, "
                + "(SELECT m2.member_role FROM group_members m2 WHERE m2.group_id = g.id AND m2.user_id = ?) AS my_role "
                + "FROM groups_tbl g JOIN users u ON u.id = g.created_by ORDER BY g.created_at DESC";
        List<Group> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Group g = new Group();
                    g.setId(rs.getInt("id"));
                    g.setName(rs.getString("name"));
                    g.setDescription(rs.getString("description"));
                    g.setCategory(rs.getString("category"));
                    g.setMaxMembers(rs.getInt("max_members"));
                    g.setMemberCount(rs.getInt("members"));
                    g.setOwnerName(rs.getString("owner_name"));
                    g.setMyRole(rs.getString("my_role"));
                    list.add(g);
                }
            }
        }
        return list;
    }

    // Creates the group AND makes the creator its OWNER, in one transaction
    public int create(String name, String description, String category, int userId) throws Exception {
        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                int groupId;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO groups_tbl (name, description, category, created_by) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, name);
                    ps.setString(2, description);
                    ps.setString(3, category);
                    ps.setInt(4, userId);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        groupId = keys.getInt(1);
                    }
                }
                try (PreparedStatement ps2 = con.prepareStatement(
                        "INSERT INTO group_members (group_id, user_id, member_role) VALUES (?,?,'OWNER')")) {
                    ps2.setInt(1, groupId);
                    ps2.setInt(2, userId);
                    ps2.executeUpdate();
                }
                con.commit();
                return groupId;
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    // Returns null if joined, or an error message. Uses a lock so two people can't take the last seat.
    public String join(int groupId, int userId) throws Exception {
        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                int max;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT max_members FROM groups_tbl WHERE id = ? FOR UPDATE")) {
                    ps.setInt(1, groupId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) { con.rollback(); return "Group not found."; }
                        max = rs.getInt(1);
                    }
                }
                int count;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT COUNT(*) FROM group_members WHERE group_id = ?")) {
                    ps.setInt(1, groupId);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        count = rs.getInt(1);
                    }
                }
                if (count >= max) { con.rollback(); return "This group is full."; }
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT IGNORE INTO group_members (group_id, user_id) VALUES (?,?)")) {
                    ps.setInt(1, groupId);
                    ps.setInt(2, userId);
                    ps.executeUpdate();
                }
                con.commit();
                return null;
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    // The owner cannot leave, only normal members can
    public void leave(int groupId, int userId) throws Exception {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM group_members WHERE group_id = ? AND user_id = ? AND member_role = 'MEMBER'")) {
            ps.setInt(1, groupId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
}