# CampusRadar: Groups & Meetups

Module 2 of CampusRadar. Students create and join interest groups and talk on a member-only message board.

## Features

- Create a group with a name, category, description and a member limit
- Join and leave groups; the creator becomes the OWNER and cannot leave
- Member-only message board with the member list
- Group names link to the board only for members

## How it works

- **Creating a group** inserts the group and its OWNER membership in one transaction.
- **Joining** locks the group row with `SELECT ... FOR UPDATE` and checks the member count, so the group cannot go over its limit.
- **Access control:** `GroupBoardDAO.isMember` is checked both when opening a board and when posting a message, so typing a URL does not bypass it.
- Messages are shown with `c:out`, so text such as `<script>` appears as plain text.
- The table is named `groups_tbl` because `groups` is a reserved word in newer MySQL.

## Files

```
src/main/java/com/campusradar/groups/
  Group.java, GroupDAO.java, Message.java, GroupBoardDAO.java
  GroupListServlet.java, GroupCreateServlet.java, GroupMemberServlet.java
  GroupViewServlet.java, MessagePostServlet.java
  DevLoginServlet.java        (temporary, for testing this module alone)
  HelloServlet.java, DbTestServlet.java   (setup checks, not used in the combined project)
src/main/webapp/WEB-INF/views/groups/
  list.jsp, create.jsp, view.jsp
```

`DBUtil.java` and `User.java` in the `common` package are shared with the other modules.

## Database

```sql
CREATE TABLE groups_tbl (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  category VARCHAR(40),
  created_by INT NOT NULL,
  max_members INT DEFAULT 50,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE group_members (
  group_id INT NOT NULL,
  user_id INT NOT NULL,
  member_role ENUM('OWNER','MEMBER') DEFAULT 'MEMBER',
  joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (group_id, user_id),
  FOREIGN KEY (group_id) REFERENCES groups_tbl(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE group_messages (
  id INT AUTO_INCREMENT PRIMARY KEY,
  group_id INT NOT NULL,
  user_id INT NOT NULL,
  message VARCHAR(500) NOT NULL,
  posted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (group_id) REFERENCES groups_tbl(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

It also reads the shared `users` table.

## Run this module alone

```
$env:DB_URL  = "jdbc:mysql://HOST:PORT/campusradar?sslMode=REQUIRED"
$env:DB_USER = "your-db-user"
$env:DB_PASS = "your-db-password"
mvn package cargo:run
```

Open `http://localhost:8080/campusradar/groups/devlogin?id=1`, which logs in as that user and opens `/groups/list`. Database credentials are read from environment variables and must never be committed.

## Test checklist

- [ ] Create a group; it shows "You own this group" and `1/50 members`
- [ ] As a second user, JOIN, then open the board and post a message
- [ ] As a user who has not joined, open `/groups/view?id=<id>`; you are redirected
- [ ] Post `<b>test</b>`; it appears as text, not bold
- [ ] The owner has no Leave button

## Integration

In the combined project, `DevLoginServlet`, `HelloServlet` and `DbTestServlet` are removed, and `AuthFilter` protects `/groups/*`.

## Team and ownership

| Role | Person | Notes |
|---|---|---|
| Original development, integration and module lead | Himanshu (`pekkaeth`) | Repository owner |

| Contribution | Status | Commits |
|---|---|---|
| Test checklist completed and results recorded | Planned | |
| Extension: "my groups" filter on the groups list | Planned | |
