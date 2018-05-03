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

    private int ave, str;
    private String hLoc;

    public House(int avenue, int street, String houseloc) {
        ave = avenue;
        str = street;
        hLoc = houseloc;
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
    public static int getDistance(House a, House b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }
    public static int getDistancePoint(House a, int[] b) {
        return (int) (Math.pow(Math.abs(a.getX() - b[0]),2) + Math.pow(Math.abs(a.getY() - b[1]),2));
    }
    public String toString() {
        return "(" + ave + "," + str + "," + hLoc + ")";
    }
}
