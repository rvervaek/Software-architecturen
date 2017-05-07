package org.soft.assignment1.lagom.it;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.soft.assignment1.lagom.board.api.Board;
import org.soft.assignment1.lagom.board.api.BoardService;
import org.soft.assignment1.lagom.board.api.BoardStatus;

import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import com.lightbend.lagom.javadsl.testkit.ServiceTest.TestServer;

public class BoardServiceTest {

	private static final String SERVICE_LOCATOR_URI = "http://localhost:8000";

//	private static LagomClientFactory clientFactory;
//	private static BoardService boardService;
	
	private static TestServer server;

	@BeforeClass
	public static void setup() {
//		clientFactory = LagomClientFactory.create("integration-test", BoardServiceTest.class.getClassLoader());
		// One of the clients can use the service locator, the other can use the
		// service gateway, to test them both.

//		boardService = clientFactory.createDevClient(BoardService.class, URI.create(SERVICE_LOCATOR_URI));
		
		
		server = ServiceTest.startServer(defaultSetup().withCluster(false));
	}
	
	@AfterClass
	public static void tearDown() {
		if (server != null) {
			server.stop();
			server = null;
		}
//		if (clientFactory != null) {
//			clientFactory.close();
//		}
	}
	
	@Test
	public void testCreate() throws Exception {	
//		withServer(defaultSetup(), server -> {		
			BoardService service = server.client(BoardService.class);		
			service.create().invoke(new Board(null, "test", BoardStatus.CREATED)).toCompletableFuture().get(5, TimeUnit.SECONDS);		
//		});	
	}
	
}
