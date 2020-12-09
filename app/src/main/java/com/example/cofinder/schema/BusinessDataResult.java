package com.example.cofinder.schema;

import java.util.List;

public class BusinessDataResult {
    public String businessId;
    public String name;
    public String registrationDate;
    public String companyForm;
    public String detailsUri;
    public List<BisCompanyLiquidation> liquidations;
    public List<BisCompanyName> names;
    public List<BisCompanyName> auxiliaryNames;
    public List<BisAddress> addresses;
    public List<BisCompanyForm> companyForms;
    public List<BisCompanyBusinessLine> businessLines;
    public List<BisCompanyLanguage> languages;
    public List<BisCompanyRegisteredOffice> registedOffices;
    public List<BisCompanyContactDetail> contactDetails;
    public List<BisCompanyRegisteredEntry> registeredEntries;
    public List<BisCompanyBusinessIdChange> businessIdChanges;
}
