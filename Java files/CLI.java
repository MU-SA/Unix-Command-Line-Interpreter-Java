import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CLI {

    public static void main(String[] args) throws IOException, InterruptedException {
        StringBuilder terminal = new StringBuilder();
        StringBuilder directory = new StringBuilder();
        StringBuilder file = new StringBuilder();
        StringBuilder destination = new StringBuilder();
        StringBuilder source = new StringBuilder();
        String write = "reg";
        terminal.append("MUSA>> ");
        System.out.print(terminal.toString());

        String command = new Scanner(System.in).nextLine().toLowerCase();
        while (!command.equals("exit")) {
            if (command.equals("")) {
                System.out.println("Enter the command");
            } else if (command.equals("clear")) {
                terminal.delete(0, terminal.length());
                terminal.append("MUSA>> ");
                for (int i = 0; i < 40; i++) {
                    System.out.println();
                }
            } else if (command.contains("cd")) {
                if (command.contains("args")) {
                    System.out.println("Takes the file directory as a parameter");
                } else {
                    if (command.length() < 4) {// cd bs
                        if (command.contains(">") && command.indexOf('>') == command.length() - 1) { //cd bs and >
                            write = "over";
                            setToCurrentDirectory(write);

                        }

                        setToCurrentDirectory(write);
                        if (setToCurrentDirectory(write)) {
                            System.out.println("File directory changed successfully to:");
                            System.out.println(pwd());
                        } else {
                            System.out.println("Error Occurred");
                        }
                    } else {
                        for (int i = 3; i < command.length(); i++) {
                            directory.append(command.charAt(i));
                        }

                        cd(directory.toString());
                        directory.delete(0, directory.length());
                        if (cd(directory.toString())) {
                            System.out.println("File directory changed successfully to:");
                            System.out.println(pwd());
                        } else {
                            System.out.println("Error Occurred");
                        }

                    }
                }
            } else if (command.equals("pwd")) {
                if (command.contains("args")) {
                    System.out.println("pwd takes no parameter");
                } else {
                    pwd();
                    System.out.println(pwd());
                }

            } else if (command.contains("ls")) {
                if (command.contains("args")) {
                    System.out.println("ls takes no parameter" + "\n- add > or >> to write on a external file" + "\n- " +
                            "> to overwrite on the file" + "\n" + "- >> to append on the file");
                } else {
                    if (command.contains(">") && command.indexOf('>') == command.length() - 1) {
                        write = "over";
                        ls(write);
                        write = "reg";
                    } else if ((command.contains(">") && command.charAt(command.indexOf('>')) == '>')) {
                        write = "app";
                        ls(write);
                        write = "reg";
                    } else {
                        ls(write);
                    }
                }


            } else if (command.contains("cat")) {
                if (command.contains("args")) {
                    System.out.println("cat (Concatenate) takes a file directory as a parameter");
                } else if (command.contains("?")) {
                    System.out.println("[cat] takes a directory as a parameter");
                    System.out.println("[cat] return the content of any file");
                } else {
                    if (command.length() < 4) {
                        System.out.println("retype cat then space then add directory");
                    } else {
                        for (int i = 4; i < command.length(); i++) {
                            file.append(command.charAt(i));
                        }
                        cat(file.toString());
                        file.delete(0, file.length());
                    }
                }

            } else if (command.contains("cp")) {
                if (command.contains("args")) {
                    System.out.println("cp (copy) takes two parameter the source file and the destination directory");
                } else if (command.contains("?")) {
                    System.out.println("[cp] takes two directories as a parameter the first is the source the second is the destination");
                } else {
                    for (int i = 3; i < command.length(); i++) {
                        if (!String.valueOf(command.charAt(i)).equals(" ")) {
                            source.append(command.charAt(i));
                        } else {
                            break;
                        }
                    }
                    for (int i = 4 + source.length(); i < command.length(); i++) {
                        if (!String.valueOf(command.charAt(i)).equals(" ")) {
                            destination.append(command.charAt(i));
                        } else {
                            break;
                        }
                    }
                    cp(source.toString(), destination.toString());
                    source.delete(0, source.length());
                    destination.delete(0, destination.length());
                }

            } else if (command.contains("mv")) {
                if (command.contains("args")) {
                    System.out.println("mv (move) takes two parameter the source file and the destination directory");
                } else if (command.contains("?")) {
                    System.out.println("[mv] takes two arguments as a parameter source and the destination");
                } else {
                    for (int i = 3; i < command.length(); i++) {
                        if (!String.valueOf(command.charAt(i)).equals(" ")) {
                            source.append(command.charAt(i));
                        } else {
                            break;
                        }
                    }
                    for (int i = 4 + source.length(); i < command.length(); i++) {
                        if (!String.valueOf(command.charAt(i)).equals(" ")) {
                            destination.append(command.charAt(i));
                        } else {
                            break;
                        }
                    }
                    mv(source.toString(), destination.toString());
                    source.delete(0, source.length());
                    destination.delete(0, destination.length());
                }

            } else if (command.contains("rm") && command.length() < 5) {
                if (command.contains("args")) {
                    System.out.println("rm (remove) takes the file needed to be deleted directory as a parameter");
                } else if (command.contains("?")) {
                    System.out.println("[rm] takes one directory as a parameter");
                } else {
                    for (int i = 3; i < command.length(); i++) {
                        if (!String.valueOf(command.charAt(i)).equals(" ")) {
                            source.append(command.charAt(i));
                        } else {
                            break;
                        }
                    }
                    rm(source.toString());
                    source.delete(0, source.length());
                }

            } else if (command.contains("more")) {
                if (command.contains("args")) {
                    System.out.println("more takes file directory as a parameter");
                } else {
                    StringBuilder filename = new StringBuilder();
                    for (int i = 5; i < command.length(); i++) {
                        filename.append(command.charAt(i));
                    }
                    more(filename.toString());
                }

            } else if (command.equals("help")) {
                System.out.println("[cat]  reads files sequentially, writing them to standard output.");
                System.out.println("[cp] Copy SOURCE to DEST, or multiple SOURCE(s) to DIRECTORY");
                System.out.println("[mv] moves one or more files or directories from one place to another");
                System.out.println("[rm] command used to remove objects such as files, directories");
                System.out.println("[mkdir] is used to make a new directory");
                System.out.println("[rmdir] is used to remove a new directory");
                System.out.println("[more] more is a command to view the contents of a text file one screen at a time");
                System.out.println("[date] used to display the current date");
                System.out.println("[pwd] used to display the current directory");

            } else if (command.contains("mkdir")) {
                if (command.contains("args")) {
                    System.out.println("mkdir takes a directory as a parameter");
                } else if (command.contains("?")) {
                    System.out.println("[mkdir] is used to make a new directory");
                } else {
                    for (int i = 6; i < command.length(); i++) {
                        if (!String.valueOf(command.charAt(i)).equals(" ")) {
                            source.append(command.charAt(i));
                        } else {
                            break;
                        }
                    }
                    mkdir(source.toString());
                    source.delete(0, source.length());
                }


            } else if (command.contains("rmdir") && command.length() > 2) {
                if (command.contains("args")) {
                    System.out.println("rmdir takes a directory as a parameter");
                } else if (command.contains("?")) {
                    System.out.println("[rmdir] is used to remove a new directory");
                } else {
                    for (int i = 6; i < command.length(); i++) {
                        source.append(command.charAt(i));
                    }
                    rmdir(source.toString());
                    source.delete(0, source.length());
                }

            } else if (command.contains("date")) {
                if (command.contains("args")) {
                    System.out.println("date takes no parameter");
                } else {
                    currentdate();
                }

            } else {
                System.out.println("No such Command");
                terminal.delete(0, terminal.length());
                terminal.append("MUSA>> ");
            }
            System.out.print(terminal);
            command = new Scanner(System.in).nextLine().toLowerCase();
        }

    }

    private static void currentdate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
    }

    private static void rmdir(String s) {
        File file = new File(s);
        if (s.length() == 0) {
            System.out.println("rmdir takes a directory as a parameter");
        } else {
            if (file.exists()) {
                file.delete();
                if (!file.exists()) {
                    System.out.println("File deleted successfully");
                }
            } else {
                System.out.println("File not exist");
            }
        }

    }

    private static void mkdir(String source) {
        if (source.length() == 0) {
            System.out.println("mkdir takes a directory as a parameter");
        } else {
            File newDirectory = new File(source);
            if (!newDirectory.exists()) {
                System.out.println("creating new directory: " + newDirectory.getName());
                boolean result = false;
                try {
                    newDirectory.mkdir();
                    result = true;
                } catch (SecurityException se) {
                    System.out.println("Not secured!");
                }
                if (result) {
                    System.out.println("Directory created");
                }
            } else {
                System.out.println("The directory already exists");
            }
        }

    }

    private static void cp(String src, String dest) throws IOException {
        File source = new File(src);
        File destination = new File(dest);
        if (source.exists()) {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied");
        } else if (src.length() == 0 || dest.length() == 0) {
            System.out.println("cp takes two parameters  1: File location \n\t\t\t\t\t\t 2: Destination path ");
        } else {
            System.out.println("File not Exist");
        }

    }

    private static void mv(String src, String dest) throws IOException {
        File source = new File(src);
        File destination = new File(dest);
        if (source.exists()) {
            Files.move(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied");
        } else if (src.length() == 0 || dest.length() == 0) {
            System.out.println("mv takes two parameters  1: File location \n\t\t\t\t\t\t 2: Destination path ");
        } else {
            System.out.println("File not Exist");
        }

    }

    private static void rm(String src) throws IOException {
        File Fsrc = new File(src);
        if (Fsrc.exists())
            Files.delete(Fsrc.toPath());
        else if (src.length() == 0) {
            System.out.println("rm takes one parameter the file directory");
        } else
            System.out.println("File not Exist");
    }

    private static void cat(String file) {
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader in = new BufferedReader(fileReader);
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            fileReader.close(); // sa3at bydrb lma ast5dm cat lazm 22flo 34an ast5do f el mv w el cp
        } catch (FileNotFoundException ex) {
            System.out.println(file + ", file not found.");
        } catch (IOException ex) {
            System.out.println(file + ", input/output error.");
        }
    }

    private static void ls(String write) throws FileNotFoundException, UnsupportedEncodingException {
        if (write.equals("over")) {
            PrintWriter writer = new PrintWriter("lsOver.txt", "UTF-8");
            File dir = new File(System.getProperty("user.dir"));
            String childs[] = dir.list();
            assert childs != null;
            for (String child : childs) {
                writer.println(child);
                writer.append(child);
            }
            writer.close();
        } else if (write.equals("reg")) {
            File dir = new File(System.getProperty("user.dir"));
            String childs[] = dir.list();
            assert childs != null;
            for (String child : childs) {
                System.out.println(child);
            }

        } else if (write.equals("app")) {
            try (FileWriter fw = new FileWriter("lsAppended.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                File dir = new File(System.getProperty("user.dir"));
                String childs[] = dir.list();
                assert childs != null;
                for (String child : childs) {
                    out.println(child);
                }

            } catch (IOException e) {
                System.out.println("Error Occurred");
            }

        }


    }

    private static String pwd() {
        String pwd = System.getProperty("user.dir");
        return pwd;
    }

    private static boolean cd(String directory_name) {
        boolean result = false;  // Boolean indicating whether directory was set
        File directory;       // Desired current working directory
        directory = new File(directory_name).getAbsoluteFile();
        if (directory.exists() || directory.mkdirs()) {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }
        return result;
    }

    private static boolean setToCurrentDirectory(String write) throws IOException {
        boolean result = false;  // Boolean indicating whether directory was set
        File directory;
        String current = new java.io.File(".").getCanonicalPath();
        // Desired current working directory
        directory = new File(current).getAbsoluteFile();
        if (directory.exists() || directory.mkdirs()) {
            result = (System.setProperty("user.dir", directory.getAbsolutePath()) != null);
        }
        return result;
    }

    private static void more(String filename) {
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            String sCurrentLine;
            for (int i = 0; i < 10; i++) {
                sCurrentLine = br.readLine();
                if (sCurrentLine != null) {
                    System.out.print(sCurrentLine);
                }
                while ((sCurrentLine = br.readLine()) != null) {
                    String s = new Scanner(System.in).nextLine();
                    if (s.length() == 0) {
                        System.out.print(sCurrentLine);
                    }


                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File Not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

