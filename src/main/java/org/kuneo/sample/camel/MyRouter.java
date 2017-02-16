package org.kuneo.sample.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.kuneo.sample.Person;
import org.springframework.stereotype.Component;

@Component
public class MyRouter extends RouteBuilder {

  /**
   * 以下のようなサンプルです。
   *  1. プロパティ[csvFromDir]で指定したディレクトリからcsvファイルを取得する
   *  2. トランザクション開始
   *  3. CSVファイルをJavaオブジェクトに変換する
   *  4. プロセッサーで何らかの処理を行う
   *  5. ExchangeのBodyをログ出力する
   */
  @Override
  public void configure() throws Exception {
    from("file://{{csvFromDir}}").id("route-file-from")
        .transacted()
        .unmarshal(new BindyCsvDataFormat(Person.class))
        .process(new MyProcessor()).id("processor")
        .to("mock:result");
  }
}
