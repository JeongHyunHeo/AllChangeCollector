package AllChangeCollector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;


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

    // INCOMPLETE(reason: not needed)
    public static void crawl_commit_id_jgit(ArrayList<String> repo_name)
    {
        System.out.println("======          Starting Task : Commit ID Collecting            ======");

        for (String curr_repo : repo_name) {
            String work_dir = System.getProperty("user.dir") + "/data/" + curr_repo;
        }
    }
    
    /*
     * instead, crawl_commit_id() in GitFunction.java was used
     * because both brings same result.
     */
    public static void all_commit(ArrayList<String> repo_list, ArrayList<String> repo_name)
            throws IOException, GitAPIException {
        System.out.println("===== Starting Task : Commit ID Collecting(all commit) ======");
        int i = 0; // to keep the connection between repo_list and repo_name
        for (String name : repo_list) {
            try (Repository repo = new FileRepository(name)) {
                // get a list of all known heads, tags, remotes, ...
                Collection<Ref> allRefs = repo.getAllRefs().values();

                // a RevWalk allows to walk over commits based on some filtering that is defined
                try (RevWalk revWalk = new RevWalk(repo)) {
                    for (Ref ref : allRefs) {
                        revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
                    }
                    // System.out.println("Walking all commits starting with " + allRefs.size() + "
                    // refs: " + allRefs);
                    int count = 0;
                    ArrayList<String> commit_sha_list = new ArrayList<String>();
                    for (RevCommit commit : revWalk) {

                        String beforeCommit = commit.getName();
                        if (commit_sha_list.size() == 0) {
                            commit_sha_list.add(beforeCommit);
                            count++;
                        } else {
                            Gumtree.get_changed_file(repo_list.get(i), repo_name.get(i), commit_sha_list.get(count - 1),
                                    beforeCommit); // (repo_git_directory, repo_name, current commit, old commit)
                            commit_sha_list.add(beforeCommit);
                            count++;
                        }

                    }
                }
                i++;
            }
            // reader.close();
            // writer.close();
            System.out.println();
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
