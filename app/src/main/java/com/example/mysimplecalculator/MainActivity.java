package com.example.mysimplecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private Boolean negative = false;
    private Boolean res = false;
    private CalculatorCore calc = new CalculatorCore();
    private final static String CALC_VALUES = "CalcValues";
    static final String TAG = "MySimpleCalculator";
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this; // может пригодитья, но не факт
        initView();
    }

    private void initView() {
        display = findViewById(R.id.textView);

        MaterialButton ac = findViewById(R.id.ac);
        MaterialButton negativeSwitch = findViewById(R.id.min_plus);
        MaterialButton percent = findViewById(R.id.percent);
        MaterialButton divide = findViewById(R.id.div);
        MaterialButton multiply = findViewById(R.id.multiply);
        MaterialButton subtraction = findViewById(R.id.minus);
        MaterialButton plus = findViewById(R.id.plus);
        MaterialButton comma = findViewById(R.id.comma);
        MaterialButton equal = findViewById(R.id.equal);
        Switch themeSwitch = (Switch) findViewById(R.id.themeSwitch);

        Map<Integer, Button> mNumberButtons = new HashMap<>();
        mNumberButtons.put(1, (MaterialButton) findViewById(R.id._1));
        mNumberButtons.put(2, (MaterialButton) findViewById(R.id._2));
        mNumberButtons.put(3, (MaterialButton) findViewById(R.id._3));
        mNumberButtons.put(4, (MaterialButton) findViewById(R.id._4));
        mNumberButtons.put(5, (MaterialButton) findViewById(R.id._5));
        mNumberButtons.put(6, (MaterialButton) findViewById(R.id._6));
        mNumberButtons.put(7, (MaterialButton) findViewById(R.id._7));
        mNumberButtons.put(8, (MaterialButton) findViewById(R.id._8));
        mNumberButtons.put(9, (MaterialButton) findViewById(R.id._9));
        mNumberButtons.put(0, (MaterialButton) findViewById(R.id._0));

        for(Integer num: mNumberButtons.keySet()) {
            Button btn = mNumberButtons.get(num);
            btn.setOnClickListener(v -> {
                if ("0".equals(display.getText().toString()) || res) {
                    display.setText("");
                    res = false;
                }
                display.append(String.valueOf(num));
            });
        }

        comma.setOnClickListener(v -> {
            if (display.getText().toString().contains(".") || res) {
                return;
            }
            display.append(".");
        });

        ac.setOnClickListener(v -> acOperation());
        equal.setOnClickListener(v -> calculate());
        negativeSwitch.setOnClickListener(v -> negativeSwitch());
        plus.setOnClickListener(v -> setOperation(CalculatorCore.OPERATIONS.PLUS));
        subtraction.setOnClickListener(v -> setOperation(CalculatorCore.OPERATIONS.SUBTRACTION));
        multiply.setOnClickListener(v -> setOperation(CalculatorCore.OPERATIONS.MULTIPLY));
        divide.setOnClickListener(v -> setOperation(CalculatorCore.OPERATIONS.DIVIDE));
        percent.setOnClickListener(v -> setOperation(CalculatorCore.OPERATIONS.PERCENT));

//        themeSwitch.setOnClickListener(v -> {
//            if(themeSwitch.isChecked()) {
//                Toast.makeText(activity, "Switch is in ON State", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(activity, "Switch is in OFF State", Toast.LENGTH_LONG).show();
//            }
//        });

        /*theme_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //setTheme(R.style.Theme_MySimpleCalculatorDark);
                    display.setText("Checked");
                } else {
                    //setTheme(R.style.Theme_MySimpleCalculator);
                    display.setText("Unchecked");
                }
                //recreate();
            }
        });*/
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        if (calc.getOperation() == CalculatorCore.OPERATIONS.NONE && !"0".equals(display.getText().toString())) {
            calc.addNumber(Double.parseDouble(display.getText().toString()));
        }
        instanceState.putParcelable(CALC_VALUES, calc);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle instanceState) {
        super.onRestoreInstanceState(instanceState);
        calc = instanceState.getParcelable(CALC_VALUES);
        restoreDisplay();
    }

    private void setOperation(CalculatorCore.OPERATIONS operations) {
        if (calc.getOperation() != CalculatorCore.OPERATIONS.NONE) {
            return;
        }
        calc.addNumber(parseString(display.getText().toString()));
        calc.setOperation(operations);
        display.setText(R.string.zero);
    }

    private void restoreDisplay() {
        if (calc.getOperand2() != null) {
            displayResult(calc.getResult());
            res = true;
            return;
        }
        if (calc.getOperand1() != null && calc.getOperation() == CalculatorCore.OPERATIONS.NONE) {
            displayResult(calc.getOperand1());
            res = true;
            return;
        }
        display.setText(R.string.zero);
    }

    private void acOperation() {
        calc.reset();
        negative = false;
        res = false;
        display.setText(R.string.zero);
    }

    private void negativeSwitch() {
        if ("0".contentEquals(display.getText())) {
            return;
        }
        negative = !negative;
        String val = display.getText().toString();
        if (negative) {
            display.setText("-");
            display.append(val);
        } else {
            display.setText(val.substring(1));
        }
    }

    private Double parseString(String val) {
        Double res = null;
        try {
            res = Double.parseDouble(val);
        } catch (ArithmeticException e) {
            Log.e(TAG, e.getMessage());
        }
        return res;
    }

    private void calculate() {
        if (calc.getOperation() == CalculatorCore.OPERATIONS.NONE) {
            return;
        }
        calc.addNumber(Double.parseDouble(display.getText().toString()));

        displayResult(calc.getResult());

        res = true;
        calc.reset();
    }

    private void displayResult(Double res) {
        if (res == null) {
            display.setText(R.string.error);
            return;
        }

        // не знаю лучшего способа не выводить 0 если он не значащий.
        // способы с форматированием или с извлечением дробной части в числовых значения сложнее моего.
        String[] val = String.valueOf(res).split("\\."); // Android Studio предлагает
                                                                // этот вариант вместо просто точки
        if (val[1].length() == 1 && "0".equals(val[1])) {
            display.setText(val[0]);
        } else {
            display.setText(String.valueOf(res));
        }
    }
}