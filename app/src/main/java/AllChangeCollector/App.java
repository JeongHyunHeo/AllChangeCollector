/*
    Format of cli <command> <filename> <cli>
 */
package AllChangeCollector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {
    static ArrayList<String> repo_url = new ArrayList<String>();
    static ArrayList<String> repo_name = new ArrayList<String>();
    static boolean debug = false, append = false, autoFlush = true;

    public static void main(String[] args) throws IOException, ParseException {
        //cli options
        Options options = new Options();

        options.addOption("h", false, "help");
        options.addOption("d", false, "directory");
        options.addOption("p", false, "customizing input file path");

        //parsing stage
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        //cli interrogation stage
        if (cmd.hasOption("h") || cmd.hasOption("help"))
        {
            print_help();
            System.exit(0);
        }
        if (cmd.hasOption("d") || cmd.hasOption("debug"))
        {
            debug = true;
        }


        if (debug == true)
        {
            PrintStream debug_file = new PrintStream(new FileOutputStream("log.txt", append), autoFlush);
            System.setOut(debug_file);
        }

        String filename = args[1];

        try {
            extract(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        // Cloning 
        clone_repo(repo_url);

        crawl_commit_id(repo_name);

        getChange();
    }
    
    public static void extract(String file_name) throws FileNotFoundException, IOException
    {
        System.out.println("======= Extracting File Data =======");
        File file = new File(file_name);
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                count++;
                String[] result = line.split(" ");
                repo_url.add(result[0]);
                repo_name.add(result[1]);
            }
        }

        System.out.println("Counted repo: " + count + "\n\n");
    }

    public static void clone_repo(ArrayList<String> repo_url) throws IOException
    {
        System.out.println("======    Starting Task : Cloning Repository    ========");
        ProcessBuilder processBuilder = new ProcessBuilder();
        
        File directory = new File(System.getProperty("user.dir") + "/data");
        
        if (!directory.exists())
        {
            directory.mkdir();
        }
        
        
        processBuilder.directory(directory);
        
        
        for (String curr_url : repo_url)
        {
            processBuilder.command("git", "clone", curr_url);
            try {
                System.out.println("Start cloning " + curr_url + ".............");
                Process process = processBuilder.start();
                //process = Runtime.getRuntime().exec("git clone " + curr_url);
                printResult(process);
            } catch (IOException e) {
                System.out.println("Failed Cloning");
                e.printStackTrace();
            }
        }
        
        System.out.println("Cloning Completed\n\n");
    }

    public static void crawl_commit_id(ArrayList<String> repo_name)
    {
        System.out.println("===== Starting Task : Commit ID Collecting ======");

        ProcessBuilder processbuilder = new ProcessBuilder();
        processbuilder.command("git", "log", "--pretty=format:\"%H\"");

        // get history commit ids using 'git log'
        System.out.println("git log executing");
        for (String curr_repo : repo_name) {
            String work_dir = System.getProperty("user.dir") + "/data/" + curr_repo;
            processbuilder.directory(new File(work_dir));
            try {
                Process process = processbuilder.start();
                saveResult(process, work_dir);
                System.out.println("commit ID for " + curr_repo + " have been extracted");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All commit ID extracted\n\n");
    }
    

    //Function that extracting all changes of the repo
    public static void getChange()
    {
        
    }

    // Question: it cannot print the execution of the git cloning process, why?
    public static void printResult(Process process) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }

    // saves all the commitID in the filename 'commitID.txt'
    public static void saveResult(Process process, String working_dir) throws IOException {
        File file = new File(working_dir, "commitID.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        String line = "";
        while ((line = reader.readLine()) != null) {
            line = line.substring(1, line.length() - 1);
            writer.write(line + "\n");
        }
        writer.close();
        reader.close();
    }
    
    public static void print_help() {
        System.out.println("====== HELP ======\n");
        System.out.println("List of Commands");
        System.out.println("------------------");
        System.out.println("-h, -help : listing all the commands with the description of functionality of commands");
        System.out.println(
                "-d, -debug : saves all the progresses into text file, textfile will be saved as 'log.txt' as default");
        System.out.println("-p, -path : ");
    }
}
