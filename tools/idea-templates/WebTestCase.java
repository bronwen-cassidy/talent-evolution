#parse("File Header.java")
package ${PACKAGE_NAME};

import net.sourceforge.jwebunit.TestContext;
import net.sourceforge.jwebunit.WebTestCase;
#if (${TESTED_CLASS_PACKAGE_NAME} != "" && ${TESTED_CLASS_NAME} != "")
import ${TESTED_CLASS_PACKAGE_NAME}.${TESTED_CLASS_NAME};
#end

#parse("Class Header.java")
public class ${NAME} extends WebTestCase {

   public ${NAME}(String name) { 
        super(name);
        TestContext testContext = getTestContext();
        testContext.setBaseUrl("http://localhost:7001/talent-studio");

        beginAt("/login.htm");
        setFormElement("username", "zynapsys");
        setFormElement("password", "zynapsys");
        submit();
        checkCheckbox("acceptedPolicy");
        submit();
        testContext.setAuthorization("zynapsys", "zynapsys");
    }    
}