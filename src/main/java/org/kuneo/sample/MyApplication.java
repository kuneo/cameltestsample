package org.kuneo.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>[概 要] アプリケーションを起動するクラスです。</p>
 * <p>[詳 細] </p>
 * <p>[備 考] </p>
 * <p>[環 境] JRE 8</p>
 *
 * @author t.nemoto.x
 */
@SpringBootApplication
public class MyApplication {

  /**
   * <p>[概 要] アプリケーションを起動する。</p>
   * <p>[詳 細] </p>
   * <p>[備 考] </p>
   *
   * @param args 引数
   */
  public static void main(String[] args) {
    SpringApplication.run(MyApplication.class, args);
  }
}
