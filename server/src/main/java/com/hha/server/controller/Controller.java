package com.hha.server.controller;

import com.hha.server.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
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

    @Autowired
    private final MessageRepository messageRepository;

    @Autowired
    private final SurveyRepository surveyRepository;

    public Controller(ClientRepository clientRepository, WorkerRepository workerRepository, VisitRepository visitRepository, ReferralRepository referralRepository, MessageRepository messageRepository, SurveyRepository surveyRepository) {
        this.clientRepository = clientRepository;
        this.workerRepository = workerRepository;
        this.visitRepository = visitRepository;
        this.referralRepository = referralRepository;
        this.messageRepository = messageRepository;
        this.surveyRepository = surveyRepository;
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
            clientRepository.save(new Client(client.getId(), client.getConsent(), client.getDate(), client.getFirstName(),
                    client.getLastName(), client.getAge(), client.getGender(), client.getLocation(), client.getVillageNumber(),
                    client.getContact(), client.getCaregiverPresence(), client.getCaregiverNumber(), client.getPhoto(), client.getDisability(),
                    client.getHealthRate(), client.getHealthReq(), client.getHealthGoal(), client.getEducationRate(), client.getEducationReq(),
                    client.getEducationGoal(), client.getSocialRate(), client.getSocialReq(), client.getSocialGoal(), client.getLatitude(), client.getLongitude(),
                    client.getWorkerID(), "1"));
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
            if(!workerRepository.findByUsername(worker.getUsername()).isEmpty()) {
                throw new EntityExistsException();
            }

            else {
                ID = (int) Long.parseLong(worker.getId());
                while (!workerRepository.findByID(String.valueOf(ID)).isEmpty()) {
                    ID += 1;
                }

                workerRepository.save(new CBRWorker(worker.getFirstName(), worker.getLastName(), worker.getUsername(), worker.getZone(),
                                      worker.getPhoto(), worker.getPassword(), String.valueOf(ID), worker.getIsAdmin()));
            }
        }

        return workerRepository.findAll();
    }

    @PostMapping ("/update-worker/{id}")
    CBRWorker editCBRWorker (@PathVariable("id") String workerID,
                             @RequestBody CBRWorker newWorker) {

        if ((workerRepository.findByID(newWorker.getId()).size() > 0 )
            && (workerRepository.findByUsername(newWorker.getUsername()).size() <= 1)) {
            workerRepository.updateWorkerById(newWorker.getFirstName(), newWorker.getLastName(), newWorker.getUsername(), newWorker.getZone(), newWorker.getPhoto(), newWorker.getId());
            return newWorker;
        }

        throw new IllegalArgumentException();
    }

    @GetMapping ("/num-workers/{id}")
    List<CBRWorker> numCBRbyID (@PathVariable("id") String workerID) {
        return workerRepository.findByID(workerID);
    }

    @GetMapping ("/num-workers-un/{username}")
    List<CBRWorker> numCBRbyUN (@PathVariable("username") String username) {
        return workerRepository.findByUsername(username);
    }

    @GetMapping ("/id-by-un/{username}")
    String getIdByUsername (@PathVariable("username") String username) {
        return workerRepository.findByUsername(username).get(0).getId();
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

            referralRepository.save(new Referral(referral.getId(), referral.getServiceReq(),referral.getReferralPhoto(), referral.getBasicOrInter(),
                    referral.getHipWidth(), referral.getHasWheelChair(), referral.getWheelchairRepairable(), referral.getBringToCentre(),
                    referral.getConditions(), referral.getInjuryLocationKnee(), referral.getInjuryLocationElbow(), referral.getStatus(),
                    referral.getOutcome(), referral.getClientID(), "1"));
        }

        return referralRepository.findAll();
    }

    //SYNC ENDPOINTS - ADMINMESSAGES
    //1. App has no data
    @GetMapping("/get-admin-messages")
    List<AdminMessage> emptySyncMessages() {
        return messageRepository.findAll();
    }

    //2. App has entries
    @PostMapping("/admin-messages")
    List<AdminMessage> multipleSyncMessages(@RequestBody List<AdminMessage> messages) {
        for (AdminMessage message : messages) {
            messageRepository.save(new AdminMessage(message.getMessageID(), message.getAdminID(), message.getTitle(), message.getDate(), message.getLocation(),
                    message.getMessage(), "1", message.getViewedStatus()));
        }

        return messageRepository.findAll();
    }

    //SYNC ENDPOINTS - Survey
    //1. App has no data
    @GetMapping("/get-surveys")
    List<Survey> emptySyncSurveys() {
        return surveyRepository.findAll();
    }

    //2. App has entries
    @PostMapping("/surveys")
    List<Survey> multipleSyncSurveys(@RequestBody List<Survey> surveys) {
        for (Survey survey : surveys) {
            surveyRepository.save(new Survey(survey.getSurveyId(), survey.getHealth_condition(), survey.getHave_rehab_access(),
                    survey.getNeed_rehab_access(), survey.getHave_device(), survey.getDevice_condition(), survey.getNeed_device(),
                    survey.getDevice_type(), survey.getIs_satisfied(), survey.getIs_student(), survey.getGrade_no(), survey.getReason_no_school(),
                    survey.getWas_student(), survey.getWant_school(), survey.getIs_valued(), survey.getIs_independent(), survey.getIs_social(),
                    survey.getIs_socially_affected(), survey.getWas_discriminated(), survey.getIs_working(), survey.getWork_type(),
                    survey.getIs_self_employed(), survey.getNeeds_met(), survey.getIs_work_affected(), survey.getWant_work(),
                    survey.getFood_security(), survey.getIs_diet_enough(), survey.getChild_condition(), survey.getReferral_required(),
                    survey.getIs_member(), survey.getOrganisation(), survey.getIs_aware(), survey.getIs_influence(), survey.getIs_shelter_adequate(),
                    survey.getItems_access(), survey.getClient_id(), survey.getIs_synced()));
        }

        return surveyRepository.findAll();
    }

    //---DELETE ENDPOINTS--
    @GetMapping("/delete-client/{id}")
    public String deleteClientByID(@PathVariable("id") String clientID) {
        if (clientRepository.deleteByID(clientID) > 0) {
            return "Client with id " + clientID + " deleted!";
        }

        return "Client with id " + clientID + " doesn't exist.";
    }

    @GetMapping("/delete-worker/{id}")
    public String deleteWorkerByID(@PathVariable("id") String workerID) {
        if (workerRepository.deleteByID(workerID) > 0) {
            return "Client with id " + workerID + " deleted!";
        }

        return "Client with id " + workerID + " doesn't exist.";
    }

    //Exception Handlers
    //1. Already exists
    @ResponseStatus(value = HttpStatus.CONFLICT,
            reason = "Username is already in use.")
    @ExceptionHandler(EntityExistsException.class)
    public void alreadyExistsExceptionHandler() {

    }
}

