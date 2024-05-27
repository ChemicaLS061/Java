import javax.swing.plaf.ScrollPaneUI;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        Scanner s = new Scanner(System.in);
        String arithOper = s.nextLine();
        String answer = calc(arithOper);
        System.out.println(answer);
    }

    public static String calc (String inOper) throws IOException {
        int cal = 0;
        boolean flagRim = false;
        String outOper = "";
        inOper = inOper.replaceAll(" ", "");
        inOper = inOper.replaceAll("\t", "");
        if(inOper.isEmpty()){
            throw new IOException();
        }
        inOper = inOper.toUpperCase();
        int aCode = inOper.codePointAt(0);
        if(aCode == 73 || aCode == 86 || aCode == 88){
            outOper = rim(inOper);
        } else if(aCode >= 48 && aCode <= 57) outOper = String.valueOf(arab(inOper));
        else throw new IllegalArgumentException("Введите числа в диапозоне 1-10");
        return outOper;
    }

    public static int arab (String inOper) throws IOException {
        int a = 0;
        int b = 0;
        int sum = 0;
        boolean flag0 = false;
        boolean flag1 = false;
        boolean flag2 = false;
        char sign = '0';
        char number;
        for (int i = 0; i < inOper.length(); i++) {
            int aCode = inOper.codePointAt(i);
            if (!flag1 && !flag0 && !flag2) {
                if (aCode == 49) {
                    flag1 = true;
                    if(sign == '0') a = 1;
                    else b = 1;
                } else if (aCode > 49 && aCode <= 57) {
                    number = (char) aCode;
                    flag2 = true;
                    if(sign == '0') a = Integer.parseInt(String.valueOf(number));
                    else b = Integer.parseInt(String.valueOf(number));
                } else {
                    throw new IllegalArgumentException("Введите числа в диапозоне 1-10");
                }
            } else if (aCode == 48 && !flag0 && !flag2) {
                flag1 = false;
                flag0 = true;
                if(sign == '0') a = 10;
                else b = 10;
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
                    default: throw new IOException();
                }
                flag0 = false;
                flag1 = false;
                flag2 = false;
            } else throw new IOException();
        }
        switch (sign) {
            case '+': sum = a+b; break;
            case  '*': sum = a*b; break;
            case '-': sum = a-b; break;
            case '/': sum = a/b; break;
        }
        return sum;
    }

    public static String rim (String inOper) throws IOException {
        int a = 0;
        int b = 0;
        int sum = 0;
        byte flagI = 0;
        byte flagV = 0;
        byte flagX = 0;
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
                    if (flagI < 4 && flagV < 2 && flagX < 2) {
                        if (aCode == 73 && flagI != 3) {
                            flagI += 1;}
                    if (aCode == 86 && flagI <= 1) flagV += 1;
                    if (aCode == 88 && flagI <= 1) flagX += 1;
                    } else throw new IOException();    //Изменить на ошибку
                }
                if(a!=0) {
                    b += convertRimArab(element);
                } else {
                    a += convertRimArab(element);
                }
                flagI = 0;
                flagV = 0;
                flagX = 0;
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
        boolean flagSum = false;
        if(inOper.length() > 1) {
            for (int i = inOper.length() - 1; i > 0; i--) {
                if (inOper.charAt(i - 1) < inOper.charAt(i)) {
                    Converting conv = Converting.valueOf(String.valueOf(inOper.charAt(i)));
                    answer += conv.getValue();
                    conv = Converting.valueOf(String.valueOf(inOper.charAt(i - 1)));
                    outOper += answer - conv.getValue();
                }
                if ((byte) inOper.charAt(i - 1) >= (byte) inOper.charAt(i)) {
                    if(!flagSum) {
                        Converting conv = Converting.valueOf(String.valueOf(inOper.charAt(i)));
                        outOper += conv.getValue();
                    }
                    Converting conv = Converting.valueOf(String.valueOf(inOper.charAt(i - 1)));
                    outOper += conv.getValue();
                    flagSum = true;
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
            throw new IllegalArgumentException(number + " число находится за пределами диапазона (0,100]");
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