package Controller;

import Exceptions.*;
import utils.JsonEnergySuppliersDataReader;
import utils.JsonSmartHomesDataReader;
import View.View;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;

public class Controller {
    public static void run() {
        SmartManagerFacade smf = new SmartManagerFacade();
        smf.setSmarthomes(JsonSmartHomesDataReader.parserHomes());
        smf.setSuppliers(JsonEnergySuppliersDataReader.parserSuppliers());
        smf.setCurrentDate(LocalDate.now());

        while (true) {
            int opcao = -1;
            while (opcao < 0 || opcao > 12) {
                opcao = View.Menu(smf.getCurrentDate());
            }

            switch (opcao) {
                case 1:
                    int days = View.askInt("Insert number of days to go ahead: ");
                    try {
                        smf.passTime(days);
                    } catch (InvalidHomeException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    int id = View.showSuppliers(smf.supplierList(), "Choose energy supplier:");
                    try {
                        View.showInvoices(smf.listInvoices(id));
                    } catch (InvalidSupplierException e){
                        View.showError(e.getMessage());
                    }
                    break;
                case 3:
                    int day = View.askInt("Insert start day: ");
                    int month = View.askInt("Insert start month: ");
                    int year = View.askInt("Insert start year: ");
                    int day2 = View.askInt("Insert end day: ");
                    int month2 = View.askInt("Insert end month: ");
                    int year2 = View.askInt("Insert end year: ");
                    try {
                        View.showTopHouse(smf.topHousePeriod(LocalDate.of(year, month, day), LocalDate.of(year2, month2, day2)), LocalDate.of(year, month, day), LocalDate.of(year2, month2, day2));
                    } catch (DateTimeException e) {
                        View.showError("Invalid Date");
                    } catch (InexistentInvoices e){
                        //e.printStackTrace();
                        View.showError(e.getMessage());
                    }

                    break;
                case 4:
                    try {
                        View.showTopSupplier(smf.topSupplier());
                    } catch (InexistentInvoices e){
                        //e.printStackTrace();
                        View.showError(e.getMessage());
                    }
                    break;
                case 5:
                    day = View.askInt("Insert start day: ");
                    month = View.askInt("Insert start month: ");
                    year = View.askInt("Insert start year: ");
                    day2 = View.askInt("Insert end day: ");
                    month2 = View.askInt("Insert end month: ");
                    year2 = View.askInt("Insert end year: ");
                    try {
                        View.showTopConsumers(smf.topConsumers(LocalDate.of(year, month, day), LocalDate.of(year2, month2, day2)), LocalDate.of(year, month, day), LocalDate.of(year2, month2, day2));
                    } catch (DateTimeException e) {
                        View.showError("Invalid Date");
                    }
                    break;
                case 6:
                    int type = View.askInt("Current houses state: \n> (1) By NIF;\n> (2) Show all.\n");
                    if(type==2){
                        View.showStateHousesAll(smf.smartHomeCurrentState());
                    }else if(type==1){
                        try{
                            String nif = View.askString("Insert house owner NIF: ");
                            View.showStateHouse(nif,smf.smartHomeStateNIF(nif));
                        } catch (InvalidHomeException e){
                            //e.printStackTrace();
                            View.showError(e.getMessage());
                        }
                    } else{
                        View.showError("Invalid option");
                    }
                    break;
                case 7:
                    type = View.askInt("Current energy suppliers state: \n> (1) By ID;\n> (2) Show all.\n");
                    if(type==2){
                        View.showStateSuppliersAll(smf.supplierCurrentState());
                    }else if(type==1){
                        try{
                            id = View.askInt("Insert energy supplier ID: ");
                            View.showStateSupplier(id,smf.supplierCurrentStateID(id));
                        } catch (InvalidSupplierException e){
                            //e.printStackTrace();
                            View.showError(e.getMessage());
                        }
                    } else{
                        View.showError("Invalid option");
                    }
                    break;
                case 8:
                    String homenif = View.chooseHome(smf.smartHomeDescription(), "\nChoose SmartHouse to toggle device(s): ");
                    try{
                        View.showStateHouse(homenif,smf.smartHomeStateNIF(homenif));
                    } catch (InvalidHomeException e){
                        //e.printStackTrace();
                        View.showError(e.getMessage());
                        break;
                    }
                    type = View.askInt("\n> (1) Toggle all devices in room;\n> (2) Toggle specific device.\n");
                    if(type==1){
                        String room = View.askString("Insert room name: ");
                        int subtype = View.askInt("\n> (1) Turn on all devices;\n> (2) Turn off all devices.\n");
                        if(subtype==1){
                            try {
                                smf.toggleRoomDevices(homenif, room, 1);
                                View.toggleRoomDevices(homenif, room,1);
                            } catch (InvalidRoomException e){
                                View.showError(e.getMessage());
                                break;
                            }
                        } else if(subtype==2){
                            try {
                                smf.toggleRoomDevices(homenif, room, 0);
                                View.toggleRoomDevices(homenif, room,0);
                            } catch (InvalidRoomException e){
                                View.showError(e.getMessage());
                                break;
                            }
                        } else {
                            View.showError("Invalid option");
                        }
                    }else if(type==2){
                        try{
                            id = View.askInt("Insert device ID: ");
                            View.toggleDevices(homenif,id,smf.toggleDevice(homenif,id));
                        } catch (InvalidDeviceException e){
                            //e.printStackTrace();
                            View.showError(e.getMessage());
                        }
                    } else{
                        View.showError("Invalid option");
                    }
                    break;
                case 9:
                    id = View.showSuppliers(smf.supplierList(), "Choose energy supplier:");
                    type = View.askInt("\n> (1) Change supplier base energy rate;\n> (2) Change supplier energy tax.\n");
                    if(type==1){
                        try {
                            float baseRate = View.askInt("Insert new base energy rate: ");
                            smf.changeSupplierBaseRate(id, baseRate);
                            View.updateBaseRate(id, baseRate, 1);
                        } catch (InvalidSupplierException e){
                            //e.printStackTrace();
                            View.showError(e.getMessage());
                        }
                    }else if(type==2){
                        try {
                            float tax = View.askInt("Insert new energy tax: ");
                            smf.changeSupplierTax(id, tax);
                            View.updateBaseRate(id, tax, 2);
                        } catch (InvalidSupplierException e){
                            //e.printStackTrace();
                            View.showError(e.getMessage());
                        }
                    } else{
                        View.showError("Invalid option");
                    }
                    break;
                case 10:
                    try {
                        homenif = View.chooseHome(smf.smartHomeDescription(), "\nChoose SmartHouse change energy supplier: ");
                        id = View.showSuppliers(smf.supplierList(), "\nChoose which energy supplier to change to:");
                        smf.changeSupplier(homenif,id);
                        View.updateClientSUpplier(homenif,id);
                    } catch(InvalidHomeException  | InvalidSupplierException | SameSupplierException e){
                        View.showError(e.getMessage());
                    }
                    break;
                case 11:
                    try {
                        String file = View.askString("Insert save file name: ");
                        smf.save(file, smf);
                        View.show_save(file);
                    } catch (IOException e){
                        View.showError(e.getMessage());
                    }
                    break;
                case 12:
                    try {
                        String file = View.askString("Insert load file name: ");
                        smf = smf.load(file);
                        View.show_load(file);
                    } catch (IOException | ClassNotFoundException e) {
                        View.showError(e.getMessage());
                    }
                    break;
                case 0:
                    View.leave();
                    System.exit(0);
                    break;

            }
        }
    }
}
