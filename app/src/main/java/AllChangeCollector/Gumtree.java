package AllChangeCollector;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.annotations.NonNull;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffConfig;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.FollowFilter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;


public class Gumtree {
    // runs gumtree for all(just only runs individual function accumulately)
    public static void runGumtreeForAll(ArrayList<String> repo_name) {
        //makes arraylist to run 
        for (String curr_repo : repo_name) {
            ArrayList<PrepareGumtree> commits = new ArrayList<PrepareGumtree>();
            // commits.repo_name = curr_repo;
            // Repository repo = curr_repo;
            // RevWalk walk = new RevWalk(repo);


            // for()
        }
    }

    // 
    public static void runGumtreeForIndividual(String repo_name)
    {

    }
    



    /*
    needs to fix : has to receive repository name -> currently git directory
    role : finding changed files between two commits, in this case, current commit and one before

    */
    public static void get_changed_file(String repo_git, String repo_name, String newCommit) throws IOException, GitAPIException {
        /*
        directing which directory to save diff entry log - has problem
        
        String git_dir = System.getProperty("user.dir");
        System.out.println("Writing at " + git_dir); // DEBUG
        File file = new File(git_dir,  "diff.txt");
        BufferedReader f_reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        
        String line = "";
        while ((line = f_reader.readLine()) != null) {
            line = "old: " + entry.getOldPath() +
                    ", new: " + entry.getNewPath() +
                    ", entry: " + entry;
            }
        writer.close();
        f_reader.close();
        */
        System.out.println("===== Starting Diff of the tree ======"); // DEBUG
        System.out.println(repo_name + " " + newCommit); // DEBUG

        //setting output directory
        String git_dir = System.getProperty("user.dir") + "/data/" + repo_name;
        System.out.println("Writing at " + git_dir); // DEBUG
        File file = new File(git_dir, repo_name + "_diff.txt");
        FileOutputStream fos = new FileOutputStream(file, true);
        PrintStream ps = new PrintStream(fos, true);
        System.setOut(ps);

        try (Repository repository = new FileRepository(repo_git)) {

            ObjectId oldHead = repository.resolve(newCommit + "^^{tree}");
            ObjectId head = repository.resolve(newCommit + "^{tree}"); // current

            // System.out.println("Printing diff between tree: " + oldHead + " and " + head);

            if (head == null || oldHead == null) {
                // For exception (when it comes to first commit)
            } else {
                // prepare the two iterators to compute the diff between
                try (ObjectReader reader = repository.newObjectReader()) {
                    CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
                    oldTreeIter.reset(reader, oldHead);
                    CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
                    newTreeIter.reset(reader, head);

                    // finally get the list of changed files
                    try (Git git = new Git(repository)) {
                        List<DiffEntry> diffs = git.diff()
                                .setNewTree(newTreeIter)
                                .setOldTree(oldTreeIter)
                                .call();
                        for (DiffEntry entry : diffs) {
                            System.out.println(newCommit + " " + entry.getNewPath()); // needs confirmation
                        }
                    }
                }
            }
        }
        System.setOut(System.out);
        System.out.println("Done");
    }
    


    /*
    executes 'git diff' for two commit
    
    requirements
    repo -> current working repository
    old commit -> one commit before current commit
    new commit -> current commit
    path -> 
    
    works to do
    1. needs to save the log as file
    2. not applied to main function
    */
    private static void runDiff(Repository repo, String oldCommit, String newCommit, String path) throws IOException, GitAPIException {
        DiffEntry diff = diffFile(repo,
                oldCommit,
                newCommit,
                path);

        // Display the diff
        System.out.println("Showing diff of " + path);
        try (DiffFormatter formatter = new DiffFormatter(System.out)) {
            formatter.setRepository(repo);
            //noinspection ConstantConditions
            formatter.format(diff);
        }
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(repository.resolve(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }

    private static @NonNull DiffEntry diffFile(Repository repo, String oldCommit,
                       String newCommit, String path) throws IOException, GitAPIException {
        Config config = new Config();
        config.setBoolean("diff", null, "renames", true);
        DiffConfig diffConfig = config.get(DiffConfig.KEY);
        try (Git git = new Git(repo)) {
            List<DiffEntry> diffList = git.diff().
                setOldTree(prepareTreeParser(repo, oldCommit)).
                setNewTree(prepareTreeParser(repo, newCommit)).
                setPathFilter(FollowFilter.create(path, diffConfig)).
                call();
            if (diffList.size() == 0)
                return null;
            if (diffList.size() > 1)
                throw new RuntimeException("invalid diff");
            return diffList.get(0);
        }
    }
}
