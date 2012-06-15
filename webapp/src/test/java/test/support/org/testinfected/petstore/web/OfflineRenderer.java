package test.support.org.testinfected.petstore.web;

import org.testinfected.hamcrest.ExceptionImposter;
import org.testinfected.petstore.Renderer;
import org.w3c.dom.Element;

import java.util.HashMap;

import static test.support.com.pyxis.petstore.views.HTMLDocument.toElement;

public class OfflineRenderer {

    public static OfflineRenderer render(String template) {
        return new OfflineRenderer(template);
    }

    private final String template;

    private Renderer renderer;
    private Object context;
    private String content;

    private OfflineRenderer(String template) {
        this.template = template;
        this.context = new HashMap<String, String>();
    }

    public OfflineRenderer with(Renderer renderer) {
        this.renderer = renderer;
        return this;
    }

    public OfflineRenderer using(Object context) {
        this.context = context;
        return this;
    }

    public String asString() {
        render();
        return content;
    }

    public Element asDom() {
        return toElement(asString());
    }

    private void render() {
        try {
            content = renderer.render(template, context);
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}
