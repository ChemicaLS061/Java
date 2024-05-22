import javax.swing.plaf.ScrollPaneUI;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String arithOper = s.nextLine();
        String answer = calc(arithOper);
        System.out.println(answer);
    }

    public static String calc (String inOper) {
        int cal = 0;
        boolean flagRim = false;
        String outOper = "";
        inOper = inOper.trim();
        if(inOper.isEmpty()){}
        inOper = inOper.toUpperCase();
        int aCode = inOper.codePointAt(0);
        if(aCode == 73 || aCode == 86 || aCode == 88){
            outOper = rim(inOper);
        }
        if(aCode >= 48 && aCode <= 57) arab(inOper);
//        try {
//            cal = Integer.parseInt(outOper);
//        } catch (NumberFormatException nfe){
//            System.out.println("NumberFormatException: " + nfe.getMessage());
//        }

        return outOper;
    }

    public static void arab (String inOper) {
        boolean flag0 = false;
        boolean flag1 = false;
        boolean flag2 = false;
        char sign = '0';
        for (int i = 0; inOper.codePointAt(i) != 0; i++) {
            int aCode = inOper.codePointAt(i);
            if (!flag1 && !flag0 && !flag2) {
                if (aCode == 49) flag1 = true;
                if (aCode > 49 && aCode <= 57) flag2 = true;
            } else if (aCode == 48 && !flag0 && !flag2) {
                flag1 = false;
                flag0 = true;
            } else if(sign == '0'){
                switch (aCode) {
                    case 42:
                        sign = '*';
                        break;
                    case 43:
                        sign = '+';
                        break;
                    case 45:
                        sign = '-';
                        break;
                    case 47:
                        sign = '/';
                        break;
                    default: System.out.println("Некорректный ввод");       //Изменить на ошибку
                }
                flag0 = false;
                flag1 = false;
                flag2 = false;
            } else System.out.println("Некорректный ввод");     //Изменить на ошибку
        }
    }

    public static String rim (String inOper) {
        int a = 0;
        int b = 0;
        int sum = 0;
        byte flagI = 0;
        boolean flagV = false;
        boolean flagX = false;
        char sign = '0';
        int i = 0;
        String outOper = "";
        String str = "\\";
        for (i = 0; i < inOper.length(); i++) {
            int aCode = inOper.codePointAt(i);
            switch (aCode) {
                case 42:
                    sign = '*';
                    break;
                case 43:
                    sign = '+';
                    break;
                case 45:
                    sign = '-';
                    break;
                case 47:
                    sign = '/';
                    break;
            }
        }
            str = str.concat(String.valueOf(sign));
            String [] strings = inOper.split(str);
            for (String element: strings) {
                for (i = 0; i < element.length(); i++) {
                    int aCode = element.codePointAt(i);
                    if (flagI < 4 && !flagV && !flagX) {
                        if (aCode == 73 && flagI != 3) {
                            flagI += 1;}
                    if (aCode == 86 && flagI <= 1) flagV = true;
                    if (aCode == 88 && flagI <= 1) flagX = true;
                    } else System.out.println("Некорректный ввод3");     //Изменить на ошибку
                }
                if(a!=0) {
                    b += convertRimArab(element);
                } else {
                    a += convertRimArab(element);
                }
                flagI = 0;
                flagV = false;
                flagX = false;
            }
            switch (sign) {
                case '+': sum = a+b; break;
                case  '*': sum = a*b; break;
                case '-': sum = a-b; break;
                case '/': sum = a/b; break;
            }
            outOper = convertArabRim(sum);
            return outOper;
        }

    public static int convertRimArab(String inOper){
        int outOper = 0;
        int answer = 0;
        if(inOper.length() > 1) {
            for (int i = inOper.length() - 1; i > 0; i--) {
                if ((byte) inOper.charAt(i - 1) >= (byte) inOper.charAt(i)) {
                    Converting conv = Converting.valueOf(String.valueOf(inOper.charAt(i - 1)));
                    outOper += conv.getValue();
                    conv = Converting.valueOf(String.valueOf(inOper.charAt(i)));
                    outOper += conv.getValue();
                }
                if (inOper.charAt(i - 1) < inOper.charAt(i)) {
                    Converting conv = Converting.valueOf(String.valueOf(inOper.charAt(i)));
                    answer += conv.getValue();
                    conv = Converting.valueOf(String.valueOf(inOper.charAt(i - 1)));
                    outOper += answer - conv.getValue();
                }
            }
        } else {
            Converting conv = Converting.valueOf(String.valueOf(inOper.charAt(0)));
            outOper += conv.getValue();
        }

        return outOper;
    }

    public static String convertArabRim(int number) {
        if ((number <= 0) || (number > 100)) {
            throw new IllegalArgumentException(number + " is not in range (0,100]");
        }
        String outOper = "";
        Converting s = Converting.C;
        int i = s.ordinal();
        while ((number > 0) && (i >= 0 )) {
            Converting currentSymbol = Converting.values()[i];
            if (currentSymbol.getValue() <= number) {
                outOper = outOper.concat(String.valueOf(currentSymbol));
                number -= currentSymbol.getValue();
            } else {
                i--;
            }
        }
        return outOper;
    }
}