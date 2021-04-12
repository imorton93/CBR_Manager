package com.hha.server.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "CLIENT_SURVEYS")
public class Survey implements Serializable {
    @Id
    @JsonProperty("ID")
    private String surveyId;

    //health
    @Column
    @JsonProperty("HEALTH_CONDITION")
    private String health_condition;

    @Column
    @JsonProperty("HAVE_REHAB_ACCESS")
    private String have_rehab_access;

    @Column
    @JsonProperty("NEED_REHAB_ACCESS")
    private String need_rehab_access;

    @Column
    @JsonProperty("HAVE_DEVICE")
    private String have_device;

    @Column
    @JsonProperty("DEVICE_CONDITION")
    private String device_condition;

    @Column
    @JsonProperty("NEED_DEVICE")
    private String need_device;

    @Column
    @JsonProperty("DEVICE_TYPE")
    private String device_type;

    @Column
    @JsonProperty("IS_SATISFIED")
    private String is_satisfied;

    //education
    @Column
    @JsonProperty("IS_STUDENT")
    private String is_student;

    @Column
    @JsonProperty("GRADE_NO")
    private String grade_no;

    @Column
    @JsonProperty("REASON")
    private String reason_no_school;

    @Column
    @JsonProperty("WAS_STUDENT")
    private String was_student;

    @Column
    @JsonProperty("WANT_SCHOOL")
    private String want_school;

    //social
    @Column
    @JsonProperty("IS_VALUED")
    private String is_valued;

    @Column
    @JsonProperty("IS_INDEPENDENT")
    private String is_independent;

    @Column
    @JsonProperty("IS_SOCIAL")
    private String is_social;

    @Column
    @JsonProperty("IS_SOCIALLY_AFFECTED")
    private String is_socially_affected;

    @Column
    @JsonProperty("WAS_DISCRIMINATED")
    private String was_discriminated;

    //Livelihood
    @Column
    @JsonProperty("IS_WORKING")
    private String is_working;

    @Column
    @JsonProperty("WORK_TYPE")
    private String work_type;

    @Column
    @JsonProperty("IS_SELF_EMPLOYED")
    private String is_self_employed;

    @Column
    @JsonProperty("NEEDS_MET")
    private String needs_met;

    @Column
    @JsonProperty("IS_WORK_AFFECTED")
    private String is_work_affected;

    @Column
    @JsonProperty("WANT_WORK")
    private String want_work;

    //food and nutrition
    @Column
    @JsonProperty("FOOD_SECURITY")
    private String food_security;

    @Column
    @JsonProperty("IS_DIET_ENOUGH")
    private String is_diet_enough;

    @Column
    @JsonProperty("CHILD_CONDITION")
    private String child_condition;

    @Column
    @JsonProperty("REFERRAL_REQUIRED")
    private String referral_required;

    //empowerment
    @Column
    @JsonProperty("IS_MEMBER")
    private String is_member;

    @Column
    @JsonProperty("ORGANISATION")
    private String organisation;

    @Column
    @JsonProperty("IS_AWARE")
    private String is_aware;

    @Column
    @JsonProperty("IS_INFLUENCE")
    private String is_influence;

    //shelter and care
    @Column
    @JsonProperty("IS_SHELTER_ADEQUATE")
    private String is_shelter_adequate;

    @Column
    @JsonProperty("ITEMS_ACCESS")
    private String items_access;

    //sync
    @Column
    @JsonProperty("CLIENT_ID")
    private String client_id;

    @Column
    @JsonProperty("IS_SYNCED")
    private String is_synced;

    public Survey() {
    }