@Component
interface ClientRepository extends JpaRepository<Client, Long> {
    @Query(value = "SELECT * FROM CLIENT_DATA WHERE ID = ?1", nativeQuery = true)
    List<Client> findByID(String ID);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CLIENT_DATA WHERE ID = ?1", nativeQuery = true)
    Integer deleteByID(String ID);
}

@Component
interface WorkerRepository extends JpaRepository<CBRWorker, Long> {
    @Query(value = "SELECT * FROM WORKER_DATA WHERE ID = ?1", nativeQuery = true)
    List<CBRWorker> findByID(String ID);

    @Query(value = "SELECT * FROM WORKER_DATA WHERE USERNAME = ?1", nativeQuery = true)
    List<CBRWorker> findByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE WORKER_DATA SET FIRST_NAME = ?1, LAST_NAME = ?2, USERNAME = ?3, ZONE = ?4, PHOTO = ?5 WHERE ID = ?6", nativeQuery = true)
    void updateWorkerById(String firstname, String lastname, String username, String zone, String photo, String workerID);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM WORKER_DATA WHERE ID = ?1", nativeQuery = true)
    Integer deleteByID(String ID);
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

@Component
interface MessageRepository extends JpaRepository<AdminMessage, Long> {

}

@Component
interface SurveyRepository extends JpaRepository<Survey, Long> {

}
