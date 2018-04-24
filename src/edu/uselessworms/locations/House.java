package edu.uselessworms.locations;

public class House {
    private int x;

    public int getX() {
        return x;
    }

    private int y;

    public int getY() {
        return y;
    }

    public House(int avenue, int street, String houseloc) {
        x = 200 * avenue - 100;
        int yOffset;
        switch(houseloc.charAt(0))
        {
            case 'A':
                yOffset = 50;
                break;
            case 'B':
                yOffset = 150;
                break;
            case 'C':
                yOffset = 250;
                break;
            case 'D':
                yOffset = 350;
                break;
            case 'E':
                yOffset = 450;
                break;
            case 'F':
                yOffset = 550;
                break;
            case 'G':
                yOffset = 650;
                break;
            case 'H':
                yOffset = 750;
                break;
            case 'I':
                yOffset = 850;
                break;
            case 'J':
                yOffset = 950;
                break;
            default:
                yOffset = 0;
                break;
        }
        y = 1000 * street + yOffset;
    }
}
