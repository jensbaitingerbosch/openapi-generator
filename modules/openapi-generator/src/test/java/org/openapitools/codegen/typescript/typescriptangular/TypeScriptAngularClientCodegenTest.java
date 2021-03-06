package org.openapitools.codegen.typescript.typescriptangular;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.TestUtils;
import org.openapitools.codegen.languages.TypeScriptAngularClientCodegen;
import org.testng.Assert;
import org.testng.annotations.Test;


public class TypeScriptAngularClientCodegenTest {
    @Test
    public void testModelFileSuffix() {
        TypeScriptAngularClientCodegen codegen = new TypeScriptAngularClientCodegen();
        codegen.additionalProperties().put("modelFileSuffix", "MySuffix");
        codegen.additionalProperties().put("modelSuffix", "MySuffix");
        codegen.processOpts();

        Assert.assertEquals("testNameMySuffix", codegen.toModelFilename("testName"));
    }

    @Test
    public void testOperationIdParser() {
        OpenAPI openAPI = TestUtils.createOpenAPI();
        Operation operation1 = new Operation().operationId("123_test_@#$%_special_tags").responses(new ApiResponses().addApiResponse("201", new ApiResponse().description("OK")));
        openAPI.path("another-fake/dummy/", new PathItem().get(operation1));
        final TypeScriptAngularClientCodegen codegen = new TypeScriptAngularClientCodegen();
        codegen.setOpenAPI(openAPI);

        CodegenOperation co1 = codegen.fromOperation("/another-fake/dummy/", "get", operation1, null);
        org.testng.Assert.assertEquals(co1.operationId, "_123testSpecialTags");

    }

    @Test
    public void testSnapshotVersion() {
        TypeScriptAngularClientCodegen codegen = new TypeScriptAngularClientCodegen();
        codegen.additionalProperties().put("npmName", "@openapi/typescript-angular-petstore");
        codegen.additionalProperties().put("snapshot", true);
        codegen.additionalProperties().put("npmVersion", "1.0.0-SNAPSHOT");
        codegen.processOpts();

        Assert.assertTrue(codegen.getNpmVersion().matches("^1.0.0-SNAPSHOT.[0-9]{12}$"));

        codegen = new TypeScriptAngularClientCodegen();
        codegen.additionalProperties().put("npmName", "@openapi/typescript-angular-petstore");
        codegen.additionalProperties().put("snapshot", true);
        codegen.additionalProperties().put("npmVersion", "3.0.0-M1");
        codegen.processOpts();

        Assert.assertTrue(codegen.getNpmVersion().matches("^3.0.0-M1-SNAPSHOT.[0-9]{12}$"));

    }

    @Test
    public void testWithoutSnapshotVersion() {
        TypeScriptAngularClientCodegen codegen = new TypeScriptAngularClientCodegen();
        codegen.additionalProperties().put("npmName", "@openapi/typescript-angular-petstore");
        codegen.additionalProperties().put("snapshot", false);
        codegen.additionalProperties().put("npmVersion", "1.0.0-SNAPSHOT");
        codegen.processOpts();

        Assert.assertTrue(codegen.getNpmVersion().matches("^1.0.0-SNAPSHOT$"));

        codegen = new TypeScriptAngularClientCodegen();
        codegen.additionalProperties().put("npmName", "@openapi/typescript-angular-petstore");
        codegen.additionalProperties().put("snapshot", false);
        codegen.additionalProperties().put("npmVersion", "3.0.0-M1");
        codegen.processOpts();

        Assert.assertTrue(codegen.getNpmVersion().matches("^3.0.0-M1$"));

    }

    @Test
    public void testRemovePrefixSuffix() {
        TypeScriptAngularClientCodegen codegen = new TypeScriptAngularClientCodegen();

        // simple noop test
        Assert.assertEquals("TestName", codegen.removeModelPrefixSuffix("TestName"));

        codegen.setModelNamePrefix("abc");
        codegen.setModelNameSuffix("def");
        codegen.additionalProperties().put("modelSuffix", "Ghi");
        codegen.processOpts();

        Assert.assertEquals("TestName", codegen.removeModelPrefixSuffix("TestName"));
        Assert.assertEquals("TestName", codegen.removeModelPrefixSuffix("TestNameGhi"));
        Assert.assertEquals("TestNameghi", codegen.removeModelPrefixSuffix("TestNameghi"));
        Assert.assertEquals("abcTestName", codegen.removeModelPrefixSuffix("abcTestName"));
        Assert.assertEquals("TestName", codegen.removeModelPrefixSuffix("AbcTestName"));
        Assert.assertEquals("AbcTestName", codegen.removeModelPrefixSuffix("AbcAbcTestName"));
        Assert.assertEquals("TestName", codegen.removeModelPrefixSuffix("TestNameDef"));
        Assert.assertEquals("TestNamedef", codegen.removeModelPrefixSuffix("TestNamedef"));
        Assert.assertEquals("TestNamedefghi", codegen.removeModelPrefixSuffix("TestNamedefghi"));
        Assert.assertEquals("TestNameDefghi", codegen.removeModelPrefixSuffix("TestNameDefghi"));
        Assert.assertEquals("TestName", codegen.removeModelPrefixSuffix("TestNameDefGhi"));
    }

}
