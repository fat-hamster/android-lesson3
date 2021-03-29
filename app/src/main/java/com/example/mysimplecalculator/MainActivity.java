package com.example.mysimplecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private Boolean negative = false;
    private Boolean res = false;
    private CalculatorCore calc = new CalculatorCore();
    private final static String CalcValues = "CalcValues";
    static final String TAG = "MySimpleCalculator";


    /* Code operations
        1 - plus
        2 - minus
        3 - multiply
        4 - divide
        5 - percent
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        display = findViewById(R.id.textView);

        Button ac = findViewById(R.id.ac);
        Button negativeSwitch = findViewById(R.id.min_plus);
        Button percent = findViewById(R.id.percent);
        Button divide = findViewById(R.id.div);
        Button multiply = findViewById(R.id.multiply);
        Button minus = findViewById(R.id.minus);
        Button plus = findViewById(R.id.plus);
        Button comma = findViewById(R.id.comma);
        Button equal = findViewById(R.id.equal);
        Switch theme_switch = (Switch) findViewById(R.id.theme_switch);

        Map<Integer, Button> mNumberButtons = new HashMap<>();
        mNumberButtons.put(1, (Button) findViewById(R.id._1));
        mNumberButtons.put(2, (Button) findViewById(R.id._2));
        mNumberButtons.put(3, (Button) findViewById(R.id._3));
        mNumberButtons.put(4, (Button) findViewById(R.id._4));
        mNumberButtons.put(5, (Button) findViewById(R.id._5));
        mNumberButtons.put(6, (Button) findViewById(R.id._6));
        mNumberButtons.put(7, (Button) findViewById(R.id._7));
        mNumberButtons.put(8, (Button) findViewById(R.id._8));
        mNumberButtons.put(9, (Button) findViewById(R.id._9));
        mNumberButtons.put(0, (Button) findViewById(R.id._0));

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
        negativeSwitch.setOnClickListener(v -> negativeSwitch());
        plus.setOnClickListener(v -> plus());
        minus.setOnClickListener(v -> minus());
        multiply.setOnClickListener(v -> multiply());
        divide.setOnClickListener(v -> divide());
        equal.setOnClickListener(v -> calculate());
        percent.setOnClickListener(v -> percent());

        theme_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(MainActivity.this,
                        "Switch On", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,
                        "Switch Off", Toast.LENGTH_SHORT).show();
            }
        });

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
        if (calc.getOperation() == 0 && !"0".equals(display.getText().toString())) {
            calc.addNumber(Double.parseDouble(display.getText().toString()));
        }
        instanceState.putParcelable(CalcValues, calc);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle instanceState) {
        super.onRestoreInstanceState(instanceState);
        calc = instanceState.getParcelable(CalcValues);
        restoreDisplay();
    }

    private void restoreDisplay() {
        if (calc.getOperand2() != null) {
            displayResult(calc.getResult());
            res = true;
            return;
        }
        if (calc.getOperand1() != null && calc.getOperation() == 0) {
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

    private void plus() {
        if (calc.getOperation() != 0) {
            return;
        }
        calc.addNumber(parseString(display.getText().toString()));
        calc.setOperation(1);
        display.setText(R.string.zero);
    }

    private void minus() {
        if (calc.getOperation() != 0) {
            return;
        }
        calc.addNumber(parseString(display.getText().toString()));
        calc.setOperation(2);
        display.setText(R.string.zero);
    }

    private void multiply() {
        if (calc.getOperation() != 0) {
            return;
        }
        calc.addNumber(parseString(display.getText().toString()));
        calc.setOperation(3);
        display.setText(R.string.zero);
    }

    private void divide() {
        if (calc.getOperation() != 0) {
            return;
        }
        calc.addNumber(parseString(display.getText().toString()));
        calc.setOperation(4);
        display.setText(R.string.zero);
    }

    private void percent() {
        if (calc.getOperation() != 0) {
            return;
        }
        calc.addNumber(parseString(display.getText().toString()));
        calc.setOperation(5);
        display.setText(R.string.zero);
    }

    private void calculate() {
        if (calc.getOperation() == 0) {
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
        String[] val = String.valueOf(res).split("\\.");
        if (val[1].length() == 1 && "0".equals(val[1])) {
            display.setText(val[0]);
        } else {
            display.setText(String.valueOf(res));
        }
    }
}