    public Survey(String surveyId, String health_condition, String have_rehab_access, String need_rehab_access, String have_device, String device_condition, String need_device, String device_type, String is_satisfied, String is_student, String grade_no, String reason_no_school, String was_student, String want_school, String is_valued, String is_independent, String is_social, String is_socially_affected, String was_discriminated, String is_working, String work_type, String is_self_employed, String needs_met, String is_work_affected, String want_work, String food_security, String is_diet_enough, String child_condition, String referral_required, String is_member, String organisation, String is_aware, String is_influence, String is_shelter_adequate, String items_access, String client_id, String is_synced) {
        this.surveyId = surveyId;
        this.health_condition = health_condition;
        this.have_rehab_access = have_rehab_access;
        this.need_rehab_access = need_rehab_access;
        this.have_device = have_device;
        this.device_condition = device_condition;
        this.need_device = need_device;
        this.device_type = device_type;
        this.is_satisfied = is_satisfied;
        this.is_student = is_student;
        this.grade_no = grade_no;
        this.reason_no_school = reason_no_school;
        this.was_student = was_student;
        this.want_school = want_school;
        this.is_valued = is_valued;
        this.is_independent = is_independent;
        this.is_social = is_social;
        this.is_socially_affected = is_socially_affected;
        this.was_discriminated = was_discriminated;
        this.is_working = is_working;
        this.work_type = work_type;
        this.is_self_employed = is_self_employed;
        this.needs_met = needs_met;
        this.is_work_affected = is_work_affected;
        this.want_work = want_work;
        this.food_security = food_security;
        this.is_diet_enough = is_diet_enough;
        this.child_condition = child_condition;
        this.referral_required = referral_required;
        this.is_member = is_member;
        this.organisation = organisation;
        this.is_aware = is_aware;
        this.is_influence = is_influence;
        this.is_shelter_adequate = is_shelter_adequate;
        this.items_access = items_access;
        this.client_id = client_id;
        this.is_synced = is_synced;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getHealth_condition() {
        return health_condition;
    }

    public void setHealth_condition(String health_condition) {
        this.health_condition = health_condition;
    }

    public String getHave_rehab_access() {
        return have_rehab_access;
    }

    public void setHave_rehab_access(String have_rehab_access) {
        this.have_rehab_access = have_rehab_access;
    }

    public String getNeed_rehab_access() {
        return need_rehab_access;
    }

    public void setNeed_rehab_access(String need_rehab_access) {
        this.need_rehab_access = need_rehab_access;
    }

    public String getHave_device() {
        return have_device;
    }

    public void setHave_device(String have_device) {
        this.have_device = have_device;
    }

    public String getDevice_condition() {
        return device_condition;
    }

    public void setDevice_condition(String device_condition) {
        this.device_condition = device_condition;
    }

    public String getNeed_device() {
        return need_device;
    }

    public void setNeed_device(String need_device) {
        this.need_device = need_device;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getIs_satisfied() {
        return is_satisfied;
    }

    public void setIs_satisfied(String is_satisfied) {
        this.is_satisfied = is_satisfied;
    }

    public String getIs_student() {
        return is_student;
    }

    public void setIs_student(String is_student) {
        this.is_student = is_student;
    }

    public String getGrade_no() {
        return grade_no;
    }

    public void setGrade_no(String grade_no) {
        this.grade_no = grade_no;
    }

    public String getReason_no_school() {
        return reason_no_school;
    }

    public void setReason_no_school(String reason_no_school) {
        this.reason_no_school = reason_no_school;
    }

    public String getWas_student() {
        return was_student;
    }

    public void setWas_student(String was_student) {
        this.was_student = was_student;
    }

    public String getWant_school() {
        return want_school;
    }

    public void setWant_school(String want_school) {
        this.want_school = want_school;
    }

    public String getIs_valued() {
        return is_valued;
    }

    public void setIs_valued(String is_valued) {
        this.is_valued = is_valued;
    }

    public String getIs_independent() {
        return is_independent;
    }

    public void setIs_independent(String is_independent) {
        this.is_independent = is_independent;
    }

    public String getIs_social() {
        return is_social;
    }

    public void setIs_social(String is_social) {
        this.is_social = is_social;
    }

    public String getIs_socially_affected() {
        return is_socially_affected;
    }

    public void setIs_socially_affected(String is_socially_affected) {
        this.is_socially_affected = is_socially_affected;
    }

    public String getWas_discriminated() {
        return was_discriminated;
    }

    public void setWas_discriminated(String was_discriminated) {
        this.was_discriminated = was_discriminated;
    }

    public String getIs_working() {
        return is_working;
    }

    public void setIs_working(String is_working) {
        this.is_working = is_working;
    }

    public String getWork_type() {
        return work_type;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

    public String getIs_self_employed() {
        return is_self_employed;
    }

    public void setIs_self_employed(String is_self_employed) {
        this.is_self_employed = is_self_employed;
    }

    public String getNeeds_met() {
        return needs_met;
    }

    public void setNeeds_met(String needs_met) {
        this.needs_met = needs_met;
    }

    public String getIs_work_affected() {
        return is_work_affected;
    }

    public void setIs_work_affected(String is_work_affected) {
        this.is_work_affected = is_work_affected;
    }

    public String getWant_work() {
        return want_work;
    }

    public void setWant_work(String want_work) {
        this.want_work = want_work;
    }

    public String getFood_security() {
        return food_security;
    }

    public void setFood_security(String food_security) {
        this.food_security = food_security;
    }

    public String getIs_diet_enough() {
        return is_diet_enough;
    }

    public void setIs_diet_enough(String is_diet_enough) {
        this.is_diet_enough = is_diet_enough;
    }

    public String getChild_condition() {
        return child_condition;
    }

    public void setChild_condition(String child_condition) {
        this.child_condition = child_condition;
    }

    public String getReferral_required() {
        return referral_required;
    }

    public void setReferral_required(String referral_required) {
        this.referral_required = referral_required;
    }

    public String getIs_member() {
        return is_member;
    }

    public void setIs_member(String is_member) {
        this.is_member = is_member;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getIs_aware() {
        return is_aware;
    }

    public void setIs_aware(String is_aware) {
        this.is_aware = is_aware;
    }

    public String getIs_influence() {
        return is_influence;
    }

    public void setIs_influence(String is_influence) {
        this.is_influence = is_influence;
    }

    public String getIs_shelter_adequate() {
        return is_shelter_adequate;
    }

    public void setIs_shelter_adequate(String is_shelter_adequate) {
        this.is_shelter_adequate = is_shelter_adequate;
    }

    public String getItems_access() {
        return items_access;
    }

    public void setItems_access(String items_access) {
        this.items_access = items_access;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getIs_synced() {
        return is_synced;
    }

    public void setIs_synced(String is_synced) {
        this.is_synced = is_synced;
    }
}
