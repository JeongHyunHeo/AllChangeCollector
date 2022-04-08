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
    static boolean output, help;
    static String input_path = null;

    public static void main(String[] args) throws IOException, ParseException, GitAPIException {
        //cli options
        Options options = createOptions();

        if(parseOptions(options,args)){
            if(help)
            {
                print_help();
                System.exit(0);
            }   
        }

        // file path
        String filename = args[0];
        File file = new File(filename);
        if(!file.isAbsolute())
        {
            filename = file.getAbsolutePath();
        }

        try {
            extract(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        // Cloning, get commit sha 
        GitFunctions.clone_repo_jgit(repo_url, repo_list);

        GitFunctions.all_commit(repo_list, repo_name);

        Gumtree.runGumtreeForAll(repo_name, repo_list);

        for (String name : repo_name)
        {
            Vectorize.extract_vector(name);
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

        System.out.println("Counted repo: " + count + "\n\n");
    }
    
    public static void print_help() {
        System.out.println("====== HELP ======\n");
        System.out.println("List of Commands");
        System.out.println("------------------");
        System.out.println("-h, -help : listing all the commands with the description of functionality of commands");
        System.out.println("-o, -output : makes one txt file that contains all the changes in cloned repositories");
    }

    private static Options createOptions(){
        Options options = new Options();

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
