package wiki_trac2redmine;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: esinev
 * Date: 4/26/11
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class RedmineServiceImplTest {


    @Test
    public void test() {

        RedmineServiceImpl service = new RedmineServiceImpl(System.getProperty("REDMINE_PROJECT_URL"));
        System.out.println("service.isPageExists(\"\") = " + service.isPageExists("Start_hudson"));

    }

    @Test
    public void testConvert() throws IOException, InterruptedException {
        RedmineServiceImpl service = new RedmineServiceImpl(System.getProperty("REDMINE_PROJECT_URL"));
        String text = service.convertFromTracWikiText("= hello =");
        System.out.println("text = " + text);

    }
}
