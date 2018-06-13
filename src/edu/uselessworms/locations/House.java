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
    public House(House q) {
        x = q.getX();
        y = q.getY();
        ave = (x+100)/200;
        str = ((int) (y/1000));
        int offset = y-str;
        switch(offset)
        {
            case 50:
                hLoc = "A";
            case 150:
                hLoc = "B";
            case 250:
                hLoc = "C";
            case 350:
                hLoc = "D";
            case 450:
                hLoc = "E";
            case 550:
                hLoc = "F";
            case 650:
                hLoc = "G";
            case 750:
                hLoc = "H";
            case 850:
                hLoc = "I";
            case 950:
                hLoc = "J";
        }
    }
    public House(int street, int avenue, String houseloc) {
        ave = street;
        str = avenue;
        hLoc = houseloc;
        x = 200 * street - 100;
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
        y = 1000 * (avenue-1) + yOffset;
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
    public static boolean isSame(House a, House b) {
        return (a.getX() == b.getX() && a.getY() == b.getY());
    }
}
