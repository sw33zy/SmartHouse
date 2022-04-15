package View;

import java.time.LocalDate;
import java.util.*;

public class View {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD = "\u001B[1m";

    private static void clearConsoleScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void currentDate(LocalDate currDate){
        System.out.println("\n                  CURRENT DATE: " + currDate);
    }

    private static void line(){
        String CSI = "\u001B[";
        System.out.print (CSI + "33" + "m");
        System.out.print(" ----------------------------------------------------------");
        System.out.println (CSI + "m");
    }

    public static int Menu(LocalDate currDate) {
        clearConsoleScreen();
        String CSI = "\u001B[";
        System.out.println();
        System.out.print (CSI + "33" + "m");
        System.out.print ("*************************** MENU ***************************");
        currentDate(currDate);
        System.out.println (CSI + "m");
        System.out.print (CSI + "33" + "m");
        System.out.print ("                      Select an option");
        System.out.println (CSI + "m");
        line();
        System.out.println("1- Pass Time.");
        line();
        System.out.println("2- List invoices by supplier.");
        line();
        System.out.println("3- Top house spender by period.");
        line();
        System.out.println("4- Top supplier to date.");
        line();
        System.out.println("5- Top energy consumers by period.");
        line();
        System.out.println("6- Check current houses state.");
        line();
        System.out.println("7- Check current energy suppliers state.");
        line();
        System.out.println("8- Toggle house device(s).");
        line();
        System.out.println("9- Change supplier energy rates.");
        line();
        System.out.println("10- Change client energy supplier.");
        line();
        System.out.println("11- Save state.");
        line();
        System.out.println("12- Load state.");
        line();
        System.out.print (CSI + "31" + "m");
        System.out.println("0- Leave.\n");
        System.out.println (CSI + "m");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();

    }

    public static int askInt(String msg){
        System.out.print(msg);
        Scanner input = new Scanner(System.in);
        boolean ok = false;
        int i = 0;
        while(!ok) {
            try {
                i = input.nextInt();
                ok = true;
            }
            catch(InputMismatchException e)
            { System.out.println("Invalid Integer");
                System.out.print("New input: ");
                input.nextLine();
            }
        }
        //input.close();
        return i;
    }

    public static float askFloat(String msg){
        System.out.print(msg);
        Scanner input = new Scanner(System.in);
        boolean ok = false;
        float i = 0;
        while(!ok) {
            try {
                i = input.nextFloat();
                ok = true;
            }
            catch(InputMismatchException e)
            { System.out.println("Invalid Value");
                System.out.print("New input: ");
                input.nextLine();
            }
        }
        //input.close();
        return i;
    }

    public static String askString(String message){
        System.out.println(message);
        Scanner input = new Scanner(System.in);
        boolean ok = false;
        String txt = "";
        while(!ok) {
            try {
                txt = input.nextLine();
                ok = true;
            }
            catch(InputMismatchException e)
            { System.out.println("Invalid text");
                System.out.print("New Input: ");
                input.nextLine();
            }
        }
        //input.close();
        return txt;
    }

    public static void showError(String msg){
        System.out.println(ANSI_RED + msg + ANSI_RESET);
    }


    public static int showSuppliers(Map<Integer, String> list, String msg){
        System.out.println(ANSI_YELLOW + msg + ANSI_RESET);
        int i = 1;
        for(Map.Entry<Integer, String> entry : list.entrySet()){
            System.out.println("\t" + (i++) + ")ID - " + entry.getKey() + " Name - " + entry.getValue());
        }
        return askInt("\nInsert ID of supplier: ");
    }

