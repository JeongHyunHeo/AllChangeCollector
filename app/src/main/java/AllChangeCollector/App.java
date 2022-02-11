/*
    Format of cli <command> <filename> <cli>
 */
package AllChangeCollector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {
    static ArrayList<String> repo_url = new ArrayList<String>();
    static ArrayList<String> repo_name = new ArrayList<String>();
    static boolean output, help;
    static String input_path = null;

    public static void main(String[] args) throws IOException, ParseException {
        //cli options
        Options options = createOptions();

        if(parseOptions(options,args)){
            if(help)
                print_help();
            
        }
        String filename = args[0];

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
        System.out.println("-i, -input : giving the input file custompath, default path is current working directory");
        System.out.println("-o, -output : makes one txt file that contains all the changes in cloned repositories");
    }

    private static Options createOptions(){
        Options options = new Options();

        options.addOption(Option.builder("i").longOpt("input").desc("give input file custom path").hasArg().argName("input_path").required().build());
        options.addOption(Option.builder("o").longOpt("output").desc("makes the output file containing all the changes of all cloned repository").build());
        options.addOption(Option.builder("h").longOpt("help").desc("Help").build());

        return options;
    }

    private static boolean parseOptions(Options options, String[] args){
        CommandLineParser parser = new DefaultParser();

        try{
            CommandLine cmd = parser.parse(options, args);
            try{
                if(cmd.hasOption("o"))
                    output = true;
                if(cmd.hasOption("h"))
                    help = true;

                input_path = cmd.getOptionValue("i");
            } catch (Exception e){
                System.out.println(e.getMessage());
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
