package com.policestrategies.calm_stop;

import android.util.Log;
import android.util.Patterns;

/**
 * Contains verification methods for all fields (officer & citizen) used in the signup process.
 * @author Talal Abou Haiba
 */
public class SignupVerification {

    public static boolean validEmail(String email) {
        return (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean validPassword(String password) {
        return (!(password.length() < 6) && password.matches("((.*\\d.*)(.*[A-Za-z].*))|((.*[A-Za-z].*)(.*\\d.*))"));
    }

    public static boolean validLicense(String license) {
        return (license.matches("^[A-Z]([0-9]{8})"));
    }

    public static boolean validAddress(String address) {
        return (!address.isEmpty());
    }

    public static boolean validZip(String zip) { return (zip.matches("^\\d{5}")); }

    public static boolean validPhone(String phone) {
        return (phone.matches("^(1|(1(-|\\s)))?(\\d{10}|" + "\\(?\\d{3}\\)?(-|\\s)?\\d{3}(-|\\s)?\\d{4})\\s?"));
    }


    public static boolean validFirstName(String firstname) {
        return (!firstname.isEmpty());
    }

    public static boolean validLastName(String lastname) {
        return (!lastname.isEmpty());
    }

    public static boolean validDepartment(String department) { return (!department.isEmpty()); }

    public static boolean validBadge(String badge) { return (!badge.isEmpty()); }

    public static boolean validDateOfBirth(String dateofbirth) {
        if (dateofbirth.matches("^\\d{1,2}-\\d{1,2}-\\d{4}")) {
            int i, j, k;
            String S_day = "", S_month = "", S_year = "";
            int I_day, I_month, I_year;
            String dummy = dateofbirth.substring(0, 1);
            for (i = 0, j = 1; !dummy.equalsIgnoreCase("-"); ) {
                S_month = dateofbirth.substring(0, j);
                i++;
                j++;
                dummy = dateofbirth.substring(i, j);
            }
            //i,j = (1,2)|(2,3)
            k = j;
            i = j + 1;
            dummy = dateofbirth.substring(j, i);
            while (!dummy.equalsIgnoreCase("-")) {
                S_day = dateofbirth.substring(k, i);
                i++;
                j++;
                dummy = dateofbirth.substring(j, i);
            }
            S_year = dateofbirth.substring(i, dateofbirth.length());

            I_day = Integer.parseInt(S_day);
            I_month = Integer.parseInt(S_month);
            I_year = Integer.parseInt(S_year);
            return validDateOfBirth(I_month, I_day, I_year);
        } else {
            return false;
        }
    }

    public static boolean validDateOfBirth(int I_month, int I_day, int I_year) {
        if ((I_day > 31) || (I_day < 1) || (I_month > 12) || (I_year < 1910) || (I_year > 2001)) {
            return true;
        } else {
            //FEBRUARY
            if (I_month == 2) {
                //LEAPYEAR FEBRUARY
                if ((I_year % 4) == 0) {
                    if (I_day < 30) {
                        return true;
                    }
                } else {
                    //NOT LEAPYEAR FEBRUARY
                    if (I_day < 29) {
                        return true;
                    }
                }
                return false;
            }
            //MONTHS with 31 days
            if ((I_month == 1 || I_month == 3 || I_month == 5 || I_month == 7 || I_month == 8 || I_month == 10 || I_month == 12)
                    && (I_day < 32)) {
                return true;
                //MONTHS WITH 30 DAYS
            } else if ((I_month == 4 || I_month == 6 || I_month == 9 || I_month == 11) && (I_day < 31)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean validDateOfBirth(String S_month, String S_day, String S_year) {
        int I_day = Integer.parseInt(S_day);
        int I_month = Integer.parseInt(S_month);
        int I_year = Integer.parseInt(S_year);
        if ((I_day > 31) || (I_month > 12) || (I_year < 1930) || (I_year > 2001)) {
            return false;
        } else {
            //FEBRUARY
            if (I_month == 2) {
                //LEAPYEAR FEBRUARY
                if ((I_year % 4) == 0) {
                    if (I_day < 30 && I_day > 0) {
                        return true;
                    }
                } else {
                    //NOT LEAPYEAR FEBRUARY
                    if (I_day < 29 && I_day > 0) {
                        return true;
                    }
                }
                return false;
            }
            //MONTHS with 31 days
            if ((I_month == 1 || I_month == 3 || I_month == 5 || I_month == 7 || I_month == 8 || I_month == 10 || I_month == 12)
                    && (I_day < 32 || I_day > 0)) {
                return true;
                //MONTHS WITH 30 DAYS
            } else if ((I_month == 4 || I_month == 6 || I_month == 9 || I_month == 11) && (I_day < 31 || I_day > 0)) {
                return true;
            } else {
                return false;
            }
        }
    }

} // end class SignupVerification