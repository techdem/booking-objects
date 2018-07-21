import javax.swing.*;
import java.io.*;

public class ZooBookings {

    public static void main(String[] args) {

        // Declare main variables
        int userOption;
        String[] options = new String[] {"Book 1st show", "Book 2nd show", "See bookings", "Create backup of booking file"};
        String userName = "";
        String firstShow = "";
        String secondShow = "";

        // Validate user name
        while(userName == null || userName.equals("")) {
            userName = JOptionPane.showInputDialog(null, "Please tell us your name: ");

            if (userName == null || userName.equals("")) {
                JOptionPane.showMessageDialog(null, "Not a valid name!");
            }
        }

        // Display menu
        do {
            userOption = JOptionPane.showOptionDialog(null, "Please choose one of the following options: ",
                    "Dublin Zoo Booking App", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);
            System.out.println("User selection: " + userOption);

            // Menu logic
            switch (userOption) {
                case 0:
                    firstShow = JOptionPane.showInputDialog(null, "Type the name of the show: ");

                    // If user has specified two bookings then write to the file
                    if (firstShow != null && !firstShow.equals("") && secondShow != null && !secondShow.equals("")) {
                        System.out.println("Creating booking!");
                        createBooking(new Booking(userName, firstShow, secondShow), false);
                    }
                    break;
                case 1:
                    secondShow = JOptionPane.showInputDialog(null, "Type the name of the show: ");

                    // If user has specified two bookings then write to the file
                    if (secondShow != null &&!secondShow.equals("") && firstShow != null && !firstShow.equals("")) {
                        System.out.println("Creating booking!");
                        createBooking(new Booking(userName, firstShow, secondShow), false);
                    }
                    break;
                case 2:

                    // Read from the bookings file
                    JOptionPane.showMessageDialog(null, readBooking(userName, false));
                    break;
                case 3:

                    // Invoke the readBooking method with a backup switch to generate a backup file
                    readBooking(userName, true);
                    System.out.println("Backup file created!");
                    JOptionPane.showMessageDialog(null, "Backup File Created!");
                    break;
                default:
                    System.out.println("Terminating application");
                    break;
            }
        } while (userOption != -1);

        JOptionPane.showMessageDialog(null, "Thank you for using the Dublin Zoo Booking App !!!");
    }

    public static void createBooking(Booking b, boolean backup) {

        // Create an output stream for original or backup and check for all possible exceptions
        try {
            FileOutputStream outputStream = (backup) ? new FileOutputStream("bookingListBackup.ser", true) : new FileOutputStream("bookingList.ser");
            ObjectOutputStream outputStreamOut = new ObjectOutputStream(outputStream);
            outputStreamOut.writeObject(b);
            outputStreamOut.close();
            outputStream.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found !!!");
        }
        catch (IOException e) {
            System.out.println("IO Exception !!!");
        }
    }

    public static String readBooking(String search, boolean backup) {

        // Create an input stream for the original file and check for all possible exceptions
        String userBookings = "";
        Booking bookingsObject = null;

        try {
            FileInputStream inputStream = new FileInputStream("bookingList.ser");
            ObjectInputStream inputStreamIn = new ObjectInputStream(inputStream);
            bookingsObject = (Booking) inputStreamIn.readObject();

            if(backup) {
                createBooking(bookingsObject, true);
                return "";
            }

            if (bookingsObject.getName().equals(search)) {
                userBookings += "You (" + search + ") have the following bookings:\n\n\t" + "First booking: " + bookingsObject.getFirstShow()
                        + "\n\t" + "Second booking: " + bookingsObject.getSecondShow();
            } else {
                userBookings += "You do not have any bookings...";
            }

            inputStream.close();
            inputStreamIn.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found !!!");
        }
        catch (IOException e) {
            System.out.println("IO Exception !!!");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found !!!");
        }

        return userBookings;
    }
}

// Define a booking object that will store the name and the bookings for a specific user
class Booking implements java.io.Serializable {
    private String name;
    private String firstShow;
    private String secondShow;

    public Booking(String name, String firstShow, String secondShow) {
        this.name = name;
        this.firstShow = firstShow;
        this.secondShow = secondShow;
    }

    public String getName() {
        return name;
    }

    public String getFirstShow() {
        return firstShow;
    }

    public String getSecondShow() {
        return secondShow;
    }
}
