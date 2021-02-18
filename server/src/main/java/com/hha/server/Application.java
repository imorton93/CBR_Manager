package com.hha.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    //spring boot connects this template to the database
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //Initialize the DATABASE Here:
        // - adding the table of clients
        // - adding the table of workers (admin, cbr, etc)

        //CBR worker table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS WORKER_DATA(" +
                "FIRST_NAME TEXT, LAST_NAME TEXT, EMAIL TEXT, PASSWORD TEXT, ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");

        //clients table
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS CLIENT_DATA(" +
                "FIRST_NAME TEXT, LAST_NAME TEXT, AGE INTEGER, VILLAGE_NUMBER INTEGER, LOCATION TEXT, DISABILITY TEXT, IS_SYNCED INTEGER NOT NULL DEFAULT 0)");
    }

}
