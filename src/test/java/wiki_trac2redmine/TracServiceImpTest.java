package wiki_trac2redmine;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: esinev
 * Date: 4/26/11
 * Time: 1:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TracServiceImpTest {

    @Test
    public void testList() {
        TracServiceImpl service = new TracServiceImpl(System.getProperty("TRAC_URL"));
        List<String> pages = service.getWikiPages();
        System.out.println("pages = " + pages);
    }


    @Test
    public void getWikiText() throws IOException {
        TracServiceImpl service = new TracServiceImpl(System.getProperty("TRAC_URL"));
        String text = service.getWikiText("adduser");
        System.out.println("text = " + text);
    }


}
