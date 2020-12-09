package com.example.cofinder;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.net.NetworkRequest;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cofinder.common.Api;
import com.example.cofinder.common.Utils;
import com.example.cofinder.schema.BisAddress;
import com.example.cofinder.schema.BisCompanyContactDetail;
import com.example.cofinder.schema.BusinessData;
import com.example.cofinder.schema.BusinessDataResult;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "CoFinderMSG";
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    LinearLayout linearLayoutScrollView;
    TextView textViewCompanyId;
    EditText editTextCompanyId;
    Button buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initializeData();
        initializeViews();
    }

    private void initializeData() {

    }

    private void initializeViews() {
        linearLayoutScrollView = findViewById(R.id.linearLayoutScrollView);
        textViewCompanyId = findViewById(R.id.textViewCompanyId);
        editTextCompanyId = findViewById(R.id.editTextCompanyId);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(buttonSearchOnClick);
    }

    private void switchViewMode() {
        LinearLayout.LayoutParams textViewCompanyIdParams = (LinearLayout.LayoutParams) textViewCompanyId.getLayoutParams();
        textViewCompanyIdParams.setMargins(0, 32, 0, 32);
        textViewCompanyId.setLayoutParams(textViewCompanyIdParams);
    }

    private void displayBusinessData(BusinessData businessData) {
        linearLayoutScrollView.removeAllViews();

        BusinessDataResult businessDataResult = businessData.results.get(0);
        String businessId = businessDataResult.businessId;
        String name = businessDataResult.name;
        List<BisAddress> addresses = businessDataResult.addresses;
        List<BisCompanyContactDetail> contactDetails = businessDataResult.contactDetails.stream().filter(contact -> contact.language.equals("FI")).collect(Collectors.toList());

        TextView businessIdLabel = (TextView) getLayoutInflater().inflate(R.layout.item_label, linearLayoutScrollView, false);
        TextView companyNameLabel = (TextView) getLayoutInflater().inflate(R.layout.item_label, linearLayoutScrollView, false);
        TextView businessIdItem = (TextView) getLayoutInflater().inflate(R.layout.item, linearLayoutScrollView, false);
        TextView companyNameItem = (TextView) getLayoutInflater().inflate(R.layout.item, linearLayoutScrollView, false);
        TextView addressesLabel = (TextView) getLayoutInflater().inflate(R.layout.item_label, linearLayoutScrollView, false);

        businessIdLabel.setText(R.string.business_id);
        companyNameLabel.setText(R.string.company_name);
        businessIdItem.setText(businessId);
        companyNameItem.setText(name);
        addressesLabel.setText(R.string.addresses);

        linearLayoutScrollView.addView(businessIdLabel);
        linearLayoutScrollView.addView(businessIdItem);
        linearLayoutScrollView.addView(companyNameLabel);
        linearLayoutScrollView.addView(companyNameItem);
        linearLayoutScrollView.addView(addressesLabel);

        for (BisAddress address : addresses) {
            TextView addressItem = (TextView) getLayoutInflater().inflate(R.layout.item, linearLayoutScrollView, false);
            addressItem.setText(String.format("%s, %s, %s", address.street, address.postCode, address.city));
            addressItem.setOnClickListener(addressOnClick);
            linearLayoutScrollView.addView(addressItem);
        }

        TextView contactsLabel = (TextView) getLayoutInflater().inflate(R.layout.item_label, linearLayoutScrollView, false);
        contactsLabel.setText(R.string.contacts);
        linearLayoutScrollView.addView(contactsLabel);

        for (BisCompanyContactDetail contact : contactDetails) {
            TextView contactItem = (TextView) getLayoutInflater().inflate(R.layout.item, linearLayoutScrollView, false);
            contactItem.setText(contact.value);

            if (contact.type.equals("Matkapuhelin")) {
                contactItem.setOnClickListener(phoneOnClick);
            } else if (contact.type.equals("Kotisivun www-osoite")) {
                contactItem.setOnClickListener(websiteOnClick);
            }

            linearLayoutScrollView.addView(contactItem);
        }
    }

    TextView.OnClickListener phoneOnClick = view -> {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", ((TextView) view).getText().toString(), null));
        startActivity(intent);
    };

    TextView.OnClickListener websiteOnClick = view -> {
        String url = ((TextView) view).getText().toString();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    };

    TextView.OnClickListener addressOnClick = view -> {
        String address = ((TextView) view).getText().toString();
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);
    };

    Response.Listener<JSONObject> apiResponseListener = response -> {
        Gson gson = new Gson();
        BusinessData businessData = gson.fromJson(response.toString(), BusinessData.class);

        switchViewMode();
        displayBusinessData(businessData);
    };

    Response.ErrorListener apiErrorListener = error -> {
        Log.d(TAG, ": " + error.getMessage());
    };

    Button.OnClickListener buttonSearchOnClick = (view) -> {
        String businessId = editTextCompanyId.getText().toString();
        String apiUrl = Utils.buildApiUrl(businessId);

        Api.sendApiRequest(this, apiUrl, apiResponseListener, apiErrorListener);
    };

    public static Context getContext() {
        return mContext;
    }
}