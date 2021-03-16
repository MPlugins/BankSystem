package de.marvinleiers.banksystem.utils;

import com.zaxxer.hikari.HikariDataSource;
import de.marvinleiers.banksystem.BankSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQL
{
    private final HikariDataSource hikari;
    private final String database;
    private final String password;
    private final String host;
    private final String user;

    public MySQL(String host, String database, String user, String password)
    {
        this.hikari = new HikariDataSource();
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public void connect()
    {
        this.hikari.setMaximumPoolSize(10);
        this.hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        this.hikari.addDataSourceProperty("serverName", this.host);
        this.hikari.addDataSourceProperty("port", 3306);
        this.hikari.addDataSourceProperty("databaseName", this.database);
        this.hikari.addDataSourceProperty("user", this.user);
        this.hikari.addDataSourceProperty("password", this.password);
        this.hikari.addDataSourceProperty("zeroDateTimeBehavior", "convertToNull");
    }

    public void createTable(String tableName, String content)
    {
        if (!(tableName.isEmpty() || content.isEmpty()))
            update("CREATE TABLE IF NOT EXISTS " + tableName + " " + content);
    }

    public void update(String query)
    {
        Connection connection = null;

        try
        {
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            statement.close();
            connection.close();
        }
        catch (SQLException | NullPointerException e)
        {
            try
            {
                connection.close();
            }
            catch (SQLException | NullPointerException ignored)
            {
            }

            e.printStackTrace();
            BankSystem.getMPlugin().log("§4Fehler beim Schreiben in die Datenbank aufgetreten!");
        }
    }

    public Connection getConnection()
    {
        try
        {
            return hikari.getConnection();
        }
        catch (SQLException e)
        {
            BankSystem.getMPlugin().log("§4Verbindung zur Datenbank konnte nicht hergestellt werden!");
        }

        return null;
    }

    public void close()
    {
        try
        {
            this.hikari.close();
            BankSystem.getMPlugin().log("MySQL-Verbindung erfolgreich getrennt!");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
