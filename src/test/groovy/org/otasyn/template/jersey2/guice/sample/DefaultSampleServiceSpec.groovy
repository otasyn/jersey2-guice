package org.otasyn.template.jersey2.guice.sample

import com.google.inject.AbstractModule
import com.google.inject.name.Named
import com.google.inject.name.Names
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

import jakarta.inject.Inject
import jakarta.inject.Provider

import spock.guice.UseModules
import spock.lang.Specification
import spock.lang.Unroll

import org.otasyn.template.jersey2.guice.MockGuiceModule
import org.otasyn.template.jersey2.guice.TypesafeConfig

class DefaultSampleServiceSpecGuiceModule extends AbstractModule {

  def configMessage = 'whatever'
  def config = ConfigFactory.parseString("""
    public.sample {
      message = "${configMessage}"
    }
  """)

  @Override
  protected void configure() {
    bind(Config).annotatedWith(TypesafeConfig).toInstance(config)
    bind(String).annotatedWith(Names.named("message")).toInstance(configMessage)
  }

}

@Unroll
@UseModules([MockGuiceModule,DefaultSampleServiceSpecGuiceModule])
class DefaultSampleServiceSpec extends Specification {

  @Inject AnotherService anotherService
  @Inject @Named("message") String configMessage

  @Inject Provider<DefaultSampleService> toTestProvider
  def toTest

  def setup() {
    toTest = toTestProvider.get()
  }

  def 'build a message using AnotherService' () {
    when:
      def message = toTest.getMessage()

    then:
      message == 'Hi blah'
      1 * anotherService.provideName() >> 'blah'
  }

  def 'build a message using typesafe config' () {
    when:
      def message = toTest.getTypesafeConfigMessage()

    then:
      message == configMessage
  }

}
