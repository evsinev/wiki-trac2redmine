package wiki_trac2redmine;

import java.io.IOException;

/**
 *
 */
public interface IRedmineService {

    boolean isPageExists(String aPageName);

    void createPage(String aPageName, String aTextSource);

    String convertFromTracWikiText(String aTracWikiText) throws IOException, InterruptedException;

}
