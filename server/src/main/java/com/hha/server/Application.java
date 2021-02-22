package com.hha.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    //----------------------------------------------------------------------------
    //Worker Table
    private static final String TABLE_NAME = "WORKER_DATA";
    private static final String COL_1 = "FIRST_NAME";
    private static final String COL_2 = "LAST_NAME";
    private static final String COL_3 = "EMAIL";
    private static final String COL_4 = "PASSWORD";
    private static final String COL_5 = "ID";

    //Client Table
    private static final String client_table_name = "CLIENT_DATA";
    private static final String client_id = "ID";
    private static final String client_consent = "CONSENT";
    private static final String client_date = "DATE";
    private static final String client_first_name = "FIRST_NAME";
    private static final String client_last_name = "LAST_NAME";
    private static final String client_age = "AGE";
    private static final String client_gender = "GENDER";
    private static final String client_village_no = "VILLAGE_NUMBER";
    private static final String client_location = "LOCATION";
    private static final String client_contact = "CONTACT";
    private static final String client_caregiver_presence = "CAREGIVER_PRESENCE";
    private static final String client_caregiver_number = "CAREGIVER_NUMBER";
    private static final String client_disability = "DISABILITY";
    private static final String client_heath_rate = "HEALTH_RATE";
    private static final String client_health_requirement = "HEALTH_REQUIREMENT";
    private static final String client_health_goal = "HEALTH_GOAL";
    private static final String client_education_rate = "EDUCATION_RATE";
    private static final String client_education_requirement = "EDUCATION_REQUIRE";
    private static final String client_education_goal = "EDUCATION_GOAL";
    private static final String client_social_rate = "SOCIAL_RATE";
    private static final String client_social_requirement= "SOCIAL_REQUIREMENT";
    private static final String client_social_goal = "SOCIAL_GOAL";
    private static final String is_synced = "IS_SYNCED";

    //Visits Table
    private static final String visit_table = "CLIENT_VISITS";
    private static final String visit_id = "ID";
    private static final String visit_date = "VISIT_DATE";
    private static final String visit_purpose = "PURPOSE_OF_VISIT";
    private static final String if_cbr = "IF_CBR";
    private static final String visit_location = "LOCATION";
    private static final String visit_village_no = "VILLAGE_NUMBER";
    private static final String health_provided = "HEALTH_PROVIDED";
    private static final String health_goal_status = "HEALTH_GOAL_STATUS";
    private static final String health_outcome = "HEALTH_OUTCOME";
    private static final String education_provided = "EDU_PROVIDED";
    private static final String edu_goal_status = "EDU_GOAL_STATUS";
    private static final String education_outcome = "EDUCATION_OUTCOME";
    private static final String social_provided = "SOCIAL_PROVIDED";
    private static final String social_goal_status = "SOCIAL_GOAL_STATUS";
    private static final String social_outcome = "SOCIAL_OUTCOME";
    private static final String client_visit_id = "CLIENT_ID";
    //----------------------------------------------------------------------------

    //spring boot connects this template to the database
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
         //Setting up CBR Worker table
        String create_worker_table = "CREATE TABLE " + TABLE_NAME + " (" + COL_1 + " TEXT, " + COL_2 + " TEXT, " + COL_3
                + " TEXT UNIQUE NOT NULL, " + COL_4 + " TEXT, " + COL_5 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL);";
        jdbcTemplate.execute(create_worker_table);

        //Setting up Clients table
        String create_client_table = "CREATE TABLE " + client_table_name + " (" + client_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + client_consent + " BOOLEAN NOT NULL, " + client_date + " STRING NOT NULL, " + client_first_name + " TEXT, "
                + client_last_name + " TEXT, " + client_age + " INTEGER, " + client_gender + " TEXT, "
                + client_village_no + " INTEGER, "  + client_location + " TEXT, " + client_contact + " STRING, "+ client_caregiver_presence
                + " BOOLEAN NOT NULL, " + client_caregiver_number +" STRING, " + client_disability + " TEXT, " + client_heath_rate
                + " STRING, "+ client_health_requirement + " STRING, " + client_health_goal + " STRING, " + client_education_rate +" STRING, "
                + client_education_requirement + " STRING, " + client_education_goal  + " STRING, " + client_social_rate + " STRING, "
                + client_social_requirement + " STRING, " +  client_social_goal + " STRING, " + is_synced + " INTEGER NOT NULL DEFAULT 0);";
        jdbcTemplate.execute(create_client_table);

        //Setting up Visits table
        String create_visit_table = "CREATE TABLE " +
                visit_table + " (" + visit_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + visit_date + " STRING NOT NULL, "
                + visit_purpose + " STRING, " + if_cbr + " TEXT, " +  visit_location + " TEXT, " + visit_village_no + " INTEGER, "
                + health_provided + " TEXT, " + health_goal_status + " TEXT, " + health_outcome + " STRING, "
                + education_provided + " TEXT, " + edu_goal_status + " TEXT, " + education_outcome + " STRING, "
                + social_provided + " TEXT, " + social_goal_status + " TEXT, " + social_outcome + " STRING, "
                + client_visit_id + " INTEGER NOT NULL);";
        jdbcTemplate.execute(create_visit_table);
    }

}
