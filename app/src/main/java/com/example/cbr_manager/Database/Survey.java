package com.example.cbr_manager.Database;

import java.io.Serializable;

public class Survey implements Serializable {

    private Long id;

    //health
    private byte health_condition; // stored as values in 1-4
    private boolean have_rehab_access;
    private boolean need_rehab_access;
    private boolean have_device;
    private boolean device_condition;
    private boolean need_device;
    private String device_type;
    private byte is_satisfied;

    //education
    private boolean is_student;
    private byte grade_no;
    private String reason_no_school;
    private boolean was_student;
    private boolean want_school;

    //social
    private boolean is_valued;
    private boolean is_independent;
    private boolean is_social;
    private boolean is_socially_affected;
    private boolean was_discriminated;

    //Livelihood
    private boolean is_working;
    private String work_type;
    private String is_self_employed;
    private boolean needs_met;
    private boolean is_work_affected;
    private boolean want_work;

    //food and nutrition
    private String food_security;
    private boolean is_diet_enough;
    private String child_condition;
    private boolean referral_required;

    //empowerment
    private boolean is_member;
    private String organisation;
    private boolean is_aware;
    private boolean is_influence;

    //shelter and care
    private boolean is_shelter_adequate;
    private boolean items_access;

    //sync
    private long client_id;
    private boolean is_synced = false;

    public Survey() {
    }

    public Survey(Long id, byte health_condition, boolean have_rehab_access, boolean need_rehab_access, boolean have_device, boolean device_condition, boolean need_device, String device_type, byte is_satisfied, boolean is_student, byte grade_no, String reason_no_school, boolean was_student, boolean want_school, boolean is_valued, boolean is_independent, boolean is_social, boolean is_socially_affected, boolean was_discriminated, boolean is_working, String work_type, String is_self_employed, boolean needs_met, boolean is_work_affected, boolean want_work, String food_security, boolean is_diet_enough, String child_condition, boolean referral_required, boolean is_member, String organisation, boolean is_aware, boolean is_influence, boolean is_shelter_adequate, boolean items_access, long client_id, boolean is_synced) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte getHealth_condition() {
        return health_condition;
    }

    public void setHealth_condition(byte health_condition) {
        this.health_condition = health_condition;
    }

    public boolean isHave_rehab_access() {
        return have_rehab_access;
    }

    public void setHave_rehab_access(boolean have_rehab_access) {
        this.have_rehab_access = have_rehab_access;
    }

    public boolean isNeed_rehab_access() {
        return need_rehab_access;
    }

    public void setNeed_rehab_access(boolean need_rehab_access) {
        this.need_rehab_access = need_rehab_access;
    }

    public boolean isHave_device() {
        return have_device;
    }

    public void setHave_device(boolean have_device) {
        this.have_device = have_device;
    }

    public boolean isDevice_condition() {
        return device_condition;
    }

    public void setDevice_condition(boolean device_condition) {
        this.device_condition = device_condition;
    }

    public boolean isNeed_device() {
        return need_device;
    }

    public void setNeed_device(boolean need_device) {
        this.need_device = need_device;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public byte getIs_satisfied() {
        return is_satisfied;
    }

    public void setIs_satisfied(byte is_satisfied) {
        this.is_satisfied = is_satisfied;
    }

    public boolean isIs_student() {
        return is_student;
    }

    public void setIs_student(boolean is_student) {
        this.is_student = is_student;
    }

    public byte getGrade_no() {
        return grade_no;
    }

    public void setGrade_no(byte grade_no) {
        this.grade_no = grade_no;
    }

    public String getReason_no_school() {
        return reason_no_school;
    }

    public void setReason_no_school(String reason_no_school) {
        this.reason_no_school = reason_no_school;
    }

    public boolean isWas_student() {
        return was_student;
    }

    public void setWas_student(boolean was_student) {
        this.was_student = was_student;
    }

    public boolean isWant_school() {
        return want_school;
    }

    public void setWant_school(boolean want_school) {
        this.want_school = want_school;
    }

    public boolean isIs_valued() {
        return is_valued;
    }

    public void setIs_valued(boolean is_valued) {
        this.is_valued = is_valued;
    }

    public boolean isIs_independent() {
        return is_independent;
    }

    public void setIs_independent(boolean is_independent) {
        this.is_independent = is_independent;
    }

    public boolean isIs_social() {
        return is_social;
    }

    public void setIs_social(boolean is_social) {
        this.is_social = is_social;
    }

    public boolean isIs_socially_affected() {
        return is_socially_affected;
    }

    public void setIs_socially_affected(boolean is_socially_affected) {
        this.is_socially_affected = is_socially_affected;
    }

    public boolean isWas_discriminated() {
        return was_discriminated;
    }

    public void setWas_discriminated(boolean was_discriminated) {
        this.was_discriminated = was_discriminated;
    }

    public boolean isIs_working() {
        return is_working;
    }

    public void setIs_working(boolean is_working) {
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

    public boolean isNeeds_met() {
        return needs_met;
    }

    public void setNeeds_met(boolean needs_met) {
        this.needs_met = needs_met;
    }

    public boolean isIs_work_affected() {
        return is_work_affected;
    }

    public void setIs_work_affected(boolean is_work_affected) {
        this.is_work_affected = is_work_affected;
    }

    public boolean isWant_work() {
        return want_work;
    }

    public void setWant_work(boolean want_work) {
        this.want_work = want_work;
    }

    public String getFood_security() {
        return food_security;
    }

    public void setFood_security(String food_security) {
        this.food_security = food_security;
    }

    public boolean isIs_diet_enough() {
        return is_diet_enough;
    }

    public void setIs_diet_enough(boolean is_diet_enough) {
        this.is_diet_enough = is_diet_enough;
    }

    public String getChild_condition() {
        return child_condition;
    }

    public void setChild_condition(String child_condition) {
        this.child_condition = child_condition;
    }

    public boolean isReferral_required() {
        return referral_required;
    }

    public void setReferral_required(boolean referral_required) {
        this.referral_required = referral_required;
    }

    public boolean isIs_member() {
        return is_member;
    }

    public void setIs_member(boolean is_member) {
        this.is_member = is_member;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public boolean isIs_aware() {
        return is_aware;
    }

    public void setIs_aware(boolean is_aware) {
        this.is_aware = is_aware;
    }

    public boolean isIs_influence() {
        return is_influence;
    }

    public void setIs_influence(boolean is_influence) {
        this.is_influence = is_influence;
    }

    public boolean isIs_shelter_adequate() {
        return is_shelter_adequate;
    }

    public void setIs_shelter_adequate(boolean is_shelter_adequate) {
        this.is_shelter_adequate = is_shelter_adequate;
    }

    public boolean isItems_access() {
        return items_access;
    }

    public void setItems_access(boolean items_access) {
        this.items_access = items_access;
    }

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public boolean isIs_synced() {
        return is_synced;
    }

    public void setIs_synced(boolean is_synced) {
        this.is_synced = is_synced;
    }
}
