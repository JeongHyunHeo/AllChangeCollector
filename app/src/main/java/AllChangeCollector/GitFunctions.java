package AllChangeCollector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;


public class GitFunctions {
    // running git clone using Runtime
    public static void clone_repo(ArrayList<String> repo_url) throws IOException, GitAPIException
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
            System.out.println("Cloning from " + curr_url);
            /*
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
            */
        }
        
        System.out.println("Cloning Completed\n\n");
    }

    // Cloning Git repo using JGit
    public static void clone_repo_jgit(ArrayList<String> repo_url, ArrayList<String> repo_list) throws IOException, GitAPIException{

        for(String curr : repo_url)
        {
            // make directory
            int start = curr.lastIndexOf("/");
            int end = curr.lastIndexOf(".");
            String name = curr.substring(start, end);
            
            File curr_directory = new File(System.getProperty("user.dir") + "/data" + name);

            if (!curr_directory.exists())
            {
                curr_directory.mkdir();
            }

            // cloning repository
            System.out.println("Cloning from " + curr);
            try(Git result = Git.cloneRepository()
            .setURI(curr)
            .setDirectory(curr_directory)
            .setProgressMonitor(new TextProgressMonitor()) // SimpleProgressMonitor -> TextProgressMonitor
            .call()){
                System.out.println("Having repository: " + result.getRepository().getDirectory());
                String output = result.getRepository().getDirectory().toString();
                repo_list.add(output);
            }
        }
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

    // 
    public static void crawl_commit_id_jgit(ArrayList<String> repo_name)
    {
        System.out.println("======          Starting Task : Commit ID Collecting            ======");
        
        for(String curr_repo : repo_name)
        {
            String work_dir = System.getProperty("user.dir") + "/data/" + curr_repo;
        }
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
    
}
