package test.unit.org.testinfected.molecule.decoration;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.testinfected.molecule.decoration.ContentProcessor;
import org.testinfected.molecule.decoration.HtmlDocumentProcessor;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

public class HtmlDocumentProcessorTest {

    ContentProcessor processor = new HtmlDocumentProcessor();

    String page =
            "<html>\n" +
            "<head>\n" +
            "  <title>\n" +
            "  Page Title  \n" +
            "  </title>   \n" +
            "  <meta name=\"description\" content=\"Description\"/>\n" +
            "  <meta name=\"author\" content=\"Author\"/>\n" +
            "</head>\n" +
            "<body>\n" +
            "Content of the body\n" +
            "</body>\n" +
            "</html>\n";

    @Test public void
    extractsHtmlHeadMinusTitle() {
        assertThat("html chunks", processor.process(page), hasChunk("head",
                        "  <meta name=\"description\" content=\"Description\"/>\n" +
                        "  <meta name=\"author\" content=\"Author\"/>"));
    }

    @Test public void
    extractsHtmlBody() {
        assertThat("html chunks", processor.process(page), hasChunk("body", "Content of the body"));
    }

    @Test public void
    extractsHtmlTitle() {
        assertThat("html chunks", processor.process(page), hasChunk("title", "Page Title"));
    }

    @Test public void
    extractsMetaData() {
        assertThat("html chunks", processor.process(page), hasChunk("meta[description]", "Description"));
        assertThat("html chunks", processor.process(page), hasChunk("meta[author]", "Author"));
    }

    private Matcher<Map<? extends String, ? extends Object>> hasChunk(final String key, final String value) {
        return Matchers.<String, Object>hasEntry(key, value);
    }
}
