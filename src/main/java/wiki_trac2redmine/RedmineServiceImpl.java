package wiki_trac2redmine;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import static wiki_trac2redmine.WebDriverUtils.waitForId;

/**
 */
public class RedmineServiceImpl implements IRedmineService {

    public RedmineServiceImpl(String aRedmineUrl) {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("network.http.phishy-userpass-length", 255);

        theDriver = new FirefoxDriver(profile);
        theRedmineUrl = aRedmineUrl;
        loginToRedmine(aRedmineUrl);
    }

    private void loginToRedmine(String aRedmineUrl) {
        StringTokenizer st = new StringTokenizer(aRedmineUrl, "/:@");
        st.nextToken(); // skip http
        String username = st.nextToken();
        String password = st.nextToken();

        theDriver.get(aRedmineUrl);

        waitForId(theDriver, "footer");

        theDriver.findElement(By.id("username")).sendKeys(username);
        theDriver.findElement(By.id("password")).sendKeys(password);
        theDriver.findElement(By.name("login")).click();

        waitForId(theDriver, "footer");

    }

    @Override
    public void createPage(String aPageName, String aTextSource) {
        theDriver.get(theRedmineUrl + "/wiki/"+aPageName);

        waitForId(theDriver, "footer");


        theDriver.findElement(By.id("auto_preview_box")).click();

        theDriver.findElement(By.id("content_text")).sendKeys(aTextSource);

        theDriver.findElement(By.name("commit")).click();

        waitForId(theDriver, "footer");

    }

    @Override
    public String convertFromTracWikiText(String aTracWikiText) throws IOException, InterruptedException {
        // 1. creates file
        PrintWriter out = new PrintWriter("target/trac.wiki");
        try {
            out.write(aTracWikiText);
        } finally {
            out.close();
        }

        // 2. do convert
        Process process = Runtime.getRuntime().exec(new String[]{"ruby", "trac2redmine.rb"});
        int result = process.waitFor();
        if(result!=0) throw new IllegalStateException("Error converting from trac wiki");

        // 3. read file
        LineNumberReader in = new LineNumberReader(new FileReader("target/redmine.wiki"));
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while( (line=in.readLine()) !=null ) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } finally {
            in.close();
        }
    }


    @Override
    public boolean isPageExists(String aPageName) {
        theDriver.get(theRedmineUrl + "/wiki/"+aPageName);

        waitForId(theDriver, "footer");

        return !WebDriverUtils.isElementExist(theDriver, "content_text");
    }

    private final String theRedmineUrl;
    private final WebDriver theDriver ;
}
