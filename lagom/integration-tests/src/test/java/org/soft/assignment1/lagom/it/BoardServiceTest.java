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

	private static TestServer server;

	@BeforeClass
	public static void setup() {
		server = ServiceTest.startServer(defaultSetup().withCluster(false));
	}
	
	@AfterClass
	public static void tearDown() {
		if (server != null) {
			server.stop();
			server = null;
		}
	}
	
	@Test
	public void testCreate() throws Exception {
		BoardService service = server.client(BoardService.class);
		service.create().invoke(new Board(null, "test", BoardStatus.CREATED)).toCompletableFuture().get(5,
				TimeUnit.SECONDS);
	}
	
}
