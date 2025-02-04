package aufgabe3;


import java.util.HashMap;


public class Main {
    
    static HashMap<String, Integer> map = new HashMap<>();
    static HashMap<String, Integer> map2 = new HashMap<>();


    public static void main(String[] args) {    
        { // Setup
            for (int x = 0; x < 9; x++) {
                for (int y = 0; y < 9; y++) {
                    map.put(toString(x,y), 0);
                    map2.put(toString(x,y), 0);
                }
            }
            map.replace("0,0", 1);
            map2.replace("0,8", 1);
            System.out.println("Map");
            for (int y = 0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    System.out.print(map.get(toString(x,y)) + " ");
                }
                System.out.println("");
            }
            System.out.println("Map 2");
            for (int y = 0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    System.out.print(map2.get(toString(x,y)) + " ");
                }
                System.out.println("");
            }
        }

        // For each block
        for (int i = 0; i < 9; i += 3) {

            System.out.println("");

            // For each line in a block
            for (int ii = 0; ii < 3; ii++) {

                // Check for bow/collumn permutation within the block
                for (int iii = 0; iii < 3; iii++) {

                    if (i + ii != i + iii) {

                        if (checkRow(i + ii, i + iii)) {
                            System.out.println("Das Sudoku wurde wiederverwendet");
                            return;
                        }
                        if (checkCollumn(i + ii, i + iii)) {
                            System.out.println("Das Sudoku wurde wiederverwendet");
                            return;
                        }

                    }

                }

            }


            System.out.println("");


            // Check for row-/collumn-Block permutation
            for (int ii = 0; ii < 9; ii += 3) {

                if (i != ii) {

                    if (checkRowBlock(i, ii)) {
                        System.out.println("Das Sudoku wurde wiederverwendet!");
                        return;
                    };
                    if (checkCollumnBlock(i, ii)) {
                        System.out.println("Das Sudoku wurde wiederverwendet!");
                        return;
                    };

                }

            }

        }

        if (checkRotation()) {
            System.out.println("Das Sudoku wurde wiederverwendet!");
            return;
        };

        System.out.println("Die 2 Sudokus sind verschieden!");

    }
    

    public static String toString(int x, int y) {
        return x + "," + y;
    }


    public static boolean checkRow(int y, int y2) {
        
        for (int x = 0; x < 9; x++) {
        
            if (map.get(toString(x,y)) != map2.get(toString(x,y2))) return false;
        
        }
        
        // If all numbers are 0 return false, else return true   
        for (int x = 0; x < 9; x++) {
            if (map.get(toString(x,y)) == 0) continue;
            return true;
        }      
        return false;

    }


    public static boolean checkCollumn(int x, int x2) {
        
        for (int y = 0; y < 9; y++) {
        
            if (map.get(toString(x,y)) != map2.get(toString(x2, y))) return false;
        
        }

        // If all numbers are 0 return false, else return true        
        for (int y = 0; y < 9; y++) {
            if (map.get(toString(x,y)) == 0) continue;
            return true;
        }
        return false;

    }


    public static boolean checkRowBlock(int y, int y2) {

        // For each line in the block
        for (int i = 0; i < 3; i++) {

            // Check for bow/collumn permutation within the block
            for (int ii = 0; ii < 3; ii++) {

                if (checkRow(y + i, y2 + ii)) return true;
                if (checkCollumn(y + i, y2 + ii)) return true;

            }

        }

        return false;

    }


    public static boolean checkCollumnBlock(int x, int x2) {

        // For each line in the block
        for (int i = 0; i < 3; i++) {

            // Check for bow/collumn permutation within the block
            for (int ii = 0; ii < 3; ii++) {

                if (checkRow(x + i, x2 + ii)) return true;
                if (checkCollumn(x + i, x2 + ii)) return true;

            }

        }

        return false;
        
    }

    
    public static boolean checkRotation() {

        // 90 degree rotation
        boolean breakBoth = false;
        for (int x = 0; x < 9; x++) {

            for (int y = 0; y < 9; y++) {

                if (map.get(toString(x,y)) != map2.get(toString(8 - y, x))) {
                    breakBoth = true;
                    System.out.println("missmatch 90");
                    break;
                };

            }

            if (breakBoth) break;

        }

        if (!breakBoth) return true;


        // 180 degree rotation
        breakBoth = false;
        for (int x = 0; x < 9; x++) {

            for (int y = 0; y < 9; y++) {

                if (map.get(toString(x,y)) != map2.get(toString(8 - x, 8 - y))) {
                    breakBoth = true;
                    System.out.println("missmatch 180");
                    break;
                };

            }

            if (breakBoth) break;

        }

        if (!breakBoth) return true;

        // 270 degree rotation
        breakBoth = false;
        for (int x = 0; x < 9; x++) {

            for (int y = 0; y < 9; y++) {

                if (map.get(toString(x,y)) != map2.get(toString(y, 8 - x))) {
                    breakBoth = true;
                    System.out.println("missmatch 270");
                    break;
                };

            }

            if (breakBoth) break;

        }

        if (!breakBoth) return true;

        return false;
    }

}