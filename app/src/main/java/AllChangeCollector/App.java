/*
    Format of cli <command> <filename> <cli>
 */
package AllChangeCollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.eclipse.jgit.api.errors.GitAPIException;

public class App {
    static ArrayList<String> repo_url = new ArrayList<String>(); // clone url for the repository
    static ArrayList<String> repo_name = new ArrayList<String>(); // name of the repository
    static ArrayList<String> repo_list = new ArrayList<String>(); // git dir
    static boolean output, help, all = false, lec = false;
    static String input_path = null;
    static String file_all, file_selected;

    // cil option not yet added
    public static void main(String[] args) throws IOException, ParseException, GitAPIException {
        //cli options
        Options options = createOptions();

        String filename = "", filename_lec = "";
        File file, file_lec;
        if (parseOptions(options, args)) {
            if (help) {
                print_help();
                System.exit(0);
            }
            if (lec) {
                filename_lec = file_selected;
                file_lec = new File(filename_lec);
                if (!file_lec.isAbsolute()) {
                    filename_lec = file_lec.getAbsolutePath();
                }
            }
            if (all) {
                filename = file_all;
                file = new File(filename);
                if (!file.isAbsolute()) {
                    filename = file.getAbsolutePath();
                }
            }
        }

        // file path
        // filename = args[0];
        // File file = new File(filename);
        // String filename_lcs = args[1];
        // File file_lcs = new File(filename_lcs);
        // if(!file.isAbsolute())
        // {
        //     filename = file.getAbsolutePath();
        // }
        // if (!file_lcs.isAbsolute())
        // {
        //     filename_lcs = file_lcs.getAbsolutePath();
        // }

        System.out.println("all:" + all + " lec:" + lec); // DEBUG
        if (all) {
            try {
                extract(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Cloning, get commit sha
            GitFunctions.clone_repo_jgit(repo_url, repo_list);
            GitFunctions.all_commit(repo_list, repo_name);
            Gumtree.runGumtreeForAll(repo_name, repo_list);
        }
        if (lec) {
            try {
                extract_lec(filename_lec);
            } catch (Exception e) {
                System.out.println("Error: extracting lcs git repository");
            }
        }

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

        System.out.println("Counted repo: " + count + "\n");
    }

    public static void extract_lec(String file_name) throws FileNotFoundException, IOException
    {
        System.out.println("======== Extracting for Given input ============");
        File file = new File(file_name);

        String java_file = "", commit = "", lcs_url = "", lec_name = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                //Format : <file_name(java)> <commit> <git_name> <git_url>
                String[] result = line.split(" ");
                java_file = result[0];
                commit = result[1];
                lec_name = result[2];
                lcs_url = result[3];
            }
        }
        
        try {
            String repo_git = GitFunctions.clone_designated_lcs(lcs_url, lec_name);
            Gumtree.get_changed_file_lec(repo_git, lec_name, commit, java_file);
            Gumtree.runGumtreeForLEC(lec_name, repo_git, commit, java_file);

        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }
    
    public static void print_help() {
        System.out.println("====== HELP ======\n");
        System.out.println("List of Commands");
        System.out.println("------------------");
        System.out.println("-h, -help : listing all the commands with the description of functionality of commands");
        System.out.println("-o, -output : makes one txt file that contains all the changes in cloned repositories");
        System.out.println("-a, -all : extracts all the repositories changes");
        System.out.println("-l, -lec : gets vector for lcs algorithm");
    }

    private static Options createOptions(){
        Options options = new Options();

        options.addOption(Option.builder("o").longOpt("output").desc("makes the output file containing all the changes of all cloned repository").build());
        options.addOption(Option.builder("h").longOpt("help").desc("Help").build());
        options.addOption(Option.builder("a").longOpt("all").desc("All extract").build());
        options.addOption("l", true, "lec");


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
                if (cmd.hasOption("a")) {
                    all = true;
                    file_all = cmd.getOptionValue("a");
                }
                if (cmd.hasOption("l")) {
                    lec = true;
                    file_selected = cmd.getOptionValue("l");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}