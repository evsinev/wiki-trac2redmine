package wiki_trac2redmine;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public interface ITracService {

    /**
     * Lists wiki pages
     * @return list of pages names
     */
    List<String> getWikiPages();

    String getWikiText(String aPageName) throws IOException;

}
