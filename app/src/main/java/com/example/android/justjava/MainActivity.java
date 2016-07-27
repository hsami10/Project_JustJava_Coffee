package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //called when + button is clicked
    public void increment(View view) {
        if (quantity == 100)
            Toast.makeText(MainActivity.this, "You cannot have more than a 100 coffees", Toast.LENGTH_SHORT).show();
        else {
            quantity = quantity + 1;
            display(quantity);
        }
    }

    //method called when - button is clicked
    public void decrement(View view) {
        if (quantity == 1)
            Toast.makeText(MainActivity.this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
        else {
            quantity = quantity - 1;
            display(quantity);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }


    /**
     * Calculates the price of the order.
     *
     * @return total price.
     */
    private int calculatePrice(boolean hasCream, boolean hasChocolate) {
        int basePrice = 5;

        if (hasCream)
            basePrice += 1;

        if (hasChocolate)
            basePrice += 2;

        return quantity * basePrice;
    }

    /**
     * creates the order summary for the order
     *
     * @param price        is price of order
     * @param hasCream     is whether or not the customer wants whipped cream topping
     * @param hasChocolate is whether the customer wants chocolate topping
     * @param name         is the entered name of the customer
     * @return order text summary
     */
    private String createOrderSummary(int price, boolean hasCream, boolean hasChocolate, String name) {
        return getString(R.string.order_summary_name, name) +
                "\n" + getString(R.string.order_summary_whipped_cream, hasCream) +
                "\n" + getString(R.string.order_summary_chocolate, hasChocolate) +
                "\n" + getString(R.string.order_summary_quantity, quantity) +
                "\n" + getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price)) +
                "\n" + getString(R.string.thank_you);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //get the name entered in text field
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        String name = nameEditText.getText().toString();

        //get the state of the whipped cream checkbox
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasCream = whippedCreamCheckBox.isChecked();
        //use the log below to see if code above works. If it does then we don't need it anymore, so take it out
        //Log.v("MainActivity", "The checkbox is checked: " + hasCream);

        //get the state of the chocolate checkbox
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int cost = calculatePrice(hasCream, hasChocolate);

        String priceMessage = createOrderSummary(cost, hasCream, hasChocolate, name);

        /*
        Email the order summary to the customer
        Use the email Intent
         */
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); //only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}