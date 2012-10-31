package test.unit.org.testinfected.petstore.controllers;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;

@RunWith(JMock.class)
public class CreateProductTest {

    Mockery context = new JUnit4Mockery();
    ProcurementRequestHandler requestHandler = context.mock(ProcurementRequestHandler.class);
    CreateProduct createProduct = new CreateProduct(requestHandler);

    Controller.Request request = context.mock(Controller.Request.class);
    Controller.Response response = context.mock(Controller.Response.class);
    final int CREATED = 201;

    @Test public void
    makesProductProcurementRequestAndRespondsWithCreated() throws Exception {
        setRequestParametersTo("LAB-1234", "Labrador", "Friendly Dog", "labrador.jpg");

        context.checking(new Expectations() {{
            oneOf(requestHandler).addProductToCatalog(with("LAB-1234"), with("Labrador"), with("Friendly Dog"), with("labrador.jpg"));
        }});

        context.checking(new Expectations() {{
            oneOf(response).renderHead(CREATED);
        }});

        createProduct.process(request, response);
    }

    private void setRequestParametersTo(final String number, final String name, final String description, final String photoFileName) {
        context.checking(new Expectations() {{
            allowing(request).getParameter("number"); will(returnValue(number));
            allowing(request).getParameter("name"); will(returnValue(name));
            allowing(request).getParameter("description"); will(returnValue(description));
            allowing(request).getParameter("photo"); will(returnValue(photoFileName));
        }});
    }
}