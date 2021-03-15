package com.hha.server.controller;

import com.hha.server.model.CBRWorker;
import com.hha.server.model.Client;

import com.hha.server.model.Referral;
import com.hha.server.model.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private final WorkerRepository workerRepository;

    @Autowired
    private final VisitRepository visitRepository;

    @Autowired
    private final ReferralRepository referralRepository;


    public Controller(ClientRepository clientRepository, WorkerRepository workerRepository, VisitRepository visitRepository, ReferralRepository referralRepository) {
        this.clientRepository = clientRepository;
        this.workerRepository = workerRepository;
        this.visitRepository = visitRepository;
        this.referralRepository = referralRepository;
    }

    @GetMapping
    String homepage() {
        return "Welcome to myCBR Server!";
    }

    @GetMapping("/count-clients")
    long numClients() {
        return clientRepository.count();
    }
    
    @GetMapping("/count-workers")
    long numWorkers() {
        return workerRepository.count();
    }
    
    @GetMapping("/count-visits")
    long numVisits() { return visitRepository.count(); }
    
    @GetMapping("/count-referrals")
    long numReferrals() { return referralRepository.count(); }

    //SYNC ENDPOINTS - CLIENT
    //1. App has no data
    @GetMapping("/get-clients")
    List<Client> emptySyncClients() {
        return clientRepository.findAll();
    }

    //2. App has entries
    @PostMapping("/clients")
    List<Client> multipleSyncClients(@RequestBody List<Client> clients) {
        for (Client client : clients) {
            clientRepository.save(new Client(client.getID(), client.getCONSENT(), client.getDATE(), client.getFIRST_NAME(),
                    client.getLAST_NAME(), client.getAGE(), client.getGENDER(), client.getLOCATION(), client.getVILLAGE_NUMBER(),
                    client.getCONTACT(), client.getCAREGIVER_PRESENCE(), client.getCAREGIVER_NUMBER(), client.getDISABILITY(),
                    client.getHEALTH_RATE(), client.getHEALTH_REQUIREMENT(), client.getHEALTH_GOAL(), client.getEDUCATION_RATE(), client.getEDUCATION_REQUIRE(),
                    client.getEDUCATION_GOAL(), client.getSOCIAL_RATE(), client.getSOCIAL_REQUIREMENT(), client.getSOCIAL_GOAL(), client.getLATITUDE(), client.getLONGITUDE(),
                    client.getWORKER_ID(), "1"));
        }

        return clientRepository.findAll();
    }

    //SYNC ENDPOINTS - WORKER
    //1. App has no data
    @GetMapping("/get-workers")
    List<CBRWorker> emptySyncWorkers() {
        return workerRepository.findAll();
    }

    //2. App has entries
    @PostMapping("/workers")
    List<CBRWorker> multipleSyncWorkers(@RequestBody List<CBRWorker> workers) {
        int ID = 0;

        for (CBRWorker worker : workers) {
            //If worker email already exists in database, return error code 409
            if(!workerRepository.findByUsername(worker.getUSERNAME()).isEmpty()) {
                throw new IllegalArgumentException();
            }

            else {
                ID = (int) Long.parseLong(worker.getID());
                while (!workerRepository.findByID(String.valueOf(ID)).isEmpty()) {
                    ID += 1;
                }

                workerRepository.save(new CBRWorker(String.valueOf(ID), worker.getFIRST_NAME(), worker.getLAST_NAME(), worker.getUSERNAME(), worker.getPASSWORD()));
            }
        }

        return workerRepository.findAll();
    }

    ///SYNC ENDPOINTS - VISITS
    //1. App has no data
    @GetMapping("/get-visits")
    List<Visit> emptySyncVisits() {
        return visitRepository.findAll();
    }

    //2. App has entries
    @PostMapping("/visits")
    List<Visit> multipleSyncVisits(@RequestBody List<Visit> visits) {
        for (Visit visit : visits) {
            visitRepository.save(new Visit(visit.getVisit_id(), visit.getPurposeOfVisit(), visit.getIfCbr(), visit.getDate(),
                    visit.getLocation(), visit.getVillageNumber(), visit.getHealthProvided(), visit.getHealthGoalMet(),visit.getHealthIfConcluded(),
                    visit.getSocialProvided(), visit.getSocialGoalMet(), visit.getSocialIfConcluded(), visit.getEducationProvided(),
                    visit.getEducationGoalMet(), visit.getEducationIfConcluded(), visit.getClient_id(), "1"));
        }

        return visitRepository.findAll();
    }

    //SYNC ENDPOINTS - REFERRALS
    //1. App has no data
    @GetMapping("/get-referrals")
    List<Referral> emptySyncReferrals() {
        return referralRepository.findAll();
    }

    //2. App has entries
    @PostMapping("/referrals")
    List<Referral> multipleSyncReferrals(@RequestBody List<Referral> referrals) {
        for (Referral referral : referrals) {

            referralRepository.save(new Referral(referral.getSERVICE_REQ(),referral.getREFERRAL_PHOTO(), referral.getBASIC_OR_INTER(),
                    referral.getHIP_WIDTH(), referral.getHAS_WHEEL_CHAIR(), referral.getWHEEL_CHAIR_REPARABLE(), referral.getBRING_TO_CENTRE(),
                    referral.getCONDITIONS(), referral.getINJURY_LOCATION_KNEE(), referral.getINJURY_LOCATION_ELBOW(), referral.getSTATUS(),
                    referral.getOUTCOME(), referral.getCLIENT_ID(), "1"));
        }

        return referralRepository.findAll();
    }

    //Exception Handlers
    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "Email is already in use.")
    @ExceptionHandler(IllegalArgumentException.class)
    public void alreadyExistsExceptionHandler() {

    }
}

@Component
interface ClientRepository extends JpaRepository<Client, Long> {
    @Query(value = "SELECT * FROM CLIENT_DATA WHERE ID = ?1", nativeQuery = true)
    List<Client> findByID(String ID);
}

@Component
interface WorkerRepository extends JpaRepository<CBRWorker, Long> {
    @Query(value = "SELECT * FROM WORKER_DATA WHERE ID = ?1", nativeQuery = true)
    List<CBRWorker> findByID(String ID);

    @Query(value = "SELECT * FROM WORKER_DATA WHERE USERNAME = ?1", nativeQuery = true)
    List<CBRWorker> findByUsername(String username);
}

@Component
interface VisitRepository extends JpaRepository<Visit, Long> {
    @Query(value = "SELECT * FROM CLIENT_VISITS WHERE ID = ?1", nativeQuery = true)
    List<Visit> findByID(String ID);
}

@Component
interface ReferralRepository extends JpaRepository<Referral, Long> {
    //TODO: Add ID field for referrals, and search by ID
}
