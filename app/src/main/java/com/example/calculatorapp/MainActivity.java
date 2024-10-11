package com.example.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    boolean firstClickDone = false;
    TextView solutionBox, displayBox;
    MaterialButton buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine, buttonZero;
    MaterialButton buttonPlus, buttonMultiply, buttonMinus, buttonDivide, buttonEqual;
    MaterialButton buttonAC, buttonDecimal;
    MaterialButton buttonC, buttonOpenBracket, buttonCloseBracket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        solutionBox = findViewById(R.id.result_box);
        displayBox = findViewById(R.id.solution_box);
        assignID(buttonC,R.id.button_c);
        assignID(buttonEqual,R.id.button_equal);
        assignID(buttonOne,R.id.button_one);
        assignID(buttonTwo,R.id.button_two);
        assignID(buttonThree,R.id.button_three);
        assignID(buttonFour,R.id.button_four);
        assignID(buttonFive,R.id.button_five);
        assignID(buttonSix,R.id.button_six);
        assignID(buttonSeven,R.id.button_seven);
        assignID(buttonEight,R.id.button_eight);
        assignID(buttonNine,R.id.button_nine);
        assignID(buttonZero,R.id.button_zero);
        assignID(buttonOpenBracket,R.id.button_open_bracket);
        assignID(buttonCloseBracket,R.id.button_close_bracket);
        assignID(buttonMinus,R.id.button_minus);
        assignID(buttonMultiply,R.id.button_multiply);
        assignID(buttonPlus,R.id.button_plus);
        assignID(buttonDivide,R.id.button_divide);
        assignID(buttonDecimal,R.id.button_decimal);
        assignID(buttonAC,R.id.button_ac);
        displayBox.setText("44571680");
        solutionBox.setText("Krai Pongrapeeporn");

    }
    void assignID(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = displayBox.getText().toString();

        if (!firstClickDone){
            displayBox.setText("");
            solutionBox.setText("0");
            firstClickDone = true;
            return;
        }

        if (buttonText.equals("AC")){
            displayBox.setText("");
            solutionBox.setText("0");
            return;
        }

        if (buttonText.equals("=") && dataToCalculate.length() > 0){
            String finalResult = calculateData(dataToCalculate);
            if (!finalResult.equals("Unexpected error")) {
                solutionBox.setText(finalResult);  // Display the result
                displayBox.setText("");  // Reset solution box
            }
            return;
        }

        if (buttonText.equals("C")) {
            if (dataToCalculate.length() > 0) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            }
            if (dataToCalculate.isEmpty()) {
                solutionBox.setText("0");  // Reset to "0" when nothing is left
            } else {
                solutionBox.setText(dataToCalculate);  // Display the updated input
            }
            displayBox.setText(dataToCalculate);  // Update displayBox text
            return;
        }

        if (!buttonText.equals("=")) {
            dataToCalculate += buttonText;  // Append clicked button's text
            displayBox.setText(dataToCalculate);  // Update solution box
        }

        // Display the current data (before evaluation)
        solutionBox.setText(dataToCalculate);

        // Calculate and display the result after appending operators or digits
/*        String finalResult = calculateData(dataToCalculate);
        if (!finalResult.equals("Unexpected error")) {
            solutionBox.setText(finalResult);  // Update the display with calculated result
        }*/
    }

    String calculateData(String data){
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            return context.evaluateString(scriptable,data, "Javascript",1,null).toString();

        } catch (Exception e){
            return "Unexpected error";
        }
    }

}