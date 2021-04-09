package hw24di.services;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class IOServiceConsole implements IOService {
    private final PrintStream out;
    private final Scanner in;

    public IOServiceConsole(PrintStream out, InputStream in) {
        this.out = out;
        this.in = new Scanner(in);
    }


    @Override
    public void out(String message) {
        out.println(message);
    }

    @Override
    public String readLn(String prompt) {
        out(prompt);
        return in.next();
    }

    @Override
    public int readInt(String prompt) {
        out(prompt);
        int n = -1;
        do{
           if( in.hasNextInt() ) { n = in.nextInt(); }
           else {
               in.next();
               out("Введите число, пожалуйста: ");
           }
        }
        while ( n <= 0 );
        return n;
    }



}
