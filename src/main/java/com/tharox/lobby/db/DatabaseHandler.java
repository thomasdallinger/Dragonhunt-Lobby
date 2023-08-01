package com.tharox.lobby.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseHandler {
    void execute(Connection connection) throws SQLException;
}

