package com.example.myapplication_java;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication_java.model.Payment;

import java.util.List;

public class AddPaymentDialog extends Dialog {

    private List<String> availableTypes;
    private PaymentDialogListener listener;

    private Spinner paymentTypeSpinner;
    private EditText amountEditText;
    private LinearLayout additionalDetailsLayout;
    private EditText providerEditText;
    private EditText referenceEditText;

    public interface PaymentDialogListener {
        void onPaymentAdded(Payment payment);
    }

    public AddPaymentDialog(Context context, List<String> availableTypes) {
        super(context);
        this.availableTypes = availableTypes;
    }

    public void setListener(PaymentDialogListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_payment);

        paymentTypeSpinner = findViewById(R.id.paymentTypeSpinner);
        amountEditText = findViewById(R.id.amountEditText);
        additionalDetailsLayout = findViewById(R.id.additionalDetailsLayout);
        providerEditText = findViewById(R.id.providerEditText);
        referenceEditText = findViewById(R.id.referenceEditText);
        Button addButton = findViewById(R.id.addButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        // Setup  of spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                availableTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentTypeSpinner.setAdapter(adapter);

        // Handling  selection changes
        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = availableTypes.get(position);
                if (selectedType.equals(Payment.TYPE_CASH)) {
                    additionalDetailsLayout.setVisibility(View.GONE);
                } else {
                    additionalDetailsLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                additionalDetailsLayout.setVisibility(View.GONE);
            }
        });

        addButton.setOnClickListener(v -> {
            if (validateInput()) {
                createAndAddPayment();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private boolean validateInput() {
        if (amountEditText.getText().toString().isEmpty()) {
            amountEditText.setError("Please enter an amount");
            return false;
        }

        if (!availableTypes.isEmpty() &&
                !paymentTypeSpinner.getSelectedItem().toString().equals(Payment.TYPE_CASH)) {
            if (providerEditText.getText().toString().isEmpty()) {
                providerEditText.setError("Provider is required");
                return false;
            }
            if (referenceEditText.getText().toString().isEmpty()) {
                referenceEditText.setError("Reference is required");
                return false;
            }
        }
        return true;
    }

    private void createAndAddPayment() {
        try {
            String type = paymentTypeSpinner.getSelectedItem().toString();
            double amount = Double.parseDouble(amountEditText.getText().toString());

            Payment payment;
            if (type.equals(Payment.TYPE_CASH)) {
                payment = new Payment(amount, type);
            } else {
                String provider = providerEditText.getText().toString();
                String reference = referenceEditText.getText().toString();
                payment = new Payment(amount, type, provider, reference);
            }

            if (listener != null) {
                listener.onPaymentAdded(payment);
            }
            dismiss();
        } catch (NumberFormatException e) {
            amountEditText.setError("Invalid amount format");
        }
    }
}

