
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String mode = "enc";
        int key = 0;

        boolean isData = false;
        String data = " ";

        boolean isIn = false;
        String inPath = " ";

        String outPath = " ";

        String alg = "shift";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-mode")) {
                mode = args[i + 1];
            }
            if (args[i].equals("-key")) {
                key = Integer.parseInt(args[i + 1]);
            }
            if (args[i].equals("-data")) {
                isData = true;
                data = args[i + 1];
            }
            if (args[i].equals("-in")) {
                isIn = true;
                inPath = args[i + 1];
            }
            if (args[i].equals("-out")) {
                outPath = args[i + 1];
            }
            if (args[i].equals("-alg")) {
                alg = args[i + 1];
            }
        }

        if (mode.equals("dec")) {
            key = -key;
        }

        Context context = null;

        if (isData) {
            context = new Context(new DataCypher());
            context.toEncrypt(mode, inPath, outPath, data, alg, key);

        } else if (isIn) {
            context = new Context(new FileCypher());
            context.toEncrypt(mode, inPath, outPath, data, alg, key);

        } else {
            context = new Context(new SpaceCypher());
            context.toEncrypt(mode, inPath, outPath, data, alg, key);
        }
    }



}

interface Encrypt {
    void cypher(String mode, String inPath, String outPath, String data, String alg, int key);
}

class Context {

    Encrypt encrypt;

    public Context(Encrypt encrypt) {
        this.encrypt = encrypt;
    }

    public void toEncrypt(String mode, String inPath, String outPath, String data, String alg, int key) {
        this.encrypt.cypher(mode, inPath, outPath, data, alg, key);
    }
}

class DataCypher implements Encrypt {

    StringBuilder answerString = new StringBuilder();
    int decryptedChar;
    Swifter swifter = new Swifter();

    @Override
    public void cypher(String mode, String inPath, String outPath, String data, String alg, int key) {
        char[] dataToChars = data.toCharArray();

        if (alg.equals("shift")) {
            swifter.swift(dataToChars, answerString, key);
        } else {
            for (char dataToChar : dataToChars) {
                decryptedChar = dataToChar + key;
                answerString.append((char) decryptedChar);
            }
        }

        if (!outPath.equals(" ")) {
            try (PrintWriter printWriter = new PrintWriter(outPath)) {
                printWriter.println(answerString);
            } catch (FileNotFoundException e) {
                System.out.println("Error");
            }
        } else {
            System.out.println(answerString);
        }
    }
}

class FileCypher implements Encrypt {

    StringBuilder answerString = new StringBuilder();
    int decryptedChar;
    Swifter swifter = new Swifter();

    @Override
    public void cypher(String mode, String inPath, String outPath, String data, String alg, int key) {
        File file = new File(inPath);

        String phrase = " ";
        try (Scanner scanner = new Scanner(file)) {
            phrase = scanner.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("Error");
        }

        char[] fileChars = phrase.toCharArray();

        if (alg.equals("shift")) {
            swifter.swift(fileChars, answerString, key);
        } else {
            for (char fileChar : fileChars) {
                decryptedChar = fileChar + key;
                answerString.append((char) decryptedChar);
            }
        }

        if (!outPath.equals(" ")) {
            try (PrintWriter printWriter = new PrintWriter(outPath)) {
                printWriter.println(answerString);
            } catch (FileNotFoundException e) {
                System.out.println("Error");
            }
        } else {
            System.out.println(answerString);
        }
    }
}

class SpaceCypher implements Encrypt {

    int decryptedChar;

    @Override
    public void cypher(String mode, String inPath, String outPath, String data, String alg, int key) {
        decryptedChar = ' ' + key;

        if (!outPath.equals(" ")) {
            try (PrintWriter printWriter = new PrintWriter(outPath)) {
                printWriter.println((char) decryptedChar);
            } catch (FileNotFoundException e) {
                System.out.println("Error");
            }
        } else {
            System.out.println((char) decryptedChar);
        }
    }
}

class Swifter {

    void swift (char[] dataToChars, StringBuilder answerString, int key) {
        for (char dataToChar : dataToChars) {
            if (dataToChar >= 65 && dataToChar <= 90) {
                if (dataToChar + key > 90) {
                    answerString.append((char) (dataToChar + key - 26));
                } else if (dataToChar + key < 65) {
                    answerString.append((char) (dataToChar + key + 26));
                } else {
                    answerString.append((char) (dataToChar + key));
                }
            } else if (dataToChar >= 97 && dataToChar <= 122) {
                if (dataToChar + key > 122) {
                    answerString.append((char) (dataToChar + key - 26));
                } else if (dataToChar + key < 97) {
                    answerString.append((char) (dataToChar + key + 26));
                } else {
                    answerString.append((char) (dataToChar + key));
                }
            } else {
                answerString.append(dataToChar);
            }

        }
    }
}
