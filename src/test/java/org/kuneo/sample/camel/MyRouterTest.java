package org.kuneo.sample.camel;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuneo.sample.Person;
import org.kuneo.sample.service.MyService;
import org.kuneo.sample.test.DummyTransactionManager;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class MyRouterTest extends CamelTestSupport {

  private String currentPath = new File(".").getAbsoluteFile().getParent();

  // Mock化するサービス
  @Mock
  MyService service = mock(MyService.class);

  // サービスの引数を確認するためのキャプチャ
  @Captor
  ArgumentCaptor<Person> argCap;

  // モックをインジェクトするプロセッサ
  @InjectMocks
  private MyProcessor processor = new MyProcessor();

  // 試験するルート
  @Override
  protected RouteBuilder createRouteBuilder() {
    return new MyRouter();
  }

  // トランザクションを用いる場合はダミーのトランザクションマネージャを注入
  @Override
  protected CamelContext createCamelContext() throws Exception {
    SimpleRegistry registry = new SimpleRegistry();
    registry.put("transactionManager", new DummyTransactionManager());
    CamelContext context = new DefaultCamelContext(registry);
    return context;
  }

  // プロパティファイルの設定値を試験用に上書き
  @Override
  protected Properties useOverridePropertiesWithPropertiesComponent() {
    Properties extra = new Properties();
    extra.put("csvFromDir", String.join("/", currentPath, "src", "test", "sandbox"));
    return extra;
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    // モックを有効化
    MockitoAnnotations.initMocks(this);
    // プロセッサーをモックをインジェクションしたオブジェクトに差し替える
    context.getRouteDefinition("route-file-from").adviceWith(context, new AdviceWithRouteBuilder() {
      @Override
      public void configure() throws Exception {
        weaveById("processor").replace().process(processor);
      }
    });
  }

  // 試験に用いたファイルが残っていれば削除する
  @After
  public void tearDown() throws Exception {
    super.tearDown();
    FileSystem fileSystem = FileSystems.getDefault();
    Files.deleteIfExists(fileSystem.getPath(String.join("/", currentPath, "src", "test", "sandbox", "person.csv")));
  }

  @Test
  public void 取得したファイルがCSVに変換できていることを確認する() throws InterruptedException, IOException {
    // MyService#helloを呼び出したとき、必ず"test"が返却されるように設定
    when(service.hello(argCap.capture())).thenReturn("test");

    // ExchangeのBodyに対する期待値の設定
    getMockEndpoint("mock:result").expectedBodiesReceived("test");

    // 試験開始
    // ルートの開始条件となるCSVファイルをコピーする
    csvCopy();
    // ExchangeのBodyに流れた値がモックの返却値であることを確認
    getMockEndpoint("mock:result").assertIsSatisfied();
    // サービスの引数を確認
    assertThat(argCap.getValue(), is(new Person(25, "tanaka")));
  }

  // ファイルをコピーする
  private void csvCopy() throws IOException {
    FileSystem fileSystem = FileSystems.getDefault();
    Path source = fileSystem.getPath(String.join("/", currentPath, "src", "test", "resources", "person.csv"));
    Path out = fileSystem.getPath(String.join("/", currentPath, "src", "test", "sandbox", "person.csv"));
    Files.copy(source, out);
  }
}
