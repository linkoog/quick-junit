package junit.extensions.eclipse.quick;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


/**
 * This test class shows JavaElements specification.
 */
public class JavaElementsTest {
	
	private IMethodMockBuilder methodBuilder;
	private ITypeMockBuilder typeBuilder;
	
	@Before
	public void before() throws Exception {
		methodBuilder = new IMethodMockBuilder();
		typeBuilder = new ITypeMockBuilder();
	}

	@Test
	public void recognition_test_method_by_annotation() throws Exception {
		
		IMethod element = methodBuilder.normal_test3().addTestAnnotation().build();	
		assertThat(JavaElements.isTestMethod(element),is(true));
		
	}
	
	@Test
	public void recognition_test_method_by_method_name() throws Exception {
		
		IMethod element = methodBuilder.normal_test3().setMethodName("test_mode").build();	
		assertThat(JavaElements.isTestMethod(element),is(true));
		
	}

	@Test
	public void test_method_should_has_no_args() throws Exception {
		
		IMethod element = methodBuilder.setNumberOfParameters(1).build();
		assertThat(JavaElements.isTestMethod(element),is(false));
		
	}
	
	@Test
	public void test_method_should_public_method() throws Exception {
		
		IMethod element = methodBuilder.returnVoid().build(); // package private
		assertThat(JavaElements.isTestMethod(element),is(false));
		
		element = methodBuilder.normal_test3().setPrivate().build();
		assertThat(JavaElements.isTestMethod(element),is(false));
		
		element = methodBuilder.normal_test3().setProtected().build();
		assertThat(JavaElements.isTestMethod(element),is(false));

	}

	@Test
	public void test_method_should_not_static_method() throws Exception {
		
		IMethod element = methodBuilder.normal_test3().setStatic().build();
		assertThat(JavaElements.isTestMethod(element),is(false));
		
		element = methodBuilder.normal_test3().setPrivate().setStatic().build();
		assertThat(JavaElements.isTestMethod(element),is(false));
		
		element = methodBuilder.normal_test3().setPrivate().setStatic().build();
		assertThat(JavaElements.isTestMethod(element),is(false));
	}

	
	@Test
	public void it_should_return_null_when_empty_class() throws Exception {
		IType element = typeBuilder.normal().build();
		assertThat(JavaElements.getTestMethodOrClass(element),is(nullValue()));
	}
	
	@Ignore
	@Test
	public void parameterized_test_should_return_class() throws Exception {
		
		ITypeHierarchy typeHierarchy = mock(ITypeHierarchy.class);
		when(typeHierarchy.getAllInterfaces()).thenReturn(new IType[]{});

		IType element = mock(IType.class);
		when(element.isClass()).thenReturn(true);
		when(element.getFlags()).thenReturn(Flags.AccPublic);
		when(element.newSupertypeHierarchy(null)).thenReturn(typeHierarchy);
		when(element.getMethods()).thenReturn(new IMethod[]{});
		
		assertThat(JavaElements.getTestMethodOrClass(element) == element,is(true));
		
	}

}
