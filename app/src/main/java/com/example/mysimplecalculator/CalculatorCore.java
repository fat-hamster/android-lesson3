package com.example.mysimplecalculator;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import static com.example.mysimplecalculator.CalculatorCore.OPERATIONS.*;

public class CalculatorCore implements Parcelable {
    private Double operand1;
    private Double operand2;
    private OPERATIONS operation;
    private Double result;

    public enum OPERATIONS{
        PLUS,
        SUBTRACTION,
        MULTIPLY,
        DIVIDE,
        PERCENT,
        NONE
    }

    public CalculatorCore() {
        operand1 = null;
        operand2 = null;
        result = null;
        operation = NONE;
    }

    private CalculatorCore(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);
        if ("null".equals(data[0])) {
            result = null;
        } else {
            result = Double.parseDouble(data[0]);
        }
        if ("null".equals(data[1])) {
            operand1 = null;
        } else {
            operand1 = Double.parseDouble(data[1]);
        }
        if ("null".equals(data[2])) {
            operand2 = null;
        } else {
            operand2 = Double.parseDouble(data[2]);
        }

        operation = OPERATIONS.valueOf(data[3]);
        MainActivity.dark = Boolean.valueOf(data[4]); // восстанавливаем значение темы
    }

    public void setOperation(OPERATIONS operation) {
        this.operation = operation;
    }

    public Double getOperand1() {
        return operand1;
    }

    public Double getOperand2() {
        return operand2;
    }

    public OPERATIONS getOperation() {
        return operation;
    }

    public void reset() {
        operand1 = null;
        operand2 = null;
        result = null;
        operation = NONE;
    }

    public void addNumber(Double num) {
        if (operand1 != null && operand2 != null) {
            return;
        }
        if (operation == NONE) {
            operand1 = num;
        } else {
            operand2 = num;
        }
    }

    public Double getResult() {
        switch (operation) {
            case PLUS:
                return sum();
            case SUBTRACTION:
                return sub();
            case MULTIPLY:
                return mult();
            case DIVIDE:
                return div();
            case PERCENT:
                return percent();
        }
        return result;
    }

    private Double sum() {
        return (Double) (operand1 + operand2);
    }

    private Double sub() {
        return (Double) (operand1 - operand2);
    }

    private Double mult() {
        return (Double) (operand1 * operand2);
    }

    private Double div() {
        try {
            return operand1 / operand2;
        } catch (ArithmeticException e) {
            Log.e(MainActivity.TAG, "Divide by zero");
            return null;
        }
    }

    private Double percent() {
        return (Double) (operand1 * (operand2 / 100));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(result), String.valueOf(operand1),
                String.valueOf(operand2), operation.toString(), MainActivity.dark.toString()});
    }

    public static final Parcelable.Creator<CalculatorCore> CREATOR = new Parcelable.Creator<CalculatorCore>() {

        @Override
        public CalculatorCore createFromParcel(Parcel source) {
            return new CalculatorCore(source);
        }

        @Override
        public CalculatorCore[] newArray(int size) {
            return new CalculatorCore[size];
        }
    };
}
