package com.viroyal.test;

public class Test01 {
    public static void main(String[] argv) {
        int per = getPowerPercent(370/2);
        System.out.println("per=" + per);
    }

    private static int getPowerPercent(int voltage) {
        int percentVol = 0;
        voltage *= 2;

        if (voltage > 415) {
            percentVol = 100;
        } else if (voltage > 380) {
            percentVol = 80 + (voltage - 380) * 20 / (415 - 380);
        } else if (voltage > 360) {
            percentVol = 20 + (voltage - 360) * 60 / (380 - 360);
        } else if (voltage > 340) {
            percentVol = (voltage - 340) * 20 / (360 - 340);
        }

        return percentVol;
    }
}
