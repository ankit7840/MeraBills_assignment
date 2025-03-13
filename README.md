# Payment Management App

## Features

1. **Add Payments**: Users can add different types of payments (Cash, Bank Transfer, Credit Card) through a user-friendly dialog interface.
2. **Display Payments**: Payments are displayed using `Chip` components, showing the type and amount of each payment.
3. **Remove Payments**: Users can remove payments by clicking the close icon on each `Chip`.
4. **Save Payments**: Payments are saved to a local file (`LastPayment.txt`) to persist data between app sessions.
5. **Load Payments**: Payments are loaded from the local file when the app starts, ensuring data persistence.
6. **Total Amount Calculation**: The app calculates and displays the total amount of all payments.


## Edge Cases Handled

1. **Empty Amount**: The app checks if the amount field is empty and prompts the user to enter an amount.
2. **Invalid Amount Format**: The app handles invalid amount formats and shows an error message.
3. **Missing Provider/Reference**: For non-cash payments, the app ensures that the provider and reference fields are filled.
4. **Duplicate Payment Types**: The app prevents adding duplicate payment types.
5. **No Changes to Save**: The app checks if there are any changes before saving to avoid unnecessary file operations.
6. **File Read/Write Errors**: The app handles errors during file read/write operations and shows appropriate error messages.

## Instructions to Check `LastPayment.txt` File

1. **Open Android Studio**: Launch Android Studio and open your project.
2. **Run Your Application**: Ensure your application is running on an emulator or a connected device.
3. **Open Device File Explorer**:
   - Go to `View` > `Tool Windows` > `Device File Explorer`.
   - Alternatively, you can find the `Device File Explorer` icon on the bottom-right corner of Android Studio.
4. **Navigate to App Files**:
   - In the `Device File Explorer`, navigate to the following path:
     ```
     /data/data/com.example.myapplication_java/files/
     ```
   - Replace `com.example.myapplication_java` with your actual application package name if it is different.
5. **Locate `LastPayment.txt`**:
   - Find the `LastPayment.txt` file in the `files` directory.
6. **View or Download the File**:
   - Right-click on `LastPayment.txt` and select `Save As...` to download the file to your local machine.
   - Alternatively, you can double-click the file to view its contents directly in Android Studio.
