package AllChangeCollector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
    public static void runGumtreeForAll(ArrayList<String> repo_name, ArrayList<String> repo_git) throws IOException {

        System.out.println("====  Starting Task : Gumtree ====");
        for (int i = 0; i < repo_name.size(); i++) {
            runGumtreeForIndividual(repo_name.get(i), repo_git.get(i));
        }
    }

    // 
    public static void runGumtreeForIndividual(String repo_name, String repo_git) throws IOException
    {
        System.out.println("====> Task : " + repo_name); // DEBUG
        Repository repo = new FileRepository(repo_git);
        RevWalk walk = new RevWalk(repo);


        String dir = System.getProperty("user.dir") + "/data/" + repo_name;
        String file = dir + "/diff.txt";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        while((line = reader.readLine()) != null)
        {
            String[] token = line.split("\\s+");

        }

        reader.close();
    }
    



    /*
    role : finding changed files between two commits, in this case, current commit and one before
    */
    public static void get_changed_file(String repo_git, String repo_name, String newCommit, String oldCommit)
            throws IOException, GitAPIException {
        System.out.println("===== Starting Diff of the tree ======"); // DEBUG
        System.out.println(repo_name + " " + newCommit); // DEBUG

        //setting output directory
        String git_dir = System.getProperty("user.dir") + "/data/" + repo_name;
        File file = new File(git_dir, "diff.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        String line = "";

        try (Repository repository = new FileRepository(repo_git)) {

            ObjectId oldHead = repository.resolve(oldCommit + "^{tree}");
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
                            System.out.println(newCommit + " " + entry.getNewPath() + " " + oldCommit + " " + entry.getOldPath()); // needs confirmation
                            line = newCommit + " " + oldCommit + " " + entry.getNewPath() + " " + entry.getOldPath(); // 
                            writer.write(line + "\n");
                        }
                    }
                }
            }
        }

        writer.close();
    }
    
    /*
    would it be needed?
    */
    private void gitDiff(ArrayList<String> repo_git, ArrayList<String> repo_name) {
        
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
