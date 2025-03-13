package com.example.myapplication_java;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication_java.model.Payment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddPaymentDialog.PaymentDialogListener {
    private static final String FILE_NAME = "LastPayment.txt";
    private static final String TAG = "MainActivity";

    private List<Payment> payments = new ArrayList<>();
    private TextView totalAmountTextView;
    private ChipGroup paymentChipGroup;
    private TextView addPaymentLink;
    private Button saveButton;
    private String originalPaymentsJson = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        paymentChipGroup = findViewById(R.id.paymentChipGroup);
        addPaymentLink = findViewById(R.id.addPaymentLink);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setEnabled(false);

        loadPayments();

        addPaymentLink.setOnClickListener(v -> showAddPaymentDialog());
        saveButton.setOnClickListener(v -> savePayments());

        updatePaymentDisplay();
    }

    private void showAddPaymentDialog() {
        List<String> allPaymentTypes = Arrays.asList(
                Payment.TYPE_CASH,
                Payment.TYPE_BANK_TRANSFER,
                Payment.TYPE_CREDIT_CARD
        );

        List<String> availableTypes = new ArrayList<>();
        for (String type : allPaymentTypes) {
            if (!isPaymentTypeUsed(type)) {
                availableTypes.add(type);
            }
        }

        if (availableTypes.isEmpty()) {
            Toast.makeText(this, "All payment types already added", Toast.LENGTH_SHORT).show();
            return;
        }

        AddPaymentDialog dialog = new AddPaymentDialog(this, availableTypes);
        dialog.setListener(this);
        dialog.show();
    }

    private boolean isPaymentTypeUsed(String type) {
        for (Payment payment : payments) {
            if (payment.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPaymentAdded(Payment payment) {
        payments.add(payment);
        updatePaymentDisplay();
    }

    private void updatePaymentDisplay() {
        double total = 0;
        for (Payment payment : payments) {
            total += payment.getAmount();
        }
        totalAmountTextView.setText(String.format("₹%.2f", total));
        paymentChipGroup.removeAllViews();

        for (int i = 0; i < payments.size(); i++) {
            Payment payment = payments.get(i);
            final int index = i;

            Chip chip = new Chip(this);
            chip.setText(String.format("%s - ₹%.2f", payment.getType(), payment.getAmount()));
            chip.setCloseIconVisible(true);

            chip.setOnCloseIconClickListener(v -> {
                payments.remove(index);
                updatePaymentDisplay();
            });

            paymentChipGroup.addView(chip);
        }

        String currentJson = getCurrentPaymentsJson();

        boolean hasChanges = !currentJson.equals(originalPaymentsJson);
        saveButton.setEnabled(hasChanges);


    }

    private String getCurrentPaymentsJson() {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Payment payment : payments) {
                jsonArray.put(payment.toJson());
            }
            return jsonArray.toString();
        } catch (Exception e) {
            Toast.makeText(this, "Error converting payments to JSON", Toast.LENGTH_SHORT).show();
            return "[]";
        }
    }

    private void savePayments() {
        try {
            String currentJson = getCurrentPaymentsJson();
            if (currentJson.equals(originalPaymentsJson)) {
                Toast.makeText(this, "No changes to save", Toast.LENGTH_SHORT).show();
                return;
            }

            File file = new File(getFilesDir(), FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(currentJson.getBytes());
            fos.close();

            originalPaymentsJson = currentJson;

            Toast.makeText(this, "Payments saved", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error saving payments", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPayments() {
        payments.clear();

        File file = new File(getFilesDir(), FILE_NAME);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(isr);

                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();

                String jsonContent = content.toString();
                originalPaymentsJson = jsonContent;

                JSONArray jsonArray = new JSONArray(jsonContent);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Payment payment = Payment.fromJson(jsonObject);
                    payments.add(payment);
                }

            } catch (Exception e) {
                Toast.makeText(this, "Error reading from internal storage", Toast.LENGTH_SHORT).show();
                originalPaymentsJson = ""; // Reset on error
            }
        } else {
            originalPaymentsJson = ""; // No file exists yet
        }

    }
}