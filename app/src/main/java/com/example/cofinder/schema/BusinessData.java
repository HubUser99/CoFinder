package com.example.cofinder.schema;

import java.io.Serializable;
import java.util.List;

public class BusinessData implements Serializable {
    public String type;
    public String version;
    public Integer totalResults;
    public Integer resultsFrom;
    public String previousResultsUri;
    public String nextResultsUri;
    public String exceptionNoticeUri;
    public List<BusinessDataResult> results;
}
