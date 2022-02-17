package AllChangeCollector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.input.BoundedReader;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffConfig;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;


public class Gumtree {
    public static void runGumtreeForAll(ArrayList<String> repo_name) {
        for (String curr_repo : repo_name) {

        }
    }

    public static DiffEntry runDiff(Repository repo, String curr_commit, String old_commit)
    {
        DiffEntry diffEntry = null;

        Config config = new Config();
        config.setBoolean("diff", null, "name", true);
        DiffConfig diffConfig = config.get(DiffConfig.KEY);

        try(Git git = new Git(repo))
        {
            List<DiffEntry> diffList;
        }
        
        return diffEntry;
    }

    public static void all_commit(ArrayList<String> repo_list, ArrayList<String> repo_name) throws IOException, GitAPIException
    {
        for(String name : repo_list)
        {
            try (Repository repo = new FileRepository(name)) {
                // get a list of all known heads, tags, remotes, ...
                Collection<Ref> allRefs = repo.getAllRefs().values();

                /*
                String working_dir = System.getProperty("user.dir") + "/data/" +
                File file = new File(working_dir, "commitID.txt");
                BoundedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    line = line.substring(1, line.length() - 1);
                    writer.write(line + "\n");
                }
                writer.close();
                reader.close();
                */
                
                // a RevWalk allows to walk over commits based on some filtering that is defined
                try (RevWalk revWalk = new RevWalk(repo)) {
                    for (Ref ref : allRefs) {
                        revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
                    }
                    // System.out.println("Walking all commits starting with " + allRefs.size() + " refs: " + allRefs);
                    // int count = 0;
                    for (RevCommit commit : revWalk) {
                        System.out.print(commit + " ");
                        System.out.print(commit.getName()); // sha
                        // count++;
                    }
                    // System.out.println("Had " + count + " commits");
                }
            }
        }
            /*
            for (String name : repo_name) {
                Repository repo = null;
                try {
                    repo = new FileRepository(System.getProperty("user.dir") + "/" + name + "/.git");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Git git = new Git(repo);

                Iterable<RevCommit> log = git.log().call();
                
                
                for(RevCommit commit : log)
                {
                    String main_branch = 
                }
                
            }
            */
    }
}
