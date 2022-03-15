package AllChangeCollector;

public class PrepareGumtree{
    public String repo_name;
    public String commit_id;
    public String current_commit_sha;
    public String current_commit_id;
    public String current_commit_path;
    public String before_current_sha;
    public String before_current_id;
    public String before_current_path;

    public PrepareGumtree(String repo_name, String current_commit_id, String current_commit_sha, String current_commit_path, String before_current_id, String before_current_sha, String before_current_path)
    {
        this.repo_name = repo_name;
        this.current_commit_id = current_commit_id;
        this.current_commit_sha = current_commit_sha;
        this.current_commit_path = current_commit_path;
        this.before_current_id = before_current_id;
        this.before_current_sha = before_current_sha;
        this.before_current_path = before_current_path;
    }
}