    public static void showInvoices(Map<String, String> invoices){
        if(invoices.size() == 0)
            showError("\nThis supplier hasn't generated any invoices yet!");
        for(Map.Entry<String, String> entry : invoices.entrySet()){
            line();
            System.out.println("Invoice nº - " + entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    public static void showTopHouse(AbstractMap.Entry<String, Float> entry, LocalDate from , LocalDate till){
        System.out.println(ANSI_YELLOW + "\nTop spending house in the period: " + from + " - " + till + ANSI_RESET);
        System.out.println("> Owner Name: " + entry.getKey());
        System.out.println("> Total spent: " + entry.getValue() + "€");
    }

    public static void showTopSupplier(AbstractMap.Entry<String, Float> entry){
        System.out.println(ANSI_YELLOW + "\nTop supplier to date" + ANSI_RESET);
        System.out.println("> Supplier Name: " + entry.getKey());
        System.out.println("> Total earned: " + entry.getValue() + "€");
    }

    public static void showTopConsumers(Map<String, Float> top, LocalDate from, LocalDate till){
        if(top.size() == 0) {
            showError("\nThere haven't been generated any invoices");
            return;
        }
        System.out.println(ANSI_YELLOW + "\nTop energy consuming houses in the period: " + from + " - " + till + ANSI_RESET);
        int i = top.size();
        for(Map.Entry<String, Float> entry : top.entrySet()){
            System.out.println("\t" + (i--) + ") Owner Name: " + entry.getKey() + "; Consumption: " + entry.getValue() + "kWday");
        }
    }

    public static void showStateHousesAll(List<String> houses){
        for(String house : houses){
            System.out.println(house);
            line();
        }
    }

    public static void showStateHouse(String housenif, String house){
        System.out.println(ANSI_YELLOW + "\nCurrent state of house: " + housenif + ANSI_RESET);
        System.out.println(house);
    }

    public static void showStateSuppliersAll(List<String> suppliers){
        for(String house : suppliers){
            System.out.println(house);
            line();
        }
    }

    public static void showStateSupplier(int id, String supplier){
        System.out.println(ANSI_YELLOW + "\nCurrent state of energy supplier: " + id + ANSI_RESET);
        System.out.println(supplier);
    }

    public static String chooseHome(Map<String, String> details, String msg){
        System.out.println(ANSI_YELLOW + msg + ANSI_RESET);
        int i = 1;
        for(Map.Entry<String, String> entry : details.entrySet()){
            System.out.println("\t" + (i++) + ") Owner Name: " + entry.getValue() + ", Owner NIF: " + entry.getKey() );
        }
        return askString("NIF: ");
    }

    public static void toggleRoomDevices(String homenif, String room, int value){
        if(value==1)
            System.out.println("All devices in room: " + room + " in house: " + homenif + " have been turned " + ANSI_GREEN + "ON" + ANSI_RESET);
        if(value==0)
            System.out.println("All devices in room: " + room + " in house: " + homenif + " have been turned " + ANSI_RED + "OFF" + ANSI_RESET);
    }

    public static void toggleDevices(String homenif, int id, int value){
        if(value==1)
            System.out.println("Device: " + id + " in house: " + homenif + " have been turned " + ANSI_GREEN + "ON" + ANSI_RESET);
        if(value==0)
            System.out.println("Device: " + id + " in house: " + homenif + " have been turned " + ANSI_RED + "OFF" + ANSI_RESET);
    }

    public static void updateBaseRate(int id, float baseRate, int value) {
        if(value==1)
            System.out.println("Energy supplier: " + id + " has changed base energy rate to: " + baseRate);
        if(value==2)
            System.out.println("Energy supplier: " + id + " has changed energy tax to: " + baseRate);
    }

    public static void updateClientSUpplier(String homenif, int id) {
        System.out.println("Updated SmartHouse: " + homenif + " to recieve energy from: " + id);
    }

    public static void show_load(String file){
        System.out.println(ANSI_GREEN + "State staved in: " + file + " loaded successfully!" + ANSI_RESET);
    }

    public static void show_save(String file){
        System.out.println(ANSI_GREEN + "State staved in: " + file + " successfully!" + ANSI_RESET);
    }

    public static void leave(){
        String CSI = "\u001B[";
        System.out.print (ANSI_YELLOW);
        System.out.println("/////////////////////////////////////////////////////////////");
        System.out.println("\t\t\t\tSHM - Smart House Management\t\t");
        System.out.println("\n\t\t@author Leonardo de Freitas Marreiros - pg47398\n");
        System.out.println("/////////////////////////////////////////////////////////////");
        System.out.print (ANSI_RESET);
    }



}
