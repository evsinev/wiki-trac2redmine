package wiki_trac2redmine;

import org.junit.Test;

import java.io.*;

/**
 * Get files from dir and creates wiki pages on redmine
 */
public class CreateWikiPagesTest {

    @Test
    public void test() throws IOException {
        IRedmineService redmineService = new RedmineServiceImpl(System.getProperty("REDMINE_PROJECT_URL"));

        File dir = new File(System.getProperty("PAGES_DIR"));
        for (File file : dir.listFiles()) {
            if(file.isFile() && file.getName().endsWith(".txt")) {
                String text = readFile(file);
                String name = file.getName().replace(".txt", "");
                redmineService.createPage(name, text);
            }
        }
    }

    private String readFile(File aFile) throws IOException, UnsupportedEncodingException {
        LineNumberReader in = new LineNumberReader(new InputStreamReader(new FileInputStream(aFile), "utf-8"));
        try {
            String line;
            StringBuilder sb = new StringBuilder();
            while ( (line=in.readLine()) !=null ) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } finally {
            in.close();
        }
    }
}
