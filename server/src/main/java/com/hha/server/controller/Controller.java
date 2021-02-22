package com.hha.server.controller;

import com.hha.server.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    //connecting to the local database
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public List<Client> getAllClients() {
        List<Client> clientsInDatabase = new ArrayList<>();

        /* clientsInDatabase = jdbcTemplate.query("SELECT * FROM CLIENT_DATA",
                (resultSet, rowNum) ->
                        new Client(resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getInt("AGE"), resultSet.getInt("VILLAGE_NUMBER"), resultSet.getString("LOCATION"), resultSet.getString("DISABILITY"))); */
        return clientsInDatabase;

    }

    //--TO-DO--
    //Currently, this is only assuming that the Android app is sending CLIENT data.
    //This code needs to be extended to include other tables in the database (i.e worker information)
    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.OK) //signifies that the database has been updated
    public List<Client> sync(@RequestBody List<Client> clients) {
        //SYNC ENDPOINT
        //1. Requests a JSon Array -> takes the array and stores it in the local database
        //2. The 'SYNC' column for the rows goes from FALSE to TRUE
        //2. Returns the updated database in JSon format -> use this JSon to update the Android database

        //TODO: Update the following SQLite statements and constructor calls to match the new columns added to database

        for (Client client : clients) {
            //Make sure that the 'is_synced' column is set to true
            jdbcTemplate.execute("INSERT INTO CLIENT_DATA VALUES ('" + client.getFirstName() + "','" + client.getLastName() + "'," + client.getAge() + "," + client.getVillageNumber() + ",'" + client.getLocation() + "','" + client.getDisabilities() + "', 1)");
            System.out.println("INSERT INTO CLIENT_DATA VALUES ('" + client.getFirstName() + "','" + client.getLastName() + "'," + client.getAge() + "," + client.getVillageNumber() + ",'" + client.getLocation() + "','" + client.getDisabilities() + "', 1)");
        }

        List<Client> clientsInDatabase = new ArrayList<>();

        /* clientsInDatabase = jdbcTemplate.query("SELECT * FROM CLIENT_DATA",
                (resultSet, rowNum) ->
                        new Client(resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getInt("AGE"), resultSet.getInt("VILLAGE_NUMBER"), resultSet.getString("LOCATION"), resultSet.getString("DISABILITY"))); */

        //returns the newly updated database
        return clientsInDatabase;
    }

    //Exception Handlers
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
            reason = "Request ID not found.")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler() {

    }
}

