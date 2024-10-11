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

    /**
     * This is an auxiliary function, used to assign a MaterialButton object to its associated view component
     * by attaching the ID of the view component
     * @param btn The MaterialButton object
     * @param id The id of the View component
     */
    void assignID(MaterialButton btn, int id){
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    /**
     * This function handles click events by overriding Views that implements OnClickListener
     * It obtains the button being pressed and the current contents of the calculator Display
     * Then it checks what button is being pressed, for AC, =, and C, special operations are executed
     * AC - clears the Display and Solution Display
     * = - Makes sure there is currently something in the Display, and then evaluates the string currently
     *     in the Display, pushes the results onto the Solution Display, and clears the Display for further operations
     * C - Erases the last character in Display, and makes sure there is something to erase. If only one character is
     *     in display to erase, replaces it with zero
     * If non of the special buttons have been clicked, appends the Display with whatever button was pressed
     * The handler also checks if it's the first button click in the app, so that it can erase the initialization text
     * This function relies on calculateData() to obtain the results, by passing a numerical expression to be evaluated
     * by Javascript as code
     * Because it is using the Javascript engine to evaluate numerical expressions, there can be some odd results
     * such as numbers starting with 0 being treated as an octal number (base 8)
     * @param view The View component being examined to perform operations, it is a MaterialButton and this button
     *             is what the user clicks on
     */
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
                solutionBox.setText(finalResult);
                displayBox.setText("");
            }
            return;
        }

        if (buttonText.equals("C")) {
            if (dataToCalculate.length() > 0) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            }
            if (dataToCalculate.isEmpty()) {
                solutionBox.setText("0");
            } else {
                solutionBox.setText(dataToCalculate);
            }
            displayBox.setText(dataToCalculate);
            return;
        }

        if (!buttonText.equals("=")) {
            dataToCalculate += buttonText;
            displayBox.setText(dataToCalculate);
            solutionBox.setText(dataToCalculate);
            return;
        }



    }

    /**
     * This function executes a string as Javascript code
     * This is achieved by using Rhino, a Javascript engine used in Java applications
     * First the function initializes a scope to execute the Javascript, sets the Optimization level for Rhino
     * and then the Javascript code is executed.
     * @param data This is the Javascript code in the form of a String
     * @return The result of the evaluation is returned as a String
     */
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